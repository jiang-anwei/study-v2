package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/20/16.
 */
public class ArrowCell extends AlignCenterCell implements PdfPCellEvent{
    private Arrow arrow;

    private PdfWriter pdfWriter;

    private int directionPointTo;

    private float expandAngle;

    private static final float IDENTIFIER_ICON_WIDTH_RATIO = 0.4f;    //表示每一个标识符的图表宽度占据整个cell40%
    private static final float IDENTIFIER_ICON_WIDTH_MAX = 4;    //标识符图标最大宽度

    protected BaseColor arrowFillColor;

    public ArrowCell(PdfWriter pdfWriter) {
        this(pdfWriter,Arrow.DEFAULT_ARROW_FILL_COLOR,Arrow.DEFAULT_EXPAND_ANGLE,Arrow.DEFAULT_DIRECTION_POINT_TO);
    }

    public ArrowCell(PdfWriter pdfWriter, BaseColor arrowFillColor,float expandAngle,int directionPointTo) {
        this.pdfWriter = pdfWriter;
        this.directionPointTo = directionPointTo;
        this.expandAngle = expandAngle;
        this.arrowFillColor = arrowFillColor;

        this.setCellEvent(this);
    }

    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        //画一个居中显示的.宽度为当前cell宽度80%的箭头
        arrow = new Arrow(pdfWriter,
                this.arrowFillColor,
                (position.getRight() + position.getLeft()) / 2,
                (position.getTop() + position.getBottom()) / 2,
                (position.getRight() - position.getLeft()) * IDENTIFIER_ICON_WIDTH_RATIO / 2 > IDENTIFIER_ICON_WIDTH_MAX
                        ? IDENTIFIER_ICON_WIDTH_MAX
                        : (position.getRight() - position.getLeft()) * IDENTIFIER_ICON_WIDTH_RATIO / 2,
                expandAngle,
                directionPointTo);

        arrow.draw();
    }

    public Point getArrowPointToVertex() {
        return arrow.getArrowPointToVertex();
    }

    public void setArrowPointToVertex(Point arrowPointToVertex) {
        this.arrow.setArrowPointToVertex(arrowPointToVertex);
    }

    public Point getArrowPointFromVertex() {
        return arrow.getArrowPointFromVertex();
    }

    public void setArrowPointFromVertex(Point arrowPointFromVertex) {
        this.arrow.setArrowPointFromVertex(arrowPointFromVertex);
    }


    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            PdfPCell arrowCell = new ArrowCell(writer,new BaseColor(0x33,0x33,0x33,0xff),60,Arrow.POINT_TO_DIRECTION_DOWN);
            arrowCell.setColspan(1);
            arrowCell.setFixedHeight(60);

            PdfPCell emptyCell = new AlignCenterCell(new Phrase(new Chunk("Test")));
            emptyCell.setColspan(2);

            PdfPTable mainFrame = new PdfPTable(33);
            mainFrame.setWidthPercentage(100);

            for(int index = 0;index < 11 ;index ++){
                mainFrame.addCell(arrowCell);
                mainFrame.addCell(emptyCell);
            }

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
