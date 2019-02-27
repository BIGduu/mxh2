package net.mxh.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {
	
	// 默认日期格式
	public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";
	
	// 默认时间格式
	public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	
	public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";
	
	// 日期格式化
	private static DateFormat dateFormat = null;
	
	// 时间格式化
	private static DateFormat dateTimeFormat = null;
	
	private static DateFormat dateTimeFormat2 = null;
	
	private static DateFormat timeFormat = null;
	
	private static Calendar gregorianCalendar = null;
	
	static {
		dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
		dateTimeFormat2 = new SimpleDateFormat(DATETIME_FORMAT);
		timeFormat = new SimpleDateFormat(TIME_DEFAULT_FORMAT);
		gregorianCalendar = new GregorianCalendar();
	}
	
	/**
	 * 日期格式化yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static Date formatDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 日期格式化yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getDateFormat(Date date) {
		return dateFormat.format(date);
	}
	
	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormat(Date date) {
		return dateTimeFormat.format(date);
	}
	
	/**
	 * 日期格式化yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormat2(Date date) {
		return dateTimeFormat2.format(date);
	}
	
	/**
	 * 时间格式化
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getTimeFormat(Date date) {
		return timeFormat.format(date);
	}
	
	/**
	 * 日期格式化
	 * @param date
	 * @param 格式类型
	 * @return
	 */
	public static String getDateFormat(Date date, String formatStr) {
		if (StringUtil.isNotEmpty(formatStr)) {
			return new SimpleDateFormat(formatStr).format(date);
		}
		return null;
	}
	
	/**
	 * 日期格式化
	 * @param date
	 * @param 格式类型
	 * @return
	 */
	public static Date getDateFormat(String dateStr, String formatStr) {
		if (StringUtil.isNotEmpty(formatStr)) {
			try {
				return new SimpleDateFormat(formatStr).parse(dateStr);
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 日期格式化
	 * @param date
	 * @return
	 */
	public static Date getDateFormat(String date) {
		try {
			return dateFormat.parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 时间格式化
	 * @param date
	 * @return
	 */
	public static Date getDateTimeFormat(String date) {
		try {
			return dateTimeFormat.parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取当前日期(yyyy-MM-dd)
	 * @param date
	 * @return
	 */
	public static Date getNowDate() {
		return DateUtil.getDateFormat(dateFormat.format(new Date()));
	}
	
	/**
	 * 获取当前日期(yyyy-MM-dd)
	 * @param date
	 * @return
	 */
	public static String getNowDateStr() {
		return dateFormat.format(new Date());
	}
	
	/**
	 * 获取当前日期星期一日期
	 * @return date
	 */
	public static Date getFirstDayOfWeek() {
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
		gregorianCalendar.set(Calendar.MINUTE, 0);
		gregorianCalendar.set(Calendar.SECOND, 0);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取当前日期星期日日期
	 * @return date
	 */
	public static Date getLastDayOfWeek() {
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 23);
		gregorianCalendar.set(Calendar.MINUTE, 59);
		gregorianCalendar.set(Calendar.SECOND, 59);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取日期星期一日期
	 * @param 指定日期
	 * @return date
	 */
	public static Date getFirstDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
		gregorianCalendar.set(Calendar.MINUTE, 0);
		gregorianCalendar.set(Calendar.SECOND, 0);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取日期星期天日期
	 * @param 指定日期
	 * @return date
	 */
	public static Date getLastDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 23);
		gregorianCalendar.set(Calendar.MINUTE, 59);
		gregorianCalendar.set(Calendar.SECOND, 59);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取当前月的第一天
	 * @return date
	 */
	public static Date getFirstDayOfMonth() {
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
		gregorianCalendar.set(Calendar.MINUTE, 0);
		gregorianCalendar.set(Calendar.SECOND, 0);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取当前月的最后一天
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, 1);
		gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 23);
		gregorianCalendar.set(Calendar.MINUTE, 59);
		gregorianCalendar.set(Calendar.SECOND, 59);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取指定月的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
		gregorianCalendar.set(Calendar.MINUTE, 0);
		gregorianCalendar.set(Calendar.SECOND, 0);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取指定月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, 1);
		gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, 23);
		gregorianCalendar.set(Calendar.MINUTE, 59);
		gregorianCalendar.set(Calendar.SECOND, 59);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取当天开始的时间
	 * @param date
	 * @return
	 */
	public static Date getBeginTimeOfDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取当天结束的时间
	 * @param date
	 * @return
	 */
	public static Date getEndTimeOfDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前日期星期一日期
	 * @return date
	 */
	public static Date getBeginTimeOfWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		return calendar.getTime();
	}
	
	/**
	 * 获取当前日期星期日日期
	 * @return date
	 */
	public static Date getEndTimeOfWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		return calendar.getTime();
	}
	
	/**
	 * 获取上星期一日期
	 * @return date
	 */
	public static Date getBeginTimeOfLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, -7);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		return calendar.getTime();
	}
	
	/**
	 * 获取上星期日日期
	 * @return date
	 */
	public static Date getEndTimeOfLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.add(Calendar.DATE, -7);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		return calendar.getTime();
	}
	
	/**
	 * 获取下星期一日期
	 * @return date
	 */
	public static Date getBeginTimeOfNextWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, 7);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		return calendar.getTime();
	}
	
	/**
	 * 获取下星期日日期
	 * @return date
	 */
	public static Date getEndTimeOfNextWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.add(Calendar.DATE, 7);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
		return calendar.getTime();
	}
	
	/**
	 * 获取日期前一天
	 * @param date
	 * @return
	 */
	public static Date getDayBefore(Date date) {
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day - 1);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取日期后一天
	 * @param date
	 * @return
	 */
	public static Date getDayAfter(Date date) {
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day + 1);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取当前年
	 * @return
	 */
	public static int getNowYear() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前月份
	 * @return
	 */
	public static int getNowMonth() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取当月天数
	 * @return
	 */
	public static int getNowMonthDay() {
		Calendar d = Calendar.getInstance();
		return d.getActualMaximum(Calendar.DATE);
	}
	
	/**
	 * 获取时间段的每一天
	 * @param 开始日期
	 * @param 结算日期
	 * @return 日期列表
	 */
	public static List<Date> getEveryDay(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		// 格式化日期(yy-MM-dd)
		startDate = DateUtil.getDateFormat(DateUtil.getDateFormat(startDate));
		endDate = DateUtil.getDateFormat(DateUtil.getDateFormat(endDate));
		List<Date> dates = new ArrayList<Date>();
		gregorianCalendar.setTime(startDate);
		dates.add(gregorianCalendar.getTime());
		while (gregorianCalendar.getTime().compareTo(endDate) < 0) {
			// 加1天
			gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
			dates.add(gregorianCalendar.getTime());
		}
		return dates;
	}
	
	/**
	 * 获取提前多少个月
	 * @param monty
	 * @return
	 */
	public static Date getFirstMonth(int monty) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -monty);
		return c.getTime();
	}
	
	/**
	 * 获取提前多少个小时
	 * @param monty
	 * @return
	 */
	public static Date getNowBeforeHour(int hour) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -hour);
		return c.getTime();
	}
	
	/**
	 * 获取多少分以后
	 * @param monty
	 * @return
	 */
	public static Date getAfterNowMinute(int minute) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}
	
	/**
	 * 获取提前多少分钟的时间
	 * @description 方法描述
	 * @author lizhie
	 * @date 2017年8月28日
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getDateBeforeMinute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, -minute);
		return c.getTime();
	}
	
	/**
	 * 获取提前多少个小时
	 * @param monty
	 * @return
	 */
	public static Date getDateBeforeHour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, -hour);
		return c.getTime();
	}
	
	/**
	 * @description 获取前一天
	 * @author ZhongHan
	 * @date 2017年11月29日
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date getYesterday(int hour, int minute) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * @description 获取当天时间
	 * @author ZhongHan
	 * @date 2017年11月29日
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Long getToday(int hour, int minute) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static void main(String[] args) {
		System.out.println(getFirstDayOfWeek());
	}
}
