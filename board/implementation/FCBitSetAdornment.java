package board.implementation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import board.AbstractRespawnableBoardAdornment;
import board.FCAdornment;
import board.RespawnableBoardAdornment;

public class FCBitSetAdornment extends AbstractRespawnableBoardAdornment implements FCAdornment{

	private BitSet blackBitSet = new BitSet(81);
	
	private BitSet whiteBitSet = new BitSet(81);
	
	protected List<BitSet> whiteFC = new ArrayList<>();
	
	protected List<BitSet> blackFC = new ArrayList<>();
	
	
	public FCBitSetAdornment(RespawnableBoardAdornment son) {
		super(son);
		init();
		initBoard();
	}

	
	@Override
	public void respawn(Pawn pawn) {
		int index = pawn.position.number.ordinal()*pawn.position.letter.ordinal();;
		if(pawn.owner == Player.BLACK){
			blackBitSet.flip(index);
		}else{
			whiteBitSet.flip(index); 
		}
		son.respawn(pawn);
	}

	@Override
	public List<Pawn> make(Move m) {
		List<Pawn> catches = son.make(m);
		Cell to = Board.getInstance().fromBoard(m.to.letter, m.to.number);
		Player pType = to.occupied.owner;
		int fromIndex = m.from.letter.ordinal()*9+(8-m.from.number.ordinal());
		int toIndex =  m.to.letter.ordinal()*9+(8-m.to.number.ordinal());
		if(pType == Player.BLACK){
			//aggiorno il bitset del nero
			blackBitSet.flip(fromIndex);
			blackBitSet.flip(toIndex);
		}else{
			//aggiorno il bitset del bianco
			whiteBitSet.flip(fromIndex);
			whiteBitSet.flip(toIndex);
		}
		
		if(catches != null){
			catches.forEach(p -> {
				int index = p.position.letter.ordinal()*p.position.number.ordinal();
				if(p.owner == Player.BLACK){
					blackBitSet.flip(index);
				}else{
					whiteBitSet.flip(index);
				}
			});
		}
		return catches;
	}


	@Override
	public int getEnemyDistance(Move m) {
		return son.getEnemyDistance(m);
	}

	@Override
	public double getMaxConfiguration(Player player) {
		//prendo il player e faccio un and con ogni configurazione
		//finale
		
		double value = 0;
		double maxValue = 0;
		if(player == Player.BLACK){
			for(BitSet fc:blackFC){
				BitSet clone = (BitSet) fc.clone();
				clone.and(blackBitSet);
				value = clone.cardinality();
				if( value > maxValue){
					maxValue = value;
				}
			}
		}else{
			for(BitSet fc:whiteFC){
				BitSet clone = (BitSet) fc.clone();
				clone.and(whiteBitSet);
				value = clone.cardinality();
				if( value > maxValue){
					maxValue = value;
				}
			}
		}
		
		return maxValue;
	}

	@Override
	public ScorePair getCumulativeMax(Player player) {
		//prendo il player e faccio un and con ogni configurazione
		//finale
		int numberOf = 0;
		double value = 0;
		double cumulative = 0;
		double maxValue = Double.MIN_VALUE;
		if(player == Player.BLACK){
			for(BitSet fc:blackFC){
				BitSet clone = (BitSet) fc.clone();
				clone.and(blackBitSet);
				value = clone.cardinality();
				if( value > maxValue){
					maxValue = value;
				}else if( value == maxValue){
					numberOf++;
				}else{
					cumulative += value;
				}
			}
		}else{
			for(BitSet fc:whiteFC){
				BitSet clone = (BitSet) fc.clone();
				clone.and(whiteBitSet);
				value = clone.cardinality();
				if( value > maxValue){
					maxValue = value;
				}else if( value == maxValue){
					numberOf++;
				}else{
					cumulative += value;
				}
			}
		}

		return new ScorePair(maxValue, numberOf,(int) cumulative);
	}

	@Override
	public int getNumberOfFinalConfiguration(Cell c, Player playerType) {
		BitSet bitSetPosition = new BitSet();
		bitSetPosition.set(c.letter.ordinal()*c.number.ordinal());
		int count = 0;
		if(playerType == Player.BLACK){
			for(BitSet blackFC : blackFC){
				BitSet clone = (BitSet)bitSetPosition.clone();
				clone.and(blackFC);
				if(clone.cardinality()>0){
					count++;
				}
			}
		}else{
			for(BitSet whiteFC : whiteFC){
				BitSet clone = (BitSet)bitSetPosition.clone();
				clone.and(whiteFC);
				if(clone.cardinality()>0){
					count++;
				}
			}
		}
		
		return count;
	}
	
	private void init(){
		//genero bitset per le fc del bianco
		for(int i = 0 ; i < 3 ; i++){
			for(int j = 0 ; j < 9 ; j++){
				//genero quelle verticale
				BitSet vertical = new BitSet();
				int count = 0;
				while(count < 5){
					vertical.set(i*9+count*9+j);
					count++;
				}
				whiteFC.add(vertical);
				if(j+4<9){
					//creo le right configuration
					BitSet right = new BitSet();
					count = 0;
					while(count < 5 ){
						right.set(i*9+count*9+j+count);
						count++;
					}
					whiteFC.add(right);
				}
				if(j-4>=0){
					//creo le left configuration
					BitSet left = new BitSet();
					count = 0;
					while(count < 5){
						left.set(i*9+count*9+j-count);
						count++;
					}
					whiteFC.add(left);
				}
			}
		}//for white
		
		//genero le final configuration per il black
		for(int i = 8 ; i > 5 ; i--){
			for(int j = 0 ; j < 9 ; j++){
				//genero la verticale
				BitSet vertical = new BitSet();
				int count = 0;
				while(count < 5){
					vertical.set(i*9-count*9+j);
					count++;
				}
				blackFC.add(vertical);
				if(j+4<9){
					//creo le right configuration
					BitSet right = new BitSet();
					count = 0;
					while(count < 5 ){
						right.set(i*9-count*9+j+count);
						count++;
					}
					blackFC.add(right);
				}
				if(j-4>=0){
					//creo le left configuration
					BitSet left = new BitSet();
					count = 0;
					while(count < 5){
						left.set(i*9-count*9+j-count);
						count++;
					}
					blackFC.add(left);
				}
			}
		}
	}
	
	private void initBoard(){
		//inizializzo i neri
		for(int i = 0 ; i < 2 ; i++){
			for(int j = 0 ; j < 9 ; j++){
				blackBitSet.set(i*9+j);
			}
		}
		//inizializzo i bianchi
		for(int i = 7 ; i < 9 ; i++){
			for(int j = 0 ; j < 9 ; j++){
				whiteBitSet.set(i*9+j);
			}
		}
	}


	@Override
	public int getMaxFinalConfiguration(Cell from,Cell to ,Player player) {
		throw new UnsupportedOperationException("Non implementato");
	}	
}
