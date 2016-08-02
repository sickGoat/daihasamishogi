package board;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import board.implementation.Move;
import board.implementation.Pawn;
import board.implementation.Player;

public interface BoardAdornment {
	
	List<Pawn> make(Move m);
	
	List<Move> getAllLegalMoves(Player player);
	
	Set<Move> getSortedMoves(Player player,Comparator<Move> comparator);
	
	int getPawnCount(Player player);
	
	int getEnemyDistance(Move m);
	
	void setFCAdornment(FCAdornment fc);
}
