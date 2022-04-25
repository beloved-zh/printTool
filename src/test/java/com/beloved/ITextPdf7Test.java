package com.beloved;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ITextPdf7Test {

    public static void main(String[] args) throws IOException {
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
        PdfFont font = PdfFontFactory.createFont("classpath:/fonts/simhei.ttf");
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


    public static void fillTemplate(String tempPdfPath,String targetPdfPath) {// 利用模板生成pdf
        try {
            //Initialize PDF document
            PdfDocument pdf = new PdfDocument(new PdfReader(tempPdfPath), new PdfWriter(targetPdfPath));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();

            //处理中文问题
            PdfFont font = PdfFontFactory.createFont();
            String[] str = {
                    "社会主义核心价值观",
                    "富强 民主 文明 和谐",
                    "自由 平等 公正 法制",
                    "爱国 敬业 诚信 友善"
            };
            int i = 0;
            java.util.Iterator<String> it = fields.keySet().iterator();
            while (it.hasNext()) {
                //获取文本域名称
                String name = it.next().toString();
                //填充文本域
                fields.get(name).setValue(str[i++]).setFont(font).setFontSize(12);
            }
            form.flattenFields();//设置表单域不可编辑
            pdf.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
