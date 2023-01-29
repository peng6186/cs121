public class assertObject {

    private Object obj;
    public assertObject(Object o ) { obj = o; }

    public assertObject isNotNull(){
        if (obj == null) {
            throw new RuntimeException("assertObject: is null");
        }
        return this;
    }

    public assertObject isNull(){
        if (obj != null) {
            throw new RuntimeException("assertObject: is not null");
        }
        return this;
    }

    public assertObject isEqualTo(Object o2) {
        if (!obj.equals(o2)) {
            throw new RuntimeException("assertObject: is not equalTo(Object o2)");
        }
        return this;
    }

    public assertObject isNotEqualTo(Object o2) {
        if (obj.equals(o2)) {
            throw new RuntimeException("assertObject: is equalTo(Object o2)");
        }
        return this;
    }

    public assertObject isInstanceOf(Class c) {
        if (obj.getClass() != c) {
            throw new RuntimeException("assertObject: is not an instance of Class c");
        }
        return this;
    }
}
