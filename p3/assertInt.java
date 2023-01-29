public class assertInt {
    private int i;
    public assertInt(int i) {this.i = i;}

    public assertInt isEqualTo(int i2){
        if (i != i2) {
            throw new RuntimeException("assertInt: i is not equal to Given int i2");
        }
        return this;
    }

    public assertInt isLessThan(int i2){
        if ( i >= i2) {
            throw new RuntimeException("assertInt: i is not less than i2");
        }
        return this;
    }

    public assertInt isGreaterThan(int i2) {
        if ( i <= i2 ) {
            throw new RuntimeException("assertInt: i is not greater than i2)");
        }
        return this;
    }
}
