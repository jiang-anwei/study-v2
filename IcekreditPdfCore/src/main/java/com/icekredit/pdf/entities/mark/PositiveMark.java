package com.icekredit.pdf.entities.mark;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;

/**
 * Created by icekredit on 8/2/16.
 */
public class PositiveMark extends Mark {
    private static final int POSITIVE_MARK_VERTICES_COUNT = 8;
    public PositiveMark(float centerXPosition, float centerYPosition, float outerRadius, int showAs, BaseColor backgroundColor, BaseColor foregroundColor) {
        super(centerXPosition, centerYPosition, outerRadius, MARK_TYPE_POSITIVE, showAs, backgroundColor, foregroundColor);

        this.backgroundColor = ColorUtil.strRGBAToColor("00bed9ff");
        this.foregroundColor = ColorUtil.strRGBAToColor("ffffffff");
    }

    @Override
    protected Point[] getForeMarkVertices() {
        Point[] vertices = getInitializedVertices(POSITIVE_MARK_VERTICES_COUNT);

        float longerSideLength = outerRadius * 2 * GOLDEN_SPLIT_RIGHT;
        float shorterSideLength = outerRadius * 2 * GOLDEN_SPLIT_RIGHT * GOLDEN_SPLIT_RIGHT;
        float rectangleWidth = shorterSideLength * RECTANGLE_WIDTH_RATIO;

        vertices[0].x = centerXPosition - longerSideLength / 2;
        vertices[0].y = centerYPosition + rectangleWidth / 2;
        vertices[1].x = centerXPosition - longerSideLength / 2;
        vertices[1].y = centerYPosition - rectangleWidth / 2;
        vertices[2].x = centerXPosition + longerSideLength / 2;
        vertices[2].y = centerYPosition - rectangleWidth / 2;
        vertices[3].x = centerXPosition + longerSideLength / 2;
        vertices[3].y = centerYPosition + rectangleWidth / 2;
        vertices[4].x = centerXPosition + rectangleWidth / 2;
        vertices[4].y = centerYPosition + longerSideLength / 2;
        vertices[5].x = centerXPosition - rectangleWidth / 2;
        vertices[5].y = centerYPosition + longerSideLength / 2;
        vertices[6].x = centerXPosition - rectangleWidth / 2;
        vertices[6].y = centerYPosition - longerSideLength / 2;
        vertices[7].x = centerXPosition + rectangleWidth / 2;
        vertices[7].y = centerYPosition - longerSideLength / 2;

        return vertices;
    }
}
