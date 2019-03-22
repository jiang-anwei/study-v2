package com.icekredit.pdf.utils;

import java.io.IOException;

import sun.misc.BASE64Decoder;

/**
 * Image数据格式转换工具
 *
 * @version        1.0, 16/6/24
 * @author         lawliet
 */
public class ImageTools {

    /**
     * 将base64编码的图片文件解码，并且返回对应的byte array 数据
     *
     *
     * @param imgStr base64 格式编码的图像文件数据
     *
     * @return 对应的byte array 数据
     *
     * @throws IOException BASE64Decoder.decodeBuffer函数可能抛出IoException
     */
    public static byte[] generateImage(String imgStr) throws IOException {    // 对字节数组字符串进行Base64解码并生成图片
        BASE64Decoder decoder = new BASE64Decoder();

        // Base64解码
        byte[] b = decoder.decodeBuffer(imgStr);

        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {    // 调整异常数据
                b[i] += 256;
            }
        }


        return b;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
