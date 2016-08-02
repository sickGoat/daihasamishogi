package game;

import board.implementation.Move;
import board.implementation.Player;

/**
 * Interfaccia di comunicazione tra
 * il Broker e il Monitor
 * @author antonio
 *
 */
public interface MonitorBroker {
	
	/**
	 * Metodo utilizzato per ottenere
	 * la mossa dell'avversario
	 * @return
	 */
	Move getOpponentMove();
	
	/**
	 * Metodo utilizzato per verificare
	 * se è il proprio turno per giocare
	 */
	void myTurn();
	
	/**
	 * Metodo utilizzato per settare
	 * la mossa da eseguire
	 * @param move
	 */
	void setMove(Move move);
	
	/**
	 * Metodo utilizzato per ottenere
	 * il colore del mio player
	 * @return
	 */
	Player getPLayer();
	
	/**
	 * Metodo utilizzato per verificare
	 * la terminazione del tempo a disposizione
	 * per la ricerca
	 * @return
	 */
	boolean isTimeOver();
}
