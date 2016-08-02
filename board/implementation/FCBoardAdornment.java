package board.implementation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import board.AbstractRespawnableBoardAdornment;
import board.FCAdornment;
import board.RespawnableBoardAdornment;

public class FCBoardAdornment extends AbstractRespawnableBoardAdornment implements FCAdornment {

	// I tipi di link da fare per le final configuration
	private enum LinkType {
		ONLY_VERTICAL, RIGHT, LEFT
	}

	// Per ordinare le fc. Questo dipende dal gocatore
	private class FinalConfigurationComparator implements Comparator<FinalConfiguration> {

		// moltiplicatore
		private int m;

		private FinalConfigurationComparator(Player p) {
			m = p.getPlayerValue();
		}

		@Override
		public int compare(FinalConfiguration o1, FinalConfiguration o2) {
			int res = 0;
			// comparo prima il valore se è uguale ordino sull'id
			res = m * Double.compare(o1.value, o2.value);
			// questo ci vuole perché il sorted set elimina i duplicati quindi
			// non posso restituire zero
			if (res == 0)
				res = Byte.compare(o1.id, o2.id);
			return res;
		}

	}

	// serve per salvare solo le configurazioni interessanti e dimenticarsi
	// delle altre
	private final byte THRESHOLD = 2;
	protected Map<Cell, List<FinalConfiguration>> fcmap = new HashMap<>();
	// mantiene tutte le final configuration interessanti per un giocatore
	private SortedSet<FinalConfiguration> blackObserved = new TreeSet<>(new FinalConfigurationComparator(Player.BLACK));
	private SortedSet<FinalConfiguration> whiteObserved = new TreeSet<>(new FinalConfigurationComparator(Player.WHITE));

	public FCBoardAdornment(RespawnableBoardAdornment son) {
		super(son);
		init();
	}

	@Override
	public void respawn(Pawn pawn) {
		updateFinalConfigurations(pawn.position, false, pawn.owner);
		son.respawn(pawn);
	}

	@Override
	public List<Pawn> make(Move m) {
		List<Pawn> catches = son.make(m);
		// le celle potrebbero aver riferimento a celle strane
		Cell from = Board.getInstance().fromBoard(m.from.letter, m.from.number);
		Cell to = Board.getInstance().fromBoard(m.to.letter, m.to.number);
		Player pType = to.occupied.owner;
		updateFinalConfigurations(from, true, pType);
		updateFinalConfigurations(to, false, pType);
		// Le catch porca puttana
		if (catches != null) {
			for (Pawn p : catches) {
				updateFinalConfigurations(p.position, true, p.owner);
			}
		}
		return catches;
	}

	@Override
	public double getMaxConfiguration(Player player) {
		SortedSet<FinalConfiguration> ref = (player == Player.BLACK) ? blackObserved : whiteObserved;
		double res = 0;
		if (!ref.isEmpty()) {
			// il massimo è alla fine
			// puttà SortedSet ha last()!
			res = ref.last().value;
		}
		return res * player.getPlayerValue();
	}
	
	
	@Override
	public ScorePair getCumulativeMax(Player player) {
		SortedSet<FinalConfiguration> ref = (player == Player.BLACK) ? blackObserved : whiteObserved;
		final double res;
		int mul = player.getPlayerValue();
		int count = 0;
		int otherSum = 0;
		if (!ref.isEmpty()) {
			res = ref.last().value*mul;
			count = (int) ref.stream().filter(fc -> fc.value*mul == res ).count();
			//se la massima configurazione è maggiore di due
			//faccio una ricerca più esaustiva
			if(res>2){
				otherSum = (int) ref.stream().filter(fc -> fc.value*mul < res)
						.mapToDouble(FinalConfiguration::getValue).sum()*mul;
			}else{
				otherSum = 0;
			}
		}else{
			res = 0;
		}
		
		return new ScorePair(res,count,otherSum);
	}
	
	/**
	 * Metodo restituisce il numero di final configurations passanti per
	 * la cella passata come parametro, eseguo il controllo
	 * sul player perche ancora non so se usare il playertype o meno
	 */
	@Override
	public int getNumberOfFinalConfiguration(Cell c,Player playerType) {
		int count = 0;
		if(playerType != null){
			count = (int) fcmap.get(c).stream().filter(fc -> fc.homePlayer!=playerType || 
					fc.homePlayer == null).count();
		}else{
			count = (int) fcmap.get(c).stream().count();
		}
		return count;
	}

	@Override
	public int getEnemyDistance(Move m) {
		return son.getEnemyDistance(m);
	}
	


	// Metodo che gestisce le final configurations
	private void updateFinalConfigurations(Cell cell, boolean emptyCell, Player playerType) {
		// Incremento da dare alle fc
		//byte incP = (byte) ((emptyCell ? -1 : 1) * playerType.getPlayerValue());
		//incremento avversario
		//byte incA = (byte) ((emptyCell ? -1 : 1) * playerType.getPlayerValue());
		// FC da lavorare
		List<FinalConfiguration> fcs = fcmap.get(cell);
		for (FinalConfiguration fc : fcs) {
			// prima cosa di tutto devo rimuovere tutte fc osservate
			if (Math.abs(fc.value) >= THRESHOLD) {
				if (Math.signum(fc.value) == Player.BLACK.getPlayerValue()) {
					blackObserved.remove(fc);
				} else {
					whiteObserved.remove(fc);
				}
			}
			// incremento il valore della fc
			fc.update(emptyCell,playerType);
			// vedo se è il caso di aggiungerla a quelle osservate
			if (Math.abs(fc.value) >= THRESHOLD) {
				if (Math.signum(fc.value) == Player.BLACK.getPlayerValue() && fc.homePlayer != Player.BLACK) {
					blackObserved.add(fc);
				} else if (Math.signum(fc.value) == Player.WHITE.getPlayerValue() && fc.homePlayer != Player.WHITE) {
					whiteObserved.add(fc);
				}
			}
		}
	}

	private void init() {
		// mi carico la board
		Board board = Board.getInstance();
		FinalConfiguration diagonal = null;
		FinalConfiguration vertical = null;
		// riga
		int i = 0;
		// colonna
		int j = 0;
		// partiamo con quelle che vanno a destra
		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				Cell tmp = board.fromBoard(i, j);
				diagonal = new LeftFinalConfiguration();
				vertical = new VerticalFinalConfiguration();
				if (i < 2) {
					// nero
					diagonal.homePlayer = Player.BLACK;
					vertical.homePlayer = Player.BLACK;
					diagonal.value = (byte) ((2 - i) * (-1));
					vertical.value = (byte) ((2 - i) * (-1));
					diagonal.blackCount = -2;
					vertical.blackCount = -2;
				} else if (i > 2) {
					// bianco
					diagonal.homePlayer = Player.WHITE;
					vertical.homePlayer = Player.WHITE;
					diagonal.value = (byte) (i - 2);
					vertical.value = (byte) (i - 2);
					diagonal.whiteCount = 2;
					vertical.whiteCount = 2;
				}
				putInMap(tmp, diagonal);
				putInMap(tmp, vertical);
				linkInMap(tmp, LinkType.LEFT, diagonal);
				linkInMap(tmp, LinkType.ONLY_VERTICAL, vertical);
			} // for2
		} // for1
			// ora con quelle che partono da sinistra
		for (i = 0; i < 5; i++) {
			for (j = 8; j > 3; j--) {
				Cell tmp = board.fromBoard(i, j);
				diagonal = new RightFinalConfiguration();
				if (j > 4) {
					vertical = new VerticalFinalConfiguration();
				}
				if (i < 2) {
					// nero
					diagonal.homePlayer = Player.BLACK;
					vertical.homePlayer = Player.BLACK;
					diagonal.value = (byte) ((2 - i) * (-1));
					vertical.value = (byte) ((2 - i) * (-1));
					diagonal.blackCount = -2;
					vertical.blackCount = -2;
				} else if (i > 2) {
					// bianco
					diagonal.homePlayer = Player.WHITE;
					vertical.homePlayer = Player.WHITE;
					diagonal.value = (byte) ((i - 2));
					vertical.value = (byte) ((i - 2));
					diagonal.whiteCount = 2;
					vertical.blackCount = 2;
				}
				putInMap(tmp, diagonal);
				linkInMap(tmp, LinkType.RIGHT, diagonal);
				if (j > 4) {
					putInMap(tmp, vertical);
					linkInMap(tmp, LinkType.ONLY_VERTICAL, vertical);
				}

			}
		}
	}

	private void putInMap(Cell c, FinalConfiguration fc) {
		List<FinalConfiguration> ref = fcmap.get(c);
		if (ref == null) {
			ref = new LinkedList<>();
			fcmap.put(c, ref);
		}
		ref.add(fc);
	}

	private void linkInMap(Cell c, LinkType lt, FinalConfiguration fc) {
		int dj = 0;
		int count = 1;
		switch (lt) {
		case ONLY_VERTICAL:
			break;
		case RIGHT:
			dj = -1;
			break;
		case LEFT:
			dj = 1;
			break;
		}
		while (count < 5) {
			Cell toLink = Board.getInstance().fromBoard(c.letter.ordinal() + count, c.number.ordinal() + dj * count);
			putInMap(toLink, fc);
			count++;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n" + son.toString());
		builder.append("\n W: " + whiteObserved.toString());
		builder.append("\n B: " + blackObserved.toString());
		return builder.toString();
	}

	@Override
	public int getMaxFinalConfiguration(Cell from, Cell to, Player player) {
		// TODO Auto-generated method stub
		return 0;
	}
}
