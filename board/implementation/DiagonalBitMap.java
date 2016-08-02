package board.implementation;

public class DiagonalBitMap implements Cloneable {

	protected long bm = 0;

	protected DiagonalBitMap() {
	}

	protected DiagonalBitMap(long bm) {
		this();
		this.bm = bm;
	}

	public DiagonalBitMap and(long value) {
		bm &= value;
		return this;
	}

	public DiagonalBitMap or(long value) {
		bm |= value;
		return this;
	}

	public DiagonalBitMap xor(long value) {
		bm ^= value;
		return this;
	}
	
	public DiagonalBitMap and(DiagonalBitMap o) {
		bm &= o.bm;
		return this;
	}

	public DiagonalBitMap or(DiagonalBitMap o) {
		bm |= o.bm;
		return this;
	}

	public DiagonalBitMap not() {
		bm = ~bm;
		return this;
	}

	public DiagonalBitMap xor(DiagonalBitMap o) {
		bm ^= o.bm;
		return this;
	}

	public DiagonalBitMap clone() {
		DiagonalBitMap res = null;
		try {
			res = (DiagonalBitMap) super.clone();
			res.bm = bm;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		long mask[] = { 0, 0, 0, 0, 0, 0x1F, 0x3F, 0x7F, 0xFF, 0x1FF };
		int shift = 9;
		long t = bm;
		for (int i = 0; i < 9; i++) {
			sb.append(
					String.format("%d:%" + shift + "s\n", i, Long.toBinaryString(t & mask[shift])).replace(' ', '0'));
			t = t >>> shift;
			if (i == 0) {
				shift--;
				continue;
			}
			i++;
			sb.append(
					String.format("%d:%" + shift + "s\n", i, Long.toBinaryString(t & mask[shift])).replace(' ', '0'));
			t = t >>> shift;
			shift--;
		}
		return sb.toString();
	}

}
