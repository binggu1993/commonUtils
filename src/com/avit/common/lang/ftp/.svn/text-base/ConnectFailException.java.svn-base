package com.avit.common.lang.ftp;

public class ConnectFailException extends RuntimeException
{
    private int code;
    /**
     * 
     */
    private static final long serialVersionUID = -897739170707821428L;
    
    public ConnectFailException() {
        super();
    }

    public ConnectFailException(String message) {
        super(message);
    }
    

    public ConnectFailException(int code,String message)
    {
        this(message);
        this.code = code;
    }

    public ConnectFailException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConnectFailException(Throwable cause) {
        super(cause);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return code+"  "+getMessage();
    }
    
}
