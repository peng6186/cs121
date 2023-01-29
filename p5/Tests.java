import static org.junit.Assert.*;
import org.junit.*;

import java.util.List;

public class Tests {
  @Test
  public void testPass() {
    assertTrue("true should be true", true);
  }


  /* Train, Station, Passenger */
  @Test
  public void testTrainStationPassenger(){
    assertEquals("Train should be a singleton pattern", Train.make("red"), Train.make("red"));
    assertEquals("Station should be a singleton pattern", Station.make("Davis"), Station.make("Davis"));
    assertNotEquals("Station should be a singleton pattern", Station.make("Davis"), Station.make("davis"));
    assertEquals("Passenger should be a singleton pattern", Passenger.make("bob"), Passenger.make("bob"));
  }

  /* MBTA */
  @Test
  public void test_MBTA_reset() {
    MBTA mbta = new MBTA();
    List<String> stations = List.of("Davis", "Porter");
    mbta.addLine("red", stations);
    assertEquals("There should be only one line",1, mbta.getTrainThreads().size());
    mbta.reset();
    assertEquals("There should be empty line",0, mbta.getTrainThreads().size());
  }
  @Test
  public void test_verifier()
  {
    MBTA mbta = new MBTA();
    /* create a red_line train*/
    List<String> red_line = List.of("Davis", "Porter", "Harvard", "Kendall", "Park", "Downtown Crossing",
            "South Station", "Broadway", "Andrew", "JFK");
    mbta.addLine("red",red_line);

    /* create a passenger */
    List<String> bob_journey = List.of("Davis", "Harvard");
    mbta.addJourney("Bob",bob_journey);

    /* create a log*/
    Train red_train = Train.make("red");
    Passenger bob = Passenger.make("Bob");
    Station davis = Station.make("Davis");
    Station porter = Station.make("Porter");
    Station harvard = Station.make("Harvard");
    Log log = new Log();
    log.passenger_boards(bob,red_train,davis);
    log.train_moves(red_train, davis, porter);
    log.train_moves(red_train, porter, harvard);
    log.passenger_deboards(bob, red_train, harvard);

    /* verify the simulation against thje log*/
    Verify.verify(mbta,log);
  }

  @Test
  public void test_Movement()
  {
    MBTA mbta = new MBTA();
    /* create a red_line train*/
    List<String> red_line = List.of("Davis", "Porter", "Harvard", "Kendall", "Park", "Downtown Crossing",
            "South Station", "Broadway", "Andrew", "JFK");
    mbta.addLine("red",red_line);

    /* create a passenger */
    List<String> bob_journey = List.of("Davis", "Harvard", "Park");
    mbta.addJourney("Bob",bob_journey);

    /* create a log*/
    Train red_train = Train.make("red");
    Passenger bob = Passenger.make("Bob");
    Station davis = Station.make("Davis");
    Station porter = Station.make("Porter");
    Station harvard = Station.make("Harvard");
    Station kendall = Station.make("Kenall");
    Station park = Station.make("Park");
    Station jfk = Station.make("JFK");

    Log log = new Log();
    log.passenger_boards(bob,red_train,davis);
    log.train_moves(red_train, davis, porter);
    log.train_moves(red_train, porter, harvard);
    log.passenger_deboards(bob, red_train, jfk);
    log.train_moves(red_train,  harvard, kendall);
    log.train_moves(red_train,  kendall, park);

    /* verify the simulation against thje log*/
    Verify.verify(mbta, log);
  }

}
