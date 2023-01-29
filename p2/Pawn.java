import java.util.*;

public class Pawn extends Piece {
    public Pawn(Color c) {
        this.color = c;
    } // okay
    // implement appropriate methods

    public String toString() { // okay checked
        String color = (super.color().equals(Color.WHITE) ? "w" : "b");
        String res = color.concat("p");
        return res;
    }

    public List<String> moves(Board b, String loc) {
        List<String> res = new LinkedList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);
        String new_loc;

        if (color() == Color.WHITE) { // Color.WHITE
            /* white */
            // forward
            new_loc = String.valueOf((char) (col)) + String.valueOf((char) (row + 1));
            if (b.isValidStringIndex(new_loc)) {
                if (b.getPiece(new_loc) == null) {
                    res.add(new_loc);
                    if (row == '2') { // home row
                        new_loc = String.valueOf((char) (col)) + String.valueOf((char) (row + 2));
                        if (b.getPiece(new_loc) == null)
                            res.add(new_loc);
                    }
                }
            }
            // diagonal
            // left-diag
            new_loc = String.valueOf((char) (col - 1)) + String.valueOf((char) (row + 1));
            if (b.isValidStringIndex(new_loc)) {
                if ( (b.getPiece(new_loc) != null)&& (b.getPiece(new_loc).color() != color()) )    // enemy
                    res.add(new_loc);
            }
            // right-diag
            new_loc = String.valueOf((char) (col + 1)) + String.valueOf((char) (row + 1));
            if (b.isValidStringIndex(new_loc)) {
                if ( (b.getPiece(new_loc) != null)&& (b.getPiece(new_loc).color() != color()) )    // enemy
                    res.add(new_loc);

            }
        } else { // Color.Black
            /* black */
            // forward
            new_loc = String.valueOf((char) (col)) + String.valueOf((char) (row - 1));
            if (b.isValidStringIndex(new_loc)) {
                if (b.getPiece(new_loc) == null) {
                    res.add(new_loc);
                    if (row == '7') { // home row
                        new_loc = String.valueOf((char) (col)) + String.valueOf((char) (row - 2));
                        if (b.getPiece(new_loc) == null)
                            res.add(new_loc);
                    }
                }
            }
            // diagonal
            // left-diag
            new_loc = String.valueOf((char) (col - 1)) + String.valueOf((char) (row - 1));
            if (b.isValidStringIndex(new_loc)) {
                if ((b.getPiece(new_loc) != null)&& (b.getPiece(new_loc).color() != color()))    // enemy
                    res.add(new_loc);
            }
            // right-diag
            new_loc = String.valueOf((char) (col + 1)) + String.valueOf((char) (row - 1));
            if (b.isValidStringIndex(new_loc)) {
                if ((b.getPiece(new_loc) != null)&& (b.getPiece(new_loc).color() != color()))    // enemy
                    res.add(new_loc);

            }
        }

        return res;
    }
}