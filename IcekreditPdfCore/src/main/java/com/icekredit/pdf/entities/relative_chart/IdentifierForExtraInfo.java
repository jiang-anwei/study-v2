package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.Debug;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/19/16.
 */
public class IdentifierForExtraInfo extends Identifier {
    protected static final int IDENTIFIER_FOR_EXTRA_INFO_SHOW_AS = SHOW_AS_CIRCLE;

    public IdentifierForExtraInfo(PdfWriter pdfWriter, float centerXPosition, float centerYPosition) {
        super(pdfWriter, centerXPosition, centerYPosition);

        this.showAs = IDENTIFIER_FOR_EXTRA_INFO_SHOW_AS;
    }

    public IdentifierForExtraInfo(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                  float identifierWidth, float identifierHeight) {
        super(pdfWriter, centerXPosition, centerYPosition, identifierWidth, identifierHeight,IDENTIFIER_FOR_EXTRA_INFO_SHOW_AS);
    }

    public IdentifierForExtraInfo(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                  float identifierWidth, float identifierHeight,
                                  float[] multiLevelCircleRadiuses, BaseColor[] multiLevelCircleColors) {
        super(pdfWriter, centerXPosition, centerYPosition, identifierWidth, identifierHeight, IDENTIFIER_FOR_EXTRA_INFO_SHOW_AS,
                multiLevelCircleRadiuses, multiLevelCircleColors);
    }

    @Override
    public void draw() {
        super.draw();

        drawExtraInfoFlag();

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

    /**
     * 勾画表示还有额外信息的标志，其实是三个圆形
     */
    private void drawExtraInfoFlag() {
        float centerXPosition = 0;
        float centerYPosition = 0;
        float minLevelCircleRadius = 0;

        centerXPosition = this.centerXPosition;
        centerYPosition = this.centerYPosition;
        if(multiLevelCircleRadiuses.length > 0){
            minLevelCircleRadius = multiLevelCircleRadiuses[multiLevelCircleRadiuses.length - 1];
        }else {
            minLevelCircleRadius = DEFAULT_MULTI_LEVEL_CIRCLE_RADIUSES[DEFAULT_MULTI_LEVEL_CIRCLE_RADIUSES.length];
        }

        float minOffset = GOLDEN_SPLITE_RATE * minLevelCircleRadius / 5;

        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();

        canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
        canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));
        canvas.setLineWidth(0.1);

        canvas.circle(centerXPosition,centerYPosition,minOffset);
        canvas.circle(centerXPosition - 3 * minOffset,centerYPosition,minOffset);
        canvas.circle(centerXPosition + 3 * minOffset,centerYPosition,minOffset);
        canvas.closePathFillStroke();

        canvas.restoreState();
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            new IdentifierForExtraInfo(writer,400,100,40,30,new float[]{20,17},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    }).draw();
            new IdentifierForExtraInfo(writer,400,300,40,30,new float[]{20,17},
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
