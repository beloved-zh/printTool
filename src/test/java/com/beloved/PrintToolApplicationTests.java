package com.beloved;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.beloved.enums.FontEnum;
import com.beloved.utils.BarCodeUtil;
import com.beloved.utils.PdfUtil;
import com.beloved.utils.QRCodeUtil;
import com.beloved.utils.WordUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class PrintToolApplicationTests {

    @Test
    void wordTemplateFillToPdf() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String templatePath = new ClassPathResource("templates/测试模板.docx").getPath();
        String targetPath = "E:\\downloads\\pdf临时文件";
        String targetFileName = System.currentTimeMillis() + ".docx";
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "张三");
        params.put("age", 20);
        params.put("sex", "男");
        params.put("birthday", "2018-04-05");
        params.put("image_photo", new ImageEntity("http://www.wupaas.com/files/wuyunlogo.jpg", 100, 100));
        params.put("bar_code", new ImageEntity(BarCodeUtil.getBarCodeByteArray("123456789"), 200, 50));
        params.put("qr_code", new ImageEntity(QRCodeUtil.getQRCodeByteArray("https://www.bilibili.com/"), 100, 100));
        params.put("print_date", sdf.format(new Date()));

        ArrayList<Object> table = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("index", i+1);
            map.put("userName", "张三-" + (i+1));
            map.put("age", 20+i);
            map.put("birthday", sdf.format(System.currentTimeMillis()));
            ImageEntity image = new ImageEntity("./images/logo.jpg", 100, 100);
            map.put("photo ", image);
            table.add(map);
        }
        params.put("table", table);


        WordUtil.wordToPdf(WordUtil.templateFillInputByteStream(templatePath, params), "E:\\downloads\\pdf临时文件\\toPdf.pdf");
    }

    @Test
    void wordToPdf() throws Exception {
        String wordPath = "E:\\downloads\\pdf临时文件\\1111.docx";
        String padPath = "E:\\downloads\\pdf临时文件\\test\\toPdf.pdf";
        WordUtil.wordToPdf(wordPath, padPath, true);
    }

    @Test
    void wordTemplateFill() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String templatePath = new ClassPathResource("templates/测试模板.docx").getPath();
        String targetPath = "E:\\downloads\\pdf临时文件" + System.currentTimeMillis() + ".docx";
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "张三");
        params.put("age", 20);
        params.put("sex", "男");
        params.put("birthday", "2018-04-05");
        params.put("image_photo", new ImageEntity("http://www.wupaas.com/files/wuyunlogo.jpg", 100, 100));
        params.put("bar_code", new ImageEntity(BarCodeUtil.getBarCodeByteArray("123456789"), 200, 50));
        params.put("qr_code", new ImageEntity(QRCodeUtil.getQRCodeByteArray("https://www.bilibili.com/"), 100, 100));
        params.put("print_date", sdf.format(new Date()));

        ArrayList<Object> table = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("index", i+1);
            map.put("userName", "张三-" + (i+1));
            map.put("age", 20+i);
            map.put("birthday", sdf.format(System.currentTimeMillis()));
            ImageEntity image = new ImageEntity("./images/logo.jpg", 100, 100);
            map.put("photo ", image);
            table.add(map);
        }
        params.put("table", table);


        WordUtil.templateFill(templatePath, targetPath, params);
    }

    @Test
    void pdfTemplateFill() throws IOException, WriterException {
        String templatePath = new ClassPathResource("templates/测试模板.pdf").getPath();
        String targetPath = "E:\\downloads\\pdf临时文件" + System.currentTimeMillis() + ".pdf";
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "张三");
        params.put("age", 20);
        params.put("sex", "男");
        params.put("birthday", new Date());
        params.put("image_photo", "sas");
        params.put("bar_code", "123456789");
        params.put("qr_code", "https://www.bilibili.com/");


        PdfUtil.templateFill(templatePath, targetPath, FontEnum.MSYH, params);
    }

}
