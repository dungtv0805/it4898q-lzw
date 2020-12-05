package com.it4898q.lzw;

public class CodeOutputPacker {
	int[] output;
	int index = 0;

	public void putN(int oneCode) {

		this.output[index] = oneCode;
		this.index++;
	}
}
