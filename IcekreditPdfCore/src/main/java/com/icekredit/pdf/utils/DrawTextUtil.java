package com.icekredit.pdf.utils;

import com.icekredit.pdf.entities.View;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * 文本勾画工具类
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class DrawTextUtil {

    /**
     * 将指定参考点当作中点参考
     */
    public static final int REFER_CENTER = 0;

    /**
     * 将指定参考点当作左边参考点（文字会在参考点右边靠左对齐）
     */
    public static final int REFER_LEFT = 1;

    /**
     * 将指定参考点当作右边边参考点（文字会在参考点左边边靠右对齐）
     */
    public static final int REFER_RIGHT = 2;

    /**
     * 在页面上画一个字符串的方法
     *
     * @param canvas       画字符串的PdfContentByte对象
     * @param str          需要勾画的字符串
     * @param strXPosition 参考点x坐标
     * @param strYPosition 参考点y坐标
     * @param strFont      画字符串使用的字体
     * @param isAlignRight 是否向右对齐
     */
    public static void drawReportAttributeStr(PdfContentByte canvas, String str, float strXPosition,
                                              float strYPosition, Font strFont, boolean isAlignRight) {
        try {
            float strWidth = FontUtils.baseFontChinese.getWidthPoint(str, strFont.getSize()) + 1;
            float strHeight = DrawTextUtil.getStringHeight((int) strFont.getSize());
            Phrase strPhrase = new Phrase(new Chunk(str, strFont));
            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(new Rectangle(strXPosition - (isAlignRight ? strWidth : 0),
                    strYPosition - strHeight / 2,
                    strXPosition + (!isAlignRight
                            ? strWidth
                            : 0),
                    strYPosition + strHeight / 2));
            columnText.setUseAscender(true);
            columnText.addText(strPhrase);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以指定像素为参考点，居中（refer_center）/左对齐(refer_left)/右对齐(refer_right)勾画一个文本
     *
     * @param canvas  画字符串的PdfContentByte对象
     * @param msg 需要勾画的字符串
     * @param chineseFont 使用的字体
     * @param referXPosition 参考点x坐标
     * @param referYPosition 参考点y坐标
     * @param referType 参考方式
     */
    public static void drawText(PdfContentByte canvas, String msg, Font chineseFont, float referXPosition,
                                float referYPosition, int referType) {
        float strHeight = FontUtils.baseFontChinese.getWidthPoint("中文", chineseFont.getSize()) / 2;
        float strWidth = FontUtils.baseFontChinese.getWidthPoint(msg, chineseFont.getSize());

        try {
            Rectangle position = null;

            switch (referType) {
                case REFER_CENTER:
                    position = new Rectangle(referXPosition - strWidth / 2,
                            referYPosition - strHeight / 2,
                            referXPosition + strWidth / 2,
                            referYPosition + strHeight / 2);

                    break;

                case REFER_LEFT:
                    position = new Rectangle(referXPosition - 0,
                            referYPosition - strHeight / 2,
                            referXPosition + strWidth,
                            referYPosition + strHeight / 2);

                    break;

                case REFER_RIGHT:
                    position = new Rectangle(referXPosition - strWidth,
                            referYPosition - strHeight / 2,
                            referXPosition + 0,
                            referYPosition + strHeight / 2);

                    break;

                default:
                    position = new Rectangle(referXPosition - strWidth / 2,
                            referYPosition - strHeight / 2,
                            referXPosition + strWidth / 2,
                            referYPosition + strHeight / 2);

                    break;
            }

            ColumnText strColumnText = new ColumnText(canvas);

            strColumnText.setSimpleColumn(new Phrase(new Chunk(msg, chineseFont)),
                    position.getLeft(),
                    position.getBottom(),
                    position.getRight(),
                    position.getTop(),
                    0,
                    Element.ALIGN_CENTER);
            View.showPosition(canvas, position);
            strColumnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字符串高度的函数
     *
     * @param fontSize 字符串展示的字体大小
     *
     * @return 字符串高度
     */
    public static float getStringHeight(int fontSize) {
        return FontUtils.baseFontChinese.getWidthPoint("中文", fontSize) / 2;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
