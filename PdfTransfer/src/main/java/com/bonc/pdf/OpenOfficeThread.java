package com.bonc.pdf;

import com.bonc.pdf.util.FileToPdfUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ldf on 2018/3/8.
 */
public class OpenOfficeThread implements Runnable {

    private String inPath;
    private String outPath;
    public OpenOfficeThread(String inPath, String outPath){
        this.inPath = inPath;
        this.outPath = outPath;
    }
    @Override
    public void run() {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() throws IOException{
        FileToPdfUtil coc2HtmlUtil = FileToPdfUtil.getDoc2HtmlUtilInstance();
        long start = System.currentTimeMillis();
        File file = new File(inPath);
        byte[] outFile = coc2HtmlUtil.fileToPdf(file, "172.16.38.173",8100);
        long end = System.currentTimeMillis();
        System.out.println("花费了"+(end-start)/1000);
        FileOutputStream outputStream = new FileOutputStream(outPath);
        outputStream.write(outFile);
    }

}
