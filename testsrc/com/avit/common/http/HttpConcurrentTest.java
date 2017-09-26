package com.avit.common.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.commons.httpclient.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpConcurrentTest
{
    public static Logger log = LogManager.getLogger(HttpConcurrentTest.class);
    private static  int count=300;
//    private static ExecutorService executors =Executors.newFixedThreadPool(count);
    private static ExecutorService executors2 =Executors.newFixedThreadPool(count);
//    private static long time0=0;
//    private static AtomicInteger atomicInteger=new AtomicInteger(1);
    public static void main(String[] args) 
    {
//        test1();
      test2();
       
    }
   /* public static void test1()
    {
    
        long time1=System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(count*100);
        for(int i=0;i<count*100;i++)
        {
            executors.execute(new HttpConcurrentTest.TestSendAio(countDownLatch));
        }
        try
        {
            countDownLatch.await();
        }
        catch (InterruptedException e)
        {
           log.error("线程异常："+e);
        }finally
        {
            long time2=System.currentTimeMillis();
            executors.shutdown();
            log.debug("本次post请求测试耗时："+(time2-time1));
        }
    }
    */
    public static void test2()
    {

        long time1=System.currentTimeMillis();
        for(int i=0;i<count*100;i++)
        {
            executors2.execute(new HttpConcurrentTest.TestSendAio());
        }
        try
        {
            executors2.shutdown();
        }
        finally
        {
            while(true)
            {
                if (executors2.isTerminated())
                {
                    long time2 = System.currentTimeMillis();
                    executors2.shutdown();
                    log.debug("本次post请求测试耗时：" + (time2 - time1));
//                    log.debug("本次post请求测试耗时2：" + (time2 - time0));
//                    log.debug("本次线程池初始化时间：" + (time0 - time1));
                    break;
                }
            }
        }
    
    }
            
    
    
   static class  TestSendAio implements Runnable
    {
//        private CountDownLatch countDownLatch;
        public TestSendAio()
        {
        }
       /* public TestSendAio(CountDownLatch countDownLatchparam)
        {
            this.countDownLatch=countDownLatchparam;
        }*/
        public void run()
        {

//            if(atomicInteger.getAndIncrement()==1)
//            {
//                time0=System.currentTimeMillis();
//            }
            String url="http://192.168.2.181:8080/commonTestServer/TestServlet?name=haha";
            try
            {
              long  time0=System.currentTimeMillis();
                HashMap<String, String> result=Http.sentRequest(true,url, null, HttpRequest.METHOD_GET, null);
                long time1=System.currentTimeMillis();
                //                log.debug("response status:"+response.getStatus());
//                log.debug("response result:"+response.getStringResult());
            }
            catch (Exception e)
            {
                log.error("send post error: ",e);
            }finally
            {
                /*if(countDownLatch!=null)
                {
                countDownLatch.countDown();
                }*/
            }
        
            
        }
        /*public CountDownLatch getCountDownLatch()
        {
            return countDownLatch;
        }
        public void setCountDownLatch(CountDownLatch countDownLatch)
        {
            this.countDownLatch = countDownLatch;
        }
        */
    }
}
