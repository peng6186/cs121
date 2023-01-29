import java.util.*;

public class DeboardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    MBTA.PassengerWrapper pw = mbta.pasToWrapper.get(p);
    MBTA.TrainWrapper tw = mbta.trainToWrapper.get(t);
    MBTA.StationWrapper sw = mbta.staToWrapper.get(s);
    if (pw == null || tw == null || sw == null) {
      throw new RuntimeException("one of entities doen't exist");
    }
    // check the pre-condition
    if (tw.sta_at != sw || pw.t_on != tw ) {
      throw new RuntimeException("train should be at the station, and p should be on the train");
    }
    // check deboard on the next sta on the journey
    int curStaIdx = pw.journey.indexOf(pw.sta_in);
    int nxtStaIdx = pw.journey.indexOf(sw);
    if (nxtStaIdx - curStaIdx != 1) {
      throw new RuntimeException("Passenger should deboard on the nxt station of the journey");
    }

    // do the action
    pw.t_on = null;
    pw.sta_in= sw;

  }
}
