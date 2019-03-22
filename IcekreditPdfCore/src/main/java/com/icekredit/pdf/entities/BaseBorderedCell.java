package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.line_chart.Line;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * Created by icekredit on 10/17/16.
 */
public class BaseBorderedCell extends AlignLeftCell implements PdfPCellEvent {
    protected boolean isNeedShowTopBorder;
    protected boolean isNeedShowBottomBorder;
    protected boolean isNeedShowLeftBorder;
    protected boolean isNeedShowRightBorder;

    protected float borderWidth;
    public static final float DEFAULT_BORDER_WIDTH = 0.5f;

    protected BaseColor borderColor;
    protected static final BaseColor DEFAULT_BORDER_COLOR = new BaseColor(0xf1,0xf1,0xf1,0xff);

    protected BaseColor backgroundColor;
    public static BaseColor DEFAULT_HEADER_BACKGROUND_COLOR = new BaseColor(0xf9,0xf9,0xf9,0xff);
    public static BaseColor DEFAULT_CONTENT_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    protected static final float DEFAULT_CELL_HEIGHT = 25;

    protected String content;

    public BaseBorderedCell(String content,
                            boolean isNeedShowTopBorder, boolean isNeedShowBottomBorder, boolean isNeedShowLeftBorder, boolean isNeedShowRightBorder,
                            BaseColor backgroundColor) {
        this(content, DEFAULT_BORDER_WIDTH,DEFAULT_BORDER_COLOR,isNeedShowTopBorder, isNeedShowBottomBorder, isNeedShowLeftBorder, isNeedShowRightBorder, backgroundColor);
    }

    public BaseBorderedCell(String content, float borderWidth, BaseColor borderColor,
                            boolean isNeedShowTopBorder, boolean isNeedShowBottomBorder, boolean isNeedShowLeftBorder, boolean isNeedShowRightBorder, BaseColor backgroundColor) {
        super(new Phrase(new Chunk(content,FontUtils.chineseFont)));

        this.isNeedShowTopBorder = isNeedShowTopBorder;
        this.isNeedShowBottomBorder = isNeedShowBottomBorder;
        this.isNeedShowLeftBorder = isNeedShowLeftBorder;
        this.isNeedShowRightBorder = isNeedShowRightBorder;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;

        this.setBackgroundColor(backgroundColor);
        this.setCellEvent(this);
        this.setIndent(2);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            if(isNeedShowTopBorder){
                Line line = new Line(canvases[PdfPTable.LINECANVAS],
                        new Point[]{
                                new Point(position.getLeft(),position.getTop()),
                                new Point(position.getRight(),position.getTop())
                        },
                        10,0,
                        borderWidth,borderColor,false, Line.NODE_SHOWN_AS_CIRCLE);
                line.draw();
            }

            if(isNeedShowBottomBorder){
                Line line = new Line(canvases[PdfPTable.LINECANVAS],
                        new Point[]{
                                new Point(position.getLeft(),position.getBottom()),
                                new Point(position.getRight(),position.getBottom())
                        },
                        10,0,
                        borderWidth,borderColor,false, Line.NODE_SHOWN_AS_CIRCLE);
                line.draw();
            }

            if(isNeedShowLeftBorder){
                Line line = new Line(canvases[PdfPTable.LINECANVAS],
                        new Point[]{
                                new Point(position.getLeft(),position.getBottom()),
                                new Point(position.getLeft(),position.getTop())
                        },
                        10,0,
                        borderWidth,borderColor,false, Line.NODE_SHOWN_AS_CIRCLE);
                line.draw();
            }

            if(isNeedShowRightBorder){
                Line line = new Line(canvases[PdfPTable.LINECANVAS],
                        new Point[]{
                                new Point(position.getRight(),position.getBottom()),
                                new Point(position.getRight(),position.getTop())
                        },
                        10,0,
                        borderWidth,borderColor,false, Line.NODE_SHOWN_AS_CIRCLE);
                line.draw();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
