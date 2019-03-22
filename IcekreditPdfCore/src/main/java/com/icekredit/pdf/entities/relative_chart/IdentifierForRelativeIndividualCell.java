package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/20/16.
 */
public class IdentifierForRelativeIndividualCell extends IdentifierCell {
    private static final String TAG = "IdentifierForRelativeIndividualCell";

    private IdentifierForRelativeIndividual identifierForRelativeIndividual;

    private PdfWriter pdfWriter;
    private BaseColor[] multiLevelCircleColors;

    public IdentifierForRelativeIndividualCell(PdfWriter pdfWriter, BaseColor[] multiLevelCircleColors){
        super(pdfWriter,IdentifierForRelativeIndividual.RELATIVE_INDIVIDUAL_SHOW_AS,multiLevelCircleColors);

        this.pdfWriter = pdfWriter;
        this.multiLevelCircleColors = multiLevelCircleColors;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        identifierForRelativeIndividual = new IdentifierForRelativeIndividual(pdfWriter,
                (position.getRight() + position.getLeft()) / 2,
                (position.getTop() + position.getBottom()) / 2,
                (position.getRight() - position.getLeft()),
                (position.getTop() - position.getBottom()),
                new float[]{
                        (position.getRight() - position.getLeft()) * 0.45f,
                        (position.getRight() - position.getLeft()) * 0.4f
                },
                multiLevelCircleColors);

        identifierForRelativeIndividual.draw();
    }

    public Point getRelatedAsInvestVertex() {
        return identifierForRelativeIndividual.getRelatedAsInvestVertex();
    }

    public void setRelatedAsInvestVertex(Point relatedAsInvestVertex) {
        this.identifierForRelativeIndividual.setRelatedAsInvestVertex(relatedAsInvestVertex);
    }

    public Point getRelatedAsStockholderVertex() {
        return identifierForRelativeIndividual.getRelatedAsStockholderVertex();
    }

    public void setRelatedAsStockholderVertex(Point relatedAsStockholderVertex) {
        this.identifierForRelativeIndividual.setRelatedAsStockholderVertex(relatedAsStockholderVertex);
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            PdfPCell identifierCell = new IdentifierForRelativeIndividualCell(writer,
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
