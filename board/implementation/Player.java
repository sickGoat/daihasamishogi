package board.implementation;

public enum Player {
	
	WHITE(1),BLACK(-1);
	
	private int playerValue;
	
	Player(int playerValue){
		this.playerValue = playerValue;
	}
	
	public int getPlayerValue(){
		return playerValue;
	}
	
	public Player getOpponent(){
		return Player.values()[1-this.ordinal()];
	}
}
