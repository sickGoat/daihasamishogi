package board;

import board.implementation.Cell;
import board.implementation.Player;

public interface FCAdornment extends RespawnableBoardAdornment {
	
	double getMaxConfiguration(Player player);
	
	ScorePair getCumulativeMax(Player player);
	
	int getNumberOfFinalConfiguration(Cell c,Player playerType);
	
	int getMaxFinalConfiguration(Cell from,Cell to,Player player);

	
	/**
	 * Classe che mantiene il valore
	 * della massima configurazione finale
	 * e il numero di configurazioni con 
	 * l'equivalente punteggio
	 * @author antonio
	 *
	 */
	public static class ScorePair {
		
		public double fcValue;
		
		public int numberOf;
		
		//contiene la somma delle configurazioni non massime
		public int otherSum;
		
		public ScorePair(double res,int numberOf,int otherSum){
			this.fcValue = res;
			this.numberOf = numberOf;
			this.otherSum = otherSum;
		}
		
		/**
		 * Flatta il valore
		 * @return
		 */
		public double flatten(){
			return fcValue*numberOf+otherSum;
		}
		
		
		@Override
		public String toString() {
			return "Score fc: "+this.fcValue + " numberOf: "+this.numberOf +" otherSum: "+this.otherSum;
		}
	}
}
