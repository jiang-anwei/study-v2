package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by icekredit on 4/19/16.
 */
public abstract class View {
    public abstract void draw();

    protected void testCalculatedVertices(PdfWriter pdfWriter, Point[] testVertices, int testVerticesCount) {
        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();
        canvas.setLineWidth(0.1);
        canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));
        canvas.setColorFill(new BaseColor(0x98,0xbf,0x21,0xff));

        for(int index = 0;index < testVerticesCount;index ++){
            canvas.circle(testVertices[index].x,testVertices[index].y,1);
        }

        canvas.closePathFillStroke();

        canvas.restoreState();
    }

    protected void testCalculatedVertices(PdfContentByte canvas, Point[] testVertices, int testVerticesCount) {
        canvas.saveState();
        canvas.setLineWidth(0.1);
        canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));
        canvas.setColorFill(new BaseColor(0x98,0xbf,0x21,0xff));

        for(int index = 0;index < testVerticesCount;index ++){
            canvas.circle(testVertices[index].x,testVertices[index].y,1);
        }

        canvas.closePathFillStroke();

        canvas.restoreState();
    }
}
