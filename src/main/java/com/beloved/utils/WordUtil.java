package com.beloved.utils;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class WordUtil {

    /**
     * 生成填充模板后的文件
     * @param templatePath      模板路径
     * @param targetPath        输出目录  .docx
     * @param params            参数
     * @throws Exception
     */
    public static void templateFill(String templatePath, String targetPath,  Map<String, Object> params) throws Exception {
        Assert.notNull(templatePath,"模板文件路径不能为空");
        Assert.isTrue(targetPath.endsWith(".docx"),"导出请使用docx格式");

        File targetFile = new File(targetPath);
        // 是否有父级目录没有则创建
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(targetFile);
            XWPFDocument docx = WordExportUtil.exportWord07(templatePath, params);

            docx.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

    }

    /**
     * 填充 word 模板返回输出流
     * @param templatePath
     * @param params
     * @return
     * @throws Exception
     */
    public static InputStream templateFillInputByteStream(String templatePath, Map<String, Object> params) throws Exception {

        ByteArrayOutputStream os = null;
        ByteArrayInputStream is = null;

        try {
            os = templateFillOutputByteStream(templatePath, params);

            is = new ByteArrayInputStream(os.toByteArray());

            return is;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 填充 word 模板返回输出流
     * @param templatePath
     * @param params
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream templateFillOutputByteStream(String templatePath, Map<String, Object> params) throws Exception {
        Assert.notNull(templatePath,"模板文件路径不能为空");

        ByteArrayOutputStream os = null;

        try {
            os = new ByteArrayOutputStream();
            XWPFDocument docx = WordExportUtil.exportWord07(templatePath, params);

            docx.write(os);

            return os;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * word 转 pdf
     * @param wordPath      word 模板路径
     * @param pdfPath       生成 pdf 路径
     * @throws Exception
     */
    public static void wordToPdf(String wordPath, String pdfPath) throws Exception {
        wordToPdf(wordPath, pdfPath, false);
    }

    /**
     * word 转 pdf
     * @param wordPath      word 模板路径
     * @param pdfPath       生成 pdf 路径
     * @param deleteWord    是否删除 word 模板
     * @throws Exception
     */
    public static void wordToPdf(String wordPath, String pdfPath, boolean deleteWord) throws Exception {
        Assert.notNull(wordPath,"word模板文件路径不能为空");
        Assert.isTrue(pdfPath.endsWith(".pdf"), "只能转换为pdf文件");

        FileOutputStream os = null;

        try {
            // 获取凭证，校验凭证
            InputStream is = new ClassPathResource("/license.xml").getInputStream();
            new License().setLicense(is);

            File pdf = new File(pdfPath);

            // 是否有父级目录没有则创建
            if(!pdf.getParentFile().exists()){
                pdf.getParentFile().mkdirs();
            }

            os = new FileOutputStream(pdf);

            // 要转换的word文件
            Document word = new Document(wordPath);
            word.save(os, SaveFormat.PDF);

            if (deleteWord) {
                Files.deleteIfExists(Paths.get(wordPath));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * word 转 pdf
     * @param wordInputStream   word 文件输入流
     * @param pdfPath           生成 pdf 路径
     * @throws Exception
     */
    public static void wordToPdf(InputStream wordInputStream, String pdfPath) throws Exception {
        Assert.isTrue(pdfPath.endsWith(".pdf"), "只能转换为pdf文件");

        FileOutputStream os = null;

        try {
            // 获取凭证，校验凭证
            InputStream is = new ClassPathResource("/license.xml").getInputStream();
            new License().setLicense(is);

            File pdf = new File(pdfPath);

            // 是否有父级目录没有则创建
            if(!pdf.getParentFile().exists()){
                pdf.getParentFile().mkdirs();
            }

            os = new FileOutputStream(pdf);

            // 要转换的word文件
            Document word = new Document(wordInputStream);
            word.save(os, SaveFormat.PDF);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (wordInputStream != null) {
                wordInputStream.close();
            }
            if (os != null) {
                os.close();
            }
        }

    }

}