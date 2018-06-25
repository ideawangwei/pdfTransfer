package com.bonc.pdf.util.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * Created by ldf on 2018/3/11.
 */
public class PdfBoxUtil {

    public static String pdfContent(File file) throws IOException {
        String content = "";
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            int pages = document.getNumberOfPages();
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setStartPage(1);
            textStripper.setEndPage(pages);
            content = textStripper.getText(document);
        }finally {
            if(document != null) document.close();
        }
        return content;
    }

    public static String pdfContent(String filePath) throws IOException {
        File file = new File(filePath);
        return pdfContent(file);
    }

    public static String pdfContent(byte[] bytes) throws IOException {
        String content = "";
        PDDocument document = null;
        try{
            document = PDDocument.load(bytes);
            int pages = document.getNumberOfPages();
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setStartPage(1);
            textStripper.setEndPage(pages);
            content = textStripper.getText(document);
        }finally {
            if(document != null) document.close();
        }
        return content;
    }

}
