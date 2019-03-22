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
public class IdentifierForCurrentCompany extends Identifier {
    private static final int BRANCHES_VERTICES_COUNT = 11;
    private Point[] investBranchVertices = null;
    private Point[] stockHolderBranchesVertices = null;

    private static final int INVEST_BRANCHES_VERTICES_START_ANGLE = 220;
    private static final int INVEST_BRANCHES_VERTICES_END_ANGLE = 320;

    private static final int STOCKHOLDER_BRANCHES_VERTICES_START_ANGLE = 40;
    private static final int STOCKHOLDER_BRANCHES_VERTICES_END_ANGLE = 140;

    private static final int ANGLE_INCREMENT_STEP = 10;
    public IdentifierForCurrentCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition) {
        super(pdfWriter, centerXPosition, centerYPosition);

        initGlobalScope();
    }

    public IdentifierForCurrentCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                       float identifierWidth, float identifierHeight, int showAs) {
        super(pdfWriter, centerXPosition, centerYPosition, identifierWidth, identifierHeight, showAs);

        initGlobalScope();
    }

    public IdentifierForCurrentCompany(PdfWriter pdfWriter, float centerXPosition, float centerYPosition,
                                       float identifierWidth, float identifierHeight, int showAs,
                                       float[] multiLevelCircleRadiuses, BaseColor[] multiLevelCircleColors) {
        super(pdfWriter, centerXPosition, centerYPosition,
                identifierWidth, identifierHeight, showAs,
                multiLevelCircleRadiuses, multiLevelCircleColors);

        initGlobalScope();
    }

    private void initGlobalScope() {
        investBranchVertices = new Point[BRANCHES_VERTICES_COUNT];    //计算出用于画投资关联企业关联线条的起始坐标
        stockHolderBranchesVertices = new Point[BRANCHES_VERTICES_COUNT];    //计算用于画股东关联企业关联线条的终止坐标

        for(int index = 0;index < BRANCHES_VERTICES_COUNT;index ++){
            investBranchVertices[index] = new Point(0,0);
            stockHolderBranchesVertices[index] = new Point(0,0);
        }

        float centerXPosition = 0;
        float centerYPosition = 0;
        float maxLevelCircleRadius = 0;

        centerXPosition = this.centerXPosition;
        centerYPosition = this.centerYPosition;
        if(multiLevelCircleRadiuses.length > 0){
            maxLevelCircleRadius = multiLevelCircleRadiuses[0];
        }else {
            maxLevelCircleRadius = DEFAULT_MULTI_LEVEL_CIRCLE_RADIUSES[0];
        }

        int currentAngle;
        int index = 0;
        for(index = 0,currentAngle = INVEST_BRANCHES_VERTICES_START_ANGLE;
            currentAngle <= INVEST_BRANCHES_VERTICES_END_ANGLE;
            currentAngle += ANGLE_INCREMENT_STEP,index ++){
            investBranchVertices[index].x = centerXPosition + maxLevelCircleRadius * (float) Math.cos(currentAngle * Math.PI * 2 / 360);
            investBranchVertices[index].y = centerYPosition + maxLevelCircleRadius * (float) Math.sin(currentAngle * Math.PI * 2 / 360);
        }

        for(index = 0,currentAngle = STOCKHOLDER_BRANCHES_VERTICES_START_ANGLE;
            currentAngle <= STOCKHOLDER_BRANCHES_VERTICES_END_ANGLE;
            currentAngle += ANGLE_INCREMENT_STEP,index ++){
            stockHolderBranchesVertices[index].x = centerXPosition + maxLevelCircleRadius * (float) Math.cos(currentAngle * Math.PI * 2 / 360);
            stockHolderBranchesVertices[index].y = centerYPosition + maxLevelCircleRadius * (float) Math.sin(currentAngle * Math.PI * 2 / 360);
        }
    }

    @Override
    public void draw() {
        super.draw();

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(pdfWriter,investBranchVertices,BRANCHES_VERTICES_COUNT);
            testCalculatedVertices(pdfWriter,stockHolderBranchesVertices,BRANCHES_VERTICES_COUNT);
        }
    }

    public Point[] getInvestBranchVertices() {
        return investBranchVertices;
    }

    public void setInvestBranchVertices(Point[] investBranchVertices) {
        this.investBranchVertices = investBranchVertices;
    }

    public Point[] getStockHolderBranchesVertices() {
        return stockHolderBranchesVertices;
    }

    public void setStockHolderBranchesVertices(Point[] stockHolderBranchesVertices) {
        this.stockHolderBranchesVertices = stockHolderBranchesVertices;
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            new IdentifierForCurrentCompany(writer,200,100).draw();
            new IdentifierForCurrentCompany(writer,200,300,100,100,0).draw();

            new IdentifierForCurrentCompany(writer,400,100,40,34,0,new float[]{20,17},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    }).draw();
            new IdentifierForCurrentCompany(writer,400,300,40,34,1,new float[]{20,17},
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
