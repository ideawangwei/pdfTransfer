package com.bonc.pdf.util.openoffice;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.bonc.pdf.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.concurrent.*;

/**
 * Created by ldf on 2018/3/2.
 */
public class OpenOfficeUtil {

    /**
     * 建立openoffice连接
     * @param host
     * @param port
     * @return
     * @throws ConnectException
     */
    public static OpenOfficeConnection OpenConnection(String host, int port) throws ConnectException {
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
        connection.connect();
        return connection;
    }

    /**
     * 关闭openoffice连接
     * @param connection
     */
    public static void CloseConnection(OpenOfficeConnection connection){
        connection.disconnect();
    }

    public static DocumentFormat DocumentFormatInit(String type){
        DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
        return formatReg.getFormatByFileExtension(type);
    }

    public static void convertTimeOut(final String fileType, final OpenOfficeConnection connection,
                                      final InputStream fis, final OutputStream fos, long time){
        ExecutorService exec = Executors.newFixedThreadPool(1);
        Callable<Boolean> call = new Callable<Boolean>() {
            public Boolean call() throws Exception {
                convert(fileType,connection,fis,fos);
                return true;
            }
        };
        try{
            Future<Boolean> future = exec.submit(call);
            future.get(time, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public static void convert(String fileType, OpenOfficeConnection connection,
                               InputStream fis, OutputStream fos) throws FileNotFoundException {
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        DocumentFormat fromFormat;
        DocumentFormat pdfFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_PDF.getType());
        if (FileUtil.isDoc(fileType)) {
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_DOC.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        }
        else if(FileUtil.isDocx(fileType)){
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_DOCX.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        }
        else if (FileUtil.isXls(fileType)) {
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_XLS.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        }else if (FileUtil.isXlsx(fileType)) {
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_XLSX.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        } else if (FileUtil.isPpt(fileType)) {
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_PPT.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        } else if (FileUtil.isPptx(fileType)) {
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_PPTX.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        } else if (FileUtil.isText(fileType)) {
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_TXT.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        } else if(FileUtil.isOdt(fileType)){
            fromFormat = OpenOfficeUtil.DocumentFormatInit(FileTypeEnum.TYPE_ODT.getType());
            converter.convert(fis, fromFormat, fos, pdfFormat);
        }
    }

}
