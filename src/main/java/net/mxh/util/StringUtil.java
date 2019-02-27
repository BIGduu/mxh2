package net.mxh.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	/**
	 * 去掉字符串中的中文
	 * @param str
	 * @return
	 */
	public static String getOrderNumber(String str) {
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);
		Matcher mat = pat.matcher(str);
		String repickStr = mat.replaceAll("");
		return repickStr;
	}
	
	/**
	 * 判断是否是空字符串 null和"" 都返回 true
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null && !str.equals("")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否是空,字符串 null和"" 都返回 false
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (str != null && !str.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否是空,字符串 null和"" 都返回 false
	 * @param str
	 * @return
	 */
	public static Integer parseInt(String str) {
		if (isNotEmpty(str))
			return Integer.parseInt(str);
		else
			return null;
	}
	
	/**
	 * 将object转为string
	 * @param str
	 * @return
	 */
	public static String toString(Object object) {
		if (null != object)
			return String.valueOf(object);
		else
			return null;
	}
	
	/**
	 * 将object转为BigDecimal
	 * @param str
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal)value;
			}
			else if (value instanceof String) {
				ret = new BigDecimal((String)value);
			}
			else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger)value);
			}
			else if (value instanceof Number) {
				ret = new BigDecimal(((Number)value).doubleValue());
			}
			else {
				throw new ClassCastException(
					"Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
			}
		}
		return ret;
	}
	
	/**
	 * 将object转为string
	 * @param str
	 * @return
	 */
	public static Integer toInteger(Object object) {
		if (null != object) {
			try {
				return Integer.parseInt(object.toString());
			}
			catch (NumberFormatException e) {
				return 22;
			}
		}
		return 0;
	}
	
	/**
	 * 将object转为string
	 * @param str
	 * @return
	 */
	public static String getCity(String address) {
		String city = "";
		if (isNotEmpty(address)) {
			Integer begin = address.indexOf("省");
			begin = begin == -1 ? 0 : begin + 1;
			Integer end = address.indexOf("市") + 1;
			begin = begin > end ? 0 : begin;
			city = address.substring(begin, end);
		}
		return city;
	}
	
	/**
	 * 截取字符串到指定长度，且后面补充上“...” 1、中文当两个字符 2、长度不够则返回原始值
	 */
	public static String cutText(String text, int length) {
		if (text == null)
			return "";
		
		String newText = trimTrailingWhitespace(text);
		
		if (getLength(newText) <= length)
			return newText;
		
		StringBuilder sb = new StringBuilder(text);
		while (getLength(sb.toString()) > length) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return trimTrailingWhitespace(sb.toString()) + "...";
	}
	
	public static int getLength(String text) {
		try {
			return text.getBytes("UTF-8").length;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	public static boolean IsNullorEmpty(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		else
			return false;
	}
	
	/**
	 * @param strs 待转化字符串
	 * @return
	 * @author estone
	 * @description 下划线格式字符串转换成驼峰格式字符串 eg: player_id -> playerId;<br>
	 *              player_name -> playerName;
	 */
	public static String underScore2CamelCase(String strs) {
		String[] elems = strs.split("_");
		for (int i = 0; i < elems.length; i++) {
			elems[i] = elems[i].toLowerCase();
			if (i != 0) {
				String elem = elems[i];
				char first = elem.toCharArray()[0];
				elems[i] = "" + (char)(first - 32) + elem.substring(1);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String e : elems) {
			sb.append(e);
		}
		return sb.toString();
	}
	
	/**
	 * @param param 待转换字符串
	 * @return
	 * @author estone
	 * @description 驼峰格式字符串 转换成 下划线格式字符串 eg: playerId -> player_id;<br>
	 *              playerName -> player_name;
	 */
	public static String camelCase2Underscore(String param) {
		Pattern p = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
			i++;
		}
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}
	
	/**
	 * @param idType 格式化类型，目前只格式化两种 身份证idType为1匹配证件类型id或身份证,账号idType=bank
	 * @param idNo 被格式化的字符串
	 * @return 格式化后的字符串
	 * @author
	 * @description idType类型 身份证出生年月前后加空格 bank:每4位加空格显示 其他不处理
	 */
	public static String formatAccountShow(String idType, String idNo) {
		// 只处理身份证及银行账号
		if (!"1".equals(idType) && !"bank".equals(idType) && !"身份证".equals(idType) || null == idNo || "".equals(idNo)) {
			return idNo;
		}
		StringBuilder builder = new StringBuilder(idNo);
		int strLen = builder.length();
		
		if ("1".equals(idType) || "身份证".equals(idType)) {// 身份证类型，出生日期前后加空格
			if (strLen == 15) {
				builder.insert(6, ' ');
				builder.insert(13, ' ');
			}
			else if (strLen == 18) {
				builder.insert(6, ' ');
				builder.insert(15, ' ');
			}
		}
		else {
			int times = strLen / 4;
			if (times * 4 == strLen) {
				times = times - 1;
			}
			
			for (int i = 0; i < times; i++) {// 每隔4位插入1个空格，插入过的长度为4+1位
				builder.insert(5 * i + 4, ' ');
			}
		}
		
		return builder.toString();
	}
	
	public static boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		return matcher.find();
	}
	
	public static String null2Empty(Object input) {
		return input == null ? "" : input.toString();
	}
	
	public static String trimAll(Object input) {
		return input == null ? "" : input.toString().replaceAll("\\s+", "");
	}
}
