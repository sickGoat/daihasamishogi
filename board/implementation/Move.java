package board.implementation;

import java.util.ArrayList;
import java.util.List;

public class Move implements Cloneable{
	
	public enum MoveType{ GOAL,
						  DANGER,
						  CATCH, 
						  FC_BUILD,
						  QUIET
						};
	
	protected Cell from;
	
	protected Cell to;
	
	//default la mossa è quiet
	public MoveType type = MoveType.QUIET;
	
	protected byte catchesCount;
	
	protected List<Pawn> pawnCaught = new ArrayList<>(7);
	
	public byte fcBuildRank = 0;
	
	protected boolean reversed;

	public Move(){}
	
	/**
	 * Metodo restituisce il numero di caselle attraversato
	 * dalla mossa, prendo il massimo tra asse x e asse y
	 * @return
	 */
	public int getCoveredDistance(){
		int xDistance = this.from.number.ordinal()-this.to.number.ordinal();
		int yDistance = this.from.letter.ordinal()-this.to.letter.ordinal();
		
		return Integer.max(Math.abs(xDistance), Math.abs(yDistance));
	}
	
	
	public boolean isGoal(){
		return this.type == MoveType.GOAL;
	}
	
	public boolean isDanger(){
		return this.type == MoveType.DANGER;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MOVE "+from.toString()+""+to.toString();
	}
	
	public Move clone(){
		Move res = null;
		try {
			res = (Move) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return res;
	}
}
