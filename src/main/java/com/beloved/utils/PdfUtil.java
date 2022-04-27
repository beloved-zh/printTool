package com.beloved.utils;

import com.beloved.enums.FontEnum;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author beloved
 */
public class PdfUtil {

    // 图片前缀
    private static final String IMAGE_PREFIX = "IMAGE_";
    // 二维码
    private static final String QR_PREFIX = "QR_";
    // 条码
    private static final String BAR_PREFIX = "BAR_";

    /**
     * 生成填充模板后的文件
     * @param templatePath      模板文件地址
     * @param targetPath        输入目录
     * @param targetFileName    输出文件名 必须.pdf
     * @param fontEnum          字体枚举
     * @param params            参数
     * @throws IOException
     * @throws WriterException
     */
    public static void templateFill(String templatePath, String targetPath, FontEnum fontEnum, Map<String, Object> params) throws IOException, WriterException {
        Assert.notNull(templatePath,"模板文件路径不能为空");
        Assert.isTrue(targetPath.endsWith(".pdf"),"导出请使用pdf格式");

        File targetFile = new File(targetPath);
        // 是否有父级目录没有则创建
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }

        // 创建 pdf 文件
        PdfDocument pdf = new PdfDocument(new PdfReader(templatePath), new PdfWriter(targetFile));

        // 创建字体
        PdfFont font = getPdfFont(fontEnum);

        // 获取 pdf 模板中的域值信息
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fieldMap = form.getFormFields();

        for (String key : params.keySet()) {
            Object value = params.get(key);

            PdfFormField formField = fieldMap.get(key);

            if (ObjectUtils.isEmpty(formField) || ObjectUtils.isEmpty(value)) {
                continue;
            }

            // 填充图像域
            if (StringUtils.startsWith(StringUtils.upperCase(key), IMAGE_PREFIX) && value instanceof File) {
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
                    formField.setValue(BarCodeUtil.getBarCodeBase64(value.toString()));
                }
                continue;
            }

            // 填充文本域
            formField.setValue(value.toString()).setFont(font);
        }

        // 设置文本不可编辑
        form.flattenFields();

        pdf.close();
    }

    /**
     * 获取统一字体
     * @param fontEnum 字体定义枚举
     * @return
     */
    private static PdfFont getPdfFont(FontEnum fontEnum){
        PdfFont pdfFont = null;
        try {
            pdfFont = PdfFontFactory.createFont(fontEnum.getPath(), PdfEncodings.IDENTITY_H);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFont;
    }
}
