package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.AlignLeftCell;
import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.FontUtils;
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
public class IdentifierForCurrentCompanyCell extends IdentifierCell {
    private static final String TAG = "IdentifierForCurrentCompanyCell";

    private IdentifierForCurrentCompany identifierForCurrentCompany ;

    public IdentifierForCurrentCompanyCell(PdfWriter pdfWriter, int showAs,BaseColor[] multiLevelCircleColors) {
        super(pdfWriter,showAs,multiLevelCircleColors);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        identifierForCurrentCompany = new IdentifierForCurrentCompany(pdfWriter,(position.getRight() + position.getLeft()) / 2,
                (position.getTop() + position.getBottom()) / 2,
                (position.getRight() - position.getLeft()),
                (position.getRight() - position.getLeft()),
                showAs,
                new float[]{(position.getRight() - position.getLeft()) * 0.45f,(position.getRight() - position.getLeft()) * 0.4f},
                multiLevelCircleColors);

        identifierForCurrentCompany.draw();
    }

    public Point[] getInvestBranchVertices() {
        return identifierForCurrentCompany.getInvestBranchVertices();
    }

    public void setInvestBranchVertices(Point[] investBranchVertices) {
        this.identifierForCurrentCompany.setInvestBranchVertices(investBranchVertices);
    }

    public Point[] getStockHolderBranchesVertices() {
        return identifierForCurrentCompany.getStockHolderBranchesVertices();
    }

    public void setStockHolderBranchesVertices(Point[] stockHolderBranchesVertices) {
        this.setStockHolderBranchesVertices(stockHolderBranchesVertices);
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            PdfPCell emptyCellPre = new PdfPCell(new Phrase(new Chunk("Test")));
            emptyCellPre.setColspan(15);

            PdfPCell identifierCell = new IdentifierForCurrentCompanyCell(writer, Identifier.SHOW_AS_CIRCLE,
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    });
            identifierCell.setColspan(3);
            identifierCell.setFixedHeight(100);


            PdfPCell emptyCellPost = new AlignLeftCell(new Phrase(new Chunk("南京同创信息产业集团有限公司", FontUtils.chineseFont)));
            emptyCellPost.setColspan(15);


            PdfPTable mainFrame = new PdfPTable(33);
            mainFrame.setWidthPercentage(100);

            mainFrame.addCell(emptyCellPre);
            mainFrame.addCell(identifierCell);
            mainFrame.addCell(emptyCellPost);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
