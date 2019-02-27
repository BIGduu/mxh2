package net.mxh.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		}
		catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	public static boolean onlyLittleLetter(String content, int min, int max) {
		String regex = "^[a-z]{" + min + "," + max + "}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(content);
		boolean b = match.matches();
		return b;
	}
	
	public static boolean lengthBetween(String content, int min, int max) {
		if (content.length() >= min && content.length() <= max) {
			return true;
		}
		return false;
	}
	
	public static boolean isMobile(String mobile) {
		return Pattern.matches("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$", mobile);
	}
}