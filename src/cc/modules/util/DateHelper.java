package cc.modules.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;

/**
 * @author kgdoqi extends saiyo
 * 
 */
public class DateHelper {

	public Calendar cal;
	private Integer year, day, month, hour, minute;

	/**
	 * 
	 */
	public DateHelper() {
		cal = Calendar.getInstance();
		year = Integer.valueOf(cal.get(Calendar.YEAR));
		day = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		month = Integer.valueOf(cal.get(Calendar.MONTH) + 1);
		hour = Integer.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		minute = Integer.valueOf(cal.get(Calendar.MINUTE));
	}

	//
	public String getCurrentDate(String language) {

		if (language.equals("EN")) {
			String currentDateEN = month + "-" + day + "-" + year + " " + hour
					+ ":" + minute;
			return currentDateEN;
		}
		String currentDateCN = year + "年" + month + "月" + day + "日" + hour
				+ ":" + minute;
		return currentDateCN;
	}

	public String getCurrentDate() {
		String currentDateEN = year + "-" + month + "-" + day;
		return currentDateEN;
	}

	// 
	public String getRandomNum() {
		String strTime = year.toString() + month.toString() + day.toString()
				+ hour.toString() + minute.toString();
		String strRan = strTime
				+ String.valueOf(Math.round(Math.random() * 8999 + 1000));
		return strRan;
	}

	public String getCurrentDateTimeNum() {
		String currentDateEN = year + "" + month + "" + day + "" + hour + ""
				+ minute + "";
		return currentDateEN;
	}

	public int compareDate(String strDate1, String strDate2) {
		Date date1 = convertToDate(strDate1);
		Date date2 = convertToDate(strDate2);
		return date1.compareTo(date2);
	}

	public Date convertToDate(String strDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		if (strDate != null && !"".equals(strDate)) {
			try {
				date = df.parse((String) strDate);
			} catch (Exception pe) {
				pe.printStackTrace();
				throw new ConversionException("Error converting String to Date");
			}
		}
		return date;
	}

	/**
	 * 获取一个日期，该日期是在date的days天之前(days是负数)或之后(days是正数)
	 * 
	 * @param date
	 * @param days
	 * @return Date
	 */
	public static Date getDateByCalculateDays(Date date, int days) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DATE, days);
		return calender.getTime();
	}

	/**
	 * 判断传入的date是否是新日期，将当前时间减去days天得出一个时间date2，然后再用date和date2比较，
	 * 如果date在date2之后或等于date2返回true，否则返回false
	 * 
	 * @param days
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isNewDate(Date date, int days) throws Exception {
		if (date == null) {
			throw new Exception("日期不能为空！");
		}
		Date date2 = getDateByCalculateDays(new Date(), days * (-1));
		int compareResult = date.compareTo(date2);
		if (compareResult < 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 把日期按照某个格式转换成字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dataToString(Object date, String format) {
		if (null != date) {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		}
		return null;
	}

	/**
	 * 把字符串按照某个格式转换成日期类型
	 * 
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String strDate, String format) {
		if (StringUtil.isNotBlank(strDate)) {
			DateFormat df = new SimpleDateFormat(format);
			Date date = null;
			if (strDate != null && !"".equals(strDate)) {
				try {
					date = df.parse((String) strDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return date;
		}
		return null;
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static Date getCurrentDate_yyyy_MM_dd() {
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(calender.getTime());
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取昨天时间，格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static Date getYesterdayDate_yyyy_MM_dd() {
		Calendar calender = Calendar.getInstance();
		calender.setTime(new Date());
		calender.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(calender.getTime());
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return date;
	}

	public static Date getDateByAddNum(Date date, long num) {
		if(null != date) {
			Calendar calender = Calendar.getInstance();
			calender.setTimeInMillis(date.getTime() + num);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strDate = sdf.format(calender.getTime());
			try {
				date = sdf.parse(strDate);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			return date;
		}
		return null;
	}

	public static String getWeekByDate(Date date) {
		if (null != date) {
			Calendar calender = Calendar.getInstance();
			calender.setTime(date);
			if (Calendar.MONDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周一";
			}
			if (Calendar.TUESDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周二";
			}
			if (Calendar.WEDNESDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周三";
			}
			if (Calendar.THURSDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周四";
			}
			if (Calendar.FRIDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周五";
			}
			if (Calendar.SATURDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周六";
			}
			if (Calendar.SUNDAY == calender.get(Calendar.DAY_OF_WEEK)) {
				return "周日";
			}
		}
		return null;
	}
	
	public static int compareDate(Date date1, Date date2) {
		return date1.compareTo(date2);
	}
	
	/**
	 * 计算两个时间的时间差转换成X小时X分格式
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String betweenTime(Date begin, Date end) {
		if(end != null && begin != null) {
			long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
			long hour1=between/3600;
			long minute1=between%3600/60;
			long second1=between%60/60;
			String betweenTime = "";
			if(hour1>0)
				betweenTime += hour1+"小时";
			betweenTime += minute1+"分";
			if(hour1<1 && minute1<1){
				return "<1分";
			}
			return betweenTime;
		}
		return "<1分";
	}
	
	/**
	 * 把long类型的时间转换成X小时X分格式
	 * @param time
	 * @return
	 */
	public static String formatLongTime(long time) {
		if(time > 0) {
			String betweenTime = "";
			long between=time/1000;//除以1000是为了转换成秒
			long hour1=between/3600;
			long minute1=between%3600/60;
			long second1=between%60/60;
			if(hour1>0)
				betweenTime += hour1+"小时";
			betweenTime += minute1+"分";
			if(hour1<1 && minute1<1){
				return "<1分";
			}
			return betweenTime;
		}
		return "<1分";
	}
	
	public static Date getDateByAddMonth(Date date, int month) {
		if(null != date) {
			Calendar calender = Calendar.getInstance();
			calender.setTime(date);
			calender.add(Calendar.MONTH, month);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String strDate = sdf.format(calender.getTime());
			try {
				date = sdf.parse(strDate);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			return date;
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(DateHelper.dataToString(new Date(), "HH:mm"));
		// System.out.println((DateHelper.stringToDate("2015-07-16 17:29:00",
		// "yyyy-MM-dd HH:mm:ss").getTime() -
		// DateHelper.stringToDate("2015-07-16 17:28:00",
		// "yyyy-MM-dd HH:mm:ss").getTime())/1000);
		// System.out.println(DateHelper.getDateByAddNum(new Date(), 1800000));
		//System.out.println(DateHelper.getWeekByDate(new Date()));
		//System.out.println(DateHelper.compareDate(DateHelper.getCurrentDate_yyyy_MM_dd(), DateHelper.stringToDate("2015-07-31 00:00:00", "yyyy-MM-dd HH:mm:ss")));
		//System.out.println(DateHelper.betweenTime(DateHelper.stringToDate("2015-08-03 11:38:59", "yyyy-MM-dd HH:mm:ss"),new Date()));
		//System.out.println(DateHelper.formatLongTime(59000));
		//System.out.println(DateHelper.getDateByAddMonth(DateHelper.stringToDate("2015-06-03 11:38:59", "yyyy-MM-dd HH:mm:ss"), 1));
		System.out.println(DateHelper.getYesterdayDate_yyyy_MM_dd());
		System.out.println(DateHelper.getCurrentDate_yyyy_MM_dd());
	}

}
