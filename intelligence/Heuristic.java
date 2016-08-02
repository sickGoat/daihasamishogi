package intelligence;

import board.implementation.Player;

public interface Heuristic {
	
	/**
	 * Metodo utilizzato per valutare la board
	 * 
	 * @return
	 */
	double evaluate();
	
	/**
	 * Metodo utilizzato per cambiare
	 * la strategia durante le varie fasi del
	 * gioco
	 * @param strategy
	 */
	void checkStrategy();
	
	/**
	 * Setta il giocatore che massimizza
	 * @param p
	 */
	void setMaximizerPlayer(Player p);
}
