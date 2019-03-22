package com.icekredit.pdf.entities.mark;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.BaseColor;

/**
 * Created by icekredit on 8/2/16.
 */
public class UnknownMark extends Mark {
    public UnknownMark(float centerXPosition, float centerYPosition, float outerRadius, int showAs, BaseColor backgroundColor, BaseColor foregroundColor) {
        super(centerXPosition, centerYPosition, outerRadius, MARK_TYPE_UNKNOWN, showAs, backgroundColor, foregroundColor);
    }

    @Override
    protected Point[] getForeMarkVertices() {
        return super.getForeMarkVertices();
    }
}
