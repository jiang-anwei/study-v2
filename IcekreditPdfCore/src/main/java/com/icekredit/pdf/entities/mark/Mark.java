package com.icekredit.pdf.entities.mark;

import com.icekredit.pdf.entities.core.MessageConfig;
import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.View;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Created by icekredit on 7/21/16.
 */
public class Mark extends View {
    protected int markType;
    public static final int MARK_TYPE_RIGHT = 0;
    public static final int MARK_TYPE_WRONG = 1;
    public static final int MARK_TYPE_POSITIVE = 2;
    public static final int MARK_TYPE_NEGATIVE = 3;
    public static final int MARK_TYPE_UNKNOWN = 4;

    protected BaseColor backgroundColor;
    protected BaseColor foregroundColor;

    protected float centerXPosition;
    protected float centerYPosition;
    protected float outerRadius;

    protected int showAs;

    protected static final float RECTANGLE_WIDTH_RATIO = 1.0f / 5;

    public Mark(float centerXPosition, float centerYPosition, float outerRadius,
                int markType, int showAs,BaseColor backgroundColor,BaseColor foregroundColor) {
        this.centerXPosition = centerXPosition;
        this.centerYPosition = centerYPosition;
        this.outerRadius = outerRadius;
        this.showAs = showAs;
        this.markType = markType;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void draw(PdfContentByte canvas) {

        canvas.saveState();
        canvas.setLineWidth(0.1f);
        canvas.setColorFill(backgroundColor);
        canvas.setColorStroke(backgroundColor);
        switch (showAs){
            case MessageConfig.IDENTIFIER_SHOW_AS_CIRCLE:
                canvas.circle(centerXPosition,centerYPosition,outerRadius);
                canvas.fillStroke();
                break;
            case MessageConfig.IDENTIFIER_SHOW_AS_RECTANGLE:
                canvas.rectangle(centerXPosition - outerRadius,centerYPosition - outerRadius,
                        outerRadius * 2,outerRadius * 2);
                canvas.fillStroke();
                break;
        }

        canvas.setColorFill(foregroundColor);
        canvas.setColorStroke(foregroundColor);

        Point[] vertices = getForeMarkVertices();
        for (int index = 0; index < vertices.length / 4; index++) {
            canvas.moveTo(vertices[index * 4 + 0].x, vertices[index * 4 + 0].y);
            canvas.lineTo(vertices[index * 4 + 1].x, vertices[index * 4 + 1].y);
            canvas.lineTo(vertices[index * 4 + 2].x, vertices[index * 4 + 2].y);
            canvas.lineTo(vertices[index * 4 + 3].x, vertices[index * 4 + 3].y);

            canvas.fillStroke();
        }

        canvas.restoreState();
    }

    protected Point[] getForeMarkVertices(){
        return new Point[]{};
    }

    protected Point[] getInitializedVertices(int verticesCount){
        Point[] vertices = new Point[verticesCount];

        for(int index = 0;index < verticesCount;index ++){
            vertices[index] = new Point(0,0);
        }

        return vertices;
    }
}
