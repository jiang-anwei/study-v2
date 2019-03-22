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
public class IdentifierCell extends AlignCenterCell implements PdfPCellEvent{
    private Identifier identifier;

    protected PdfWriter pdfWriter;
    protected int showAs;
    protected BaseColor[] multiLevelCircleColors;

    public IdentifierCell(PdfWriter pdfWriter, int showAs, BaseColor[] multiLevelCircleColors) {
        this.pdfWriter = pdfWriter;
        this.showAs = showAs;
        this.multiLevelCircleColors = multiLevelCircleColors;

        this.setCellEvent(this);
    }

    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        identifier = new Identifier(pdfWriter,(position.getRight() + position.getLeft()) / 2,
                (position.getTop() + position.getBottom()) / 2,
                (position.getRight() - position.getLeft()),
                (position.getRight() - position.getLeft()),
                showAs,
                new float[]{(position.getRight() - position.getLeft()) * 0.45f,(position.getRight() - position.getLeft()) * 0.4f},
                multiLevelCircleColors);

        identifier.draw();
    }

    public Point getRelatedAsInvestVertex() {
        return identifier.getRelatedAsInvestVertex();
    }

    public void setRelatedAsInvestVertex(Point relatedAsInvestVertex) {
        this.identifier.setRelatedAsInvestVertex(relatedAsInvestVertex);
    }

    public Point getRelatedAsStockholderVertex() {
        return identifier.getRelatedAsStockholderVertex();
    }

    public void setRelatedAsStockholderVertex(Point relatedAsStockholderVertex) {
        this.identifier.setRelatedAsStockholderVertex(relatedAsStockholderVertex);
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            PdfPCell identifierCell = new IdentifierCell(writer, Identifier.SHOW_AS_CIRCLE,
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    });
            identifierCell.setColspan(1);
            identifierCell.setFixedHeight(60);


            PdfPCell emptyCell = new PdfPCell(new Phrase(new Chunk("Test")));
            emptyCell.setColspan(32);


            PdfPTable mainFrame = new PdfPTable(33);
            mainFrame.setWidthPercentage(100);

            mainFrame.addCell(identifierCell);

            mainFrame.addCell(emptyCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
