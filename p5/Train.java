import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Train extends Entity {
  private Train(String name) { super(name); }

  private static HashMap<String, Train> record = new HashMap<>();

  public static Train make(String name) {

    if ( record.get(name) != null ) {
      return record.get(name);
    }
    Train new_train = new Train(name);
    record.put(name, new_train);
    return new_train;
  }
}
