import java.util.HashMap;

public class Passenger extends Entity {
  private Passenger(String name) { super(name); }


  private static HashMap<String, Passenger> record = new HashMap<>();
  public static Passenger make(String name) {
    // Change this method!
    if ( record.get(name) != null ) {
      return record.get(name);
    }
    Passenger new_passenger = new Passenger(name);
    record.put(name, new_passenger);
    return new_passenger;
  }
}
