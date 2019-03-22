package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Created by icekredit on 4/20/16.
 */
public abstract class DrawCurveController {
    public abstract Point calculateNextCurveToPositon(Point startPoint, Point endPoint, float currentStepRatio);
    public abstract void drawCurve(PdfContentByte canvas, Point startPoint, Point endPoint);
}
