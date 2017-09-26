package com.avit.common.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间工具类
* @ClassName: DateUtils
* @Description: 时间工具类
* @author liyongquan@avit.com.cn
* @date 2017-1-9 下午5:45:52
*
 */
public class DateUtils
{
    
    //日期格式
    /**
     * yyyy-MM-dd
     */
    public static final String DATE_PATTERN_1 = "yyyy-MM-dd";
    
    /**
     * yyyyMMdd
     */
    public static final String DATE_PATTERN_2 = "yyyyMMdd";
    
    /**
     * yyyy/MM/dd
     */
    public static final String DATE_PATTERN_3 = "yyyy/MM/dd";
    
    //时间格式
    /**
     * HH:mm:ss
     */
    public static final String TIME_PATTERN_1 = "HH:mm:ss";
    
    /**
     * HHmmss
     */
    public static final String TIME_PATTERN_2 = "HHmmss";
    
    //日期时间格式
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * yyyyMMddHHmmsssss
     */
    public static final String TIMESTAMP = "yyyyMMddHHmmssSSS";
    
    /**
     * <p>the date format patterns to use,contain:</p>
     *  <p><code>DATETIME_PATTERN_1</code> = yyyy-MM-dd HH:mm:ss</p>
     *  <p><code>TIMESTAMP</code> = yyyyMMddHHmmssSSS</p>
     *  <p><code>DATE_PATTERN_1</code> = yyyy-MM-dd</p>
     *  <p><code>DATE_PATTERN_2</code> = yyyyMMdd</p>
     *  <p><code>DATE_PATTERN_3</code> = yyyy/MM/dd</p>
     */
    public static final String[] PARSEPATTERNS = new String[] {DATE_PATTERN_1, DATE_PATTERN_2, DATE_PATTERN_3, DATETIME_PATTERN_1, TIMESTAMP};
    
    /**
     * 
    * Parses a string representing a date by trying a variety of different parsers.
    * default use parsers is <code>static final</code> field <code>PARSEPATTERNS</code>
    * @param str the date to parse, not null
    * @return the parsed date
    * @throws IllegalArgumentException if the date string is null
    * @throws ParseException if none of the date patterns were suitable
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 上午10:52:06
     */
    public static Date parseDate(String str) throws ParseException
    {
        return org.apache.commons.lang.time.DateUtils.parseDateStrictly(str, PARSEPATTERNS);
    }
    
    /**
     * 
    * <p>Parses a string representing a date by trying a variety of different parsers.</p>
    * @param str str the date to parse, not null
    * @param parsePatterns the date format patterns to use, see SimpleDateFormat, not null
    * @return the parsed date
    * @throws IllegalArgumentException if the date string is null
    * @throws ParseException if none of the date patterns were suitable
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 上午10:52:06
     */
    public static Date parseDate(String str, String[] parsePatterns) throws ParseException
    {
        return org.apache.commons.lang.time.DateUtils.parseDateStrictly(str, parsePatterns);
    }
    

    /**
     * 
    * <p>Formats a Date into a date/time string by pattern.</p>
    * @param date
    * @param pattern 
    * @return the formatted time string
    * @throws IllegalArgumentException if the date string or pattern is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午2:43:34
     */
    public static String formatDate(Date date, String pattern)
    {
        if (null == date || null == pattern)
        {
            throw new IllegalArgumentException("Date and pattern must not be null");
        }
        SimpleDateFormat formater = new SimpleDateFormat();
        formater.setLenient(false);
        formater.applyPattern(pattern);
        return formater.format(date);
    }
    
    /**
     * 
    * <p>Formats a Date into a date/time string by patterns and put into a Map.</p>
    * @param date
    * @param patterns 自定义的时间模板
    * @return Map  key值为<code>patterns</code>中的元素，values值为对应格式的时间串
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 上午10:39:50
     */
    public static Map<String, String> formatDate(Date date, String[] patterns)
    {
        if (null == date || null == patterns)
        {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        Map<String, String> result = new HashMap<String, String>();
        for (String pattern : patterns)
        {
            String dateS = formatDate(date, pattern);
            if (null != dateS && !"".equals(dateS) && dateS.length() == pattern.length())
            {
                result.put(pattern, dateS);
            }
        }
        return result;
    }
    
    /**
     *
    * <p>Formats a Date into a date/time string by patterns and put into a Map.</p>
    * default use <code>static final</code> field <code>PARSEPATTERNS</code> as patterns
    * @param date
    * @return Map key值为<code>PARSEPATTERNS</code>中的元素，values值为对应格式的时间串
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 上午10:44:28
     */
    public static Map<String, String> formatDate(Date date)
    {
        return formatDate(date, PARSEPATTERNS);
    }
    
    /**
     * default use <code>static final</code> field <code>PARSEPATTERNS</code>
    * <p>获取当前时间格式串</p>
    * @return Map key值为<code>PARSEPATTERNS</code>中的元素，values值为对应格式的时间串
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午2:19:15
     */
    public static Map<String, String> getCurrentDateStr()
    {
        return formatDate(Calendar.getInstance().getTime());
    }
    
    /**
     * 
    * <p>获取指定格式的当前时间串</p>
    * @param pattern
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午2:43:01
     */
    public static String getCurrentDateStr(String pattern)
    {
        return formatDate(Calendar.getInstance().getTime(), pattern);
    }
    
    /**
     * 
    * <p>根据自定义的时间格式模板获取当前时间对于格式串</p>
    * @param patterns 自定义的时间模板
    * @return Map  key值为<code>patterns</code>中的元素，values值为对应格式的时间串
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午2:21:43
     */
    public static Map<String, String> getCurrentDateStr(String[] patterns)
    {
        return formatDate(Calendar.getInstance().getTime(), patterns);
    }
    
    /**
     * 
    * <p>根据<code>calendarField</code>获取指定<code>date</code>的对应field值,注意月份是从0-11</p>
    * @param date
    * @param calendarField the calendar field to get value
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午2:55:13
     */
    public static int getCalendarField(Date date, int calendarField)
    {
        if (date == null)
        {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(calendarField);
    }
    
    /**
     * 
    * <p>根据<code>calendarField</code>获取当前时间的对应field值</p>
    * @param date
    * @param calendarField the calendar field to get value
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午2:55:13
     */
    public static int getCurCalendarField(int calendarField)
    {
        return getCalendarField(Calendar.getInstance().getTime(), calendarField);
    }
    
    /**
     * 
    * <p>对指定日期时间基础上加减年</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addYears(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addYears(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减月</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addMonths(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addMonths(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减周</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addWeeks(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addWeeks(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减天</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addDays(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addDays(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减小时</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addHours(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addHours(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减分钟</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addMinutes(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addMinutes(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减秒</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addSeconds(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addSeconds(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减毫秒</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    public static Date addMilliseconds(Date date, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.addMilliseconds(date, amount);
    }
    /**
     * 
    * <p>对指定日期时间基础上加减指定属性值</p>
    * @param date
    * @param amount
    * @return
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午3:58:37
     */
    @SuppressWarnings("deprecation")
    public static Date add(Date date, int calendarField, int amount)
    {
        return org.apache.commons.lang.time.DateUtils.add(date, calendarField, amount);
    }
    
    /**
     * 
    * <p>获取指定日期所在周的第一天</p>
    * @param date  the date, not null
    * @param firstDayOfWeek the given first day of the week;
    * e.g., <code>Calendar.MONDAY</code> or <code>Calendar.SUNDAY</code>
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午6:06:45
     */
    public static Date getFirstDayOfWeek(Date date,int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                      calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }
    
    /**
     * 
    * <p>获取指定日期所在周的第一天，默认从星期一开始</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午6:06:45
     */
    public static Date getFirstDayOfWeek(Date date) {
        return getFirstDayOfWeek(date,Calendar.MONDAY);
    }
    
    /**
    * <p>获取指定日期所在周的最后一天</p>
    * @param date  the date, not null
    * @param firstDayOfWeek the given first day of the week;
    * e.g., <code>Calendar.MONDAY</code> or <code>Calendar.SUNDAY</code>
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午6:06:45
     */
    public static Date getLastDayOfWeek(Date date,int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayOfWeek(date, firstDayOfWeek));
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }
    
    /**
    * <p>获取指定日期所在周的最后一天，默认从星期一开始</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-6 下午6:06:45
     */
    public static Date getLastDayOfWeek(Date date) {
        return getLastDayOfWeek(date,Calendar.MONDAY);
    }
    
    /**
     * 
    * <p>获取加减周后的周第一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param firstDayOfWeek the given first day of the week;
    * e.g., <code>Calendar.MONDAY</code> or <code>Calendar.SUNDAY</code>
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:02:19
     */
    public static Date getFirstDayOfAddWeek(Date date,int firstDayOfWeek,int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                      calendar.getFirstDayOfWeek());
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return calendar.getTime();
    }
    
    /**
     * 
    * <p>获取加减周后的周第一天,默认从星期一开始, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:02:19
     */
    public static Date getFirstDayOfAddWeek(Date date,int amount) {
        return getFirstDayOfAddWeek(date,Calendar.MONDAY,amount);
    }
    
    /**
     * 
    * <p>获取加减周后的周最后一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param firstDayOfWeek the given first day of the week;
    * e.g., <code>Calendar.MONDAY</code> or <code>Calendar.SUNDAY</code>
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:06:23
     */
    public static Date getLastDayOfAddWeek(Date date,int firstDayOfWeek,int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayOfAddWeek(date, firstDayOfWeek,amount));
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }
    
    /**
     * 
    * <p>获取加减周后的周最后一天,默认从星期一开始 , <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:06:23
     */
    public static Date getLastDayOfAddWeek(Date date,int amount) {
        return getLastDayOfAddWeek(date,Calendar.MONDAY,amount);
    }
    
    /**
     * 
    * <p>获取指定日期所在月的第一天</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:18:55
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }
    
    /**
     * 
    * <p>获取指定日期加减月后所在月的第一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:40:08
     */
    public static Date getFirstDayOfAddMonth(Date date,int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        return getFirstDayOfMonth(calendar.getTime());
    }
    
    /**
     * 
    * <p>获取指定日期所在月的最后一天</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:18:55
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }
    
    /**
     * 
    * <p>获取指定日期加减月后所在月的最后一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午10:45:00
     */
    public static Date getLastDayOfAddMonth(Date date,int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        return getLastDayOfMonth(calendar.getTime());
    }
    
    /**
     * 
    * <p>获取指定日期所在的季度</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午11:17:05
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }
    
    /**
     * 
    * <p>获取指定日期所在季度的第一天</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午11:40:04
     */
    public static Date getFirstDayOfQuarter(Date date) {
        int quarter = getQuarterOfYear(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(getCalendarField(date, Calendar.YEAR),(quarter-1)*3,1);
        return calendar.getTime();
    }
    
    /**
     * 
    *<p>获取指定日期加减季度后所在季度的第一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws IllegalArgumentException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午11:44:46
     */
    public static Date getFirstDayOfAddQuarter(Date date,int amount) {
        return getFirstDayOfQuarter(addMonths(date, amount*3));
    }
    
    /**
     * 
    * <p>获取指定日期所在季度的最后一天</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午11:40:36
     */
    public static Date getLastDayOfQuarter(Date date) {
        return getLastDayOfAddMonth(getFirstDayOfQuarter(date), 2);
    }
    
    /**
     * 
    * <p>获取指定日期加减季度后所在季度的最后一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws IllegalArgumentException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 上午11:40:36
     */
    public static Date getLastDayOfAddQuarter(Date date,int amount) {
        return getLastDayOfAddMonth(getFirstDayOfAddQuarter(date,amount), 2);
    }
    
    /**
     * 
    * <p>获取指定日期所在年的第一天</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午1:52:17
     */
    public static Date getFirstDayOfYear(Date date){ 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(getCalendarField(date, Calendar.YEAR), 0, 1);
        return calendar.getTime();  
    }
    
    /**
     * 
    * <p>获取指定日期加减年后所在年的第一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午1:57:08
     */
    public static Date getFirstDayOfAddYear(Date date,int amount){ 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(getCalendarField(date, Calendar.YEAR), 0, 1);
        calendar.add(Calendar.YEAR, amount);
        return calendar.getTime();  
    }
    
    /**
     * 
    * <p>获取指定日期所在年的最后一天</p>
    * @param date  the date, not null
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午1:55:49
     */
    public static Date getLastDayOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayOfYear(date));
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();  
    }
    
    /**
     * 
    * <p>获取指定日期加减年后所在年的最后一天, <code>amount</code>为负数表示减</p>
    * @param date  the date, not null
    * @param amount  the amount to add, may be negative
    * @return the new date object with the amount added
    * @throws NullPointerException if the date is null
    * @author liyongquan@avit.com.cn
    * @date 2017-1-9 下午1:57:08
     */
    public static Date getLastDayOfAddYear(Date date,int amount){ 
        return getLastDayOfYear(getFirstDayOfAddYear(date,amount));  
    }
}
