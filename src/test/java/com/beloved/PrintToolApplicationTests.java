package com.beloved;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class PrintToolApplicationTests {

    @Test
    void contextLoads() throws IOException {
        String targetPdfPath = "D:\\download\\pdf临时文件\\" + System.currentTimeMillis() + ".pdf";
        Map<String, Object> params = new HashMap<>();
        params.put("lotId", "100000000000");
        params.put("lotIdT", "100000000000");
        params.put("printDate", new Date());
        params.put("customer", "张三");
        params.put("custCode", 8888888);
        params.put("deliveryTime", "2022-04-25");
        params.put("image", "2022-04-25");

        String templatePdfPath = "C:\\Users\\Beloved\\Desktop\\IC测试流程卡(FT).pdf";
        File templatePdf = new File(templatePdfPath);

        templateFill(templatePdf, targetPdfPath, params);
    }


    public static void templateFill(File templatePdf, String targetPdfPath, Map<String, Object> params) throws IOException {

        // 创建 pdf 文件
        PdfDocument pdf = new PdfDocument(new PdfReader(templatePdf), new PdfWriter(targetPdfPath));

        // 创建字体
//        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
        PdfFont font = PdfFontFactory.createFont("./static/fonts/simhei.ttf");
        pdf.addFont(font);

        // 获取 pdf 模板中的域值信息
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fieldMap = form.getFormFields();

        for (String key : params.keySet()) {
            PdfFormField formField = fieldMap.get(key);
            if (ObjectUtils.isEmpty(formField) || ObjectUtils.isEmpty(params.get(key))) {
                continue;
            }
            System.out.println(formField);
            System.out.println(key);
            System.out.println(params.get(key));
            System.out.println("======================");

            // 填充文本域
            formField.setValue(params.get(key).toString());
        }

        // 设置文本不可编辑
        form.flattenFields();

        pdf.close();
    }
}
