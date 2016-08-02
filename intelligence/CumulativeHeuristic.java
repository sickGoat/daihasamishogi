package intelligence;

import board.CompleteBoardManager;
import board.TimedCompleteBoardManager;
import board.implementation.Player;
import intelligence.strategies.CatchStrategy;
import intelligence.strategies.FCStrategy;
import intelligence.strategies.StrategyWeightType;
import util.GameInfo;

public class CumulativeHeuristic extends StandardHeuristic {
	
	
	public CumulativeHeuristic(CompleteBoardManager manager) {
		super(manager);
	}

	@Override
	public double evaluate() {
		
		double blackFc = manager.getMaxConfigurationValue(Player.BLACK);
		double whiteFc = manager.getMaxConfigurationValue(Player.WHITE);
		
		double pawnDifference = 0;
		double fcDifference = 0;
		
		
		if( maximizerPlayer == Player.WHITE){
			pawnDifference = manager.getPawnCount(Player.WHITE) - manager.getPawnCount(Player.BLACK);
			fcDifference = whiteFc - blackFc;
		}else{
			pawnDifference = manager.getPawnCount(Player.BLACK) - manager.getPawnCount(Player.WHITE);
			fcDifference = blackFc - whiteFc;  
		}
		
		return strategy.getWeight(StrategyWeightType.CATCH)*pawnDifference + 
				strategy.getWeight(StrategyWeightType.FINAL_CONFIGURATION)*fcDifference;
	}
	
	@Override
	public void checkStrategy() {
		int time = ((TimedCompleteBoardManager) manager).getTime();
		//se il 70% delle mosse è stato eseguito
		if( time >= GameInfo.MAX_MOVES/30*100){
			if( manager.getPawnCount(maximizerPlayer) > 
				manager.getPawnCount(maximizerPlayer.getOpponent())+8){
				strategy = new CatchStrategy();
			}
		}else if( manager.getMaxConfigurationValue(maximizerPlayer)>=4){
			strategy = new FCStrategy();
		}
		System.out.println("Strategy changed: "
				+ ((TimedCompleteBoardManager)manager).getTime()+" current Strategy: "
				+strategy.toString());
	}

	
}
