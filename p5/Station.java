import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Station extends Entity {
  private Station(String name) { super(name); }

  private static HashMap<String, Station> record = new HashMap<>();
  public static Station make(String name) {
    // Change this method!
    if ( record.get(name) != null ) {
      return record.get(name);
    }
    Station new_station = new Station(name);
    record.put(name, new_station);
    return new_station;
  }
}
