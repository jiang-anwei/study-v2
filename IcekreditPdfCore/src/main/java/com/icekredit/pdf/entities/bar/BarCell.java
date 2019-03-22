package com.icekredit.pdf.entities.bar;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.chart.SuperChart;
import com.icekredit.pdf.entities.chart.part.ItemDescPart;
import com.icekredit.pdf.entities.chart.partdata.ItemDescPartData;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * 横向条形图单元格
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class BarCell extends BaseCell implements PdfPCellEvent {

    /**
     * 条形图item总高度
     */
    protected static final float BAR_ITEM_HEIGHT = 30;

    /**
     * 条形图矩形条高度
     */
    protected static final float BAR_RECTANGLE_HEIGHT = 10;

    /**
     * x轴水平刻度描述高度
     */
    protected static final int X_AXIS_SCALE_DESC_HEIGHT = 15;

    /**
     *条形图每一个部分的描述条目高度，可以画两行part描述，每一个part描述，&lt;/br&gt;
     * 包括当前part样式、颜色、名称，com.icekredit.pdf.entities.chart.part.ItemDescPart.PartDesc
     */
    protected static final int ITEMS_DESC_HEIGHT = 25;

    /**
     *  条形图表示的最小值
     */
    protected float barValueMin;

    /**
     * 条形图能表示的最大值
     */
    protected float barValueMax;

    /**
     * 条形图多个条目每一个部分的数值大小
     */
    protected List<List<Float>> barPartsSpans;

    /**
     * 条形图每一个条目每一个部分的描述
     */
    protected List<String> barPartsDesc;

    /**
     * 条形图每一个条目每一个部分的颜色
     */
    protected List<BaseColor> barPartsColor;

    /**
     * 条形图每一个条目的描述（用于构建条形图纵坐标轴刻度）
     */
    protected List<String> barItemsDesc;

    /**
     * x轴刻度信息（用于构建条形图横坐标轴刻度）
     */
    protected List<String> barScaleDescs;

    /**
     * itext pdf pdfWriter对象
     */
    protected PdfWriter pdfWriter;

    /**
     * 条形图每一个总条目的高度
     */
    protected float barItemHeight;

    /**
     * 条形图是否按照百分比100%来展示。如果按100%来展示，那么每一部分数值将被换算成相对total的百分比
     */
    protected boolean isShownAsPercentage;

    /**
     * 构造函数，本构造函数提供较少的参数，部分参数由默认值提供
     *
     * @param pdfWriter     itext pdf pdfWriter对象
     * @param barValueMin   条形图表示的最小值
     * @param barValueMax   条形图能表示的最大值
     * @param barPartsSpan  条形图多个条目每一个部分的数值大小
     * @param barPartsDesc  条形图每一个条目每一个部分的描述
     * @param barPartsColor 条形图每一个条目每一个部分的颜色
     * @param barItemsDesc  条形图每一个条目的描述（用于构建条形图纵坐标轴刻度）
     * @param barScaleDescs x轴刻度信息（用于构建条形图横坐标轴刻度）
     */
    public BarCell(PdfWriter pdfWriter, float barValueMin, float barValueMax, List<List<Float>> barPartsSpan,
                   List<String> barPartsDesc, List<BaseColor> barPartsColor, List<String> barItemsDesc,
                   List<String> barScaleDescs) {
        this(pdfWriter,
                barValueMin,
                barValueMax,
                barPartsSpan,
                barPartsDesc,
                barPartsColor,
                barItemsDesc,
                barScaleDescs,
                BAR_ITEM_HEIGHT,
                true);
    }

    /**
     * CONSTRUCTOR_DESCRIPTION
     *
     * @param pdfWriter           itext pdf pdfWriter对象
     * @param barValueMin         条形图表示的最小值
     * @param barValueMax         条形图能表示的最大值
     * @param barPartsSpan        条形图多个条目每一个部分的数值大小
     * @param barPartsDesc        条形图每一个条目每一个部分的描述
     * @param barPartsColor       条形图每一个条目每一个部分的颜色
     * @param barItemsDesc        条形图每一个条目的描述（用于构建条形图纵坐标轴刻度）
     * @param barScaleDescs       x轴刻度信息（用于构建条形图横坐标轴刻度）
     * @param barItemHeight       条形图每一个条目的高度
     * @param isShownAsPercentage 是否按照百分比100%来展示。如果按100%来展示，那么每一部分数值将被换算成相对total的百分比
     */
    public BarCell(PdfWriter pdfWriter, float barValueMin, float barValueMax, List<List<Float>> barPartsSpan,
                   List<String> barPartsDesc, List<BaseColor> barPartsColor, List<String> barItemsDesc,
                   List<String> barScaleDescs, float barItemHeight, boolean isShownAsPercentage) {
        this.pdfWriter = pdfWriter;
        this.barValueMin = barValueMin;
        this.barValueMax = barValueMax;
        this.barPartsSpans = barPartsSpan;
        this.barPartsDesc = barPartsDesc;
        this.barPartsColor = barPartsColor;
        this.barItemsDesc = barItemsDesc;
        this.barScaleDescs = barScaleDescs;
        this.barItemHeight = barItemHeight;
        this.isShownAsPercentage = isShownAsPercentage;
        this.setFixedHeight(barPartsSpan.size() * barItemHeight + X_AXIS_SCALE_DESC_HEIGHT + ITEMS_DESC_HEIGHT);
        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        float chartWidth = position.getWidth();
        float chartHeight = position.getHeight();
        float barPanelWidth = chartWidth * 0.8f;
        float barPanelHeight = chartHeight - (X_AXIS_SCALE_DESC_HEIGHT + ITEMS_DESC_HEIGHT);
        float barPanelLLXOffset = position.getLeft() + chartWidth * 0.15f;
        float barPanelLLYOffset = position.getBottom() + X_AXIS_SCALE_DESC_HEIGHT + ITEMS_DESC_HEIGHT;

        drawCoordinateSystem(canvases[PdfPTable.BASECANVAS],
                barPanelLLXOffset,
                barPanelLLYOffset,
                barPanelWidth,
                barPanelHeight);
        drawBars(canvases[PdfPTable.BASECANVAS], barPanelLLXOffset, barPanelLLYOffset, barPanelWidth, barPanelHeight);
        drawBarItemsDesc(canvases[PdfPTable.BASECANVAS],
                barPanelLLXOffset,
                barPanelLLYOffset,
                barPanelWidth,
                barPanelHeight);
        drawBarScalesDesc(canvases[PdfPTable.BASECANVAS],
                barPanelLLXOffset,
                barPanelLLYOffset,
                barPanelWidth,
                barPanelHeight);

        SuperChart parentPart = new SuperChart();
        float llx = position.getLeft();
        float lly = position.getBottom();
        float width = position.getWidth();
        float height = position.getHeight();

        parentPart.setLlx(llx);
        parentPart.setLly(lly);
        parentPart.setPartWidth(width);
        parentPart.setPartHeight(height);

        List<ItemDescPart.PartDesc> partDescs = new ArrayList<ItemDescPart.PartDesc>();
        ItemDescPart.PartDesc partDesc = null;
        int index = 0;

        for (String barPartDesc : barPartsDesc) {
            partDesc = new ItemDescPart.PartDesc(ItemDescPart.PartDesc.PART_TYPE_PILLAR,
                    barPartsColor.get(index),
                    barPartDesc);
            partDescs.add(partDesc);
            index++;
        }

        ItemDescPartData itemDescPartData = new ItemDescPartData(partDescs);
        ItemDescPart itemDescPart = new ItemDescPart(itemDescPartData);

        itemDescPart.setLlx(itemDescPartData.getPositionRect().getLeft() * width / 100.0f);
        itemDescPart.setLly(itemDescPartData.getPositionRect().getBottom() * height / 100.0f);
        itemDescPart.setPartWidth(width * itemDescPartData.getPositionRect().getWidth() / 100.0f);
        itemDescPart.setPartHeight(ITEMS_DESC_HEIGHT);
        parentPart.appendPart(itemDescPart);
        parentPart.draw(canvases[PdfPTable.BASECANVAS]);
    }

    /**
     * 画每一个条形图条目的描述信息(Y轴对应每个条形图条目的描述)
     *
     * @param canvas            画笔
     * @param barPanelLLXOffset 条形图主面板的x方向偏移
     * @param barPanelLLYOffset 条形图主面板的y方向偏移
     * @param barPanelWidth     条形图主面板的宽度
     * @param barPanelHeight    条形图主面板的高度
     */
    protected void drawBarItemsDesc(PdfContentByte canvas, float barPanelLLXOffset, float barPanelLLYOffset,
                                    float barPanelWidth, float barPanelHeight) {
        int position = 0;
        Font currentFont = new Font(FontUtils.baseFontChinese, 8, Font.NORMAL, new BaseColor(0xaa, 0xaa, 0xaa, 0xff));

        for (String barItemDesc : barItemsDesc) {
            ColumnText strColumnText = new ColumnText(canvas);
            float width = FontUtils.baseFontChinese.getWidthPoint(barItemDesc, currentFont.getSize());

            try {
                strColumnText.setSimpleColumn(new Phrase(new Chunk(barItemDesc, currentFont)),
                        barPanelLLXOffset - width - 10,
                        barPanelLLYOffset + barItemHeight * position + barItemHeight / 2
                                - DrawTextUtil.getStringHeight((int) currentFont.getSize()) / 2,
                        barPanelLLXOffset,
                        barPanelLLYOffset + barItemHeight * position + barItemHeight / 2
                                - DrawTextUtil.getStringHeight((int) currentFont.getSize()) / 2
                                + DrawTextUtil.getStringHeight((int) currentFont.getSize()) / 8,
                        0,
                        ALIGN_CENTER);
                strColumnText.go();
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * DrawTextUtil.drawText(canvas,barItemDesc,currentFont,
             *       barPanelLLXOffset,
             *       barPanelLLYOffset + barItemHeight * position + barItemHeight / 2 ,DrawTextUtil.REFER_RIGHT);
             */
            position++;
        }
    }

    /**
     * 画条形图x轴的刻度信息
     *
     * @param canvas            画笔
     * @param barPanelLLXOffset 条形图主面板的x方向偏移
     * @param barPanelLLYOffset 条形图主面板的y方向偏移
     * @param barPanelWidth     条形图主面板的宽度
     * @param barPanelHeight    条形图主面板的高度
     */
    protected void drawBarScalesDesc(PdfContentByte canvas, float barPanelLLXOffset, float barPanelLLYOffset,
                                     float barPanelWidth, float barPanelHeight) {
        int position = 0;
        Font currentFont = new Font(FontUtils.baseFontChinese, 8, Font.NORMAL, new BaseColor(0xaa, 0xaa, 0xaa, 0xff));

        for (String barScaleDesc : barScaleDescs) {
            ColumnText strColumnText = new ColumnText(canvas);
            float width = FontUtils.baseFontChinese.getWidthPoint(barScaleDesc, currentFont.getSize());
            float blockWidth = barPanelWidth / (barScaleDescs.size() - 1);

            try {
                strColumnText.setSimpleColumn(new Phrase(new Chunk(barScaleDesc, currentFont)),
                        barPanelLLXOffset + position * blockWidth - width / 3 * 2,
                        barPanelLLYOffset - barItemHeight / 2,
                        barPanelLLXOffset + position * blockWidth + width / 3 * 2,
                        barPanelLLYOffset - barItemHeight / 2,
                        0,
                        ALIGN_CENTER);
                strColumnText.go();
            } catch (Exception e) {
                e.printStackTrace();
            }

            position++;
        }
    }

    /**
     * 画条形图的每一个条
     *
     * @param canvas            画笔
     * @param barPanelLLXOffset 条形图主面板的x方向偏移
     * @param barPanelLLYOffset 条形图主面板的y方向偏移
     * @param barPanelWidth     条形图主面板的宽度
     * @param barPanelHeight    条形图主面板的高度
     */
    protected void drawBars(PdfContentByte canvas, float barPanelLLXOffset, float barPanelLLYOffset,
                            float barPanelWidth, float barPanelHeight) {
        float total = 0;
        int position = 0;

        for (List<Float> barPartsSpan : barPartsSpans) {
            if (!isShownAsPercentage) {
                total = barValueMax - barValueMin;
            } else {
                total = 0;

                for (Float value : barPartsSpan) {
                    total += value;
                }
            }

            if (total <= 0) {
                total = 1;
            }

            float partsDrawn = 0;
            int index = 0;

            for (Float value : barPartsSpan) {
                canvas.saveState();
                canvas.setLineWidth(0);
                canvas.setColorStroke(barPartsColor.get(index));
                canvas.setColorFill(barPartsColor.get(index));
                canvas.rectangle(barPanelLLXOffset + partsDrawn / total * barPanelWidth,
                        barPanelLLYOffset + barItemHeight * position + barItemHeight / 2
                                - BAR_RECTANGLE_HEIGHT / 2,
                        value / total * barPanelWidth,
                        BAR_RECTANGLE_HEIGHT);
                canvas.fillStroke();
                canvas.restoreState();
                partsDrawn += value;
                index++;
            }

            position++;
        }
    }

    /**
     * 画条形图的坐标轴
     *
     * @param canvas            画笔
     * @param barPanelLLXOffset 条形图主面板的x方向偏移
     * @param barPanelLLYOffset 条形图主面板的y方向偏移
     * @param barPanelWidth     条形图主面板的宽度
     * @param barPanelHeight    条形图主面板的高度
     */
    protected void drawCoordinateSystem(PdfContentByte canvas, float barPanelLLXOffset, float barPanelLLYOffset,
                                        float barPanelWidth, float barPanelHeight) {
        canvas.saveState();
        canvas.setLineWidth(1f);
        canvas.setColorStroke(ColorUtil.strRGBAToColor("0x999999ff"));
        canvas.moveTo(barPanelLLXOffset, barPanelLLYOffset);
        canvas.lineTo(barPanelLLXOffset + barPanelWidth, barPanelLLYOffset);
        canvas.stroke();
        canvas.moveTo(barPanelLLXOffset, barPanelLLYOffset);
        canvas.lineTo(barPanelLLXOffset, barPanelLLYOffset + barPanelHeight);
        canvas.stroke();
        canvas.restoreState();
    }

    public static void main(String[] args) {
        String destStr = "results/test.pdf";
        File destFile = new File(destStr);

        destFile.getParentFile().mkdirs();

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);

            mainFrame.setWidthPercentage(100);

            Float[] datas = new Float[]{new Float(10), new Float(20), new Float(20),
                    new Float(20)};
            List<List<Float>> barPartsDatas = new ArrayList<List<Float>>();

            for (int index = 0; index < 5; index++) {
                barPartsDatas.add(Arrays.asList(datas));
            }

            BarCell barCell = new BarCell(pdfWriter, 0, 100, barPartsDatas, Arrays.asList(new String[]{"第一部分", "第二部分", "第三部分", "第四部分", "第五部分"}), Arrays.asList(new BaseColor[]{
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[0]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[1]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[2]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[3]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[4])}), Arrays.asList(new String[]{"2015/06", "2015/06", "2015/06", "2015/06",
                    "2015/06"}), Arrays.asList(new String[]{
                    "0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"
            }), 20, true);

            barCell.setColspan(12);
            mainFrame.addCell(barCell);
            document.add(mainFrame);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
