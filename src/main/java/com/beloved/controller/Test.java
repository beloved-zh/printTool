package com.beloved.controller;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.beloved.utils.BarCodeUtil;
import com.beloved.utils.QRCodeUtil;
import com.beloved.utils.WordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class Test {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    
    @GetMapping("/print")
    public void print(HttpServletResponse response) throws Exception {

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


        // 设置相应类型
        response.setHeader("Content-Type", "application/pdf");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        ByteArrayOutputStream os = WordUtil.templateFillToPdfOutputByteStream(templatePath, params);

        servletOutputStream.write(os.toByteArray());
        servletOutputStream.flush();
        servletOutputStream.close();
    }

}
