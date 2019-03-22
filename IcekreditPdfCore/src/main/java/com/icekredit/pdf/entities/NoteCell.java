package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.TextUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 7/24/16.
 *
 * 备注信息单元格
 */
public class NoteCell extends BaseCell implements PdfPCellEvent{
    protected String note;
    protected float fontSize;
    private static final int DEFAULT_FONT_SIZE = 10;
    protected int fontStyle;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;
    protected BaseColor fontColor;
    private static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0x59,0x57,0x57,0xff);

    private static final int DEFAULT_PADDING  = 4;
    private static final int DEFAULT_MARGIN = 2;

    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xef,0xef,0xef,0xff);

    private static final int DEFAULT_VISIBLE_CELL_HEIGHT = 22;

    public NoteCell(String note) {
        this(note,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR,1,12);
    }

    public NoteCell(String note, float fontSize, int fontStyle, BaseColor fontColor, int startColumn, int spanColumn) {
        this.note = note;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;

        Rectangle noteStrAvailableRectangle = new Rectangle(36,0,559,0);

        float pageWidth = noteStrAvailableRectangle.getWidth();

        noteStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        int linesCountNeeded = TextUtil.getLinesCountNeeded(note,fontSize,noteStrAvailableRectangle);
        this.setFixedHeight(DEFAULT_VISIBLE_CELL_HEIGHT * linesCountNeeded);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle noteStrAvailableRectangle = new Rectangle(position);
        noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(noteStrAvailableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);

        if(Debug.DEBUG_FLAG){
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS],noteStrAvailableRectangle);
        }

        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(note,fontSize,noteStrAvailableRectangle);

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

            cellChartConfig.setBackgroundColor(CELL_BACKGROUND_COLOR);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

            cellChart.fragments.add(getNoteStrSegment(cellChartConfig,linesDrawnCount,noteStrAvailableRectangle));

            cellChart.draw(canvases[PdfPTable.BASECANVAS]);
        }
    }

    private Fragment getNoteStrSegment(CellChartConfig cellChartConfig, int linesDrawnCount, Rectangle valueStrAvailableRectangle) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(TextUtil.getSpecLineToDraw(note,fontSize,linesDrawnCount,valueStrAvailableRectangle));
        messageConfig.setExtraLink("");
        messageConfig.setFontSize((int) fontSize);
        messageConfig.setFontStyle(fontStyle);
        messageConfig.setFontColor(fontColor);

        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

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

            NoteCell noteCell = new NoteCell("备注:货币单位 本报告之金额若无特殊声明均为人民币表示“ 信息不足” 或 “没有评价”");
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
