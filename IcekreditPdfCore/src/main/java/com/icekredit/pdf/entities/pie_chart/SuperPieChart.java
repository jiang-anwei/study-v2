package com.icekredit.pdf.entities.pie_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.EmptyCell;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 6/6/16.
 */
public class SuperPieChart extends PdfPTable {
    protected PdfWriter pdfWriter;

    protected String pieChartTitle;

    protected float[] sectorsAngle;
    protected BaseColor[] sectorsColor;
    protected String[] sectorsDesc;

    protected int identifierShownAs;

    protected boolean isNeedDrawCover;

    public SuperPieChart(String pieChartTitle,
                         float[] sectorsAngle, BaseColor[] sectorsColor,
                         String[] sectorsDesc,int identifierShownAs,boolean isNeedDrawCover) {
        this.pdfWriter = pdfWriter;
        this.pieChartTitle = pieChartTitle;
        this.sectorsAngle = sectorsAngle;
        this.sectorsColor = sectorsColor;
        this.sectorsDesc = sectorsDesc;

        this.identifierShownAs = identifierShownAs;
        this.isNeedDrawCover = isNeedDrawCover;

        this.setWidthPercentage(100);
        this.resetColumnCount(12);
        initTableContent();
    }

    private void initTableContent() {
        //添加标题
        if(pieChartTitle != null && !pieChartTitle.trim().equals("")){
            AlignCenterCell pieChartTitleCell = new AlignCenterCell(
                    new Phrase(new Chunk(pieChartTitle,
                            new Font(FontUtils.baseFontChinese,12,Font.BOLD,new BaseColor(0x00,0x00,0x00,0xff)))));
            pieChartTitleCell.setColspan(12);
            this.addCell(pieChartTitleCell);
        }

        String [] percentageDesc = getPercentageDesc();

//        PieChartCell pieChartCell = new PieChartCell(35,26,72,sectorsAngle,sectorsColor,percentageDesc,isNeedDrawCover,true,sectorsDesc,sectorsColor);
//        pieChartCell.setColspan(12);

//        this.addCell(pieChartCell);
    }

    public static void main(String [] args){
        try{
            String destFileStr = "results/object/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            SuperPieChart superPieChart = new SuperPieChart("标题",new float[]{48,60,72,84,96},new BaseColor[]{
                    new BaseColor(0xff,0x00,0x00,0xff),
                    new BaseColor(0x00,0xff,0x00,0xff),
                    new BaseColor(0x00,0x00,0xff,0xff),
                    new BaseColor(0xff,0xff,0x00,0xff),
                    new BaseColor(0xff,0x00,0xff,0xff)
            },new String[]{
                    "Red One",
                    "Green One",
                    "Blue One",
                    "Yellow One",
                    "Yan One"
            },SuperPieChartCell.IDENTIFIER_SHOWN_AS_CIRCLE,false);

            document.add(superPieChart);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] getPercentageDesc() {
        String [] percentageDesc = new String[sectorsAngle.length];

        float total = 0;
        for(float sectorAngle:sectorsAngle){
            total += sectorAngle;
        }

        int index = 0;
        for(float sectorAngle:sectorsAngle){
            percentageDesc[index] = (((int)(sectorAngle / total * 10000)) * 1.0 / 100) + "%";

            index++;
        }

        return percentageDesc;
    }
}
