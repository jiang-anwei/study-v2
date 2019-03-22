package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.TextUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 9/12/16.
 */
public class TagCell extends BaseCell implements PdfPCellEvent {
    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xf7,0xff,0xff,0xff);
    private static final BaseColor CELL_BORDER_COLOR = new BaseColor(0xdf,0xf3,0xff,0xff);
    private static final float CELL_BORDER_WIDTH = 0.5f;

    protected String tagName;
    protected float fontSize;
    private static final int DEFAULT_FONT_SIZE = 10;
    protected int fontStyle;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;
    protected BaseColor fontColor;
    private static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0x27,0x62,0x98,0xff);

    private static final int DEFAULT_PADDING  = 4;
    private static final int DEFAULT_MARGIN = 2;

    private static final int DEFAULT_VISIBLE_CELL_HEIGHT = 22;

    public TagCell(String tagName) {
        this(tagName,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR,1,12);
    }

    public TagCell(String tagName, float fontSize, int fontStyle, BaseColor fontColor, int startColumn, int spanColumn) {
        this.tagName = tagName;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;

        Rectangle noteStrAvailableRectangle = new Rectangle(36,0,559,0);

        float pageWidth = noteStrAvailableRectangle.getWidth();

        noteStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        int linesCountNeeded = TextUtil.getLinesCountNeeded(tagName,DEFAULT_FONT_SIZE,noteStrAvailableRectangle);
        this.setFixedHeight(DEFAULT_VISIBLE_CELL_HEIGHT * linesCountNeeded + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle noteStrAvailableRectangle = new Rectangle(position);
        noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(noteStrAvailableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);
        noteStrAvailableRectangle.setTop(noteStrAvailableRectangle.getTop() - DEFAULT_MARGIN);
        noteStrAvailableRectangle.setBottom(noteStrAvailableRectangle.getBottom() + DEFAULT_MARGIN);

        if(Debug.DEBUG_FLAG){
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS],noteStrAvailableRectangle);
        }

        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(tagName,DEFAULT_FONT_SIZE,noteStrAvailableRectangle);

        for(int linesDrawnCount = 0;linesDrawnCount < linesCountNeedToDrawn;linesDrawnCount ++){
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setLLX(position.getLeft());
            cellChartConfig.setLLY(position.getBottom()  + DEFAULT_VISIBLE_CELL_HEIGHT * (linesCountNeedToDrawn - linesDrawnCount - 1));
            cellChartConfig.setURX(position.getRight());
            cellChartConfig.setURY(position.getBottom() + DEFAULT_VISIBLE_CELL_HEIGHT * (linesCountNeedToDrawn - linesDrawnCount));

            cellChartConfig.setPaddingLeft(DEFAULT_PADDING);
            cellChartConfig.setPaddingRight(DEFAULT_PADDING);

            cellChartConfig.setMarginRight(DEFAULT_MARGIN);
            cellChartConfig.setMarginLeft(DEFAULT_MARGIN);

            cellChartConfig.setBorderWidth(CELL_BORDER_WIDTH);
            cellChartConfig.setBorderColor(CELL_BORDER_COLOR);

            cellChartConfig.setBackgroundColor(CELL_BACKGROUND_COLOR);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

            cellChart.fragments.add(getTagStrSegment(cellChartConfig,linesDrawnCount,noteStrAvailableRectangle));

            cellChart.draw(canvases[PdfPTable.BASECANVAS]);
        }
    }

    private Fragment getTagStrSegment(CellChartConfig cellChartConfig, int linesDrawnCount, Rectangle valueStrAvailableRectangle) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(TextUtil.getSpecLineToDraw(tagName,DEFAULT_FONT_SIZE,linesDrawnCount,valueStrAvailableRectangle));
        messageConfig.setExtraLink("");
        messageConfig.setFontSize(DEFAULT_FONT_SIZE);
        messageConfig.setFontStyle(DEFAULT_FONT_STYLE);
        messageConfig.setFontColor(DEFAULT_FONT_COLOR);

        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_CENTER | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        return new Fragment(messageConfig);
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
            mainFrame.getDefaultCell().setBorder(NO_BORDER);

            TagCell noteCell = new TagCell("备注");
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
