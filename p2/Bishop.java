import java.util.*;

public class Bishop extends Piece {
    public Bishop(Color c) {
        this.color = c;
    }
    // implement appropriate methods

    public String toString() {
        String color = (super.color().equals(Color.WHITE) ? "w" : "b");
        String res = color.concat("b");
        return res;
    }

    public List<String> moves(Board b, String loc) {
        List<String> res = new LinkedList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);
        String new_loc;

        // up-left diagonal
        new_loc = String.valueOf( (char)(col - 1) ) + String.valueOf( (char)(row + 1) );
        for (char cur_col = (char)(col - 1), cur_row = (char)(row + 1); b.isValidStringIndex(new_loc);) {

            if ( b.getPiece(new_loc) == null ) {
                res.add(new_loc);
            } else if ( b.getPiece(new_loc).color() != color() ) {
                res.add(new_loc);
                break;
            } else if( b.getPiece(new_loc).color() == color() ) {
                break;
            }

            --cur_col; ++cur_row;
            new_loc = String.valueOf(cur_col) + String.valueOf( cur_row );

        }
        // up-right
        new_loc = String.valueOf( (char)(col + 1) ) + String.valueOf( (char)(row + 1) );
        for (char cur_col = (char)(col + 1), cur_row = (char)(row + 1); b.isValidStringIndex(new_loc); ) {

            if ( b.getPiece(new_loc) == null ) {
                res.add(new_loc);
            } else if ( b.getPiece(new_loc).color() != color() ) {
                res.add(new_loc);
                break;
            } else if( b.getPiece(new_loc).color() == color() ) {
                break;
            }

            ++cur_col; ++cur_row;
            new_loc = String.valueOf(cur_col) + String.valueOf( cur_row );

        }
        // down-left
        new_loc = String.valueOf( (char)(col - 1) ) + String.valueOf( (char)(row - 1) );
        for (char cur_col = (char)(col - 1), cur_row = (char)(row - 1); b.isValidStringIndex(new_loc); ) {

            if ( b.getPiece(new_loc) == null ) {
                res.add(new_loc);
            } else if ( b.getPiece(new_loc).color() != color() ) {
                res.add(new_loc);
                break;
            } else if( b.getPiece(new_loc).color() == color() ) {
                break;
            }

            --cur_col; --cur_row;
            new_loc = String.valueOf(cur_col) + String.valueOf( cur_row );
        }

        // down-right
        new_loc = String.valueOf( (char)(col + 1) ) + String.valueOf( (char)(row - 1));
        for (char cur_col = (char)(col + 1), cur_row = (char)(row - 1); b.isValidStringIndex(new_loc);) {

            if ( b.getPiece(new_loc) == null ) {
                res.add(new_loc);
            } else if ( b.getPiece(new_loc).color() != color() ) {
                res.add(new_loc);
                break;
            } else if( b.getPiece(new_loc).color() == color() ) {
                break;
            }

            ++cur_col; --cur_row;
            new_loc = String.valueOf(cur_col) + String.valueOf( cur_row );

        }

        return res;
    }
}