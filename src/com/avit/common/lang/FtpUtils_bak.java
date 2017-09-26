package com.avit.common.lang;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;


@Deprecated
public class FtpUtils_bak
{
    private static Logger logger = Logger.getLogger(FtpUtils_bak.class);
    
    private static final String ENCODING = "UTF-8";
    /**
     * window系统的文件路径分隔符
     */
    private static final String WIN_FS_SEPARATOR = "\\";
    /**
     * linux系统的文件路径分隔符
     */
    private static final String LINUX_FS_SEPARATOR = "/";
    
    /**
     * 小缓存 1k
     */
    public static final int BUFFERSIZE_S = 1024;
    /**
     * 中缓存 64k
     */
    public static final int BUFFERSIZE_M = 1024*64;
    
    /**
     * 大缓存  1M
     */
    public static final int BUFFERSIZE_G = 1024*1024;
    
    /**
     * 断点续传模式
     */
    public static final int RESTART_MODEL = 0;
    /**
     * 重置上传模式
     */
    public static final int RESET_MODEL = 1;
    /**
     * 其他上传模式，未使用留做拓展
     */
    public static final int OTHER_MODEL = 2;
    /**
     * 传输成功完成
     */
    public static final int TRANS_OK = 200;
    /**
     * 传输失败
     */
    public static final int TRANS_FAIL = 500;
    /**
     * 未完整传输完，待续传
     */
    public static final int TRANS_CONTINU = 400;
    
    /**
     * 上传偏移量为全满
     */
    public static final long TRANOFFSET_FULL = -1;
    
    /**
     * 1K单位的偏移量
     */
    public static final long Unit_K = 1024;
    
    /**
     * 1M单位的偏移量
     */
    public static final long Unit_M = 1024*1024;
    
    /**
     * 1G单位的偏移量
     */
    public static final long Unit_G = 1024*1024*1024;
    /**
     * ftp连接超时
     */
    private static final int CONNECTTIMEOUT = 5*1000;
    
    
    /**
     * 
    * <p>获取一个已登录的ftpclient对象,工作目录为登录用户的工作目录，如果失败返回null</p>
    * @param ftpURL
    * @param ftpport
    * @param username
    * @param pwd
    * @return
    * @throws SocketException If the socket timeout could not be set.
    * @throws IOException If the socket could not be opened.  In most
    *  cases you will only want to catch IOException since SocketException is
    *  derived from it.
    * @exception UnknownHostException If the hostname cannot be resolved.
    * @exception NullPointerException If the params any one is empty or null.
    * @exception IllegalArgumentException If the param <code>ftpport</code> is not between 0 and 65535.
    * @exception SecurityException If the ftpclient cannot login by using params <code>username</code> and <code>pwd</code> .
    * @author liyongquan@avit.com.cn
    * @date 2017-1-12 下午5:22:50
     */
    public static FTPClient getFtpClientInstance(String ftpURL, int ftpport, String username, String pwd) throws SocketException, IOException{
        if(null==ftpURL||null==username||null==pwd){
            throw new NullPointerException("ftpURL username and pwd must not be null");
            //throw new IllegalArgumentException("ftpURL username and pwd must not be null");
        }
        if(ftpport<0||ftpport>65535){
            logger.debug("ftpport="+ftpport);
            throw new IllegalArgumentException("ftp connect port is illegal");
        }
        
        FTPClient ftpClient = new FTPClient();
        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding(ENCODING);
        /*FTPClientConfig  conf = new FTPClientConfig(FTPClientConfig.SYST_NT);   
        conf.setServerLanguageCode("zh");*/
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.setConnectTimeout(CONNECTTIMEOUT);
        //连接ftp服务器
        
        ftpClient.connect(ftpURL, ftpport);
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(username, pwd)) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                return ftpClient;
            }else{
                try{
                    logger.debug(String.format("登录失败,username=%s,pwd=%s", username,pwd));
                    throw new SecurityException("登录失败,用户名或密码错误");  
                }finally{
                    disconnect(ftpClient);
                }
            }
        }
        return null;
    }
    
    /**
     * 
    * <p>根据ftpurl获得ftpclient</p>
    * @param ftpUrl e.g, ftp://hadoop:h111111@192.168.2.181:21/path1/path2
    * @return
    * @throws SocketException
    * @throws IOException
    * @exception IllegalArgumentException If the param <code>ftpUrl</code> is Illegal.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-17 下午2:08:52
     */
    public static FTPClient getFtpClientInstance(String ftpUrl) throws SocketException, IOException {
        URI uri = URI.create(ftpUrl);
        String userInfo = uri.getUserInfo();
        if (userInfo==null||"".equals(userInfo)) {
            throw new IllegalArgumentException("ftpUrl is error");
        }
        String[] strs = userInfo.split(":");
        FTPClient result = getFtpClientInstance(uri.getHost(),uri.getPort(),strs[0],strs[1]);
        
        String path = uri.getPath();
        if(null!=path&&!"".equals(path)){
            if(!path.endsWith(LINUX_FS_SEPARATOR)){
                path = path+LINUX_FS_SEPARATOR;
            }
            makeOrChangeDir(result, path);
        }
        return result;
    }
    
    /***
     * Closes the connection to the FTP server and restores
     * connection parameters to the default values.
     * <p>
     * @exception IOException If an error occurs while disconnecting.
     ***/
    public static void disconnect(FTPClient ftpClient) throws IOException {
        if(null==ftpClient){
            throw new IllegalArgumentException("ftpClient must not be null");
        }
        
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }
    
    /**
     * 
    * <p>上传文件,模式为非续传,缓存为小缓存</p>
    * @param ftpClient
    * @param remoteFile 支持相对和绝对目录或文件句柄，目录是以/结束,文件名同本地上传文件名同名;文件句柄以文件名结束
    * @param localFile 本地需上传文件的绝对路径
    * @return
    * @author liyongquan@avit.com.cn
     * @throws IOException 
    * @date 2017-1-12 下午5:30:01
     */
    public static int uploadFileDefault(FTPClient ftpClient,String remoteFile, String localFile) throws IOException{
        
        return uploadFile(ftpClient,remoteFile,localFile,FtpUtils_bak.RESET_MODEL,TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
    }
    
    /**
     * 
    * <p>断点续传</p>
    * @param ftpClient
    * @param remoteFile 远程文件目录或文件句柄
    * @param localFile 本地需上传文件的绝对路径
    * @param operaModel 上传模式
    * @param updateOffset 续传偏移量
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @exception IllegalArgumentException If source filePath is not exist any file.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-17 下午2:22:18
     */
    public static int uploadFile(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long updateOffset,int bufferSize) throws IOException{
        
        if(null==ftpClient||isEmpty(remoteFile)||isEmpty(localFile)){
            throw new NullPointerException("ftpClient and remoteFile and localFile must not be empty");
        }
        boolean flag = false;
        localFile = formatPath(localFile,File.separator);
        remoteFile = formatPath(remoteFile,FtpUtils_bak.LINUX_FS_SEPARATOR);
        if(remoteFile.endsWith(FtpUtils_bak.LINUX_FS_SEPARATOR)){
            String fileName = localFile.substring(localFile.lastIndexOf(File.separator)+1);
            remoteFile = remoteFile+fileName;
        }
        remoteFile = encodeUtf8ToISO(remoteFile);
        File lFile = new File(localFile);
        if(!lFile.exists()){
            logger.debug("本地目录："+localFile+"不存在该文件");
            throw new IllegalArgumentException("本地目录："+localFile+"不存在该文件");
        }
        
        FTPFile[] files = ftpClient.listFiles(remoteFile);
        if (files.length == 1) {
            if(FtpUtils_bak.RESET_MODEL==operaModel){
                logger.debug("非续传模式,删除远程文件重新上传");
                deleteFile(ftpClient,remoteFile);
                flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                return flag?TRANS_OK:TRANS_FAIL;
            }else if(FtpUtils_bak.RESTART_MODEL==operaModel){
                logger.debug("续传模式");
                long remoteSize = files[0].getSize();
                if(remoteSize<lFile.length()){
                    flag = continueUpdateFile(ftpClient,remoteFile, lFile, remoteSize,updateOffset,bufferSize);
                    if(TRANOFFSET_FULL==updateOffset||(remoteSize+updateOffset)>=lFile.length()){
                        return flag?TRANS_OK:TRANS_FAIL;
                    }else{
                        return flag?TRANS_CONTINU:TRANS_FAIL;
                    }
                    /*if(!flag){
                        logger.debug("断点续传失败，需删除远程文件重新上传");
                        deleteFile(ftpClient,remoteFile);
                        flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                    }*/
                }else{
                    logger.debug("远程断点文件大小大于或等于本地文件无需继续上传,remoteSize="+remoteSize+";本地文件大小="+lFile.length());
                    return TRANS_OK;
                }
            }else{
                //others
            }
        } else {
            logger.debug("远程未存在需上传文件，需创建上传");
            if(FtpUtils_bak.RESTART_MODEL==operaModel){
                flag = continueUpdateFile(ftpClient,remoteFile, lFile,0,updateOffset,bufferSize);
                if(TRANOFFSET_FULL==updateOffset||updateOffset>=lFile.length()){
                    return flag?TRANS_OK:TRANS_FAIL;
                }else{
                    return flag?TRANS_CONTINU:TRANS_FAIL;
                }
            }else{
                flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                return flag?TRANS_OK:TRANS_FAIL;
            }
        }
        return TRANS_FAIL;
    }
    
    private static String encodeUtf8ToISO(String str) throws UnsupportedEncodingException
    {
        return new String(str.getBytes("utf-8"),"iso-8859-1");
    }

    /**
     * 
    * <p>上传整个文件夹，包括文件夹中的文件以及子文件夹</p>
    * @param ftpClient
    * @param remoteDir  远程目录
    * @param localDir   本地待上传目录
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-18 上午11:11:25
     */
    public static boolean uploadDirectory(FTPClient ftpClient,String remoteDir,String localDir) throws IOException{
        boolean result = false;
        if(null==ftpClient||isEmpty(remoteDir)||isEmpty(localDir)){
            throw new NullPointerException("ftpClient and remoteDir and localDir must not be null");
        }
        remoteDir = formatPath(remoteDir);
        localDir = formatPath(localDir,File.separator);
        if(!remoteDir.endsWith(LINUX_FS_SEPARATOR)){
            remoteDir = remoteDir+LINUX_FS_SEPARATOR;
        }
        remoteDir = encodeUtf8ToISO(remoteDir);
        //ftpClient.setFileTransferMode(FTP.BLOCK_TRANSFER_MODE);
        makeOrChangeDir(ftpClient,remoteDir);
        // 去掉目录末尾的路径分隔符
        if(localDir.endsWith(File.separator)){
            localDir = localDir.substring(0, localDir.length()-1);
        }
        // 上传目录
        result = uploadDirOrFile(ftpClient,remoteDir,localDir,null);
        //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return result;
    }
    
    private static boolean uploadDirOrFile(FTPClient ftpClient,String remoteDir, String localDir, String baseDir) throws IOException {
        boolean result = false;
        remoteDir = formatPath(remoteDir);
        localDir = formatPath(localDir,File.separator);
        File lFile = new File(localDir);

        // 如果文件时目录
        if (lFile.isDirectory()) {

            baseDir = localDir.substring(localDir.lastIndexOf(File.separator) + 1)+LINUX_FS_SEPARATOR;
            // 改变FTP服务器的路径
            makeOrChangeDir(ftpClient,baseDir);
            // 列出本地文件列表
            File[] subFiles = lFile.listFiles();
            for (File f : subFiles) {
                result = uploadDirOrFile(ftpClient,remoteDir,f.getPath(), baseDir);
                if(!result){
                    break;
                }
            }
            // 回答上一级目录
            ftpClient.changeToParentDirectory();
            ftpClient.printWorkingDirectory();
            return result;
        } else {

            String remoteFileName = lFile.getName();
            // 开始上传文件
            return uploadFileDefault(ftpClient,remoteFileName, localDir)==TRANS_OK?true:false;
        }
    }
    
    /**
     * 
    * <p>断点下载</p>
    * @param ftpClient
    * @param remoteFile
    * @param localFile
    * @param operaModel 传输模式
    * @param offset  传输偏移量
    * @param bufferSize 传输缓存大小
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @exception IllegalArgumentException If source filePath is not exist any file.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-18 下午4:11:11
     */
    public static int downloadFile(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize) throws IOException {
         boolean flag = false;//continueDownLoadFile执行结果
         if(null==ftpClient||isEmpty(remoteFile)||isEmpty(localFile)){
             throw new NullPointerException("ftpClient and remoteFile and localFile must not be null");
         }
         
         localFile = formatPath(localFile,File.separator);
         remoteFile = formatPath(remoteFile,FtpUtils_bak.LINUX_FS_SEPARATOR);
         remoteFile = encodeUtf8ToISO(remoteFile);
         
         FTPFile[] remoteFiles = ftpClient.listFiles(remoteFile);
         if(remoteFiles.length!=1){
             logger.debug("远程目录："+remoteFile+"不存在该文件");
             throw new IllegalArgumentException("远程目录："+remoteFile+"不存在该文件");
         }
         
         if(localFile.endsWith(File.separator)){
             localFile = localFile + remoteFile.substring(remoteFile.lastIndexOf(LINUX_FS_SEPARATOR)+1);
         }
         File lFile = new File(localFile);
         if (lFile.exists()) {
             if(FtpUtils_bak.RESET_MODEL==operaModel){
                 logger.debug("非续传模式,删除本地文件重新下载");
                 lFile.delete();
                 flag = continueDownLoadFile(ftpClient,remoteFile,lFile,0,TRANOFFSET_FULL,bufferSize);
                 return flag?TRANS_OK:TRANS_FAIL;
             }else if(FtpUtils_bak.RESTART_MODEL==operaModel){
                 logger.debug("续传模式");
                 long size = lFile.length();
                 if(size<remoteFiles[0].getSize()){
                     flag = continueDownLoadFile(ftpClient,remoteFile, lFile, size,offset,bufferSize);
                     if(TRANOFFSET_FULL==offset||(size+offset)>=remoteFiles[0].getSize()){
                         return flag?TRANS_OK:TRANS_FAIL;
                     }else{
                         return flag?TRANS_CONTINU:TRANS_FAIL;
                     }
                     /*if(!flag){
                         logger.debug("断点续传失败，需删除本地文件重新下载");
                         lFile.delete();
                         flag = continueDownLoadFile(ftpClient,remoteFile, lFile, 0,updateOffset,bufferSize);
                     }*/
                 }else{
                     logger.debug("本地文件大小大于或等于远程文件无需继续传输（下载）,remoteSize="+remoteFiles[0].getSize()+";本地文件大小="+size);
                     return FtpUtils_bak.TRANS_OK;
                 }
             }else{
                 //其他
             }
         } else {
             logger.debug("本地未存在文件，需创建并下载");
             if(FtpUtils_bak.RESTART_MODEL==operaModel){
                 flag = continueDownLoadFile(ftpClient,remoteFile, lFile,0,offset,bufferSize);
                 if(TRANOFFSET_FULL==offset||offset>=remoteFiles[0].getSize()){
                     return flag?TRANS_OK:TRANS_FAIL;
                 }else{
                     return flag?TRANS_CONTINU:TRANS_FAIL;
                 }
             }else{
                 flag = continueDownLoadFile(ftpClient,remoteFile, lFile, 0,TRANOFFSET_FULL,bufferSize);
                 return flag?TRANS_OK:TRANS_FAIL;
             }
         }
        return TRANS_FAIL;
    }
    
    /**
     * 
    * <p>下载文件,模式为非续传,缓存为小缓存</p>
    * @param ftpClient
    * @param remoteFile
    * @param localFile
    * @return
    * @throws IOException
    * @author liyongquan@avit.com.cn
    * @date 2017-1-19 上午10:20:24
     */
    public static int downloadFileDefault(FTPClient ftpClient,String remoteFile, String localFile) throws IOException{
        return downloadFile(ftpClient, remoteFile,localFile,FtpUtils_bak.RESET_MODEL, FtpUtils_bak.TRANOFFSET_FULL, FtpUtils_bak.BUFFERSIZE_S);
    }
    
    /**
     * 
    * <p>以输出流形式下载</p>
    * @param ftpClient
    * @param remoteFile
    * @param localFileOut 读出流
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-19 上午10:21:04
     */
    public static boolean downloadFile(FTPClient ftpClient,String remoteFile, OutputStream localFileOut) throws IOException {
        remoteFile = encodeUtf8ToISO(formatPath(remoteFile));
        // 设置以二进制流的方式传输
        //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //ftpClient.retr
        return ftpClient.retrieveFile(remoteFile, localFileOut);
    }
    
    public static boolean downloadDir(FTPClient ftpClient,String remoteDir, String localDir) throws IOException{
        if(null==ftpClient||isEmpty(remoteDir)||isEmpty(localDir)){
            throw new NullPointerException("ftpClient and remoteDir and localDir must not be null");
        }
        boolean result = false;
        remoteDir = encodeUtf8ToISO(formatPath(remoteDir));
        localDir = formatPath(localDir,File.separator);
        // 设置以二进制流的方式传输
        //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
        if(ftpFiles.length<1){
            logger.debug("远程目录为空");
            return true;
        }
        String basePath = null;
        
        if(remoteDir.endsWith(LINUX_FS_SEPARATOR)){
            remoteDir = remoteDir.substring(0,remoteDir.length()-1);
        }
        basePath = remoteDir.substring(remoteDir.lastIndexOf(LINUX_FS_SEPARATOR)+1);
        localDir = localDir+File.separator+basePath;
        
        OutputStream fileOS = null;
        for (FTPFile ftpf : ftpFiles) {
            if(ftpf.isFile()){
                String name = ftpf.getName();
                File file = new File(localDir + File.separator + name);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
               
                try{
                    fileOS = new BufferedOutputStream(new FileOutputStream(file));
                    String remoteFileName = remoteDir + LINUX_FS_SEPARATOR + name;
                    result = ftpClient.retrieveFile(remoteFileName, fileOS);
                    if(!result){
                        logger.debug("远程文件"+remoteFileName+"下载失败");
                        break;
                    }
                }finally{
                    if (fileOS != null) {
                        fileOS.close();
                        fileOS = null;
                    }
                }
            }else if(ftpf.isDirectory()){
                String remoteDirTemp = new String(remoteDir);
                remoteDirTemp = remoteDirTemp+LINUX_FS_SEPARATOR+ftpf.getName();
                result = downloadDir(ftpClient,remoteDirTemp, localDir);
                if(!result){
                    logger.debug("远程目录"+remoteDir+"下载失败");
                    break;
                }
            }
            
        }
        //返回上级目录
        remoteDir = remoteDir.substring(0,remoteDir.lastIndexOf(LINUX_FS_SEPARATOR));
        localDir = localDir.substring(0,localDir.lastIndexOf(File.separator));
        return result;
    }
    
    /**
     * 
    * <p>移动文件</p>
    * @param ftpClient
    * @param from 源文件句柄
    * @param to 目的地 文件句柄或文件路径（以/结尾）
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @exception IllegalArgumentException If source filePath is not exist any file.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-20 上午9:25:29
     */
    public static boolean moveFile(FTPClient ftpClient,String from, String to) throws IOException {
        if(null==ftpClient||isEmpty(from)||isEmpty(to)){
            throw new NullPointerException("ftpClient and fromPath and toPath must not be null");
        }
        from = encodeUtf8ToISO(formatPath(from));
        to = encodeUtf8ToISO(formatPath(to));
        String homePath = ftpClient.printWorkingDirectory();
        String toTemp = new String(to);
        if(!toTemp.endsWith(LINUX_FS_SEPARATOR)){
            if(toTemp.contains(LINUX_FS_SEPARATOR)){
                toTemp = toTemp.substring(0,toTemp.lastIndexOf(LINUX_FS_SEPARATOR)+1);
            }
        }else{
            to = to+from.substring(from.lastIndexOf(LINUX_FS_SEPARATOR)+1);
        }
        makeOrChangeDir(ftpClient, toTemp);
        //切换用户家目录
        makeOrChangeDir(ftpClient, homePath+LINUX_FS_SEPARATOR);
        FTPFile[] files = ftpClient.listFiles(from);
        if (files.length > 0) {
            return ftpClient.rename(from, to);
        }else{
            logger.debug("该目录中没有该文件");
            throw new IllegalArgumentException("该目录"+from+"中没有该文件");
        }
    }
    
    /**
     * 
    * <p>删除文件</p>
    * @param ftpClient
    * @param filename
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @exception IllegalArgumentException If source filePath is not exist any file.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-20 上午10:10:18
     */
    public static boolean deleteFile(FTPClient ftpClient,String filename) throws IOException {
        if(null==ftpClient||isEmpty(filename)){
            throw new NullPointerException("ftpClient and filename must not be null");
        }
        boolean result = true;
        
        FTPFile[] files = ftpClient.listFiles(filename);
        if (files.length > 0){
            result = ftpClient.deleteFile(filename);
        }else{
            logger.debug("删除的文件未存在");
            throw new IllegalArgumentException("删除的文件"+filename+"未存在");
        }
        
        return result;
    }
    
    private static boolean deleteDir(FTPClient ftpClient,String remoteDir) throws IOException {
        if(null==ftpClient||null==remoteDir){
            throw new IllegalArgumentException("ftpClient and remoteDir must not be null");
        }
        
        boolean result = false;
        if(!remoteDir.endsWith(LINUX_FS_SEPARATOR)){
            remoteDir = remoteDir+LINUX_FS_SEPARATOR;
        }
        FTPFile[] files = ftpClient.listFiles(remoteDir);
        makeOrChangeDir(ftpClient, remoteDir);
        
        if (files.length > 0){
            for(FTPFile ftpFile :files){
               if(ftpFile.isFile()){
                   result = ftpClient.deleteFile(ftpFile.getName());
                   if(!result){
                       logger.debug("文件"+ftpFile.getName()+"删除失败");
                       break;
                   }
               }else if(ftpFile.isDirectory()){
                   result = deleteDir(ftpClient,ftpFile.getName());
                   if(!result){
                       logger.debug("文件目录"+ftpFile.getName()+"删除失败");
                       break;
                   }
               }
            }
        }else{
            result = true;
            logger.debug("删除的文件未存在");
        }
        
        // 回答上一级目录
        ftpClient.changeToParentDirectory();
        ftpClient.printWorkingDirectory();
        if(result){
            result = ftpClient.removeDirectory(remoteDir.substring(0,remoteDir.length()-1));
        }
        return result;
    }
    
    /**
     * 
    * <p>删除目录</p>
    * @param ftpClient
    * @param remoteDir
    * @return
    * @throws IOException
    * @exception NullPointerException If someone of the params is null.
    * @author liyongquan@avit.com.cn
    * @date 2017-1-20 下午2:13:33
     */
    public static boolean deleteDirectory(FTPClient ftpClient,String remoteDir) throws IOException {
        if(null==ftpClient||isEmpty(remoteDir)){
            throw new NullPointerException("ftpClient and remoteDir must not be null");
        }
        String basePath = null;
        String subRemoteDir = null;
        if(remoteDir.endsWith(LINUX_FS_SEPARATOR)){
            remoteDir = remoteDir.substring(0,remoteDir.length()-1);
        }
        
        subRemoteDir = remoteDir.substring(remoteDir.lastIndexOf(LINUX_FS_SEPARATOR)+1);
        basePath = remoteDir.substring(0,remoteDir.lastIndexOf(LINUX_FS_SEPARATOR)+1);
        makeOrChangeDir(ftpClient, basePath);
        
        return deleteDir(ftpClient,subRemoteDir);
    }
    
    /**
     * 上传文件到服务器,文件不存在新上传，如果文件存在则继续上传
     * 
     * @param remoteFile
     *            远程文件名（支持相对和绝对目录）,当前工作目录为登录用户的工作目录
     * @param localFile
     *            本地文件 File句柄，绝对路径
     * @param remoteSize
     *            远程文件大小
     * @return
     * @throws IOException
     */
    private static boolean continueUpdateFile(FTPClient ftpClient,String remoteFile, File localFile, long remoteSize,int bufferSize) throws IOException{
        return continueUpdateFile(ftpClient,remoteFile,localFile,remoteSize,TRANOFFSET_FULL,bufferSize);
    }
    
    /**
     * 
    * 断点上传文件
    * @param ftpClient
    * @param remoteFile
    * @param localFile
    * @param startPosition 上传文件开始点
    * @param offset 上传文件大小偏移量,负数表示偏移量移至文件末尾
    * @return
    * @throws IOException
    * @author liyongquan@avit.com.cn
    * @date 2017-1-17 上午9:09:42
     */
    private static boolean continueUpdateFile(FTPClient ftpClient,String remoteFile, File localFile, long startPosition,long offset,int bufferSize) throws IOException{
        logger.debug(String.format("params'value:remoteFile=%s;localFile=%s;startPosition=%s;offset=%s", 
                remoteFile,localFile,startPosition,offset));
        
        
        if(startPosition>localFile.length()||startPosition<0){
            logger.debug(String.format("remoteStartPosition=%s;localFile'length=%s;they's relation is wrong", 
                    startPosition,localFile.length()));
            return false;
        }
        if(offset==0){
            logger.debug("remoteOffset is 0");
            return false;
        }
        ftpClient.setBufferSize(bufferSize);   
        String remoteFileName = remoteFile;
        if(remoteFile.contains(LINUX_FS_SEPARATOR)){
            remoteFileName = remoteFile.substring(remoteFile.lastIndexOf("/")+1);
            String directory = remoteFile.substring(0, remoteFile.lastIndexOf("/")+1);
            makeOrChangeDir(ftpClient, directory);
        }
        
        long step = localFile.length() / 100;
        long process = 0;
        long localreadbytes = 0L;
        
        OutputStream out = null;
        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(localFile, "r");
            out = ftpClient.appendFileStream(new String(remoteFileName));// .appendFileStream(new
            // 续传
            ftpClient.setRestartOffset(startPosition);
            if (step != 0) {
                process = startPosition / step;
            }
            raf.seek(startPosition);
            localreadbytes = startPosition;
            
            byte[] bytes = new byte[bufferSize];
            int c;
            while ((c = raf.read(bytes)) != -1) {
                out.write(bytes, 0, c);
                localreadbytes += c;
                
                if (step != 0) {
                    if (localreadbytes / step > process) {
                        process = localreadbytes / step;
                        if(process%10==0){
                            logger.debug("上传进度:"+process+"%");
                        }
                    }
                }
                if(offset>0 && (localreadbytes-startPosition)>=offset){
                    logger.debug("readbytes offset:"+(localreadbytes-startPosition)+" is over remoteOffset:"+offset+",will break out update");
                    break;
                }
            }
            out.flush();
        }finally{
            if(null!=out){
                logger.debug("远程文件输出流out已写完，即将关闭");
                out.close();
                logger.debug("远程文件输出流out已关闭");
            }
            if(null!=raf){
                logger.debug("本地文件的RandomAccessFile已写完，即将关闭");
                raf.close();
                logger.debug("本地文件的RandomAccessFile已关闭");
            }
        }
        
        return ftpClient.completePendingCommand();
    }
    
    private static boolean continueDownLoadFile(FTPClient ftpClient,String remoteFile, File localFile, long startPosition,long offset,int bufferSize) throws IOException{
        logger.debug(String.format("params'value:remoteFile=%s;localFile=%s;startPosition=%s;offset=%s", 
                remoteFile,localFile,startPosition,offset));
        FTPFile[] remoteFiles = ftpClient.listFiles(remoteFile);
        if(remoteFiles.length!=1){
            logger.debug("远程目录："+remoteFile+" 不存在对应文件");
            return false;
        }
        if(startPosition>remoteFiles[0].getSize()||startPosition<0){
            logger.debug(String.format("startPosition=%s;remoteFile'length=%s;they's relation is wrong", 
                    startPosition,remoteFiles[0].getSize()));
            return false;
        }
        if(offset==0){
            logger.debug("remoteOffset is 0");
            return false;
        }
        
        //File file = new File(localFile);
        if (!localFile.exists()) {
            if(!localFile.getParentFile().exists()){
                localFile.getParentFile().mkdirs();
            }
            localFile.createNewFile();
        }
        
        long step = remoteFiles[0].getSize() / 100;
        long process = 0;
        long readbytes = 0L;
        
        InputStream in = null;
        //RandomAccessFile raf = null;
        OutputStream out = null;
        try{
            //raf = new RandomAccessFile(localFile,"rw");
            out = new BufferedOutputStream(new FileOutputStream(localFile,true));
            ftpClient.setBufferSize(bufferSize); 
            //ftpClient.setFileType(FTP.LOCAL_FILE_TYPE);
            //ftpClient.setFileTransferMode(FTP.BLOCK_TRANSFER_MODE);
            ftpClient.setRestartOffset(startPosition);
            in =ftpClient.retrieveFileStream(remoteFile);
            //in =new BufferedInputStream(ftpClient.retrieveFileStream(remoteFile));
            
            //raf.seek(startPosition);
            // 续传
            if (step != 0) {
                process = startPosition / step;
            }
            readbytes = startPosition;
            
            byte[] bytes = new byte[bufferSize];
            int c;
            while ((c = in.read(bytes)) != -1) {
                //raf.write(bytes, 0, c);
                out.write(bytes, 0, c);
                readbytes += c;
                //logger.debug("----------"+readbytes);
                if (step != 0) {
                    if (readbytes / step > process) {
                        process = readbytes / step;
                        if(process%10==0){
                            //out.flush();
                            logger.debug("下载进度:"+process+"%");
                        }
                    }
                }
                if(offset>0 && (readbytes-startPosition)>=offset){
                    logger.debug("readbytes offset:"+(readbytes-startPosition)+" is over remoteOffset:"+offset+",will break out");
                    break;
                }
            }
            out.flush();
            //result = ftpClient.completePendingCommand();
        }finally{
            if(null!=in){
                logger.debug("远程文件的输入流已读完，即将关闭");
                in.close();
                logger.debug("远程文件的输入流已关闭");
            }
            
            if(null!=out){
                logger.debug("本地文件输出流已写完，即将关闭");
                out.close();
                logger.debug("本地文件输出流已关闭");
            }
        }
        //return result;
        int code = ftpClient.getReply();
        return (((code >= 200 && code < 300))||code==426);
    }
    
    public static String formatPath(String path,String separator){
        if(null==path||null==separator){
            throw new IllegalArgumentException("path and separator must not be null");
        }
        return path.replace(WIN_FS_SEPARATOR, separator);
    }
    
    public static String formatPath(String path){
        if(null==path){
            throw new IllegalArgumentException("path must not be null");
        }
        return path.replace(WIN_FS_SEPARATOR, LINUX_FS_SEPARATOR);
    }
    
    /**
     *   改变FTP服务器的工作路径,
     *   目录不存在，则创建目录，然后进入目录.
     * @param remoteDir 支持绝对路径和相对路径，目录需以/结尾，如/home/test/
     * @throws IOException
     */
    public static void makeOrChangeDir(FTPClient ftpClient,String remoteDir) throws IOException {
        if(null==ftpClient){
            throw new IllegalArgumentException("ftpClient must not be null");
        }
        if(isEmpty(remoteDir)){
            logger.debug("切换的目录值为空:"+remoteDir);
            return;
        }
        String directory = remoteDir;
        if (!ftpClient.changeWorkingDirectory(new String(directory.getBytes("UTF-8"), "iso-8859-1"))
                &&!directory.equals(FtpUtils_bak.LINUX_FS_SEPARATOR)) {
            // 如果远程目录不存在，则递归创建远程服务器目录
            int start = 0;
            int end = 0;
            /*if (directory.startsWith("/")) {
                start = 0;
            } else {
                start = 0;
            }*/
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remoteDir.substring(start, end+1).getBytes("UTF-8"), "iso-8859-1");
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        logger.debug("创建目录失败,可能该用户权限不够");
                        return;
                    }
                }

                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
    }
    
    private static boolean isEmpty(String str){
        
        return null==str||"".equals(str);
    }
    
}
