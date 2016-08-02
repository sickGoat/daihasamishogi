package board.implementation;

import java.util.Comparator;
import java.util.List;
import java.util.Set;


import board.CompleteBoardManager;
import board.FCAdornment;
import board.FCAdornment.ScorePair;

public class CompleteBoardManagerImpl implements CompleteBoardManager {
	
	private ShadowBoardAdornment shadow;
	
	public FCAdornment fc;
	
	public ConcreteBoardAdornment board;
	
	public CompleteBoardManagerImpl() {
		board = new ConcreteBoardAdornment();
		fc = new FCBitMapBoardAdornment(board);
		shadow = new ShadowBoardAdornment(fc);
		board.setFCAdornment(fc);
	}
	
	@Override
	public double getMaxConfigurationValue(Player player) {
		return fc.getMaxConfiguration(player);
	}
	
	@Override
	public ScorePair getCumulativeMax(Player player) {
		return fc.getCumulativeMax(player);
	}
	
	@Override
	public int getNumberOfFinalConfiguration(Move move,Player playerType) {
		return fc.getNumberOfFinalConfiguration(move.to,playerType);
	}
	
	@Override
	public List<Move> getAllLegalMoves(Player player) {
		return board.getAllLegalMoves(player);
	}
	

	@Override
	public Set<Move> getSortedMoves(Player player, Comparator<Move> comparator) {
		return board.getSortedMoves(player, comparator);
	}
	
	
	@Override
	public void make(Move move) {
		fc.make(move);
	}

	@Override
	public int getPawnCount(Player player) {
		return board.getPawnCount(player);
	}

	@Override
	public void makeShadow(Move move) {
		shadow.make(move);
	}

	@Override
	public void backOneStep() {
		shadow.backOneStep();
	}

	@Override
	public void returnToTop() {
		shadow.returnToTop();
	}
	
	@Override
	public boolean isGameOver() {
		return fc.getMaxConfiguration(Player.WHITE) == 5 ||
			   fc.getMaxConfiguration(Player.BLACK) == 5 ||
			   board.getPawnCount(Player.WHITE) <= 1 ||
			   board.getPawnCount(Player.BLACK) <= 1;
	}

	@Override
	public String toString() {
		return fc.toString();
	}

	@Override
	public int getEnemyDistance(Move m) {
		return fc.getEnemyDistance(m);
	}

	

}
