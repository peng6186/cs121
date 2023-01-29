import java.util.*;

public class King extends Piece {
    public King(Color c) { // okay
        this.color = c;
    }
    // implement appropriate methods

    public String toString() { // okay
        String color =  (super.color().equals(Color.WHITE) ? "w" : "b");
        String res = color.concat("k");
        return res;    }

    public List<String> moves(Board b, String loc) {
        List<String> res = new LinkedList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);
        // up okay
        String new_loc = String.valueOf(col) + String.valueOf( (char)(row + 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // down okay
        new_loc = String.valueOf(col) + String.valueOf( (char)(row - 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // left okay
        new_loc = String.valueOf( (char)(col - 1) ) + String.valueOf( row );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // right okay
        new_loc = String.valueOf( (char)(col + 1) ) + String.valueOf( row );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // up-left diagonal
        new_loc = String.valueOf( (char)(col - 1) ) + String.valueOf( (char)(row + 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // up-right
        new_loc = String.valueOf( (char)(col + 1) ) + String.valueOf( (char)(row + 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }
        // down-left
        new_loc = String.valueOf( (char)(col - 1) ) + String.valueOf( (char)(row - 1) );
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }

        // down-right
        new_loc = String.valueOf( (char)(col + 1) ) + String.valueOf( (char)(row - 1));
        if (b.isValidStringIndex(new_loc)) {
            if ( (b.getPiece(new_loc) == null) || (b.getPiece(new_loc).color() != color()) )    // empty or enemy
                res.add(new_loc);
        }

        return res;
    }

}