import java.io.IOException;
import java.net.UnknownHostException;

import board.CompleteBoardManager;
import board.TimedCompleteBoardManager;
import board.implementation.MoveFactory;
import board.implementation.TimedCompleteBoardManagerImpl;
import game.Broker;
import game.Connector;
import game.Monitor;
import intelligence.Alghoritm;
import intelligence.CumulativeHeuristic_v2;
import intelligence.MinMaxV2;
import util.GameInfo;

public class Starter {
	
	public static void main(String[] args) {
			GameInfo.initInfo(args);
			MoveFactory.getInstance();
			//TODO le game info
			CompleteBoardManager board = new TimedCompleteBoardManagerImpl();
			Monitor monitor = new Monitor();
			Connector connector = new Connector(); 
			Alghoritm alg = new MinMaxV2();
			
			Broker broker = new Broker();
			broker.init(monitor, alg, board);
			try {
				connector.init(monitor, GameInfo.SERVER, GameInfo.SERVER_PORT,GameInfo.TIME_OUT);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			alg.init(board, broker, new CumulativeHeuristic_v2((TimedCompleteBoardManager) board));
			
			Thread t1 = new Thread(connector);
			Thread t2 = new Thread(broker);
			// il broker vive per servire il connetctor
			t2.setDaemon(true);
			t1.start();
			t2.start();
			
	  } 
}
