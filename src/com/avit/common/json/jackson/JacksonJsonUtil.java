package com.avit.common.json.jackson;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.avit.common.http.Utils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JacksonJsonUtil
{
    /**
     * json操作对象
     */
    public static Logger log = LogManager.getLogger(JacksonJsonUtil.class);
    private static ObjectMapper OM ;
    public  static String DATE_PATTERN="yyyy-MM-dd HH:mm:ss";
   
    /**
     * 默认初始化方法.默认初始值为
     * excludesFieldsWithoutInObject true
     * isSerializeNulls false
     * SerializeNullStr null
     * datePattern yyyy-MM-dd HH:mm:ss
     */
    public static void initJacksonJsonUtil()
    {
        if(OM==null)
        {
            initJacksonJsonUtil(true,false,null,DATE_PATTERN);
        }
        
    }
    
    /**
     * 指定初始化参数
     * @param excludesFieldsWithoutInObject  是否  忽略在JSON字符串中存在但Java对象实际没有的属性
     * @param isSerializeNulls 序列化时 是否序列化空值属性
     * @param SerializeNullStr 序列化时 String类型属性 设置序列化默认值
     * @param datePattern 日期属性指定序列化 日期格式
     */
    public static void initJacksonJsonUtil(Boolean excludesFieldsWithoutInObject,Boolean isSerializeNulls,final String SerializeNullStr,String datePattern )
    {
        if(OM==null)
        {
            initObjectMapper(excludesFieldsWithoutInObject,isSerializeNulls,SerializeNullStr,datePattern);
        }
    }
    
    /**
     * 初始化ObjectMapper对象
     * @param excludesFieldsWithoutInObject  是否  忽略在JSON字符串中存在但Java对象实际没有的属性
     * @param isSerializeNulls 反序列化时 是否包含空值属性
     * @param SerializeNullStr 序列化时  空值处理。默认为空
     *  @param datePattern 指定日期格式转换
     */
    private synchronized static void initObjectMapper(Boolean excludesFieldsWithoutInObject,Boolean isSerializeNulls,final String SerializeNullStr,String datePattern )
    {
        // 允许单引号、允许不带引号的字段名称
        // OM.enableSimple();
        // 反序列化。设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        if (OM != null)
        {
            return;
        }
        OM = new ObjectMapper();
        if (excludesFieldsWithoutInObject)
        {
            OM.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
        //OM.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 空值处理为空串
        OM.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()
        {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException
            {
                if (Utils.isEmpty(SerializeNullStr))
                {
                    jgen.writeString("");
                }
                else
                {
                    jgen.writeString(SerializeNullStr);
                }
            }
        });
        if (!isSerializeNulls)
        //不包含空值属性
        {
            OM.setSerializationInclusion(Include.NON_NULL);
        }
        
        //设置时区
        OM.setTimeZone(TimeZone.getDefault());
        if (!Utils.isEmpty(datePattern))
        {
            OM.setDateFormat(new SimpleDateFormat(datePattern));
        }
        //接收空值属性
        OM.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;   

    }
    
    /**
     * 把对象json格式，包括list数组
     * 转化格式为：
     * 对象 -- {"XXXX":{"name":"兵古","age":12,"p":false,"tes":"aaa"}}
     * 数组 -- {"XXXX":[{"name":"兵古","age":12,"p":false,"tes":"aaa"},
     *                 {"name":"兵古","age":12,"p":false,"tes":"aaa"},
     *                 {"name":"兵古","age":12,"p":false,"tes":"aaa"}]}
     * @param 0 对象或者list数组
     * @throws Exception 
     */
    public static String transObjToJsonClassName(Object obj)
    {
        initJacksonJsonUtil();
        String json = "";
        try
        {
            OM.configure(SerializationFeature.WRAP_ROOT_VALUE,true);
            json = OM.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return json;
    }
    
    /**
     * 把对象json格式，包括list数组
     * 转化格式为：
     * 对象 -- {"name":"安迪","age":12,"p":false,"tes":"aaa"}
     * 数组 -- [{"name":"安迪","age":12,"p":false,"tes":"aaa"},{"name":"安迪","age":12,"p":false,"tes":"aaa"},{"name":"安迪","age":12,"p":false,"tes":"aaa"}]
     * @param 0 对象或者list数组
     * @throws Exception 
     */
    public static String transObjToJson(Object obj)
    {
        initJacksonJsonUtil();
        String json = "";
        try
        {
            OM.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
            json = OM.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return json;
    }
    
    /**
     * 转化Json为指定对象
     * @param json json字符串
     * @param clasz 对象
     * @return 指定对象
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws Exception 
     */
    public static <T> T transJsonToObj(String json,Class<T> clazz)
    {
        initJacksonJsonUtil();
    	if(json == null){
    		return null;
    	}
    	
        T jsonObj = null;
        try
        {
            jsonObj = OM.readValue(json, clazz);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return jsonObj;
    }
    
    
	public static Object jacksonToCollection(String src,Class<?> collectionClass, Class<?>... valueType)  { 
	    initJacksonJsonUtil();
		if(src == null) return null;
		try
	        {
			  JavaType javaType= OM.getTypeFactory().constructParametricType(collectionClass, valueType);   
		      return OM.readValue(src, javaType);  
	        } catch (Exception e)
	        {
	            log.error(e);
	        }
		return null;
    }
  
    
    /**
     * 转化Json为指定对象
     * @param json json字符串
     * @param clasz 对象数组
     * @return 指定对象
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws Exception 
     */
    public static <T> T[] transJsonToArray(String json,Class<T[]> clazz)
    {
        initJacksonJsonUtil();
        T[] jsonObj = null;
        try
        {
            jsonObj = OM.readValue(json, clazz);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return jsonObj;
    }
    
    
}
