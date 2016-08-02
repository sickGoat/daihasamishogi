package game;

import board.implementation.Move;
import board.implementation.Player;

/**
 * Interfaccia di comunicazione tra il
 * connector e il monitor 
 * @author antonio
 *
 */
public interface MonitorConnector {
	
	/**
	 * Il connector setta la mossa dell'avversario
	 * se siamo nella fase iniziale e il turno
	 * è il mio move sarà null.
	 * @param move
	 */
	void setOpponentMove(Move move);
	
	/**
	 * Il connector informa il broker
	 * che è il proprio turno di giocare
	 */
	void yourTurn();
	
	/**
	 * Metodo utilizzato per ottenere
	 * la mossa calcolata dal broker
	 * @return
	 */
	Move getMove();
	
	
	/**
	 * Metodo utilizzato per settare il
	 * current plyer della partita
	 * @param player
	 */
	void setPLayer(Player player);
	
	/**
	 * Metodo utilizzato per informare
	 * il broker che è finito il tempo
	 * per la ricerca della mossa
	 */
	void setTimeOver();
	
	
}
