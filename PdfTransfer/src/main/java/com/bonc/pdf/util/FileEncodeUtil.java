package com.bonc.pdf.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Created by ldf on 2018/3/8.
 */
public class FileEncodeUtil {

    public static void convert(File file, String fromCharsetName,
                               String toCharsetName) throws Exception {
        convert(file, fromCharsetName, toCharsetName, null);
    }

    public static void convert(File file, String fromCharsetName,
                               String toCharsetName, FilenameFilter filter) throws Exception {
        if (file.isDirectory()) {
            File[] fileList = null;
            if (filter == null) {
                fileList = file.listFiles();
            } else {
                fileList = file.listFiles(filter);
            }
            for (File f : fileList) {
                convert(f, fromCharsetName, toCharsetName, filter);
            }
        } else {
            if (filter == null
                    || filter.accept(file.getParentFile(), file.getName())) {
                String fileContent = getFileContentFromCharset(file,
                        fromCharsetName);
                saveFile2Charset(file, toCharsetName, fileContent);
            }
        }
    }

    public static String getFileContentFromCharset(File file,
                                                   String fromCharsetName) throws Exception {
        if (!Charset.isSupported(fromCharsetName)) {
            throw new UnsupportedCharsetException(fromCharsetName);
        }
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(inputStream,
                fromCharsetName);
        char[] chs = new char[(int) file.length()];
        reader.read(chs);
        String str = new String(chs).trim();
        reader.close();
        return str;
    }

    public static void saveFile2Charset(File file, String toCharsetName,
                                        String content) throws Exception {
        if (!Charset.isSupported(toCharsetName)) {
            throw new UnsupportedCharsetException(toCharsetName);
        }
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outWrite = new OutputStreamWriter(outputStream,
                toCharsetName);
        outWrite.write(content);
        outWrite.close();
    }

}
