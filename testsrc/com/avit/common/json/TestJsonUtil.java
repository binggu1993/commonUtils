package com.avit.common.json;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.print.SimpleDoc;

import net.sf.json.util.NewBeanInstanceStrategy;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.avit.common.http.Utils;
import com.avit.common.json.bean.DeptBean;
import com.avit.common.json.fastJson.FastJsonUtil;
import com.avit.common.json.gson.GsonJsonUtil;
import com.avit.common.json.jackson.JacksonJsonUtil;
import com.avit.common.lang.DateUtils;

public class TestJsonUtil
{
    
    private String person="{\"name\":\"中央溪谷大厦\",\"address\":\"中央溪谷大厦\"}";
    /**
     * 测试gson序列化和反序列化。默认初始化方法
     */
    @Test
    public  void testGson()
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        String json = GsonJsonUtil.toJson(son);
        System.out.println(json);
        Person son1 = GsonJsonUtil.fromJson("wonidaye", Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"birthday" + son1.getBirthday()));
    }
    
    /**
     * 测试gson序列化和反序列化。null值属性序列化
     */
    @Test
    public  void testGsonSerializeNulls()
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        GsonJsonUtil.initGsonUtil(true, false, null, null);
        String json = GsonJsonUtil.toJson(son);
        System.out.println(json);
        Person son1 = GsonJsonUtil.fromJson(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"birthday" + son1.getBirthday()));
    }
    
    /**
     * 测试gson序列化和反序列化。null值属性忽略,String类型属性值为空时序列化为默认值
     */
    @Test
    public  void testGsonSerializeStringNull()
    {
        Person son=new Person();
        son.setAddress("中央溪谷大厦");
        GsonJsonUtil.initGsonUtil(false, false, null, "hehe");
        String json=GsonJsonUtil.toJson(son);
        System.out.println(json);
        Person son1 = GsonJsonUtil.fromJson(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"birthday" + son1.getBirthday()));
    }
    
    /**
     * 测试gson序列化和反序列化。null值属性忽略,日期格式默认格式
     */
    @Test
    public  void testGsonWithOutDatePattern()
    {

        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        son.setJob("IT");
        son.setBirthday(new Date());
        String json = GsonJsonUtil.toJson(son);
        System.out.println(json);
        Person son1 = GsonJsonUtil.fromJson(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"birthday" + son1.getBirthday()));
    
    }
    
    /**
     * 测试gson序列化和反序列化。排除 未标注 {@literal @Expose} 注解的字段
     */
    @Test
    public  void testGsonWithOutExpose()
    {

        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        son.setBirthday(new Date());
        son.setJob("IT");
        GsonJsonUtil.initGsonUtil(false, true, null, null);
        String json = GsonJsonUtil.toJson(son);
        
        System.out.println(json);
        assertEquals("don't exclude without  @Expose", false, json.contains("job"));
        Person son1 = GsonJsonUtil.fromJson(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"birthday" + son1.getBirthday()));
    
    }
    
    /**
     * 测试gson序列化和反序列化。null值属性忽略,日期格式设置为yyyy-MM-dd HH:mm:ss
     */
    @Test
    public  void testGsonWithDatePattern()
    {

        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        son.setBirthday(new Date());
        GsonJsonUtil.initGsonUtil(false, false, "yyyy-MM-dd HH:mm:ss", null);
        String json = GsonJsonUtil.toJson(son);
        System.out.println(json);
        Person son1 = GsonJsonUtil.fromJson(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"birthday" + son1.getBirthday()));
    
    }
   
   
    /**
     * 测试jackson序列化和反序列化 默认初始化方法.
     */
    @Test
    public void testJackSon()
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
            
        son.setBirthday(new Date());
        String json=JacksonJsonUtil.transObjToJson(son);
        System.out.println(json);
        Person son1 = JacksonJsonUtil.transJsonToObj(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":"name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":" birthday:" + son1.getBirthday()));
    }
    
    /**
     * 测试jackson序列化和反序列化  序列化空值属性，默认为空串
     */
    @Test
    public void testJackSonSerialize()
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
            
        son.setBirthday(new Date());
        JacksonJsonUtil.initJacksonJsonUtil(true, true, null, JacksonJsonUtil.DATE_PATTERN);
        String json=JacksonJsonUtil.transObjToJson(son);
        System.out.println(json);
        Person son1 = JacksonJsonUtil.transJsonToObj(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":" name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"  address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"  birthday:" + son1.getBirthday()));
    }
    
    /**
     * 测试jackson序列化和反序列化  序列化空值属性，指定序列化值
     */
    @Test
    public void testJackSonNullString()
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
            
        son.setBirthday(new Date());
        JacksonJsonUtil.initJacksonJsonUtil(true, true, "", JacksonJsonUtil.DATE_PATTERN);
        String json=JacksonJsonUtil.transObjToJson(son);
        System.out.println(json);
        Person son1 = JacksonJsonUtil.transJsonToObj(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":" name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"  address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"  birthday:" + son1.getBirthday()));
    }
    
    /**
     * 测试  fastJson 不使用初始化配置项
     */
    
    @Test
    public void testFastJson() 
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        String json=FastJsonUtil.toJSONString(son);
        System.out.println(json);
        try
        {
            Person son1 = FastJsonUtil.fromJson(json, Person.class);
            System.out.println("person 对象值为：" +
                    (Utils.isEmpty(son1.getName())?"":" name:" + son1.getName())+
                    (Utils.isEmpty(son1.getAddress())?"":"  address:" + son1.getAddress()) + 
                    (Utils.isEmpty(son1.getBirthday())?"":"  birthday:" + son1.getBirthday()));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 测试  fastJson 使用默认初始化配置项
     */
    @Test
    public void testFastJsonWithDefaultFeatures()
    {
        Person son = new Person();
        List<Person> list=new ArrayList<Person>();
        list.add(son);
        son.setAddress("中央溪谷大厦");
        String json=FastJsonUtil.toJSONStringWithDefaultFeatures(list);
        System.out.println(json);
        try
        {
            List<Person> list1= FastJsonUtil.fromJson(json, List.class);
            List<Person> list2= FastJsonUtil.fromJsonToList(json, Person.class);
            List<String> list3=FastJsonUtil.fromJsonToListString(json);
            List<Map<String, Object>> list4=FastJsonUtil.fromJsonToMapList(json);
            System.out.println("hehe");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    /**
     * 测试  fastJson 使用自定义初始化配置项
     */
    
    @Test
    public void testFastJsonWithFeatures()
    {
        Person son = new Person();
        son.setAddress("中央溪谷大厦");
        SerializerFeature[] features={ 
                SerializerFeature.WriteMapNullValue, // 输出空置字段  
                SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
                SerializerFeature.WriteNullStringAsEmpty
        };
        String json=FastJsonUtil.toJSONStringWithFeatures(son,features);
        System.out.println(json);
        Person son1 = GsonJsonUtil.fromJson(json, Person.class);
        System.out.println("person 对象值为：" +
                (Utils.isEmpty(son1.getName())?"":" name:" + son1.getName())+
                (Utils.isEmpty(son1.getAddress())?"":"  address:" + son1.getAddress()) + 
                (Utils.isEmpty(son1.getBirthday())?"":"  birthday:" + son1.getBirthday()));
    } 
    
    //------------------------------------华丽的分割线-----------------------------
    //以下是各类库交叉测试
    /**
     * 测试gson反序列化（单类库测试）
     */
    @Test
    public void testFromGsonOnly()
    {

        //负载大小
        int count = 100;
        //循环次数
        int num=50;
        long totalTime = 0;
        for (int j = 0; j < 5; j++)
        {
            for (int i = 0; i < num; i++)
            {
                String testData = createFromGsonData(count);
               
                long time1 = System.currentTimeMillis();
                GsonJsonUtil.fromJson(testData, List.class);
                long time2 = System.currentTimeMillis();
              /*  if(i==0&&j==0)
                {
                }else
                {
                }*/
                totalTime=totalTime+(time2-time1);
            }
            System.out.println("gson size ：" + count);
            System.out.println("gson平均总耗时：" + (double)(totalTime) / num);
            System.out.println("gson平均负载耗时：" + (double)(totalTime) / num / count);
            System.out.println("--------------分割线------------------");
            count = count * 10;
        }
     
    }
    /**
     * 测试jackson反序列化（单类库测试）
     */
    @Test
    public void testjackson()
    {
        //负载大小
        int count = 100;
        //循环次数
        int num=50;
        long totalTime = 0;
        for (int j = 0; j < 5; j++)
        {
            for (int i = 0; i < num; i++)
            {
                String testData = createFromjacksonData(count);
                long time3 = System.currentTimeMillis();
                JacksonJsonUtil.transJsonToObj(testData, List.class);
                long time4 = System.currentTimeMillis();
              /*  if(i==0&&j==0)
                {
                }else
                {
                }*/
                totalTime=totalTime+(time4-time3);
            }
            System.out.println("gson size ：" + count);
            System.out.println("jackson平均总耗时：" + (double)(totalTime) / num);
            System.out.println("jackson平均负载耗时：" + (double)(totalTime) / num / count);
            System.out.println("--------------分割线------------------");
            count = count * 10;
        }
    }
    
    /**
     * 测试jackson反序列化(混合类库测试，gson序列化，jackson反序列化。)
     * @param count
     * @return
     */
    @Test
    public void testFromGsonToJackson()
    {
        //负载大小
        int count = 100;
        //循环次数
        int num=50;
        long totalTime = 0;
        long totalTime1 = 0;
        for (int j = 0; j < 5; j++)
        {
            for (int i = 0; i < num; i++)
            {
                String testData = createFromGsonData(count);
               
                long time1 = System.currentTimeMillis();
                GsonJsonUtil.fromJson(testData, List.class);
                long time2 = System.currentTimeMillis();
             /*   if(i==0&&j==0)
                {
                }else
                {
                }*/
                
                totalTime=totalTime+(time2-time1);
                long time3 = System.currentTimeMillis();
                JacksonJsonUtil.transJsonToObj(testData, List.class);
                long time4 = System.currentTimeMillis();
              /*  if(i==0&&j==0)
                {
                }else
                {
                }*/
                totalTime1=totalTime1+(time4-time3);
            }
            System.out.println("gson size ：" + count);
            System.out.println("gson平均总耗时：" + (double)(totalTime) / num);
            System.out.println("gson平均负载耗时：" + (double)(totalTime) / num / count);
            System.out.println();
            System.out.println("jackson平均总耗时：" + (double)(totalTime1) / num);
            System.out.println("jackson平均负载耗时：" + (double)(totalTime1) / num / count);
            System.out.println("--------------分割线------------------");
            count = count * 10;
        }
    }
    
    /**
     * 测试gson反序列化(混合类库测试，jackson序列化，gson反序列化。)
     */
    @Test
    public void testFromJacksonToGson()
    {
        //负载大小
        int count = 100;
        //循环次数
        int num=50;
        long totalTime = 0;
        for (int j = 0; j < 5; j++)
        {
            for (int i = 0; i < num; i++)
            {
                String testData = createFromjacksonData(count);
                long time1 = System.currentTimeMillis();
                GsonJsonUtil.fromJson(testData, List.class);
                long time2 = System.currentTimeMillis();
            /*    if(i==0&&j==0)
                {
                }else
                {
                }*/
                totalTime=totalTime+(time2-time1);
            }
            System.out.println("size ：" + count);
            System.out.println("gson平均总耗时：" + (double)(totalTime) / num);
            System.out.println("gson平均负载耗时：" + (double)(totalTime) / num / count);
            System.out.println("--------------分割线------------------");
            count = count * 10;
        }
    
    }
    
    /**
     * 创建序列化测试数据
     * @param count
     * @return
     */
    public List createtoJsonData(int count)
    {
        List list = new ArrayList<Person>();
        int j=count;
        Person per=null;
        while(j>0)
        {
            per=new Person();
            per.setAddress("china"+new Random().nextInt(j));
            per.setName("wu"+new Random().nextInt(j));
            list.add(per);
            j--;
        }
        return list;
    }
    
    /**
     * 创建gson反序列化测试数据
     */
    public String createFromGsonData(int count)
    {
        String strFromJson=GsonJsonUtil.toJson(createtoJsonData(count));
        return strFromJson;
    }
    
    /**
     * 创建jackson反序列化测试数据
     */
    public String createFromjacksonData(int count)
    {
        String strFromJson=JacksonJsonUtil.transObjToJson(createtoJsonData(count));
        return strFromJson;
    }
    @Test
    public void testDate()
    {
        //        String json = "{\"createDate\":\"2017-05-11T17:47:13\",\"dateFormat\":\"2015-12-22T10:10:10\"}";
        String json = "{\"createDate\":\"2017-05-11T17:47:13\",\"dateFormat\":}";
        DeptBean jsonToDeptBean1 = JacksonJsonUtil.transJsonToObj(json, DeptBean.class);
        DeptBean jsonToDeptBean = new DeptBean();
        jsonToDeptBean.setCreateDate(new Date());
        jsonToDeptBean.setDateFormat(new Date());
        System.out.println(JacksonJsonUtil.transObjToJson(jsonToDeptBean));
        System.out.println("反序列化："+(jsonToDeptBean1.getCreateDate()));
        System.out.println("序列化："+(jsonToDeptBean1.getCreateDate()));
    }
    
    
    
}
