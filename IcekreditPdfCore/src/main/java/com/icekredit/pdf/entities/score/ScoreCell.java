package com.icekredit.pdf.entities.score;

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
public class ScoreCell extends BaseCell implements PdfPCellEvent{
    protected String scoreValue;
    protected int scoreValueSize;
    private static final int DEFAULT_SCORE_VALUE_SIZE = 20;
    protected int scoreValueStyle;
    private static final int DEFAULT_SCORE_VALUE_STYLE = Font.BOLD;
    protected BaseColor scoreValueColor;
    private static final BaseColor DEFAULT_SCORE_VALUE_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    protected String scoreDesc;
    protected int scoreDescSize;
    private static final int DEFAULT_SCORE_DESC_SIZE = 10;
    protected int scoreDescStyle;
    private static final int DEFAULT_SCORE_DESC_STYLE = Font.NORMAL;
    protected BaseColor scoreDescColor;
    private static final BaseColor DEFAULT_SCORE_DESC_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    private static final int DEFAULT_VISIABLE_CELL_HEIGHT = 28;
    private static final int DEFAULT_VISIABLE_CELL_HEIGHT_WITH_DESC = 48;
    private static final int DEFAULT_VISIABLE_CELL_MIN_WIDTH = 60;
    private static final int DEFAULT_VISIABLE_CELL_MAX_WIDTH = 80;


    private static final float DEFAULT_SPLIT_LINE_HEIGHT = 1.0f;
    private static final BaseColor DEFAULT_SPLIT_LINE_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    private static final int DEFAULT_MARGIN = 3;
    private static final int DEFAULT_PADDING = 4;

    private static final float GOLDEN_SPLIT_RATE = 0.618f;

    protected BaseColor cellBackgroundColor;
    protected static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0x1d,0xaf,0xfc,0xff);

    public ScoreCell(String scoreValue) {
        this(scoreValue,null);
    }

    public ScoreCell(String scoreValue, String scoreDesc) {
        this(scoreValue,scoreDesc,DEFAULT_BACKGROUND_COLOR);
    }
    public ScoreCell(String scoreValue, String scoreDesc,BaseColor cellBackgroundColor) {
        this(scoreValue,DEFAULT_SCORE_VALUE_SIZE,DEFAULT_SCORE_VALUE_STYLE,DEFAULT_SCORE_VALUE_COLOR,
                scoreDesc,DEFAULT_SCORE_DESC_SIZE,DEFAULT_SCORE_DESC_STYLE,DEFAULT_SCORE_DESC_COLOR,
                cellBackgroundColor);
    }


    public ScoreCell(String scoreValue, int scoreValueSize,
                     int scoreValueStyle, BaseColor scoreValueColor,
                     String scoreDesc, int scoreDescSize, int scoreDescStyle,
                     BaseColor scoreDescColor,BaseColor cellBackgroundColor) {
        this.scoreValue = scoreValue;
        this.scoreValueSize = scoreValueSize;
        this.scoreValueStyle = scoreValueStyle;
        this.scoreValueColor = scoreValueColor;
        this.scoreDesc = scoreDesc;
        this.scoreDescSize = scoreDescSize;
        this.scoreDescStyle = scoreDescStyle;
        this.scoreDescColor = scoreDescColor;
        this.cellBackgroundColor = cellBackgroundColor;

        if(scoreDesc == null || scoreDesc.trim().equals("")){
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

        drawScoreValue(canvas,position,chartWidth,chartHeight);
        drawSeparateLine(canvas,position,chartWidth,chartHeight);
        drawScoreDesc(canvas,position,chartWidth,chartHeight);
    }

    /**
     * 画具体的分数值
     * @param canvas
     * @param chartWidth
     * @param chartHeight
     */
    private void drawScoreValue(PdfContentByte canvas, Rectangle position,float chartWidth, float chartHeight) {
        try {
            float scoreValueHeight = 0;
            if(scoreDesc == null || scoreDesc.trim().equals("")){
                scoreValueHeight = chartHeight;
            } else {
                scoreValueHeight = chartHeight * GOLDEN_SPLIT_RATE;
            }

            float strHeight = DrawTextUtil.getStringHeight(scoreValueSize);

            Rectangle scoreValuePosition = new Rectangle(
                    position.getLeft(),
                    ((position.getTop() + position.getBottom())/ 2 + chartHeight / 2) - scoreValueHeight / 2 - strHeight / 2,
                    position.getRight(),
                    ((position.getTop() + position.getBottom())/ 2 + chartHeight / 2) - scoreValueHeight / 2 + strHeight / 2
            );

            ColumnText strColumnText = new ColumnText(canvas);

            strColumnText.setSimpleColumn(new Phrase(new Chunk(scoreValue + "",
                            new Font(FontUtils.baseFontChinese,scoreValueSize,scoreValueStyle,scoreValueColor))),
                    scoreValuePosition.getLeft(),
                    scoreValuePosition.getBottom(),
                    scoreValuePosition.getRight(),
                    scoreValuePosition.getBottom() + strHeight / 8,
                    0, Element.ALIGN_CENTER);

            View.showPosition(canvas,position);

            strColumnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 画分数值与分数描述之间的分割线
     * @param canvas
     * @param chartWidth
     * @param chartHeight
     */
    private void drawSeparateLine(PdfContentByte canvas,Rectangle position, float chartWidth, float chartHeight) {
        if(scoreDesc == null || scoreDesc.trim().equals("")){
            return;
        }

        float scoreValueHeight = 0;
        if(scoreDesc == null || scoreDesc.trim().equals("")){
            scoreValueHeight = chartHeight;
        } else {
            scoreValueHeight = chartHeight * GOLDEN_SPLIT_RATE;
        }

        canvas.saveState();
        canvas.setLineWidth(0);
        canvas.setColorStroke(DEFAULT_SPLIT_LINE_COLOR);
        canvas.setColorFill(DEFAULT_SPLIT_LINE_COLOR);

        chartWidth = chartHeight * 0.95f;
        canvas.rectangle((position.getLeft() + position.getRight()) / 2 - chartWidth / 2,
                (((position.getTop() + position.getBottom())/ 2 + chartHeight / 2) - scoreValueHeight) - DEFAULT_SPLIT_LINE_HEIGHT / 2,
                chartWidth,
                DEFAULT_SPLIT_LINE_HEIGHT);
        canvas.fillStroke();

        canvas.restoreState();
    }

    /**
     * 画分数描述字符串
     * @param canvas
     * @param chartWidth
     * @param chartHeight
     */
    private void drawScoreDesc(PdfContentByte canvas,Rectangle position, float chartWidth, float chartHeight) {
        try {
            if(scoreDesc == null || scoreDesc.trim().equals("")){
                return;
            }

            float scoreDescHeight = chartHeight * (1 - GOLDEN_SPLIT_RATE);

            float strHeight = DrawTextUtil.getStringHeight(scoreDescSize);

            Rectangle scoreDescPosition = new Rectangle(
                    position.getLeft(),
                    (((position.getTop() + position.getBottom())/ 2 - chartHeight / 2) + scoreDescHeight / 2) - strHeight / 2,
                    position.getRight(),
                    (((position.getTop() + position.getBottom())/ 2 - chartHeight / 2) + scoreDescHeight / 2) + strHeight / 2
            );

            ColumnText strColumnText = new ColumnText(canvas);

            strColumnText.setSimpleColumn(new Phrase(new Chunk(scoreDesc + "",
                            new Font(FontUtils.baseFontChinese,scoreDescSize,scoreDescStyle,scoreDescColor))),
                    scoreDescPosition.getLeft(),
                    scoreDescPosition.getBottom(),
                    scoreDescPosition.getRight(),
                    scoreDescPosition.getBottom() + strHeight / 8,
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
        float chartWidth = FontUtils.baseFontChinese.getWidthPoint(scoreValue,scoreValueSize) + DEFAULT_MARGIN * 2 + DEFAULT_PADDING * 2;
        chartWidth = chartWidth < DEFAULT_VISIABLE_CELL_MIN_WIDTH ? DEFAULT_VISIABLE_CELL_MIN_WIDTH : chartWidth;
        chartWidth = chartWidth > DEFAULT_VISIABLE_CELL_MAX_WIDTH ? DEFAULT_VISIABLE_CELL_MAX_WIDTH : chartWidth;

        chartWidth = chartWidth > position.getWidth() ? position.getWidth() : chartWidth;
        return chartWidth;
    }

    /**
     * 计算出恰当的图高度
     * @param position
     * @return
     */
    private float getProperChartHeight(Rectangle position) {
        float chartHeight = 0;
        if (scoreDesc == null || scoreDesc.trim().equals("")){
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
        canvas.setColorStroke(cellBackgroundColor);
        canvas.setColorFill(cellBackgroundColor);
        canvas.rectangle((position.getLeft() + position.getRight()) / 2 - chartWidth / 2,
                ((position.getTop() + position.getBottom())/ 2 - chartHeight / 2),
                chartWidth,
                chartHeight);
        canvas.fillStroke();
        canvas.restoreState();
    }

    public BaseColor getCellBackgroundColor() {
        return cellBackgroundColor;
    }

    public void setCellBackgroundColor(BaseColor cellBackgroundColor) {
        this.cellBackgroundColor = cellBackgroundColor;
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

            ScoreCell headerCell = new ScoreCell("666", "");
            headerCell.setColspan(2);
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
