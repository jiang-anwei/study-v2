package com.icekredit.pdf.entities;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

/**
 * Created by icekredit on 8/30/16.
 */
public class AlignCenterImageCell extends BaseCell implements PdfPCellEvent{
    private PdfWriter pdfWriter;
    private Image image;

    public AlignCenterImageCell(PdfWriter pdfWriter,Image image) {
        this.pdfWriter = pdfWriter;
        this.image = image;
        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            float imageWidth = image.getWidth();
            float imageHeight = image.getHeight();

            if(position.getBottom() - 36 < imageHeight){
                pdfWriter.newPage();
            }

            Rectangle positionRect = new Rectangle(
                    (position.getLeft() + position.getRight()) / 2 - imageWidth / 2,
                    (position.getTop() + position.getBottom()) / 2 - imageHeight / 2,
                    (position.getLeft() + position.getRight()) / 2 + imageWidth / 2,
                    (position.getTop() + position.getBottom()) / 2 + imageHeight / 2
            );

            ColumnText columnText = new ColumnText(canvases[PdfPTable.BASECANVAS]);
            columnText.setSimpleColumn(positionRect);
            columnText.addElement(image);
            columnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
