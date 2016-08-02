package intelligence;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import board.CompleteBoardManager;
import board.implementation.Comparators.DefaultMoveComparator;
import board.implementation.Move;
import board.implementation.MoveFactory;
import board.implementation.Player;
import game.Broker;

public abstract class Alghoritm {

	protected Heuristic heuristic;

	protected CompleteBoardManager manager;

	protected Player myPlayer;

	protected Broker broker;
	
	protected DefaultMoveComparator comparator;

	protected static final double NEGATIVE_INFINITY = -Double.MAX_VALUE;

	protected static final double POSITIVE_INFINITY = Double.MAX_VALUE;

	public Alghoritm() {
	}

	public void init(CompleteBoardManager board, Broker broker, Heuristic heuristic) {
		this.broker = broker;
		this.manager = board;
		this.heuristic = heuristic;
		this.comparator = new DefaultMoveComparator();
	}

	protected double heuristicEvaluation() {
		return heuristic.evaluate();
	}

	public void setPlayer(Player p) {
		myPlayer = p;
		this.heuristic.setMaximizerPlayer(p);
	}
	
	
	public Heuristic getHeuristic(){return heuristic;}
	
	/**
	 * Execute mantiene tutte le mosse del primo livello, cioè quelle da inviare
	 * al server chiama al suo interno l'algoritmo vero e proprio che
	 * restituisce il valore da associare alla mossa al ritorno dal metodo
	 * verifico se il valore ottenuto e migliore dell'altro.
	 * 
	 * |ATTENZIONE| Questo metodo restituisce una mossa clonata perhé bisogna
	 * evitare eventuali modifiche del factory
	 */
	public Move execute(int depth) {
		Set<Move> firstLevelMoves = manager.getSortedMoves(myPlayer, comparator);
		// eventuali ordinamenti
		double bestValue = NEGATIVE_INFINITY;
		double value = 0;
		Move bestMove = null;
		Iterator<Move> it = firstLevelMoves.iterator();
		int count = 0;
		while (it.hasNext() && !broker.interrupt()) {
			Move move = it.next();
			manager.makeShadow(move);
			value = executeAlghoritm(depth, NEGATIVE_INFINITY, POSITIVE_INFINITY, myPlayer.getOpponent(),move);
			manager.backOneStep();
			if (value > bestValue) {
				count = depth;
				bestMove = move.clone();
				bestValue = value;
			}
		}
		System.out.println("Mossa trovata a profondita: "+count);
		MoveFactory.getInstance().invalidate(firstLevelMoves);
		// la mossa viene clonata in quanto è riferita dal Factory
		return bestMove==null?((TreeSet<Move>)firstLevelMoves).last().clone():
						bestMove;
	}

	protected abstract double executeAlghoritm(int depth, double alpha, double beta, Player player,Move parentMove);

}
