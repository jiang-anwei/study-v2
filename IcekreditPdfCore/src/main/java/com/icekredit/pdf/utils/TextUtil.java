package com.icekredit.pdf.utils;

import com.itextpdf.text.Rectangle;

/**
 * Text工具类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class TextUtil {

    /**
     * 用于计算将指定文本放置到指定单元格中是需要占用的行数
     *
     *
     * @param message 需要勾画的字符串
     * @param fontSize 勾画字符串使用的字体大小
     * @param availableSpace 用来放置当前字符串的指定空间
     *
     * @return 放置整个字符串需要的行数
     */
    public static int getLinesCountNeeded(String message, float fontSize, Rectangle availableSpace) {
        StringBuilder builder = new StringBuilder();

        if (message == null) {
            return 0;
        }

        // 如果不需要勾画value str,那么得到的需要勾画的行数为0,此时必须手动置为1
        if (message.trim().equals("")) {
            return 1;
        }

        int linesCountNeeded  = 1;
        int currentDrawnIndex = 0;

        while (currentDrawnIndex < message.length()) {
            if (message.charAt(currentDrawnIndex) == '\n') {
                linesCountNeeded++;
                builder.delete(0, builder.length());
                currentDrawnIndex++;

                continue;
            }

            builder.append(message.charAt(currentDrawnIndex));

            if (FontUtils.baseFontChinese.getWidthPoint(builder.toString(), fontSize) > availableSpace.getWidth()) {
                currentDrawnIndex--;
                linesCountNeeded++;
                builder.delete(0, builder.length());

                continue;
            }

            currentDrawnIndex++;
        }

        return linesCountNeeded;
    }

    /**
     * 根据当前所画的字符串part开始位置计算勾画字符串的结束位置
     *
     *
     * @param message 当前勾画的字符串
     * @param fontSize 勾画当前字符串使用的字体大小
     * @param currentDrawnIndex 当前勾画字符串部分开始位置，比如第一次勾画时，此值应该为0
     * @param availableSpace 用来放置当前字符串的指定空间
     *
     * @return 当前勾画字符串部分结束位置，比如如果当前行可以容纳的字体大小为fontSize的字符个数为n，那么第一次勾画是此值应该为n
     */
    public static int getNextDrawableTextIndex(String message, float fontSize, int currentDrawnIndex,
                                               Rectangle availableSpace) {
        StringBuilder builder = new StringBuilder();

        while (currentDrawnIndex < message.length()) {
            if (message.charAt(currentDrawnIndex) == '\n') {
                currentDrawnIndex++;

                break;
            }

            builder.append(message.charAt(currentDrawnIndex));

            if (FontUtils.baseFontChinese.getWidthPoint(builder.toString(), fontSize) > availableSpace.getWidth()) {
                currentDrawnIndex--;

                break;
            }

            currentDrawnIndex++;
        }

        return currentDrawnIndex;
    }

    /**
     * 根据当前勾画的行数从指定需要勾画的字符串中取出当前行需要勾画的部分
     *
     *
     * @param message 需要勾画的整个字符串
     * @param fontSize 勾画字符串使用的字体大小
     * @param lineCountAlreadyDrawn 已经勾画的行数
     * @param availableSpace 可用空间
     *
     * @return 需要勾画的当前行字符串
     */
    public static String getSpecLineToDraw(String message, float fontSize, int lineCountAlreadyDrawn,
                                           Rectangle availableSpace) {
        int currentLineCount  = 0;
        int currentStartIndex = 0;
        int currentEndIndex   = 0;

        while ((currentLineCount <= lineCountAlreadyDrawn) && (currentEndIndex < message.length())) {
            currentStartIndex = currentEndIndex;
            currentEndIndex   = getNextDrawableTextIndex(message, fontSize, currentStartIndex, availableSpace);
            currentLineCount++;
        }

        return message.substring(currentStartIndex, currentEndIndex);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
