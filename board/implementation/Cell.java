package board.implementation;

public class Cell {

	protected Letter letter;

	protected Number number;

	protected Pawn occupied;

	protected int id;

	public Cell() {
	}

	public Cell(Letter letter, Number number) {
		this.letter = letter;
		this.number = number;
		this.id = (letter.ordinal() * 9) + (8 - number.ordinal());
	}

	public Letter getLetter() {
		return letter;
	}

	public Number getNumber() {
		return number;
	}

	public void setLetter(Letter letter) {
		this.letter = letter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((letter == null) ? 0 : letter.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		Cell other = (Cell) obj;
		if (letter != other.letter)
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.letter.toString() + "" + (this.number.ordinal() + 1);
	}

}
