package board.implementation;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import board.AbstractRespawnableBoardAdornment;
import board.RespawnableBoardAdornment;
import board.ShadowAdornment;

public class ShadowBoardAdornment extends AbstractRespawnableBoardAdornment implements ShadowAdornment {

	protected Stack<Move> shadowMoves = new Stack<>();

	public ShadowBoardAdornment(RespawnableBoardAdornment son) {
		super(son);
	}

	

	@Override
	public void respawn(Pawn pawn) {
		son.respawn(pawn);
	}

	@Override
	public List<Pawn> make(Move m) {
		List<Pawn> catches = son.make(m);
		m.pawnCaught.addAll(catches);
		shadowMoves.push(m);
		return catches;
	}

	@Override
	public List<Move> getAllLegalMoves(Player player) {
		return son.getAllLegalMoves(player);
	}

	
	@Override
	public int getEnemyDistance(Move m) {
		return son.getEnemyDistance(m);
	}
	
	@Override
	public void backOneStep() {
		if (!shadowMoves.isEmpty()) {
			Move move = shadowMoves.pop();
			Iterator<Pawn> it = move.pawnCaught.iterator();
			while(it.hasNext()){
				son.respawn(it.next());
				it.remove();
			}
			
			move = MoveFactory.getInstance().getReverse(move);
			son.make(move);
			MoveFactory.getInstance().invalidate(move);
		}
	}

	@Override
	public void returnToTop() {
		while (!shadowMoves.isEmpty()) {
			backOneStep();
		}
	}

}
