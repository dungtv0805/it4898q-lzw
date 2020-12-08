package com.it4898q.lzw;

import java.io.IOException;
import java.io.InputStream;

public class LZWUtils {
	// SHIFTCOUNT = 4: 4 << firstCharacter (8 bit) ^ secondChar (8 bit) de mix 2
	// value giup tao index
	// MAXVALUE = 2^12,
	// TABLESIZE la so nguyen to sau 2^12 de tao index
	public static final int SHIFTCOUNT = 4, MAXVALUE = (1 << 12) - 1, MAXCODE = MAXVALUE - 1, TABLESIZE = 5021;

	/**
	 * Nen 1 mang gom cac byte sau khi duoc doc tu file
	 * 
	 * @param inRaw
	 * @return tra ve mang sau khi dc nen
	 * @throws IOException
	 */
	public static CodeOutputPacker Compress(InputStream inRaw) throws IOException {

		int nextCode = 256;

		// mang object luu gia tri token gom token va gia gia tri dau cung voi gia tri
		// noi vao
		LZWHistoryItem[] history = new LZWHistoryItem[TABLESIZE];

		// return mang gia tri duoc compress
		CodeOutputPacker cop = new CodeOutputPacker();

		// gia tri dau tien cua raw text
		int oneCode = inRaw.read();

		int oneByte;
		while (-1 != (oneByte = inRaw.read())) {
			// tao index cho xau
			int index = FindMatch(history, oneCode, oneByte);

			if (history[index] != null) {
				// gan gia tri dau thanh token neu token da duoc tao
				oneCode = history[index].Code;
			} else {
				// them gia tri dau thanh output
				cop.putN(oneCode);
				if (nextCode <= MAXCODE) {
					// tao token va luu gia tri dau va sau
					history[index] = new LZWHistoryItem(nextCode++, oneCode, oneByte);
					oneCode = oneByte;
				}
			}

		}

		return cop;
	}

	/**
	 * tim token trong LWZHistoryItem[] hoac tao index cho token moi trong
	 * LZWHistoryItem[]
	 * 
	 * @param history
	 * @param aPrefix    co the la token co the la character
	 * @param aCharacter character duoc noi vao
	 * @return index cho token moi
	 */
	private static int FindMatch(LZWHistoryItem[] history, int aPrefix, int aCharacter) {
		int offset;

		// mix gia tri de tao index
		int index = (aCharacter << SHIFTCOUNT) ^ aPrefix;

		if (index == 0) {
			offset = 1;
		} else {
			offset = TABLESIZE - index;
		}

		while (true) {
			// neu token da ton tai hoac gia tri dau cuoi trung nhau thi return index cho
			// history
			if ((history[index] == null
					|| ((history[index].Prefix == aPrefix) && history[index].Append == aCharacter))) {
				return index;
			}

			// tao index moi
			index -= offset;
			if (index < 0)
				index += TABLESIZE;
		}
	}

	/**
	 * Giai nen 1 mang sau khi duoc doc tu 1 file da duoc nen
	 * 
	 * @param inComp
	 * @return
	 */
	public static CodeOutputPacker Expand(CodeInputPacker inComp) {
		// so character duoc them vao output
		int nPushed;

		// Object dung lam bo dem de luu gia tri khi gap token
		LZWByteStack charStack = new LZWByteStack();

		LZWHistoryItem[] history = new LZWHistoryItem[TABLESIZE];
		CodeOutputPacker cop = new CodeOutputPacker();
		int nextCode = 256;

		// doc vao gia tri dau tien
		int oldCode = (short) inComp.getN();
		// vi LZW dung 1 xau 2 ky tro len de tao token nen gia tri dau luon < 255
		if (oldCode > 255) {
			throw new IllegalArgumentException("Not LZW");
		}

		// Day character dau tien vao output
		int aChar = oldCode;
		cop.putN(aChar);

		int newCode;

		while (MAXVALUE != (newCode = inComp.getN())) {
			// neu gia tri duoc doc vao lon hoac bang token thi token
			if (newCode >= nextCode) {
				charStack.push(aChar);
				nPushed = 1 + Decode(history, charStack, oldCode);
			} else {
				nPushed = Decode(history, charStack, newCode);
			}

			// day character truoc character cuoi trong LZWByteStack vao ouput
			cop.putN(aChar = charStack.pop());

			// neu token chua 1 xau hon 3 ky tu thi day tiep character trong LZWByteStack
			// vao output
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

	/**
	 * Them character hoac token vao 1 bo dem de khi gap token , neu la 1 thi token
	 * la xau gom 2 ky tu, 2 thi token gom 3 gia tri
	 * 
	 * @param history
	 * @param cStack
	 * @param code
	 * @return so ky tu token chua + 1
	 */
	private static int Decode(LZWHistoryItem[] history, LZWByteStack cStack, int code) {
		int ii = 1;

		// neu code > 255 thi them gia tri thu 2 cua token vao LZWByteStack
		while (code > 255) {
			cStack.push(history[code].Append);
			// lay character hoac token sau do de day vao LZWByteStack
			code = history[code].Prefix;
			ii++;
		}
		// day code vao LZWByteStack, neu la character thi code vao stack 0
		cStack.push((byte) code);

		return ii;
	}
}
