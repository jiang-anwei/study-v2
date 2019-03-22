package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/19/16.
 */
public class Identifier extends View {
    protected PdfWriter pdfWriter = null;
    protected static final float GOLDEN_SPLITE_RATE = 0.618f;

    protected float centerXPosition;
    protected float centerYPosition;

    protected static final int MAX_CIRCLE_LEVEL = 5;     //标识符圆圈层数最多五层

    protected float identifierWidth = 0;
    protected static final float DEFAULT_IDENTIFIER_WIDTH = 100 / (1 - GOLDEN_SPLITE_RATE);
    protected float identifierHeight = 0;
    protected static final float DEFAULT_IDENTIFIER_HEIGHT = 200;


    protected float [] multiLevelCircleRadiuses;    //标识符每一级圆圈层数半径
    protected static final float [] DEFAULT_MULTI_LEVEL_CIRCLE_RADIUSES = new float[]{
            100,90,70,50,30
    };
    protected static final float DEFAULT_MAX_LEVEL_CIRCLE_RADIUS = 100;

    protected BaseColor [] multiLevelCircleColors;    //标识符每一级圆圈颜色
    protected static final BaseColor [] DEFAULT_MULTI_LEVEL_CIRCLE_COLORS = new BaseColor[]{
            new BaseColor(0xff,0x00,0x00,0xff),
            new BaseColor(0x00,0xff,0x00,0xff),
            new BaseColor(0x00,0x00,0xff,0xff),
            new BaseColor(0xff,0xff,0x00,0xff),
            new BaseColor(0x00,0xff,0xff,0xff)
    };

   /* protected String description;
    protected static final String DEFAULT_DEACRIPTION = "";
    protected BaseFont descriptionBaseFont;
    protected static final BaseFont DEFAULT_DESCRIPTION_BASE_FONT = FontUtils.baseFontChinese;
    protected int descriptionFontSize;
    protected static final int DEFAULT_DESCRIPTION_FONT_SIZE = 8;
    protected int descriptionFontStyle;
    protected static final int DEFAULT_DESCRIPTION_FONT_STYLE = Font.NORMAL;
    protected BaseColor descriptionFontColor;
    protected static final BaseColor DEFAULT_DESCRIPTION_FONT_COLOR = new BaseColor(0x33,0x33,0x33,0xff);*/

    protected int showAs;
    protected static final int SHOW_AS_RECTANGLE = 0;
    protected static final int SHOW_AS_CIRCLE = 1;

    protected Point relatedAsStockholderVertex = null;
    protected Point relatedAsInvestVertex = null;

    public Identifier(PdfWriter pdfWriter,float centerXPosition, float centerYPosition) {
        this(pdfWriter,centerXPosition,centerYPosition,
                DEFAULT_IDENTIFIER_WIDTH,DEFAULT_IDENTIFIER_HEIGHT,SHOW_AS_CIRCLE);
    }

    public Identifier(PdfWriter pdfWriter,float centerXPosition, float centerYPosition,
                      float identifierWidth, float identifierHeight,int showAs) {
        this(pdfWriter, centerXPosition, centerYPosition,
                identifierWidth, identifierHeight, showAs,
                DEFAULT_MULTI_LEVEL_CIRCLE_RADIUSES, DEFAULT_MULTI_LEVEL_CIRCLE_COLORS);
    }

    public Identifier(PdfWriter pdfWriter,float centerXPosition, float centerYPosition,
                      float identifierWidth, float identifierHeight,int showAs,
                      float[] multiLevelCircleRadiuses, BaseColor[] multiLevelCircleColors) {
        this.pdfWriter = pdfWriter;
        this.centerXPosition = centerXPosition;
        this.centerYPosition = centerYPosition;
        this.identifierWidth = identifierWidth;
        this.identifierHeight = identifierHeight;
        this.multiLevelCircleRadiuses = multiLevelCircleRadiuses;
        this.multiLevelCircleColors = multiLevelCircleColors;
        this.showAs = showAs;

        initGlobalScope();
    }

    private void initGlobalScope() {
        relatedAsStockholderVertex = new Point(0,0);
        relatedAsInvestVertex = new Point(0,0);

        float centerXPosition = 0;
        float centerYPosition = 0;
        float maxLevelCircleRadius = 0;

        centerXPosition = this.centerXPosition;
        centerYPosition = this.centerYPosition;
        if(multiLevelCircleRadiuses.length > 0){
            maxLevelCircleRadius = multiLevelCircleRadiuses[0];
        }else {
            maxLevelCircleRadius = DEFAULT_MULTI_LEVEL_CIRCLE_RADIUSES[0];
        }

        relatedAsStockholderVertex.x = centerXPosition;
        relatedAsStockholderVertex.y = centerYPosition - maxLevelCircleRadius;

        relatedAsInvestVertex.x = centerXPosition;
        relatedAsInvestVertex.y = centerYPosition + maxLevelCircleRadius;
    }

    public void draw(){
        drawIdentifierTag();
    }

    private void drawIdentifierTag() {
        PdfContentByte canvas = this.pdfWriter.getDirectContent();

        canvas.saveState();
        canvas.setLineWidth(0.1);

        canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));

        int index = 0;
        BaseColor currentLevelColor = null;

        for(float circleRadius:multiLevelCircleRadiuses){
            if(index < multiLevelCircleColors.length){    //如果用户的确提供了当前一级的图形填充色，那么使用用户提供的颜色
                currentLevelColor = multiLevelCircleColors[index];
            }else {    //否则使用默认的当前级别图形填充色
                currentLevelColor = DEFAULT_MULTI_LEVEL_CIRCLE_COLORS[index];
            }

            canvas.setColorFill(currentLevelColor);    //将画板填充色设置为当前级别图形应该使用的颜色

            if(showAs == SHOW_AS_CIRCLE){      //如果需要以圆形模式展示identifier
                canvas.circle(centerXPosition,centerYPosition,circleRadius);
            }else {     //如果需要以u矩形模式展示identifier
                canvas.rectangle(centerXPosition - circleRadius,centerYPosition - circleRadius,
                        circleRadius * 2,circleRadius * 2);
            }

            index ++;
            canvas.closePathFillStroke();
        }

        canvas.restoreState();
    }

    public Point getRelatedAsInvestVertex() {
        return relatedAsInvestVertex;
    }

    public void setRelatedAsInvestVertex(Point relatedAsInvestVertex) {
        this.relatedAsInvestVertex = relatedAsInvestVertex;
    }

    public Point getRelatedAsStockholderVertex() {
        return relatedAsStockholderVertex;
    }

    public void setRelatedAsStockholderVertex(Point relatedAsStockholderVertex) {
        this.relatedAsStockholderVertex = relatedAsStockholderVertex;
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            /*new Identifier(writer,200,100).draw();
            new Identifier(writer,200,300,100,100,0).draw();

            new Identifier(writer,400,100,40,30,0,new float[]{20,17},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    },"Description for test test for description").draw();*/
            new Identifier(writer,400,300,20,16,1,new float[]{10,8},
                    new BaseColor[]{
                            new BaseColor(0xff,0x00,0x00,0xff),
                            new BaseColor(0x00,0xff,0x00,0xff)
                    }).draw();
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
