package board;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import board.implementation.Move;
import board.implementation.Player;

public interface BoardManager {
	
	List<Move> getAllLegalMoves(Player player);
	
	Set<Move> getSortedMoves(Player player,Comparator<Move> comparator);
	
	void make(Move move);
	
	int getPawnCount(Player player);
	
	boolean isGameOver();
	
	int getEnemyDistance(Move m);

}
