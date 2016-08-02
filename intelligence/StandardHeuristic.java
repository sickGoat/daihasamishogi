package intelligence;

import board.CompleteBoardManager;
import board.implementation.Player;
import intelligence.strategies.DefaultStrategy;
import intelligence.strategies.Strategy;

public abstract class StandardHeuristic implements Heuristic {

	protected CompleteBoardManager manager;
	
	protected Strategy strategy;
	
	protected Player maximizerPlayer;
	
	@Override
	public void setMaximizerPlayer(Player p) {
		this.maximizerPlayer = p;
	}

	public StandardHeuristic(CompleteBoardManager manager) {
		this.manager = manager;
		strategy = new DefaultStrategy();
	}
	
	
}
