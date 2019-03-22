package com.icekredit.pdf.entities;

import com.icekredit.pdf.utils.Debug;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Created by icekredit on 7/19/16.
 */
public abstract class View {
    public static final float GOLDEN_SPLIT_RIGHT = 0.618f;

    public abstract void draw(PdfContentByte canvas);

    public static void testCalculatedVertices(PdfContentByte canvas, Point[] testVertices, int testVerticesCount) {
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

    public static void showPosition(PdfContentByte canvas, Rectangle position){
        if(Debug.DEBUG_FLAG){
            View.testCalculatedVertices(canvas,
                    new Point[]{new Point((int)position.getLeft(),(int)position.getBottom()),
                            new Point((int)position.getRight(),(int)position.getTop())},2);
        }
    }
}
