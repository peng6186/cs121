import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MBTA {

  /* fields */
  public Map<Train, TrainWrapper> trainToWrapper= new HashMap<>();
  public Map<Station, StationWrapper> staToWrapper = new HashMap<>();
  public Map<Passenger, PassengerWrapper> pasToWrapper = new HashMap<>();
  private List<TrainWrapper> train_threads = new ArrayList<>();
  private List<PassengerWrapper> passenger_threads = new ArrayList<>();

  public List <Thread> train_workers  = new ArrayList<Thread>();
  public List <Thread> pas_workers = new ArrayList<>();
  private Log log;



  public class StationWrapper {
    public Station s;
    public TrainWrapper t_hold;

    public StationWrapper (Station sta) {
      s = sta;
      staToWrapper.put(s, this);
    }
    Lock s_t_lock = new ReentrantLock();
    Condition s_t_lock_con = s_t_lock.newCondition();

    Lock s_p_board_lock = new ReentrantLock();
    Condition s_p_board_lock_con = s_p_board_lock.newCondition();

    Lock s_p_deboard_lock = new ReentrantLock();
    Condition s_p_deboard_lock_con = s_p_deboard_lock.newCondition();

  }

  public class TrainWrapper implements Runnable{ // FIXEME! run()
    public Train t;
    public StationWrapper sta_at;
    public List<StationWrapper> route = new ArrayList<>();

    public int direction = 1; // 1 means left-to-right, -1 mreans right-to-left
    public TrainWrapper(String train_name, List<String> stations) {
      // initialize Train
      t = Train.make(train_name);

      // initialize Station List
      for (String station: stations) {
        Station s = Station.make(station);
        StationWrapper sw = staToWrapper.get(s);
        if (sw == null) {sw = new StationWrapper(s); }
        route.add(sw);
      }

      // Set up the initial status of the train and the station and require the lock
      sta_at = route.get(0); // train is in the starting station
//      route.get(0).s_t_lock.lock(); // acquire the lock for starting station

      route.get(0).t_hold = this; // starting station holds this train

      trainToWrapper.put(t, this);
    }

    @Override
    public void run(){
      sta_at.s_t_lock.lock();
      int currentStationIdx = 0;
      int nextStationIdx = 1;
      int flip = 1;
      final int step = 1;
      final int terminalStationIdx = route.size() - 1;

      while (true) {
//        System.out.println("Train enter while true loop");
        if (isWorkDone()) {
          route.get(currentStationIdx).s_t_lock_con.signalAll();
          route.get(currentStationIdx).s_t_lock.unlock();
          return;
        }
//        System.out.println("Before train run MoveNxtStation()");
        boolean isSucceed = moveToNextStation(currentStationIdx, nextStationIdx);
        if (!isSucceed) { continue; }
//        System.out.println("After train run MoveNxtStation()");
        if (nextStationIdx == 0 || nextStationIdx == terminalStationIdx) {
          flip = -1 * flip;
        }
        currentStationIdx = nextStationIdx;
        nextStationIdx += step * flip;
      }
    }
    // helper func
    // return boolean: if the movement is successful
    private boolean moveToNextStation(int curStationIdx, int nxtStationIdx) {
      /* wait for 0.5 sec*/
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      /* ask for the nxt sta loc */
      StationWrapper curStaWrapper = route.get(curStationIdx);
      StationWrapper nxtStaWrapper = route.get(nxtStationIdx);
      nxtStaWrapper.s_t_lock.lock();

      while (nxtStaWrapper.t_hold != null) {
        if (isWorkDone()) {
          return false;
        } else {
          try {
            nxtStaWrapper.s_t_lock_con.await();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }

      }


      /* move the train to next sta */
      sta_at = nxtStaWrapper;
      nxtStaWrapper.t_hold = this;
      curStaWrapper.t_hold = null;

      log.train_moves(t, curStaWrapper.s, nxtStaWrapper.s); // log the event

      /* release the lock for curStation */
      curStaWrapper.s_t_lock_con.signalAll();
      curStaWrapper.s_t_lock.unlock();
      /* notify passengers to deboard*/
      nxtStaWrapper.s_p_deboard_lock.lock();
      nxtStaWrapper.s_p_deboard_lock_con.signalAll();
      nxtStaWrapper.s_p_deboard_lock.unlock();
      /* notify passengers to board*/
      nxtStaWrapper.s_p_board_lock.lock();
      nxtStaWrapper.s_p_board_lock_con.signalAll();
      nxtStaWrapper.s_p_board_lock.unlock();

      return true; // the movement succeed
    }

  }

  public class PassengerWrapper implements Runnable { // FIXME! run()
    Passenger p;
    /* status varibles */
    public StationWrapper sta_in;
    public TrainWrapper t_on;
    List<StationWrapper> journey = new ArrayList<>();
    public PassengerWrapper(String passenger_name, List<String> stations) {
      // initialize Passenger
      p = Passenger.make(passenger_name);

      // initialize Station List
      for (String station: stations) {
        Station s = Station.make(station);
        StationWrapper sw = staToWrapper.get(s);
        if (sw == null) {sw = new StationWrapper(s); }
        journey.add(sw);
      }

      // Set up the initial status of the passenger and the station
      sta_in = journey.get(0); // passenger is in the starting station

      pasToWrapper.put(p, this);
    }

    @Override
    public void run() {
      for (int idx = 0; idx < journey.size() - 1; ++idx) {
        StationWrapper start = journey.get(idx);
        StationWrapper end = journey.get(idx + 1);
//        System.out.println("Before pass run Board()");
        Board(start, end);
//        System.out.println("After pass run Board()");
        log.passenger_boards(p, t_on.t, start.s); // log the board event
        Train train = t_on.t;
//        System.out.println("Before pass run DeBoard()");
        DeBoard(end);
//        System.out.println("After pass run DeBoard()");
        log.passenger_deboards(p, train, end.s);
      }
      synchronized (pas_workers) {
        pas_workers.remove(Thread.currentThread());
      }


    }

    // helper func
    private void Board(StationWrapper start, StationWrapper end) {
      /* check whether train is arriving */
      start.s_p_board_lock.lock();
      while (start.t_hold == null || !start.t_hold.route.contains(end)) {
        try {
          start.s_p_board_lock_con.await();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      start.s_p_board_lock_con.signalAll();
      start.s_p_board_lock.unlock();
      /* leave the cur station */
//      sta_in = null;
      /* board the train */
      t_on = start.t_hold;
    }

    private void DeBoard(StationWrapper end) {
//      System.out.println("In Deboard()");
      /* check whether arriving at the dst*/
      end.s_p_deboard_lock.lock();
      while (end != t_on.sta_at) {
        try {
//          System.out.println("Not arriving end station yet");
          end.s_p_deboard_lock_con.await();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      end.s_p_deboard_lock_con.signalAll();
      end.s_p_deboard_lock.unlock();
      /* leave the train */
      t_on = null;
      /* land the station */
      sta_in = end;

    }
  }

  private class SimConfig {
    public HashMap<String, List<String>> lines = new HashMap<>();
    public HashMap<String, List<String>> trips = new HashMap<>();
  }


  /* methods */
  // Creates an initially empty simulation
  public MBTA() { }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) { // okay

    TrainWrapper train_t = new TrainWrapper(name, stations);
    train_threads.add(train_t);
    train_workers.add(new Thread(train_t));
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) { // okay
    PassengerWrapper passenger_t = new PassengerWrapper(name, stations);
    passenger_threads.add(passenger_t);
    pas_workers.add(new Thread(passenger_t));
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() { // okay
    // trains are at their staring stations
    for (TrainWrapper t_th: train_threads) {
      if (t_th.sta_at != t_th.route.get(0)) {
        throw new RuntimeException("Trains should be at their starting stations");
      }
    }

    // passengers are at their station stations
    for (PassengerWrapper p_th: passenger_threads) {
      if (p_th.sta_in != p_th.journey.get(0)) {
        throw  new RuntimeException("Passengers should be at their starting stations");
      }
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() { // okay
    for (PassengerWrapper p_th: passenger_threads) {
      if (p_th.sta_in!= p_th.journey.get(p_th.journey.size() - 1)) {
        throw new RuntimeException("Passengers should reach to their destinations");
      }
    }
  }

  // reset to an empty simulation
  public void reset() { // okay
    trainToWrapper.clear();
    staToWrapper.clear();
    pasToWrapper.clear();
    train_threads.clear();
    passenger_threads.clear();

    train_workers.clear();
    pas_workers.clear();

  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {

    Path file = Path.of(filename);
    String s;
    try {
      s = Files.readString(file);
    } catch (IOException e) {
      throw  new RuntimeException("IO exception");
    }

    Gson gson = new Gson();
    SimConfig config = gson.fromJson(s, SimConfig.class);

    for (String line: config.lines.keySet()) {
      addLine(line, config.lines.get(line));
    }

    for (String journey: config.trips.keySet()) {
      addJourney(journey, config.trips.get(journey));
    }

  }

  /* Helper method */
  public List<TrainWrapper> getTrainThreads() {
    return train_threads;
  }
  public List<PassengerWrapper> getPassengerThreads() {
    return passenger_threads; }
  public void setLog(Log log) {this.log = log;}

  private boolean isWorkDone() {
    if (pas_workers.size() > 0) {
      return false;
    }
    return true;
  }
}
