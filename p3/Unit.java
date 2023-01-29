/**
 * 
 * @File Unit.java
 * @Author Pengcheng Xu(pengcheng.xu@tufts.edu)
 * This is the implementation for Proj3 of CS121 Software Engineering
 * The purpose of this project is to use Reflection to imitate Java's Unit testing
 * 
 */
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class Unit {
    private static class MethodSorter implements Comparator<Method> {
        public int compare(Method m1, Method m2) {
            return m1.getName().compareTo(m2.getName());
        }
    }

    private final int MAX_ITERATIONS = 100;
    private static class Pair implements Comparable<Pair>{
        public Pair(String name, Method m) {
            methodName = name;
            method = m;
        }
        private String methodName;
        private Method method;

        @Override
        public int compareTo(Pair o) {
            return methodName.compareTo(o.methodName);
        }
    }

    public static Map<String, Throwable> testClass(String name) {
	    Map<String, Throwable> res = new HashMap<>();

        /* get a runtime Class obj for this class "name" */
        Class testing_class;
        try {
            testing_class = Class.forName(name);
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("Class could not be found");
        }

        Constructor cons;
        Object o;
        try {
            cons = testing_class.getConstructor();
            o = cons.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No nullary constructor");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("No nullary constructor");
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation Exp");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("InvocationTarget Exp");
        }

        /* declare lists we're gonna use */
        List<Pair> tests = new LinkedList<>();
        List<Pair> befores = new LinkedList<>();
        List<Pair> afters = new LinkedList<>();
        List<Pair> beforeClasses = new LinkedList<>();
        List<Pair> afterClasses = new LinkedList<>();

        /* collect diff lists */
        for (Method m: testing_class.getDeclaredMethods()) {
            Annotation[] annotations = m.getDeclaredAnnotations();
            if (annotations.length > 1) {
                throw new RuntimeException("Every method could only have one annotation");
            }

            if(m.isAnnotationPresent(Test.class)) { // @Test
                tests.add(new Pair(m.getName(), m));
            } else if (m.isAnnotationPresent(Before.class)) { // @Before
                befores.add(new Pair(m.getName(), m));
            } else if (m.isAnnotationPresent(After.class)) { // @After
                afters.add(new Pair(m.getName(), m));
            } else if (m.isAnnotationPresent(BeforeClass.class)) { // @BeforeClass FIXME!! check static
                if ( Modifier.isStatic(m.getModifiers()) ) {
                    beforeClasses.add(new Pair(m.getName(), m));
                } else {
                    throw new RuntimeException("@BeforeClass should appear on static method");
                }

            } else if (m.isAnnotationPresent(AfterClass.class)) { // @AfterClass FIXME!! check static
                if ( Modifier.isStatic(m.getModifiers()) ) {
                    afterClasses.add(new Pair(m.getName(), m));
                } else {
                    throw new RuntimeException("@AfterClass should appear on static method");
                }
            } else { // invalid annotation
                throw new RuntimeException("Invalid annotation (must among @Test, @Before, @BeforeClass, @After, @AfterClass)");
            }
        }

        /* sort tests */
        Collections.sort(tests);
        Collections.sort(befores);
        Collections.sort(afters);
        Collections.sort(beforeClasses);
        Collections.sort(afterClasses);

        // run @BeforeClass
        try {

            for (Pair p : beforeClasses) {
                p.method.invoke(null);
            }
        } catch (Exception e) {
            throw new RuntimeException("BeforeClass exp");
        }

        // run @Test
        for (Pair p: tests) {
            try {
                // run @Before
                try {
                    for (Pair p_b: befores) {
                        p_b.method.invoke(o);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Exp thrown in @Before");
                }

                p.method.invoke(o);
                res.put(p.methodName, null);
            } catch(InvocationTargetException e) { // catch the testcase running Exp
                res.put(p.methodName, e.getCause());
            } catch(IllegalAccessException e) {
                throw new RuntimeException("IllegalAccess Exp");
            }

            // run @After
            try {
                for (Pair p_a: afters) {
                    p_a.method.invoke(o);
                }
            } catch (Exception e) {
                throw new RuntimeException("Exp thrown in @After");
            }

        }

        // run @AfterClass
        try {
            for (Pair p_ac : afterClasses) {
                p_ac.method.invoke(null);
            }
        } catch (Exception e) {
            throw new RuntimeException("AfterClass exp");
        }

        return res;

    }

    public static Map<String, Object[]> quickCheckClass(String name) {
        Map<String, Object[]> res = new HashMap<>();

        /* get a runtime Class obj for this class "name" */
        Class testing_class;
        try {
            testing_class = Class.forName(name);
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("Class could not be found");
        }

        Constructor cons;
        Object o;
        try {
            cons = testing_class.getConstructor();
            o = cons.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No nullary constructor");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("No nullary constructor");
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation Exp");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("InvocationTarget Exp");
        }


        /* collect all @Property methods */
        List<Method> Property_method_list = new LinkedList<>();
        for (Method m: testing_class.getDeclaredMethods()) {
            Annotation[] annotations = m.getDeclaredAnnotations();
            if (annotations.length > 1) {
                throw new RuntimeException("Every method could only have one annotation");
            }
            if(m.isAnnotationPresent(Property.class)) {
                Property_method_list.add(m);
            }
        }

        /* sort Property methods */
        Collections.sort(Property_method_list, new MethodSorter());

        /* invoke all @Property methods */
        for (Method m: Property_method_list) {
            try {
                runPropertyMethod(m, res, o, testing_class);
            } catch (testFailedException e) {

            } catch (Exception e) {
                throw new RuntimeException("Error: Something Wrong Happened");
            }
        }

        return res;
    }

    private static void runPropertyMethod(Method m, Map<String, Object[]> res, Object obj_being_running_on, Class testing_class) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Parameter[] params = m.getParameters();
        List<List<Object>> arg_list = new LinkedList<>();

        /* build up list of Arg set */
        for (Parameter p: params) {
            AnnotatedType at = p.getAnnotatedType();
            Annotation an = at.getDeclaredAnnotations()[0]; // every param only has one annotation
            if (an.annotationType() == IntRange.class) { // okay
                int min = ((IntRange)an).min();
                int max = ((IntRange)an).max();
                List<Object> l = getIntegerArgList(min, max);
                arg_list.add(l);
            } else if (an.annotationType() == StringSet.class) { // okay
                String []strs = ((StringSet)an).strings();
                List<Object> l = getStringArgList(strs);
                arg_list.add(l);
            } else if (an.annotationType() == ForAll.class) { // okay
                String methodName = ((ForAll)an).name();
                Method method = testing_class.getMethod(methodName);
                int invoke_times = ((ForAll)an).times();
                List<Object> l = getObjectArgList(method, invoke_times, obj_being_running_on);
                arg_list.add(l);
            } else if (an.annotationType() == ListLength.class) {
                int min = ((ListLength)an).min();
                int max = ((ListLength)an).max();
                List<Object> l = getListArgList(min, max, at, testing_class, obj_being_running_on);
                arg_list.add(l);
            } else {
                throw new RuntimeException("Invalid Annotation type for @Property");
            }
        }

        /* Run combination of args */
        runQuickCheckOnMethod(m, arg_list, obj_being_running_on, res);
        res.put(m.getName(), null);
    }


    static int num_iterations;
    private static void runQuickCheckOnMethod(Method m, List<List<Object>> arg_list, Object obj_being_running_on, Map<String, Object[]> res)  {
        num_iterations = 0;
        int arglist_len = arg_list.size();
        int idx = 0;
        Stack<Object> stack = new Stack<>();
        recursive_call(arglist_len, idx, arg_list, stack, m, obj_being_running_on, res);
    }
    private static void recursive_call( int length, int idx,  List<List<Object>> arg_list, Stack<Object> stack, Method m, Object obj_being_running_on, Map<String, Object[]> res) {
        if (idx < length ) {
            for ( Object o : arg_list.get(idx) ) {
                stack.push(o);
                ++idx;
                recursive_call(length, idx, arg_list, stack, m, obj_being_running_on, res);
                stack.pop();
                --idx;
            }

        } else {
            Object bool_res = true;
            try {
//                System.out.println("num_iterations = " + num_iterations);
                if (num_iterations >= 100) {
                    return;
                }
                ++num_iterations;
                bool_res = m.invoke(obj_being_running_on, stack.toArray());


            } catch(InvocationTargetException e) {
                res.put(m.getName(), stack.toArray());
                throw new testFailedException("first args comb caused exception thrown");

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Something Bad Happened: IllegalAccessException");
            }
            if (Boolean.FALSE.equals(bool_res)) { // invoking method returns false
                res.put(m.getName(), stack.toArray());
                throw new testFailedException("first args comb caused return false");
            }
//
        }
    }

    private static List<Object> getIntegerArgList(int min, int max) {
        List<Object> res = new LinkedList<>();
        for (int i = min; i <= max; ++i) {
            res.add(i);
        }
        return res;
    }

    private static List<Object> getStringArgList(String[] strs) {
        List<Object> res = new LinkedList<>();
        for (String str: strs){
            res.add(str);
        }
        return res;
    }

    private static List<Object> getObjectArgList(Method method, int invoke_times, Object obj_being_running_on) throws InvocationTargetException, IllegalAccessException {
        List<Object> res = new LinkedList<>();
        for (int i = 0; i< invoke_times; ++i) {
            res.add(method.invoke(obj_being_running_on));
        }
        return res;
    }

    private static List<Object> getListArgList(int listlen_min, int listlen_max, AnnotatedType at,Class testing_class, Object obj_being_running_on) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Object> res = new LinkedList<>();
        List<Object> list_to_be_run_permutatation = new LinkedList<>();
        if (at instanceof AnnotatedParameterizedType) {
            AnnotatedType nested_at = ((AnnotatedParameterizedType)at).getAnnotatedActualTypeArguments()[0];
            Annotation nested_an = nested_at.getDeclaredAnnotations()[0];
            if (nested_an.annotationType() == IntRange.class) {
                int min = ((IntRange)nested_an).min();
                int max = ((IntRange)nested_an).max();
                list_to_be_run_permutatation = getIntegerArgList(min, max);
            } else if (nested_an.annotationType() == StringSet.class) {
                String []strs = ((StringSet)nested_an).strings();
                list_to_be_run_permutatation = getStringArgList(strs);
            } else if (nested_an.annotationType() == ForAll.class) {
                String methodName = ((ForAll)nested_an).name();
                Method method = testing_class.getMethod(methodName);
                int invoke_times = ((ForAll)nested_an).times();
                list_to_be_run_permutatation = getObjectArgList(method, invoke_times, obj_being_running_on);
            } else if (nested_an.annotationType() == ListLength.class) {
                int nested_listlen_min = ((ListLength)nested_an).min();
                int nested_listlen_max = ((ListLength)nested_an).max();
                list_to_be_run_permutatation = getListArgList(nested_listlen_min, nested_listlen_max, nested_at, testing_class, obj_being_running_on);
            }

            /* do the selection from the list_to_be_run_permutatation */
            for (int len = listlen_min; len<= listlen_max; ++ len) {
                int idx = 0;
                Stack<Object> stack = new Stack<>();
                recursive_call_list( len, idx, list_to_be_run_permutatation, stack, res);
            }

            /* return the result */
            return res;

        } else {
            throw new RuntimeException("List must have an annotated generic type in it. i.e. List<Annotated K>");
        }

    }

    private static void recursive_call_list(int list_len, int idx, List<Object> list_to_be_run_permutatation, Stack<Object> stack, List<Object> res) {
        if (idx < list_len) {
            for (Object o: list_to_be_run_permutatation) {
                stack.push(o);
                ++idx;
                recursive_call_list(list_len, idx, list_to_be_run_permutatation, stack, res);
                stack.pop();
                --idx;
            }
        } else {
            res.add( Arrays.asList(stack.toArray()) );
        }

    }

}