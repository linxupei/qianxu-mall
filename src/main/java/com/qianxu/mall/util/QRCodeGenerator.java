package com.qianxu.mall.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 15:54
 * @describe 生成二维码工具
 */
public class QRCodeGenerator {
    public static void generateQRCodeImage(String content, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        content = new String(content.getBytes("UTF-8"),"ISO-8859-1");
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) throws IOException, WriterException {
        generateQRCodeImage("师姐牛啊\n\uD83E\uDD28\uD83E\uDD28\uD83E\uDD28\uD83E\uDD28", 350, 350, "D:/Development/IdeaProject/qianxu-mall/static/Test.png");
    }
}
