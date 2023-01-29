import java.util.*;

abstract public class Piece {

    /* fields */
    protected Color color;
    private static HashMap<Character, PieceFactory> map = new HashMap<>();

    /* methods */
    public static void registerPiece(PieceFactory pf) { // okay checked
	    char piece = pf.symbol();
        map.put(piece, pf);
    }

    public static Piece createPiece(String name) { // okay checked
        char color = name.charAt(0);
        char piece = name.charAt(1);
        PieceFactory pf = map.get(piece);
        if (pf == null) {
            throw new RuntimeException("invalid Piece Name");
        }
        if ( !isValidColor(color) ) {
            throw new RuntimeException("invalid color");
        }
        Color c = (color == 'w' ? Color.WHITE : Color.BLACK);
        return pf.create(c);
    }

    public Color color() {  // okay checked
	    return color;
    }

    abstract public String toString();

    abstract public List<String> moves(Board b, String loc);

    // helper method
    static boolean isValidColor(char color) {
        return ( color == 'w' || color == 'b');
    }
}