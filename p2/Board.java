import java.util.LinkedList;

public class Board {

    /* fields */
    private Piece[][] pieces = new Piece[8][8];

    private static Board singleBoard;

    private LinkedList<BoardListener> listeners_list = new LinkedList<>();

    /* methods */
    private Board() { }

    public static Board theBoard() { // okay checked lazy singleton
	    if (singleBoard == null)
            singleBoard =  new Board();

        return singleBoard;
    }

    // Returns piece at given loc or null if no such piece exists
    public Piece getPiece(String loc) { // okay checked
        char char_col = loc.charAt(0);
	    int col = getNumIndexOfCol(char_col);
        int row = Integer.parseInt(loc.substring(1)) - 1;
        if( !isValidIndex(row, col) )
            throw new RuntimeException("invalid index");

        return pieces[row][col]; // don't need to check, cuz the empty square is initially null.
    }

    public void addPiece(Piece p, String loc) { // okay checked
        char char_col = loc.charAt(0);
        int col = getNumIndexOfCol(char_col);
        int row = Integer.parseInt(loc.substring(1)) - 1;

        if ( !isValidIndex(row, col) ) {
            throw new RuntimeException("invalid index");
        }
        if ( isOccupied(row, col) ) {
            throw new RuntimeException("location already occupied");
        }

        pieces[row][col] = p;
    }

    public void movePiece(String from, String to) { // !FIXME PIECE.MOVES() need to be implemented
        Piece p_from = getPiece(from);
        if (p_from == null) {
            throw new RuntimeException("movePiece: from is empty");
        }

        if ( !p_from.moves(singleBoard, from).contains(to) ) { // move is invalid
            throw new RuntimeException("invalid move");
        } else { // peform the move
                callOnMoveListeners(from, to, getPiece(from));

                Piece p_to = getPiece(to);
                if ( p_to != null ) { // capture
                    callOnCaptureListeners(getPiece(from), getPiece(to));
                }

                // set pieces[from] = null
                char from_char_col = from.charAt(0);
                int from_col = getNumIndexOfCol(from_char_col);
                int from_row = Integer.parseInt(from.substring(1)) - 1;
                Piece temp = pieces[from_row][from_col];
                pieces[from_row][from_col] = null;

                // set pieces[to] = pieces[from]
                char to_char_col = to.charAt(0);
                int to_col = getNumIndexOfCol(to_char_col);
                int to_row = Integer.parseInt(to.substring(1)) - 1;
                pieces[to_row][to_col] = temp;
        }

    }

    public void clear() { // okay checked
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                pieces[row][col] = null;
            }
        }
    }

    public void registerListener(BoardListener bl) {
        listeners_list.add(bl);
    }

    public void removeListener(BoardListener bl) {
        listeners_list.remove(bl);
    }

    public void removeAllListeners() {
        listeners_list.clear();
    }

    public void iterate(BoardInternalIterator bi) { // okay checked
        for (int row = 1; row <= 8; ++row) {
            for (char col = 'a'; col <= 'h'; ++col) {
                bi.visit(String.valueOf(col) + String.valueOf(row), pieces[row - 1][col - 'a']);
            }
        }
    }

    // helper method
    int getNumIndexOfCol(char char_col) {
        switch (char_col) {
            case 'a' :
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            default: // invalid
                throw new RuntimeException("invalid col ascii char!");
        }
    }

    boolean isValidIndex(int row, int col) {
        return (0 <= row && row <= 7) && (0 <= col && col <= 7);
    }
    boolean isOccupied(int row, int col) {
        return pieces[row][col] == null ? false : true;
    }

    void callOnMoveListeners(String from, String to, Piece p) {
        for (BoardListener bl: listeners_list) {
            bl.onMove(from, to, p);
        }
    }

    void callOnCaptureListeners(Piece attacker, Piece captured) {
        for (BoardListener bl: listeners_list) {
            bl.onCapture(attacker, captured);
        }
    }

    public boolean isValidStringIndex(String loc) {
        if (loc.length() != 2)
            throw new RuntimeException("invalid loc, must be two chars");
        char col = loc.charAt(0);
        char row = loc.charAt(1);
        if ( ('a'<=col && col<='h')  && (row >= '1' && row <= '8') ) {
            return true;
        } else {
            return false;
        }
    }
}