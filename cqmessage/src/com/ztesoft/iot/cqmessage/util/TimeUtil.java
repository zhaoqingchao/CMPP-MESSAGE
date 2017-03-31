package com.ztesoft.iot.cqmessage.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class TimeUtil {

	public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	//系统默认日期格式
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	//系统默认日期时间格式
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	//8位日期格式
	public static final String DATE_FORMAT_8 = "yyyyMMdd";
	public static final String DATE_FORMAT_YYYYMM = "yyyyMM";
	public static final String DATE_FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd-HH";
	//14位日期时间格式
	public static final String DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
	//12位日期格式
	public static final String DATE_TIME_FORMAT_12 = "yyMMddHHmmss";
	
	public static String getNow(String pattern){
		SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
		return sFormat.format(new Date());
	}
	
	
	/**
	 * 得到应用服务器当前日期，以默认格式显示。
	 * 
	 * @return
	 */
	public static String getFormatedDate() {
		Date date = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT);
		return dateFormator.format(date);

	}

	/**
	 * 得到应用服务器当前日期时间，以默认格式显示。
	 * 
	 * @return
	 */
	public static String getFormatedDateTime() {

		Date date = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_TIME_FORMAT);
		return dateFormator.format(date);

	}
	
	/**
	 * 得到应用服务器的当前时间
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	

	/**
	 * 得到应用服务器的当前时间，毫秒。
	 * 
	 * @return
	 */
	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 得到应用服务器当前日期 按照指定的格式返回。
	 * 
	 * @param pattern
	 *            格式类型，通过系统常量中定义，如：DATE_FORMAT_8
	 * @return
	 */
	public static String formatDate(String pattern) {

		Date date = new Date();
		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);
		String str = dateFormator.format(date);

		return str;
	}

	/**
	 * 转换输入日期 按照指定的格式返回。
	 * 
	 * @param date
	 * @param pattern
	 *            格式类型，通过系统常量中定义，如：DATE_FORMAT_8
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {

		if (date == null)
			return "";

		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);
		String str = dateFormator.format(date);

		return str;
	}

	/**
	 * 转换输入日期 按照指定的格式返回。
	 * 
	 * @param date
	 * @param pattern
	 *            格式类型，通过系统常量中定义，如：DATE_FORMAT_8
	 * @param loc
	 *            locale
	 * @return
	 */
	public static String formatDate(Date date, String pattern, Locale loc) {
		if (pattern == null || date == null) {
			return "";
		}
		String newDate = "";
		if (loc == null)
			loc = Locale.getDefault();
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, loc);
			newDate = sdf.format(date);
		}
		return newDate;
	}

	/**
	 * 将字符时间从一个格式转换成另一个格式。时间的格式，最好通过系统常量定义。 如：
	 * String dateStr = "2006-10-9 12:09:08";
	 * TimeUtil.convertDateFormat(dateStr,
	 * DATE_TIME_FORMAT,
	 * DATE_FORMAT_8);
	 * 
	 * @param sdate
	 * @param patternFrom
	 *            格式类型，通过系统常量中定义，如：DATE_FORMAT_8
	 * @param patternTo
	 *            格式类型，通过系统常量中定义，如：DATE_FORMAT_8
	 * @return
	 */
	public static String convertDateFormat(String dateStr, String patternFrom, String patternTo) {

		if (dateStr == null || patternFrom == null || patternTo == null) {
			return "";
		}

		String newDate = "";

		try {

			Date dateFrom = parseStrToDate(dateStr, patternFrom);
			newDate = formatDate(dateFrom, patternTo);

		} catch (Exception e) {
		}

		return newDate;
	}

	/**
	 * 将时间串按照默认格式DATE_FORMAT，格式化成Date。
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseStrToDate(String dateStr) {

		if (null == dateStr || "".equals(dateStr))
			return null;

		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT);

		java.util.Date tDate = dateFormator.parse(dateStr, new ParsePosition(0));
		

		return tDate;
	}

	public static String parseDateStrToDateTimeStr(String dateStr) {

		if (null == dateStr || "".equals(dateStr))
			return null;

		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT);

		java.util.Date tDate = dateFormator.parse(dateStr, new ParsePosition(0));
		return formatDate(tDate, DATE_TIME_FORMAT);

	}
	
	

	/**
	 * 将时间串按照默认格式DATE_FORMAT，格式化成Date。
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Calendar parseStrToCalendar(String dateStr) {

		if (null == dateStr || "".equals(dateStr))
			return null;

		SimpleDateFormat dateFormator = null;
		try {
			if(dateStr.length() > 10) {
				dateFormator = new SimpleDateFormat(DATE_TIME_FORMAT);
			} else {
				dateFormator = new SimpleDateFormat(DATE_FORMAT);
			}
        } catch (Exception e) {
        	dateFormator = new SimpleDateFormat(DATE_FORMAT);
        }
        java.util.Date tDate = dateFormator.parse(dateStr, new ParsePosition(0));

		Locale loc = Locale.getDefault();
		Calendar cal = new GregorianCalendar(loc);
		cal.setTime(tDate);

		return cal;
	}

	/**
	 * 将时间串按照默认格式DATE_TIME_FORMAT，格式化成Date。
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date parseStrToDateTime(String dateStr) {

		if (null == dateStr || "".equals(dateStr))
			return null;

		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_TIME_FORMAT);

		java.util.Date tDate = dateFormator.parse(dateStr, new ParsePosition(0));

		return tDate;
	}

	/**
	 * 将时间串按照指定格式，格式化成Date。
	 * 
	 * @param date
	 * @param pattern
	 *            格式类型，通过系统常量中定义，如：DATE_FORMAT_8
	 * @return
	 */
	public static Date parseStrToDate(String dateStr, String pattern) {
		if (null == dateStr || "".equals(dateStr))
			return null;

		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);

		java.util.Date tDate = dateFormator.parse(dateStr, new ParsePosition(0));

		return tDate;
	}
	
	public static Date parseDateToFormatByDb(String dataStr,String formate) {

//		String getSql = "select to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd HH24:mi:ss') from dual";
//		List params = new ArrayList();
//		params.add(dataStr);
//		String forMaStr = sqlMapExe.queryValueBySqlAndCond(getSql, params); 
		Date date = TimeUtil.parseDateToFormat(dataStr, formate); 
		return date;
	}
	
	private static LinkedHashMap dateFormatMap=null;
	static{
		if(dateFormatMap==null){
			dateFormatMap=new LinkedHashMap();
			dateFormatMap.put(DATE_TIME_FORMAT,"[0-3]{1}[0-9]{1}[0-9]{1}[0-9]{1}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1} [0-2]?[0-9]{1}:[0-5]?[0-9]{1}:[0-5]?[0-9]{1}");//yyyy-mm-dd hh:mm:ss
			dateFormatMap.put(DATE_FORMAT,"[0-3]{1}[0-9]{1}[0-9]{1}[0-9]{1}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}");//yyyy-mm-dd
			dateFormatMap.put(DATE_FORMAT_8,"[0-3]{1}[0-9]{1}[0-9]{1}[0-9]{1}[0-1]?[0-9]{1}[0-3]?[0-9]{1}");//yyyymmdd
			dateFormatMap.put(DATE_TIME_FORMAT_12,"[0-9]{1}[0-9]{1}[0-1]?[0-9]{1}[0-3]?[0-9]{1}[0-2]?[0-9]{1}[0-5]?[0-9]{1}[0-5]?[0-9]{1}");//yyMMddHHmmss
			dateFormatMap.put(DATE_TIME_FORMAT_14,"[0-3]{1}[0-9]{1}[0-9]{1}[0-9]{1}[0-1]?[0-9]{1}[0-3]?[0-9]{1}[0-2]?[0-9]{1}[0-5]?[0-9]{1}[0-5]?[0-9]{1}");//yyyyMMddHHmmss
		}
	}
	private static String getDateFormat(String dataStr){
		for(Iterator it=dateFormatMap.entrySet().iterator();it.hasNext();){
			Map.Entry map=(Map.Entry)it.next();
			String regMatch=(String)map.getValue();
			if(java.util.regex.Pattern.matches(regMatch, dataStr)){
				return (String)map.getKey();
			}
		}
		return null;
	}
	public static Date parseDateToFormat(String dataStr,String formate) {

		//add by AyaKoizumi 101208支持格式:
		/*
		yyyy-mm-dd hh:mi:ss;
		yyyy-mm-dd;
		yyyymmdd;
		yyyyMMddHHmmss;
		yyMMddHHmmss;
		*/
		String currDateFormat=getDateFormat(dataStr);
		if(currDateFormat==null)//默认格式匹配不上才用传入的格式
			currDateFormat=formate;
		SimpleDateFormat sf = new SimpleDateFormat(currDateFormat);
		try {
			return sf.parse(dataStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(" 时间格式判断有误。判断的时间格式必须为以下几种" +
					"[yyyy-mm-dd hh:mi:ss]"+
					"[yyyy-mm-dd]"+
					"[yyyymmdd]"+
					"[yyyyMMddHHmmss]"+
					"[yyMMddHHmmss]" +
					"不能是:"+dataStr); 
		}
//		SimpleDateFormat sf = new SimpleDateFormat(formate);
//		Date data = new Date();
//		try {
//			//20101116 lin 还需要继续细化判断。
//			
//			if(formate.length() > dataStr.trim().length()){
//				dataStr = dataStr+" 00:00:00";
//				if(dataStr.trim().length() > formate.length()){
//					dataStr = dataStr.replace(" 00:00:00", "");
//					throw new RuntimeException(" 时间格式判断有误。判断的时间格式必须为："+formate+"，不能是:"+dataStr); 
//				}
//			}
//			data = sf.parse(dataStr);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException(" 时间格式判断有误。判断的时间格式必须为："+formate+"，不能是:"+dataStr); 
//		} 
//		return data;
	}
	/**
	 * 按时间格式相加：
	 * 输入要相加的时间基点（字符串型或时间类型），相加的时长（整型），格式（"year"年、"month"月、"day"日、”hour“时、”minute“分、”second“秒、"week"周）
	 * 输出相加后的时间（字符串型或时间类型）
	 * 
	 * @param dateStr
	 * @param pattern
	 * @param step
	 * @param type
	 *            "year"年、"month"月、"day"日、”hour“时、”minute“分、”second“秒、"week"周
	 *            通过常量TimeUtil.YEAR等来设置.
	 * @return
	 */
	public static String addDate(String dateStr, String pattern, int step, String type) {
		if (dateStr == null) {
			return dateStr;
		} else {
			Date date1 = TimeUtil.parseStrToDate(dateStr, pattern);

			Locale loc = Locale.getDefault();
			Calendar cal = new GregorianCalendar(loc);
			cal.setTime(date1);

			if (TimeUtil.WEEK.equals(type)) {

				cal.add(Calendar.WEEK_OF_MONTH, step);

			} else if (TimeUtil.YEAR.equals(type)) {

				cal.add(Calendar.YEAR, step);

			} else if (TimeUtil.MONTH.equals(type)) {

				cal.add(Calendar.MONTH, step);

			} else if (TimeUtil.DAY.equals(type)) {

				cal.add(Calendar.DAY_OF_MONTH, step);

			} else if (TimeUtil.HOUR.equals(type)) {

				cal.add(Calendar.HOUR, step);

			} else if (TimeUtil.MINUTE.equals(type)) {

				cal.add(Calendar.MINUTE, step);

			} else if (TimeUtil.SECOND.equals(type)) {

				cal.add(Calendar.SECOND, step);

			}

			return TimeUtil.formatDate(cal.getTime(), pattern);
		}
	}

	/**
	 * 按时间格式相减：
	 * 输入要相加的时间基点（字符串型或时间类型），相加的时长（整型），格式（"year"年、"month"月、"day"日、”hour“时、”minute“分、”second“秒、"week"周）
	 * 输出相加后的时间（字符串型或时间类型）
	 * 
	 * @param dateStr
	 * @param pattern
	 * @param step
	 * @param type
	 *            "year"年、"month"月、"day"日、”hour“时、”minute“分、”second“秒、"week"周
	 * @return
	 */
	public static String minusDate(String dateStr, String pattern, int step, String type) {

		return addDate(dateStr, pattern, (0 - step), type);

	}

	/**
	 * 日期增加天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDay(Date date, int days) {
		if (date == null) {
			return date;
		} else {
			Locale loc = Locale.getDefault();
			Calendar cal = new GregorianCalendar(loc);
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, days);
			return cal.getTime();
		}
	}

	/**
	 * 获取两个日期之差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDays(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		else
			return (int) ((date2.getTime() - date1.getTime()) / 0x5265c00L);//86400000 代表毫秒
	}
	
	/**
	 * 获取两个日期之差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getSecs(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		else
			return (int) ((date2.getTime() - date1.getTime()) / 1000);//1000 代表毫秒
	}
	


	/**
	 * 日期比较大小
	 * 
	 * @param dateStr1
	 * @param dateStr2
	 * @param pattern
	 * @return
	 */
	public static int compareDate(String dateStr1, String dateStr2, String pattern) {

		Date date1 = TimeUtil.parseDateToFormat(dateStr1, pattern);
		Date date2 = TimeUtil.parseDateToFormat(dateStr2, pattern);

		return date1.compareTo(date2);

	}
	 
	/**
	 * 日期比较大小
	 * 
	 * @param dateStr1
	 * @param dateStr2
	 * @param pattern
	 * @return
	 */
	public static int compareDateByDb(String dateStr1, String dateStr2, String pattern) {

		Date date1 = TimeUtil.parseDateToFormatByDb(dateStr1, pattern);
		Date date2 = TimeUtil.parseDateToFormatByDb(dateStr2, pattern);

		return date1.compareTo(date2);

	}

	/**
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getFirstDayInMonth(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		cal.add(Calendar.DAY_OF_MONTH, (1 - day)); //日期加减天数

		return TimeUtil.formatDate(cal.getTime(), pattern);
	}

	/**
	 * 获取当月1号
	 * @param dateStr
	 * @param pattern
	 * @author mzp
	 * @time 20080717
	 * @return
	 */
	public static String getFirstDayInMonth(String dateStr, String pattern,String isZeroSecond) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int year=cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour=cal.get(Calendar.HOUR);
		int minute=cal.get(Calendar.MINUTE);
		int second=cal.get(Calendar.SECOND);
		day=1;hour=0;minute=0;second=0;
		cal.clear();
		cal.set(year, month, day, hour, minute, second);
		return TimeUtil.formatDate(cal.getTime(), pattern);
	}
	/**
	 * 获取下月1号
	 * @param dateStr
	 * @param pattern
	 * @author AyaKoizumi
	 * @time 20101112
	 * @return
	 */
	public static String getFirstDayInNextMonth(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int year=cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour=cal.get(Calendar.HOUR);
		int minute=cal.get(Calendar.MINUTE);
		int second=cal.get(Calendar.SECOND);
		day=1;hour=0;minute=0;second=0;
		cal.clear();
		cal.set(year, month+1, day, hour, minute, second);
		return TimeUtil.formatDate(cal.getTime(), pattern);
	}
	/**
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getLastDayInMonth(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		int maxDayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int step = maxDayInMonth - day;
		
		cal.add(Calendar.DAY_OF_MONTH, step); //当月天上再添加几天

		return TimeUtil.formatDate(cal.getTime(), pattern);
	}
	
	//取当月最后一天最后一秒
	public static String getLastDayLastSecInMonth(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		int maxDayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int step = maxDayInMonth - day + 1;
		
		cal.add(Calendar.DAY_OF_MONTH, step); //当月天上再添加几天，得到次月1号
		cal.add(Calendar.SECOND, -1);//减1秒，得到本月最后一天最后一秒
		return TimeUtil.formatDate(cal.getTime(), pattern);
	}

	//取上月最后一天最后一秒 核心版本同步：106530
	public static String getUpDayLastSecInMonth(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		cal.add(Calendar.DAY_OF_MONTH, -day+1); //当月天上再添加几天，得到次月1号
		cal.add(Calendar.SECOND, -1);//减1秒，得到本月最后一天最后一秒
		return TimeUtil.formatDate(cal.getTime(), pattern);
	}
	
	/**
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getFirstDayInWeek(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int day = cal.get(Calendar.DAY_OF_WEEK);

		cal.add(Calendar.DAY_OF_MONTH, (1 - day));

		return TimeUtil.formatDate(cal.getTime(), pattern);
	}

	/**
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getLastDayInWeek(String dateStr, String pattern) {
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int day = cal.get(Calendar.DAY_OF_WEEK);

		cal.add(Calendar.DAY_OF_MONTH, (6 - day));

		return TimeUtil.formatDate(cal.getTime(), pattern);
	}

	public static long calendarDayPlus(String dateStr1, String dateStr2) {
		
		if (dateStr1 == null || dateStr2 == null||dateStr1.equals("")||dateStr2.equals(""))
			return 0;
		Date date1 = TimeUtil.parseStrToDate(dateStr1);
		Date date2 = TimeUtil.parseStrToDate(dateStr2);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		long t1 = c1.getTimeInMillis();
		long t2 = c2.getTimeInMillis();
		long day = (t2 - t1) / (1000 * 60 * 60 * 24);
		return day;
	}
	
	/**
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static int calendarPlus(String dateStr1, String dateStr2) {
		
		if (dateStr1 == null || dateStr2 == null||dateStr1.equals("")||dateStr2.equals(""))
			return 0;
		
		Calendar cal1 = TimeUtil.parseStrToCalendar(dateStr1);
		
		Calendar cal2 = TimeUtil.parseStrToCalendar(dateStr2);
		
		int dataStr1Year = cal1.get(Calendar.YEAR);
		int dataStr2Year = cal2.get(Calendar.YEAR);
		
		int dataStr1Month = cal1.get(Calendar.MONTH);
		int dataStr2Month = cal2.get(Calendar.MONTH);


		return ((dataStr2Year*12+dataStr2Month+1)-(dataStr1Year*12+dataStr1Month));
		
	}
	
	/**
	 * 获取当前年
	 * @param dateStr
	 * @param pattern
	 * @author AyaKoizumi
	 * @time 20100112
	 * @return
	 */
	public static String getCurrYear() {
		String dateStr=TimeUtil.getFormatedDate();
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int year = cal.get(Calendar.YEAR);
		return String.valueOf(year);
	}
	
	/**
	 * 获取当月月份
	 * @param dateStr
	 * @param pattern
	 * @author AyaKoizumi
	 * @time 20100112
	 * @return
	 */
	public static String getCurrMonth() {
		String dateStr=TimeUtil.getFormatedDate();
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int month = cal.get(Calendar.MONTH);
		return String.valueOf(month);
	}
	
	
	
	public static String getCurrDay(){
		
		Calendar c = Calendar.getInstance();
		int datenum=c.get(Calendar.DATE);
		return datenum+"";
	}
	
	
	/**
	 * 获取下月月份
	 * @param dateStr
	 * @param pattern
	 * @author AyaKoizumi
	 * @time 20100112
	 * @return
	 */
	public static String getNextMonth() {
		String dateStr=TimeUtil.getFormatedDate();
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int month = cal.get(Calendar.MONTH);
		if(month==12)
			month=1;
		else
			month=month+1;
		return String.valueOf(month);
	}
	
	/**
	 * 获取当前年月：2016-09
	 * @param dateStr
	 * @param pattern
	 * @author AyaKoizumi
	 * @time 20100112
	 * @return
	 */
	public static String getCurrYearAndMonth() {
		String dateStr = TimeUtil.getFormatedDate();
		Calendar cal = TimeUtil.parseStrToCalendar(dateStr);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String date = "";
		/**
		 * 拼接年份和月份
		 */
		date = year + "-" + (month < 10 ? "0" + month : month);
		return date;
	}

	
	public static int getCurHour(){
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(date);//给Calendar对象设置时间
        int hour = c.get(Calendar.HOUR_OF_DAY);//取得当前时间的时数
        return hour;
	}
	
	public final static String YEAR = "year";

	public final static String MONTH = "month";

	public final static String DAY = "day";

	public final static String HOUR = "hour";

	public final static String MINUTE = "minute";

	public final static String SECOND = "second";

	public final static String WEEK = "week";
	
	public final static String DATE = "yyyy-mm-dd"; 
	
	public final static String TIME = "yyyy-MM-dd HH24:mi:ss";
	
	public static final String defaultPattern = "yyyy-MM-dd HH:mm:ss";

}
