package com.icekredit.pdf.entities.mark;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.View;
import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;

/**
 * Created by icekredit on 8/2/16.
 */
public class NegativeMark extends Mark {
    private static final int NEGATIVE_MARK_VERTICES_COUNT = 4;

    public NegativeMark(float centerXPosition, float centerYPosition, float outerRadius, int showAs, BaseColor backgroundColor, BaseColor foregroundColor) {
        super(centerXPosition, centerYPosition, outerRadius, MARK_TYPE_NEGATIVE, showAs, backgroundColor, foregroundColor);

        this.backgroundColor = ColorUtil.strRGBAToColor("ff9300ff");
        this.foregroundColor = ColorUtil.strRGBAToColor("ffffffff");
    }

    @Override
    protected Point[] getForeMarkVertices() {
        Point[] vertices = getInitializedVertices(NEGATIVE_MARK_VERTICES_COUNT);

        float longerSideLength = outerRadius * 2 * View.GOLDEN_SPLIT_RIGHT;
        float shorterSideLength = outerRadius * 2 * View.GOLDEN_SPLIT_RIGHT * View.GOLDEN_SPLIT_RIGHT;
        float rectangleWidth = shorterSideLength * RECTANGLE_WIDTH_RATIO;

        vertices[0].x = centerXPosition - longerSideLength / 2;
        vertices[0].y = centerYPosition + rectangleWidth / 2;
        vertices[1].x = centerXPosition - longerSideLength / 2;
        vertices[1].y = centerYPosition - rectangleWidth / 2;
        vertices[2].x = centerXPosition + longerSideLength / 2;
        vertices[2].y = centerYPosition - rectangleWidth / 2;
        vertices[3].x = centerXPosition + longerSideLength / 2;
        vertices[3].y = centerYPosition + rectangleWidth;

        return vertices;
    }
}
