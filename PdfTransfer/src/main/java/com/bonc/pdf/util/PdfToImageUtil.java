package com.bonc.pdf.util;

import com.bonc.pdf.exception.ServiceException;
import com.bonc.pdf.exception.SysErrorEnums;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by ldf on 2018/2/28.
 */
public class PdfToImageUtil {


    public static final float DEFAULT_DPI=105;

    /**
     * 指定pdf页转为图片，类型默认为png
     * @param fileByte pdf文件字节流
     * @param pageNum 页码
     * @param configure  基本配置（字体、颜色、类型等），为null时使用默认配置
     * @return
     * @throws IOException
     */
    public static byte[] pdfToImgByte(byte[] fileByte, int pageNum,
                                      ImageWaterMarkConfigure configure) throws IOException {
        byte[] imgByte;
        PDDocument pdfDocument = null;
        InputStream input;
        try{
            input = new ByteArrayInputStream(fileByte);
            pdfDocument = PDDocument.load(input);
            PDFRenderer reader = new PDFRenderer(pdfDocument);
            imgByte  = createImgByte(reader, pageNum, configure);
        }finally {
            if(pdfDocument != null) pdfDocument.close();
        }
        return imgByte;
    }

    /**
     * 指定pdf页转为图片，类型默认为png
     * @param file pdf文件
     * @param pageNum 页码
     * @param configure 基本配置（字体、颜色、类型等），为null时使用默认配置
     * @return
     * @throws IOException
     */
    public static byte[] pdfToImgByte(File file, int pageNum,
                                      ImageWaterMarkConfigure configure) throws IOException {
        byte[] imgByte;
        PDDocument pdfDocument = null;
        try{
            pdfDocument = PDDocument.load(file);
            PDFRenderer reader = new PDFRenderer(pdfDocument);
            imgByte  = createImgByte(reader, pageNum, configure);
        }finally {
            if(pdfDocument != null) pdfDocument.close();
        }
        return imgByte;
    }
    /**
     * pdf转图片（零拷贝）
     * @param filePath
     * @param pageNum
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] pdfToImgByteNoMemory(String filePath, int pageNum,
                                              ImageWaterMarkConfigure configure) throws IOException{
        byte[] imgByte;
        File file = new File(filePath);
        imgByte = pdfToImgByteNoMemory(file, pageNum, configure);
        return imgByte;
    }

    /**
     * pdf转图片（零拷贝）
     * @param file pdf文件
     * @param pageNum 页码
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] pdfToImgByteNoMemory(File file,int pageNum,
                                              ImageWaterMarkConfigure configure) throws IOException {
        byte[] imgByte = new byte[1024];
        RandomAccessFile raf = null;
        FileChannel channel = null;
        MappedByteBuffer buf = null;
        try{
            if (configure == null) configure = new ImageWaterMarkConfigure();
            raf = new RandomAccessFile(file, "r");
            channel = raf.getChannel();
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);
            int totalPageNum = pdffile.getNumPages();
            checkPage(pageNum,totalPageNum);
            imgByte = createImgByteNoMemory(pdffile, pageNum, configure);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(channel != null){
                channel.close();
            }
            if(raf != null){
                raf.close();
            }
            unmap(buf);/**如果要在转图片之后删除pdf，就必须要这个关闭流和清空缓冲的方法*/
        }
        return imgByte;
    }


    /**
     *  pdf转图片(零拷贝)
     * @param file pdf文件
     * @param outPath 图片输出地址（不需要写类型）
     * @param pageNum 页码
     * @param configure
     */
    public static void pdfToImgNoMemory(File file,String outPath,
                                        int pageNum,
                                        ImageWaterMarkConfigure configure) throws IOException {
        RandomAccessFile raf = null;
        FileChannel channel = null;
        MappedByteBuffer buf = null;
        try{
            if (configure == null) configure = new ImageWaterMarkConfigure();
            raf = new RandomAccessFile(file, "r");
            channel = raf.getChannel();
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);
            int totalPageNum = pdffile.getNumPages();
            checkPage(pageNum,totalPageNum);
            createImgNoMemory(pdffile,pageNum,outPath,configure);
        }finally {
            channel.close();
            raf.close();
            unmap(buf);/**如果要在转图片之后删除pdf，就必须要这个关闭流和清空缓冲的方法*/
        }
    }

    /**
     * pdf转图片（零拷贝)
     * @param inputPath pdf文件地址
     * @param outPath 生成图片存储地址（不需要写类型，类型在configure中配置）
     * @param pageNum
     * @param configure
     */
    public static void pdfToImgNoMemory(String inputPath,String outPath,
                                        int pageNum,
                                        ImageWaterMarkConfigure configure) throws IOException {
        File file = new File(inputPath);
        pdfToImgNoMemory(file,outPath,pageNum,configure);
    }

    /**
     * 生成图片字节流
     * @param renderer
     * @param i 页码
     * @param configure
     * @return
     * @throws IOException
     */
    private static byte[] createImgByte(PDFRenderer renderer,int i,ImageWaterMarkConfigure configure) throws IOException {
        byte[] imgByte;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try{
            if(configure == null) configure = new ImageWaterMarkConfigure();
            int shiftHeight = 0;//偏移宽度
            String type = configure.getType();
            int enlarge = configure.getEnlarge();
            BufferedImage image = renderer.renderImageWithDPI(i-1, DEFAULT_DPI, ImageType.RGB);
            int imageHeight = image.getHeight() * enlarge;
            int width = image.getWidth() * enlarge;
            if(enlarge > 1) image = resize(image,width,imageHeight);
            int[] singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);
            image.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width); // 写入流中
            ImageIO.write(image, type, output);// 写图片
            imgByte = output.toByteArray();
        }finally {
            output.close();
        }
        return imgByte;
    }

    /**
     * 生成指定页的图片--输出文件
     * @param pdffile
     * @param i 页码
     * @param outPath
     * @param configure
     * @throws IOException
     */
    private static void createImgNoMemory(PDFFile pdffile,int i,String outPath,ImageWaterMarkConfigure configure) throws IOException {
        FileOutputStream out = null;
        try{
            int n = configure.getEnlarge();//放大参数
            float quality = configure.getQuality();//质量参数
            String type = configure.getType();
            PDFPage page = pdffile.getPage(i);
            Rectangle rect = new Rectangle(0, 0, ((int) page.getBBox().getWidth()), ((int) page.getBBox().getHeight()));
            Image img = page.getImage(rect.width * n, rect.height * n, rect,null,true,true);
            BufferedImage tag = new BufferedImage(rect.width * n, rect.height * n, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img, 0, 0, rect.width * n, rect.height * n, null);
            out = new FileOutputStream(outPath + i + "." + type); /** 输出到文件流*/
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param2 = encoder.getDefaultJPEGEncodeParam(tag);
            param2.setQuality(quality, true);
            encoder.setJPEGEncodeParam(param2);
            encoder.encode(tag);/** JPEG编码  */
        }finally {
            out.close();
        }
    }

    /**
     * 生成图片字节流
     * @param pdffile
     * @param i
     * @param configure
     * @return
     * @throws IOException
     */
    private static byte[] createImgByteNoMemory(PDFFile pdffile,int i,ImageWaterMarkConfigure configure) throws IOException {
        byte[] result;
        ByteArrayOutputStream out = null;
        try{
            int n = configure.getEnlarge();//放大参数
            float quality = configure.getQuality();//质量参数
            PDFPage page = pdffile.getPage(i);
            Rectangle rect = new Rectangle(0, 0, ((int) page.getBBox().getWidth()), ((int) page.getBBox().getHeight()));
            long begin = System.currentTimeMillis();
            Image img = page.getImage(rect.width * n, rect.height * n, rect,  /**放大pdf到n倍，创建图片。*/null, /**null for the ImageObserver  */true, /** fill background with white  */true /** block until drawing is done  */);
            long diff = System.currentTimeMillis() - begin;
            BufferedImage tag = new BufferedImage(rect.width * n, rect.height * n, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img, 0, 0, rect.width * n, rect.height * n, null);
            out = new ByteArrayOutputStream(); /** 输出到文件流*/
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param2 = encoder.getDefaultJPEGEncodeParam(tag);
            param2.setQuality(quality, true);
            encoder.setJPEGEncodeParam(param2);
            encoder.encode(tag);/** JPEG编码  */
            result = out.toByteArray();
        }finally {
            out.close();
        }
        return result;
    }

    /**
     * 检验要转化的页面是否合法
     * @param pageNum
     * @param pageTotalPageNum
     * @return
     */
    private static void checkPage(int pageNum, int pageTotalPageNum){
        if(pageNum > pageTotalPageNum || pageNum < 1){
            throw new ServiceException(SysErrorEnums.CHECK_PAGE_NUM_WRONGFUL);
        }
    }
    private static void unmap(final Object buffer) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static BufferedImage resize(BufferedImage source, int targetW,  int targetH) {
        int type=source.getType();
        BufferedImage target=null;
        double sx=(double)targetW/source.getWidth();
        double sy=(double)targetH/source.getHeight();
        if(sx>sy){
            sx=sy;
            targetW=(int)(sx*source.getWidth());
        }else{
            sy=sx;
            targetH=(int)(sy*source.getHeight());
        }
        if(type==BufferedImage.TYPE_CUSTOM){
            ColorModel cm=source.getColorModel();
            WritableRaster raster=cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied=cm.isAlphaPremultiplied();
            target=new BufferedImage(cm,raster,alphaPremultiplied,null);
        }else{
            target=new BufferedImage(targetW, targetH,type);
        }
        Graphics2D g=target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }
}