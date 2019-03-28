package com.icekredit.pdf.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 资源加载器
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class ResourceLoader {

    /**
     * 从资源文件中加载json数据
     *
     *
     * @param jsonFileStr 资源文件名称
     *
     * @return    加载到的json数据字符串  如果未找到指定资源文件，返回空字符串
     */
    public static String getJsonStr(String jsonFileStr) {
        try {
            InputStream           inputStream  = ResourceLoader.class.getClassLoader().getResourceAsStream(jsonFileStr);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[]                buffer       = new byte[1024];
            int                   length       = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            return new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}


//~ Formatted by Jindent --- http://www.jindent.com