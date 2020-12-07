package com.it4898q.lzw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class common {
	public static void main(String[] args) throws IOException {
		String s = args[1];
//		String s = "/home/helloword/Documents/helloworld.text";
//		String expand = "expanded.txt";
		String compressed = "compressed.lzw";
		if (args[0].equals("compress")) {
			Compress(s);
			File file = new File("compressed.lzw");
			System.out.println("Sucessfully!!! File was saved in project with name compressed.lzw with : "
					+ file.length() + " bytes");
		}
		if (args[0].equals("expand")) {
			Expand(s);
			File file = new File("expanded.txt");
			System.out.println("Sucessfully!!! File was saved in project with name expanded.txt with : " + file.length()
					+ " bytes");
		}

	}

	public static void Compress(String path) throws IOException {
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		String compressed = "compressed.lzw";
		InputStream inRaw = new FileInputStream(path);
		CodeOutputPacker cop = LZWUtils.Compress(inRaw);
		try {
			fos = new FileOutputStream(compressed);
			dos = new DataOutputStream(fos);
			for (short ss : (short[]) cop.output) {
				dos.writeShort(ss);
			}
			dos.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void Expand(String path) throws FileNotFoundException {
		InputStream is = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		FileOutputStream fos = null;
		String expand = "expanded.txt";

		is = new FileInputStream(path);
		dis = new DataInputStream(is);
		CodeInputPacker cip = new CodeInputPacker();
		try {
			while (dis.available() > 0) {
				cip.putN(dis.readShort());
			}
//			System.out.println(Arrays.toString(cip.ints));

			fos = new FileOutputStream(expand);
			dos = new DataOutputStream(fos);

			CodeInputPacker cip2 = new CodeInputPacker(cip.ints);
			CodeOutputPacker cop = LZWUtils.Expand(cip2);

			for (short s : cop.output) {
				System.out.print((char) s);

			}
			System.out.println();
			for (short i : cop.output) {
				dos.write(i);
			}
			dos.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
