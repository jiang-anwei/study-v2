package com.icekredit.pdf.entities.mark;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.View;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.Graphics2DUtil;
import com.itextpdf.text.BaseColor;

/**
 * Created by icekredit on 8/2/16.
 */
public class RightMark extends Mark {
    private static final int RIGHT_MARK_VERTICES_COUNT = 8;

    public RightMark(float centerXPosition, float centerYPosition, float outerRadius, int showAs, BaseColor backgroundColor, BaseColor foregroundColor) {
        super(centerXPosition, centerYPosition, outerRadius, MARK_TYPE_RIGHT, showAs, backgroundColor, foregroundColor);

        this.backgroundColor = ColorUtil.strRGBAToColor("59ca70ff");
        this.foregroundColor = ColorUtil.strRGBAToColor("ffffffff");
    }

    @Override
    protected Point[] getForeMarkVertices() {
        Point[] vertices = getInitializedVertices(RIGHT_MARK_VERTICES_COUNT);

        float longerSideLength = outerRadius * 2 * View.GOLDEN_SPLIT_RIGHT;
        float shorterSideLength = outerRadius * 2 * View.GOLDEN_SPLIT_RIGHT * View.GOLDEN_SPLIT_RIGHT;
        float rectangleWidth = shorterSideLength * RECTANGLE_WIDTH_RATIO;

        vertices[0].x = centerXPosition - shorterSideLength / 2;
        vertices[0].y = centerYPosition - longerSideLength / 2 + rectangleWidth;
        vertices[1].x = centerXPosition - shorterSideLength / 2;
        vertices[1].y = centerYPosition - longerSideLength / 2;
        vertices[2].x = centerXPosition + shorterSideLength / 2;
        vertices[2].y = centerYPosition - longerSideLength / 2;
        vertices[3].x = centerXPosition + shorterSideLength / 2;
        vertices[3].y = centerYPosition - longerSideLength / 2 + rectangleWidth;
        vertices[4].x = centerXPosition + shorterSideLength / 2;
        vertices[4].y = centerYPosition - longerSideLength / 2 + rectangleWidth;
        vertices[5].x = centerXPosition + shorterSideLength / 2;
        vertices[5].y = centerYPosition + longerSideLength / 2;
        vertices[6].x = centerXPosition + shorterSideLength / 2 - rectangleWidth;
        vertices[6].y = centerYPosition + longerSideLength / 2;
        vertices[7].x = centerXPosition + shorterSideLength / 2 - rectangleWidth;
        vertices[7].y = centerYPosition - longerSideLength / 2 + rectangleWidth;

        Point referPoint = new Point(centerXPosition,centerYPosition);
        for(int index = 0;index < RIGHT_MARK_VERTICES_COUNT;index ++){
            Graphics2DUtil.rotateVector(vertices[index],referPoint, (float) (Math.PI / 4));
        }

        return vertices;
    }
}
