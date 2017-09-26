package com.avit.common.http4.exception;


/** 
 * 
 * @author arron
 * @date 2015骞�1鏈�鏃�涓嬪崍2:31:37 
 * @version 1.0 
 */
public class HttpProcessException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2749168865492921426L;
    private int code;
    private String message;
	public HttpProcessException(Exception e){
		super(e);
	}

	/**
	 * @param string
	 */
	public HttpProcessException(String msg) {
		super(msg);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public HttpProcessException(String message, Exception e) {
		super(message, e);
	}
	
	/**
     * @param message
     * @param e
     */
    public HttpProcessException(int code,String message, Exception e) {
        super(message, e);
        this.code=code;
        this.message=message;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
	
}
