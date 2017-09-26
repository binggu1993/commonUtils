package com.avit.common.lang.ftp;

public class FtpReply
{
    private int code;
    private String msg;
    
    public static final FtpReply COMMAND_OK = new FtpReply(FtpReplyCode.COMMAND_OK,FtpReplyCode.COMMAND_OK_MSG);
    public static final FtpReply COMMAND_FAIL = new FtpReply(FtpReplyCode.COMMAND_FAIL,FtpReplyCode.COMMAND_FAIL_MSG);
    public static final FtpReply TRANS_CONTINUE = new FtpReply(FtpReplyCode.TRANS_CONTINUE,FtpReplyCode.TRANS_CONTINUE_MSG);
    public static final FtpReply TRANS_FAIL = new FtpReply(FtpReplyCode.TRANS_FAIL,FtpReplyCode.TRANS_FAIL_MSG);
    
    public FtpReply()
    {
    }
    public FtpReply(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }
    public int getCode()
    {
        return code;
    }
    public void setCode(int code)
    {
        this.code = code;
    }
    public String getMsg()
    {
        return msg;
    }
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public static boolean isPositiveCompletion(int reply)
    {
        return (reply >= 200 && reply < 300);
    }
    @Override
    public String toString()
    {
        return code+"  "+msg ;
    }
    
}
