import java.util.*;

public class MoveEvent implements Event {
  public final Train t; public final Station s1, s2;
  public MoveEvent(Train t, Station s1, Station s2) {
    this.t = t; this.s1 = s1; this.s2 = s2;
  }
  public boolean equals(Object o) {
    if (o instanceof MoveEvent e) {
      return t.equals(e.t) && s1.equals(e.s1) && s2.equals(e.s2);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(t, s1, s2);
  }
  public String toString() {
    return "Train " + t + " moves from " + s1 + " to " + s2;
  }
  public List<String> toStringList() {
    return List.of(t.toString(), s1.toString(), s2.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    MBTA.TrainWrapper tw = mbta.trainToWrapper.get(t);
    MBTA.StationWrapper sw1 = mbta.staToWrapper.get(s1);
    MBTA.StationWrapper sw2 = mbta.staToWrapper.get(s2);
//    System.out.println("train: " + tw.t + "; s1: " + sw1.s + "; s2: " + sw2.s.toString());

    if (tw == null || sw1 == null || sw2 == null) {
      throw new RuntimeException("one of entities doen't exist");
    }
    // check out if the train is in s1, and s2 is empty now
    if (tw.sta_at != sw1) {
      throw new RuntimeException("The train should be in station s1");
    }
    if (sw2.t_hold != null) {
      throw new RuntimeException(" s2 should be empty");
    }

    // s2 is the nxt station
    int sw1Idx = tw.route.indexOf(sw1);
    int sw2Idx = tw.route.indexOf(sw2);
    if (tw.direction == 1) { // train: left to right
      if (sw2Idx - sw1Idx != 1) {
        throw new RuntimeException("sw2idx should be one more than sw1idx (left -> right)");
      }
    } else if (tw.direction == -1) {// train: right to left
      if (sw2Idx - sw1Idx != -1) {
        throw new RuntimeException("sw2idx should be one less than sw1idx (right -> left)");
      }
    }
    // update the direction
    if ( sw2Idx == 0 || sw2Idx == tw.route.size() - 1 ) {
      tw.direction *= -1;
    }

    // make the movement
    tw.sta_at = sw2;
    sw2.t_hold = tw;
    sw1.t_hold = null;
  }
}
