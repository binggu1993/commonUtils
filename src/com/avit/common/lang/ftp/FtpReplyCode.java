package com.avit.common.lang.ftp;

public final class FtpReplyCode
{
    public static final int COMMAND_OK = 200;
    public static final String COMMAND_OK_MSG = "success";
    
    public static final int TRANS_CONTINUE = 250;
    public static final String TRANS_CONTINUE_MSG = "the file trans not finish";
    
    public static final int TRANS_NOT_NEED = 255;
    public static final String TRANS_NOT_NEED_MSG = "the transfile'size over source file";
    
    //-----------------
    
    public static final int FTP_LOGIN_FAIL = 530;
    public static final String FTP_LOGIN_FAIL_MSG = "the ftpclient login fail";
    
    //-----------------
    
    public static final int FILE_NOT_EXIST = 911;
    public static final String FILE_NOT_EXIST_MSG = "The file is not exist";
    
    public static final int FTPURL_NOT_ACCESS = 922;
    public static final String FTPURL_NOT_ACCESS_MSG = "The ftpUrl is not access";
    
    public static final int FTPREQUEST_IS_NULL = 923;
    public static final String FTPREQUEST_IS_NULL_MSG = "The ftpRequest is null";
    
    public static final int FTP_CONNECT_FAIL = 924;
    public static final String FTP_CONNECT_FAIL_MSG = "the ftpclient connect fail";
    
    public static final int LOCALFILE_NOT_EXIST = 926;
    public static final String LOCALFILE_NOT_EXIST_MSG = "the localFile is not exist";
    
    public static final int NULL_POINT = 925;
    public static final String NULL_POINT_MSG = "the Argument'value exist null";
    
    public static final int TRANS_FAIL = 927;
    public static final String TRANS_FAIL_MSG = "the File trans fail";
    
    public static final int ILLEGAL_Argument = 928;
    public static final String ILLEGAL_Argument_MSG = ":the Argument'value is illegal";
    
    public static final int ILLEGAL_OFFSET = 929;
    public static final String ILLEGAL_OFFSET_MSG = "the offset' value is illegal";
    
    public static final int COMMAND_FAIL = 955;
    public static final String COMMAND_FAIL_MSG = "fail";
    
    
}
