package intelligence;

import java.util.Iterator;
import java.util.Set;

import board.CompleteBoardManager;
import board.implementation.Move;
import board.implementation.MoveFactory;
import board.implementation.Player;
import game.Broker;


public class NegaScout extends Alghoritm {
	
	
	public NegaScout() {
	}
	
	
	@Override
	public void init(CompleteBoardManager board, Broker broker, Heuristic heuristic) {
		super.init(board, broker, heuristic);
	}

	@Override
	public Move execute(int depth) {
		Set<Move> firstLevelMoves = manager.getSortedMoves(myPlayer, comparator);
		double lastValue = 0, bestValue = NEGATIVE_INFINITY;
		Move bestMove = null;
		Iterator<Move> it = firstLevelMoves.iterator();
		while(it.hasNext() /*&& !broker.interrupt()*/) {
			Move move = it.next();
			manager.makeShadow(move);
			lastValue = -executeAlghoritm(depth , NEGATIVE_INFINITY, POSITIVE_INFINITY, myPlayer.getOpponent(),move);
			manager.backOneStep();
			if (bestMove==null || lastValue > bestValue) {
				bestMove = move.clone();
				bestValue = lastValue;
			}
			
		}
		
		MoveFactory.getInstance().invalidate(firstLevelMoves);
		return bestMove;
	}

	@Override
	protected double executeAlghoritm(int depth, double alpha, double beta, Player player,Move parentMove) {
		if(parentMove.isGoal()){
			return Double.MAX_VALUE * player.getOpponent().getPlayerValue();
		}
		
		if(parentMove.isDanger()){
			return Double.MAX_VALUE * player.getPlayerValue();
		}
		
		if (depth == 0 || manager.isGameOver()) {
			return heuristicEvaluation();
		}
		boolean isFirstChild = true;
		double b = beta;
		Set<Move> legalMoves = manager.getSortedMoves(player,comparator);
		Iterator<Move> it = legalMoves.iterator();
		while(it.hasNext() /*&& !broker.interrupt()*/) {
			Move move = it.next();
			manager.makeShadow(move);
			double v = -executeAlghoritm(depth - 1, -b, -alpha, player.getOpponent(),move);
			if (!isFirstChild && alpha < v && v < beta) {
				// eseguo ricerca con finestra nulla
				v = -executeAlghoritm(depth - 1, -beta, -v, player.getOpponent(),move);
			}
			manager.backOneStep();
			alpha = Double.max(alpha, v);
			if (alpha >= beta) {
				break;// pruining
			}
			b = alpha + 1;
			if (isFirstChild) {
				isFirstChild = false;
			}
		}
		
		MoveFactory.getInstance().invalidate(legalMoves);
		return alpha;
	}


}
