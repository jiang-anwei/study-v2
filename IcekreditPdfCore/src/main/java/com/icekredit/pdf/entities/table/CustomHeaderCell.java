package com.icekredit.pdf.entities.table;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.View;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 7/24/16.
 *
 * 冰鉴分数单元格
 */
public class CustomHeaderCell extends BaseCell implements PdfPCellEvent{
    protected String headerName;
    protected int headerNameSize;
    private static final int DEFAULT_SCORE_VALUE_SIZE = 12;
    protected int headerNameStyle;
    private static final int DEFAULT_SCORE_VALUE_STYLE = Font.NORMAL;
    protected BaseColor headerNameColor;
    private static final BaseColor DEFAULT_SCORE_VALUE_COLOR = new BaseColor(0x72,0x71,0x71,0xff);

    protected String subHeaderName;
    protected int subHeaderNameSize;
    private static final int DEFAULT_SCORE_DESC_SIZE = 8;
    protected int subHeaderNameStyle;
    private static final int DEFAULT_SCORE_DESC_STYLE = Font.NORMAL;
    protected BaseColor subHeaderNameColor;
    private static final BaseColor DEFAULT_SCORE_DESC_COLOR = new BaseColor(0x72,0x71,0x71,0xff);

    private static final int DEFAULT_VISIABLE_CELL_HEIGHT = 48;
    private static final int DEFAULT_VISIABLE_CELL_HEIGHT_WITH_DESC = 48;

    private static final float GOLDEN_SPLIT_RATE = 0.618f;

    protected BaseColor backgroundColor;
    protected static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0xef,0xef,0xef,0xff);

    public CustomHeaderCell(String headerName) {
        this(headerName,null);
    }

    public CustomHeaderCell(String headerName, String subHeaderName) {
        this(headerName, subHeaderName,DEFAULT_BACKGROUND_COLOR);
    }
    public CustomHeaderCell(String headerName, String subHeaderName, BaseColor backgroundColor) {
        this(headerName,DEFAULT_SCORE_VALUE_SIZE,DEFAULT_SCORE_VALUE_STYLE,DEFAULT_SCORE_VALUE_COLOR,
                subHeaderName,DEFAULT_SCORE_DESC_SIZE,DEFAULT_SCORE_DESC_STYLE,DEFAULT_SCORE_DESC_COLOR,
                backgroundColor);
    }


    public CustomHeaderCell(String headerName, int headerNameSize,
                            int headerNameStyle, BaseColor headerNameColor,
                            String subHeaderName, int subHeaderNameSize, int subHeaderNameStyle,
                            BaseColor subHeaderNameColor, BaseColor backgroundColor) {
        this.headerName = headerName;
        this.headerNameSize = headerNameSize;
        this.headerNameStyle = headerNameStyle;
        this.headerNameColor = headerNameColor;
        this.subHeaderName = subHeaderName;
        this.subHeaderNameSize = subHeaderNameSize;
        this.subHeaderNameStyle = subHeaderNameStyle;
        this.subHeaderNameColor = subHeaderNameColor;
        this.backgroundColor = backgroundColor;

        if(subHeaderName == null || subHeaderName.trim().equals("")){
            this.setFixedHeight(DEFAULT_VISIABLE_CELL_HEIGHT);
        }else {
            this.setFixedHeight(DEFAULT_VISIABLE_CELL_HEIGHT_WITH_DESC);
        }

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.BASECANVAS];

        float chartWidth = getProperChartWidth(position);

        float chartHeight = getProperChartHeight(position);

        drawBackground(canvas, position ,chartWidth, chartHeight);

        drawHeader(canvas,position,chartWidth,chartHeight);
        drawSubHeader(canvas,position,chartWidth,chartHeight);
    }

    /**
     * 画具体的分数值
     * @param canvas
     * @param chartWidth
     * @param chartHeight
     */
    private void drawHeader(PdfContentByte canvas, Rectangle position, float chartWidth, float chartHeight) {
        try {
            float headerNameHeight = 0;
            if(subHeaderName == null || subHeaderName.trim().equals("")){
                headerNameHeight = chartHeight;
            } else {
                headerNameHeight = chartHeight * GOLDEN_SPLIT_RATE;
            }

            float strHeight = DrawTextUtil.getStringHeight(headerNameSize);

            Rectangle headerNamePosition = new Rectangle(
                    position.getLeft(),
                    (position.getTop() - headerNameHeight / 2) - strHeight / 2,
                    position.getRight(),
                    (position.getTop() - headerNameHeight / 2) + strHeight / 2
            );

            ColumnText strColumnText = new ColumnText(canvas);

            strColumnText.setSimpleColumn(new Phrase(new Chunk(headerName + "",
                            new Font(FontUtils.baseFontChinese,headerNameSize,headerNameStyle,headerNameColor))),
                    headerNamePosition.getLeft(),
                    headerNamePosition.getBottom(),
                    headerNamePosition.getRight(),
                    headerNamePosition.getBottom() + strHeight / 8,
                    0, Element.ALIGN_CENTER);

            View.showPosition(canvas,position);

            strColumnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 画分数描述字符串
     * @param canvas
     * @param chartWidth
     * @param chartHeight
     */
    private void drawSubHeader(PdfContentByte canvas, Rectangle position, float chartWidth, float chartHeight) {
        try {
            if(subHeaderName == null || subHeaderName.trim().equals("")){
                return;
            }

            float subHeaderNameHeight = chartHeight * (1 - GOLDEN_SPLIT_RATE);

            float strHeight = DrawTextUtil.getStringHeight(subHeaderNameSize);

            Rectangle subHeaderNamePosition = new Rectangle(
                    position.getLeft(),
                    (position.getBottom() + subHeaderNameHeight / 2) - strHeight / 2,
                    position.getRight(),
                    (position.getBottom() + subHeaderNameHeight / 2) + strHeight / 2
            );

            ColumnText strColumnText = new ColumnText(canvas);

            strColumnText.setSimpleColumn(new Phrase(new Chunk(subHeaderName + "",
                            new Font(FontUtils.baseFontChinese,subHeaderNameSize,subHeaderNameStyle,subHeaderNameColor))),
                    subHeaderNamePosition.getLeft(),
                    subHeaderNamePosition.getBottom(),
                    subHeaderNamePosition.getRight(),
                    subHeaderNamePosition.getBottom() + strHeight / 8,
                    0, Element.ALIGN_CENTER);

            View.showPosition(canvas,position);

            strColumnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 计算出恰当的图宽度
     * @param position
     * @return
     */
    private float getProperChartWidth(Rectangle position) {
        /*float chartWidth = FontUtils.baseFontChinese.getWidthPoint(headerName,headerNameSize) + DEFAULT_MARGIN * 2 + DEFAULT_PADDING * 2;
        chartWidth = chartWidth < DEFAULT_VISIABLE_CELL_MIN_WIDTH ? DEFAULT_VISIABLE_CELL_MIN_WIDTH : chartWidth;
        chartWidth = chartWidth > DEFAULT_VISIABLE_CELL_MAX_WIDTH ? DEFAULT_VISIABLE_CELL_MAX_WIDTH : chartWidth;

        chartWidth = chartWidth > position.getWidth() ? position.getWidth() : chartWidth;*/
        return position.getWidth();
    }

    /**
     * 计算出恰当的图高度
     * @param position
     * @return
     */
    private float getProperChartHeight(Rectangle position) {
        float chartHeight = 0;
        if (subHeaderName == null || subHeaderName.trim().equals("")){
            chartHeight = DEFAULT_VISIABLE_CELL_HEIGHT;
        }else {
            chartHeight = DEFAULT_VISIABLE_CELL_HEIGHT_WITH_DESC;
        }

        chartHeight = chartHeight > position.getHeight() ? position.getHeight() : chartHeight;
        return chartHeight;
    }

    /**
     * 画矩形背景
     * @param position
     * @param canvas
     * @param chartWidth
     * @param chartHeight
     */
    private void drawBackground(PdfContentByte canvas, Rectangle position, float chartWidth, float chartHeight) {
        canvas.saveState();
        canvas.setLineWidth(0);
        canvas.setColorStroke(backgroundColor);
        canvas.setColorFill(backgroundColor);
        canvas.rectangle((position.getLeft() + position.getRight()) / 2 - chartWidth / 2,
                position.getBottom(),
                chartWidth,
                chartHeight);
        canvas.fillStroke();
        canvas.restoreState();
    }

    public static void main(String [] args){
        String destFileStr = "result/test.pdf";
        File destFile = new File(destFileStr);
        destFile.getParentFile().mkdirs();

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            CustomHeaderCell headerCell = new CustomHeaderCell("近一周", "涨跌幅(%)");
            headerCell.setColspan(2);
            mainFrame.addCell(headerCell);
            mainFrame.addCell(headerCell);
            mainFrame.addCell(headerCell);
            mainFrame.addCell(headerCell);
            mainFrame.addCell(headerCell);
            mainFrame.addCell(headerCell);
            mainFrame.addCell(headerCell);


            BaseCell pdfPCell = new BaseCell();
            pdfPCell.setColspan(10);
            mainFrame.addCell(pdfPCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
