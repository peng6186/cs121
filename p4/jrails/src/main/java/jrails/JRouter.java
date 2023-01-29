package jrails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JRouter {

    private Map<String, String> map = new HashMap<>();


    public void addRoute(String verb, String path, Class clazz, String method) { // okay
        // Implement me!
        String key = verb + path;
        String val = clazz.getName() + "#" + method;
        map.put(key, val);
    }

    // Returns "clazz#method" corresponding to verb+URN
    // Null if no such route
    public String getRoute(String verb, String path) { // okay
        String key = verb + path;
        String res = map.get(key);
        return res != null ? res: null;

    }

    // Call the appropriate controller method and
    // return the result
    public Html route(String verb, String path, Map<String, String> params) { // okay
        System.out.println("Entering route()");
        String key = verb + path;
        String str_method = map.get(key);
        System.out.println("str_method: " + str_method);
        if (str_method == null) {
            throw new UnsupportedOperationException("No such route mapping (verb, path) -> class#method");
        }

        String[] str_arr = str_method.split("#");
        try {
            Class c = Class.forName(str_arr[0]);
            String method_name = str_arr[1];
            Method m = c.getMethod(method_name, Map.class);
            System.out.println("className: " + str_arr[0]);
            System.out.println("methodName: " + method_name);
            return (Html) m.invoke(null, params);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal Access Exp for method call");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation Target Exp when invoking method");
        }
    }
}
