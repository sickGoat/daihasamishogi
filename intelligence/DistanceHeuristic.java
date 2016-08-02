package intelligence;

import board.TimedCompleteBoardManager;
import board.implementation.Player;
import util.GameInfo;

/**
 * Questa funzione restituisce un valore in termini di distanza dalla vittoria
 * di uno dei due giocatori
 * 
 *
 */
public class DistanceHeuristic extends StandardHeuristic {

	TimedCompleteBoardManager managertimed;
	
	public DistanceHeuristic(TimedCompleteBoardManager manager) {
		super(manager);
		managertimed = manager;
	}

	@Override
	public double evaluate() {
		int pb = managertimed.getPawnCount(Player.BLACK);
		int pw = managertimed.getPawnCount(Player.WHITE);
		int cb = (int)managertimed.getMaxConfigurationValue(Player.BLACK);
		int cw = (int)managertimed.getMaxConfigurationValue(Player.WHITE);
		int t = managertimed.getTime();
		int tmax = GameInfo.MAX_MOVES;
		double hw = 0;
		double hb = 0;
		// valori del white
		hw = (double) (pw - pb) / (pw - 1);
		hw += (double) cw / 5;
		// valori del black
		hb = (double) (pb - pw) / (pb - 1);
		hb += (double) cb / 5;
		// adesso aggiungo i valori relativi al tempo
		hw -= (double) (18 - pw) / 18 * (double) (t / tmax);
		hb -= (double) (18 - pb) / 18 * (double) (t / tmax);

		if(maximizerPlayer == Player.BLACK){
			return hb - hw;
		}
		
		return hw - hb;
	}
	
	@Override
	public void checkStrategy() {
		//esegue aggiustamenti sui pesi
	}
}
