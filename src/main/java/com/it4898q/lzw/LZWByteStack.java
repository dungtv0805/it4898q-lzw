package com.it4898q.lzw;

public class LZWByteStack {

	private int[] ints;
	private int ctr = 0;

	public LZWByteStack() {
		this(4000);
	}

	public LZWByteStack(int size) {
		ints = new int[size];
	}

	public void push(int bv) {
		ints[ctr++] = bv;
	}

	public int pop() {
		return ints[--ctr];
	}
}
