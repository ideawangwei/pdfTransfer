package com.bonc.pdf.util;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ldf on 2018/3/1.
 */
public class PdfToImageTest {

    public static void pdfToImage(String inFile, String outFile, int pageNum){
        Document document = new Document();
        try {
            document.setFile(inFile);
        } catch (Exception ex) {

        }
        //determine the scale of the image
        //and the direction of the image
        float scale = 1.3f;
        float rotation = 0f;
        BufferedImage image = (BufferedImage)
                document.getPageImage(pageNum, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
        RenderedImage rendImage = image;
        try {
            //System.out.println("/t capturing page " + i);——only for testing,you can delete it
            //the next two lines determine the saving path
            //you can change it via the way you like
            File file = new File(outFile);
            ImageIO.write(rendImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.flush();
        document.dispose();
    }

}
