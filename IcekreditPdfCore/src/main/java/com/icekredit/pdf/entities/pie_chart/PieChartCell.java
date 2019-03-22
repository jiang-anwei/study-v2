package com.icekredit.pdf.entities.pie_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.EmptyCell;
import com.icekredit.pdf.entities.chart.SuperChartCell;
import com.icekredit.pdf.entities.chart.part.ItemDescPart;
import com.icekredit.pdf.entities.chart.partdata.ItemDescPartData;
import com.icekredit.pdf.entities.chart.partdata.PieChartPartData;
import com.icekredit.pdf.entities.chart.partdata.TitlePartData;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icekredit on 4/28/16.
 */
public class PieChartCell extends SuperChartCell{
    protected static final int MAX_PIE_CHART_RADIUS = 50;

    public PieChartCell(String title,float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc){
        this(title, sectorsAngle, sectorsColor, sectorsDesc,getPercentageDesc(sectorsAngle));
    }

    public PieChartCell(String title,float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc,String [] percentageDesc) {
        this(MAX_PIE_CHART_RADIUS,MAX_PIE_CHART_RADIUS * 0.80f,72,title,sectorsAngle,sectorsColor,sectorsDesc,
                percentageDesc == null ? getPercentageDesc(sectorsAngle) : percentageDesc);
    }

    public PieChartCell(float outerCircleRadius,float innerCircleRadius, int calcStepCount,String title,
                        float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc,String [] percentageDesc) {
        this(outerCircleRadius,
                innerCircleRadius, calcStepCount,title,
                sectorsAngle, sectorsColor,
                sectorsDesc, PieChart.DEFAULT_IS_NEED_DRAW_COVER, PieChart.DEFAULT_IS_NEED_DRAW_DESC,
                percentageDesc == null ? getPercentageDesc(sectorsAngle) : percentageDesc);
    }

    public PieChartCell(float outerCircleRadius,float innerCircleRadius, int calcStepCount,String title,
                        float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc,
                        boolean isNeedDrawCover,boolean isNeedDrawDesc,String [] percentageDesc){
        this(outerCircleRadius,
                innerCircleRadius, calcStepCount,title,
                sectorsAngle, sectorsColor,
                sectorsDesc, isNeedDrawCover,isNeedDrawDesc,
                PieChart.DEFAULT_SECTOR_DESC_FONT_SIZE,
                PieChart.DEFAULT_SETCTOR_DESC_FONT_STYLE, PieChart.DEFAULT_SECTOR_DESC_FONT_COLOR,
                percentageDesc == null ? getPercentageDesc(sectorsAngle) : percentageDesc);
    }

    public PieChartCell(float outerCircleRadius,float innerCircleRadius, int calcStepCount,String title,
                        float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc,
                        boolean isNeedDrawCover,boolean isNeedDrawDesc,
                        int sectorDescFontSize,int sectorDescFontStyle,BaseColor sectorDescFontColor,String [] percentageDesc) {
        percentageDesc = (percentageDesc == null ? getPercentageDesc(sectorsAngle) : percentageDesc);

        if(title != null && title.trim().length() != 0){
            TitlePartData titlePartData = new TitlePartData(title);
            this.getBasePartDatas().add(titlePartData);
        }

        PieChartPartData pieChartPartData = new PieChartPartData(
                outerCircleRadius,innerCircleRadius,sectorsAngle,
                sectorsColor,percentageDesc,
                sectorDescFontSize,sectorDescFontStyle,sectorDescFontColor,
                isNeedDrawDesc,isNeedDrawCover);
        this.getBasePartDatas().add(pieChartPartData);

        List<ItemDescPart.PartDesc> partsDescList = new ArrayList<ItemDescPart.PartDesc>();
        int index = 0;
        for(String sectorDesc:sectorsDesc){
            partsDescList.add(new ItemDescPart.PartDesc(ItemDescPart.PartDesc.PART_TYPE_SECTOR,sectorsColor[index],sectorDesc));

            index ++;
        }
        this.getBasePartDatas().add(new ItemDescPartData(partsDescList));
    }

    public static String[] getPercentageDesc(float[] sectorsAngle) {
        String [] percentageDesc = new String[sectorsAngle.length];

        float total = 0;
        for(float sectorAngle:sectorsAngle){
            total += sectorAngle;
        }

        int index = 0;
        for(float sectorAngle:sectorsAngle){
            percentageDesc[index] = String.format("%4.2f%%",sectorAngle / total * 100);

            index++;
        }

        return percentageDesc;
    }

    private float getProperOuterCircleRadius(float outerCircleRadius) {
        return outerCircleRadius < MAX_PIE_CHART_RADIUS ? outerCircleRadius : MAX_PIE_CHART_RADIUS;
    }

    public static void main(String [] args){
        try{
            String dest = "results/objetcs/test.pdf";
            File destFile = new File(dest);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(8);
            mainFrame.setWidthPercentage(100);

            EmptyCell emptyCell = new EmptyCell(3);
            mainFrame.addCell(emptyCell);

            /*PieChartCell pieChartCell = new PieChartCell();
            pieChartCell.setColspan(2);
            mainFrame.addCell(pieChartCell);*/

            emptyCell = new EmptyCell(3);
            mainFrame.addCell(emptyCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
