package game;

import board.CompleteBoardManager;
import board.implementation.Move;
import board.implementation.Player;
import intelligence.Alghoritm;

public class Broker implements Runnable {
	
	private CompleteBoardManager manager;
	
	private Alghoritm alghoritm;
	
	private MonitorBroker monitor;
	
	private Move lastMove;
	
	public Broker(){
	}
	
	public void init(Monitor monitor,Alghoritm alghoritm,CompleteBoardManager manager) {
		this.monitor = monitor;
		this.alghoritm = alghoritm;
		this.manager = manager;
		
	}
	
	@Override
	public void run() {
		Player myPlayer = monitor.getPLayer();
		alghoritm.setPlayer(myPlayer);
		Move opponentMove = null;
		int count = 0;
		while(true){
			opponentMove = monitor.getOpponentMove();
			if( opponentMove != null ){
				manager.make(opponentMove);
			}
			monitor.myTurn();
			int depth = 0;
			Move currentMove = null;
			while(!monitor.isTimeOver()){
				/*Iterative Deepening*/
				currentMove = alghoritm.execute(depth);
				if(!interrupt()){
					count = depth;
					monitor.setMove(currentMove);
					lastMove = currentMove;
				}else{
					System.out.printf("BROKER:: Livello raggiunto: %d\n",count);
					break;
				}
				depth++;
			}//esaurito tempo per la ricerca
			
			/*Faccio il rollback di tutte le shadow moves*/
			manager.returnToTop();
			alghoritm.getHeuristic().checkStrategy();
			/*Applico la mossa calcolata*/
			manager.make(lastMove);
		}
	}
	
	public boolean interrupt(){
		return monitor.isTimeOver();
	}
	
}
