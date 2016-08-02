package board.implementation;

import java.util.List;

import board.AbstractRespawnableBoardAdornment;
import board.RespawnableBoardAdornment;
import board.TimeAdornment;

public class TimeBoardAdornment extends AbstractRespawnableBoardAdornment 
			implements TimeAdornment{
	
	
	
	protected int timeStep = 0;
	
	public TimeBoardAdornment(RespawnableBoardAdornment son) {
		super(son);
	}

	@Override
	public List<Pawn> make(Move m) {
		timeStep = m.reversed?timeStep-1:timeStep+1;
		return son.make(m);
	}


	@Override
	public void respawn(Pawn pawn) {
		son.respawn(pawn);
	}

	@Override
	public int getTime() {
		return timeStep;
	}


	@Override
	public int getEnemyDistance(Move m) {
		return son.getEnemyDistance(m);
	}

}
