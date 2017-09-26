package com.avit.common.lang.ftp;

public class FtpRequest
{
     //ftp连接参数
    /**
     * ftp连接ip
     */
    private String ftpIp;
    /**
     * ftp连接端口
     */
    private int ftpPort = 21;
    /**
     * ftp登录用户名
     */
    private String user;
    /**
     * ftp登录密码
     */
    private String pwd;
    
    private String ftpUrl;
    
     //ftp文件传输参数
    /**
     * 文件传输方式，可选值 :默认为FtpUtils.RESET_MODEL
     * <p><code>FtpUtils.RESTART_MODEL</code>断点传输</p>
     * <p><code>FtpUtils.RESET_MODEL</code>非断点传输</p>
     */
    private int operaModel;
    /**
     * 传输缓存值，单位字节(B),默认为0表示由待传输文件大小决定
     */
    private int bufferSize;
    /**
     * 断点传输的偏移量,单位字节(B),默认为-1表示偏移量为待传输文件大小值
     */
    private long transOffset;
    /**
     * 连接超时时长  单位毫秒 默认3000
     */
    private int connecttimeout;
    /**
     * ftp字符编码，默认utf-8
     */
    private String encoding;
    /**
     * 是否打印ftp底层日志，默认屏蔽
     */
    private boolean isPrintLog=false;
    
    public FtpRequest()
    {
        initDefaults();
    }

    protected void initDefaults(){
        connecttimeout = 3*1000;
        encoding = "UTF-8";
        operaModel = FtpUtils.RESET_MODEL;
        bufferSize = 0;//根据文件大小自动选择
        transOffset = FtpUtils.TRANOFFSET_FULL;
    }
    
    public FtpRequest(String ftpIp, int ftpPort, String user, String pwd)
    {
        initDefaults();
        this.ftpIp = ftpIp;
        this.ftpPort = ftpPort;
        this.user = user;
        this.pwd = pwd;
    }
    
    public FtpRequest(String ftpIp, String user, String pwd)
    {
        initDefaults();
        this.ftpIp = ftpIp;
        this.ftpPort = 21;
        this.user = user;
        this.pwd = pwd;
    }

    public FtpRequest(String ftpUrl)
    {
        initDefaults();
        this.ftpUrl = ftpUrl;
    }

    public String getFtpIp()
    {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp)
    {
        this.ftpIp = ftpIp;
    }

    public int getFtpPort()
    {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort)
    {
        this.ftpPort = ftpPort;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getFtpUrl()
    {
        return ftpUrl;
    }

    public void setFtpUrl(String ftpUrl)
    {
        this.ftpUrl = ftpUrl;
    }


    public int getConnecttimeout()
    {
        return connecttimeout;
    }


    public void setConnecttimeout(int connecttimeout)
    {
        this.connecttimeout = connecttimeout;
    }


    public String getEncoding()
    {
        return encoding;
    }


    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    public boolean isPrintLog()
    {
        return isPrintLog;
    }


    public void setPrintLog(boolean isPrintLog)
    {
        this.isPrintLog = isPrintLog;
    }

    public int getOperaModel()
    {
        return operaModel;
    }

    public void setOperaModel(int operaModel)
    {
        this.operaModel = operaModel;
    }

    public int getBufferSize()
    {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
    }

    public long getTransOffset()
    {
        return transOffset;
    }

    public void setTransOffset(long transOffset)
    {
        this.transOffset = transOffset;
    }
    
}
