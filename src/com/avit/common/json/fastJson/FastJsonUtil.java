package com.avit.common.json.fastJson;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonUtil
{

    
    

    private static final SerializerFeature[] defaultfeatures = {
        SerializerFeature.WriteMapNullValue, // 输出空置字段  
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null  
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null  
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null  
        SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
        SerializerFeature.WriteDateUseDateFormat//时间戳转化为标准时间格式
}; 
    /**
     * 功能描述：把JSON数据转换成普通字符串列表
     * 
     * @param jsonData
     *            JSON数据
     * @return
     * @throws Exception
     * @author myclover
     */
    public static List<String> fromJsonToListString(String jsonData) throws Exception {
        return JSON.parseArray(jsonData, String.class);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     * 
     * @param jsonData
     *            JSON数据
     * @param clazz
     *            指定的java对象
     * @return
     * @throws Exception
     * @author myclover
     */
    public static <T> T fromJson(String jsonData, Class<T> clazz)
            throws Exception {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     * 
     * @param jsonData
     *            JSON数据
     * @param clazz
     *            指定的java对象
     * @return
     * @throws Exception
     * @author myclover
     */
    public static <T> List<T> fromJsonToList(String jsonData, Class<T> clazz)
            throws Exception {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的java对象列表
     * 
     * @param jsonData
     *            JSON数据
     * @return
     * @throws Exception
     * @author myclover
     */
    public static List<Map<String, Object>> fromJsonToMapList(String jsonData)
            throws Exception {
        return JSON.parseObject(jsonData,
                new TypeReference<List<Map<String, Object>>>() {
                });
    }
    
    /**
     * 将指定的java对象转换为json数据,使用默认的初始化配置
     * @param object
     * @return
     */
    public static String toJSONStringWithDefaultFeatures(Object object) {  
        return JSON.toJSONString(object, defaultfeatures);
    } 
    
    /**
     * 将指定的java对象转换为json数据,不配置 初始化项
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {  
        return JSON.toJSONString(object);
    } 
    
    /**
     * 将指定的java对象转换为json数据，自定义配置项
     * @param object
     * @param features
     * @return
     */
    public static String toJSONStringWithFeatures(Object object,SerializerFeature[] features) {  
        return JSON.toJSONString(object);  
    } 

}
