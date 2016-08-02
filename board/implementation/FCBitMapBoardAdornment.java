package board.implementation;

import static board.implementation.BitUtil.*;

import java.util.List;

import board.AbstractRespawnableBoardAdornment;
import board.FCAdornment;
import board.RespawnableBoardAdornment;

public class FCBitMapBoardAdornment extends AbstractRespawnableBoardAdornment implements FCAdornment {

	private DiagonalBitMap[] leftd;
	private DiagonalBitMap[] rightd;
	private BitMap[] vertical;

	public FCBitMapBoardAdornment(RespawnableBoardAdornment son) {
		super(son);
		// BLACK
		// 0x18018006020603L
		// white
		// 0x1806006001810180L
		// diagonali sinistre
		leftd = new DiagonalBitMap[2];
		leftd[0] = new DiagonalBitMap(0x1806006001810180L);
		leftd[1] = new DiagonalBitMap(0x18018006020603L);
		// diagonali destre
		rightd = new DiagonalBitMap[2];
		rightd[0] = new DiagonalBitMap(0x1806006001810180L);
		rightd[1] = new DiagonalBitMap(0x18018006020603L);
		// verticali
		vertical = new BitMap[2];
		// TODO da init 0x6030180
		vertical[0] = new BitMap(0xc0603, 0xc0603, 0xc0603);
		vertical[1] = new BitMap(0x6030180, 0x6030180, 0x6030180);
	}

	@Override
	public void respawn(Pawn pawn) {
		update(pawn.position.id, pawn.owner.ordinal());
		son.respawn(pawn);
	}

	@Override
	public List<Pawn> make(Move m) {
		int from = Board.getInstance().fromBoard(m.from.letter, m.from.number).id;
		int to = Board.getInstance().fromBoard(m.to.letter, m.to.number).id;
		int player = Board.getInstance().fromBoard(m.from.letter, m.from.number).occupied.owner.ordinal();
		update(from, player);
		update(to, player);
		List<Pawn> catches = son.make(m);
		if (catches != null) {
			for (Pawn p : catches) {
				update(p.position.id, p.owner.ordinal());
			}
		}
		return catches;
	}

	@Override
	public double getMaxConfiguration(Player player) {
		int max = Integer.MIN_VALUE;
		int index = -1;
		int fc = -1;
		int p = player.ordinal();
		int score = -1;
		int[] toEval = FC_DIAGONAL_LOOKUP[p];
		int vert_mask = VERTICAL_MASK[p];
		for (int i = 0; i < toEval.length; i++) {
			index = toEval[i];
			fc = (int) (leftd[p].bm >>> DIAGONAL_SHIFT[index]);
			fc = fc & FC_DIAGONAL_MASK[p][index];
			score = FC_MAX_EVALUATION[fc];
			max = (score > max) ? score : max;
			fc = (int) (rightd[p].bm >>> DIAGONAL_SHIFT[index]);
			fc = fc & FC_DIAGONAL_MASK[p][index];
			score = FC_MAX_EVALUATION[fc];
			max = (score > max) ? score : max;
		}
		for (int i = 0; i < 3; i++) {
			fc = vertical[p].bm[i];
			fc = fc & vert_mask;
			score = FC_MAX_EVALUATION[fc];
			max = (score > max) ? score : max;
			fc = vertical[p].bm[i] >>> 9;
			fc = fc & vert_mask;
			score = FC_MAX_EVALUATION[fc];
			max = (score > max) ? score : max;
			fc = vertical[p].bm[i] >>> 18;
			fc = fc & vert_mask;
			score = FC_MAX_EVALUATION[fc];
			max = (score > max) ? score : max;
		}
		return max;
	}

	@Override
	public int getMaxFinalConfiguration(Cell from, Cell to, Player player) {
		 int p = player.ordinal();
		 int max = Integer.MIN_VALUE;
		 int score = -1;
		 int fc = -1;
		 int indexFrom = from.id;
		 int index = to.id;
		 int fcIndex = -1;
		 int vert_mask = VERTICAL_MASK[p];
		 // sinistra
		 fcIndex = DIAGONAL_LEFT_OWNERSHIP[index];
		 fc = (int) ((leftd[p].bm ^ DIAGONAL_LEFT_MOVESET[index]
		 ^ DIAGONAL_LEFT_MOVESET[indexFrom]) >>> DIAGONAL_SHIFT[fcIndex]);
		 fc = fc & FC_DIAGONAL_MASK[p][fcIndex];
		 score = FC_MAX_EVALUATION[fc];
		 max = score;
		 // destra
		 fcIndex = DIAGONAL_RIGHT_OWNERSHIP[index];
		 fc = (int) ((rightd[p].bm ^ DIAGONAL_RIGHT_MOVESET[index]
		 ^ DIAGONAL_RIGHT_MOVESET[indexFrom]) >>> DIAGONAL_SHIFT[fcIndex]);
		 fc = fc & FC_DIAGONAL_MASK[p][fcIndex];
		 score = FC_MAX_EVALUATION[fc];
		 max = max < score ? score : max;
		 // verticale
		 int[] copy = new int[3];
		 copy[0] = vertical[p].bm[0];
		 copy[1] = vertical[p].bm[1];
		 copy[2] = vertical[p].bm[2];
		 copy[VERTICAL_BLOCK_OWNERSHIP[indexFrom]] ^= VERTICAL_MOVESET[indexFrom];
		 int block = VERTICAL_BLOCK_OWNERSHIP[index];
		 int row = VERTICAL_ROW_OWNERSHIP[index];
		 fc = ((copy[block] ^ VERTICAL_MOVESET[index]) >>> row * 9);
		 fc = fc & vert_mask;
		 score = FC_MAX_EVALUATION[fc];
		 max = (score > max) ? score : max;
		 // restituisco il massimo tra i massimi
		 return max;
	 }
		

	private void update(int cell, int player) {
		vertical[player].xor(VERTICAL_BLOCK_OWNERSHIP[cell], VERTICAL_MOVESET[cell]);
		leftd[player].xor(DIAGONAL_LEFT_MOVESET[cell]);
		rightd[player].xor(DIAGONAL_RIGHT_MOVESET[cell]);
	}

	@Override
	public String toString() {
		String p0 = vertical[0].toString().replace('1', 'W').replace(' ', '-').replace('0', '-');
		String p1 = vertical[1].toString().replace('1', 'B').replace(' ', '-').replace('0', '-');
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < p0.length(); i++) {
			if (p0.charAt(i) == 'W') {
				sb.append(p0.charAt(i) + " ");
			} else if (p1.charAt(i) == 'B') {
				sb.append(p1.charAt(i) + " ");
			} else {
				sb.append("- ");
			}
		}
		p0 = sb.toString();
		sb = new StringBuilder();
		sb.append("  A B C D E F G H I");
		sb.append("\n9|" + p0.substring(0, 17) + "|9");
		sb.append("\n8|" + p0.substring(18, 35) + "|8");
		sb.append("\n7|" + p0.substring(36, 53) + "|7");
		sb.append("\n6|" + p0.substring(54, 71) + "|6");
		sb.append("\n5|" + p0.substring(72, 89) + "|5");
		sb.append("\n4|" + p0.substring(90, 107) + "|4");
		sb.append("\n3|" + p0.substring(108, 125) + "|3");
		sb.append("\n2|" + p0.substring(126, 143) + "|2");
		sb.append("\n1|" + p0.substring(144, 161) + "|1");
		sb.append("\n  A B C D E F G H I");
		sb.append("\n");
		// diagonali
		sb.append("Diagonali sinistre W:\n");
		sb.append(leftd[0].toString());
		sb.append("\nDiagonali destre W:\n");
		sb.append(rightd[0].toString());
		sb.append("\nDiagonali sinistre B:\n");
		sb.append(leftd[1].toString());
		sb.append("\nDiagonali destre B:\n");
		sb.append(rightd[1].toString());
		return sb.toString();
	}

	@Override
	public int getEnemyDistance(Move m) {
		throw new UnsupportedOperationException("Non implementato");
	}

	@Override
	public ScorePair getCumulativeMax(Player player) {
		throw new UnsupportedOperationException("Non implementato");
	}

	@Override
	public int getNumberOfFinalConfiguration(Cell c, Player playerType) {
		throw new UnsupportedOperationException("Non implementato");
	}
}
