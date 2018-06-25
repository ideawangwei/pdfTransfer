package com.bonc.pdf.util;

import com.bonc.pdf.enums.PdfWaterEnum;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfStamper;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by ldf on 2018/2/27.
 * 水印工具类
 */
public class WaterMarkUtil {

    /**
     * 添加pdf水印
     * @param total
     * @param stamper
     * @param waterMarkName
     * @param configure
     * @return
     */
    public static void addPdfWaterMark(int total,PdfStamper stamper,
                                       String waterMarkName,
                                       PdfWaterMarkConfigure configure,
                                       int position) throws IOException, DocumentException {
        try{
            if(configure == null) configure = new PdfWaterMarkConfigure();
            PdfContentByte under;
            Rectangle pageRect = null;
            //这里的字体设置比较关键，这个设置是支持中文的写法
//            BaseFont base = BaseFont.createFont("STSong-Light",
//                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 使用系统字体
            BaseFont base = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.EMBEDDED);// 使用系统字体
            for (int i = 1; i < total; i++) {
                pageRect = stamper.getReader().
                        getPageSizeWithRotation(i);
                float w = pageRect.getWidth();
                float h = pageRect.getHeight();
                if(PdfWaterEnum.WATER_MARK_UNDER == position){
                    under = stamper.getUnderContent(i);
                }else {
                    under = stamper.getOverContent(i);
                }
                // 获得PDF最顶层
                under.saveState();
                PdfGState gs = new PdfGState();
                // 设置透明度
                gs.setFillOpacity(configure.getFillOpacity());
                under.setGState(gs);
                // 注意这里必须调用一次restoreState 否则设置无效
                under.restoreState();
                under.setLineWidth(configure.getLineWidth());
                under.stroke();
                float[] lineDash = configure.getLineDash();
                under.setLineDash(lineDash[0], lineDash[1], lineDash[2]);
                under.beginText();
                float fontSize = configure.getFontSize();
                under.setFontAndSize(base, fontSize);
                int[] colorFill = configure.getColorFill();
                under.setColorFill(new BaseColor(colorFill[0], colorFill[1], colorFill[2]));
                float width = under.getEffectiveStringWidth(waterMarkName, true);
                float height = base.getAscentPoint(waterMarkName, fontSize) + base.getDescentPoint(waterMarkName, 8);
                //水印文字成45度角倾斜
                int WATERMARK_PAGE_ANGLE = configure.getAngle();
                float xFactor = (float) Math.cos(Math.toRadians(WATERMARK_PAGE_ANGLE));
                float yFactor = (float) Math.sin(Math.toRadians(WATERMARK_PAGE_ANGLE));
                float v = (width * xFactor) + (height * yFactor);
                float k = width * yFactor + height * xFactor;
                for (float x = 0; x < w; x += width * xFactor + height * yFactor) {
                    for (float y = -10f * yFactor; y < h; y += (width * yFactor + height * xFactor)*configure.density) {
                        under.showTextAlignedKerned(Element.ALIGN_CENTER,
                                waterMarkName, x, y,
                                WATERMARK_PAGE_ANGLE);
                    }
                }
                // 添加水印文字
                under.endText();
            }
        }finally {

        }
    }

    public static void addImageWatermark(BufferedImage original, String watermarkText,
                                         ImageWaterMarkConfigure configure) {
        try{
            Graphics2D g2d = original.createGraphics();
            double[] scale = configure.getScale();//水印比例
            g2d.scale(scale[0], scale[1]);
            int[] color = configure.getColor();
            Color markContentColor = new Color(color[0], color[1], color[2], color[3]);
            g2d.setColor(markContentColor);
            g2d.addRenderingHints(
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON));
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // create watermark text shape for rendering
            Object[] fontParam = configure.getFont();//水印字体
            Font font = new Font(String.valueOf(fontParam[0]), (Integer) fontParam[1], (Integer)fontParam[2]);
            GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), watermarkText);
            java.awt.Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
            Shape textShape = fontGV.getOutline();
            double textWidth = size.getWidth();
            double textHeight = size.getHeight();
            AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / 4d);
            Shape rotatedText = rotate45.createTransformedShape(textShape);
            // use a gradient that repeats 4 times
            g2d.setPaint(new GradientPaint(0, 0,
                    new Color(0f, 0f, 0f, 0.1f),
                    original.getWidth() / 2, original.getHeight() / 2,
                    new Color(1f, 1f, 1f, 0.1f)));
            g2d.setStroke(new BasicStroke(0.5f));
            // step in y direction is calc'ed using pythagoras + 5 pixel padding
            double yStep = Math.sqrt(textWidth * textWidth / 2) + 5;
            // step over image rendering watermark text
            for (double x = -textHeight * 3; x < original.getWidth(); x += (textHeight * 3)) {
                double y = -yStep;
                for (; y < original.getHeight(); y += yStep) {
                    g2d.draw(rotatedText);
                    g2d.fill(rotatedText);
                    g2d.translate(0, yStep);
                }
                g2d.translate(textHeight * 3, -(y + yStep));
            }
        }finally {

        }
    }




}
