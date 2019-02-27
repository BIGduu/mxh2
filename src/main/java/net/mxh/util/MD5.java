package net.mxh.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private final static String[] strDigits =
		{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	
	public static String md5(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(message.getBytes());
			return byteToString(md5);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}
	
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}
}
