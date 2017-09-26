package com.avit.common.lang;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;;


/**
 * String类组件
 *@author hudongyu
 *@date 2017-1-6
 */
public class StringUtil {
	
	public static final String EMPTY = "";
	
	/**
	 * 去除去除空指针替换到""的方法
	 * 
	 * @param str
	 * @return
	 */
	public static String defaultString(final String str) {
        return str == null ?  EMPTY: str;
    }
	
	/**
	 * 将null元素替换为默认字符序列
	 * 
	 * @param str
	 * @param defaultStr
	 * @return
	 */
	public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
	
	/**
	 * 将null或""元素替换为默认字符序列
	 * 
	 * @param str
	 * @param defaultStr
	 * @return
	 */
	public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }
	
	/**
	 * 判定是否为空，使用接口类CharSequence,相比String,增加了对StringBuilder、StringBuffer的支持
	 * 相对JAVA原生方法，处理了参数空指针问题
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param cs
	 * @return boolean
	 */
	public static boolean isEmpty(final CharSequence cs){
		return cs == null || cs.length() == 0;
	}
	
	/**
	 * 判定是否为空白字符序列，如有非空格元素则返回flase
	 * 参数对象为空时返回true
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param cs
	 * @return boolean
	 */
	public static boolean isBlank(final CharSequence cs){
		int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
	}
	
	/**
	 * 比对两个字符序列是否相等
	 * 其中一个参数为null时返回false
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param cs1
	 * @param cs2
	 * @return boolean
	 */
	public static boolean equals(final CharSequence cs1, final CharSequence cs2){
		return StringUtils.equals(cs1,cs2);
	}
	
	/**
	 * 比较字符序列是否存在与多个对象中
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param string
	 * @param searchStrings
	 * @return boolean
	 */
	public static boolean equalsAny(final CharSequence string, final CharSequence... searchStrings){
		return StringUtils.equalsAny(string,searchStrings);
	}
	
	/**
	 * 无视大小写的equals方法
	 * 
	 * @author hudongyu
	 * @2017-1-5
	 * @param str1
	 * @param str2
	 * @return boolean
	 */
	public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2){
		return StringUtils.equalsIgnoreCase(str1, str2);
	}
	
	/**
	 * 是否包含目标序列
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param cs
	 * @param searchChars
	 * @return boolean
	 */
	public static boolean containsAny(final CharSequence cs, final CharSequence... searchChars){
		return StringUtils.containsAny(cs,searchChars);
	}
	
	
	/**
	 * 不包含某些字符
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param cs
	 * @param invalidChars 分割为char后逐个字节进行比对
	 * @return boolean
	 */
	public static boolean containsNone(final CharSequence cs, final String invalidChars){
		return StringUtils.containsNone(cs,invalidChars);
	}
	
	
	/**
	 * 避免空指针异常的split方法
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param str 可以为空和null
	 * @param separatorChars  被分割符号 可以为字符串 可以为null，视为对空格进行处理
	 * @return String
	 */
	public static String[] splitByWholeSeparator(final String str, final String separatorChars){
		return StringUtils.splitByWholeSeparator(str,separatorChars);
	}
	
	/**
	 * 合并数组
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param array
	 * @param separator 合并数组使用的分隔符，为null时视为"";
	 * @return String
	 */
	public static String join(final Object[] array, final String separator){
		return StringUtils.join(array,separator);
	}
	
	/**
	 * 迭代器合并
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param iterator
	 * @param separator 分隔符，为null时视为""
	 * @return
	 */
	public static String join(final Iterator<?> iterator, final String separator){
		return StringUtils.join(iterator,separator);
	}
	
	
	/**
	 * 对象合并
	 * 
	 * @author hudongyu
	 * @date 2017-1-5
	 * @param separator 分隔符，为null时视为""
	 * @param objects
	 * @return
	 */
	public static String joinWith(final String separator, final Object... objects){
		return StringUtils.joinWith(separator,objects);
	}
	
	
	/**
	 * 去除字符串空格
	 * 
	 * 
	 * @param str
	 * @return String
	 */
	public static String deleteWhitespace(final String str){
		return StringUtils.deleteWhitespace(str);
	}
	
	/**
	 * 消去特定内容
	 * 
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String remove(final String str, final String remove){
		return StringUtils.remove(str, remove);
	}
	
	/**
	 * 替换
	 * 
	 * @param text
	 * @param regex 正则表达式
	 * @param replacement 
	 * @return
	 */
	public static String replaceAll(final String text, final String regex, final String replacement){
		return StringUtils.replaceAll(text, regex, replacement);
	}
	
	
	
	/**
	 * 替换
	 * 
	 * @param text
	 * @param searchString
	 * @param replacement
	 * @return
	 */
	public static String replace(final String text, final String searchString, final String replacement){
		return StringUtils.replace(text, searchString, replacement);
	}
	
	/**
	 * 匹配计数
	 * 
	 * 
	 * @param str
	 * @param sub
	 * @return
	 */
	public static int countMatches(final CharSequence str, final CharSequence sub){
		return StringUtils.countMatches(str, sub);
	}
	
	
	
	
	public static void main(String[] args) {
		StringBuffer a =  new StringBuffer();
		a.append("");
		StringBuffer b =  new StringBuffer();
		b.append("Test");
		StringBuffer c =  new StringBuffer();
		c.append("Test");
		System.out.println(equals(c,StringUtil.defaultIfBlank(a, b)));
	}
}
