package com.beloved;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.beloved.utils.BarCodeUtils;
import com.beloved.utils.QRCodeUtil;
import com.beloved.utils.WordUtil2;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextMarginFinder;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.DocumentRenderer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class PrintToolApplicationTests {

    @Test
    void wordToPdf() {
        File inputWord = new File("E:\\downloads\\pdf临时文件\\1650989184997.docx");
        File outputFile = new File("E:\\downloads\\pdf临时文件\\toPdf.pdf");
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWord() throws IOException {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("userName", "张三");
        parms.put("age", 20);
        ImageEntity imageEntity = new ImageEntity("E:\\downloads\\pdf临时文件\\1650984331740.jpg", 200, 200);
        parms.put("image", imageEntity);

        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("index", i+1);
            map.put("userName", "张三"+(i+1));
            map.put("age", 20+i);
//            map.put("image", imageEntity);
            list.add(map);
        }
        parms.put("list", list);

        ArrayList<Object> list2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", i+1);
            map.put("aaa", "张三"+(i+1));
            list.add(map);
        }
//        parms.put("list2", list2);


        WordUtil2.exportWord("C:\\Users\\张恒\\Desktop\\测试模板.docx", "E:\\downloads\\pdf临时文件", System.currentTimeMillis()+".docx", parms);
    }

    @Test
    void test01() throws IOException, WriterException, NotFoundException {
        QRCodeUtil.getQRCode("https://www.bilibili.com/", "E:\\downloads\\pdf临时文件\\" + System.currentTimeMillis() + ".jpg");
//        System.out.println(QRCodeUtil.decodeQRCode("D:\\download\\pdf临时文件\\1650940735922.jpg"));
//        System.out.println(QRCodeUtil.getQRCodeBase64("https://www.bilibili.com/"));

//        BarCodeUtils.getBarCode("rewqrwqerwerq", "D:\\download\\pdf临时文件1\\" + System.currentTimeMillis() + ".jpg");
//        BarCodeUtils.getBarCodeWords("TTT123456789", "TTT123456789", "D:\\download\\pdf临时文件1\\" + System.currentTimeMillis() + ".jpg");
//        BarCodeUtils.getBarCodeWords("张三",
//                "张三",
//                "坐上",
//                "忧伤",
//                300,200,0,0,10,0,-10,0,15,
//                "D:\\download\\pdf临时文件1\\" + System.currentTimeMillis() + ".jpg");

//        System.out.println(BarCodeUtils.decodeBarCode("D:\\download\\pdf临时文件1\\1650952571606.jpg"));
//        System.out.println(QRCodeUtil.decodeQRCode("D:\\download\\pdf临时文件1\\1650941601952.jpg"));
    }

    @Test
    void contextLoads() throws IOException, WriterException {
        String targetPdfPath = "E:\\downloads\\pdf临时文件\\" + System.currentTimeMillis() + ".pdf";
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "张三三三三三三三三三三三三三三三三三李四李四李四李四李四王五王五");
        params.put("age", 22);
        params.put("sex", "男");
        params.put("birthday",  new Date());
        params.put("image_photo", new ClassPathResource("static/image/logo.jpg").getFile());
        params.put("qr_code", "https://www.bilibili.com/");
//        params.put("bar_code", new File("D:\\download\\pdf临时文件1\\1650953701683.jpg"));
        params.put("bar_code", "TTT1234567889");

        File templatePdf = new ClassPathResource("templates/测试模板.pdf").getFile();

        templateFill(templatePdf, targetPdfPath, params);
    }

    private static final String IMAGE_PREFIX = "IMAGE_";
    private static final String QR_PREFIX = "QR_";
    private static final String BAR_PREFIX = "BAR_";

    public static void templateFill(File templatePdf, String targetPdfPath, Map<String, Object> params) throws IOException, WriterException {

        // 创建 pdf 文件
        PdfDocument pdf = new PdfDocument(new PdfReader(templatePdf), new PdfWriter(targetPdfPath));

        // 创建字体
        PdfFont font = getPdfFont("./static/fonts/simsun.ttc,0");

        // 获取 pdf 模板中的域值信息
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fieldMap = form.getFormFields();

        for (String key : params.keySet()) {
            Object value = params.get(key);
            PdfFormField formField = fieldMap.get(key);

            System.out.println(formField.getParent());

            if (ObjectUtils.isEmpty(formField) || ObjectUtils.isEmpty(value)) {
                continue;
            }

            // 填充图像域
            if (StringUtils.startsWith(StringUtils.upperCase(key), IMAGE_PREFIX)) {
                PdfButtonFormField formButtonField = (PdfButtonFormField)formField;
                File imageFile = (File)value;
                formButtonField.setImage(imageFile.getPath());
                continue;
            }

            // 填充二维码
            if (StringUtils.startsWith(StringUtils.upperCase(key), QR_PREFIX)) {
                if (value instanceof File) {
                    PdfButtonFormField formButtonField = (PdfButtonFormField)formField;
                    File imageFile = (File)value;
                    formButtonField.setImage(imageFile.getPath());
                } else {
                    formField.setValue(QRCodeUtil.getQRCodeBase64(value.toString()));
                }
                continue;
            }

            // 填充条形码
            if (StringUtils.startsWith(StringUtils.upperCase(key), BAR_PREFIX)) {
                if (value instanceof File) {
                    PdfButtonFormField formButtonField = (PdfButtonFormField)formField;
                    File imageFile = (File)value;
                    formButtonField.setImage(imageFile.getPath());
                } else {
                    formField.setValue(BarCodeUtils.getBarCodeWordsBase64(value.toString(), value.toString()));
//                    formField.setValue(BarCodeUtils.getBarCodeBase64(value.toString()));
                }
                continue;
            }

            // 填充文本域
            formField.setValue(value.toString()).setFont(font);
        }

        // 设置文本不可编辑
        form.flattenFields();

        Document doc = new Document(pdf);

        Table table = new Table(10).setWidth(UnitValue.createPercentValue(50));

        for (int i = 0; i < 10; i++) {
            table.addHeaderCell("TITLE"+i);
        }

        for (int i = 0; i < 1000; i++) {
            table.addCell("张三"+i);
            if (i % 5 == 0) {
                table.startNewRow();
            }
        }
        doc.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));
        doc.close();
        pdf.close();
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
