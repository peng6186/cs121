public class assertString {

    private String s;
    public assertString(String str) {s = str;}

    public assertString isNotNull(){
        if (s == null) {
            throw new RuntimeException("assertString: is null");
        }
        return this;
    }

    public assertString isNull(){
        if (s != null) {
            throw new RuntimeException("assertString: is not null");
        }
        return this;
    }

    public assertString isEqualTo(Object o2) {
        if (!s.equals(o2)) {
            throw new RuntimeException("assertString: is not equalTo(Object o2)");
        }
        return this;
    }

    public assertString isNotEqualTo(Object o2) {
        if (s.equals(o2)) {
            throw new RuntimeException("assertString: is equalTo(Object o2)");
        }
        return this;
    }


    public assertString startsWith(String s2) {
        if (!s.startsWith(s2)) {
            throw new RuntimeException("assertString: string doesn't start with the given string s2");
        }
        return this;
    }

    public assertString isEmpty() {
        if (!s.isEmpty()) {
            throw new RuntimeException("assertString: string is not Empty");
        }
        return this;
    }

    public assertString contains(String s2) {
        if (!s.contains(s2)) {
            throw new RuntimeException("assertString: string doesn't contain the given string s2");
        }
        return this;
    }
}
