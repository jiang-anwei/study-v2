package com.icekredit.pdf.entities.pie_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 6/6/16.
 */
public class SectorDescChartCell extends AlignCenterCell implements PdfPCellEvent {
    protected PdfWriter pdfWriter;
    protected String desc;
    protected BaseColor descIconColor;

    protected int identifierShownAs;

    public SectorDescChartCell(PdfWriter pdfWriter, String desc, BaseColor descIconColor,int identifierShownAs) {
        this.pdfWriter = pdfWriter;
        this.desc = desc;
        this.descIconColor = descIconColor;
        this.identifierShownAs =identifierShownAs;

        this.setFixedHeight(10);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        SectorDescChart sectorDescChart = new SectorDescChart(
                pdfWriter,desc,descIconColor,
                position.getLeft(),position.getBottom(),
                position.getRight()-position.getLeft(),
                position.getTop() - position.getBottom(),identifierShownAs);
        sectorDescChart.draw();
    }

    public static void main(String [] args){
        try{
            String destFileStr = "results/object/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            SectorDescChartCell sectorDescChartCell = new SectorDescChartCell(pdfWriter,"张三",new BaseColor(0x98,0xbf,0x21,0xff), SuperPieChartCell.IDENTIFIER_SHOWN_AS_CIRCLE);
            sectorDescChartCell.setColspan(6);

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            mainFrame.addCell(sectorDescChartCell);
            mainFrame.addCell(sectorDescChartCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
