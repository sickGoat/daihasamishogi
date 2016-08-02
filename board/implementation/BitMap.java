package board.implementation;

import static board.implementation.BitUtil.*;

public class BitMap implements Cloneable {

	protected int[] bm;

	public BitMap() {
		bm = new int[3];
	}

	public BitMap(int bm0, int bm1, int bm2) {
		this();
		bm[0] = bm0;
		bm[1] = bm1;
		bm[2] = bm2;
	}

	public BitMap and(int index, int value) {
		bm[index] &= value;
		return this;
	}

	public BitMap or(int index, int value) {
		bm[index] |= value;
		return this;
	}

	public BitMap xor(int index, int value) {
		bm[index] ^= value;
		return this;
	}

	public BitMap not(int index) {
		bm[index] = ~bm[index];
		return this;
	}

	public BitMap and(BitMap b) {
		bm[0] &= b.bm[0];
		bm[1] &= b.bm[1];
		bm[2] &= b.bm[2];
		return this;
	}

	public BitMap or(BitMap b) {
		bm[0] |= b.bm[0];
		bm[1] |= b.bm[1];
		bm[2] |= b.bm[2];
		return this;
	}

	public BitMap xor(BitMap b) {
		bm[0] ^= b.bm[0];
		bm[1] ^= b.bm[1];
		bm[2] ^= b.bm[2];
		return this;
	}

	public BitMap not() {
		bm[0] = ~bm[0];
		bm[1] = ~bm[1];
		bm[2] = ~bm[2];
		return this;
	}

	public BitMap leftShift(int n) {
		bm[0] = bm[0] << n;
		bm[1] = bm[1] << n;
		bm[2] = bm[2] << n;
		return this;
	}

	public BitMap rightShift(int n) {
		bm[0] = bm[0] >>> n;
		bm[1] = bm[1] >>> n;
		bm[2] = bm[2] >>> n;
		return this;
	}

	public int count() {
		return swar(bm[0]) + swar(bm[1]) + swar(bm[2]);
	}

	public BitMap clone() {
		try {
			return (BitMap) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Non posso clonare??");
		}
	}

	@Override
	public String toString() {
		return String.format("%27s%27s%27s", Integer.toBinaryString(bm[0]), Integer.toBinaryString(bm[1]),
				Integer.toBinaryString(bm[2]));
	}
}
