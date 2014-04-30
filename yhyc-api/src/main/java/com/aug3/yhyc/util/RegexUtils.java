package com.aug3.yhyc.util;

import java.util.regex.Pattern;

public class RegexUtils {

	public static Pattern wildMatch(String str) {
		return Pattern.compile("^.*" + str + ".*$", Pattern.CASE_INSENSITIVE);
	}

	public static boolean isLetter(String charString) {
		return charString.matches("^[a-zA-Z]*");
	}

}
