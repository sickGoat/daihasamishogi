package board.implementation;

import java.util.Comparator;

import board.implementation.Move.MoveType;

public class Comparators {
	
	
	/**
	 * Ordina le mosse nell'ordine
	 * GOAL,CATCH,FC_BUILD,QUITE
	 * @author antonio
	 *
	 */
	public static class DefaultMoveComparator implements Comparator<Move>{

		@Override
		public int compare(Move o1, Move o2) {
			
			int res = Integer.compare(o1.type.ordinal(), o2.type.ordinal());
			//stesso type
			if(res == 0){
				int compare;
				if(o1.type == MoveType.GOAL)
					return -1;
				
				if( o1.type == MoveType.DANGER)
					return -1;

				if(o1.type == MoveType.CATCH){
					compare = Byte.compare(o2.catchesCount, o1.catchesCount);
					return compare != 0?compare:-1;
				}
				if(o1.type == MoveType.FC_BUILD){
					compare = Byte.compare(o2.fcBuildRank, o1.fcBuildRank);
					return compare != 0?compare:-1;
				}
					
				
				//se sono entrambi quiet l'ordine non mi interessa
				return -1;
			}
			
			return res;
		}
		
	}
}
