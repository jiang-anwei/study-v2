package com.icekredit.pdf.entities.tagcloud;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.PdfConvertUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Created by icekredit on 11/3/16.
 */
public class TagCloudItemCell extends BaseCell implements PdfPCellEvent{
    protected static final float DEFAULT_MARGIN = 1.2f;

    protected static final float  SYSTEM_DEFAULT_PAGE_MARGIN = 72;

    protected static final int MAIN_FRAME_TOTAL_COLUMN = 12;
    protected static final int CELL_ITEM_COUNT_FOR_SINGLE_ROW = 12;

    protected int parentSpanColumn;
    protected String tag;
    protected Font tagFont;

    protected static final Font DEFAULT_TAG_FONT = new Font(FontUtils.baseFontChinese,5,FontUtils.fontStyle,new BaseColor(0xff,0xff,0xff,0xff));

    protected BaseColor bgColor;
    public static final BaseColor DEFAULT_BG_COLOR = new BaseColor(0x66,0xc6,0xfc,0xff);//new BaseColor(0x82,0xc0,0xd7,0xff);

    protected int rowSpan;
    protected static final int DEFAULT_ROW_SPAN = 1;
    protected int columnSpan;
    protected static final int DEFAULT_COLUMN_SPAN = 1;

    public TagCloudItemCell(int parentSpanColumn,String tag) {
        this(parentSpanColumn, tag,DEFAULT_ROW_SPAN,DEFAULT_COLUMN_SPAN);
    }

    public TagCloudItemCell(int parentSpanColumn,String tag,int rowSpan,int columnSpan) {
        this(parentSpanColumn, tag,DEFAULT_TAG_FONT, rowSpan, columnSpan, DEFAULT_BG_COLOR);
    }

    public TagCloudItemCell(int parentSpanColumn,String tag,Font tagFont,int rowSpan,int columnSpan,BaseColor bgColor) {
        super(new Phrase(new Chunk(tag,tagFont)));

        this.parentSpanColumn = parentSpanColumn;
        this.tag = tag;
        this.tagFont = tagFont;
        this.bgColor = bgColor;
        this.rowSpan = rowSpan;
        this.columnSpan = columnSpan;

        //设置itemCell的高度等于宽度 乘 行数?
        this.setFixedHeight(getItemCellWidth(parentSpanColumn));

        this.setRowspan(rowSpan);
        this.setColspan(columnSpan);

        this.setNoWrap(false);

        this.setCellEvent(this);
    }

    public int getWeight(){
        return  rowSpan * columnSpan;
    }

    private float getItemCellWidth(int parentSpanColumn) {
        if(parentSpanColumn < 1){
            parentSpanColumn = 1;
        }

        if(parentSpanColumn > MAIN_FRAME_TOTAL_COLUMN){
            parentSpanColumn = MAIN_FRAME_TOTAL_COLUMN;
        }

        return (PageSize.A4.getWidth() - SYSTEM_DEFAULT_PAGE_MARGIN) * parentSpanColumn / MAIN_FRAME_TOTAL_COLUMN / CELL_ITEM_COUNT_FOR_SINGLE_ROW - DEFAULT_MARGIN * 2;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle rectangle = new Rectangle(position);
        rectangle.setLeft(rectangle.getLeft() + DEFAULT_MARGIN);
        rectangle.setBottom(rectangle.getBottom() + DEFAULT_MARGIN);
        rectangle.setTop(rectangle.getTop() - DEFAULT_MARGIN);
        rectangle.setRight(rectangle.getRight() - DEFAULT_MARGIN);

        PdfConvertUtil.drawRoundCornerBackground(rectangle,canvases[PdfPTable.BACKGROUNDCANVAS],bgColor,10);
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;

        this.setRowspan(rowSpan);
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;

        this.setColspan(columnSpan);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Font getTagFont() {
        return tagFont;
    }

    public void setTagFont(Font tagFont) {
        this.tagFont = tagFont;
    }
}
