package jrails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Model {
    private static final String database_name = "my_real_db.txt";

    private static final String sep = "###";
    public static int globalId;
    public int id;
    public Class c;

    private static Map<Integer, String> map_id_to_entry_str = new HashMap<>();
    private static Map<Class, List<Integer>> map_class_to_idlist = new HashMap<>();
    private static File database = new File(database_name);
    private static boolean isDbLoaded = false;

    public Model() {
        this.id = 0;
        this.c = this.getClass();

        if (!database.exists()) {
            try{
                database.createNewFile();

            } catch (Exception e) {
                throw new RuntimeException("Create db file failed");
            }

        } else {
            if (!isDbLoaded) {
                loadDbfromDisk(database);
                isDbLoaded = true;
            }
        }

    }

    private static void loadDbfromDisk(File database) {
        Scanner sn;
        try {
            sn = new Scanner(database);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not Found");
        }
        while (sn.hasNext()) {
            String line = sn.nextLine();
            String[] entry_str = line.split("###");  // using ### to separate diff fields of each entry
            int entry_id = Integer.parseInt(entry_str[0]);
            Class entry_class;
            try {
                entry_class = Class.forName(entry_str[1]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found");
            }
            map_id_to_entry_str.put(entry_id, line);
            List<Integer> l = map_class_to_idlist.get(entry_class);
            if (l == null) {
                l = new ArrayList<>();
            }
            l.add(entry_id);
            map_class_to_idlist.put(entry_class, l);
        }

        /* set up globalId */
        globalId = map_id_to_entry_str.size() + 1;
        /* close the scanner */
        sn.close();
    }

    public void save() {
        /* this is an instance of the current model */
        if (id == 0) {
            id = globalId;
            globalId++;
        } else { // check if non-zero id item is already in the db. if not, raise an exp
            if (Model.find(this.getClass(), id) == null) {
                throw new RuntimeException("Non-zero id item is ont in the db");
            }
        }

        /* convert this object to str representation */
        String entry_str = writeObjToStr(this, id, c);  // FIXME!!
        /* update two maps */
        map_id_to_entry_str.put(this.id, entry_str);
        List<Integer> l = map_class_to_idlist.get(this.getClass());
        if (l == null) {
            l = new ArrayList<>();
        }
        if ( !l.contains(id) ) {
            l.add(id);
        }

        map_class_to_idlist.put(this.getClass(), l);
        /* write db to the disk */
        write_db_to_disk(database);
    }

    public int id() { // 0kay
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        /* the following code is dealing with directly call this func */
        if (!database.exists()) {
            try{
                database.createNewFile();

            } catch (Exception e) {
                throw new RuntimeException("Create db file failed");
            }

        } else {
            if (!isDbLoaded) {
                loadDbfromDisk(database);
                isDbLoaded = true;
            }
        }

        if (map_class_to_idlist == null) {
            return null;
        }
        List<Integer> l = map_class_to_idlist.get(c);
        if (l == null) {
            return null;
        }
        if (!l.contains(Integer.valueOf(id))) {
            return null;
        }

        String entry_str = map_id_to_entry_str.get(id);
        /* convert an entry from str to obj representation */
        T res = convertStrToObj(c, entry_str); // Fixme!!
        return res;
    }

    private static <T> T convertStrToObj(Class<T> model_class, String entry_str) {
        /* declare an instance */
        T t;
        try {
            t = model_class.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No Such Method Exp");
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation Exp");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal Access Exp");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation Target Exp");
        }

        /* assign the instance fields */
        String[] str_arr = entry_str.split(sep);
        // assign its annotated @column fields
        for (int i = 2; i < str_arr.length; ++i) {
            String field_str = str_arr[i];
            assignObjField(t, field_str);
        }
        // assign its id and c fields
        ((Model)t).id = Integer.parseInt(str_arr[0]);
        ((Model)t).c = model_class;

        return t;
    }

    private static <T> void assignObjField(T t, String field_str) {
        String[] str_arr = field_str.split("=");
        String key = str_arr[0];
        String value = str_arr[1];

        for (Field f : t.getClass().getDeclaredFields()) {
            if (f.getName().equals(key)) {
                try {
                    if (f.getType().equals(String.class)) { // string type
                        if (value.equals("null")) { // if String is null
                            f.set(t, null);
                        } else {
                            f.set(t, value);
                        }
                    } else if (f.getType().equals(int.class)) { // int type
                        f.setInt(t, Integer.parseInt(value));
                        break;
                    } else if (f.getType().equals(boolean.class)) { // boolean type
                        f.setBoolean(t, Boolean.parseBoolean(value));
                        break;
                    } else {
                        throw new RuntimeException("@Column filed must be String, int, or boolean type");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Illegal Access Ex");
                }
            }
        }
    }
    public static <T> List<T> all(Class<T> c) { // okay
        /* the following code is dealing with directly call this func */
        if (!database.exists()) {
            try{
                database.createNewFile();

            } catch (Exception e) {
                throw new RuntimeException("Create db file failed");
            }

        } else {
            if (!isDbLoaded) {
                loadDbfromDisk(database);
                isDbLoaded = true;
            }
        }

        // Returns a List<element type>
        List<T> res = new ArrayList<>();
        List<Integer> id_list = map_class_to_idlist.get(c);
        if (id_list == null) {
            return res;
        }
        for (Integer id: id_list) {
            res.add(find(c, id));
        }
        return res;
    }

    public void destroy() { // fishy at line 89. remove (int) vs remove (Object)
        int entry_id = id();
        if (entry_id == 0) {
            throw new RuntimeException("This item has not been saved to db previously");
        }
        map_id_to_entry_str.remove(entry_id);
        map_class_to_idlist.get(this.getClass()).remove(Integer.valueOf(entry_id)); // pay attention to this. idx or object
        write_db_to_disk(database);
    }
    public static void reset () { // okay
        /* the following code is dealing with directly call this func */
        if (!database.exists()) {
            try{
                database.createNewFile();

            } catch (Exception e) {
                throw new RuntimeException("Create db file failed");
            }

        } else {
            if (!isDbLoaded) {
                loadDbfromDisk(database);
                isDbLoaded = true;
            }
        }

        map_id_to_entry_str.clear();
        map_class_to_idlist.clear();
        write_db_to_disk(database);
        globalId = 1;
    }


    /* helper method */
    private static void write_db_to_disk (File db){
        PrintWriter output;
        try {
            output = new PrintWriter(db);

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not Found");
        }
        for (String entry_str : map_id_to_entry_str.values()) {
            output.println(entry_str);
        }

        output.close();
    }

    private String writeObjToStr (Object o, int id, Class c) {
        String res = "";
        /* write id to str*/
        res += id + sep;
        /* write model class name to str*/
        res += c.getName();
        /* write fields to str */
        for (Field f : c.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class)) {
                String f_name = f.getName();
                Class f_type = f.getType();
                Object value;
                try {
                    value = f.get(o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Illegal Acess Exp");
                }

//                    System.out.println("field_name: " + f_name+ ", file_type: " + f_type);
                if (!fieldTypeValid(f_type)) {
                    throw new RuntimeException("@Column fields are only String, int, or boolan types");
                }
                res += sep + f_name + "=" + value;
            }
        }
        return res;
    }

    private boolean fieldTypeValid (Class f_type){
        return f_type.equals(String.class) || f_type.equals(int.class) || f_type.equals(boolean.class);
    }
}
