package com.it4898q.lzw;

import java.io.IOException;
import java.io.InputStream;

public class LZWUtils {
	private static final int SHIFTCOUNT = 4, MAXVALUE = (1 << 12) - 1, MAXCODE = MAXVALUE - 1, TABLESIZE = 5021;

	public static int Encoder(InputStream inRaw) throws IOException {
		int inctr = 0;
		int nextCode = 256;
		LZWHistoryItem[] history = new LZWHistoryItem[TABLESIZE];

		int oneCode = inRaw.read();
		inctr++;

		int oneByte;
		while (-1 != (oneByte = inRaw.read())) {
			inctr++;
			int index = FindMatch(history, oneCode, oneByte);

			if (history[index] != null) {
				oneCode = history[index].Code;
//				System.out.println(history[index].hashCode());
//				System.out.println(index + "sd");
			} else {
//				outComp.putN(oneCode);
				if(oneCode < 256) {
					System.out.print((char) oneCode + " ");
				}
				if(oneCode >= 256) {
					System.out.print(oneCode + " ");
				}
//				System.out.println();
//				System.out.print(oneCode + " ");
				
				if (nextCode <= MAXCODE) {
					history[index] = new LZWHistoryItem(nextCode++, oneCode, oneByte);
//					System.out.println(history[index].Code);
					oneCode = oneByte;
				}
			}

		}

//		outComp.putN(oneCode);
//		outComp.putN(MAXVALUE);
//		outComp.flush();

		return inctr;
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
}
