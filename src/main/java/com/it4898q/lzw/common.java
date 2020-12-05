package com.it4898q.lzw;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.rowset.serial.SQLOutputImpl;

public class common {
	public static void main(String[] args) throws IOException {
		InputStream inRaw = new FileInputStream("/home/helloword/Documents/helloworld.text");
		LZWUtils.Encoder(inRaw);
//		System.out.println(-1);
	}

	
}
