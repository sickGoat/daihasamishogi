package board;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import board.implementation.Move;
import board.implementation.Pawn;
import board.implementation.Player;

public abstract class AbstractRespawnableBoardAdornment implements RespawnableBoardAdornment {
	
	protected RespawnableBoardAdornment son;
	
	public AbstractRespawnableBoardAdornment(RespawnableBoardAdornment son) {
		this.son = son;
	}

	@Override
	public List<Move> getAllLegalMoves(Player player) {
		if(son != null){
			return son.getAllLegalMoves(player);
		}
		return null;
	}
	
	@Override
	public Set<Move> getSortedMoves(Player player, Comparator<Move> comparator) {
		if(son!=null){
			return son.getSortedMoves(player, comparator);
		}
		else return null;
	}
	
	@Override
	public void respawn(List<Pawn> catches) {
		for (Pawn p : catches) {
			respawn(p);
		}
	}

	@Override
	public int getPawnCount(Player player) {
		if(son != null){
			return son.getPawnCount(player);
		}
		return -1;
	}
	
	/*
	 * Questa metodo viene implementato solo dal
	 * concrete board adornment 
	*/
	@Override
	public void setFCAdornment(FCAdornment fc) {
		
	}
	
}
