import java.security.spec.ECField;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Test {

    // Run "java -ea Test" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)

    public static void test1() {
	Board b = Board.theBoard();
	Piece.registerPiece(new PawnFactory());
	Piece p = Piece.createPiece("bp");
	b.addPiece(p, "a3");
	assert b.getPiece("a3") == p;
    System.out.println("test1 succeed");
    }
    
    public static void testTheBoard() {
        Board b1 = Board.theBoard();
        Board b2 = Board.theBoard();
        assert b1 == b2;

        System.out.println("theBoard succeed");

    }

    public static void testGetPiece() {
        Board b = Board.theBoard();
        assert b.getPiece("a1") == null;

        boolean thrown = false;
        try {
            b.getPiece("a9");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.getPiece("j1");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.getPiece("a11");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.getPiece("k9");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;
        System.out.println("getPiece succeed");

    }

    public static void testAddPiece() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("bp");

        boolean thrown = false;
        try {
            b.addPiece(p,"a9");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.addPiece(p, "j1");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.addPiece(p,"a11");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.addPiece(p,"k9");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        b.addPiece(p, "a4");
        assert b.getPiece("a4") == p;

        thrown = false;
        try {
            b.addPiece(p,"a4");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        System.out.println("addPiece succeed");

    }

    public static void testClear() {
        Board b = Board.theBoard();
        b.clear();

        for (int row = 1; row <= 8; ++row) {
            for (char col = 'a'; col <= 'h'; ++col) {
                assert (b.getPiece(String.valueOf(col) + String.valueOf(row)) == null);
            }
        }

        System.out.println("clear succeed");
    }
//
    public static void testInternalIterator() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece.registerPiece(new KnightFactory());
        Piece p = Piece.createPiece("wp");
        assert p.color() == Color.WHITE;
        assert p.toString().equals( "wp" );
        Piece n = Piece.createPiece("bn");
        assert n.color() == Color.BLACK;
        assert n.toString().equals( "bn" );

        b.addPiece(p, "a2");
        b.addPiece(n, "b8");
        b.iterate(new BoardPrinter());

        System.out.println("internalIterator succeed");

    }

    public static void testKing() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new KingFactory());
        Piece k = Piece.createPiece("bk");
        assert k.toString().equals("bk");

        b.addPiece(k, "a8");

        List<String> k_moves = k.moves(b, "a8");
        assert k_moves.size() == 3;
        assert k_moves.containsAll(Arrays.asList("a7", "b7", "b8"));
        b.clear();

        Piece k2 = Piece.createPiece("bk");
        b.addPiece(k2, "d5");
        b.addPiece(k, "d4");
        k_moves = k.moves(b, "d4");
        assert k_moves.size() == 7;
        assert k_moves.containsAll(Arrays.asList("c3", "d3", "e3", "c4", "e4", "c5", "e5"));

        System.out.println("King succeed");
    }

    public static void testQueen() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new QueenFactory());
        Piece.registerPiece(new PawnFactory());
        Piece wq = Piece.createPiece("wq");
        Piece wp = Piece.createPiece("wp");
        Piece bp = Piece.createPiece("bp");
        assert wq.toString().equals("wq");


        List<String> q_moves = wq.moves(b, "h1");
        assert q_moves.size() == 21;
        assert q_moves.contains("g1");
        assert q_moves.contains("a1");
        assert q_moves.contains("h2");
        assert q_moves.contains("h8");
        assert q_moves.contains("g2");
        assert q_moves.contains("a8");
        assert !q_moves.contains("h1");
        b.clear();

        b.addPiece(wp, "d6");
        b.addPiece(wp, "d4");
        b.addPiece(wp, "c5");
        b.addPiece(wp, "e5");
        b.addPiece(bp, "c6");
        b.addPiece(bp, "e6");
        b.addPiece(bp, "c4");
        b.addPiece(bp, "e4");

        q_moves = wq.moves(b, "d5");

        assert q_moves.size() == 4;
        q_moves.containsAll(Arrays.asList("c6", "e6", "c4", "e4"));
        System.out.println("Queen succeed");
    }


    public static void testBishop() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new BishopFactory());
        Piece.registerPiece(new PawnFactory());
        Piece wb = Piece.createPiece("wb");
        Piece wp = Piece.createPiece("wp");
        Piece bp = Piece.createPiece("bp");
        assert wb.toString().equals("wb");


        List<String> b_moves = wb.moves(b, "h1");
//        System.out.println(b_moves.size());
        assert b_moves.size() == 7;
        assert b_moves.containsAll(Arrays.asList("g2", "f3", "e4", "d5", "c6", "b7", "a8"));
        b.clear();

        b.addPiece(wp, "d6");
        b.addPiece(wp, "d4");
        b.addPiece(wp, "c5");
        b.addPiece(wp, "e5");
        b.addPiece(bp, "c6");
        b.addPiece(bp, "e6");
        b.addPiece(bp, "c4");
        b.addPiece(bp, "e4");

        b_moves = wb.moves(b, "d5");

        assert b_moves.size() == 4;
        b_moves.containsAll(Arrays.asList("c6", "e6", "c4", "e4"));
        System.out.println("Bishop succeed");
    }

    public static void testRook() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new RookFactory());
        Piece.registerPiece(new PawnFactory());
        Piece wr = Piece.createPiece("wr");
        Piece wp = Piece.createPiece("wp");
        Piece bp = Piece.createPiece("bp");
        assert wr.toString().equals("wr");


        List<String> b_moves = wr.moves(b, "h1");
//        System.out.println(b_moves.size());
        assert b_moves.size() == 14;
        assert b_moves.containsAll(Arrays.asList("h2", "h8", "f1", "a1"));
        b.clear();

        b.addPiece(wp, "d6");
        b.addPiece(wp, "d4");
        b.addPiece(wp, "c5");
        b.addPiece(wp, "e5");
        b.addPiece(bp, "c6");
        b.addPiece(bp, "e6");
        b.addPiece(bp, "c4");
        b.addPiece(bp, "e4");

        b_moves = wr.moves(b, "d5");

        assert b_moves.size() == 0;
        System.out.println("Rook succeed");
    }

    public static void testKnight() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new KnightFactory());
        Piece.registerPiece(new PawnFactory());
        Piece wn = Piece.createPiece("wn");
        Piece wp = Piece.createPiece("wp");
        Piece bp = Piece.createPiece("bp");
        assert wn.toString().equals("wn");


        List<String> b_moves = wn.moves(b, "h1");
//        System.out.println(b_moves.size());
//        for(String s: b_moves) {
//            System.out.println(s);
//        }
        assert b_moves.size() == 2;
        assert b_moves.containsAll(Arrays.asList("g3", "f2"));
        b.clear();

        b.addPiece(wp, "b6");
        b.addPiece(wp, "f6");
        b.addPiece(wp, "b4");
        b.addPiece(wp, "f4");
        b.addPiece(bp, "c7");
        b.addPiece(bp, "e7");
        b.addPiece(bp, "c3");
        b.addPiece(bp, "e3");

        b_moves = wn.moves(b, "d5");

        assert b_moves.size() == 4;
        assert b_moves.containsAll(Arrays.asList("c3", "e3", "c7", "e7"));
        System.out.println("Knight succeed");
    }

    public static void testPawn() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new PawnFactory());
        Piece wp = Piece.createPiece("wp");
        Piece wp2 = Piece.createPiece("wp");
        Piece bp = Piece.createPiece("bp");
        Piece bp2 = Piece.createPiece("bp");
        assert wp.toString().equals("wp");


        List<String> b_moves = wp.moves(b, "h2");
//        System.out.println(b_moves.size());
//        for(String s: b_moves) {
//            System.out.println(s);
//        }
        assert b_moves.size() == 2;
        assert b_moves.containsAll(Arrays.asList("h4", "h3"));
        b.addPiece(wp2, "h3");
        b_moves = wp.moves(b,"h2");
        assert b_moves.size() == 0;
        b.clear();

        b.addPiece(bp, "c6");
        b_moves = wp.moves(b, "d5");

        assert b_moves.size() == 2;
        assert b_moves.containsAll(Arrays.asList("d6","c6"));
        b.clear();

        b_moves = bp.moves(b, "a1");
        assert b_moves.size() == 0;
        b_moves = bp.moves(b, "a8");
        assert b_moves.size() == 1;
        assert b_moves.contains("a7");
        b_moves = bp.moves(b, "a7");
        assert b_moves.size() == 2;
        assert b_moves.containsAll(Arrays.asList("a6", "a5"));

        b.addPiece(wp2, "b4");
        b_moves = bp.moves(b, "a5");
        assert b_moves.size() == 2;
        assert b_moves.containsAll(Arrays.asList("a4", "b4"));
        System.out.println("Pawn succeed");
    }

    public static void testMovePiece() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new PawnFactory());
        Piece wp = Piece.createPiece("wp");
        Piece wp2 = Piece.createPiece("wp");
        Piece bp = Piece.createPiece("bp");
        Piece bp2 = Piece.createPiece("bp");
        Board.theBoard().registerListener(new Logger());

        boolean thrown = false;
        try {
            b.movePiece("d2", "d4");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        b.addPiece(wp, "d2");
        thrown = false;
        try {
            b.movePiece("d2", "m9");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        thrown = false;
        try {
            b.movePiece("d2", "d5");
        } catch(Exception e) {
            thrown = true;
        }
        assert thrown;

        b.movePiece("d2", "d4");
        assert b.getPiece("d2") == null;
        assert b.getPiece("d4") == wp;

        b.clear();
        b.addPiece(wp, "b2");
        b.addPiece(bp, "c3");
        assert b.getPiece("b2") == wp;
        assert b.getPiece("c3") == bp;
        b.movePiece("b2", "c3");
        assert b.getPiece("b2") == null;
        assert b.getPiece("c3") == wp;

        System.out.println("movePiece succeed");
    }
    public static void main(String[] args) {

        test1();
        testTheBoard();
        testGetPiece();
        testAddPiece();
        testClear();
        testInternalIterator();
        testKing();
        testQueen();
        testBishop();
        testRook();
        testKnight();
        testPawn();
        testMovePiece();
    }

}