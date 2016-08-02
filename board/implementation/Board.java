package board.implementation;

import java.util.HashSet;
import java.util.Set;

public class Board {

	private static Board singleton;

	protected final Cell[] board = new Cell[81];

	protected Set<Pawn> whitePawns = new HashSet<>(18);

	protected Set<Pawn> blackPawns = new HashSet<>(18);

	private Board() {
		initBoard();
	}

	public static Board getInstance() {
		if (singleton == null) {
			singleton = new Board();
		}
		return singleton;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length + 19; i++) {
			if (i == 0) {
				sb.append("   ");
				continue;
			}
			if (i < 10) {
				sb.append(Number.values()[9 - i].ordinal() + 1 + " ");
				continue;
			}
			if (i % 10 == 0) {
				sb.append("\n" + Letter.values()[i / 10 - 1].toString() + "| ");
				continue;
			} else {
				Cell c = board[i - (11 + i / 10 - 1)];
				if (c.occupied != null) {
					if (c.occupied.owner == Player.BLACK) {
						sb.append("B ");
					} else {
						sb.append("W ");
					}
				} else {
					sb.append("- ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Alloca tutta la board. Vedere il foglio del prof coglione Nere sopra e
	 * bianche sotto. 9 8 7 6 5 4 3 2 1 oriz A B C D E F G H I vert bordello! in
	 * realtà in oriz abbiamo: 8 7 6 5 4 3 2 1 0
	 */
	private void initBoard() {
		Letter[] allLetters = Letter.values();
		Number[] allNumbers = Number.values();
		for (int i = 0; i < 81; i++) {
			// decremento circolare
			Cell cell = new Cell(allLetters[i / 9], allNumbers[8 - (i % 9)]);
			if (i < 18) {
				Pawn p = new Pawn(Player.BLACK);
				p.position = cell;
				cell.occupied = p;
				blackPawns.add(p);
			} else if (i > 62) {
				Pawn p = new Pawn(Player.WHITE);
				p.position = cell;
				cell.occupied = p;
				whitePawns.add(p);
			}
			board[i] = cell;
		} // for
	}
	
	protected Cell fromBoard(int i, int j) {
		return board[i * 9 + 8 - j];
	}

	protected Cell fromBoard(Letter letter, Number number) {
		return fromBoard(letter.ordinal(), number.ordinal());
	}
}
