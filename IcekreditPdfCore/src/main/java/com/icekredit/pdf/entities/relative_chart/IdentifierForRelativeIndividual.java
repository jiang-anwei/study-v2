package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.Debug;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/19/16.
 */
public class IdentifierForRelativeIndividual extends Identifier {
    protected static final int RELATIVE_INDIVIDUAL_SHOW_AS = SHOW_AS_RECTANGLE;

    public IdentifierForRelativeIndividual(PdfWriter pdfWriter, float centerXPosition, float centerYPosition) {
        super(pdfWriter, centerXPosition, centerYPosition);
        this.showAs = RELATIVE_INDIVIDUAL_SHOW_AS;
    }

    public IdentifierForRelativeIndividual(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                           float identifierWidth, float identifierHeight) {
        super(pdfWriter, centerXPosition, centerYPosition,
                identifierWidth, identifierHeight, RELATIVE_INDIVIDUAL_SHOW_AS);
    }

    public IdentifierForRelativeIndividual(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                           float identifierWidth, float identifierHeight,
                                           float[] multiLevelCircleRadiuses, BaseColor[] multiLevelCircleColors) {
        super(pdfWriter, centerXPosition, centerYPosition,
                identifierWidth, identifierHeight, RELATIVE_INDIVIDUAL_SHOW_AS,
                multiLevelCircleRadiuses, multiLevelCircleColors);
    }

    @Override
    public void draw() {
        super.draw();

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(pdfWriter,new Point[]{relatedAsInvestVertex},1);
            testCalculatedVertices(pdfWriter,new Point[]{relatedAsStockholderVertex},1);
        }
    }

    public Point getRelatedAsInvestVertex() {
        return relatedAsInvestVertex;
    }

    public void setRelatedAsInvestVertex(Point relatedAsInvestVertex) {
        this.relatedAsInvestVertex = relatedAsInvestVertex;
    }

    public Point getRelatedAsStockholderVertex() {
        return relatedAsStockholderVertex;
    }

    public void setRelatedAsStockholderVertex(Point relatedAsStockholderVertex) {
        this.relatedAsStockholderVertex = relatedAsStockholderVertex;
    }


    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            new IdentifierForRelativeIndividual(writer,400,100,40,30,new float[]{20,17},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    }).draw();
            new IdentifierForRelativeIndividual(writer,400,300,40,30,new float[]{20,17},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    }).draw();
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
