package com.icekredit.pdf.entities.line_chart;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.relative_chart.View;
import com.icekredit.pdf.utils.Debug;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/20/16.
 */
public class Line extends View {
    protected PdfContentByte canvas;

    protected Point[] vertices;
    protected float dash;
    protected float gap;

    protected float lineWidth;
    protected static final float DEFAULT_LINE_WIDTH = 0.1f;
    protected BaseColor lineColor;
    protected static final BaseColor DEFAULT_LINE_COLOR = new BaseColor(0xee,0xee,0xee,0xff);

    protected boolean isNeedShowNode;
    protected int noodShownAs;
    public static final int NODE_SHOWN_AS_CIRCLE = 0;
    public static final int NODE_SHOWN_AS_RECTANGLE = 1;

    public Line(PdfWriter pdfWriter, Point[] vertices, float dash, float gap){
        this(pdfWriter, vertices, dash, gap,DEFAULT_LINE_WIDTH,DEFAULT_LINE_COLOR);
    }

    public Line(PdfWriter pdfWriter, Point[] vertices, float dash, float gap, float lineWidth, BaseColor lineColor){
        this(pdfWriter, vertices, dash, gap, lineWidth, lineColor,false,NODE_SHOWN_AS_CIRCLE);
    }

    public Line(PdfWriter pdfWriter, Point[] vertices, float dash, float gap, float lineWidth, BaseColor lineColor,boolean isNeedShowNode,int nodeShownAs) {
        this.canvas = pdfWriter.getDirectContent();

        this.vertices = vertices;
        this.dash = dash;
        this.gap = gap;

        this.lineWidth = lineWidth;
        this.lineColor = lineColor;

        this.isNeedShowNode = isNeedShowNode;
        this.noodShownAs = nodeShownAs;
    }
    public Line(PdfContentByte canvas, Point[] vertices, float dash, float gap, float lineWidth, BaseColor lineColor,boolean isNeedShowNode,int nodeShownAs) {
        this.canvas = canvas;
        this.vertices = vertices;
        this.dash = dash;
        this.gap = gap;

        this.lineWidth = lineWidth;
        this.lineColor = lineColor;

        this.isNeedShowNode = isNeedShowNode;
        this.noodShownAs = nodeShownAs;
    }


    @Override
    public void draw() {
        Point startPoint;
        Point endPoint;

        float gradientSinValue;
        float gradientCosValue;

        canvas.saveState();
        canvas.setColorStroke(lineColor);
        canvas.setColorFill(lineColor);
        canvas.setLineWidth(lineWidth);

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(canvas,vertices,vertices.length);
        }

        //如果只有一个点不能画线的话，直接画一个点
        if(vertices.length == 1){
            canvas.moveTo(vertices[0].x - 5,vertices[0].y);
            canvas.lineTo(vertices[0].x + 5,vertices[0].y);

            canvas.circle(vertices[0].x,vertices[0].y,1);
            canvas.fillStroke();
        }

        for(int index = 0;index < vertices.length - 1;index ++){
            startPoint = vertices[index];
            endPoint = vertices[index + 1];

            if(isNeedShowNode){
                switch (noodShownAs){
                    case NODE_SHOWN_AS_CIRCLE:
                        canvas.circle(startPoint.x,startPoint.y,1);
                        canvas.circle(endPoint.x,endPoint.y,1);
                        break;
                    case NODE_SHOWN_AS_RECTANGLE:
                        canvas.rectangle(startPoint.x - 1,startPoint.y - 1,2,2);
                        canvas.rectangle(endPoint.x - 1,endPoint.y - 1,2,2);
                        break;
                }
                canvas.fillStroke();
            }

            gradientSinValue = (float) ((endPoint.y - startPoint.y) / (
                    Math.sqrt(Math.pow((endPoint.x - startPoint.x),2) + Math.pow(endPoint.y - startPoint.y,2))));
            gradientCosValue = (float) ((endPoint.x - startPoint.x) / (
                    Math.sqrt(Math.pow((endPoint.x - startPoint.x),2) + Math.pow(endPoint.y - startPoint.y,2))));


            //从startVertex开始，循环画出dashwidth 以及dashgap，直至没有充足的空间画整个dashwidth
            float xPosition;
            float yPostion;

            float reverseXCondition = gradientCosValue == 0 ? 1 : gradientCosValue / Math.abs(gradientCosValue);
            float reverseYCondition = gradientSinValue == 0 ? 1 : gradientSinValue / Math.abs(gradientSinValue);

            for(xPosition = startPoint.x,yPostion = startPoint.y;
                reverseXCondition * xPosition <= reverseXCondition * (endPoint.x - dash * gradientCosValue)
                        && reverseYCondition * yPostion <= reverseYCondition * (endPoint.y - dash * gradientSinValue);
                xPosition += (dash + gap) * gradientCosValue,yPostion += (dash + gap) * gradientSinValue){

                canvas.moveTo(xPosition, yPostion);
                canvas.lineTo(xPosition + dash * gradientCosValue, yPostion + dash * gradientSinValue);
                canvas.stroke();
            }

            //由于如果所画直线可能沿坐标轴方向，所以上述循环结束条件必须包含等于边界的情况，因为必须保证gradientCosValue，gradientSinValue
            //为零的时候可以进入循环，但是包含边界之后，循环会多执行一次，所以此处添加回退代码
            if(gradientSinValue == 0){
                xPosition -= (dash + gap) * gradientCosValue;
            }

            if(gradientCosValue == 0){
                yPostion -= (dash + gap) * gradientSinValue;
            }


            //画收尾的一点dash
            canvas.moveTo(xPosition, yPostion);
            canvas.lineTo(endPoint.x, endPoint.y);
            canvas.stroke();
        }

        canvas.restoreState();
    }

    public static void main(String [] args){
        try{
            String destStr = "results/object/test.pdf";
            File destFile = new File(destStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();
            Line line;

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(200,200)},10,5);
            //line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(200,100)},10,5);
//            line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(200,0)},10,5);
            //line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(100,0)},10,3);
//            line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(0,0)},10,5);
            //line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(0,100)},10,5);
//            line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(0,200)},10,5);
            //line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(100,100),new Point(100,200)},10,3);
//            line.draw();

            line = new Line(pdfWriter,new Point[]{new Point(50,50),new Point(50,100),new Point(100,100),new Point(100,50),new Point(50,50)},10,0);
            line.draw();

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
