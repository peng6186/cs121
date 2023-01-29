import java.util.*;

public class Knight extends Piece {
    public Knight(Color c) { this.color = c;} // okay checked
    // implement appropriate methods

    public String toString() { // okay checked
        String color =  (super.color().equals(Color.WHITE) ? "w" : "b"); // okay
        String res = color.concat("n");
        return res;
    }

    public List<String> moves(Board b, String loc) {
        List<String> res = new LinkedList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);
        String new_loc;

        /* up */
        // left low L

        new_loc = String.valueOf((char)(col - 2)) + String.valueOf( (char)(row + 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }

        // left high L
        new_loc = String.valueOf((char)(col - 1)) + String.valueOf( (char)(row + 2) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // right low L
        new_loc = String.valueOf((char)(col + 2)) + String.valueOf( (char)(row + 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // right high L
        new_loc = String.valueOf((char)(col + 1)) + String.valueOf( (char)(row + 2) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }

        /* down */
        // left low L
        new_loc = String.valueOf((char)(col - 1)) + String.valueOf( (char)(row - 2) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // left high L
        new_loc = String.valueOf((char)(col - 2)) + String.valueOf( (char)(row - 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // right low L
        new_loc = String.valueOf((char)(col + 1)) + String.valueOf( (char)(row - 2) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // right high L
        new_loc = String.valueOf((char)(col + 2)) + String.valueOf( (char)(row - 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }

        return res;
    }

}