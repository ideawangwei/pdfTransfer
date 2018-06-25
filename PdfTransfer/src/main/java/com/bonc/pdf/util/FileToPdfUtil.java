package com.bonc.pdf.util;

/**
 * Created by mx on 2017/11/9.
 */


import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.bonc.pdf.util.openoffice.OpenOfficeUtil;

import java.io.*;
import java.net.ConnectException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 利用jodconverter(基于OpenOffice服务)将文件(*.doc、*.docx、*.xls、*.ppt)pdf格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 */
public class FileToPdfUtil {

    private static FileToPdfUtil doc2PdfUtil;
    /**
     * 获取Doc2HtmlUtil实例
     */
    public static synchronized FileToPdfUtil getDoc2HtmlUtilInstance() {
        if (doc2PdfUtil == null) {
            doc2PdfUtil = new FileToPdfUtil();
        }
        return doc2PdfUtil;
    }

    /**
     *
     * @param filePath
     * @param host
     * @param port
     * @return
     * @throws IOException
     */
    public byte[] fileToPdf(String filePath, String host, int port) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = fileToPdf(file, host, port);
        return fileBytes;
    }

    /**
     * 其他文件 转 pdf
     * @param file
     * @param host
     * @param port
     * @return
     * @throws IOException
     */
    public byte[] fileToPdf(File file, String host, int port) throws IOException {
        byte[] outByte;
        FileInputStream fis = null;
        ByteArrayOutputStream fos = null;
        OpenOfficeConnection connection = null;
        try {
            fis = new FileInputStream(file);
            fos = new ByteArrayOutputStream();
            String fileName = file.getName();
            String fileType = FileUtil.getTypeByName(fileName);
            connection = OpenOfficeUtil.OpenConnection(host,port);
            OpenOfficeUtil.convert(fileType, connection, fis, fos);
            outByte = fos.toByteArray();
        } finally {
            if (connection != null) OpenOfficeUtil.CloseConnection(connection);
            if(fis != null) fis.close();
            if(fos != null) fos.close();
        }
        return outByte;
    }

    /**
     * 其他文件转pdf
     * @param input
     * @param fileName
     * @param host
     * @param port
     * @return
     * @throws Exception
     */
    public byte[] fileToPdf(byte[] input, String fileName, String host, int port) throws Exception {
        byte[] outByte;
        ByteArrayInputStream fis = null;
        ByteArrayOutputStream fos = null;
        OpenOfficeConnection connection = null;
        ExecutorService exec = Executors.newFixedThreadPool(1);
        try {
            fis = new ByteArrayInputStream(input);
            fos = new ByteArrayOutputStream();
            String fileType = FileUtil.getTypeByName(fileName);
            connection = OpenOfficeUtil.OpenConnection(host,port);
            OpenOfficeUtil.convert(fileType, connection, fis, fos);
            outByte = fos.toByteArray();
        } finally {
            if (connection != null) OpenOfficeUtil.CloseConnection(connection);;
            if(fis != null) fis.close();
            if(fos != null) fos.close();
        }
        return outByte;
    }

    /**
     * 转pdf 超时限制
     * @param file
     * @param host
     * @param port
     * @param time
     * @return
     * @throws IOException
     */
    public byte[] fileToPdfTimeLimit(File file, String host, int port, long time) throws IOException {
        byte[] outBytes;
        byte[] outByte;
        FileInputStream fis = null;
        ByteArrayOutputStream fos = null;
        OpenOfficeConnection connection = null;
        try {
            fis = new FileInputStream(file);
            fos = new ByteArrayOutputStream();
            String fileName = file.getName();
            String fileType = FileUtil.getTypeByName(fileName);
            connection = OpenOfficeUtil.OpenConnection(host,port);
            OpenOfficeUtil.convertTimeOut(fileType, connection, fis, fos, time);
            outByte = fos.toByteArray();
        } finally {
            if (connection != null)
                OpenOfficeUtil.CloseConnection(connection);
            if(fis != null) fis.close();
            if(fos != null) fos.close();
        }
        return outByte;
    }

    /**
     * 转pdf限制超时
     * @param filePath
     * @param host
     * @param port
     * @param time
     * @return
     * @throws IOException
     */
    public byte[] fileToPdfLimitTime(String filePath, String host, int port, long time) throws IOException{
        File file = new File(filePath);
        byte[] fileBytes = fileToPdfTimeLimit(file, host, port, time);
        return fileBytes;
    }

}
