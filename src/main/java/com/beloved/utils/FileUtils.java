package com.beloved.utils;

import java.io.*;

/**
 * @author beloved
 */
public class FileUtils {

    /**
     * File转byte[]数组
     *
     * @param fileFullPath
     * @return
     */
    public static byte[] file2byte(String fileFullPath) {
        if (fileFullPath == null || "".equals(fileFullPath)) {
            return null;
        }
        return file2byte(new File(fileFullPath));
    }

    /**
     * File转byte[]数组
     *
     * @param file
     * @return
     */
    public static byte[] file2byte(File file) {
        if (file == null) {
            return null;
        }
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fileInputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0 , n);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * byte[]数组转File
     *
     * @param bytes
     * @param fileFullPath
     * @return
     */
    public static File byte2file(byte[] bytes, String fileFullPath) {
        if (bytes == null) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileFullPath);
            //判断文件是否存在
            if (file.exists()) {
                file.mkdirs();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }  catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return null;
    }


}
