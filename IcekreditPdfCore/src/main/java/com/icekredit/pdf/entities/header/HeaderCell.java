package com.icekredit.pdf.entities.header;

import com.icekredit.pdf.entities.*;
import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 7/24/16.
 *
 * 标题单元格基类
 */
public class HeaderCell extends BaseCell implements PdfPCellEvent{
    public static final float DEFAULT_VISIBLE_BORDER_WIDTH_LEFT = 4;

    protected float cellHeight;
    public static final float DEFAULT_VISIBLE_CELL_HEIGHT = 26;


    protected float cellNoteHeight;
    public static final float DEFAULT_CELL_NOTE_HEIGHT = 16;

    protected String headerName;
    private BaseColor headerNameColor;
    private static final BaseColor HEADER_NAME_COLOR = new BaseColor(0xff,0xff,0xff,0xff);
    private static final int HEADER_NAME_FONT_SIZE = 12;
    private static final int HEADER_NAME_FONT_STYLE = Font.NORMAL;

    protected String headerDesc;
    protected BaseColor headerDescColor;
    private static final BaseColor HEADER_DESC_COLOR = new BaseColor(0xff,0xff,0xff,0xff);
    private static final int HEADER_DESC_FONT_SIZE = 12;
    private static final int HEADER_DESC_FONT_STYLE = Font.NORMAL;

    protected int headerDescIdentifierType;
    protected BaseColor headerDescIdentifierBackgroundColor;
    protected BaseColor headerDescIdentifierForegroundColor;

    private static BaseColor DEFAULT_IDENTIFIER_FOREGROUND_COLOR = new BaseColor(0x59,0xca,0x70,0xff);
    private static BaseColor DEFAULT_IDENTIFIER_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);


    private String headerValue;
    protected BaseColor headerValueColor;
    private static final BaseColor HEADER_VALUE_COLOR = new BaseColor(0xff,0xff,0xff,0xff);
    private static final int HEADER_VALUE_FONT_SIZE = 16;
    private static final int HEADER_VALUE_FONT_STYLE = Font.BOLD;
    private static final int HEADER_VALUE_MIN_WIDTH = 50;

    protected int headerValueIdentifierType;
    protected BaseColor headerValueIdentifierBackgroundColor;
    protected BaseColor headerValueIdentifierForegroundColor;

    protected BaseColor cellBackGroundColor;
    protected BaseColor cellLeftBorderColor;

    private static final int PADDING_LEFT = 8;
    private static final int PADDING_TOP = 4;
    private static final int PADDING_BOTTOM = 4;

    private static final int DEFAULT_MARGIN = 2;

    protected String headerNote;
    protected static final int HEADER_NOTE_FONT_SIZE = 7;
    protected static final int HEADER_NOTE_FONT_STYLE = Font.NORMAL;
    protected static final BaseColor HEADER_NOTE_COLOR = new BaseColor(0xcc,0x6d,0x67,0xff);
    protected BaseColor headerNoteColor;

    public HeaderCell(String headerName,String headerDesc,int headerDescIdentifierType,
                      String headerValue,int headerValueIdentifierType,
                      BaseColor cellBackGroundColor,BaseColor cellLeftBorderColor,String headerNote) {
        this(headerName, headerDesc, headerDescIdentifierType, headerValue,
                headerValueIdentifierType, cellBackGroundColor, cellLeftBorderColor,
                DEFAULT_IDENTIFIER_FOREGROUND_COLOR,DEFAULT_IDENTIFIER_BACKGROUND_COLOR,
                DEFAULT_IDENTIFIER_FOREGROUND_COLOR,DEFAULT_IDENTIFIER_BACKGROUND_COLOR,headerNote);
    }

    public HeaderCell(String headerName,String headerDesc,int headerDescIdentifierType,String headerValue,int headerValueIdentifierType,
                      BaseColor cellBackGroundColor,BaseColor cellLeftBorderColor,
                      BaseColor headerDescIdentifierForegroundColor,BaseColor headerDescIdentifierBackgroundColor,
                      BaseColor headerValueIdentifierForegroundColor,BaseColor headerValueIdentifierBackgroundColor,
                      String headerNote) {
        this(headerName,HEADER_NAME_COLOR, headerDesc, HEADER_DESC_COLOR,headerDescIdentifierType,
                headerValue,HEADER_VALUE_COLOR, headerValueIdentifierType,
                cellBackGroundColor, cellLeftBorderColor,
                headerDescIdentifierForegroundColor,headerDescIdentifierBackgroundColor,
                headerValueIdentifierForegroundColor,headerValueIdentifierBackgroundColor,
                headerNote);
    }

    public HeaderCell(String headerName,BaseColor headerNameColor,
                      String headerDesc,BaseColor headerDescColor,int headerDescIdentifierType,
                      String headerValue,BaseColor headerValueColor,int headerValueIdentifierType,
                      BaseColor cellBackGroundColor,BaseColor cellLeftBorderColor,
                      BaseColor headerDescIdentifierForegroundColor,BaseColor headerDescIdentifierBackgroundColor,
                      BaseColor headerValueIdentifierForegroundColor,BaseColor headerValueIdentifierBackgroundColor,
                      String headerNote) {
        this(headerName, headerNameColor, headerDesc, headerDescColor,
                headerDescIdentifierType, headerValue, headerValueColor,
                headerValueIdentifierType, cellBackGroundColor, cellLeftBorderColor,
                headerDescIdentifierForegroundColor, headerDescIdentifierBackgroundColor,
                headerValueIdentifierForegroundColor, headerValueIdentifierBackgroundColor,
                headerNote,DEFAULT_VISIBLE_CELL_HEIGHT,DEFAULT_CELL_NOTE_HEIGHT);
    }

    public HeaderCell(String headerName,BaseColor headerNameColor,
                      String headerDesc,BaseColor headerDescColor,int headerDescIdentifierType,
                      String headerValue,BaseColor headerValueColor,int headerValueIdentifierType,
                      BaseColor cellBackGroundColor,BaseColor cellLeftBorderColor,
                      BaseColor headerDescIdentifierForegroundColor,BaseColor headerDescIdentifierBackgroundColor,
                      BaseColor headerValueIdentifierForegroundColor,BaseColor headerValueIdentifierBackgroundColor,
                      String headerNote,float cellHeight,float cellNoteHeight) {
        this.headerName = headerName;
        this.headerNameColor = headerNameColor;
        this.headerDesc = headerDesc;
        this.headerDescColor = headerDescColor;
        this.headerValue = headerValue;
        this.headerValueColor = headerValueColor;

        this.headerDescIdentifierType = headerDescIdentifierType;
        this.headerValueIdentifierType = headerValueIdentifierType;

        this.cellBackGroundColor = cellBackGroundColor;
        this.cellLeftBorderColor = cellLeftBorderColor;

        this.headerDescIdentifierForegroundColor = headerDescIdentifierForegroundColor;
        this.headerDescIdentifierBackgroundColor = headerDescIdentifierBackgroundColor;
        this.headerValueIdentifierForegroundColor = headerValueIdentifierForegroundColor;
        this.headerValueIdentifierBackgroundColor = headerValueIdentifierBackgroundColor;

        this.headerNote = headerNote;
        this.headerNoteColor = HEADER_NOTE_COLOR;

        this.cellHeight = cellHeight;
        this.cellNoteHeight = cellNoteHeight;

        if(headerNote != null && !headerNote.trim().equals("")){
            this.setFixedHeight(cellHeight + cellNoteHeight * getNoteLinesCount(headerNote) + DEFAULT_MARGIN * 2);
        } else {
            this.setFixedHeight(cellHeight + DEFAULT_MARGIN * 2);
        }

        this.setColspan(12);

        this.setCellEvent(this);
    }

    private static int getNoteLinesCount(String note){
        Rectangle availableRectangle = new Rectangle(PageSize.A4.getLeft(),0,PageSize.A4.getLeft() + PageSize.A4.getWidth() / 2,0);

        return TextUtil.getLinesCountNeeded(note,HEADER_NOTE_FONT_SIZE,availableRectangle);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        //画header
        CellChartConfig cellChartConfig = CellChartConfig.newInstance();
        cellChartConfig.setPaddingLeft(PADDING_LEFT);
        cellChartConfig.setPaddingTop(PADDING_TOP);
        cellChartConfig.setPaddingBottom(PADDING_BOTTOM);
        cellChartConfig.setBorderWidthLeft(DEFAULT_VISIBLE_BORDER_WIDTH_LEFT);

        cellChartConfig.setLLX(position.getLeft());
        cellChartConfig.setURX(position.getRight());
        cellChartConfig.setURY(position.getTop() - DEFAULT_MARGIN);
        if(headerNote != null && !headerNote.trim().equals("")){
            cellChartConfig.setLLY(DEFAULT_MARGIN + position.getBottom() + cellNoteHeight * getNoteLinesCount(headerNote));
        }else {
            cellChartConfig.setLLY(DEFAULT_MARGIN + position.getBottom());
        }


        showBorders(cellChartConfig);
        // 不知道为什么虽然设置的边框宽度为零，还是会有一点以默认颜色来画的边框
        cellChartConfig.setBorderColor(cellBackGroundColor);
        cellChartConfig.setBorderColorLeft(cellLeftBorderColor);

        CellChart headerCellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

        MessageConfig headerNameMessageConfig = new MessageConfig(cellChartConfig);
        headerNameMessageConfig.setMessage(headerName);
        headerNameMessageConfig.setExtraLink(UrlUtil.generateLocalDestLink(headerName));
        headerNameMessageConfig.setFontColor(headerNameColor);
        headerNameMessageConfig.setFontSize(HEADER_NAME_FONT_SIZE);
        headerNameMessageConfig.setFontStyle(HEADER_NAME_FONT_STYLE);
        headerNameMessageConfig.setIdentifierType(MessageConfig.IDENTIFIER_TYPE_NONE);
        headerNameMessageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        Fragment headerNameFragment = new Fragment(headerNameMessageConfig);

        MessageConfig headerDescMessageConfig = new MessageConfig(cellChartConfig);
        headerDescMessageConfig.setMessage(headerDesc);
        headerDescMessageConfig.setFontColor(headerDescColor);
        headerDescMessageConfig.setFontSize(HEADER_DESC_FONT_SIZE);
        headerDescMessageConfig.setFontStyle(HEADER_DESC_FONT_STYLE);
        headerDescMessageConfig.setIdentifierType(headerDescIdentifierType);
        headerDescMessageConfig.setIdentifierShowAs(MessageConfig.IDENTIFIER_SHOW_AS_CIRCLE);
        headerDescMessageConfig.setMarginLeft(0);

        headerDescMessageConfig.setIdentifierForegroundColor(headerDescIdentifierForegroundColor);
        headerDescMessageConfig.setIdentifierBackgroundColor(headerDescIdentifierBackgroundColor);
        headerDescMessageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        Fragment headerDescFragment = new Fragment(headerDescMessageConfig);

        MessageConfig headerValueMessageConfig = new MessageConfig(cellChartConfig);
        headerValueMessageConfig.setMessage(headerValue);
        headerValueMessageConfig.setFontColor(headerValueColor);
        headerValueMessageConfig.setFontSize(HEADER_VALUE_FONT_SIZE);
        headerValueMessageConfig.setFontStyle(HEADER_VALUE_FONT_STYLE);
        headerValueMessageConfig.setIdentifierType(headerValueIdentifierType);
        headerValueMessageConfig.setIdentifierShowAs(MessageConfig.IDENTIFIER_SHOW_AS_CIRCLE);
        headerValueMessageConfig.setIdentifierForegroundColor(headerValueIdentifierForegroundColor);
        headerValueMessageConfig.setIdentifierBackgroundColor(headerValueIdentifierBackgroundColor);
        headerValueMessageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        if(headerValue != null && !headerValue.trim().equals("")){
            headerValueMessageConfig.setWidth(40);
        }

        Fragment headerValueFragment = new Fragment(headerValueMessageConfig);

        headerCellChart.fragments.add(headerNameFragment);   //画左边第一个（一般左边只有一个）
        headerCellChart.fragments.add(headerValueFragment);   //画右边第一个
        headerCellChart.fragments.add(headerDescFragment);   //画右边第二个

        headerCellChart.draw(canvases[PdfPTable.BASECANVAS]);


        //画headerNote
        if(headerNote != null && !headerNote.trim().equals("")){
            int linesCount = getNoteLinesCount(headerNote);
            Rectangle availableRectangle = new Rectangle(PageSize.A4.getLeft(),0,PageSize.A4.getLeft() + PageSize.A4.getWidth() / 2,0);

            float maxWidthUsed = -1;

            String currentPartDrawn = null;
            for(int currentLineCountDrawn = 0;currentLineCountDrawn < linesCount;currentLineCountDrawn ++){
                currentPartDrawn = TextUtil.getSpecLineToDraw(headerNote,HEADER_NOTE_FONT_SIZE,currentLineCountDrawn,availableRectangle);

                cellChartConfig = CellChartConfig.newInstance();
                float noteWidth = FontUtils.baseFontChinese.getWidthPoint(currentPartDrawn,HEADER_NOTE_FONT_SIZE) + 20;

                if(noteWidth < maxWidthUsed){
                    noteWidth = maxWidthUsed;
                }else {
                    maxWidthUsed = noteWidth;
                }

                if(currentLineCountDrawn == linesCount - 1){
                    cellChartConfig.setBorderColorBottom(new BaseColor(0xff,0xe8,0xed,0xff));
                    cellChartConfig.setBorderWidthBottom(1);
                }

                cellChartConfig.setLLX(position.getRight() - noteWidth);
                cellChartConfig.setURX(position.getRight());
                cellChartConfig.setLLY(DEFAULT_MARGIN + position.getBottom() + (linesCount - currentLineCountDrawn - 1) * cellNoteHeight);
                cellChartConfig.setURY(DEFAULT_MARGIN + position.getBottom() + (linesCount - currentLineCountDrawn) * cellNoteHeight - 1);

                CellChart noteCellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

                MessageConfig noteMessageConfig = MessageConfig.newInstance(cellChartConfig);
                noteMessageConfig.setMessage(currentPartDrawn);
                noteMessageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);
                noteMessageConfig.setIdentifierType(MessageConfig.IDENTIFIER_TYPE_NONE);
                noteMessageConfig.setFontColor(headerNoteColor);
                noteMessageConfig.setFontSize(HEADER_NOTE_FONT_SIZE);
                noteMessageConfig.setFontStyle(HEADER_NOTE_FONT_STYLE);

                Fragment noteFragment =  new Fragment(noteMessageConfig);
                noteCellChart.fragments.add(noteFragment);

                noteCellChart.draw(canvases[PdfPTable.BASECANVAS]);
            }
        }
    }

    protected void showBorders(CellChartConfig cellChartConfig) {
        cellChartConfig.setBackgroundColor(cellBackGroundColor);
    }

    public BaseColor getHeaderNameColor() {
        return headerNameColor;
    }

    public void setHeaderNameColor(BaseColor headerNameColor) {
        this.headerNameColor = headerNameColor;
    }

    public BaseColor getHeaderDescColor() {
        return headerDescColor;
    }

    public void setHeaderDescColor(BaseColor headerDescColor) {
        this.headerDescColor = headerDescColor;
    }

    public BaseColor getHeaderValueColor() {
        return headerValueColor;
    }

    public void setHeaderValueColor(BaseColor headerValueColor) {
        this.headerValueColor = headerValueColor;
    }

    public BaseColor getHeaderNoteColor() {
        return headerNoteColor;
    }

    public void setHeaderNoteColor(BaseColor headerNoteColor) {
        this.headerNoteColor = headerNoteColor;
    }

    public BaseColor getCellBackGroundColor() {
        return cellBackGroundColor;
    }

    public void setCellBackGroundColor(BaseColor cellBackGroundColor) {
        this.cellBackGroundColor = cellBackGroundColor;
    }

    public BaseColor getCellLeftBorderColor() {
        return cellLeftBorderColor;
    }

    public void setCellLeftBorderColor(BaseColor cellLeftBorderColor) {
        this.cellLeftBorderColor = cellLeftBorderColor;
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

            HeaderCell headerCell = new HeaderCell("與情分数","分数分数分数", MessageConfig.IDENTIFIER_TYPE_MARK_NEGETIVE,"666", MessageConfig.IDENTIFIER_TYPE_MARK_RIGHT,
                    new BaseColor(0x66,0xc6,0xfc,0xff),new BaseColor(0x00,0xa0,0xe9,0xff),"可能原因:1.银行卡号输入错误;2.新办卡;3.该卡使用频率不高或为睡眠卡;" +
                    "4.发卡行内部交易,比如代发工资、还贷等;5.申请人偏好第三方支付申请人偏好第三方支付申请人偏好第三方支付申请人偏好第三方支付");
            headerCell.setColspan(12);
            mainFrame.addCell(headerCell);

            /*PdfConvertUtil.addUnEvaluatableCell(mainFrame,"反欺诈分析","无法评估","可能原因:1.银行卡号输入错误;2.新办卡;3.该卡使用频率不高或为睡眠卡;" +
                    "4.发卡行内部交易,比如代发工资、还贷等;5.申请人偏好第三方支付");*/


            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public float getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

    public float getCellNoteHeight() {
        return cellNoteHeight;
    }

    public void setCellNoteHeight(float cellNoteHeight) {
        this.cellNoteHeight = cellNoteHeight;
    }
}
