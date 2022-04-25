package com.beloved;

import com.beloved.utils.FileUtils;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class PrintToolApplicationTests {

    @Test
    void contextLoads() throws IOException {
        String targetPdfPath = "E:\\downloads\\pdf临时文件\\" + System.currentTimeMillis() + ".pdf";
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "张三三三三三三三三三三三三三三三三三李四李四李四李四李四王五王五");
        params.put("age", 22);
        params.put("sex", "男");
        params.put("birthday",  new Date());
        params.put("image_photo", new ClassPathResource("static/image/logo.jpg").getFile());
        params.put("code", null);

        File templatePdf = new ClassPathResource("templates/测试模板.pdf").getFile();

        templateFill(templatePdf, targetPdfPath, params);
    }

    private static final String IMAGE_PREFIX = "IMAGE_";
    private static final String OR_PREFIX = "OR_";
    private static final String BAR_PREFIX = "BAR_";

    public static void templateFill(File templatePdf, String targetPdfPath, Map<String, Object> params) throws IOException {

        // 创建 pdf 文件
        PdfDocument pdf = new PdfDocument(new PdfReader(templatePdf), new PdfWriter(targetPdfPath));

        // 创建字体
        PdfFont font = getPdfFont("./static/fonts/simsun.ttc,0");

        // 获取 pdf 模板中的域值信息
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fieldMap = form.getFormFields();

        for (String key : params.keySet()) {
            PdfFormField formField = fieldMap.get(key);
            if (ObjectUtils.isEmpty(formField) || ObjectUtils.isEmpty(params.get(key))) {
                continue;
            }

            // 填充图像域
            if (StringUtils.startsWith(StringUtils.upperCase(key), IMAGE_PREFIX)) {
                PdfButtonFormField formButtonField = (PdfButtonFormField)formField;
                File imageFile = (File)params.get(key);
                formButtonField.setImage(imageFile.getPath());
                continue;
            }

            // 填充文本域
            formField.setValue(params.get(key).toString()).setFont(font);
        }

        // 设置文本不可编辑
//        form.flattenFields();

        pdf.close();
    }

    private static void replaceFieldImage(PdfDocument pdf, PdfButtonFormField formField, File imageFile) throws IOException {
        formField.setImage(imageFile.getPath());
    }

    /**
     * 获取统一字体
     * @param fontPath 字体文件路径
     *                 注意： ttf 类型字体文件正常传入路径   ttc 需要在路径后拼接 ,0
     * @return
     */
    public static PdfFont getPdfFont(String fontPath){
        PdfFont pdfFont = null;
        try {
            pdfFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFont;
    }
}
