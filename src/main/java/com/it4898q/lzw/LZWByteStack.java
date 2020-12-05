package com.it4898q.lzw;

public class LZWByteStack {

	private int[] ints;
	private int counter = 0;

	public LZWByteStack() {
		this(4000);
	}

	public LZWByteStack(int size) {
		ints = new int[size];
	}

	public void push(int bv) {
		ints[counter++] = bv;
	}

	public int pop() {
		return ints[counter--];
	}
}
