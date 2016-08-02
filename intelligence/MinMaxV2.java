package intelligence;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import board.implementation.Move;
import board.implementation.MoveFactory;
import board.implementation.Player;

public class MinMaxV2 extends Alghoritm{
	
	private Set<Move> firstLevelMoves = null;
	
	
	@Override
	public Move execute(int depth) {
		firstLevelMoves = manager.getSortedMoves(myPlayer, comparator);
		
		// eventuali ordinamenti
		double bestValue = NEGATIVE_INFINITY;
		double value = 0;
		boolean interrupted = false;
		Move bestMove = null;
		Iterator<Move> it = firstLevelMoves.iterator();
		
		while (it.hasNext() && (interrupted=!broker.interrupt())) {
			Move move = it.next();
			manager.makeShadow(move);
			value = executeAlghoritm(depth, bestValue, POSITIVE_INFINITY, myPlayer.getOpponent(),move);
			manager.backOneStep();
			if (value > bestValue) {
				bestMove = move.clone();
				bestValue = value;
			}
		}
		
		if(!interrupted){
			MoveFactory.getInstance().invalidate(firstLevelMoves);
		}
		// la mossa viene clonata in quanto è riferita dal Factory
		return bestMove==null?((TreeSet<Move>)firstLevelMoves).last().clone():
						bestMove;
	}
	
	@Override
	protected double executeAlghoritm(int depth, double alpha, double beta, Player player, Move parentMove) {
		return minMax(depth, alpha, beta, player, parentMove);
	}
	
	private double minMax(int depth, double alpha, double beta, Player player,Move parentMove) {
		if(parentMove.isGoal()){
			return player == myPlayer?-Double.MAX_VALUE:Double.MAX_VALUE;
		}
		
		if (depth == 0 || manager.isGameOver()) {
			return heuristicEvaluation();
		}
		
		double res;
		// il mio giocatore è sempre il massimizzatore
		boolean isMaximizing = (player == myPlayer);
		Set<Move> legalMoves = manager.getSortedMoves(player, comparator);
		Iterator<Move> it 	= legalMoves.iterator();
		if (isMaximizing) {
			res = NEGATIVE_INFINITY;
			while (it.hasNext() && !broker.interrupt()) {
				Move move = it.next();
				manager.makeShadow(move);
				res = Double.max(res, minMax(depth - 1, alpha, beta, player.getOpponent(),move));
				alpha = Double.max(alpha, res);
				manager.backOneStep();

				if (beta <= alpha) {
					// pruning
					break;
				}
			}

		} else {
			res = POSITIVE_INFINITY;
			while (it.hasNext() && !broker.interrupt()) {
				Move move = it.next();
				manager.makeShadow(move);
				res = Double.min(res, minMax(depth - 1, alpha, beta, player.getOpponent(),move));
				beta = Double.min(beta, res);
				manager.backOneStep();
				if (beta <= alpha) {
					// pruning
					break;
				}
			}
		}
		MoveFactory.getInstance().invalidate(legalMoves);
		return res;
	}
}
