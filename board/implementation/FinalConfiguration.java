package board.implementation;

public abstract class FinalConfiguration{

	private static byte IDC = 0;

	protected final byte id;

	protected double value;
	
	protected double blackCount;
	
	protected double whiteCount;
	
	protected Player homePlayer = null;

	protected FinalConfiguration() {
		this.id = IDC++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		FinalConfiguration other = (FinalConfiguration) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public double getValue(){return value;}
	
	public void update(boolean emptyCell,Player p){
		if(p == Player.BLACK){
			if(emptyCell)
				blackCount++;
			else
				blackCount--;
		}else{
			if(emptyCell)
				whiteCount--;
			else
				whiteCount++;
		}
		
		getV();
	}
	
	public void update(byte inc,Player p){
		if(p==Player.BLACK)
			blackCount += inc;
		else
			whiteCount += inc;
		
		getV();
	}
	
	public void getV(){
		if(Math.abs(blackCount)>Math.abs(whiteCount)){
			value = blackCount;
			if(whiteCount==0)
				value -= 0.5;
		}else{
			value = whiteCount;
			if(blackCount == 0)
				value += 0.5;
		}
	}
}
