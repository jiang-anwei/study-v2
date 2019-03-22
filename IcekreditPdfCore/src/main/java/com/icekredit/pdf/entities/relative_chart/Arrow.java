package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
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
public class Arrow extends View {
    protected float centerXPosition;
    protected static final float DEFAULT_CENTER_X_POSITION = 100;
    protected float centerYPosition;
    protected static final float DEFAULT_CENTER_Y_POSITION = 100;

    protected float circumcircleRadius;
    protected static final float DEFAULT_CIRCUMCIRCLE_RADIUS = 20;

    protected PdfWriter pdfWriter;
    protected BaseColor arrowFillColor;
    protected static final BaseColor DEFAULT_ARROW_FILL_COLOR = new BaseColor(0x77,0x77,0x77,0xff);

    protected float expandAngle;
    protected static final float DEFAULT_EXPAND_ANGLE = 60;

    protected Point arrowPointFromVertex;
    protected Point arrowPointToVertex;

    protected int directionPointTo;
    public static final int POINT_TO_DIRECTION_UP = 0;
    public static final int POINT_TO_DIRECTION_DOWN = 1;
    public static final int POINT_TO_DIRECTION_LEFT = 2;
    public static final int POINT_TO_DIRECTION_RIGHT = 3;
    protected static final int DEFAULT_DIRECTION_POINT_TO = POINT_TO_DIRECTION_DOWN;

    public Arrow(PdfWriter pdfWriter) {
        this(pdfWriter, DEFAULT_ARROW_FILL_COLOR);
    }

    public Arrow(PdfWriter pdfWriter, BaseColor arrowFillColor) {
        this(pdfWriter,arrowFillColor,DEFAULT_CENTER_X_POSITION,DEFAULT_CENTER_Y_POSITION,DEFAULT_CIRCUMCIRCLE_RADIUS);
    }

    public Arrow(PdfWriter pdfWriter, BaseColor arrowFillColor,float centerXPosition, float centerYPosition, float circumcircleRadius) {
        this(pdfWriter,arrowFillColor,centerXPosition,centerYPosition,circumcircleRadius,DEFAULT_EXPAND_ANGLE);
    }

    public Arrow(PdfWriter pdfWriter, BaseColor arrowFillColor,float centerXPosition,
                 float centerYPosition, float circumcircleRadius,float expandAngle) {
        this(pdfWriter,arrowFillColor,centerXPosition,centerYPosition,circumcircleRadius,expandAngle,DEFAULT_DIRECTION_POINT_TO);
    }

    public Arrow(PdfWriter pdfWriter, BaseColor arrowFillColor,float centerXPosition,
                 float centerYPosition, float circumcircleRadius,float expandAngle,int directionPointTo) {
        this.centerXPosition = centerXPosition;
        this.centerYPosition = centerYPosition;
        this.circumcircleRadius = circumcircleRadius;

        this.pdfWriter = pdfWriter;
        this.arrowFillColor = arrowFillColor;
        this.expandAngle = expandAngle;

        this.directionPointTo = directionPointTo;

        initGlobalScope();
    }

    private void initGlobalScope(){
        arrowPointFromVertex = new Point(0,0);
        arrowPointToVertex = new Point(0,0);

        arrowPointFromVertex.x = centerXPosition;
        arrowPointFromVertex.y = centerYPosition;
        switch (directionPointTo){
            case POINT_TO_DIRECTION_UP:
                arrowPointToVertex.x = centerXPosition;
                arrowPointToVertex.y = centerYPosition + circumcircleRadius;
                break;
            case POINT_TO_DIRECTION_DOWN:
                arrowPointToVertex.x = centerXPosition;
                arrowPointToVertex.y = centerYPosition - circumcircleRadius;
                break;
            case POINT_TO_DIRECTION_LEFT:
                arrowPointToVertex.x = centerXPosition - circumcircleRadius;
                arrowPointToVertex.y = centerYPosition;
                break;
            case POINT_TO_DIRECTION_RIGHT:
                arrowPointToVertex.x = centerXPosition + circumcircleRadius;
                arrowPointToVertex.y = centerYPosition;
                break;
        }
    }

    public void draw() {
        //计算出勾画箭头需要的顶点信息
        Point[] arrowVertices = getArrowVertices();

        //勾画箭头
        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();
        canvas.setColorStroke(arrowFillColor);
        canvas.setColorFill(arrowFillColor);

        canvas.moveTo(arrowVertices[0].x,arrowVertices[0].y);
        canvas.lineTo(arrowVertices[1].x,arrowVertices[1].y);
        canvas.lineTo(arrowVertices[2].x,arrowVertices[2].y);
        canvas.lineTo(arrowVertices[3].x,arrowVertices[3].y);

        canvas.closePathFillStroke();
        canvas.restoreState();

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(pdfWriter,new Point[]{arrowPointFromVertex},1);
            testCalculatedVertices(pdfWriter,new Point[]{arrowPointToVertex},1);
        }
    }

    private Point[] getArrowVertices(){
        Point[] arrowVertices = new Point[4];

        for(int index = 0;index < 4;index ++){
            arrowVertices[index] = new Point(0,0);
        }

        //计算出从0度开始逆时针方向第一个顶点的偏移角度
        float angleForFirstVertex = 0;
        switch (directionPointTo){
            case POINT_TO_DIRECTION_UP:
                angleForFirstVertex = (float) ((Math.PI - Math.PI * 2 * expandAngle / 360) / 2 + Math.PI);
                break;
            case POINT_TO_DIRECTION_DOWN:
                angleForFirstVertex = (float) (Math.PI - Math.PI * 2 * expandAngle / 360) / 2;
                break;
            case POINT_TO_DIRECTION_LEFT:
                angleForFirstVertex = (float) ((Math.PI - Math.PI * 2 * expandAngle / 360) / 2 + Math.PI * 3 / 2);
                break;
            case POINT_TO_DIRECTION_RIGHT:
                angleForFirstVertex = (float) ((Math.PI - Math.PI * 2 * expandAngle / 360) / 2 + Math.PI / 2);
                break;
        }

        arrowVertices[0].x = (float) (centerXPosition + circumcircleRadius * Math.cos(angleForFirstVertex));
        arrowVertices[0].y = (float) (centerYPosition + circumcircleRadius * Math.sin(angleForFirstVertex));

        arrowVertices[1].x = centerXPosition;
        arrowVertices[1].y = centerYPosition;

        arrowVertices[2].x = (float) (centerXPosition + circumcircleRadius * Math.cos(angleForFirstVertex + expandAngle * Math.PI * 2 / 360));
        arrowVertices[2].y = (float) (centerYPosition + circumcircleRadius * Math.sin(angleForFirstVertex + expandAngle * Math.PI * 2 / 360));

        arrowVertices[3].x = (float) (centerXPosition + circumcircleRadius * Math.cos(
                angleForFirstVertex + expandAngle * Math.PI * 2 / 360 / 2 + Math.PI));
        arrowVertices[3].y = (float) (centerYPosition + circumcircleRadius * Math.sin(
                angleForFirstVertex + expandAngle * Math.PI * 2 / 360 / 2 + Math.PI));

        return arrowVertices;
    }

    public Point getArrowPointToVertex() {
        return arrowPointToVertex;
    }

    public void setArrowPointToVertex(Point arrowPointToVertex) {
        this.arrowPointToVertex = arrowPointToVertex;
    }

    public Point getArrowPointFromVertex() {
        return arrowPointFromVertex;
    }

    public void setArrowPointFromVertex(Point arrowPointFromVertex) {
        this.arrowPointFromVertex = arrowPointFromVertex;
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            new Arrow(writer).draw();
            new Arrow(writer,new BaseColor(0x98,0xbf,0x21,0xff),100,50,10,60, Arrow.POINT_TO_DIRECTION_UP).draw();
            new Arrow(writer,new BaseColor(0x98,0xbf,0x21,0xff),200,50,10,60, Arrow.POINT_TO_DIRECTION_DOWN).draw();
            new Arrow(writer,new BaseColor(0x98,0xbf,0x21,0xff),300,50,10,60, Arrow.POINT_TO_DIRECTION_LEFT).draw();
            new Arrow(writer,new BaseColor(0x98,0xbf,0x21,0xff),400,50,10,60, Arrow.POINT_TO_DIRECTION_RIGHT).draw();
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
