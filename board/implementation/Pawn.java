package board.implementation;

public class Pawn {
	
	private static byte IDC = 0;
	
	protected final byte id;
	
	protected Cell position;
	
	protected final Player owner;
	
	protected Pawn(Player player) {
		this.owner = player;
		this.id = ++IDC;
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
		Pawn other = (Pawn) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pawn [id=" + id + "]";
	}
}



