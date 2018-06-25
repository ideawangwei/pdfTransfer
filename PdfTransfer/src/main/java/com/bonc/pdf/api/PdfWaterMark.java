package com.bonc.pdf.api;

import com.bonc.pdf.util.ImageWaterMarkConfigure;
import com.bonc.pdf.util.PdfToImageUtil;
import com.bonc.pdf.util.PdfWaterMarkConfigure;
import com.bonc.pdf.util.WaterMarkUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;

/**
 * Created by ldf on 2018/2/27.
 */
public abstract class PdfWaterMark {

    /**
     * 注意： 只有在stamper 关闭之后 才能拿到字节流数据
     */




    /**
     * pdf添加水印
     * @param inputFile
     * @param waterMarkName
     * @param configure
     * @param position 水印位置
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static byte[] waterMark(String inputFile, String waterMarkName,
                                   PdfWaterMarkConfigure configure, int position
                                   ) throws IOException, DocumentException {
        byte[] bytes = new byte[1024];
        PdfStamper stamper = null;
        ByteArrayOutputStream bos = null;
        try {
            PdfReader reader = new PdfReader(inputFile);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            int total = reader.getNumberOfPages() + 1;
            WaterMarkUtil.addPdfWaterMark(total,stamper,waterMarkName,configure,position);
        } finally {
            if(stamper != null) stamper.close();
            if(bos != null) {
                bytes = bos.toByteArray();
                bos.close();
            }
        }
        return bytes;
    }

    /**
     * 为pdf文件添加水印
     * @param inputFile 输入文件字节流
     * @param waterMarkName 水印内容
     * @param configure 基本配置 为NULL时使用默认设置
     * @return 添加完水印的pdf文件的字节流
     */
    public static byte[] waterMark(byte[] inputFile,String waterMarkName,
                                   PdfWaterMarkConfigure configure,
                                   int position) throws IOException, DocumentException {
        byte[] bytes = new byte[1024];
        PdfStamper stamper = null;
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        try {
            reader = new PdfReader(inputFile);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            int total = reader.getNumberOfPages() + 1;
            WaterMarkUtil.addPdfWaterMark(total,stamper,waterMarkName,configure,position);
        } finally {
            if(stamper != null) stamper.close();
            if(bos != null) {
                bytes = bos.toByteArray();
                bos.close();
            }
        }
        return bytes;
    }

    /**
     * 为pdf文件添加水印 （上层水印）
     * @param inputFile 输入文件路径
     * @param outputFile 生成带水印的pdf文件
     * @param waterMarkName 水印名称
     * @param configure 配置 为null时使用默认配置
     * @return
     */
    public static void waterMark(String inputFile,
                                    String outputFile, String waterMarkName,
                                 PdfWaterMarkConfigure configure,
                                 int position) throws IOException, DocumentException {
        PdfStamper stamper = null;
        try{
            PdfReader reader = new PdfReader(inputFile);
            stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
            int total = reader.getNumberOfPages() + 1;
            WaterMarkUtil.addPdfWaterMark(total,stamper,waterMarkName,configure,position);
        }finally {
            if(stamper != null) stamper.close();
        }
    }

    /**
     * pdf指定页转成带水印的图片
     * @param file
     * @param waterMarkName
     * @param pageNum
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] waterMarkImg(File file,String waterMarkName,
                                      int pageNum, ImageWaterMarkConfigure configure) throws IOException {
        byte[] result;
        long time1 = System.currentTimeMillis();
        byte[] imgByte = PdfToImageUtil.pdfToImgByte(file, pageNum, configure);
        long time2 = System.currentTimeMillis();
        System.out.println(time2-time1);
        result = ImageWaterMark.imageWaterMark(imgByte, waterMarkName, configure);
        long time3 = System.currentTimeMillis();
        System.out.println(time3-time2);
        return result;
    }

    /**
     * pdf指定页转成带水印的图片
     * @param pdfByte
     * @param waterMarkName
     * @param pageNum
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] waterMarkImg(byte[] pdfByte, String waterMarkName,
                                      int pageNum, ImageWaterMarkConfigure configure) throws IOException {
        byte[] result;
        //pdf转图片
        byte[] imgByte = PdfToImageUtil.pdfToImgByte(pdfByte,pageNum,configure);
        //添加水印
        result = ImageWaterMark.imageWaterMark(imgByte,waterMarkName,configure);
        return result;
    }

    /**
     * 传入pdf文件 生成带水印的图片(零拷贝)
     * @param inputFile pdf文件路径
     * @param outputFile 生成的图片存储路径（不用加文件后缀）
     * @param waterMarkName 水印名称
     * @param configure 配置信息 为null时使用默认配置
     * @return
     * @throws Exception
     */
    public static void waterMarkImgNoMemory(String inputFile, String outputFile,
                                    String waterMarkName, int pageNum,
                                    ImageWaterMarkConfigure configure) throws Exception {
        OutputStream output = null;
        try{
            if(configure == null) configure = new ImageWaterMarkConfigure();
            //pdf转图片
            File file = new File(inputFile);
            long transferBegin = System.currentTimeMillis();
            byte[] imgByte = PdfToImageUtil.pdfToImgByteNoMemory(file,pageNum,configure);
            long transferDiff = System.currentTimeMillis() - transferBegin;
            System.out.println("pdf转图片花费时间:" + transferDiff);
            //为图片添加水印
            long markBegin = System.currentTimeMillis();
            byte[] waterMarkImg = ImageWaterMark.imageWaterMark(imgByte,waterMarkName,configure);
            long markDiff = System.currentTimeMillis() - markBegin;
            System.out.println("图片添加水印花费时间："+markDiff);
            //输出文件
            String type = configure.getType();
            output = new FileOutputStream(outputFile + pageNum + "." + type);
            output.write(waterMarkImg);
        }finally {
            if(output != null) output.close();
        }
    }

    /**
     * 将pdf的指定页转化为带水印的图片 （零拷贝）
     * @param file pdf文件
     * @param waterMarkName 水印
     * @param pageNum 页码
     * @param configure 配置信息 为null时使用默认配置
     * @return 带水印图片
     * @throws IOException
     */
    public static byte[] waterMarkImgNoMemory(File file,String waterMarkName,
                                              int pageNum,ImageWaterMarkConfigure configure) throws IOException {
        byte[] result;
        //pdf转图片
        byte[] imgByte = PdfToImageUtil.pdfToImgByteNoMemory(file,pageNum,configure);
        //图片添加水印
        result = ImageWaterMark.imageWaterMark(imgByte,waterMarkName,configure);
        return result;
    }

    /**
     *
     * @param filePath 文件路径
     * @param waterMarkName
     * @param pageNum
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] waterMarkImgNoMemory(String filePath, String waterMarkName,
                                              int pageNum, ImageWaterMarkConfigure configure) throws IOException {
        byte[] result;
        File file = new File(filePath);
        //pdf转图片
        byte[] imgByte = PdfToImageUtil.pdfToImgByteNoMemory(file,pageNum,configure);
        //图片添加水印
        result = ImageWaterMark.imageWaterMark(imgByte,waterMarkName,configure);
        return result;
    }

//    private static void transfer(ByteArrayOutputStream bos, PdfStamper stamper,
//                                 PdfReader reader, String waterMarkName,
//                                 PdfWaterMarkConfigure configure) throws IOException, DocumentException {
//        stamper = new PdfStamper(reader, bos);
//        int total = reader.getNumberOfPages() + 1;
//        WaterMarkUtil.addPdfWaterMark(total,stamper,waterMarkName,configure);
//    }




}
