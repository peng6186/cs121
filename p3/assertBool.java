public class assertBool {
    private boolean bool;
    public assertBool(boolean b) { bool = b; }

    public assertBool isEqualTo(boolean b2){
        if (bool != b2) {
            throw new RuntimeException("assertBool: bool is not equal to Given boolean b2");
        }
        return this;
    }

    public assertBool isTrue(){
        if ( !(bool == true) ) {
            throw new RuntimeException("assertBool: is not true");
        }
        return this;
    }

    public assertBool isFalse() {
        if ( !(bool == false) ) {
            throw new RuntimeException("assertBool: is not false)");
        }
        return this;
    }


}
