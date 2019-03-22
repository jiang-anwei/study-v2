package com.icekredit.pdf.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 图像资源加载器
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class ImageResourceLoader extends ResourceLoader {

    /** 图像资源加载器单例 */
    public static ImageResourceLoader imageResourceLoader;

    /**
     * 私有构造函数
     *
     */
    private ImageResourceLoader() {}

    /**
     * 加载指定图片资源文件
     *
     *
     * @param imageName 图像资源文件名称
     *
     * @return 图像资源文件字节数组，如果发生异常返回null
     */
    public byte[] loadImage(String imageName) {
        InputStream inputStream = null;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            inputStream = this.getClass().getClassLoader().getResourceAsStream(imageName);

            byte[] buffer = new byte[1024];
            int    length = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 获取图像资源加载器单例
     *
     *
     * @return 图像资源加载器对象
     */
    public static ImageResourceLoader getSingleInstance() {
        if (imageResourceLoader == null) {
            imageResourceLoader = new ImageResourceLoader();
        }

        return imageResourceLoader;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
