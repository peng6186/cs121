/**
 * @File Chess.java
 * @Author Pengcheng Xu(pengcheng.xu@tufts.edu)
 * This is the implementation for project 2 of CS121 Software Engineering
 * And Chess.java is the entry point of this project
 */
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Chess {
    public static void main(String[] args) {
	if (args.length != 2) {
	    System.out.println("Usage: java Chess layout moves");
		System.exit(1);
	}
	Piece.registerPiece(new KingFactory());
	Piece.registerPiece(new QueenFactory());
	Piece.registerPiece(new KnightFactory());
	Piece.registerPiece(new BishopFactory());
	Piece.registerPiece(new RookFactory());
	Piece.registerPiece(new PawnFactory());
	Board.theBoard().registerListener(new Logger());
	// args[0] is the layout file name
	// args[1] is the moves file name
	// Put your code to read the layout file and moves files
	// here.

	/* Read and deal with layout file */
	Board b = Board.theBoard();
	File layout = new File(args[0]);
	try {
		Scanner sn = new Scanner(layout);
		while(sn.hasNext()) {
			String line = sn.nextLine();
			if (line.length() == 0 ) {
				continue;
			}
			if (line.charAt(0) == '#') {
				continue;
			}
//			System.out.println(line);

			String delim = "[=]";
			String tokens[] = line.split(delim);
			String loc = tokens[0];
			String colored_piece = tokens[1];
//			System.out.println("token[0]: " + tokens[0]+ ", token[1]: " + tokens[1]);

			Piece p = Piece.createPiece(colored_piece);
			b.addPiece(p, loc);
		}

		sn.close();
	} catch (IOException e) {
		System.err.println("File Not Found");
		System.exit(1);
	}





	/* Read and deal with layout file */
		File moves = new File(args[1]);
		try {
			Scanner sn = new Scanner(moves);
			while(sn.hasNext()) {
				String line = sn.nextLine();
				if (line.charAt(0) == '#') {
					continue;
				}
//				System.out.println(line);

				String delim = "[-]";
				String tokens[] = line.split(delim);
				String from = tokens[0];
				String to = tokens[1];
//				System.out.println("token[0]: " + tokens[0]+ ", token[1]: " + tokens[1]);

				b.movePiece(from, to);
			}

			sn.close();
		} catch (IOException e) {
			System.err.println("File Not Found");
			System.exit(1);
		}
	// Leave the following code at the end of the simulation:
	System.out.println("Final board:");
	Board.theBoard().iterate(new BoardPrinter());
    }
}