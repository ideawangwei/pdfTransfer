package com.bonc.pdf;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.bonc.pdf.api.ImageWaterMark;
import com.bonc.pdf.api.PdfWaterMark;
import com.bonc.pdf.enums.PdfWaterEnum;
import com.bonc.pdf.util.*;
import com.bonc.pdf.util.openoffice.OpenOfficeUtil;
import com.bonc.pdf.util.pdfbox.PdfBoxUtil;
import com.itextpdf.text.DocumentException;

import javax.security.auth.login.Configuration;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;

/**
 * Created by ldf on 2018/2/27.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();
        pdfWater();
        long diff = System.currentTimeMillis() - begin ;
        System.out.println("总时间：" + diff);
        System.exit(0);
    }

    public static void pdfWater() throws IOException, DocumentException {
        String path = "D:/test/东方国信XCloud大数据产品介绍 v2.2.pdf";
        String outPath = "D:/test/Water/东方国信XCloud大数据产品介绍 v2.2.pdf";
        FileOutputStream outputStream = new FileOutputStream(outPath);
        PdfWaterMarkConfigure configure = new PdfWaterMarkConfigure();
//        configure.setDensity(3).setLineWidth(2f);
        byte[] bytes = PdfWaterMark.waterMark(path,"北京东方国信科技股份有限公司",configure, PdfWaterEnum.WATER_MARK_OVER);
        outputStream.write(bytes);
        int i = 0;
    }


    public static void fileToPdf() throws IOException {
        String inFile = "D:/test/中国移动跨域大数据平台-概要汇报-v2.9.4.pptx";
        String outFile = "D:/test/中国移动跨域大数据平台-概要汇报-v2.9.4.pdf";
        String host1 = "172.16.5.12";
        String host2 = "172.16.38.173";
        String host3 = "172.16.5.24";
        String host4 = "172.16.5.19";
        FileToPdfUtil util = FileToPdfUtil.getDoc2HtmlUtilInstance();
        byte[] bytes = util.fileToPdfLimitTime(inFile, host2, 8100,500000);
        try( FileOutputStream output = new FileOutputStream(outFile);
            ){
        output.write(bytes);}
    }

    public static void convertTest() throws IOException {
        String inFile = "D:\\test\\1.pdf";
        String outFile = "D:\\test\\1.png";
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        ImageWaterMarkConfigure configure = new ImageWaterMarkConfigure();
        Object[] font = {"STZhongsong", Font.PLAIN, 25};
        configure.setFont(font).setEnlarge(1);
        byte[] bytes = PdfWaterMark.waterMarkImg(new File(inFile),"李德富", 2,configure);
        fileOutputStream.write(bytes);
    }

    public static void test(){
        String path = "D:\\test\\1.docx";
        System.out.println(new File(path).length());
    }

//    public void getFileList(String strPath){
//        File f=new File(strPath);
//        try {
//            if(f.isDirectory()){
//                File[] fs=f.listFiles();
//                for(int i=0;i<fs.length;i++){
//                    String fsPath=fs[i].getAbsolutePath();
//                    System.out.println(fsPath);
//                    getFileList(fsPath);
//                }
//            }else if(f.isFile()){
//                String fname=f.getAbsolutePath();
//                System.out.println(fname);
//            }else{
//                System.out.println("路径不正确!");
//                   　}
//        }catch (IOException e) {
//                　　System.out.println("遍历异常");
//        }
//　　　　　　}

}
