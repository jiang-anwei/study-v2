package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/20/16.
 */
public class SinCosDrawCurveController extends DrawCurveController {
    private static Point currentPoint =  new Point(0,0);

    private float cursor;
    private static final float DEFAULT_CURSOR = 0.6f;

    private float adjustAngleOffset;
    private static final float DEFAULT_ADJUST_ANGLE_OFFSET = (float) Math.PI / 12;
    private float adjustAngleSpan;
    private static final float DEFAULT_ADJUST_ANGLE_SPAN =  (float) (Math.PI / 2 - DEFAULT_ADJUST_ANGLE_OFFSET * 2);

    private BaseColor curveColor = null;
    private static final BaseColor DEFAULT_CURVE_COLOR = new BaseColor(0xe0,0xe0,0xe0,0xff);
    private float stepRatio;
    private static final float DEFAULT_STEP_RATIO = 0.02f;

    public SinCosDrawCurveController() {
        this(DEFAULT_CURSOR,DEFAULT_ADJUST_ANGLE_OFFSET,DEFAULT_ADJUST_ANGLE_SPAN,DEFAULT_CURVE_COLOR,DEFAULT_STEP_RATIO);
    }

    public SinCosDrawCurveController(float cursor, float adjustAngleOffset,
                                     float adjustAngleSpan, BaseColor curveColor, float stepRatio) {
        this.cursor = cursor;
        this.adjustAngleOffset = adjustAngleOffset;
        this.adjustAngleSpan = adjustAngleSpan;
        this.curveColor = curveColor;
        this.stepRatio = stepRatio;
    }

    @Override
    public Point calculateNextCurveToPositon(Point startPoint, Point endPoint, float currentStepRatio) {
        float xSpan = endPoint.x - startPoint.x;
        float ySpan = endPoint.y - startPoint.y;

        if(currentStepRatio < 0.5){
            currentPoint.x = startPoint.x + (float) (xSpan * cursor * (1 - Math.cos(
                    adjustAngleOffset + adjustAngleSpan * currentStepRatio * 2)));
            currentPoint.y = startPoint.y + (float) (ySpan * cursor * Math.sin(
                    adjustAngleOffset + adjustAngleSpan * currentStepRatio * 2));
        }else {
            currentPoint.x = startPoint.x + xSpan  * cursor + (float) (xSpan * (1 - cursor) * Math.sin(
                    adjustAngleOffset + adjustAngleSpan * (currentStepRatio - 0.5) * 2));
            currentPoint.y = startPoint.y + ySpan  * cursor + (float) (ySpan * (1 - cursor) * (1 - Math.cos(
                    adjustAngleOffset + adjustAngleSpan  * (currentStepRatio - 0.5) * 2)));
        }

        return currentPoint;
    }

    @Override
    public void drawCurve(PdfContentByte canvas, Point startPoint, Point endPoint){
        /*canvas.saveState();
        canvas.setColorStroke(new BaseColor(0xe0,0xe0,0xe0,0xff));
        canvas.moveTo(startPoint.x,startPoint.y);
        canvas.lineTo(endPoint.x,endPoint.y);
        canvas.stroke();
        canvas.closePath();
        canvas.restoreState();*/

        //用于测试，画出需要勾画的曲线的起点以及终点
        /*canvas.saveState();
        canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
        canvas.setColorStroke(new BaseColor(0x98,0xbf,0x21,0xff));
        canvas.circle(startPoint.x,startPoint.y,1);
        canvas.circle(endPoint.x,endPoint.y,1);
        canvas.closePathFillStroke();
        canvas.restoreState();*/


        canvas.saveState();
        canvas.setLineWidth(0.2);
        canvas.setColorStroke(curveColor);

        Point currentPosition;

        canvas.moveTo(startPoint.x,startPoint.y);
        for(float currentRatio = 0.0f;currentRatio < 1.01;currentRatio += stepRatio){
            currentPosition = calculateNextCurveToPositon(startPoint,endPoint,currentRatio);

            canvas.lineTo(currentPosition.x,currentPosition.y);
        }
        canvas.lineTo(endPoint.x,endPoint.y);

        canvas.stroke();
        canvas.closePath();
        canvas.restoreState();
    }

    public static void main(String [] args){
        Point startPoint = new Point(60,180);
        Point endPoint = new Point(10,100);
        SinCosDrawCurveController controller = new SinCosDrawCurveController();

        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            PdfContentByte canvas = writer.getDirectContent();

            canvas.saveState();
            canvas.setColorFill(new BaseColor(0x98,0xbf,0x21,0xff));
            canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));

            Point currentPosition;
            for(float currentRatio = 0.0f;currentRatio < 1.05;currentRatio += 0.05f){
                currentPosition = controller.calculateNextCurveToPositon(startPoint,endPoint,currentRatio);

                //canvas.circle(currentPosition.x,currentPosition.y,2);

                canvas.closePathFillStroke();
            }

            canvas.restoreState();


            Point firstPosition = controller.calculateNextCurveToPositon(startPoint,endPoint,0);
            canvas.moveTo(firstPosition.x,firstPosition.y);

            /*canvas.saveState();
            canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
            canvas.setColorStroke(new BaseColor(0x98,0xbf,0x21,0xff));

            for(float currentStepRatio = 0.0f;currentStepRatio < 1.05;currentStepRatio += 0.05f){
                currentPosition = controller.calculateNextCurveToPositon(startPoint,endPoint,currentStepRatio);

                canvas.lineTo(currentPosition.x,currentPosition.y);
            }

            canvas.closePathFillStroke();
            canvas.restoreState();*/

            /*canvas.saveState();
            canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
            canvas.setColorStroke(new BaseColor(0x98,0xbf,0x21,0xff));

            Point firstPoint = controller.calculateNextCurveToPositon(startPoint,endPoint,0);
            Point secondPoint = controller.calculateNextCurveToPositon(startPoint,endPoint,0.05f);

            canvas.moveTo(firstPoint.x,firstPoint.y);
            for(float currentStepRatio = 0.1f;currentStepRatio < 1.05;currentStepRatio += 0.05f){
                currentPosition = controller.calculateNextCurveToPositon(startPoint,endPoint,currentStepRatio);

                canvas.curveTo(firstPoint.x,firstPosition.y,secondPoint.x,secondPoint.y,currentPosition.x,currentPosition.y);

                firstPoint = secondPoint;
                secondPoint = currentPosition;
            }

            canvas.closePathFillStroke();
            canvas.restoreState();*/

            canvas.saveState();
            canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
            canvas.setColorStroke(new BaseColor(0x98,0xbf,0x21,0xff));

            Point lastPoint = controller.calculateNextCurveToPositon(startPoint,endPoint,0);

            canvas.moveTo(lastPoint.x,lastPoint.y);
            for(float currentStepRatio = 0.02f;currentStepRatio < 1.02;currentStepRatio += 0.02f){
                currentPosition = controller.calculateNextCurveToPositon(startPoint,endPoint,currentStepRatio);

                canvas.curveTo(lastPoint.x,lastPoint.y,currentPosition.x,currentPosition.y);

                lastPoint = currentPosition;
            }

            canvas.stroke();
            canvas.closePath();
            canvas.restoreState();

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
