package board.implementation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import board.implementation.Move.MoveType;

/**
 * Singleton
 * 
 * @author antonio & Valerio da ricchia
 *
 */
public class MoveFactory {

	private class Invalidator implements Runnable {

		@Override
		public void run() {
			Collection<Move> moves = null;
			while (!Thread.currentThread().isInterrupted()) {
				l.lock();
				try {
					if (toInvalidate.size() == 0) {
						invalidatorSleeping = true;
						invalidatorBed.await();
						invalidatorSleeping = false;
					}
					moves = toInvalidate.remove(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					l.unlock();
				}
			} // while del metodo
			Iterator<Move> it = moves.iterator();
			while (it.hasNext()) {
				try {
					l.lock();
					pool.add(it.next());
				} finally {
					l.unlock();
				}
			}
		}
	}

	private static final int POOL_INITIAL_SIZE = 1000;

	private Lock l;
	private Condition invalidatorBed;
	private boolean invalidatorSleeping = false;
	private List<Collection<Move>> toInvalidate;
	private List<Move> pool;
	// singleton
	private static MoveFactory manager;

	private MoveFactory() {
		l = new ReentrantLock();
		invalidatorBed = l.newCondition();
		pool = new LinkedList<>();
		toInvalidate = new LinkedList<>();
		while (pool.size() != POOL_INITIAL_SIZE) {
			pool.add(new Move());
		}
		Thread t = new Thread(new Invalidator());
		t.setDaemon(true);
		t.start();
	}

	protected Move create(Cell from, Cell to) {
		Move move = null;
		try {
			l.lock();
			if (pool.isEmpty()) {
				move = new Move();
			} else {
				move = pool.remove(0);
			}
		} finally {
			l.unlock();
		}
		move.from = from;
		move.to = to;
		move.catchesCount = 0;
		move.fcBuildRank = 0;
		move.pawnCaught.clear();
		move.type = MoveType.QUIET;
		move.reversed = false;
		return move;
	}

	protected Move getReverse(Move m) {
		Move res = this.create(m.to, m.from);
		res.reversed = true;
		return res;
	}

	public void invalidate(Move m) {
		try {
			l.lock();
			pool.add(m);
		} finally {
			l.unlock();
		}
	}

	public void invalidate(Collection<Move> moves) {
		try {
			l.lock();
			toInvalidate.add(moves);
			if (invalidatorSleeping) {
				invalidatorBed.signalAll();
			}
		} finally {
			l.unlock();
		}
	}

	public Move create(String cells) {
		Cell from = new Cell();
		Cell to = new Cell();
		Letter letter = convertLetter(cells.charAt(0));
		Number number = convertNumber(cells.charAt(1));
		from.letter = letter;
		from.number = number;

		letter = convertLetter(cells.charAt(2));
		number = convertNumber(cells.charAt(3));
		to.letter = letter;
		to.number = number;
		Move move = this.create(from, to);
		move.from = from;
		move.to = to;
		move.catchesCount = 0;
		move.reversed = false;

		return move;
	}

	public static MoveFactory getInstance() {
		if (manager == null) {
			manager = new MoveFactory();
		}
		return manager;
	}

	private static Letter convertLetter(char letter) {
		letter = Character.toLowerCase(letter);
		switch (letter) {
		case 'a':
			return Letter.A;
		case 'b':
			return Letter.B;
		case 'c':
			return Letter.C;
		case 'd':
			return Letter.D;
		case 'e':
			return Letter.E;
		case 'f':
			return Letter.F;
		case 'g':
			return Letter.G;
		case 'h':
			return Letter.H;
		case 'i':
			return Letter.I;
		default:
			throw new RuntimeException("Lettera Sbagliata "+letter);
		}
	}

	private static Number convertNumber(char number) {
		switch (number) {
		case '1':
			return Number.ONE;
		case '2':
			return Number.TWO;
		case '3':
			return Number.THREE;
		case '4':
			return Number.FOUR;
		case '5':
			return Number.FIVE;
		case '6':
			return Number.SIX;
		case '7':
			return Number.SEVEN;
		case '8':
			return Number.EIGHT;
		case '9':
			return Number.NINE;
		default:
			throw new RuntimeException("NumerSbagliato");
		}
	}
}
