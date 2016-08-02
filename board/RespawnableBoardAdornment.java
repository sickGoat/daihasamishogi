package board;

import java.util.List;

import board.implementation.Pawn;

public interface RespawnableBoardAdornment extends BoardAdornment {
	
	void respawn(List<Pawn> catches);
	
	void respawn(Pawn pawn);
	
}
