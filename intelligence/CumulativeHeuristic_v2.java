package intelligence;

import board.CompleteBoardManager;
import board.TimedCompleteBoardManager;
import board.implementation.Player;
import intelligence.strategies.CatchStrategy;
import intelligence.strategies.DefaultStrategy;
import intelligence.strategies.FCStrategy;
import intelligence.strategies.StrategyWeightType;
import util.GameInfo;

public class CumulativeHeuristic_v2 extends StandardHeuristic{

	public CumulativeHeuristic_v2(CompleteBoardManager manager) {
		super(manager);
	}

	@Override
	public double evaluate() {
		double blackFc = manager.getMaxConfigurationValue(Player.BLACK);
		double whiteFc = manager.getMaxConfigurationValue(Player.WHITE);
		
		double pawnDifference = 0;
		double fcValue = 0;
		
		
		if( maximizerPlayer == Player.WHITE){
			pawnDifference = manager.getPawnCount(Player.WHITE) - manager.getPawnCount(Player.BLACK);
			fcValue = blackFc>whiteFc?-blackFc:whiteFc;
		}else{
			pawnDifference = manager.getPawnCount(Player.BLACK) - manager.getPawnCount(Player.WHITE);
			fcValue = whiteFc>blackFc?-whiteFc:blackFc;
		}
		
		return strategy.getWeight(StrategyWeightType.CATCH)*pawnDifference + 
				strategy.getWeight(StrategyWeightType.FINAL_CONFIGURATION)*fcValue;
	}

	@Override
	public void checkStrategy() {
		int time = ((TimedCompleteBoardManager) manager).getTime();
		//se il 70% delle mosse è stato eseguito
		if( time >= GameInfo.MAX_MOVES/24*100){
			if(manager.getPawnCount(maximizerPlayer)<
					manager.getPawnCount(maximizerPlayer.getOpponent())-2){
				//andiamo di catch
				strategy = new CatchStrategy();
			}else if(manager.getMaxConfigurationValue(maximizerPlayer)>=3){
				strategy = new FCStrategy();
			}
		}else if( time>= GameInfo.MAX_MOVES/42*100 
				&& time <= GameInfo.MAX_MOVES/65*100){
			if(manager.getPawnCount(maximizerPlayer)>
					manager.getPawnCount(maximizerPlayer.getOpponent())+4){
				strategy = new CatchStrategy();
			}else{
				strategy = new FCStrategy();
			}
		}else if( time >= GameInfo.MAX_MOVES/83*100
				&& time <= GameInfo.MAX_MOVES/91*100){
			if(manager.getPawnCount(maximizerPlayer)>
					manager.getPawnCount(maximizerPlayer.getOpponent())+4){
				strategy = new CatchStrategy();
			}else if(manager.getMaxConfigurationValue(maximizerPlayer)>=3){
				strategy = new FCStrategy();
			}else{
				 strategy = new DefaultStrategy();
			}	
		}else{
			if(manager.getPawnCount(maximizerPlayer)<5){
				//con coltello trai denti
				strategy = new CatchStrategy();
			}else{
				strategy = new DefaultStrategy();
			}
			
		}
			
		System.out.println("Time:"+time+" current Strategy: "+strategy.toString());
	}

}
