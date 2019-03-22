package com.icekredit.pdf.utils;

import java.security.MessageDigest;

/**
 * MD5工具类
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class Md5Util {

    /**
     * 十六进制对应的表示字符 0～9 and A～F
     */
    public static final char hexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Md5摘要函数
     *
     * @param plainText 需要进行摘要的明文字符串
     * @return 摘要后的密文以十六进制表示的字符串
     */
    public final static String MD5(String plainText) {
        try {
            if ((plainText == null) || plainText.trim().equals("")) {
                throw new Exception("Invalid Input plain Text for Md5!");
            }

            byte[] btInput = plainText.getBytes();

            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            messageDigest.update(btInput);

            // 获得密文
            byte[] cipherTextByteArray = messageDigest.digest();

            // 把密文转换成十六进制的字符串形式
            return toHexString(cipherTextByteArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(Md5Util.MD5("加密 "));
        System.out.println(Md5Util.MD5("加密"));
    }

    /**
     * 将一个字节数组按照每一字节低四位 高四位的格式转换为十六进制字符串
     *
     * @param byteArray 待转换的字节数组
     * @return 转换后的十六进制字符串
     */
    public static String toHexString(byte[] byteArray) {
        int byteArrayLength = byteArray.length;
        char resultCharArray[] = new char[byteArrayLength * 2];
        int index = 0;

        for (int i = 0; i < byteArrayLength; i++) {
            resultCharArray[index++] = hexDigits[byteArray[i] >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[byteArray[i] & 0xf];
        }

        return new String(resultCharArray);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
