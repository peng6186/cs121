import java.util.*;

public class BoardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
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
    // check out the condition
    if (pw.sta_in != sw || tw.sta_at != sw) {
      throw new RuntimeException("passenger and train should be at station");
    }
    // do the action
//    pw.sta_in = null;
    pw.t_on = tw;

  }
}
