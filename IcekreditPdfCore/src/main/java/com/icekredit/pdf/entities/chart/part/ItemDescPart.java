package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.ItemDescPartData;
import com.icekredit.pdf.entities.line_chart.Line;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 图表各个模块中出现的条目描述信息图表模块，主要包括柱状图每个圆柱的表示/线图每条线的表示/饼图每个扇区的表示
 * 在进行每个条目的勾画时，都是按照从左向右从上往下的策略来放置每一个条目，
 * 放置之前会检测当前行是否有足够空间，因为可能出现描述字符串比较长的情况，如果
 * 空间足够那么放置这个描述信息，然后更新呢currentXOffset用于下一次计算，
 * 如果空间不够那么将行数加一，也就是在当前位置下方另起一行来画剩余部分，此时更新
 * currentXOffset=0,currentYOffset为currentYOffset - 行高
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class ItemDescPart extends BasePart {

    /**
     * 构造函数
     *
     */
    public ItemDescPart() {}

    /**
     * 构造函数
     *
     *
     * @param basePartData 与条目描述信息图表模块关联的条目描述信息图表模块数据对象
     */
    public ItemDescPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 条目描述信息图表模块左下角的x坐标
     * @param lly 条目描述信息图表模块左下角y坐标
     * @param partWidth 条目描述信息图表模块宽度
     * @param partHeight 条目描述信息图表模块高度
     * @param basePartData 条目描述信息图表模块关联的条目描述信息图表模块数据对象
     */
    public ItemDescPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 条目描述信息图表模块左下角的x坐标
     * @param lly 条目描述信息图表模块左下角y坐标
     * @param partWidth 条目描述信息图表模块宽度
     * @param partHeight 条目描述信息图表模块高度
     * @param basePartData 条目描述信息图表模块关联的条目描述信息图表模块数据对象
     * @param parentPart 条目描述信息图表模块依附的父模块
     * @param subParts 条目描述信息图表模块关联的附属的字模块
     */
    public ItemDescPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                        BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            if (this.getParentPart() != null) {
                this.llx += this.getParentPart().llx;
                this.lly += this.getParentPart().lly;
            }

            ItemDescPartData itemDescPartData = (ItemDescPartData) basePartData;

            drawPartDesc(canvas, itemDescPartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为线图的描述信息条目进行展示
     *
     * @param canvas 画笔
     * @param currentXOffset 当前x轴的可用空间位置
     * @param currentYOffset 当前y轴的可用空间位置
     * @param partDescHeight 描述条目占据的行高
     * @param marginLeft 左边距
     * @param marginRight 右边距
     * @param partDesc 描述字符串
     * @return 更新后的新的x轴的可用空间位置
     */
    protected float drawLineDesc(PdfContentByte canvas, float currentXOffset, float currentYOffset, float partDescHeight,
                               float marginLeft, float marginRight, PartDesc partDesc) {
        currentXOffset += marginLeft;

        Line line = new Line(canvas,
                             new Point[] { new Point(currentXOffset,
                                                     currentYOffset + partDescHeight / 2),
                                           new Point(currentXOffset + PartDesc.PART_DESC_CHART_WIDTH,
                                                     currentYOffset + partDescHeight / 2) },
                             10,
                             0,
                             1,
                             partDesc.partColor,
                             true,
                             Line.NODE_SHOWN_AS_CIRCLE);

        line.draw();
        currentXOffset += PartDesc.PART_DESC_CHART_WIDTH;
        currentXOffset += marginLeft;
        drawPartDescText(canvas, currentXOffset, currentYOffset, partDescHeight, partDesc);
        currentXOffset += FontUtils.baseFontChinese.getWidthPoint(partDesc.partDesc, partDesc.partDescFontSize);
        currentXOffset += marginRight;

        return currentXOffset;
    }

    /**
     * 画一个描述条目对象
     *
     *
     * @param canvas 画笔
     * @param itemDescPartData 描述条目对象
     */
    protected void drawPartDesc(PdfContentByte canvas, ItemDescPartData itemDescPartData) {
        try {
            float marginTop      = 2;
            float marginBottom   = 2;
            float marginLeft     = 2;
            float marginRight    = 2;
            float partDescHeight = DrawTextUtil.getStringHeight(8);
            float currentXOffset = llx;
            float currentYOffset = lly + partHeight - marginTop - partDescHeight;
            float partDescWidth  = 0;

            for (PartDesc partDesc : itemDescPartData.partsDescList) {
                partDescWidth = getPartDescWidth(partDesc, marginLeft, marginRight);

                if ((partWidth - (currentXOffset - llx)) < partDescWidth) {

                    // 如果剩余空间不够画一个partDesc，那么重启一行
                    currentYOffset -= marginBottom;
                    currentYOffset -= marginTop;
                    currentYOffset -= partDescHeight;
                    currentXOffset = llx;
                }

                switch (partDesc.partType) {
                case PartDesc.PART_TYPE_PILLAR :
                    currentXOffset = drawPillarDesc(canvas,
                                                    currentXOffset,
                                                    currentYOffset,
                                                    partDescHeight,
                                                    marginLeft,
                                                    marginRight,
                                                    partDesc);

                    break;

                case PartDesc.PART_TYPE_LINE :
                    currentXOffset = drawLineDesc(canvas,
                                                  currentXOffset,
                                                  currentYOffset,
                                                  partDescHeight,
                                                  marginLeft,
                                                  marginRight,
                                                  partDesc);

                    break;

                case PartDesc.PART_TYPE_SECTOR :
                    currentXOffset = drawSectorDesc(canvas,
                                                    currentXOffset,
                                                    currentYOffset,
                                                    partDescHeight,
                                                    marginLeft,
                                                    marginRight,
                                                    partDesc);

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画一个描述条目对象文本
     *
     *
     * @param canvas 画笔
     * @param currentXOffset 当前x轴的可用空间位置
     * @param currentYOffset 当前y轴的可用空间位置
     * @param partDescHeight 描述条目占据的行高
     * @param partDesc 具体描述信息
     */
    protected void drawPartDescText(PdfContentByte canvas, float currentXOffset, float currentYOffset,
                                  float partDescHeight, PartDesc partDesc) {
        try {
            Phrase phrase = new Phrase(new Chunk(partDesc.partDesc,
                                                 new Font(FontUtils.baseFontChinese,
                                                          partDesc.partDescFontSize,
                                                          partDesc.partDescFontStyle,
                                                          partDesc.partDescFontColor)));
            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(new Rectangle(currentXOffset,
                                                     currentYOffset + partDescHeight / 2
                                                     - DrawTextUtil.getStringHeight(partDesc.partDescFontSize) / 2,
                                                     currentXOffset
                                                     + FontUtils.baseFontChinese.getWidthPoint(partDesc.partDesc,
                                                                                               partDesc.partDescFontSize) + 1,
                                                     currentYOffset + partDescHeight / 2
                                                     + DrawTextUtil.getStringHeight(partDesc.partDescFontSize) / 2));
            columnText.setUseAscender(true);
            columnText.addText(phrase);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画圆柱描述条目
     *
     * @param canvas 画笔
     * @param currentXOffset 当前x轴的可用空间位置
     * @param currentYOffset 当前y轴的可用空间位置
     * @param partDescHeight 描述条目占据的行高
     * @param marginLeft 左边距
     * @param marginRight 右边距
     * @param partDesc 描述字符串
     * @return 更新后的新的x轴的可用空间位置
     */
    protected float drawPillarDesc(PdfContentByte canvas, float currentXOffset, float currentYOffset,
                                 float partDescHeight, float marginLeft, float marginRight, PartDesc partDesc) {
        currentXOffset += marginLeft;
        canvas.saveState();
        canvas.setLineWidth(0);
        canvas.setColorStroke(partDesc.partColor);
        canvas.setColorFill(partDesc.partColor);
        canvas.rectangle(currentXOffset, currentYOffset, PartDesc.PART_DESC_CHART_WIDTH, partDescHeight);
        canvas.fillStroke();
        canvas.restoreState();
        currentXOffset += PartDesc.PART_DESC_CHART_WIDTH;
        currentXOffset += marginLeft;
        drawPartDescText(canvas, currentXOffset, currentYOffset, partDescHeight, partDesc);
        currentXOffset += FontUtils.baseFontChinese.getWidthPoint(partDesc.partDesc, partDesc.partDescFontSize);
        currentXOffset += marginRight;

        return currentXOffset;
    }

    /**
     * 画扇形区域米描述条目
     *
     * @param canvas 画笔
     * @param currentXOffset 当前x轴的可用空间位置
     * @param currentYOffset 当前y轴的可用空间位置
     * @param partDescHeight 描述条目占据的行高
     * @param marginLeft 左边距
     * @param marginRight 右边距
     * @param partDesc 描述字符串
     * @return 更新后的新的x轴的可用空间位置
     */
    protected float drawSectorDesc(PdfContentByte canvas, float currentXOffset, float currentYOffset,
                                 float partDescHeight, float marginLeft, float marginRight, PartDesc partDesc) {
        currentXOffset += marginLeft;
        canvas.saveState();
        canvas.setLineWidth(0);
        canvas.setColorStroke(partDesc.partColor);
        canvas.setColorFill(partDesc.partColor);
        canvas.rectangle(currentXOffset, currentYOffset, PartDesc.PART_DESC_CHART_WIDTH, partDescHeight);
        canvas.fillStroke();
        canvas.restoreState();
        currentXOffset += PartDesc.PART_DESC_CHART_WIDTH;
        currentXOffset += marginLeft;
        drawPartDescText(canvas, currentXOffset, currentYOffset, partDescHeight, partDesc);
        currentXOffset += FontUtils.baseFontChinese.getWidthPoint(partDesc.partDesc, partDesc.partDescFontSize);
        currentXOffset += marginRight;

        return currentXOffset;
    }

    /**
     * 画一个扇区标识符
     *
     *
     * @param canvas 画笔
     */
    protected void drawSectors(PdfContentByte canvas) {
        float     currentSectorAngleOffset = 0;    // (float) (Math.PI / 2) * 0;
        float     currentSectorAngle;
        BaseColor currentSectorBGColor;
        Point[]   sectorVertices = null;

        canvas.saveState();
        canvas.setLineWidth(0.1);
        canvas.setColorStroke(new BaseColor(0xee, 0xee, 0xee, 0xff));

        // 画出表示比例的扇区
        for (int index = 0; index < 1; index++) {
            currentSectorAngle   = 0;
            currentSectorBGColor = null;
            sectorVertices       = getSectorVertices(currentSectorAngleOffset, currentSectorAngle, 0, 0, 0);
            canvas.setColorFill(currentSectorBGColor);
            canvas.moveTo(sectorVertices[0].x, sectorVertices[0].y);

            for (int position = 1; position < sectorVertices.length; position++) {
                canvas.lineTo(sectorVertices[position].x, sectorVertices[position].y);
            }

            canvas.closePathFillStroke();
            currentSectorAngleOffset += currentSectorAngle;
        }

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

            List<PartDesc> partDescs = new ArrayList<PartDesc>();
            PartDesc       partDesc  = null;

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_PILLAR,
                                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                                        "张三");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_LINE,
                                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                                        "张三不知里斯");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_SECTOR,
                                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                                        "张三里斯里斯");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_PILLAR,
                                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                                        "张三里斯");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_LINE,
                                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                                        "line one");
                partDescs.add(partDesc);
            }

            ItemDescPart itemDescPart = new ItemDescPart(new ItemDescPartData(partDescs));

            itemDescPart.setLlx(36);
            itemDescPart.setLly(36);
            itemDescPart.setPartWidth(PageSize.A4.getWidth() - 72);
            itemDescPart.setPartHeight(100);
            itemDescPart.draw(pdfWriter.getDirectContent());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个描述条目的宽度
     *
     *
     * @param partDesc 描述条目对象，包括描述标识符的类型线或者圆或者矩形或者扇区等 以及对应的颜色 以及描述字符串
     * @param marginLeft PARAM_DESC 左边距
     * @param marginRight PARAM_DESC 右边距
     *
     * @return identifier 消耗的宽度加上描述字符串消耗的宽度加上margin
     */
    protected float getPartDescWidth(PartDesc partDesc, float marginLeft, float marginRight) {
        return marginLeft + PartDesc.PART_DESC_CHART_WIDTH + marginLeft
               + FontUtils.baseFontChinese.getWidthPoint(partDesc.partDesc,
                                                         partDesc.partDescFontSize) + marginRight;
    }

    /**
     * 获取画一个扇区需要用到的顶点信息
     *
     *
     * @param currentSectorAngleOffset 开始角度
     * @param currentSectorAngle 跨越角度
     * @param outerCircleRadius 半径
     * @param centerXPosition 圆心x坐标
     * @param centerYPosition 圆心y坐标
     *
     * @return 计算后得到的顶点数组
     */
    protected Point[] getSectorVertices(float currentSectorAngleOffset, float currentSectorAngle,
                                      float outerCircleRadius, float centerXPosition, float centerYPosition) {

        // 计算出画当前扇区需要的顶点的数目
        int     sectorVerticesCount = Math.round(currentSectorAngle / 360 * 72 + 0.5f) + 1 + 1;
        Point[] sectorVertices      = new Point[sectorVerticesCount];

        // 初始化
        for (int index = 0; index < sectorVerticesCount; index++) {
            sectorVertices[index] = new Point(0, 0);
        }

        sectorVertices[0].x = centerXPosition;
        sectorVertices[0].y = centerYPosition;

        for (int index = 1; index < sectorVerticesCount; index++) {
            sectorVertices[index].x = (float) (centerXPosition
                                               + outerCircleRadius
                                                 * Math.cos(currentSectorAngleOffset * Math.PI * 2 / 360
                                                            + (index - 1) * 1.0f / (sectorVerticesCount - 2)
                                                              * currentSectorAngle * Math.PI * 2 / 360));
            sectorVertices[index].y = (float) (centerYPosition
                                               + outerCircleRadius
                                                 * Math.sin(currentSectorAngleOffset * Math.PI * 2 / 360
                                                            + (index - 1) * 1.0f / (sectorVerticesCount - 2)
                                                              * currentSectorAngle * Math.PI * 2 / 360));
        }

        return sectorVertices;
    }

    /**
     * 内部类，用来表示每一个描述条目，如圆柱描述条目/线描述条目/扇区描述条目
     *
     *
     * @version        1.0, 16/10/28
     * @author         wenchao
     */
    public static class PartDesc {
        /** 描述条目为圆柱类型 */
        public static final int PART_TYPE_PILLAR = 0;

        /** 描述条目为线条类型 */
        public static final int PART_TYPE_LINE = 1;

        /** 描述条目为扇区类型 */
        public static final int PART_TYPE_SECTOR = 2;

        /** 默认的描述条目展示的字体大小 */
        public static final int DEFAULT_FONT_SIZE = 6;

        /** 默认的描述条目展示的字体样式 */
        public static final int DEFAULT_FONT_STYLE = Font.NORMAL;

        /** 默认的描述条目展示的字体颜色 */
        public static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0x33, 0x33, 0x33, 0xff);

        /** 描述条目标识符的宽度 */
        public static final float PART_DESC_CHART_WIDTH = 15;

        /** 描述条目类型 */
        public int partType;

        /** 描述条目标识符颜色 */
        public BaseColor partColor;

        /** 描述条目描述字符串 */
        public String partDesc;

        /** 描述条目描述字符串字体大小 */
        public int partDescFontSize;

        /** 描述条目描述字符串字体样式 */
        public int partDescFontStyle;

        /** 描述条目描述字符串字体颜色 */
        public BaseColor partDescFontColor;

        /**
         * 构造函数，本构造函数只提供部分参数其他参数由默认值提供
         *
         *
         * @param partType 描述条目类型
         * @param partColor 描述条目标识符颜色
         * @param partDesc 描述条目描述字符串
         */
        public PartDesc(int partType, BaseColor partColor, String partDesc) {
            this(partType, partColor, partDesc, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE, DEFAULT_FONT_COLOR);
        }

        /**
         * CONSTRUCTOR_DESCRIPTION
         *
         *
         * @param partType 描述条目类型
         * @param partColor 描述条目标识符颜色
         * @param partDesc 描述条目描述字符串
         * @param partDescFontSize 描述条目描述字符串字体大小
         * @param partDescFontStyle 描述条目描述字符串字体样式
         * @param partDescFontColor 描述条目描述字符串字体颜色
         */
        public PartDesc(int partType, BaseColor partColor, String partDesc, int partDescFontSize,
                        int partDescFontStyle, BaseColor partDescFontColor) {
            this.partType          = partType;
            this.partColor         = partColor;
            this.partDesc          = partDesc;
            this.partDescFontSize  = partDescFontSize;
            this.partDescFontStyle = partDescFontStyle;
            this.partDescFontColor = partDescFontColor;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
