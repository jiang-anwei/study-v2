package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.Arrays;
import java.util.List;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.XAxisPartData;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 图表x轴模块类
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class XAxisPart extends BasePart {

    /** 坐标轴颜色 */
    protected BaseColor axisColor;

    /** 坐标轴字体大小 */
    protected int fontSize;

    /** 坐标轴字体样式 */
    protected int fontStyle;

    /** 坐标轴字体颜色 */
    protected BaseColor fontColor;

    /**
     * 构造函数
     *
     */
    public XAxisPart() {}

    /**
     * 构造函数
     *
     *
     * @param basePartData x轴模块数据对象
     */
    public XAxisPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 图表x轴模块左下角x位置
     * @param lly 图表x轴模块左下角y位置
     * @param partWidth 图表x轴模块宽度
     * @param partHeight 图表x轴模块高度
     * @param basePartData 图表x轴模块数据对象
     */
    public XAxisPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 图表x轴模块左下角x位置
     * @param lly 图表x轴模块左下角y位置
     * @param partWidth 图表x轴模块宽度
     * @param partHeight 图表x轴模块高度
     * @param basePartData 图表x轴模块数据对象
     * @param parentPart 当前图表x轴模块依附的父模块
     * @param subParts 当前图表x轴模块附属的子模块
     */
    public XAxisPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                     BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        if (this.getParentPart() != null) {
            this.llx += this.getParentPart().llx;
            this.lly += this.getParentPart().lly;
        }

        XAxisPartData xAxisPartData = (XAxisPartData) basePartData;

        drawXAxis(canvas, xAxisPartData);
        drawScaleDesc(canvas, xAxisPartData);
    }

    /**
     * 画X轴的刻度描述
     * 
     * @param canvas 画笔
     * @param xAxisPartData 图表x轴模块数据对象
     */
    protected void drawScaleDesc(PdfContentByte canvas, XAxisPartData xAxisPartData) {
        try {
            canvas.saveState();

            Font currentFont = new Font(FontUtils.baseFontChinese,
                                        this.getFontSize(),
                                        this.getFontStyle(),
                                        this.getFontColor());
            float   chineseCharacterWidth   = FontUtils.baseFontChinese.getWidthPoint("中文", currentFont.getSize()) / 2;
            Point[] scaleDescCenterVertices = getScaleDescCenterVertices(xAxisPartData.scaleDescs.size(),
                                                                         chineseCharacterWidth);
            float      pillarGroupDescWidth;
            Phrase     percentageDescPhrase = null;
            ColumnText columnText           = null;

            for (int index = 0; index < xAxisPartData.scaleDescs.size(); index++) {
                pillarGroupDescWidth = FontUtils.baseFontChinese.getWidthPoint(xAxisPartData.scaleDescs.get(index),
                                                                               currentFont.getSize() + 2);
                percentageDescPhrase = new Phrase(xAxisPartData.scaleDescs.get(index), currentFont);

                /*
                 * columnText = new ColumnText(canvas);
                 * columnText.setSimpleColumn(new Rectangle(scaleDescCenterVertices[index].x - pillarGroupDescWidth / 2,
                 *       scaleDescCenterVertices[index].y - chineseCharacterWidth / 2,
                 *       scaleDescCenterVertices[index].x + pillarGroupDescWidth / 2,
                 *       scaleDescCenterVertices[index].y + chineseCharacterWidth / 2));
                 *
                 * columnText.setUseAscender(true);
                 * columnText.addText(percentageDescPhrase);
                 */

                // 如果旋转角度等于0,那么就在当前位置水平画这个字符串，否则按照旋转角度在当前位置下方
                // pillarGroupDescWidth / 2 * Math.sin(xAxisPartData.scaleDescShownAngle * Math.PI * 2 / 360)
                // 的位置画这个字符串
                ColumnText.showTextAligned(canvas,
                                           Element.ALIGN_CENTER,
                                           percentageDescPhrase,
                                           scaleDescCenterVertices[index].x,
                                           (float) (scaleDescCenterVertices[index].y - chineseCharacterWidth / 4
                                                    - pillarGroupDescWidth / 2
                                                      * Math.abs(Math.sin(xAxisPartData.scaleDescShownAngle * Math.PI
                                                                          * 2 / 360))),
                                           xAxisPartData.scaleDescShownAngle);

//              columnText.go();
            }

            canvas.restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画x轴
     * 
     * @param canvas 画笔
     * @param xAxisPartData 图表x轴模块数据对象
     */
    protected void drawXAxis(PdfContentByte canvas, XAxisPartData xAxisPartData) {
        canvas.saveState();
        canvas.setColorStroke(this.getAxisColor());
        canvas.setLineWidth(1);

        float startX = llx;
        float startY = lly
                       + (xAxisPartData.yAxisReferTo - xAxisPartData.yAxisMin)
                         / (xAxisPartData.yAxisMax - xAxisPartData.yAxisMin) * partHeight;
        float endX = llx + partWidth;
        float endY = lly
                     + (xAxisPartData.yAxisReferTo - xAxisPartData.yAxisMin)
                       / (xAxisPartData.yAxisMax - xAxisPartData.yAxisMin) * partHeight;

        canvas.moveTo(startX, startY);
        canvas.lineTo(endX, endY);
        canvas.stroke();
        canvas.restoreState();
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File   destFile    = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document  document  = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            BasePart xAxisPart = new XAxisPart(new XAxisPartData(0,
                                                                 200,
                                                                 0,
                                                                 Arrays.asList("星期一",
                                                                               "星期一",
                                                                               "星期一",
                                                                               "星期一",
                                                                               "星期一",
                                                                               "星期一"),
                                                                 270));

            xAxisPart.setLlx(0);
            xAxisPart.setLly(100);
            xAxisPart.setPartWidth(300);
            xAxisPart.setPartHeight(200);
            xAxisPart.draw(pdfWriter.getDirectContent());
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
     * 获取坐标轴字体颜色
     *
     *
     * @return 坐标轴字体颜色
     */
    public BaseColor getFontColor() {
        return (fontColor == null)
               ? ColorUtil.strRGBAToColor("0x333333ff")
               : fontColor;
    }

    /**
     * 设置坐标轴字体颜色
     *
     *
     * @param fontColor 坐标轴字体颜色
     */
    public void setFontColor(BaseColor fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * 获取坐标轴字体大小
     *
     *
     * @return 坐标轴字体大小
     */
    public int getFontSize() {
        return (fontSize <= 5)
               ? 5
               : fontSize;
    }

    /**
     * 设置坐标轴字体大小
     *
     *
     * @param fontSize 坐标轴字体大小
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * 获取坐标轴字体样式
     *
     *
     * @return 坐标轴字体样式
     */
    public int getFontStyle() {
        return Font.NORMAL;
    }

    /**
     * 设置坐标轴字体样式
     *
     *
     * @param fontStyle 坐标轴字体样式
     */
    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     * 获取x轴刻度描述信息中点坐标位置
     *
     *
     * @param pillarGroupCount 圆柱总组数
     * @param chineseCharacterWidth 中文字符的宽度
     *
     * @return 计算后得到的x轴刻度描述信息中点坐标位置x
     */
    protected Point[] getScaleDescCenterVertices(int pillarGroupCount, float chineseCharacterWidth) {
        Point[] pillarsGroupDescCenterVertices;

        pillarsGroupDescCenterVertices = new Point[pillarGroupCount];

        for (int index = 0; index < pillarGroupCount; index++) {
            pillarsGroupDescCenterVertices[index] = new Point(0, 0);
        }

        for (int index = 0; index < pillarGroupCount; index++) {
            pillarsGroupDescCenterVertices[index].x = llx + partWidth * (index + 0.5f) / pillarGroupCount;
            pillarsGroupDescCenterVertices[index].y = lly - chineseCharacterWidth;
        }

        return pillarsGroupDescCenterVertices;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
