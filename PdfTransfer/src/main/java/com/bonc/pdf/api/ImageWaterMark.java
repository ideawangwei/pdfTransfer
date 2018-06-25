package com.bonc.pdf.api;

import com.bonc.pdf.util.ImageWaterMarkConfigure;
import com.bonc.pdf.util.WaterMarkUtil;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
/**
 * Created by ldf on 2018/2/26.
 * 为图片添加水印
 */
public class ImageWaterMark{

    /**
     * 图片添加水印
     * @param inPath
     * @param outPath
     * @param waterMarkName
     * @param configure
     * @throws IOException
     */
    public static void imageWaterMark(String inPath,String outPath,String waterMarkName,
                                         ImageWaterMarkConfigure configure) throws IOException {
        InputStream input = null;
        try{
            if(configure == null) configure = new ImageWaterMarkConfigure();
            File file = new File(inPath);
            input = new FileInputStream(file);
            byte[] bytes = new byte[input.available()];
            input.read(bytes);
            BufferedImage bufferedImage = ImageIO.read(file);
            WaterMarkUtil.addImageWatermark(bufferedImage, waterMarkName,configure);
            String imageType = configure.getType();
            File asd = new File(outPath + "."+imageType);
            ImageIO.write(bufferedImage, imageType, asd);
        }finally {
            if(input != null) input.close();
        }
    }

    /**
     * 图片添加水印
     * @param imageByte
     * @param waterMarkName
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] imageWaterMark(byte[] imageByte,String waterMarkName,
                                        ImageWaterMarkConfigure configure) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try {
            out = new ByteArrayOutputStream();
            if(configure == null) configure = new ImageWaterMarkConfigure();
            input = new ByteArrayInputStream(imageByte);
            BufferedImage bufferedImage = ImageIO.read(input);
            WaterMarkUtil.addImageWatermark(bufferedImage, waterMarkName,configure);
            String imageType = configure.getType();
            ImageIO.write(bufferedImage, imageType, out);
            bytes = out.toByteArray();
        }finally {
            if(input != null) input.close();
            if(out != null) out.close();
        }
        return bytes;
    }

    /**
     * 图片添加水印
     * @param file
     * @param waterMarkName
     * @param configure
     * @return
     * @throws IOException
     */
    public static byte[] imageWaterMark(File file, String waterMarkName,
                                        ImageWaterMarkConfigure configure) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try {
            out = new ByteArrayOutputStream();
            if(configure == null) configure = new ImageWaterMarkConfigure();
            input = new FileInputStream(file);
            BufferedImage bufferedImage = ImageIO.read(input);
            WaterMarkUtil.addImageWatermark(bufferedImage, waterMarkName,configure);
            String imageType = configure.getType();
            ImageIO.write(bufferedImage, imageType, out);
            bytes = out.toByteArray();
        }finally {
            if(input != null) input.close();
            if(out != null) out.close();
        }
        return bytes;
    }

}
