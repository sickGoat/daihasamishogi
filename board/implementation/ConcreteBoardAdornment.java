package board.implementation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import board.AbstractRespawnableBoardAdornment;
import board.FCAdornment;
import board.RespawnableBoardAdornment;
import board.implementation.Move.MoveType;

public class ConcreteBoardAdornment extends AbstractRespawnableBoardAdornment {
	
	private static enum Direction {
		RIGHT, LEFT, UP, DOWN;

		public static Direction getDirection(Cell from, Cell to) {
			int diffOriz = from.letter.ordinal() - to.letter.ordinal();
			int diffVert = from.number.ordinal() - to.number.ordinal();
			Direction res = null;
			if (diffOriz == 0) {
				if (diffVert > 0) {
					res = Direction.RIGHT;
				} else {
					res = Direction.LEFT;
				}
			} else {
				if (diffOriz > 0) {
					res = Direction.UP;
				} else {
					res = Direction.DOWN;
				}
			}
			return res;
		}
		
		public static Direction getReverseDirection(Direction currentDirection){
			Direction res = null;
			switch(currentDirection){
				case RIGHT:
					res = LEFT;
					break;
				case LEFT:
					res = RIGHT;
					break;
				case UP:
					res = DOWN;
					break;
				case DOWN:
					res = UP;
					break;
				default:
					assert false;
			}
			return res;
		}
	}

	Board board = Board.getInstance();
	
	protected FCAdornment fcAdornment;

	/**
	 * Costruttore nascosto. Bisogna utilizzare l'altro costruttore
	 * 
	 * @param son
	 */
	private ConcreteBoardAdornment(RespawnableBoardAdornment son) {
		super(null);
	}

	protected ConcreteBoardAdornment() {
		super(null);
	}
	
	
	@Override
	public void setFCAdornment(FCAdornment fc) {
		this.fcAdornment = fc;
	}
	
	@Override
	public void respawn(Pawn pawn) {
		pawn.position.occupied = pawn;
		if (pawn.owner == Player.WHITE) {
			board.whitePawns.add(pawn);
		} else {
			board.blackPawns.add(pawn);
		}
	}
	
	@Override
	public int getEnemyDistance(Move m) {
		//prendo il player dalla cella from
		Cell tmp = board.fromBoard(m.from.letter, m.from.number);
		Player player = tmp.occupied.owner;
		int distance = getMinEnemyDistance(m.to,player.getOpponent());
		
		return distance;
	}
	
	@Override
	public List<Pawn> make(Move m) {
		// Poiché m potrebbe avere celle diverse da quelle contenute nella board
		// me le devo caricare
		Cell to = board.fromBoard(m.to.letter, m.to.number);
		Cell from = board.fromBoard(m.from.letter, m.from.number);
		// scambio il riferimento alla pawn da from a to
		to.occupied = from.occupied;
		// setto il riferimento alla cella alla pawn
		to.occupied.position = to;
		// tolgo il riferimento alla pawn nella from
		from.occupied = null;
		if (m.reversed) {
			return null;
		}
		// applico le regole solo alle mosse non reversed altrimenti potrebbero
		// generarmi delle catches
		List<Pawn> res = applyCatches(from, to);
		// setto il count
		m.catchesCount = (byte) res.size();
		// restituisco le catches
		return res;
	}

	@Override
	public List<Move> getAllLegalMoves(Player player) {
		List<Move> res = new LinkedList<>();
		Set<Pawn> toSearch = null;
		if (player == Player.WHITE) {
			toSearch = board.whitePawns;
		} else {
			toSearch = board.blackPawns;
		}
		for (Pawn p : toSearch) {
			res.addAll(generateLegalMoves(p));
		}
		return res;
	}
	
	
	@Override
	public Set<Move> getSortedMoves(Player player, Comparator<Move> comparator) {
		Set<Move> res = new TreeSet<>(comparator);
		Set<Pawn> toSearch = null;
		if (player == Player.WHITE) {
			toSearch = board.whitePawns;
		} else {
			toSearch = board.blackPawns;
		}
		for (Pawn p : toSearch) {
			res.addAll(generateLegalMoves(p));
		}
		return res;
	}
	
	@Override
	public int getPawnCount(Player player) {
		if (player == Player.WHITE) {
			return board.whitePawns.size();
		}
		return board.blackPawns.size();
	}

	

	/**
	 * Applica le regole per mangiare le pedine
	 * 
	 * @return
	 */
	private List<Pawn> applyCatches(Cell from, Cell to) {
		LinkedList<Pawn> res = new LinkedList<>();
		Direction curr = Direction.getDirection(from, to);
		//direzione inversa
		Direction revCurr = Direction.getReverseDirection(curr);
		// riga
		int i = 0;
		// colonna
		int j = 0;
		// quante ne ho mangiate fino ad ora nell'ultima direzione
		int lastCatches = 0;
		// incremento di i
		int iInc = 0;
		// incremento di j
		int jInc = 0;
		// cella temporanea per il controllo della pawn
		Cell tmp = null;
		// ci vogliono 3 for per ogni direzione tranne quella contraria al movimento
		for (int k = 0; k < 4; k++) {
			i = to.letter.ordinal();
			j = to.number.ordinal();
			switch (k) {
			case 0:// destra
				if (revCurr.ordinal() != k) {
					jInc = -1;
					iInc = 0;
					j--;
				}
				break;
			case 1:// sinistra
				if (revCurr.ordinal() != k) {
					jInc = 1;
					iInc = 0;
					j++;
				}
				break;
			case 2:// sopra
				if (revCurr.ordinal() != k) {
					jInc = 0;
					iInc = -1;
					i--;
				}
				break;
			case 3:// sotto
				if (revCurr.ordinal() != k) {
					jInc = 0;
					iInc = 1;
					i++;
				}
				break;
			}
			if (revCurr.ordinal() != k) {
				for (; i <= 8 && i >= 0 && j <= 8 && j >= 0; i += iInc, j += jInc) {

					tmp = board.fromBoard(i, j);
					// caso di cella vuota
					if (tmp.occupied == null) {
						break;
					} else if (tmp.occupied.owner != to.occupied.owner) {
						res.add(tmp.occupied);
					} else {
						lastCatches = res.size();
						break;
					}
				} // for2
				if (lastCatches != res.size()) {
					while (res.size() > lastCatches) {
						res.removeLast();
					}
				}
			}
		} // for1
		// tolgo il riferimento alle pawn mangiate
		Set<Pawn> ref = null;
		if (!res.isEmpty()) {
			Pawn p = res.get(0);
			if (p.owner == Player.WHITE) {
				ref = board.whitePawns;
			} else {
				ref = board.blackPawns;
			}
			for (Pawn p1 : res) {
				p1.position.occupied = null;
				ref.remove(p1);
			}
		}
		return res;
	}

	
	
	
	/**
	 * Genera tutte le mosse per una singola pedina
	 * 
	 * @param p
	 * @return
	 */
	private List<Move> generateLegalMoves(Pawn p) {
		List<Move> res = new LinkedList<>();
		Move mv;
		Player pType = p.owner;
		boolean canJump = false;
		MoveFactory factory = MoveFactory.getInstance();
		// riga
		int i = 0;
		// colonna
		int j = 0;
		// incremento di i
		int iInc = 0;
		// incremento di j
		int jInc = 0;
		// cella temporanea per il controllo della pawn
		Cell tmp = null;
		// ci vogliono 3 for per ogni direzione tranne quella di movimento
		for (int k = 0; k < 4; k++) {
			i = p.position.letter.ordinal();
			j = p.position.number.ordinal();
			switch (k) {
			case 0:// destra
				jInc = -1;
				iInc = 0;
				j--;
				break;
			case 1:// sinistra
				jInc = 1;
				iInc = 0;
				j++;
				break;
			case 2:// sopra
				jInc = 0;
				iInc = -1;
				i--;
				break;
			case 3:// sotto
				jInc = 0;
				iInc = 1;
				i++;
				break;
			}
			for (; i <= 8 && i >= 0 && j <= 8 && j >= 0; i += iInc, j += jInc) {
				tmp = board.fromBoard(i, j);
				if (tmp.occupied == null) {
					mv = factory.create(p.position,tmp);
					//setto il tipo di mossa
					setType(mv, pType);
					res.add(mv);
					if (canJump) {
						break;
					}
				} else {
					int d = distance(p.position, tmp);
					if (d == 1) {
						canJump = true;
					} else {
						break;
					}
				}
			} // for2
			canJump = false;
		} // for1
		return res;
	}

	private int distance(Cell from, Cell to) {
		int distance;
		if (from.letter.ordinal() == to.letter.ordinal()) {
			distance = from.number.ordinal() - to.number.ordinal();
		} else {
			distance = from.letter.ordinal() - to.letter.ordinal();
		}
		return Math.abs(distance);
	}
	
	/**
	 * Prende una mossa e calcola ricava il tipo
	 * di mossa
	 * @param m
	 * @param pType
	 */
	private void setType(Move m,Player pType){
		if(isGoal(m,pType)){
			m.type = MoveType.GOAL;
			return;
		}
		if(isDanger(m, pType)){
			m.type = MoveType.DANGER;
			return;
		}
		
		if(isCatch(m,pType)){
			m.type = MoveType.CATCH;
			return;
		}
		if( isFCBuild(m,pType)){
			m.type = MoveType.FC_BUILD;
			return;
		}
		m.type = MoveType.QUIET;
	}
	
	
	private boolean isCatch(Move m,Player pType){
		//starting cell
		Cell tmp;
		Direction curDir = Direction.getDirection(m.from, m.to);
		Direction revDir = Direction.getReverseDirection(curDir);
		boolean canCatch = false;
		int i=0,j=0,iInc=0,jInc=0,catchesCount=0;
		for(int k=0;k<4;k++){
			canCatch = false;
			i = m.to.letter.ordinal();
			j = m.to.number.ordinal();
			if(k != revDir.ordinal()){
				switch (k) {
				case 0://destra
					jInc = -1;
					iInc = 0;
					j--;
					break;
				case 1://sinistra
					jInc = 1;
					iInc = 0;
					j++;
					break;
				case 2://sopra
					jInc = 0;
					iInc = -1;
					i--;
					break;
				case 3://sotto
					jInc = 0;
					iInc = 1;
					i++;
					break;
				}
				
				for(; i>=0 && i <= 8 && j>=0 && j<=8 ; j+=jInc,i+=iInc){
					tmp = board.fromBoard(i, j);
					//cella occupata dall'avversario
					if(tmp.occupied == null){
						catchesCount = 0;
						break;
					}else if(tmp.occupied.owner == pType.getOpponent()){
						catchesCount++;
					}else if( tmp.occupied.owner == pType){
						canCatch = catchesCount>0;
						break;
					}
				}//for2
				
				if(canCatch)
					m.catchesCount += catchesCount;
			}//if
		}//for
		
		return m.catchesCount>0;
	}
	
	private boolean isGoal(Move m,Player pType){
		int res = fcAdornment.getMaxFinalConfiguration(m.from,m.to, pType);
		if(res == 5){
			m.fcBuildRank = (byte)res;
			return true;
		}
		
		return false;
	}
	

	private boolean isDanger(Move m,Player pType){
		int res = fcAdornment.getMaxFinalConfiguration(m.from,m.to,pType.getOpponent());
		if(res > 3){
			return true;
		}
		return false;
	}
	
	public boolean isFCBuild(Move m,Player pType){
		//prendo la massima fc per la cella to
		if(isHome(m.to,pType))
			return false;
		int res = fcAdornment.getMaxFinalConfiguration(m.from, m.to, pType);
		if(res>1){
			m.fcBuildRank = (byte)res;
			return true;
		}
		
		return false;
	}
	
	private boolean isHome(Cell c,Player p){
		if(p == Player.WHITE && (c.letter == Letter.H || c.letter == Letter.I)){
			return true;
		}else if(p == Player.BLACK && (c.letter == Letter.A || c.letter == Letter.B)){
			return true;
		}
		return false;
	}
	
	/**
	 * Restituisce la minima distanza di un giocatore
	 * di tipo p dalla cella c
	 * @param c
	 * @param p
	 * @return
	 */
	private int getMinEnemyDistance(Cell c,Player p){
		int minDistance = Integer.MAX_VALUE;
		int curDistance = 0;
		for(int i = 0 ; i < Direction.values().length;i++){
			curDistance = searchAlongDirection(c,Direction.values()[i],p);
			if(curDistance >= 0 && curDistance < minDistance){
				minDistance = curDistance;
				if(minDistance == 1)
					break;
			}
			
		}

		return minDistance;
	}
	
	private int searchAlongDirection(Cell from,Direction d,Player p){
		int jInc = 0;
		int iInc = 0;
		switch(d){
		case RIGHT:
			jInc = -1;
			break;
		case LEFT:
			jInc = +1;
			break;
		case UP:
			iInc = -1;
			break;
		case DOWN:
			iInc = +1;
			break;
		}
		
		int i = from.letter.ordinal();
		int j = from.number.ordinal();
		int count = 1;
		int rowIndex = i+iInc*count;
		int colIndex = j+jInc*count;
		Cell tmp;
		while( rowIndex >= 0 && rowIndex <= 8 &&  colIndex >= 0 && colIndex <= 8){
			tmp = board.fromBoard(rowIndex, colIndex);
			if(tmp.occupied != null){
				//Se la prima pedina che incontro è la mia skippo
				if(tmp.occupied.owner != p)
					return -1;
				else
					return count;
			}  
			count++;
			rowIndex = i+iInc*count;
			colIndex = j+jInc*count;
		}
		
		return -1;
	}

	@Override
	public String toString() {
		return board.toString();
	}

}
