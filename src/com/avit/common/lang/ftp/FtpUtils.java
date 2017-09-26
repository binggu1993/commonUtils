package com.avit.common.lang.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;


public class FtpUtils
{
    private static Logger logger = Logger.getLogger(FtpUtils.class);
    
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
    

    private static FtpReply deleteFile(FTPClient ftpClient,String filename){
        if(null==ftpClient||isEmpty(filename)){
            throw new NullPointerException("ftpClient and filename must not be null");
        }
        try
        {
            FTPFile[] files = ftpClient.listFiles(filename);
            if (files.length > 0){
                if(ftpClient.deleteFile(filename)){
                    return FtpReply.COMMAND_OK;
                }else{
                    return new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
                }
            }else{
                logger.debug("删除的文件未存在");
                return new FtpReply(FtpReplyCode.FILE_NOT_EXIST,FtpReplyCode.FILE_NOT_EXIST_MSG);
            }
        }
        catch (IOException e)
        {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }
    }
    
    public static FtpReply deleteFile(FtpRequest ftpRequest,String filename){
        FTPClient ftpClient = null;
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            return deleteFile(ftpClient,filename);
        }
        catch (ConnectFailException e)
        {
            logger.error(e,e);
            return new FtpReply(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }
        finally{
            disconnect(ftpClient);
        }
    }
    
    private static FtpReply deleteDir(FTPClient ftpClient,String remoteDir) throws IOException {
        if(null==ftpClient||null==remoteDir){
            throw new IllegalArgumentException("ftpClient and remoteDir must not be null");
        }
        
        FtpReply result = FtpReply.COMMAND_OK;
        if(!remoteDir.endsWith(LINUX_FS_SEPARATOR)){
            remoteDir = remoteDir+LINUX_FS_SEPARATOR;
        }
        FTPFile[] files = ftpClient.listFiles(remoteDir);
        
        if (files.length > 0){
            /*result = makeOrChangeDir(ftpClient, remoteDir);
            if(!FtpReply.isPositiveCompletion(result.getCode())){
                return result;
            }*/
            ftpClient.changeWorkingDirectory(remoteDir);
            for(FTPFile ftpFile :files){
               if(ftpFile.isFile()){
                   if(!ftpClient.deleteFile(ftpFile.getName())){
                       logger.debug("文件"+ftpFile.getName()+"删除失败");
                       result = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
                       break;
                   }
               }else if(ftpFile.isDirectory()){
                   result = deleteDir(ftpClient,ftpFile.getName());
                   if(!FtpReply.isPositiveCompletion(result.getCode())){
                       logger.debug("文件目录"+ftpFile.getName()+"删除失败");
                       break;
                   }
               }
            }
         // 回答上一级目录
            ftpClient.changeToParentDirectory();
            //ftpClient.printWorkingDirectory();
        }else{
            logger.debug("删除的文件未存在");
            return new FtpReply(FtpReplyCode.FILE_NOT_EXIST, FtpReplyCode.FILE_NOT_EXIST_MSG);
        }
        
        if(FtpReply.isPositiveCompletion(result.getCode())){
            if(!ftpClient.removeDirectory(remoteDir.substring(0,remoteDir.length()-1))){
                result = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
            }
        }
        return result;
    }
    
    private static FtpReply deleteDirectory(FTPClient ftpClient,String remoteDir) throws IOException {
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
        
        //FtpReply ftpReply = makeOrChangeDir(ftpClient, basePath);
        if(!ftpClient.changeWorkingDirectory(basePath)){
            return new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
        }
        
        return deleteDir(ftpClient,subRemoteDir);
    }
    
    public static FtpReply deleteDirectory(FtpRequest ftpRequest,String remoteDir){
        
        FTPClient ftpClient = null;
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            return deleteDirectory(ftpClient,remoteDir);
        }
        catch (ConnectFailException e)
        {
            logger.error(e,e);
            return new FtpReply(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }
        finally{
            disconnect(ftpClient);
        }
    }
    
    public static FtpReply updateFile(FtpRequest ftpRequest,String remoteFile,String localFile){
        FTPClient ftpClient = null;
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            
            return updateFile(ftpClient,remoteFile,localFile,ftpRequest.getOperaModel(),ftpRequest.getTransOffset(),ftpRequest.getBufferSize());
            /*int operaModel = ftpRequest.getOperaModel();
            if(null==ftpClient||isEmpty(localFile)||isEmpty(remoteFile)){
                throw new NullPointerException("ftpClient and remoteFile and localFile must not be empty");
            }
            boolean flag = false;
            localFile = formatPath(localFile,File.separator);
            remoteFile = formatPath(remoteFile,MyFtpUtils.LINUX_FS_SEPARATOR);
            if(remoteFile.endsWith(MyFtpUtils.LINUX_FS_SEPARATOR)){
                String fileName = localFile.substring(localFile.lastIndexOf(File.separator)+1);
                remoteFile = remoteFile+fileName;
            }
            remoteFile = encodeUtf8ToISO(remoteFile);
            File lFile = new File(localFile);
            if(!lFile.exists()){
                logger.debug("本地目录："+localFile+"不存在该文件");
                return new FtpReply(FtpReplyCode.LOCALFILE_NOT_EXIST,FtpReplyCode.LOCALFILE_NOT_EXIST_MSG);
            }
            
            FTPFile[] files = ftpClient.listFiles(remoteFile);
            if (files.length == 1) {
                if(MyFtpUtils.RESET_MODEL==operaModel){
                    logger.debug("非续传模式,删除远程文件重新上传");
                    FtpReply deleReply = deleteFile(ftpClient,remoteFile);
                    if(!FtpReply.isPositiveCompletion(deleReply.getCode())){
                        return deleReply;
                    }
                    flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,ftpRequest.getBufferSize());
                    return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;
                }else if(MyFtpUtils.RESTART_MODEL==operaModel){
                    logger.debug("续传模式");
                    long remoteSize = files[0].getSize();
                    if(remoteSize<lFile.length()){
                        flag = continueUpdateFile(ftpClient,remoteFile, lFile, remoteSize,ftpRequest.getTransOffset(),ftpRequest.getBufferSize());
                        if(TRANOFFSET_FULL==ftpRequest.getTransOffset()||(remoteSize+ftpRequest.getTransOffset())>=lFile.length()){
                            return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;
                        }else{
                            return flag?FtpReply.TRANS_CONTINUE:FtpReply.TRANS_FAIL;
                        }
                        if(!flag){
                            logger.debug("断点续传失败，需删除远程文件重新上传");
                            deleteFile(ftpClient,remoteFile);
                            flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                        }
                    }else{
                        logger.debug("远程断点文件大小大于或等于本地文件无需继续上传,remoteSize="+remoteSize+";本地文件大小="+lFile.length());
                        return new FtpReply(FtpReplyCode.TRANS_NOT_NEED, FtpReplyCode.TRANS_NOT_NEED_MSG);
                    }
                }else{
                    //others
                    return FtpReply.COMMAND_OK;
                }
            } else {
                logger.debug("远程未存在需上传文件，需创建上传");
                if(FtpUtils.RESTART_MODEL==operaModel){
                    logger.debug("续传模式");
                    flag = continueUpdateFile(ftpClient,remoteFile, lFile,0,ftpRequest.getTransOffset(),ftpRequest.getBufferSize());
                    if(TRANOFFSET_FULL==ftpRequest.getTransOffset()||ftpRequest.getTransOffset()>=lFile.length()){
                        return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;
                    }else{
                        return flag?FtpReply.TRANS_CONTINUE:FtpReply.TRANS_FAIL;
                    }
                }else{
                    logger.debug("非续传模式");
                    flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,ftpRequest.getBufferSize());
                    return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;
                }
            }*/
            
        }
        catch (ConnectFailException e)
        {
            logger.error(e,e);
            return new FtpReply(e.getCode(), e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }finally{
            disconnect(ftpClient);
        }
    }
    
    private static FtpReply updateFile(FTPClient ftpClient,String remoteFile, String localFile,int bufferSize){
        return updateFile(ftpClient,remoteFile,localFile,FtpUtils.RESET_MODEL,FtpUtils.TRANOFFSET_FULL,bufferSize);
    }
    
    private static FtpReply updateFile(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize){
        
        if(null==ftpClient||isEmpty(localFile)||isEmpty(remoteFile)){
            logger.debug(String.format("ftpClient:%s;remoteFile:%s;localFile:%s;", ftpClient,remoteFile,localFile));
            return new FtpReply(FtpReplyCode.NULL_POINT, FtpReplyCode.NULL_POINT_MSG);
        }
        if(offset==0||offset<-1){
            logger.debug("offset:"+offset);
            return new FtpReply(FtpReplyCode.ILLEGAL_Argument, "[offset]"+FtpReplyCode.ILLEGAL_Argument_MSG);
        }
        if(bufferSize<0){
            logger.debug("bufferSize:"+bufferSize);
            return new FtpReply(FtpReplyCode.ILLEGAL_Argument, "[bufferSize]"+FtpReplyCode.ILLEGAL_Argument_MSG);
        }
        
        localFile = formatPath(localFile,File.separator);
        remoteFile = formatPath(remoteFile,FtpUtils.LINUX_FS_SEPARATOR);
        if(remoteFile.endsWith(FtpUtils.LINUX_FS_SEPARATOR)){
            String fileName = localFile.substring(localFile.lastIndexOf(File.separator)+1);
            remoteFile = remoteFile+fileName;
        }
        remoteFile = encodeUtf8ToISO(remoteFile);
        File lFile = new File(localFile);
        if(!lFile.exists()){
            logger.debug("本地目录："+localFile+"不存在该文件");
            return new FtpReply(FtpReplyCode.LOCALFILE_NOT_EXIST,FtpReplyCode.LOCALFILE_NOT_EXIST_MSG);
        }
        if(bufferSize==0){
            bufferSize = getDefaultRecomendBufferSize(lFile.length());
        }
        
        FtpReply updateReply =null;
        try
        {
            FTPFile[] files = ftpClient.listFiles(remoteFile);
            if (files.length > 0) {
                if(FtpUtils.RESET_MODEL==operaModel){
                    logger.debug("非续传模式,删除远程文件重新上传");
                    FtpReply deleReply = deleteFile(ftpClient,remoteFile);
                    if(!FtpReply.isPositiveCompletion(deleReply.getCode())){
                        return deleReply;
                    }
                    /*flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                    return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;*/
                    if(lFile.length()<20*1024*1024){//20M
                        return storeUpdateFile(ftpClient, remoteFile, lFile, bufferSize);
                    }else{
                        return continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                    }
                    
                }else if(FtpUtils.RESTART_MODEL==operaModel){
                    logger.debug("续传模式");
                    long remoteSize = files[0].getSize();
                    if(remoteSize<lFile.length()){
                        updateReply = continueUpdateFile(ftpClient,remoteFile, lFile, remoteSize,offset,bufferSize);
                        if(TRANOFFSET_FULL==offset||(remoteSize+offset)>=lFile.length()){
                            return updateReply;
                        }else{
                            return FtpReply.isPositiveCompletion(updateReply.getCode())?FtpReply.TRANS_CONTINUE:updateReply;
                        }
                    }else{
                        logger.debug("远程断点文件大小大于或等于本地文件无需继续上传,remoteSize="+remoteSize+";本地文件大小="+lFile.length());
                        return new FtpReply(FtpReplyCode.TRANS_NOT_NEED, FtpReplyCode.TRANS_NOT_NEED_MSG);
                    }
                }else{
                    //others
                    return FtpReply.COMMAND_OK;
                }
            } else {
                logger.debug("远程未存在需上传文件，需创建上传");
                if(FtpUtils.RESTART_MODEL==operaModel){
                    logger.debug("续传模式");
                    updateReply = continueUpdateFile(ftpClient,remoteFile, lFile,0,offset,bufferSize);
                    if(TRANOFFSET_FULL==offset||offset>=lFile.length()){
                        return updateReply;
                    }else{
                        return FtpReply.isPositiveCompletion(updateReply.getCode())?FtpReply.TRANS_CONTINUE:updateReply;
                    }
                }else{
                    logger.debug("非续传模式");
                    /*flag = continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                    return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;*/
                    //return storeUpdateFile(ftpClient, remoteFile, lFile, bufferSize);
                    if(lFile.length()<20*1024*1024){//20M
                        return storeUpdateFile(ftpClient, remoteFile, lFile, bufferSize);
                    }else{
                        return continueUpdateFile(ftpClient,remoteFile, lFile, 0,bufferSize);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }
    }

    private static int getDefaultRecomendBufferSize(Long lFileSize)
    {
        int bufferSize;
        if(lFileSize<5*FtpUtils.Unit_M){
            bufferSize = FtpUtils.BUFFERSIZE_S;
        }else{
            bufferSize = FtpUtils.BUFFERSIZE_M;
        }
        return bufferSize;
    }

    public static FtpReply uploadDirectory(FtpRequest ftpRequest,String remoteDir,String localDir){
        FTPClient ftpClient = null;
        if(isEmpty(remoteDir)||isEmpty(localDir)){
            throw new NullPointerException("remoteDir and localDir must not be null");
        }
        remoteDir = formatPath(remoteDir);
        localDir = formatPath(localDir,File.separator);
        
        File file = new File(localDir);
        if(!file.exists()){
            logger.debug("本地目录："+localDir+"不存在该目录");
            return new FtpReply(FtpReplyCode.LOCALFILE_NOT_EXIST,FtpReplyCode.LOCALFILE_NOT_EXIST_MSG);
        }
        
        if(!remoteDir.endsWith(LINUX_FS_SEPARATOR)){
            remoteDir = remoteDir+LINUX_FS_SEPARATOR;
        }
        remoteDir = encodeUtf8ToISO(remoteDir);
        
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            ftpReply = makeOrChangeDir(ftpClient,remoteDir);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            // 去掉目录末尾的路径分隔符
            if(localDir.endsWith(File.separator)){
                localDir = localDir.substring(0, localDir.length()-1);
            }
            // 上传目录
            return uploadDirOrFile(ftpClient,remoteDir,localDir,null,ftpRequest.getBufferSize());
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        }
        catch (ConnectFailException e)
        {
            logger.error(e);
            return new FtpReply(e.getCode(), e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e);
            return FtpReply.COMMAND_FAIL;
        }finally{
            disconnect(ftpClient);
        }
    }
    
    private static FtpReply uploadDirOrFile(FTPClient ftpClient,String remoteDir, String localDir, String baseDir,int bufferSize) throws IOException {
        FtpReply ftpReply = null;
        remoteDir = formatPath(remoteDir);
        localDir = formatPath(localDir,File.separator);
        File lFile = new File(localDir);
        if(!lFile.exists()){
            logger.debug("本地目录："+localDir+"不存在该目录");
            return new FtpReply(FtpReplyCode.LOCALFILE_NOT_EXIST,FtpReplyCode.LOCALFILE_NOT_EXIST_MSG);
        }
        // 如果文件时目录
        if (lFile.isDirectory()) {

            baseDir = localDir.substring(localDir.lastIndexOf(File.separator) + 1)+LINUX_FS_SEPARATOR;
            // 改变FTP服务器的路径
            ftpReply = makeOrChangeDir(ftpClient,baseDir);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            // 列出本地文件列表
            File[] subFiles = lFile.listFiles();
            for (File f : subFiles) {
                ftpReply = uploadDirOrFile(ftpClient,remoteDir,f.getPath(), baseDir,bufferSize);
                if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                    break;
                }
            }
            // 回答上一级目录
            ftpClient.changeToParentDirectory();
            ftpClient.printWorkingDirectory();
            return ftpReply;
        } else {

            String remoteFileName = lFile.getName();
            // 开始上传文件
            return updateFile(ftpClient,remoteFileName, localDir,bufferSize);
        }
    }
    
    public static FtpReply downloadFile(FtpRequest ftpRequest,String remoteFile, String localFile){
        FTPClient ftpClient = null;
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            return downloadFile(ftpClient,remoteFile,localFile,ftpRequest.getOperaModel(),ftpRequest.getTransOffset(),ftpRequest.getBufferSize());
        }
        catch (ConnectFailException e)
        {
            logger.error(e,e);
            return new FtpReply(e.getCode(), e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }finally{
            disconnect(ftpClient);
        }
    }
    
    
    private static FtpReply downloadFile(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize){
        FtpReply ftpReply = null;
        if(null==ftpClient||isEmpty(remoteFile)||isEmpty(localFile)){
            throw new NullPointerException("ftpClient and remoteFile and localFile must not be null");
        }
        if(offset==0||offset<-1){
            logger.debug("offset:"+offset);
            return new FtpReply(FtpReplyCode.ILLEGAL_Argument, "[offset]"+FtpReplyCode.ILLEGAL_Argument_MSG);
        }
        if(bufferSize<0){
            logger.debug("bufferSize:"+bufferSize);
            return new FtpReply(FtpReplyCode.ILLEGAL_Argument, "[bufferSize]"+FtpReplyCode.ILLEGAL_Argument_MSG);
        }
        
        localFile = formatPath(localFile,File.separator);
        remoteFile = formatPath(remoteFile,FtpUtils.LINUX_FS_SEPARATOR);
        remoteFile = encodeUtf8ToISO(remoteFile);
        
        try
        {
            FTPFile[] remoteFiles = ftpClient.listFiles(remoteFile);
            if(remoteFiles.length!=1){
                logger.debug("远程目录："+remoteFile+"不存在该文件");
                return new FtpReply(FtpReplyCode.FILE_NOT_EXIST,FtpReplyCode.FILE_NOT_EXIST_MSG);
            }
            if(bufferSize==0){
                bufferSize = getDefaultRecomendBufferSize(remoteFiles[0].getSize());
            }
            
            if(localFile.endsWith(File.separator)){
                localFile = localFile + remoteFile.substring(remoteFile.lastIndexOf(LINUX_FS_SEPARATOR)+1);
            }
            File lFile = new File(localFile);
            if (lFile.exists()) {
                if(FtpUtils.RESET_MODEL==operaModel){
                    logger.debug("非续传模式,删除本地文件重新下载");
                    if(!lFile.delete()){
                        return FtpReply.COMMAND_FAIL;
                    }
                    return continueDownLoadFile(ftpClient,remoteFile,lFile,0,TRANOFFSET_FULL,bufferSize);
                }else if(FtpUtils.RESTART_MODEL==operaModel){
                    logger.debug("续传模式");
                    long size = lFile.length();
                    if(size<remoteFiles[0].getSize()){
                        ftpReply = continueDownLoadFile(ftpClient,remoteFile, lFile, size,offset,bufferSize);
                        if(TRANOFFSET_FULL==offset||(size+offset)>=remoteFiles[0].getSize()){
                            return ftpReply;
                        }else{
                            return FtpReply.isPositiveCompletion(ftpReply.getCode())?FtpReply.TRANS_CONTINUE:ftpReply;
                        }
                    }else{
                        logger.debug("本地文件大小大于或等于远程文件无需继续传输（下载）,remoteSize="+remoteFiles[0].getSize()+";本地文件大小="+size);
                        return new FtpReply(FtpReplyCode.TRANS_NOT_NEED, FtpReplyCode.TRANS_NOT_NEED_MSG);
                    }
                }else{
                    //其他
                    return FtpReply.COMMAND_OK;
                }
            } else {
                logger.debug("本地未存在文件，需创建并下载");
                if(FtpUtils.RESTART_MODEL==operaModel){
                    ftpReply = continueDownLoadFile(ftpClient,remoteFile, lFile,0,offset,bufferSize);
                    if(TRANOFFSET_FULL==offset||offset>=remoteFiles[0].getSize()){
                        return ftpReply;
                    }else{
                        return FtpReply.isPositiveCompletion(ftpReply.getCode())?FtpReply.TRANS_CONTINUE:ftpReply;
                    }
                }else{
                    return continueDownLoadFile(ftpClient,remoteFile, lFile, 0,TRANOFFSET_FULL,bufferSize);
                    //return flag?FtpReply.COMMAND_OK:FtpReply.TRANS_FAIL;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            return FtpReply.COMMAND_FAIL;
        }
   }
    
    private static FtpReply continueDownLoadFile(FTPClient ftpClient,String remoteFile, File localFile, long startPosition,long offset,int bufferSize) throws IOException{
        logger.debug(String.format("params'value:remoteFile=%s;localFile=%s;startPosition=%s;offset=%s", 
                remoteFile,localFile,startPosition,offset));
        FTPFile[] remoteFiles = ftpClient.listFiles(remoteFile);
        if(remoteFiles.length!=1){
            logger.debug("远程目录："+remoteFile+" 不存在对应文件");
            return new FtpReply(FtpReplyCode.FILE_NOT_EXIST,FtpReplyCode.FILE_NOT_EXIST_MSG);
        }
        if(startPosition>remoteFiles[0].getSize()||startPosition<0){
            logger.debug(String.format("startPosition=%s;remoteFile'length=%s;they's relation is wrong", 
                    startPosition,remoteFiles[0].getSize()));
            return new FtpReply(FtpReplyCode.ILLEGAL_Argument, "[startPosition]"+FtpReplyCode.ILLEGAL_Argument_MSG);
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
            out = new BufferedOutputStream(new FileOutputStream(localFile,true),bufferSize);
            //ftpClient.setBufferSize(bufferSize); 
            ftpClient.setRestartOffset(startPosition);
            in =ftpClient.retrieveFileStream(remoteFile);
            in =new BufferedInputStream(in,bufferSize);
            
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
                //logger.debug("--------c read size "+c);
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
            //
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
        if(((code >= 200 && code < 300))||code==426){
            return FtpReply.COMMAND_OK;
        }else{
            return new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
        }
    }
    
    public static FtpReply downloadDir(FtpRequest ftpRequest,String remoteDir,String localDir){
        FTPClient ftpClient = null;
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            return downloadDir(ftpClient,remoteDir,localDir);
        }
        catch (ConnectFailException e)
        {
            logger.error(e,e);
            return new FtpReply(e.getCode(), e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }finally{
            disconnect(ftpClient);
        }
        
    }
    
    private static FtpReply downloadDir(FTPClient ftpClient,String remoteDir,String localDir){
        if(null==ftpClient||isEmpty(remoteDir)||isEmpty(localDir)){
            throw new NullPointerException("ftpClient and remoteDir and localDir must not be null");
        }
        FtpReply result = FtpReply.COMMAND_OK;
        remoteDir = encodeUtf8ToISO(formatPath(remoteDir));
        localDir = formatPath(localDir,File.separator);
        
        try
        {
            FTPFile[] ftpFiles = ftpClient.listFiles(remoteDir);
            if(ftpFiles.length<1){
                logger.debug("远程目录为空");
                return new FtpReply(FtpReplyCode.FILE_NOT_EXIST, FtpReplyCode.FILE_NOT_EXIST_MSG);
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
                        if(!ftpClient.retrieveFile(remoteFileName, fileOS)){
                            logger.debug("远程文件"+remoteFileName+"下载失败");
                            result = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
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
                    if(!FtpReply.isPositiveCompletion(result.getCode())){
                        logger.debug("远程目录"+remoteDir+"下载失败");
                        break;
                    }
                }
                
            }
            //返回上级目录
            remoteDir = remoteDir.substring(0,remoteDir.lastIndexOf(LINUX_FS_SEPARATOR));
            localDir = localDir.substring(0,localDir.lastIndexOf(File.separator));
        }
        catch (Exception e)
        {
            logger.error(e,e);
            result = FtpReply.COMMAND_FAIL;
        }
        return result;
    }
    
    private static FtpReply moveFile(FTPClient ftpClient,String from, String to) throws IOException{
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
        FTPFile[] files = ftpClient.listFiles(from);
        if (files.length > 0) {
            FtpReply ftpReply = makeOrChangeDir(ftpClient, toTemp);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            //切换用户家目录
            makeOrChangeDir(ftpClient, homePath+LINUX_FS_SEPARATOR);
            if(ftpClient.rename(from, to)){
                return FtpReply.COMMAND_OK;
            }else{
                return new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
            }
        }else{
            logger.debug("该目录中没有该文件");
            return new FtpReply(FtpReplyCode.FILE_NOT_EXIST, FtpReplyCode.FILE_NOT_EXIST_MSG);
        }
    }
    
    public static FtpReply moveFile(FtpRequest ftpRequest,String from, String to){
        FTPClient ftpClient = null;
        try
        {
            FtpReply ftpReply = checkCommonFtpRequest(ftpRequest);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
            ftpClient = getFtpClientInstance(ftpRequest);
            return moveFile(ftpClient,from,to);
        }
        catch (ConnectFailException e)
        {
            logger.error(e,e);
            return new FtpReply(e.getCode(), e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e,e);
            return FtpReply.COMMAND_FAIL;
        }finally{
            disconnect(ftpClient);
        }
    }
    
    private static FTPClient getFtpClientInstance(FtpRequest ftpRequest)
    {
        FTPClient ftpClient;
        try
        {
            ftpClient = new FTPClient();
            //设置字符集
            ftpClient.setControlEncoding(ftpRequest.getEncoding());
            //把返回信息打印到控制台system.out上
            if(ftpRequest.isPrintLog()){
                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            }
            //设置连接超时时长
            ftpClient.setConnectTimeout(ftpRequest.getConnecttimeout());
            
            
            if(ftpRequest.getFtpUrl()!=null){
                URI uri = URI.create(ftpRequest.getFtpUrl());
                String userInfo = uri.getUserInfo();
                String[] strs = userInfo.split(":");
                connectAndLogin(ftpClient,uri.getHost(),uri.getPort(),strs[0],strs[1]);
                
                String path = uri.getPath();
                if(null!=path&&!"".equals(path)){
                    if(!path.endsWith(LINUX_FS_SEPARATOR)){
                        path = path+LINUX_FS_SEPARATOR;
                    }
                    makeOrChangeDir(ftpClient, path);
                }
            }else{
                connectAndLogin(ftpClient,ftpRequest.getFtpIp(),ftpRequest.getFtpPort(),ftpRequest.getUser(),ftpRequest.getPwd() );
            }
        }
        catch (SocketException e)
        {
            throw new ConnectFailException(FtpReplyCode.FTP_CONNECT_FAIL,FtpReplyCode.FTP_CONNECT_FAIL_MSG);
        }
        catch (IOException e)
        {
            throw new ConnectFailException(FtpReplyCode.FTP_CONNECT_FAIL,FtpReplyCode.FTP_CONNECT_FAIL_MSG);
        }
        return ftpClient;
    }
    
    private static boolean isEmpty(String str){
        
        return null==str||"".equals(str);
    }
    
    /*private static FtpReply makeOrChangeDir(FTPClient ftpClient,String remoteDir){
        return makeOrChangeDir(ftpClient,remoteDir,true);
    }*/
    
    private static FtpReply makeOrChangeDir(FTPClient ftpClient,String remoteDir){
        if(isEmpty(remoteDir)){
            logger.debug("切换的目录值为空:"+remoteDir);
            return new FtpReply(FtpReplyCode.NULL_POINT, FtpReplyCode.NULL_POINT_MSG);
        }
        FtpReply ftpReply = FtpReply.COMMAND_OK;
        String directory = remoteDir;
        try
        {
            if (!ftpClient.changeWorkingDirectory(new String(directory.getBytes("UTF-8"), "iso-8859-1"))
                    &&!directory.equals(FtpUtils.LINUX_FS_SEPARATOR)) {
                // 如果远程目录不存在，则递归创建远程服务器目录
                int start = 0;
                int end = 0;
                end = directory.indexOf("/", start);
                while (true) {
                    String subDirectory = new String(remoteDir.substring(start, end+1).getBytes("UTF-8"), "iso-8859-1");
                    if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                        if (ftpClient.makeDirectory(subDirectory)) {
                            if(!ftpClient.changeWorkingDirectory(subDirectory)){
                                ftpReply = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
                                break;
                            }
                        } else {
                            logger.debug("创建目录失败,可能该用户权限不够");
                            ftpReply = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
                            break;
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
        catch (IOException e)
        {
            logger.error(e, e);
            ftpReply =  FtpReply.COMMAND_FAIL;
        }
        return ftpReply;
    }

    private static void connectAndLogin(FTPClient ftpClient,String ip,int port,String user,String pwd) throws SocketException, IOException
    {
        ftpClient.connect(ip, port);
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(user,pwd)) {
                // 设置PassiveMode传输
                ftpClient.enterLocalPassiveMode();
                // 设置传输文件类型 
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            }else{
                throw new ConnectFailException(FtpReplyCode.FTP_LOGIN_FAIL,FtpReplyCode.FTP_LOGIN_FAIL_MSG);
            }
        }else{
            throw new ConnectFailException(FtpReplyCode.FTP_CONNECT_FAIL,FtpReplyCode.FTP_CONNECT_FAIL_MSG);
        }
    }
    

    private static FtpReply checkCommonFtpRequest(FtpRequest ftpRequest)
    {
        if(null==ftpRequest){
            logger.debug("ftpRequest is "+ftpRequest);
            return new FtpReply(FtpReplyCode.FTPREQUEST_IS_NULL, FtpReplyCode.FTPREQUEST_IS_NULL_MSG);
        }
        
        if(null!=ftpRequest.getFtpUrl()){
            URI uri = URI.create(ftpRequest.getFtpUrl());
            if (uri.getUserInfo()==null||"".equals(uri.getUserInfo())) {
                logger.debug("ftpRequest'ftpUrl is not access-->"+ftpRequest.getFtpUrl());
                return new FtpReply(FtpReplyCode.FTPURL_NOT_ACCESS, FtpReplyCode.FTPURL_NOT_ACCESS_MSG);
            }
        }else{
            if(null==ftpRequest.getFtpIp()||"".equals(ftpRequest.getFtpIp())){
                logger.debug("ftpRequest'ftpIp is empty-->"+ftpRequest.getFtpIp());
                return new FtpReply(FtpReplyCode.FTPURL_NOT_ACCESS, FtpReplyCode.FTPURL_NOT_ACCESS_MSG);
            }
            if(null==ftpRequest.getUser()||null==ftpRequest.getPwd()){
                logger.debug("ftpRequest'user or pwd is empty");
                return new FtpReply(FtpReplyCode.FTPURL_NOT_ACCESS, FtpReplyCode.FTPURL_NOT_ACCESS_MSG);
            }
        }
        
        return new FtpReply(FtpReplyCode.COMMAND_OK,FtpReplyCode.COMMAND_OK_MSG);
    }
    
    private static void disconnect(FTPClient ftpClient){
        if(null==ftpClient){
            return;
        }
        try
        {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
        catch (IOException e)
        {
            logger.error(e);
        }
    }
    
    private static String formatPath(String path,String separator){
        return path.replace(WIN_FS_SEPARATOR, separator);
    }
    
    private static String formatPath(String path){
        return path.replace(WIN_FS_SEPARATOR, LINUX_FS_SEPARATOR);
    }
    private static String encodeUtf8ToISO(String str)
    {
        String result = null;
        try
        {
            return new String(str.getBytes("utf-8"),"iso-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            return result;
        }
    }
    
    private static FtpReply continueUpdateFile(FTPClient ftpClient,String remoteFile, File localFile, long remoteSize,int bufferSize) throws IOException{
        return continueUpdateFile(ftpClient,remoteFile,localFile,remoteSize,TRANOFFSET_FULL,bufferSize);
    }
    
    
    private static FtpReply continueUpdateFile(FTPClient ftpClient,String remoteFile, File localFile, long startPosition,long offset,int bufferSize) throws IOException{
        /*logger.debug(String.format("params'value:remoteFile=%s;localFile=%s;startPosition=%s;offset=%s", 
                remoteFile,localFile,startPosition,offset));*/
        FtpReply ftpReply = FtpReply.COMMAND_OK;
        if(startPosition>localFile.length()||startPosition<0){
            logger.debug(String.format("remoteStartPosition=%s;localFile'length=%s;they's relation is wrong", 
                    startPosition,localFile.length()));
            return new FtpReply(FtpReplyCode.ILLEGAL_Argument, "[startPosition]"+FtpReplyCode.ILLEGAL_Argument_MSG);
            
        }
        //ftpClient.setBufferSize(bufferSize);   
        String remoteFileName = remoteFile;
        if(remoteFile.contains(LINUX_FS_SEPARATOR)){
            remoteFileName = remoteFile.substring(remoteFile.lastIndexOf("/")+1);
            String directory = remoteFile.substring(0, remoteFile.lastIndexOf("/")+1);
            ftpReply = makeOrChangeDir(ftpClient, directory);
            if(!FtpReply.isPositiveCompletion(ftpReply.getCode())){
                return ftpReply;
            }
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
        if(!ftpClient.completePendingCommand()){
            ftpReply = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString()); 
        }
        return ftpReply;
    }
    
    private static FtpReply storeUpdateFile(FTPClient ftpClient,String remoteFile, File localFile,int bufferSize) throws IOException{
        logger.debug(String.format("params'value:remoteFile=%s;localFile=%s", 
                remoteFile,localFile));
        FtpReply result = FtpReply.COMMAND_OK;
        String remoteFileName = remoteFile;
        if(remoteFile.contains(LINUX_FS_SEPARATOR)){
            remoteFileName = remoteFile.substring(remoteFile.lastIndexOf("/")+1);
            String directory = remoteFile.substring(0, remoteFile.lastIndexOf("/")+1);
            result = makeOrChangeDir(ftpClient, directory);
            if(!FtpReply.isPositiveCompletion(result.getCode())){
                return result;
            }
        }
        InputStream in = null;
        int orgBufferSize = ftpClient.getBufferSize();
        ftpClient.setBufferSize(bufferSize);
        try{
            in = new FileInputStream(localFile);
            if(!ftpClient.storeFile(new String(remoteFileName),in)){
                result = new FtpReply(ftpClient.getReplyCode(), ftpClient.getReplyString());
            }
            
        }finally{
            ftpClient.setBufferSize(orgBufferSize);
            if(null!=in){
                logger.debug("本地文件的输入流已读完，即将关闭");
                in.close();
                logger.debug("本地文件的输入流已关闭");
            }
        }
        
        return result;
    }
}
