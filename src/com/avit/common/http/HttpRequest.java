package com.avit.common.http;

import org.apache.commons.httpclient.Header;

public class HttpRequest {
	private int connectionTimeout;
	private int readTimeout;
	private String method;
	private String request;
	private String url;

	private String charset;
	private Header[] requestHeaders;
	//private String contentType;
	public static final String METHOD_GET       = "GET";
	public static final String METHOD_POST      = "POST";
	public static final String METHOD_PUT		="PUT";
	public static final String METHOD_DELETE	="DELETE";
	
	//public static final String CONTENTTYPE_JSON = "application/json";
	//public static final String CONTENTTYPE_XML = "application/xml";
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
//	public String getContentType() {
//		return contentType;
//	}
//	public void setContentType(String contentType) {
//		this.contentType = contentType;
//	}
	public Header[] getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(Header[] requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
}
