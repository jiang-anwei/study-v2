package com.icekredit.pdf.utils;

import java.io.InputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import com.kennycason.kumo.font.KumoFont;

/**
 * pdf字体工具类
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class FontUtils {

    /**
     * base font 对象
     */
    public static BaseFont baseFontChinese = null;

    /**
     * 中文字体对象
     */
    public static Font chineseFont = null;

    /**
     * 默认字体颜色
     */
    public static BaseColor fontColor = null;

    /**
     * 默认字体大小
     */
    public static int fontSize = 10;

    /**
     * 默认字体样式
     */
    public static int fontStyle = Font.NORMAL;

    /**
     * 微软雅黑字体文件资源名称
     */
    public static final String MSYH_FONT_STR = "msyh.ttf";

    /**
     * 词云字体
     */
    public static java.awt.Font wordCloudFont = null;

    /**
     * 词云kumo字体
     */
    public static KumoFont wordCloudKumoFont = null;

    static {
        try {
            fontColor = new BaseColor(0x59, 0x57, 0x57, 0xff);
            fontSize = 8;
            fontStyle = Font.NORMAL;
            baseFontChinese = BaseFont.createFont(MSYH_FONT_STR, BaseFont.IDENTITY_H, false);
            chineseFont = new Font(baseFontChinese, fontSize, fontStyle, fontColor);

            InputStream inputStream = FontUtils.class.getClassLoader().getResourceAsStream(MSYH_FONT_STR);

            wordCloudFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
            wordCloudFont.deriveFont(java.awt.Font.BOLD);
            wordCloudKumoFont = new KumoFont(wordCloudFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
