package com.aug3.yhyc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

	public String encrypt(String info) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		byte[] srcBytes = info.getBytes();
		// 使用srcBytes更新摘要
		md.update(srcBytes);
		// 完成哈希计算，得到result
		byte[] resultBytes = md.digest();
		return resultBytes.toString();
	}

}
