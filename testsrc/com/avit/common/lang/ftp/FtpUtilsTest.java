package com.avit.common.lang.ftp;

import junit.framework.TestCase;

public class FtpUtilsTest extends TestCase
{
    private static final String fullFtpURL = "ftp://hadoop:h111111@192.168.2.181:21/home/hadoop";
    private static final String ftpURL = "192.168.2.181";
    private static final int ftpport = 21;
    private static final String username = "hadoop";
    private static final String pwd = "h111111";
    
    
    private static final String LOCALFILE_PATH_INFO = "F:\\logs\\info";
    private static final String LOCALFILE_PATH = "F:\\logs\\debug";
    private static final String LOCALFILE_TXT = "F:\\logs\\debug\\txttest.log";
    private static final String LOCALFILE_IMAGE = "F:\\logs\\debug\\imagetest.jpg";
    private static final String LOCALFILE_AUDIO = "F:\\logs\\debug\\audiotest.mp3";
    private static final String LOCALFILE_VIDEO = "F:\\logs\\debug\\videotest.mp4";
    private static final String LOCALFILE_ZIP = "F:\\logs\\debug\\ziptest.zip";
    
    public void testDeleteFile(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        FtpReply result = FtpUtils.deleteFile(ftpRequest, "testa/11122.jpg");
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    public void testUpdateFile(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        ftpRequest.setBufferSize(1024*128);
        FtpReply result = FtpUtils.updateFile(ftpRequest, "testa/111.zip",LOCALFILE_ZIP);
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    public void testUpdateFile_restart(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        ftpRequest.setOperaModel(FtpUtils.RESTART_MODEL);
        ftpRequest.setTransOffset(4*FtpUtils.Unit_M);
        //ftpRequest.setBufferSize(1024);
        FtpReply result = FtpUtils.updateFile(ftpRequest, "testa/111223.jpg",LOCALFILE_IMAGE);
        System.out.println(result);
        assertTrue(FtpReply.isPositiveCompletion(result.getCode()));
    }
    
    public void testUpdateFile_fullURl(){
        FtpRequest ftpRequest = new FtpRequest(fullFtpURL);
        ftpRequest.setPrintLog(true);
        //ftpRequest.setBufferSize(1024*128);
        FtpReply result = FtpUtils.updateFile(ftpRequest, "testa/111.zip",LOCALFILE_ZIP);
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    public void testUpdateDir(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        //ftpRequest.setBufferSize(1024*128);
        FtpReply result = FtpUtils.uploadDirectory(ftpRequest, "testa/",LOCALFILE_PATH+"\\12");
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    public void testDownloadFile(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        //ftpRequest.setBufferSize(1024);
        FtpReply result = FtpUtils.downloadFile(ftpRequest, "testa/111.zip",LOCALFILE_PATH+"\\");
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    public void testDownloadFile_restart(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        ftpRequest.setOperaModel(FtpUtils.RESTART_MODEL);
        ftpRequest.setTransOffset(10*FtpUtils.Unit_M);
        //ftpRequest.setBufferSize(1024);
        FtpReply result = FtpUtils.downloadFile(ftpRequest, "testa/111223.jpg",LOCALFILE_PATH+"\\");
        System.out.println(result);
        assertTrue(FtpReply.isPositiveCompletion(result.getCode()));
    }
    
    
    public void testDownloadDir(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        FtpReply result = FtpUtils.downloadDir(ftpRequest, "testa/12",LOCALFILE_PATH+"\\test");
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    
    public void testDeleteDirectory(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        FtpReply result = FtpUtils.deleteDirectory(ftpRequest, "testa/testdebug");
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
    
    public void testMoveFile(){
        FtpRequest ftpRequest = new FtpRequest(ftpURL,username, pwd);
        ftpRequest.setPrintLog(true);
        FtpReply result = FtpUtils.moveFile(ftpRequest, "testa/txttest1.log","testa/111");
        System.out.println(result);
        assertEquals(200, result.getCode());
    }
}
