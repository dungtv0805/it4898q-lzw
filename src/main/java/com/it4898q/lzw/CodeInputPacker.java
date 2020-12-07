package com.it4898q.lzw;

public class CodeInputPacker {

	public short[] ints;

	private int ctr = 0;

	private int size;

	public CodeInputPacker(int size) {
		this.ints = new short[size];
		this.size = size;
	}

	public CodeInputPacker() {
		
	}
	
	public CodeInputPacker(short[] ints) {
		this.ints = ints;
		this.size = ints.length;
	}

	public int getN() {
		if (ctr == this.size) {
			return LZWUtils.MAXVALUE;
		}
		return ints[ctr++];
	}

	public void putN(int oneCode) {
		if (this.ctr == this.size) {
			growSizeArrays();
		}
		this.ints[ctr] = (short) oneCode;
		ctr++;
	}

	private void growSizeArrays() {
		short[] temp = null;

		temp = new short[size + 1];

		for (int i = 0; i < this.size; i++) {
			// copies all the elements of the old array
			temp[i] = this.ints[i];
		}

		this.ints = temp;
		size += 1;
	}
}
