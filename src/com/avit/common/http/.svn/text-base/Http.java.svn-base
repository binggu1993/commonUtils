package com.avit.common.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.avit.common.http4.common.HttpHeader.Headers;
import com.avit.common.http4.exception.HttpProcessException;
import com.avit.common.lang.StringUtil;

public class Http
{
    
    public static Logger log = LogManager.getLogger(Http.class);
    
    public static org.apache.log4j.Logger log2 = org.apache.log4j.LogManager.getLogger(Http.class);
    
    public static String DEFAULT_CHARSET = "UTF8";
    
    /** 连接超时时间，由bean factory设置，缺省为8秒钟 */
    private static int defaultConnectionTimeout = 2000;
    
    /** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
    private static int defaultSoTimeout = 5000;
    
    /** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */
    private static int defaultIdleConnTimeout = 60000;
    
    private static int defaultMaxConnPerHost = 500;
    
    private static int defaultMaxTotalConn = 2000;
    
    /** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒 */
    private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;
    
    /**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
    private static HttpConnectionManager connectionManager;
    
    private static Http http;
    
    /**
     * 工厂方法
     * 
     * @return
     */
    public synchronized static void InitHttp()
    {
        if (http != null)
        {
            return;
        }
        else
        {
            http = new Http();
        }
    }
    
    public static HttpResponse sentRequest(HttpRequest request) throws HttpProcessException
    {
        InitHttp();
        HttpResponse result = new HttpResponse();
        HttpClient httpclient = new HttpClient(connectionManager);
        // 设置连接超时
        if (request.getConnectionTimeout() == 0)
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
        else
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(request.getConnectionTimeout());
        // 设置回应超时
        if (request.getReadTimeout() == 0)
            httpclient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
        else
            httpclient.getHttpConnectionManager().getParams().setSoTimeout(request.getReadTimeout());
        
        // 设置等待ConnectionManager释放connection的时间
        httpclient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);
        String strmethod = request.getMethod();
        if (strmethod == null || "".equals(strmethod))
        {
            strmethod = HttpRequest.METHOD_POST;
        }
        HttpMethodBase method = null;
        try
        {
            String charset = request.getCharset() == null ? DEFAULT_CHARSET : request.getCharset();
            if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_GET))
            {
                method = new GetMethod(request.getUrl());
                //                ((GetMethod) method).setQueryString(request.getRequest());
            }
            else if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_POST))
            {
                method = new PostMethod(request.getUrl());
                RequestEntity entity = new ByteArrayRequestEntity(request.getRequest().getBytes(charset), "text/xml; charset=" + charset);
                ((PostMethod)method).setRequestEntity(entity);
            }
            else if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_PUT))
            {
                method = new PutMethod(request.getUrl());
                RequestEntity entity = new ByteArrayRequestEntity(request.getRequest().getBytes(charset), "text/xml; charset=" + charset);
                ((PutMethod)method).setRequestEntity(entity);
            }
            else if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_DELETE))
            {
                method = new DeleteMethod(request.getUrl());
                //                ((DeleteMethod) method).setQueryString(request.getRequest());
            }
            else
            {
                result.setStatus(HttpStatus.SC_BAD_REQUEST);
                result.setStringResult(method + " not support!");
                return result;
            }
            if (request.getRequestHeaders() != null && request.getRequestHeaders().length > 0)
            {
                for (Header h : request.getRequestHeaders())
                {
                    method.addRequestHeader(h);
                }
            }
            
            httpclient.executeMethod(method);
            result.setStatus(method.getStatusCode());
            int status = method.getStatusCode();
            if (status == 200)
                //                result.setStringResult(method.getResponseBodyAsString());
                result.setStringResult(getResponseContent(method));
            result.setResponseHeaders(method.getRequestHeaders());
        }
        catch (UnknownHostException e)
        {
            log.error(e);
            result.setStatus(HttpStatus.SC_BAD_GATEWAY);
            result.setStringResult(e.getMessage());
            //            throw new HttpProcessException(HttpStatus.SC_BAD_GATEWAY, e.getMessage(), e);
        }
        catch (IOException e)
        {
             log.error(e.getMessage(),e);
             result.setStatus(HttpStatus.SC_GATEWAY_TIMEOUT);
             result.setStringResult(e.getMessage());
//            throw new HttpProcessException(HttpStatus.SC_GATEWAY_TIMEOUT, e.getMessage(), e);
        }
        catch (Exception e)
        {
             log.error(e);
             result.setStatus(HttpStatus.SC_SERVICE_UNAVAILABLE);
             result.setStringResult(e.getMessage());
//            throw new HttpProcessException(HttpStatus.SC_SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
        finally
        {
            if (method != null)
            method.releaseConnection();
            for (Header h : request.getRequestHeaders())
            {
                //Connection不为close时，客户端还得根据实际情况决定是否需要关闭链接。
               if(h.getName().equals(HttpContants.CONNECTION)&&!h.getValue().equals(Headers.CONN_CLOSE))
               {
                   httpclient.getHttpConnectionManager().closeIdleConnections(0);
               }
            }
            method = null;
            httpclient = null;
        }
        return result;
    }
    
    //非线程池调用
    public static HttpResponse sentRequestNoPool(HttpRequest request) throws HttpProcessException
    {
        InitHttp();
        HttpResponse result = new HttpResponse();
        HttpClient httpclient = new HttpClient();
        // 设置连接超时
        if (request.getConnectionTimeout() == 0)
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
        else
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(request.getConnectionTimeout());
        // 设置回应超时
        if (request.getReadTimeout() == 0)
            httpclient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
        else
            httpclient.getHttpConnectionManager().getParams().setSoTimeout(request.getReadTimeout());
        
        String strmethod = request.getMethod();
        if (strmethod == null || "".equals(strmethod))
        {
            strmethod = HttpRequest.METHOD_POST;
        }
        HttpMethodBase method = null;
        try
        {
            String charset = request.getCharset() == null ? DEFAULT_CHARSET : request.getCharset();
            if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_GET))
            {
                method = new GetMethod(request.getUrl());
                //                ((GetMethod) method).setQueryString(request.getRequest());
            }
            else if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_POST))
            {
                method = new PostMethod(request.getUrl());
                RequestEntity entity = new ByteArrayRequestEntity(request.getRequest().getBytes(charset), "text/xml; charset=" + charset);
                ((PostMethod)method).setRequestEntity(entity);
            }
            else if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_PUT))
            {
                method = new PutMethod(request.getUrl());
                RequestEntity entity = new ByteArrayRequestEntity(request.getRequest().getBytes(charset), "text/xml; charset=" + charset);
                ((PutMethod)method).setRequestEntity(entity);
            }
            else if (strmethod.equalsIgnoreCase(HttpRequest.METHOD_DELETE))
            {
                method = new DeleteMethod(request.getUrl());
                //                ((DeleteMethod) method).setQueryString(request.getRequest());
            }
            else
            {
                result.setStatus(HttpStatus.SC_BAD_REQUEST);
                result.setStringResult(method + " not support!");
                return result;
            }
            if (request.getRequestHeaders() != null && request.getRequestHeaders().length > 0)
            {
                for (Header h : request.getRequestHeaders())
                {
                    method.addRequestHeader(h);
                }
            }
            
            httpclient.executeMethod(method);
            result.setStatus(method.getStatusCode());
            int status = method.getStatusCode();
            if (status == 200)
                //                result.setStringResult(method.getResponseBodyAsString());
                result.setStringResult(getResponseContent(method));
            result.setResponseHeaders(method.getRequestHeaders());
        }
        catch (UnknownHostException e)
        {
            log.error(e);
            result.setStatus(HttpStatus.SC_BAD_GATEWAY);
            result.setStringResult(e.getMessage());
            //            throw new HttpProcessException(HttpStatus.SC_BAD_GATEWAY, e.getMessage(), e);
        }
        catch (IOException e)
        {
             log.error(e.getMessage(),e);
             result.setStatus(HttpStatus.SC_GATEWAY_TIMEOUT);
             result.setStringResult(e.getMessage());
//            throw new HttpProcessException(HttpStatus.SC_GATEWAY_TIMEOUT, e.getMessage(), e);
        }
        catch (Exception e)
        {
             log.error(e);
             result.setStatus(HttpStatus.SC_SERVICE_UNAVAILABLE);
             result.setStringResult(e.getMessage());
//            throw new HttpProcessException(HttpStatus.SC_SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
        finally
        {
            if (method != null)
            method.releaseConnection();
            for (Header h : request.getRequestHeaders())
            {
                //Connection不为close时，客户端还得根据实际情况决定是否需要关闭链接。
               if(h.getName().equals(HttpContants.CONNECTION)&&!h.getValue().equals(Headers.CONN_CLOSE))
               {
                   httpclient.getHttpConnectionManager().closeIdleConnections(0);
               }
            }
            method = null;
            httpclient = null;
        }
        return result;
    }
    
    /**
     * 方法调用
     * @param url 链接
     * @param requestXml post和put需要的报文实体
     * @param readtimeout 读超时时间设置
     * @param connectTimeOut 连接超时时间设置
     * @param requestHeaders  报头设置。报头设置项主要有：
     * 1.Content-Type报文传输格式 2.Accept期望返回的报文格式 3.Connection,是短连接还是长连接.HTTP1.1默认是keep-alive。
     * 此属性，如果期望服务器端主动关闭连接，则应设置为close。如果希望客户端主动关闭连接，则设置为keep-alive或者默认设置，客户端需主动释放链接并
     * 关闭连接。如果设置为keep-alive而客户端又不关闭，则默认为长连接。主动关闭链接的一方在四次握手后，链接状态进入time_wait约1分钟才释放，期间内该链接不可用，易造成端口不可用或者句柄超出系统最大值，根据需要设置。
     * @return
     * @throws Exception
     */
    public static HashMap<String, String> sentRequest(boolean isPool ,String url, String requestXml, String method, HashMap<String, String> requestParam) throws Exception
    {
        InitHttp();
        HashMap<String, String> result = new HashMap<String, String>();
        
        HttpRequest request = new HttpRequest();
        if (StringUtil.isEmpty(url))
        {
            result.put("code", "400");
            result.put("message", "url can not be empty!" );
//            throw new HttpException("the url can not be empty!");
        }
        if (!StringUtil.isEmpty(method) && Utils.checkRequestMethod(method))
        {
            request.setMethod(method);
        }
        if(requestParam==null)
        {
            requestParam=new HashMap<String, String>();
        }
        if (!StringUtil.isEmpty(requestParam.get(HttpContants.READ_TIME_OUT)) && Utils.checkIsLong(requestParam.get(HttpContants.READ_TIME_OUT)))
        {
            request.setReadTimeout(Integer.valueOf(requestParam.get(HttpContants.READ_TIME_OUT)));
        }
        if (!StringUtil.isEmpty(requestParam.get(HttpContants.CONNECTION_TIME_OUT)) && Utils.checkIsLong(requestParam.get(HttpContants.CONNECTION_TIME_OUT)))
        {
            request.setConnectionTimeout(Integer.valueOf(requestParam.get(HttpContants.CONNECTION_TIME_OUT)));
        }
        if (StringUtil.isEmpty(requestXml))
        {
            requestXml = "";
        }
        request.setRequest(requestXml);
        Header[] headers = HttpHeader.custom()
                .accept(StringUtil.isEmpty(requestParam.get(HttpContants.ACCEPT)) ? Headers.TEXT_XML : requestParam.get(HttpContants.ACCEPT))
                .acceptCharset(StringUtil.isEmpty(requestParam.get(HttpContants.CHARSET)) ? Headers.CONTENT_CHARSET_UTF8 : requestParam.get(HttpContants.CHARSET))
                .connection(StringUtil.isEmpty(requestParam.get(HttpContants.CONNECTION)) ? Headers.KEEP_ALIVE : requestParam.get(HttpContants.CONNECTION))
                .build();
        request.setRequestHeaders(headers);
        request.setRequest(requestXml);
        request.setUrl(url);
        HttpResponse response=null;
        if(!isPool)
        {
            response = sentRequestNoPool(request);
        }else
        {
            response = sentRequest(request);
        }
        
          result.put("code", "" + response.getStatus());
          result.put("message", response.getStringResult());
        return result;
    }
   
    private static String getResponseContent(HttpMethodBase method) throws IOException
    {
        InputStream instream = method.getResponseBodyAsStream();
        String result = "";
        ByteArrayOutputStream outstream;
        if (instream != null)
        {
            long contentLength = method.getResponseContentLength();
            outstream = new ByteArrayOutputStream(contentLength > 0L ? (int)contentLength : 4096);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = instream.read(buffer)) > 0)
            {
                int i = len;
                outstream.write(buffer, 0, i);
            }
            result = outstream.toString();
            outstream.close();
        }
        return result;
    }
    
    /**
     * 私有的构造方法
     */
    private Http()
    {
        if(http!=null) return;
        // 创建一个线程安全的HTTP连接池
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
        connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);
        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);
        ict.start();
    }
    
    public static void main(String[] args) throws Exception
    {/*
        String url="http://192.168.2.181:8080/commonTestServer/TestServlet?name=haha";
        int count=100;
        long time1=System.currentTimeMillis();
        try
        {
            for(int i=0;i<count;i++)
            {
            HashMap<String, String> result=Http.sentRequest(url, null, HttpRequest.METHOD_GET, null);
            }
            long time2=System.currentTimeMillis();
            *//**
             * 测试单线程 100次耗时：20630
             * 测试单线程 100次平均耗时：206
             *//*
            log.debug("测试单线程 "+count+"次耗时："+(time2-time1));
            log.debug("测试单线程 "+count+"次平均耗时："+(time2-time1)/count);
//            log.debug("response status:"+response.getStatus());
//            log.debug("response result:"+response.getStringResult());
        }
        catch (Exception e)
        {
            log.error("send post error: ",e);
        }
    */
//        String url="http://192.168.0.6/webAuth/";
//        HashMap<String, String> result=Http.sentRequest(url, null, HttpRequest.METHOD_POST, null);
    }
    
}