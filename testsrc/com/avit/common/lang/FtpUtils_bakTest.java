package com.avit.common.lang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import junit.framework.TestCase;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpUtils_bakTest extends TestCase
{
    private static final String fullFtpURL = "ftp://hadoop:h111111@192.168.2.181:21/root/";
    private static final String ftpURL = "192.168.2.181";
    private static final int ftpport = 21;
    private static final String username = "hadoop";
    private static final String pwd = "h111111";
    
    
    private static final String LOCALFILE_PATH_INFO = "F:\\logs\\info";
    private static final String LOCALFILE_PATH = "F:\\logs\\debug";
    private static final String LOCALFILE_TXT = "F:\\logs\\debug\\txttest111.log";
    private static final String LOCALFILE_IMAGE = "F:\\logs\\debug\\imagetest.jpg";
    private static final String LOCALFILE_AUDIO = "F:\\logs\\debug\\audiotest.mp3";
    private static final String LOCALFILE_VIDEO = "F:\\logs\\debug\\videotest.mp4";
    private static final String LOCALFILE_ZIP = "F:\\logs\\debug\\ziptest.zip";
    
    public void testGetFtpClientInstance_param4_normal() throws SocketException, IOException{
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
        assertTrue("连接失败",ftpClient.isConnected());
        FtpUtils_bak.disconnect(ftpClient);
    }
    
    public void testGetFtpClientInstance_param4_ipIllegal(){
        FTPClient ftpClient = null;
        //ip不合法
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance("192.168.2.1812", 21, username, pwd);
            fail("异常为抛出");
        }catch(Exception e){
            e.printStackTrace();
            assertTrue(e instanceof UnknownHostException);
        }finally{
            if(null!=ftpClient&&ftpClient.isConnected()){
                try
                {
                    FtpUtils_bak.disconnect(ftpClient);
                }
                catch (IOException e)
                {
                }
            }
        }
    }
    
    public void testGetFtpClientInstance_param4_portIllegal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance("192.168.2.181", -21, username, pwd);
            fail("异常为抛出");
        }catch(Exception e){
            e.printStackTrace();
            assertTrue(e instanceof IllegalArgumentException);
        }finally{
            if(null!=ftpClient&&ftpClient.isConnected()){
                try
                {
                    FtpUtils_bak.disconnect(ftpClient);
                }
                catch (IOException e)
                {
                }
            }
        }
    }
    
    public void testGetFtpClientInstance_param4_loginOrPwdError(){
        FTPClient ftpClient = null;
        
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance("192.168.2.181", 21, username+"123", pwd);
            fail("异常为抛出");
        }catch(Exception e){
            e.printStackTrace();
            assertTrue(e instanceof SecurityException);
        }finally{
            if(null!=ftpClient&&ftpClient.isConnected()){
                try
                {
                    FtpUtils_bak.disconnect(ftpClient);
                }
                catch (IOException e)
                {
                }
            }
        }
    }
    
    public void testGetFtpClientInstance_param1_normal() throws SocketException, IOException{
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = FtpUtils_bak.getFtpClientInstance(fullFtpURL);
        assertTrue("连接失败",ftpClient.isConnected());
        //ftpClient.printWorkingDirectory();
        FtpUtils_bak.disconnect(ftpClient);
    }
    
    
    public void testGetFtpClientInstance_connectTimeOut() {
        String ftpURL = "192.168.12.181";//此ip为不存在的ip，用来测试连接超时
        int ftpport = 21;
        String username = "hadoop";
        String pwd = "h111111";
        try{
            FTPClient ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            fail("连接超时");
            FtpUtils_bak.disconnect(ftpClient);
        }catch (Exception e) {
            e.printStackTrace();
            assertTrue(e instanceof SocketTimeoutException);
        }
    }
    
    public void testUploadFileDefault_paramUnvalid(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            FtpUtils_bak.uploadFileDefault(ftpClient, "", null);
            fail("未抛出预期异常");
        }catch (Exception e) {
            e.printStackTrace();
            assertTrue(e instanceof NullPointerException);
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFileDefault_txt_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFileDefault(ftpClient, "testa/txttest.log", LOCALFILE_TXT);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    
    public void testUploadFileDefault_image_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFileDefault(ftpClient, "testa/", LOCALFILE_IMAGE);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFileDefault_audio_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFileDefault(ftpClient, "testa/", LOCALFILE_AUDIO);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFileDefault_video_normal(){
       // String ftpURL = "192.168.2.181";
       // int ftpport = 21;
        //String username = "hadoop";
       // String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFileDefault(ftpClient, "testa/", LOCALFILE_VIDEO);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFileDefault_zip_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFileDefault(ftpClient, "testa/", LOCALFILE_ZIP);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFile_txt_restart_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFile(ftpClient, "testa/txttest.log", LOCALFILE_TXT,
                    FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFile_image_restart_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //Long start = System.currentTimeMillis();
            int result = FtpUtils_bak.uploadFile(ftpClient, "testa/abc.jpg", LOCALFILE_IMAGE,
                    FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
            //System.out.println("耗时："+(System.currentTimeMillis()-start));
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFile_audio_restart_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFile(ftpClient, "testa/", LOCALFILE_AUDIO,
                    FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFile_vedio_restart_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFile(ftpClient, "testa/", LOCALFILE_VIDEO,
                    FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadFile_zip_restart_normal(){
        //String ftpURL = "192.168.2.181";
        //int ftpport = 21;
        //String username = "hadoop";
        //String pwd = "h111111";
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            int result = FtpUtils_bak.uploadFile(ftpClient, "testa/111.zip", LOCALFILE_ZIP,
                    FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testUploadDirectory_normal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            boolean result = FtpUtils_bak.uploadDirectory(ftpClient, "testa/testinfo", LOCALFILE_PATH_INFO);
            assertTrue(result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    } 
    
    public void testDownloadFile_reset_normal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            int result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/imagetest.jpg",
                    LOCALFILE_PATH_INFO+"\\",FtpUtils_bak.RESET_MODEL,FtpUtils_bak.TRANOFFSET_FULL,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_OK, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    } 
    
    
    public void testDownloadFile_paramOut(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            File file = new File(LOCALFILE_PATH+"\\lyq.jpg");
            if(file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            boolean result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/imagetest.jpg",out);
            assertTrue(result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    } 
    
    public void testDownloadFile_txt_restart(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            int result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/txttest.log",
                    LOCALFILE_PATH_INFO+"\\txttest111.log",FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.Unit_K*50,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_CONTINU, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    } 
    
    public void testDownloadFile_image_restart(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            int result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/imagetest.jpg",
                    LOCALFILE_PATH_INFO+"\\imagetest111.jpg",FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.Unit_M*5,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_CONTINU, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    } 
    
    
    public void testDownloadFile_video_restart(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            int result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/videotest.mp4",
                    LOCALFILE_PATH_INFO+"\\videotest111.mp4",FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.Unit_M*5,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_CONTINU, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testDownloadFile_audio_restart(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            int result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/videotest.mp4",
                    LOCALFILE_PATH_INFO+"\\videotest111.mp4",FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.Unit_M*5,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_CONTINU, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testDownloadFile_zip_restart(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            //(FTPClient ftpClient,String remoteFile, String localFile,int operaModel,long offset,int bufferSize)
            int result = FtpUtils_bak.downloadFile(ftpClient, "testa/testdebug/debug/videotest.mp4",
                    LOCALFILE_PATH_INFO+"\\videotest111.mp4",FtpUtils_bak.RESTART_MODEL,FtpUtils_bak.Unit_M*5,FtpUtils_bak.BUFFERSIZE_S);
            assertEquals(FtpUtils_bak.TRANS_CONTINU, result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testdownloadDir_normal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            boolean result = FtpUtils_bak.downloadDir(ftpClient, "testa/testinfo/info",
                    LOCALFILE_PATH_INFO+"");
            assertTrue( result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testMoveFile_normal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            boolean result = false;
            
            //相对路径，在同一级目录下
            result = FtpUtils_bak.moveFile(ftpClient, "testa/testinfo/info/imagetest.jpg",
                    "testa/testdebug/debug/imagetest222.jpg");
            assertTrue( result);
            
            //相对路径， 在不同一级目录下
            result = FtpUtils_bak.moveFile(ftpClient, "testa/testdebug/debug/imagetest222.jpg",
                    "testa/testinfo/info/imagetest.jpg");
            assertTrue( result);
            
          //绝对路径， 
            result = FtpUtils_bak.moveFile(ftpClient, "/home/hadoop/testa/testinfo/info/imagetest.jpg",
                    "/home/hadoop/testa/testdebug/debug/aa/");
            assertTrue( result);
            
          //绝对路径， 
            result = FtpUtils_bak.moveFile(ftpClient, "/home/hadoop/testa/testdebug/debug/aa/imagetest.jpg",
                    "/home/hadoop/testa/testinfo/info/imagetest.jpg");
            assertTrue( result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void testDeleteFile_normal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            boolean result = FtpUtils_bak.deleteFile(ftpClient, "testa/abc.jpg"
                    );
            assertTrue( result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void testDeleteDirectory_normal(){
        FTPClient ftpClient = null;
        try{
            ftpClient = FtpUtils_bak.getFtpClientInstance(ftpURL, ftpport, username, pwd);
            boolean result = false;
            // 准备远程目录/home/hadoop/testa/testdebug/debug/aa/
            
            result = FtpUtils_bak.deleteDirectory(ftpClient, "/home/hadoop/testa/testdebug/debug/aa/"
                    );
            assertTrue( result);
            
            // 准备远程目录/home/hadoop/aa/
            result = FtpUtils_bak.deleteDirectory(ftpClient, "aa"
                    );
            assertTrue( result);
        }catch (Exception e) {
            fail(e.getMessage());
        }finally{
            try
            {
                if(null!=ftpClient){
                    FtpUtils_bak.disconnect(ftpClient);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
