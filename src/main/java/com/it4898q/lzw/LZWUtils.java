package com.it4898q.lzw;

import java.io.IOException;
import java.io.InputStream;

public class LZWUtils {
	public static final int SHIFTCOUNT = 4, MAXVALUE = (1 << 12) - 1, MAXCODE = MAXVALUE - 1, TABLESIZE = 5021;

	public static CodeOutputPacker Compress(InputStream inRaw) throws IOException {
		int nextCode = 256;
		LZWHistoryItem[] history = new LZWHistoryItem[TABLESIZE];
		CodeOutputPacker cop = new CodeOutputPacker();

		int oneCode = inRaw.read();

		int oneByte;
		while (-1 != (oneByte = inRaw.read())) {
			int index = FindMatch(history, oneCode, oneByte);

			if (history[index] != null) {
				oneCode = history[index].Code;
			} else {
//				outComp.putN(oneCode);
				cop.putN(oneCode);
				if (nextCode <= MAXCODE) {
					history[index] = new LZWHistoryItem(nextCode++, oneCode, oneByte);
//					System.out.println(history[index].Code);
					oneCode = oneByte;
				}
			}

		}

		return cop;
	}

	private static int FindMatch(LZWHistoryItem[] history, int aPrefix, int aCharacter) {
		int offset;

		int index = (aCharacter << SHIFTCOUNT) ^ aPrefix;

		if (index == 0) {
			offset = 1;
		} else {
			offset = TABLESIZE - index;
		}

		while (true) {
			if ((history[index] == null
					|| ((history[index].Prefix == aPrefix) && history[index].Append == aCharacter))) {
				return index;
			}
			index -= offset;
			if (index < 0)
				index += TABLESIZE;
		}
	}

	private static int Decode(LZWHistoryItem[] history, LZWByteStack cStack, int code) {
		int ii = 1;

		while (code > 255) {
			cStack.push(history[code].Append);
			code = history[code].Prefix;
			ii++;
		}

		cStack.push((byte) code);

		return ii;
	}

	public static CodeOutputPacker Expand(CodeInputPacker inComp) {

		int outctr = 0;

		int nPushed;

		LZWByteStack charStack = new LZWByteStack();
		LZWHistoryItem[] history = new LZWHistoryItem[TABLESIZE];
		CodeOutputPacker cop = new CodeOutputPacker();
		int nextCode = 256;

		int oldCode = (short) inComp.getN();
//		System.out.println(oldCode);
		if (oldCode > 255) {
			throw new IllegalArgumentException("Not LZW");
		}

		int aChar = oldCode;
//		outExp[outctr++] = (byte) aChar;
		cop.putN(aChar);
		outctr++;

		int newCode;

		while (MAXVALUE != (newCode = inComp.getN())) {
			if (newCode >= nextCode) {
				charStack.push(aChar);
				nPushed = 1 + Decode(history, charStack, oldCode);
			} else {
				nPushed = Decode(history, charStack, newCode);
			}

			cop.putN(aChar = charStack.pop());

			while (1 < nPushed--) {
				cop.putN(charStack.pop());
			}

			if (nextCode <= MAXCODE) {
				history[nextCode] = new LZWHistoryItem(nextCode, oldCode, aChar);
				nextCode++;

				oldCode = newCode;
			}
		}

		return cop;
	}
}
