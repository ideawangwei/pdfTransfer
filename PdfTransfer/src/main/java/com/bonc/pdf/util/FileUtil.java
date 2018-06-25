package com.bonc.pdf.util;

import com.itextpdf.text.pdf.PdfReader;

import java.io.*;


public class FileUtil {
    public static boolean isValidFileExtension(String filename)
    {
        if (isOfficeExtendsion(filename))
            return true;
        if (isPdf(filename))
            return true;

        return false;
    }

    public static boolean isOfficeExtendsion(String filename)
    {
        if (isWord(filename))
            return true;
        if (isExcel(filename))
            return true;
        if (isPowerPoint(filename))
            return true;
        if (isText(filename))
            return true;
        return false;
    }

    public static boolean isWord(String filename)
    {
        return fileType(filename, ".doc") ||
                fileType(filename, ".docx") ;

    }

    public static boolean isDoc(String fileName){
        return fileType(fileName, ".doc");
    }

    public static boolean isDocx(String fileName){
        return fileType(fileName, ".docx");
    }

    public static boolean isExcel(String filename)
    {
        return fileType(filename, ".xls") ||
                fileType(filename, ".xlsx");
    }

    public static boolean isXls(String filename)
    {
        return fileType(filename, ".xls");
    }

    public static boolean isXlsx(String filename)
    {
        return fileType(filename, ".xlsx");
    }

    public static boolean isPowerPoint(String filename)
    {
        return fileType(filename, ".ppt") ||
                fileType(filename, ".pptx") ;
    }

    public static boolean isPpt(String filename)
    {
        return fileType(filename, ".ppt");
    }

    public static boolean isPptx(String filename)
    {
        return fileType(filename, ".pptx") ;
    }

    public static boolean isText(String filename)
    {
        return fileType(filename, ".txt") ;
    }
    public static boolean isImage(String filename)
    {
        return fileType(filename, ".pdf") ||
                fileType(filename,".bmp") ||
                fileType(filename,".gif") ||
                fileType(filename,".jpeg") ||
                fileType(filename,".jpg") ||
                fileType(filename,".png") ||
                fileType(filename,"tif") ||
                fileType(filename,".ico");
    }

    public static boolean isPdf(String filename)
    {
        return fileType(filename, ".pdf");
    }

    public static boolean isBmp(String filename)
    {
        return fileType(filename,".bmp");
    }

    public static boolean isGif(String filename)
    {
        return fileType(filename,".gif");
    }

    public static boolean isJpeg(String filename)
    {
        return fileType(filename,".jpeg");
    }
    public static boolean isJpg(String filename)
    {
        return fileType(filename,".jpg");
    }

    public static boolean isPng(String filename)
    {
        return fileType(filename,".png");
    }

    public static boolean isTif(String filename)
    {
        return fileType(filename,"tif");
    }

    public static boolean isIco(String filename)
    {
        return fileType(filename,".ico");
    }

    public static Boolean isVsdx(String fileName) { return fileType(fileName, ".vsdx");}

    public static Boolean isOdt(String fileName) { return fileType(fileName, ".odt"); }

    private static Boolean fileType(String fileName ,String type){
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(type.equals(fileType)) return true;
        return false;
    }

    /**
     * 根据文件名称获取文件类型
     * @param fileName
     * @return
     */
    public static String getTypeByName(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取pdf页数
     * @param file
     * @return
     * @throws IOException
     */
    public static int getPdfPage(File file) throws IOException {
        PdfReader reader = new PdfReader(new FileInputStream(file));
        return reader.getNumberOfPages();
    }

    /**
     * 获取pdf页数
     * @param fileBytes
     * @return
     * @throws IOException
     */
    public static int getPdfPage(byte[] fileBytes) throws IOException {
        PdfReader pdfReader = new PdfReader(fileBytes);
        return pdfReader.getNumberOfPages() + 1;
    }



}
