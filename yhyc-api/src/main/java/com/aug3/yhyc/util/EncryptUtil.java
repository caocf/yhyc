package com.aug3.yhyc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String encrypt(String info) {
		
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		// 使用srcBytes更新摘要
		md5.update(info.getBytes());
		// 完成哈希计算，得到result
		byte[] md = md5.digest();

		// 把密文转换成十六进制的字符串形式
		int size = md.length;
		char str[] = new char[size * 2];
		int k = 0;
		for (int i = 0; i < size; i++) {
			byte b = md[i];
			str[k++] = hexDigits[b >>> 4 & 0xf];
			str[k++] = hexDigits[b & 0xf];
		}
		return new String(str);

	}

	public static void main(String args[]) {
		System.out.println(EncryptUtil.encrypt("roger"));
		System.out.println(EncryptUtil.encrypt("roger"));
	}

}
