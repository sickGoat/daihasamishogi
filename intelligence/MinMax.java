package intelligence;

import java.util.Iterator;
import java.util.Set;

import board.implementation.Move;
import board.implementation.MoveFactory;
import board.implementation.Player;

public class MinMax extends Alghoritm {

	public MinMax() {
	}

	@Override
	protected double executeAlghoritm(int depth, double alpha, double beta, Player player,Move parentMove) {
		return minMax(depth, alpha, beta, player,parentMove);

	}

	private double minMax(int depth, double alpha, double beta, Player player,Move parentMove) {
		if(parentMove.isGoal()){
			return Double.MAX_VALUE * player.getOpponent().getPlayerValue();
		}
		
		if(parentMove.isDanger()){
			return Double.MAX_VALUE * player.getOpponent().getPlayerValue();
		}
		
		if (depth == 0 || manager.isGameOver()) {
			return heuristicEvaluation();
		}
		
		double res;
		// il mio giocatore è sempre il massimizzatore
		boolean isMaximizing = (player == myPlayer);
		Set<Move> legalMoves = manager.getSortedMoves(player, comparator);
		Iterator<Move> it = legalMoves.iterator();

		if (isMaximizing) {
			res = NEGATIVE_INFINITY;
			while (it.hasNext() && !broker.interrupt()) {
				Move move = it.next();

				manager.makeShadow(move);

				res = Double.max(res, minMax(depth - 1, alpha, beta, player.getOpponent(),move));
				alpha = Double.max(alpha, res);

				manager.backOneStep();

				if (beta <= alpha) {
					// pruining
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
					// pruining
					break;
				}
			}

		}

		MoveFactory.getInstance().invalidate(legalMoves);
		return res;

	}

}
