package board;

import board.FCAdornment.ScorePair;
import board.implementation.Move;
import board.implementation.Player;

public interface FCBoardManager extends BoardManager {
	
	double getMaxConfigurationValue(Player player);
	
	ScorePair getCumulativeMax(Player player);
	
	/**
	 * Il metodo restituisce il numero di final configurations
	 * incidenti sulla cella
	 * @param c
	 * @return
	 */
	int getNumberOfFinalConfiguration(Move move,Player playerType);
}
