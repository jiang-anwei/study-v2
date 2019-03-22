package com.icekredit.pdf.entities.table;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.UrlUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 8/5/16.
 */
public class TableHeaderCell extends BaseCell implements PdfPCellEvent{
    public static final float DEFAULT_VISIBLE_CELL_HEIGHT = 22;

    protected String headerName;
    private BaseColor headerNameColor;
    private static final BaseColor HEADER_NAME_COLOR = new BaseColor(0xff,0xff,0xff,0xff);
    private static final int HEADER_NAME_FONT_SIZE = 8;
    private static final int HEADER_NAME_FONT_STYLE = Font.NORMAL;

    protected int headerNameIdentifierType;
    protected BaseColor headerNameIdentifierBackgroundColor;
    protected BaseColor headerNameIdentifierForegroundColor;

    private static BaseColor DEFAULT_IDENTIFIER_FOREGROUND_COLOR = new BaseColor(0x59,0xca,0x70,0xff);
    private static BaseColor DEFAULT_IDENTIFIER_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    protected BaseColor cellBackGroundColor;
    protected BaseColor cellBottomBorderColor;

    private static final int PADDING_LEFT = 0;
    private static final int PADDING_TOP = 4;
    private static final int PADDING_BOTTOM = 4;
    private static final int DEFAULT_MARGIN = 2;

    private static final BaseColor DEFAULT_BACKGROUND_COLOR = ColorUtil.strRGBAToColor("0xfafafaff");
    private static final BaseColor DEFAULT_VISIBLE_BORDER_COLOR_BOTTOM = new BaseColor(0xef, 0xef, 0xef, 0xff);


    public TableHeaderCell(String headerName,BaseColor headerNameColor,int headerNameIdentifierType) {
        this(headerName,headerNameColor,headerNameIdentifierType,
                DEFAULT_IDENTIFIER_BACKGROUND_COLOR,DEFAULT_IDENTIFIER_FOREGROUND_COLOR,
                DEFAULT_BACKGROUND_COLOR, DEFAULT_VISIBLE_BORDER_COLOR_BOTTOM);
    }

    public TableHeaderCell(String headerName,BaseColor headerNameColor,int headerNameIdentifierType,
                           BaseColor headerNameIdentifierBackgroundColor,BaseColor headerNameIdentifierForegroundColor,
                      BaseColor cellBackGroundColor,BaseColor cellBottomBorderColor) {
        this.headerName = headerName;
        this.headerNameColor = headerNameColor;

        this.cellBackGroundColor = cellBackGroundColor;
        this.cellBottomBorderColor = cellBottomBorderColor;

        this.headerNameIdentifierType = headerNameIdentifierType;
        this.headerNameIdentifierBackgroundColor = headerNameIdentifierBackgroundColor;
        this.headerNameIdentifierForegroundColor = headerNameIdentifierForegroundColor;

        this.setFixedHeight(DEFAULT_VISIBLE_CELL_HEIGHT + DEFAULT_MARGIN * 2);

        this.setColspan(12);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        //画header
        CellChartConfig cellChartConfig = CellChartConfig.newInstance();
        cellChartConfig.setPaddingLeft(PADDING_LEFT);
        cellChartConfig.setPaddingTop(PADDING_TOP);
        cellChartConfig.setPaddingBottom(PADDING_BOTTOM);

        cellChartConfig.setLLX(position.getLeft());
        cellChartConfig.setURX(position.getRight());
        cellChartConfig.setLLY(position.getBottom() + DEFAULT_MARGIN);
        cellChartConfig.setURY(position.getTop() - DEFAULT_MARGIN);

        showBorders(cellChartConfig);

        CellChart headerCellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

        MessageConfig headerNameMessageConfig = new MessageConfig(cellChartConfig);
        headerNameMessageConfig.setMessage(headerName);
        headerNameMessageConfig.setExtraLink(UrlUtil.generateLocalDestLink(headerName));
        headerNameMessageConfig.setFontColor(headerNameColor);
        headerNameMessageConfig.setFontSize(HEADER_NAME_FONT_SIZE);
        headerNameMessageConfig.setFontStyle(HEADER_NAME_FONT_STYLE);
        headerNameMessageConfig.setIdentifierType(headerNameIdentifierType);
        headerNameMessageConfig.setIdentifierBackgroundColor(headerNameIdentifierBackgroundColor);
        headerNameMessageConfig.setIdentifierForegroundColor(headerNameIdentifierForegroundColor);
        headerNameMessageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        Fragment headerNameFragment = new Fragment(headerNameMessageConfig);

        headerCellChart.fragments.add(headerNameFragment);   //画左边第一个（一般左边只有一个）

        headerCellChart.draw(canvases[PdfPTable.BASECANVAS]);
    }

    protected void showBorders(CellChartConfig cellChartConfig) {
        cellChartConfig.setBackgroundColor(cellBackGroundColor);
        cellChartConfig.setBorderColorBottom(cellBottomBorderColor);
        cellChartConfig.setBorderWidthBottom(0.5f);
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

            TableHeaderCell headerCell = new TableHeaderCell("與情分数",ColorUtil.strRGBAToColor("0xca4223ff"),MessageConfig.IDENTIFIER_TYPE_MARK_NEGETIVE);
            headerCell.setColspan(12);
            mainFrame.addCell(headerCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
