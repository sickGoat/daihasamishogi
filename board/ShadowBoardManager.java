package board;

import board.implementation.Move;

public interface ShadowBoardManager extends BoardManager {
	
	void makeShadow(Move move);
	
	void backOneStep();
	
	void returnToTop();

}
