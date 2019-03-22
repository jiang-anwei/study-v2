package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.Arrays;
import java.util.List;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.YAxisPartData;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 图表Y轴坐标模块
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class YAxisPart extends BasePart {

    /** 坐标轴背景颜色 */
    protected BaseColor axisColor;

    /** 坐标轴刻度字体大小 */
    protected int fontSize;

    /** 坐标轴刻度字体样式 */
    protected int fontStyle;

    /** 坐标轴刻度字体颜色 */
    protected BaseColor fontColor;

    /**
     * 构造函数
     *
     */
    public YAxisPart() {}

    /**
     * 带有y坐标轴模块数据对象的构造函数
     *
     *
     * @param basePartData y坐标轴模块数据对象
     */
    public YAxisPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx y坐标轴模块左下角x坐标
     * @param lly y坐标轴模块左下角y坐标
     * @param partWidth y坐标轴模块宽度
     * @param partHeight y坐标轴模块高度
     * @param basePartData y坐标轴模块数据对象
     */
    public YAxisPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx y坐标轴模块左下角x坐标
     * @param lly y坐标轴模块左下角y坐标
     * @param partWidth y坐标轴模块宽度
     * @param partHeight y坐标轴模块高度
     * @param basePartData y坐标轴模块数据对象
     * @param parentPart 当前y坐标轴模块依附的父模块
     * @param subParts y坐标轴模块附属的子模块
     */
    public YAxisPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                     BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    /**
     * 画坐标轴描述
     *
     *
     * @param canvas 画笔
     * @param yAxisPartData y坐标轴模块数据对象
     */
    protected void DrawAxisDesc(PdfContentByte canvas, YAxisPartData yAxisPartData) {
        Font font = new Font(FontUtils.chineseFont);

        font.setSize(6);

        if (yAxisPartData.xAxisReferTo > (yAxisPartData.xAxisMin + yAxisPartData.xAxisMax) / 2) {
            DrawTextUtil.drawText(canvas,
                                  yAxisPartData.yAxisDesc,
                                  font,
                                  this.llx + this.partWidth,
                                  lly + partHeight + DrawTextUtil.getStringHeight((int) font.getSize()),
                                  DrawTextUtil.REFER_LEFT);
        } else {
            DrawTextUtil.drawText(canvas,
                                  yAxisPartData.yAxisDesc,
                                  font,
                                  this.llx,
                                  lly + partHeight + DrawTextUtil.getStringHeight((int) font.getSize()),
                                  DrawTextUtil.REFER_RIGHT);
        }
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            if (this.getParentPart() != null) {
                this.llx += this.getParentPart().llx;
                this.lly += this.getParentPart().lly;
            }

            YAxisPartData yAxisPartData = (YAxisPartData) basePartData;

            drawAxis(canvas, yAxisPartData);
            drawScaleDesc(canvas, yAxisPartData);
            DrawAxisDesc(canvas, yAxisPartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画出坐标轴直线
     * @param canvas 画笔
     * @param yAxisPartData y坐标轴模块数据对象
     */
    protected void drawAxis(PdfContentByte canvas, YAxisPartData yAxisPartData) {
        canvas.saveState();
        canvas.setColorStroke(this.getAxisColor());
        canvas.setLineWidth(1);

        float startX = llx
                       + (yAxisPartData.xAxisReferTo - yAxisPartData.xAxisMin)
                         / (yAxisPartData.xAxisMax - yAxisPartData.xAxisMin) * partWidth;
        float startY = lly;
        float endX   = llx
                       + (yAxisPartData.xAxisReferTo - yAxisPartData.xAxisMin)
                         / (yAxisPartData.xAxisMax - yAxisPartData.xAxisMin) * partWidth;
        float endY = lly + partHeight;

        canvas.moveTo(startX, startY);
        canvas.lineTo(endX, endY);
        canvas.stroke();
        canvas.restoreState();
    }

    /**
     * 画出坐标轴刻度
     * @param canvas 画笔
     * @param yAxisPartData y坐标轴模块数据对象
     */
    protected void drawScaleDesc(PdfContentByte canvas, YAxisPartData yAxisPartData) {
        try {
            float   chineseCharacterWidth  = FontUtils.baseFontChinese.getWidthPoint("中文", FontUtils.fontSize) / 2;
            Point[] percentageDescVertices = getScaleDescVertices(yAxisPartData.scaleDescs,
                                                                  chineseCharacterWidth,
                                                                  yAxisPartData);
            Font currentFont = new Font(FontUtils.baseFontChinese,
                                        this.getFontSize(),
                                        this.getFontStyle(),
                                        this.getFontColor());
            ColumnText columnText      = null;
            Phrase     scaleDescPhrase = null;

            for (int index = 0; index < yAxisPartData.scaleDescs.size(); index++) {
                scaleDescPhrase = new Phrase(yAxisPartData.scaleDescs.get(index), currentFont);
                columnText      = new ColumnText(canvas);
                columnText.setSimpleColumn(new Rectangle(percentageDescVertices[index * 2 + 0].x,
                                                         percentageDescVertices[index * 2 + 0].y,
                                                         percentageDescVertices[index * 2 + 1].x,
                                                         percentageDescVertices[index * 2 + 1].y));
                columnText.setUseAscender(true);
                columnText.addText(scaleDescPhrase);
                columnText.go();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File   destFile    = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document  document  = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            YAxisPart yAxisPart = new YAxisPart(new YAxisPartData(0,
                                                                  200,
                                                                  200,
                                                                  Arrays.asList("星期一",
                                                                                "星期一",
                                                                                "星期一",
                                                                                "星期一",
                                                                                "星期一",
                                                                                "星期一"),
                                                                  "新奇信息"));

            yAxisPart.setLlx(PageSize.A4.getWidth() * 0.1f);
            yAxisPart.setLly(100);
            yAxisPart.setPartWidth(PageSize.A4.getWidth() * 0.8f);
            yAxisPart.setPartHeight(200);
            yAxisPart.draw(pdfWriter.getDirectContent());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取坐标轴背景颜色
     *
     *
     * @return 坐标轴背景颜色
     */
    public BaseColor getAxisColor() {
        return (axisColor == null)
               ? ColorUtil.strRGBAToColor("0xddddddff")
               : axisColor;
    }

    /**
     * 设置坐标轴背景颜色
     *
     *
     * @param axisColor 坐标轴背景颜色
     */
    public void setAxisColor(BaseColor axisColor) {
        this.axisColor = axisColor;
    }

    /**
     * 获取坐标轴刻度字体颜色
     *
     *
     * @return 坐标轴刻度字体颜色
     */
    public BaseColor getFontColor() {
        return (fontColor == null)
               ? ColorUtil.strRGBAToColor("0x333333ff")
               : fontColor;
    }

    /**
     * 设置坐标轴刻度字体颜色
     *
     *
     * @param fontColor 坐标轴刻度字体颜色
     */
    public void setFontColor(BaseColor fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * 获取坐标轴刻度字体大小
     *
     *
     * @return 坐标轴刻度字体大小
     */
    public int getFontSize() {
        return (fontSize <= 5)
               ? 5
               : fontSize;
    }

    /**
     * 设置坐标轴刻度字体大小
     *
     *
     * @param fontSize 坐标轴刻度字体大小
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * 获取坐标轴刻度字体样式
     *
     *
     * @return 坐标轴刻度字体样式
     */
    public int getFontStyle() {
        return Font.NORMAL;
    }

    /**
     * 设置坐标轴刻度字体样式
     *
     *
     * @param fontStyle 坐标轴刻度字体样式
     */
    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     * 计算用于画坐标轴刻度的顶点数据
     *
     *
     * @param percentageDesc 刻度信息字符串列表
     * @param chineseCharacterWidth 中文字符宽度
     * @param yAxisPartData y坐标轴模块数据对象
     *
     * @return 用于画坐标轴刻度的顶点数据
     */
    protected Point[] getScaleDescVertices(List<String> percentageDesc, float chineseCharacterWidth,
                                         YAxisPartData yAxisPartData) {
        Point[] scaleDescVertices = new Point[percentageDesc.size() * 2];

        for (int index = 0; index < percentageDesc.size() * 2; index++) {
            scaleDescVertices[index] = new Point(0, 0);
        }

        if (yAxisPartData.xAxisReferTo > (yAxisPartData.xAxisMin + yAxisPartData.xAxisMax) / 2) {
            for (int index = 0; index < percentageDesc.size(); index++) {
                scaleDescVertices[index * 2 + 0].x = llx + partWidth + chineseCharacterWidth;
                scaleDescVertices[index * 2 + 0].y = lly + partHeight / (percentageDesc.size() - 1) * index
                                                     - chineseCharacterWidth / 2;
                scaleDescVertices[index * 2 + 1].x =
                    llx + partWidth + +chineseCharacterWidth
                    + FontUtils.baseFontChinese.getWidthPoint(percentageDesc.get(index),
                                                              FontUtils.fontSize);
                scaleDescVertices[index * 2 + 1].y = lly + partHeight / (percentageDesc.size() - 1) * index
                                                     + chineseCharacterWidth / 2;

                /*
                 * //如果是第一个要画的字符串(底部)
                 * if(index == 0){
                 *   scaleDescVertices[index * 2 + 0].y += chineseCharacterWidth / 2;
                 *   scaleDescVertices[index * 2 + 1].y += chineseCharacterWidth / 2;
                 * }
                 *
                 * //如果是最后一个要画的字符串(顶部)
                 * if(index == percentageDesc.size() - 1){
                 *   scaleDescVertices[index * 2 + 0].y -= chineseCharacterWidth / 2;
                 *   scaleDescVertices[index * 2 + 1].y -= chineseCharacterWidth / 2;
                 * }
                 */
            }
        } else {
            for (int index = 0; index < percentageDesc.size(); index++) {
                scaleDescVertices[index * 2 + 0].x =
                    llx - FontUtils.baseFontChinese.getWidthPoint(percentageDesc.get(index),
                                                                  FontUtils.fontSize);
                scaleDescVertices[index * 2 + 0].y = lly + partHeight / (percentageDesc.size() - 1) * index
                                                     - chineseCharacterWidth / 2;
                scaleDescVertices[index * 2 + 1].x = llx;
                scaleDescVertices[index * 2 + 1].y = lly + partHeight / (percentageDesc.size() - 1) * index
                                                     + chineseCharacterWidth / 2;

                /*
                 * //如果是第一个要画的字符串(底部)
                 * if(index == 0){
                 *   scaleDescVertices[index * 2 + 0].y += chineseCharacterWidth / 2;
                 *   scaleDescVertices[index * 2 + 1].y += chineseCharacterWidth / 2;
                 * }
                 *
                 * //如果是最后一个要画的字符串(顶部)
                 * if(index == percentageDesc.size() - 1){
                 *   scaleDescVertices[index * 2 + 0].y -= chineseCharacterWidth / 2;
                 *   scaleDescVertices[index * 2 + 1].y -= chineseCharacterWidth / 2;
                 * }
                 */
            }
        }

        return scaleDescVertices;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
