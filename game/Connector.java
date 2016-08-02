package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import board.implementation.Move;
import board.implementation.Player;
import util.Parser;

/**
 * Si occupa della comunicazione col server e quindi regola le fasi di gioco
 * interagendo con il monitor Da rivedere situazione mossa iniziale
 * 
 * @author antonio
 *
 */
public class Connector implements Runnable {

	private BufferedReader reader;

	private PrintWriter writer;

	private Socket socket;

	private MonitorConnector monitor;

	private long timeLimit;

	private Parser parser = new Parser();

	public Connector(){
		
	}
	public void init(MonitorConnector monitor, String serverAddress, int serverPort, long timeLimit)
			throws UnknownHostException, IOException {
		this.monitor = monitor;
		this.timeLimit = timeLimit;
		this.socket = new Socket(serverAddress, serverPort);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream(), true);
	}
	

	@Override
	public void run() {
		Move opponentMove = null;
		Move toExecute = null;
		Player myPlayer = null;
		boolean gameOver = false;
		while (!gameOver) {
			try {
				String response;
				while ((response = reader.readLine()) == null) {
				}
				System.out.println(response);
				if (response.startsWith(ServerMessage.WELCOME)) {
					myPlayer = parser.parsePlayer(response);
					monitor.setPLayer(myPlayer);
					if(myPlayer == Player.BLACK)
						monitor.setOpponentMove(null);
				} else if (response.startsWith(ServerMessage.MESSAGE)) {
					/* Logga il messaggio */
				} else if (response.startsWith(ServerMessage.OPPONENT_MOVE)) {
					opponentMove = parser.parseMove(response);
					monitor.setOpponentMove(opponentMove);
				} else if (response.startsWith(ServerMessage.YOUR_TURN)) {
					monitor.yourTurn();
					goToSleep(timeLimit);
					//System.out.println("Connector sveglio");
					monitor.setTimeOver();
					toExecute = monitor.getMove();
					System.out.println("Player: " + myPlayer + " Execute: " + toExecute.toString());
					writer.println(toExecute.toString());
				} else if (response.startsWith(ServerMessage.VALID_MOVE)) {
					/* Log valid Move */
				} else if (response.startsWith(ServerMessage.VICTORY)) {
					gameOver = true;
					/* Log victory */
				} else if (response.startsWith(ServerMessage.TIE)) {
					gameOver = true;
					/* Log tie */
				} else if (response.startsWith(ServerMessage.DEFEAT)) {
					gameOver = true;
					/* Log defeat */
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void goToSleep(long timeLimit) {
		try {
			Thread.sleep(timeLimit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
