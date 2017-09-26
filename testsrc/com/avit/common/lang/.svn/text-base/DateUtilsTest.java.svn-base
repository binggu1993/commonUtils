package com.avit.common.lang;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

public class DateUtilsTest extends TestCase
{
    public void testParseDate_ParamString_Null(){
        try
        {
            DateUtils.parseDate(null);
            fail("IllegalArgumentException 异常未抛出");
        }
        catch (Exception e)
        {
            assertTrue("方法参数str为null时,未抛出异常:"+e, e instanceof IllegalArgumentException);
        }
    }
    
    public void testParseDate_ParamString_Valid(){
        try
        {
            Date date = DateUtils.parseDate("2016-12-22");
            assertEquals("字符串转换日期值与预期不一致",1482336000000L, date.getTime());
        }
        catch (Exception e)
        {
            fail("方法参数str为合法时,不能抛出异常:"+e);
        }
    }
    
    public void testParseDate_ParamString_NotValid(){
        try
        {
            DateUtils.parseDate("2016-12-22--");
            fail("param Str=[2016-12-22--],未抛出异常:ParseException");
        }
        catch (Exception e)
        {
            assertTrue("方法参数str为[2016-12-22--]时,未抛出异常:"+e, e instanceof ParseException);
        }
        try
        {
            DateUtils.parseDate("2016-12-40");
            fail("param Str=[2016-12-40],未抛出异常:ParseException");
        }
        catch (Exception e)
        {
            assertTrue("方法参数str为[2016-12-40]时,未抛出异常:"+e, e instanceof ParseException);
        }
        try
        {
            DateUtils.parseDate("sssssss");
            fail("param Str=[sssssss],未抛出异常:ParseException");
        }
        catch (Exception e)
        {
            assertTrue("方法参数str为[sssssss]时,未抛出异常:"+e, e instanceof ParseException);
        }
    }
    
    public void testParseDate_ParamStringAndPatterns_Valid(){
        try
        {
            String[] patterns = new String[]{"yyyy-MM-dd"};
            Date date = DateUtils.parseDate("2016-12-22",patterns);
            assertEquals("字符串转换日期值与预期不一致",1482336000000L, date.getTime());
        }
        catch (Exception e)
        {
            fail("方法参数str为合法时,不能抛出异常:"+e);
        }
    }
    
    public void testParseDate_ParamStringAndPatterns_NullPatterns(){
        
        try
        {
            String[] patterns = null;
            DateUtils.parseDate("2016-12-22",patterns);
            fail("IllegalArgumentException 异常未抛出");
        }
        catch (Exception e)
        {
            assertTrue("方法参数patterns为null时,未抛出异常:"+e, e instanceof IllegalArgumentException);
        }
    }
    
    public void testParseDate_ParamStringAndPatterns_NotValid(){
        try
        {
            String[] patterns = new String[]{"yyyy-MM-dd"};
            Date date = DateUtils.parseDate("2016-12-22",patterns);
            assertEquals("字符串转换日期值与预期不一致",1482336000000L, date.getTime());
        }
        catch (Exception e)
        {
            fail("方法参数str为合法时,不能抛出异常:"+e);
        }
    }
    
    
    public void testFormatDate_UseDefaultPatterns(){
        try
        {
            String excepted = "2016-12-22 23:59:59";
            Date date = DateUtils.parseDate(excepted);
            Map<String,String> map = DateUtils.formatDate(date);
            assertEquals(map.size(), DateUtils.PARSEPATTERNS.length);
            
            String result = map.get(DateUtils.DATETIME_PATTERN_1);
            assertEquals(excepted,result);
            
            excepted = "2016/12/22";
            result = map.get(DateUtils.DATE_PATTERN_3);
            assertEquals(excepted,result);
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testFormatDate_NorbmlPatternsParams(){
        try{
            String excepted = "2016-12-22 23:59:59";
            Date date = DateUtils.parseDate(excepted);
            String[] patterns = new String[]{"yyyyMMddHHmmss","yyyy"};
            Map<String,String> map = DateUtils.formatDate(date,patterns);
            assertEquals(map.get(patterns[0]),"20161222235959");
            assertEquals(map.get(patterns[1]),"2016");
            
        }catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    public void testFormatDate_IllegalArgumentException(){
        try{
            String[] patterns = new String[]{"yyyyMMddsshhmmss","yyyygg"};
            Map<String,String> map = DateUtils.formatDate(new Date(),patterns);
            fail("参数异常未抛出");
            for(int i=0;i<patterns.length;i++){
                assertEquals(map.get(patterns[i]).length(),patterns[i].length());
            }
            
        }catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    
    public void testGetCurrentDateStr(){
        String expected = DateUtils.formatDate(new Date(), DateUtils.DATETIME_PATTERN_1);
        String actual = DateUtils.getCurrentDateStr().get(DateUtils.DATETIME_PATTERN_1);
        assertEquals(expected, actual);
    }
    
    public void testGetCalendarField(){
        try{
            String dateS = "2016-12-22 23:59:59";
            Date date = DateUtils.parseDate(dateS);
            int result = DateUtils.getCalendarField(date,Calendar.YEAR);
            int excepted = 2016;
            assertEquals(excepted,result);
            
            result = DateUtils.getCalendarField(date,Calendar.MONTH);
            excepted = 11;//月份从0-11
            assertEquals(excepted,result);
            
        }catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetCurCalendarField(){
        String expected = DateUtils.formatDate(new Date(), "yyyy");
        int year = DateUtils.getCurCalendarField(Calendar.YEAR);
        assertEquals(expected, year+"");
    }
    
    public void testAddFields(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-31 23:59:59");
            
            Date expected = DateUtils.parseDate("2018-01-31 23:59:59");
            Date result = DateUtils.addYears(goal, 1);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addYears(goal,-1);
            expected = DateUtils.parseDate("2016-01-31 23:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addMonths(goal,1);
            expected = DateUtils.parseDate("2017-02-28 23:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addWeeks(goal,1);
            expected = DateUtils.parseDate("2017-02-07 23:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addWeeks(goal,-5);
            expected = DateUtils.parseDate("2016-12-27 23:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addDays(goal,-31);
            expected = DateUtils.parseDate("2016-12-31 23:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addDays(goal,1);
            expected = DateUtils.parseDate("2017-02-01 23:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.addHours(goal,1);
            expected = DateUtils.parseDate("2017-02-01 0:59:59");
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
            
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfWeek_ParamDateAndInt_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-09 23:59:59");
            Date result = DateUtils.getFirstDayOfWeek(goal, Calendar.MONDAY);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getFirstDayOfWeek(goal, Calendar.SUNDAY);
            expected = DateUtils.parseDate("2017-01-08 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfWeek_ParamDateAndInt_DateNull(){
        
        try{
            Date date = null;
            DateUtils.getFirstDayOfWeek(date, Calendar.MONDAY);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetFirstDayOfWeek_ParamDate_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-09 23:59:59");
            Date result = DateUtils.getFirstDayOfWeek(goal);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfWeek_ParamDate_DateNull(){
        
        try{
            Date date = null;
            DateUtils.getFirstDayOfWeek(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    

    public void testGetLastDayOfWeek_ParamDateAndInt_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-15 23:59:59");
            Date result = DateUtils.getLastDayOfWeek(goal, Calendar.MONDAY);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getLastDayOfWeek(goal, Calendar.SUNDAY);
            expected = DateUtils.parseDate("2017-01-14 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfWeek_ParamDateAndInt_DateNull(){
        
        try{
            Date date = null;
            DateUtils.getLastDayOfWeek(date, Calendar.MONDAY);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetLastDayOfWeek_ParamDate_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-15 23:59:59");
            Date result = DateUtils.getLastDayOfWeek(goal);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfWeek_ParamDate_DateNull(){
        
        try{
            Date date = null;
            DateUtils.getLastDayOfWeek(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }

    public void testGetFirstDayOfAddWeek_3Param_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-16 23:59:59");
            Date result = DateUtils.getFirstDayOfAddWeek(goal, Calendar.MONDAY,1);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getFirstDayOfAddWeek(goal, Calendar.MONDAY,-2);
            expected = DateUtils.parseDate("2016-12-26 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getFirstDayOfAddWeek(goal, Calendar.SUNDAY,1);
            expected = DateUtils.parseDate("2017-01-15 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getFirstDayOfAddWeek(goal, Calendar.SUNDAY,-2);
            expected = DateUtils.parseDate("2016-12-25 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfAddWeek_3Param_DateNull(){
            
            try{
                Date date = null;
                DateUtils.getFirstDayOfAddWeek(date,Calendar.MONDAY,1);
                fail("未抛出空指针异常");
            }catch (Exception e) {
                assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
            }
    }
    
    public void testGetFirstDayOfAddWeek_2Param_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-16 23:59:59");
            Date result = DateUtils.getFirstDayOfAddWeek(goal,1);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getFirstDayOfAddWeek(goal,-2);
            expected = DateUtils.parseDate("2016-12-26 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
            
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfAddWeek_2Param_DateNull(){
            
            try{
                Date date = null;
                DateUtils.getFirstDayOfAddWeek(date,1);
                fail("未抛出空指针异常");
            }catch (Exception e) {
                assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
            }
    }
    
   
    public void testGetLastDayOfAddWeek_3Param_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-22 23:59:59");
            Date result = DateUtils.getLastDayOfAddWeek(goal, Calendar.MONDAY,1);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getLastDayOfAddWeek(goal, Calendar.MONDAY,-2);
            expected = DateUtils.parseDate("2017-01-01 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getLastDayOfAddWeek(goal, Calendar.SUNDAY,1);
            expected = DateUtils.parseDate("2017-01-21 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getLastDayOfAddWeek(goal, Calendar.SUNDAY,-2);
            expected = DateUtils.parseDate("2016-12-31 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfAddWeek_3Param_DateNull(){
            
            try{
                Date date = null;
                DateUtils.getLastDayOfAddWeek(date,Calendar.MONDAY,1);
                fail("未抛出空指针异常");
            }catch (Exception e) {
                assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
            }
    }
    
    public void testGetLastDayOfAddWeek_2Param_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-09 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-22 23:59:59");
            Date result = DateUtils.getLastDayOfAddWeek(goal,1);
            assertEquals("firstDay为MONDAY时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getLastDayOfAddWeek(goal,-2);
            expected = DateUtils.parseDate("2017-01-01 23:59:59");
            assertEquals("firstDay为SUNDAY时两时间不一致",expected.getTime(), result.getTime());
            
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfAddWeek_2Param_DateNull(){
            
            try{
                Date date = null;
                DateUtils.getLastDayOfAddWeek(date,1);
                fail("未抛出空指针异常");
            }catch (Exception e) {
                assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
            }
    }
    
    
    public void testGetFirstDayOfMonth_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-01 23:59:59");
            Date result = DateUtils.getFirstDayOfMonth(goal);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfMonth_DateNull(){
        try{
            Date date = null;
            DateUtils.getFirstDayOfMonth(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    
    public void testGetFirstDayOfAddMonth_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-02-01 23:59:59");
            Date result = DateUtils.getFirstDayOfAddMonth(goal, 1);
            assertEquals("amount为1时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getFirstDayOfAddMonth(goal, -1);
            expected = DateUtils.parseDate("2016-12-01 23:59:59");
            assertEquals("amount为-1跨年时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfAddMonth_DateNull(){
        try{
            Date date = null;
            DateUtils.getFirstDayOfAddMonth(date , 1);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetLastDayOfMonth_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-31 23:59:59");
            Date result = DateUtils.getLastDayOfMonth(goal);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfMonth_DateNull(){
        try{
            Date date = null;
            DateUtils.getLastDayOfMonth(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetLastDayOfAddMonth_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-02-28 23:59:59");
            Date result = DateUtils.getLastDayOfAddMonth(goal, 1);
            assertEquals("amount为1时两时间不一致",expected.getTime(), result.getTime());
            
            result = DateUtils.getLastDayOfAddMonth(goal, -1);
            expected = DateUtils.parseDate("2016-12-31 23:59:59");
            assertEquals("amount为-1跨年时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfAddMonth_DateNull(){
        try{
            Date date = null;
            DateUtils.getLastDayOfAddMonth(date , 1);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    
    public void testGetQuarterOfYear_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            int result = DateUtils.getQuarterOfYear(goal);
            assertEquals("两季度不一致",1, result);
            
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetQuarterOfYear_DateNull(){
        try{
            Date date = null;
            DateUtils.getQuarterOfYear(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    public void testGetFirstDayOfQuarter_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-01 23:59:59");
            Date result = DateUtils.getFirstDayOfQuarter(goal);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfQuarter_DateNull(){
        try{
            Date date = null;
            DateUtils.getFirstDayOfQuarter(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetFirstDayOfAddQuarter_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2016-10-01 23:59:59");
            Date result = DateUtils.getFirstDayOfAddQuarter(goal,-1);
            assertEquals("amount为-1跨年时两时间不一致",expected.getTime(), result.getTime());
            
            expected = DateUtils.parseDate("2017-07-01 23:59:59");
            result = DateUtils.getFirstDayOfAddQuarter(goal,2);
            assertEquals("amount为2时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfAddQuarter_DateNull(){
        try{
            Date date = null;
            DateUtils.getFirstDayOfAddQuarter(date,1);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof IllegalArgumentException);
        }
    }
    
    public void testGetLastDayOfQuarter_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-11-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-12-31 23:59:59");
            Date result = DateUtils.getLastDayOfQuarter(goal);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfQuarter_DateNull(){
        try{
            Date date = null;
            DateUtils.getLastDayOfQuarter(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    
    public void testGetLastDayOfAddQuarter_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2016-12-31 23:59:59");
            Date result = DateUtils.getLastDayOfAddQuarter(goal,-1);
            assertEquals("amount为-1跨年时两时间不一致",expected.getTime(), result.getTime());
            
            expected = DateUtils.parseDate("2017-9-30 23:59:59");
            result = DateUtils.getLastDayOfAddQuarter(goal,2);
            assertEquals("amount为2时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfAddQuarter_DateNull(){
        try{
            Date date = null;
            DateUtils.getLastDayOfAddQuarter(date,1);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof IllegalArgumentException);
        }
    }
    
    public void testGetFirstDayOfYear_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2017-01-01 23:59:59");
            Date result = DateUtils.getFirstDayOfYear(goal);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfYear_DateNull(){
        try{
            Date date = null;
            DateUtils.getFirstDayOfYear(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetFirstDayOfAddYear_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2016-01-01 23:59:59");
            Date result = DateUtils.getFirstDayOfAddYear(goal,-1);
            assertEquals("amount为-1跨年时两时间不一致",expected.getTime(), result.getTime());
            
            expected = DateUtils.parseDate("2018-01-01 23:59:59");
            result = DateUtils.getFirstDayOfAddYear(goal,1);
            assertEquals("amount为1跨年时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetFirstDayOfAddYear_DateNull(){
        try{
            Date date = null;
            DateUtils.getFirstDayOfAddYear(date,1);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    public void testGetLastDayOfYear_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2016-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2016-12-31 23:59:59");
            Date result = DateUtils.getLastDayOfYear(goal);
            assertEquals("两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfYear_DateNull(){
        try{
            Date date = null;
            DateUtils.getLastDayOfYear(date);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
    
    
    public void testGetLastDayOfAddYear_Normal(){
        try
        {
            Date goal = DateUtils.parseDate("2017-01-10 23:59:59");
            Date expected = DateUtils.parseDate("2016-12-31 23:59:59");
            Date result = DateUtils.getLastDayOfAddYear(goal,-1);
            assertEquals("amount为-1跨年时两时间不一致",expected.getTime(), result.getTime());
            
            expected = DateUtils.parseDate("2018-12-31 23:59:59");
            result = DateUtils.getLastDayOfAddYear(goal,1);
            assertEquals("amount为1跨年时两时间不一致",expected.getTime(), result.getTime());
        }
        catch (ParseException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testGetLastDayOfAddYear_DateNull(){
        try{
            Date date = null;
            DateUtils.getLastDayOfAddYear(date,1);
            fail("未抛出空指针异常");
        }catch (Exception e) {
            assertTrue("抛出的异常不是空指针异常",e instanceof NullPointerException);
        }
    }
}
