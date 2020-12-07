package com.it4898q.lzw;

public class CodeOutputPacker {
	short[] output;
	private int ctr;
	private int size;

//	public int getN() {
//		return 1;
//	}

	public CodeOutputPacker() {
		this.output = new short[1];
		this.ctr = 0;
		this.size = 1;
	}

	public void putN(int oneCode) {
		if (this.ctr == this.size) {
			growSizeArrays();
		}
		this.output[ctr] = (short) oneCode;
		ctr++;
	}

	private void growSizeArrays() {
		short[] temp = null;

		temp = new short[size + 1];

		for (int i = 0; i < this.size; i++) {
			// copies all the elements of the old array
			temp[i] = this.output[i];
		}

		this.output = temp;
		size += 1;
	}
}
