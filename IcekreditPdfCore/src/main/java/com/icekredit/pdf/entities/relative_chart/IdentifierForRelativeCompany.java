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
public class IdentifierForRelativeCompany extends Identifier {
    private boolean hasCascadeRelativeInfo;
    private static final boolean DEFAULT_HAS_CASCADE_RELATIVE_COMPANY = true;
    private BaseColor expandTagColor;
    private static final BaseColor DEAFULT_EXPAND_TAG_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    protected static final int COMPANY_IDENTIFIER_SHOW_AS = SHOW_AS_CIRCLE;

    public IdentifierForRelativeCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition) {
        this(pdfWriter, centerXPosition, centerYPosition,DEFAULT_HAS_CASCADE_RELATIVE_COMPANY,DEAFULT_EXPAND_TAG_COLOR);
    }

    public IdentifierForRelativeCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                        boolean hasCascadeRelativeInfo,BaseColor expandTagColor) {
        super(pdfWriter, centerXPosition, centerYPosition);

        this.hasCascadeRelativeInfo = hasCascadeRelativeInfo;
        this.expandTagColor = expandTagColor;
        this.showAs = COMPANY_IDENTIFIER_SHOW_AS;
    }

    public IdentifierForRelativeCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                        boolean hasCascadeRelativeInfo,BaseColor expandTagColor,
                                        float identifierWidth, float identifierHeight) {
        super(pdfWriter, centerXPosition, centerYPosition, identifierWidth, identifierHeight, COMPANY_IDENTIFIER_SHOW_AS);

        this.hasCascadeRelativeInfo = hasCascadeRelativeInfo;
        this.expandTagColor = expandTagColor;
    }

    public IdentifierForRelativeCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                        boolean hasCascadeRelativeInfo,BaseColor expandTagColor,
                                        float identifierWidth, float identifierHeight,
                                        float[] multiLevelCircleRadiuses, BaseColor[] multiLevelCircleColors) {
        super(pdfWriter, centerXPosition, centerYPosition,
                identifierWidth, identifierHeight, COMPANY_IDENTIFIER_SHOW_AS,
                multiLevelCircleRadiuses, multiLevelCircleColors);

        this.hasCascadeRelativeInfo = hasCascadeRelativeInfo;
        this.expandTagColor = expandTagColor;
    }

    @Override
    public void draw() {
        super.draw();

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

        if(hasCascadeRelativeInfo){    //如果某一个关联的企业拥有级联的关联信息，那么当前identifier画一个可以展开的标志
            Point[] expandFlagVertices = getExpandFlagVertices(centerXPosition, centerYPosition, minLevelCircleRadius);

            PdfContentByte canvas = pdfWriter.getDirectContent();
            canvas.setLineWidth(0.1);

            canvas.saveState();
            canvas.setColorFill(this.expandTagColor);
            canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));

            canvas.moveTo(expandFlagVertices[0].x,expandFlagVertices[0].y);

            int index = 0;
            for(index = 1;index < 12;index ++){
                canvas.lineTo(expandFlagVertices[index].x,expandFlagVertices[index].y);
            }

            canvas.closePathFillStroke();
            canvas.restoreState();
        }

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(pdfWriter,new Point[]{relatedAsInvestVertex},1);
            testCalculatedVertices(pdfWriter,new Point[]{relatedAsStockholderVertex},1);
        }
    }

    /**
     * 获取用于画可展开标签（中间的十字符合哦）需要的顶点信息
     * @param centerXPosition
     * @param centerYPosition
     * @param minLevelCircleRadius
     * @return
     */
    private Point[] getExpandFlagVertices(float centerXPosition, float centerYPosition, float minLevelCircleRadius) {
        float minOffset = GOLDEN_SPLITE_RATE * minLevelCircleRadius / 5;
        float maxOffset = GOLDEN_SPLITE_RATE * minLevelCircleRadius;

        Point[] expandFlagVertices = new Point[12];
        for (int index = 0;index < 12;index++){
            expandFlagVertices[index] = new Point(0,0);
        }

        expandFlagVertices[0].x = centerXPosition + minOffset;
        expandFlagVertices[1].x = centerXPosition + minOffset;
        expandFlagVertices[2].x = centerXPosition - minOffset;
        expandFlagVertices[3].x = centerXPosition - minOffset;
        expandFlagVertices[4].x = centerXPosition - maxOffset;
        expandFlagVertices[5].x = centerXPosition - maxOffset;
        expandFlagVertices[6].x = centerXPosition - minOffset;
        expandFlagVertices[7].x = centerXPosition - minOffset;
        expandFlagVertices[8].x = centerXPosition + minOffset;
        expandFlagVertices[9].x = centerXPosition + minOffset;
        expandFlagVertices[10].x = centerXPosition + maxOffset;
        expandFlagVertices[11].x = centerXPosition + maxOffset;

        expandFlagVertices[0].y = centerYPosition + minOffset;
        expandFlagVertices[1].y = centerYPosition + maxOffset;
        expandFlagVertices[2].y = centerYPosition + maxOffset;
        expandFlagVertices[3].y = centerYPosition + minOffset;
        expandFlagVertices[4].y = centerYPosition + minOffset;
        expandFlagVertices[5].y = centerYPosition - minOffset;
        expandFlagVertices[6].y = centerYPosition - minOffset;
        expandFlagVertices[7].y = centerYPosition - maxOffset;
        expandFlagVertices[8].y = centerYPosition - maxOffset;
        expandFlagVertices[9].y = centerYPosition - minOffset;
        expandFlagVertices[10].y = centerYPosition - minOffset;
        expandFlagVertices[11].y = centerYPosition + minOffset;

        return expandFlagVertices;
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

            new IdentifierForRelativeCompany(writer,400,100,true,BaseColor.YELLOW,40,30,new float[]{20,17},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    }).draw();
            new IdentifierForRelativeCompany(writer,400,300,true,BaseColor.YELLOW,40,30,new float[]{20,17},
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
