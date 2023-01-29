public class



Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static assertObject assertThat(Object o) {

        return new assertObject(o);
    }
    static assertString assertThat(String s) {

        return new assertString(s);
    }
    static assertBool assertThat(boolean b) { // okay

        return new assertBool(b);
    }
    static assertInt assertThat(int i) { // okay

        return new assertInt(i);
    }
}