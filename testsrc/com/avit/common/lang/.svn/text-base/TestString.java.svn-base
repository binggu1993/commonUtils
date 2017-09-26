package com.avit.common.lang;
import static org.junit.Assert.*;

import org.junit.Test;
public class TestString {
	
	
	//defaultString(null)  = ""
	@Test
	public void defaultStringNull(){
		String a = null;
		assertEquals("",StringUtil.defaultString(a));
	}
	
	//defaultString("bat") = "bat"
	@Test
	public void defaultString(){
		String a = ("bat");
		assertEquals("bat",StringUtil.defaultString(a));
	}
	
	//defaultIfBlank(null, "NULL")  = "NULL"
	@Test
	public void defaultIfBlankStringNull(){
		String a = null;
		String b = "Test";
		assertEquals("Test", StringUtil.defaultIfBlank(a, b));
	}
	
	//defaultIfBlank("", "NULL")  = "NULL"
	@Test
	public void defaultIfBlankStringBlank(){
		String a = "";
		String b = "Test";
		assertEquals("Test", StringUtil.defaultIfBlank(a, b));
	}
	
	//defaultIfBlank("abc", "NULL")  = "abc"
	@Test
	public void defaultIfBlankString(){
		String a = "abc";
		String b = "Test";
		assertEquals("abc", StringUtil.defaultIfBlank(a, b));
	}
	
	//defaultIfBlank("abc", "NULL")  = "abc"
	@Test
	public void defaultIfBlankStringBuffer(){
		StringBuffer a = new StringBuffer();
		a.append("abc");
		StringBuffer b = new StringBuffer();
		b.append("Test");
		StringBuffer c = a;
		assertEquals(c, StringUtil.defaultIfBlank(a, b));
	}
	
	//defaultIfBlank(null, "NULL")  = "NULL"
	@Test
	public void defaultIfBlankStringBuffernull(){
		StringBuffer a =  new StringBuffer();
		StringBuffer b =  new StringBuffer();
		b.append("Test");
		StringBuffer c = b;
		assertEquals(c,StringUtil.defaultIfBlank(a, b));
	}
	
	//defaultIfBlank(null, "NULL")  = "NULL"
	@Test
	public void defaultIfBlankStringBufferBlank(){
		StringBuffer a =  new StringBuffer();
		a.append("");
		StringBuffer b =  new StringBuffer();
		b.append("Test");
		StringBuffer c = b;
		assertEquals(c,StringUtil.defaultIfBlank(a, b));
	}
	
	//null
	@Test
	public void isEmptyNull(){
		String a = null;
		assertEquals(true,StringUtil.isEmpty(a));
	}
	
	
	//空
	@Test
	public void isEmpty(){
		String a = "";
		assertEquals(true,StringUtil.isEmpty(a));
		
	}
	
	//空格
	@Test
	public void isEmptySpace(){
		String a = " ";
		assertEquals(false,StringUtil.isEmpty(a));
	}
	
	//字符串
	@Test
	public void isEmptyString(){
		String a = "test";
		assertEquals(false,StringUtil.isEmpty(a));
	}
	
	//StringBuilder类为空
	@Test
	public void isEmptyStringBuilderNull(){
		StringBuilder a = null;
		assertEquals(true,StringUtil.isEmpty(a));
	}
	
	//StringBuilder类为""
	@Test
	public void isEmptyStringSpace(){
		StringBuilder a = new  StringBuilder("");
		assertEquals(true,StringUtil.isEmpty(a));
	}
	
	//StringBuilder类不为空
	@Test
	public void isEmptyStringString(){
		StringBuilder a = new  StringBuilder("test");
		assertEquals(false,StringUtil.isEmpty(a));
	}
	
	//StringBuilder类为空
	@Test
	public void isEmptyStringBufferNull(){
		StringBuffer a = null;
		assertEquals(true,StringUtil.isEmpty(a));
	}
	
	//StringBuilder类为""
	@Test
	public void isEmptyStringBufferSpace(){
		StringBuffer a = new  StringBuffer("");
		assertEquals(true,StringUtil.isEmpty(a));
	}
	
	//StringBuilder类不为空
	@Test
	public void isEmptyStringBufferString(){
		StringBuffer a = new  StringBuffer("test");
		assertEquals(false,StringUtil.isEmpty(a));
	}
	
	//isBlank(null)      = true
	@Test
	public void isBlankNull(){
		assertEquals(true,StringUtil.isBlank(null));
	}
	
	//isBlank("")        = true
	@Test
	public void isBlankBlank(){
		assertEquals(true,StringUtil.isBlank(""));
	}
	
	//isBlank(" ")       = true
	@Test
	public void isBlankSpace(){
		assertEquals(true,StringUtil.isBlank(" "));
	}
	
	//isBlank("bob")     = false
	@Test
	public void isBlankString(){
		assertEquals(false,StringUtil.isBlank("bob"));
	}
	
	//equals(null, null)   = true
	@Test
	public void equalsNull(){
		
		assertEquals(true,StringUtil.equals(null,null));
	}
	
	//equals(null, "abc")  = false
	
	@Test
	public void equalsNullAnd(){
		StringBuffer a = new StringBuffer("abc");
		assertEquals(false,StringUtil.equals(null,"abc"));
		assertEquals(false,StringUtil.equals(null,a));
	}
	
	//equals("abc", "abc") = true
	
	@Test
	public void equalsString(){
		StringBuffer a = new StringBuffer("abc");
		StringBuffer b = new StringBuffer("abc");
		assertEquals(true,StringUtil.equals("abc","abc"));
		assertEquals(true,StringUtil.equals(a,b));
	}
	
	//equals("abc", "ABC") = false
	@Test
	public void equalsStringCase(){
		StringBuffer a = new StringBuffer("abc");
		StringBuffer b = new StringBuffer("ABC");
		assertEquals(false,StringUtil.equals("abc","ABC"));
		assertEquals(false,StringUtil.equals(a,b));
	}
	
	
	
	/**
	 * StringUtils.equalsAny(null, (CharSequence[]) null) = false
     * StringUtils.equalsAny(null, null, null)    = true
     * StringUtils.equalsAny(null, "abc", "def")  = false
     * StringUtils.equalsAny("abc", null, "def")  = false
     * StringUtils.equalsAny("abc", "abc", "def") = true
     * StringUtils.equalsAny("abc", "ABC", "DEF") = false
	 */
	@Test
	public void equalsAny(){
		StringBuffer b1 = new StringBuffer("abc");
		StringBuffer b2 = new StringBuffer("def");
		StringBuffer b3 = new StringBuffer("DEF");
		StringBuffer b4 = new StringBuffer("ABC");
		assertEquals(false,StringUtil.equalsAny(null, (CharSequence[]) null));
		assertEquals(true,StringUtil.equalsAny(null, null, null));
		assertEquals(false,StringUtil.equalsAny(null, "abc", "def"));
		assertEquals(false,StringUtil.equalsAny("abc", null, "def"));
		assertEquals(true,StringUtil.equalsAny("abc", "abc", "def"));
		assertEquals(false,StringUtil.equalsAny("abc", "ABC", "DEF"));
		assertEquals(false,StringUtil.equalsAny(null, b1, b2));
		assertEquals(false,StringUtil.equalsAny(b1, null, b2));
		assertEquals(true,StringUtil.equalsAny(b1, b1, b2));
		assertEquals(false,StringUtil.equalsAny(b1, b3, b4));
	}
	
	/**
	 * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
	 */
	@Test
	public void equalsIgnoreCase(){
		StringBuffer b1 = new StringBuffer("abc");
		StringBuffer b2 = new StringBuffer("ABC");
		assertEquals(true,StringUtil.equalsIgnoreCase(null, null));
		assertEquals(false,StringUtil.equalsIgnoreCase(null, "abc"));
		assertEquals(false,StringUtil.equalsIgnoreCase(null, b1));
		assertEquals(false,StringUtil.equalsIgnoreCase("abc", null));
		assertEquals(false,StringUtil.equalsIgnoreCase(b2, null));
		assertEquals(true,StringUtil.equalsIgnoreCase("abc", "abc"));
		assertEquals(true,StringUtil.equalsIgnoreCase(b1, b1));
		assertEquals(true,StringUtil.equalsIgnoreCase("abc", "ABC"));
		assertEquals(true,StringUtil.equalsIgnoreCase(b1, b2));
	}
	
	/**
	 * StringUtils.containsAny(null, *)               = false
     * StringUtils.containsAny("", *)                 = false
     * StringUtils.containsAny(*, null)               = false
     * StringUtils.containsAny(*, "")                 = false
     * StringUtils.containsAny("zzabyycdxx", "za")    = true
     * StringUtils.containsAny("zzabyycdxx", "zy")    = true
     * StringUtils.containsAny("aba","z")             = false
     * StringUtils.containsAny("abcd", "ab", null) = true
     * StringUtils.containsAny("abcd", "ab", "cd") = true
     * StringUtils.containsAny("abc", "d", "abc")  = true
	 */
	@Test
	public void containsAny(){
		StringBuffer b1 = new StringBuffer("zzabyycdxx");
		StringBuffer b2 = new StringBuffer("za");
		StringBuffer b3 = new StringBuffer("aba");
		StringBuffer b4 = new StringBuffer("z");
		assertEquals(false,StringUtil.containsAny(null, "zzabyycdxx"));
		assertEquals(false,StringUtil.containsAny(null, b1));
		assertEquals(false,StringUtil.containsAny("", b1));
		assertEquals(false,StringUtil.containsAny("", "zzabyycdxx"));
		assertEquals(false,StringUtil.containsAny("zzabyycdxx",null,null,"asd"));
		assertEquals(false,StringUtil.containsAny(b1,null,null,b3));
		assertEquals(true,StringUtil.containsAny("zzabyycdxx", "za"));
		assertEquals(true,StringUtil.containsAny(b1, b4));
		assertEquals(true,StringUtil.containsAny("zzabyycdxx", "za",null));
		assertEquals(true,StringUtil.containsAny(b1, b2,null));
	}
	
	/**
	 * * StringUtils.containsNone(null, *)       = true
     * StringUtils.containsNone(*, null)       = true
     * StringUtils.containsNone("", *)         = true
     * StringUtils.containsNone("ab", "")      = true
     * StringUtils.containsNone("abab", "xyz") = true
     * StringUtils.containsNone("ab1", "xyz")  = true
     * StringUtils.containsNone("abz", "xyz")  = false
	 */
	@Test
	public void containsNone(){
		StringBuffer b1 = new StringBuffer("ab");
		StringBuffer b2 = new StringBuffer("abab");
		StringBuffer b3 = new StringBuffer("abz");
		StringBuffer b4 = new StringBuffer("ab1");
		assertEquals(false,StringUtil.containsNone(null, "zzabyycdxx"));
		assertEquals(false,StringUtil.containsNone(b1, null));
		assertEquals(false,StringUtil.containsNone("zzabyycdxx", null));
		
	}
}
