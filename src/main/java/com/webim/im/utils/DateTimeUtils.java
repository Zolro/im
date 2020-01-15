/*
 * 文 件 名:  DateTimeUtil.java
 * 描    述:  日期和时间工具类文件
 */
package com.webim.im.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author liyichao 日期和时间工具类
 *     <p>提供日期和时间处理公共方法实现
 */
public final class DateTimeUtils {
  /** 日期格式：yyyy-MM-dd HH:mm:ss */
  public static final String DATE_FARMAT = "yyyy-MM-dd HH:mm:ss";

  /** 日期格式：yyyyMMddHHmmss */
  public static final String DATE_FORMAT_2 = "yyyyMMddHHmmss";

  /** 日期格式：yyyy-MM-dd */
  public static final String DATE_FARMAT_3 = "yyyy-MM-dd";

  /** 日期格式：yyyyMMdd */
  public static final String DATE_FARMAT_4 = "yyyyMMdd";

  /** 日期格式：yyyy-MM */
  public static final String DATE_FARMAT_5 = "yyyy-MM";

  /** 日期格式：yyMMdd */
  public static final String DATE_FARMAT_6 = "yyMMdd";

  /** 日期格式：yyyyMMddHHmmssSSS 毫秒 */
  public static final String DATE_FORMAT_7 = "yyyyMMddHHmmssSSS";

  /** 日期格式：mm:ss */
  public static final String DATE_FARMAT_8 = "HH:mm";

  /** 日期格式：yyyy-MM-dd HH:mm */
  public static final String DATE_FARMAT_9 = "yyyy-MM-dd HH:mm";

  /** 日期格式：mm:ss */
  public static final String DATE_FARMAT_10 = "HHmm";

  /** 数据库存储的date类型字段转成JAVA的String类型为2006-06-11 03:35:13.0，需要去掉后面的.0 */
  private static final String DATE_STRING_EXTRA = ".0";

  /** 默认构造方法 不允许实例化 */
  private DateTimeUtils() {}

  /**
   * 日期时间字符串格式转换
   *
   * <p>数据库存储的date类型字段转成JAVA的String类型为2006-06-11 03:35:13.0，需要去掉后面的.0
   *
   * @param dateStr 日期时间字符串
   * @return 去掉.0的日期时间字符串
   * @see [类、类#方法、类#成员]
   */
  public static String formatDateString(String dateStr) {
    if (null != dateStr) {
      // 后面包含.0日期时间字符串才需要处理
      int index = dateStr.lastIndexOf(DATE_STRING_EXTRA);
      if (0 < index) {
        dateStr = dateStr.substring(0, index);
      }
    }

    return dateStr;
  }

  public static Date getDateBefore(Date d, int day) {
    Calendar now = Calendar.getInstance();
    now.setTime(d);
    now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
    return now.getTime();
  }

  public static Date addDate(Date date, long day) throws Exception {
    long time = date.getTime(); // 得到指定日期的毫秒数
    day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
    time += day; // 相加得到新的毫秒数
    return new Date(time); // 将毫秒数转换成日期
  }

  /**
   * 将date型日期转换为想要的字符格式 <一句话功能简述> <功能详细描述>
   *
   * @param date date日期
   * @param dateFormat 日期格式：如yyyy-MM-dd HH:mm:ss
   * @return String
   */
  public static String formatDateToString(Date date, String dateFormat) {
    if (date == null) {
      return "";
    }
    SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    return format.format(date);
  }

  /**
   * 将String型日期转换为想要的date型 <一句话功能简述> <功能详细描述>
   *
   * @param currentTime currentTime
   * @return Date
   * @see [类、类#方法、类#成员]
   */
  public static Date formatStringToDate(String currentTime) {
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat(DATE_FARMAT);
    try {
      date = df.parse(currentTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * 将String型日期转换为想要的date型 <一句话功能简述> <功能详细描述>
   *
   * @param time currentTime
   * @param dataFarmat
   * @return Date
   * @see [类、类#方法、类#成员]
   */
  public static Date formatStringToDate(String time, String dataFarmat) {
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat(dataFarmat);
    try {
      date = df.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public static Date getPreviousDay(Date date, int interval) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -interval);
    date = calendar.getTime();
    return date;
  }

  /**
   * 将String型日期转换为想要的date型 <一句话功能简述> <功能详细描述>
   *
   * @param currentTime currentTime
   * @return Date
   * @see [类、类#方法、类#成员]
   */
  public static Date formatStringToDate3(String currentTime) {
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat(DATE_FARMAT_3);
    try {
      date = df.parse(currentTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * 得到本周周一
   *
   * @return yyyy-MM-dd
   */
  public static String getMondayOfThisWeek() {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FARMAT_3);
    Calendar c = Calendar.getInstance();
    int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
    if (day_of_week == 0) day_of_week = 7;
    c.add(Calendar.DATE, -day_of_week + 1);
    return df.format(c.getTime());
  }

  /**
   * 得到本周周日
   *
   * @return yyyy-MM-dd
   */
  public static String getSundayOfThisWeek() {
    SimpleDateFormat df = new SimpleDateFormat(DATE_FARMAT_3);
    Calendar c = Calendar.getInstance();
    int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
    if (day_of_week == 0) day_of_week = 7;
    c.add(Calendar.DATE, -day_of_week + 7);
    return df.format(c.getTime());
  }

  /**
   * 得到当前格林威治的日期和时间
   *
   * @return String
   */
  public static String getUTCDateTimeNow() {
    // 取时区
    TimeZone zone = TimeZone.getDefault();

    Calendar c = Calendar.getInstance();

    Date date = new Date();

    // 计算时区偏差
    c.setTimeInMillis(date.getTime() - zone.getOffset(date.getTime()));

    // 格式化
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FARMAT);

    String dateString = formatter.format(c.getTime());

    return dateString;
  }

  // 获得当天0点时间
  public static Date getTimesmorning(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
  public static Date getTimesmorning() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
  // 获得昨天0点时间
  public static Date getYesterdaymorning() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000);
    return cal.getTime();
  }

  // 获得当天近7天时间
  public static Date getWeekFromNow() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000 * 7);
    return cal.getTime();
  }

  // 获得当天24点时间
  public static Date getTimesnight() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 24);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  // 获得本周一0点时间
  public static Date getTimesWeekmorning() {
    Calendar cal = Calendar.getInstance();
    cal.set(
        cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    return cal.getTime();
  }

  // 获得本周日24点时间
  public static Date getTimesWeeknight() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getTimesWeekmorning());
    cal.add(Calendar.DAY_OF_WEEK, 7);
    return cal.getTime();
  }

  // 获得本月第一天0点时间
  public static Date getTimesMonthmorning() {
    return getTimesMonthmorning(new Date());
  }

  // 获得本月最后一天24点时间
  public static Date getTimesMonthnight() {
    return getTimesMonthnight(new Date());
  }

  /**
   * 获得传入时间的月份第一天0点时间
   *
   * @return
   */
  public static Date getTimesMonthmorning(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(
        cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    return cal.getTime();
  }

  /**
   * 获得传入时间的月份最后一天24点时间
   *
   * @return
   */
  public static Date getTimesMonthnight(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(
        cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 24);
    return cal.getTime();
  }

  public static Date getLastMonthStartMorning() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getTimesMonthmorning());
    cal.add(Calendar.MONTH, -1);
    return cal.getTime();
  }

  public static Date getCurrentQuarterStartTime() {
    Calendar c = Calendar.getInstance();
    int currentMonth = c.get(Calendar.MONTH) + 1;
    SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    Date now = null;
    try {
      if (currentMonth >= 1 && currentMonth <= 3) c.set(Calendar.MONTH, 0);
      else if (currentMonth >= 4 && currentMonth <= 6) c.set(Calendar.MONTH, 3);
      else if (currentMonth >= 7 && currentMonth <= 9) c.set(Calendar.MONTH, 4);
      else if (currentMonth >= 10 && currentMonth <= 12) c.set(Calendar.MONTH, 9);
      c.set(Calendar.DATE, 1);
      now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return now;
  }

  /**
   * 当前季度的结束时间，即2012-03-31 23:59:59
   *
   * @return
   */
  public static Date getCurrentQuarterEndTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentQuarterStartTime());
    cal.add(Calendar.MONTH, 3);
    return cal.getTime();
  }

  /**
   * 获取本年开始时间
   *
   * @return
   */
  public static Date getCurrentYearStartTime() {
    Calendar cal = Calendar.getInstance();
    cal.set(
        cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
    return cal.getTime();
  }

  /**
   * 获取本年结束时间
   *
   * @return
   */
  public static Date getCurrentYearEndTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentYearStartTime());
    cal.add(Calendar.YEAR, 1);
    return cal.getTime();
  }

  /**
   * 获取去年结束时间
   *
   * @return
   */
  public static Date getLastYearStartTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentYearStartTime());
    cal.add(Calendar.YEAR, -1);
    return cal.getTime();
  }

  /**
   * 获取两个时间的时间差（小时）
   *
   * @param time1
   * @param time2
   * @return
   */
  public static Long timeDiffForHour(Date time1, Date time2) {
    long diff = time2.getTime() - time1.getTime();

    long nh = 1000 * 60 * 60; // 一小时的毫秒数

    long diffHour = diff / nh; // 计算差多少小时

    return diffHour;
  }

  /**
   * 获取两个时间的时间差（天）
   *
   * @param time1
   * @param time2
   * @return
   */
  public static Long timeDiffForDay(Date time1, Date time2) {
    long diff = time2.getTime() - time1.getTime();
    long nh = 1000 * 60 * 60 * 24; // 一小时的毫秒数
    long diffDay = diff / nh; // 计算差多少小时
    if (diff % nh > 0) {
      diffDay += 1;
    }
    return diffDay;
  }


  /**
   * 获得距离系统当前时间days天前的时间
   *
   * @auther liyichao
   * @param days
   * @return
   */
  public static Date timeDiffNowDay(int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
    return calendar.getTime();
  }

  /**
   * 获取两个时间的时间差（分钟）
   *
   * @param time1
   * @param time2
   * @return
   */
  public static Long timeDiffForMin(Date time1, Date time2) {
    long diff = time2.getTime() - time1.getTime();

    long nh = 1000 * 60; // 一分钟的毫秒数

    long diffHour = diff / nh; // 计算差多少分钟

    return diffHour;
  }

  /**
   * 获取两个时间的时间差（分钟）
   */
  public static Long timeDiffForMin(long time1) {
    Date now = new Date();
    long diff = now.getTime() - time1;

    long nh = 1000 * 60; // 一分钟的毫秒数

    long diffHour = diff / nh; // 计算差多少分钟

    return diffHour;
  }

  /**
   * 分钟前
   *
   * @param date
   * @param interval
   * @return
   */
  public static Date getPreviousMin(Date date, int interval) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MINUTE, -interval);
    date = calendar.getTime();
    return date;
  }

  public static String timeDifferent(Date starttime, Date endtime) {
    String str = new String();
    long dif = (endtime.getTime() - starttime.getTime()) / 1000;
    long year = 365 * 24 * 60 * 60;
    long month = 30 * 24 * 60 * 60;
    long day = 24 * 60 * 60;
    long hour = 60 * 60;
    long minute = 60;
    if (dif > year) {
      str = Math.round((double) dif / year) + "年前";
    } else if (dif > month) {
      str = Math.round((double) dif / month) + "月前";
    } else if (dif > day) {
      str = Math.round((double) dif / day) + "天前";
    } else if (dif > hour) {
      str = Math.round((double) dif / hour) + "小时前";
    } else if (dif > minute) {
      str = Math.round((double) dif / minute) + "分钟前";
    } else if (dif < 60 && dif > 10) {
      str = dif + "秒前";
    } else {
      str = "刚刚";
    }
    return str;
  }

  /**
   * 获取只包含年月日的日期格式
   *
   * @param date
   * @return
   */
  public static Date getTheDateNumber(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FARMAT_3);
    String res = sdf.format(date);
    Date parseResult = null;
    try {
      parseResult = sdf.parse(res);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return parseResult;
  }
  /**
     * 获取只包含年月日的日期格式
     *
     * @param date
     * @return
     */
    public static Date getDateStart(Date date) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar1.getTime();
        return beginOfDate;
    }

    /**
     * 获取只包含年月日的日期格式
     *
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        Date endOfDate = calendar2.getTime();
        return endOfDate;
    }

  public static Date getTodayBegin(LocalDate todayDate) {
    return Date.from(todayDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date getTodayEnd(LocalDate todayDate) {
    return Date.from(todayDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
  }

  public static String now(String pattern){
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      return simpleDateFormat.format(new Date());
  }
}
