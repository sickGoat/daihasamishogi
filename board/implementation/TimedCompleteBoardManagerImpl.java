package board.implementation;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import board.TimedCompleteBoardManager;
import board.FCAdornment.ScorePair;

public class TimedCompleteBoardManagerImpl implements TimedCompleteBoardManager{
	
	private ShadowBoardAdornment shadow;
	
	private ConcreteBoardAdornment concrete;
	
	private FCBitMapBoardAdornment fc;
	
	private TimeBoardAdornment time;
	
	
	public TimedCompleteBoardManagerImpl() {
		concrete = new ConcreteBoardAdornment();
		fc = new FCBitMapBoardAdornment(concrete);
		time = new TimeBoardAdornment(fc);
		shadow = new ShadowBoardAdornment(time);
		concrete.setFCAdornment(fc);
	}
	
	/**
	 * ConcreteManager methods
	 * 
	 */
	
	@Override
	public List<Move> getAllLegalMoves(Player player) {
		return concrete.getAllLegalMoves(player);
	}

	@Override
	public Set<Move> getSortedMoves(Player player, Comparator<Move> comparator) {
		return concrete.getSortedMoves(player, comparator);
	}

	public void make(Move move) {
		time.make(move);
	}

	@Override
	public int getPawnCount(Player player) {
		return concrete.getPawnCount(player);
	}

	@Override
	public boolean isGameOver() {
		return fc.getMaxConfiguration(Player.WHITE) == 5 ||
				   fc.getMaxConfiguration(Player.BLACK) == 5 ||
				   concrete.getPawnCount(Player.WHITE) <= 1 ||
				   concrete.getPawnCount(Player.BLACK) <= 1;
	}
	
	@Override
	public int getEnemyDistance(Move m) {
		return concrete.getEnemyDistance(m);
	}
	
	
	/*
	 * FcManager methods
	 * 
	 */
	
	@Override
	public ScorePair getCumulativeMax(Player player) {
		return fc.getCumulativeMax(player);
	}
	
	@Override
	public int getNumberOfFinalConfiguration(Move move, Player playerType) {
		return fc.getNumberOfFinalConfiguration(move.to, playerType);
	}
	
	@Override
	public double getMaxConfigurationValue(Player player) {
		return fc.getMaxConfiguration(player);
	}
	
	
	/**
	 * TimeManager methods
	 */

	@Override
	public int getTime() {
		return time.getTime();
	}
	
	
	/**
	 * ShadowManager methods
	 */

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

}
