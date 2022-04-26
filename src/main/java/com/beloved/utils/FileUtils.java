package com.beloved.utils;

import java.io.*;
import java.util.Base64;

/**
 * @author beloved
 */
public class FileUtils {

    /**
     * File 转 Base64
     *
     * @param fileFullPath
     * @return
     */
    public static String fileToBase64(String fileFullPath) {
        byte[] bytes = fileToByte(fileFullPath);
        Base64.Encoder encoder  = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    /**
     * File 转 Base64
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        byte[] bytes = fileToByte(file);
        Base64.Encoder encoder  = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    /**
     * File转byte[]数组
     *
     * @param fileFullPath
     * @return
     */
    public static byte[] fileToByte(String fileFullPath) {
        if (fileFullPath == null || "".equals(fileFullPath)) {
            return null;
        }
        return fileToByte(new File(fileFullPath));
    }

    /**
     * File 转 byte[]数组
     *
     * @param file
     * @return
     */
    public static byte[] fileToByte(File file) {
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
    public static File byteToFile(byte[] bytes, String fileFullPath) {
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
