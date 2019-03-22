package com.icekredit.pdf.entities.pie_chart;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.relative_chart.View;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 4/28/16.
 */
public class PieChart extends View {
    protected PdfWriter pdfWriter;

    protected float outerCircleRadius;
    protected static final int DEFAULT_OUTER_CIRCLE_RADIUS = 35;
    protected float innerCircleRadius;
    protected static final int DEFAULT_INNER_CIRCLE_RADIUS = 31;

    protected int calcStepCount;       //用多少步数来画一个整个的外圆圆弧
    protected static final int DEFAULT_CALC_STEP_COUNT = 72;

    protected float [] sectorsAngle;
    protected static final float [] DEFAULT_SECTORS_ANGLE = new float[]{48,60,72,84,96};

    protected BaseColor [] sectorsColor;
    protected static final BaseColor [] DEFAULT_SECTORS_COLOR = new BaseColor[]{
            new BaseColor(0xff,0x00,0x00,0xff),
            new BaseColor(0x00,0xff,0x00,0xff),
            new BaseColor(0x00,0x00,0xff,0xff),
            new BaseColor(0xff,0xff,0x00,0xff),
            new BaseColor(0xff,0x00,0xff,0xff)
    };

    protected String [] sectorsDesc;
    protected static final String [] DEFAULT_SECTORS_DESC = new String[]{
            "Red One",
            "Green One",
            "Blue One",
            "Yellow One",
            "Yan One"
    };
    protected int sectorDescFontSize;
    protected static final int DEFAULT_SECTOR_DESC_FONT_SIZE = 4;
    protected int sectorDescFontStyle;
    protected static final int DEFAULT_SETCTOR_DESC_FONT_STYLE = Font.NORMAL;
    protected BaseColor sectorDescFontColor;
    protected static final BaseColor DEFAULT_SECTOR_DESC_FONT_COLOR = new BaseColor(0x55,0x55,0x55,0xff);


    protected float centerXPosition;
    protected static final int DEFAULT_CENTER_X_POSITION = 100;
    protected float centerYPosition;
    protected static final int DEFAULT_CENTER_Y_POSITION = 100;

    protected static final float LENGTH_OF_HODE_UP_LINE = 10;
    protected static final float EXTRA_PADDING_FOR_COLUMN_TEXT = 4;

    protected boolean isNeedDrawDesc;
    protected static final boolean DEFAULT_IS_NEED_DRAW_DESC = true;

    protected boolean isNeedDrawCover;
    protected static final boolean DEFAULT_IS_NEED_DRAW_COVER  = true;

    private static final float THRESHOLD_SECTOR_ANGLE = 36;

    public PieChart(PdfWriter pdfWriter) {
        this(pdfWriter,DEFAULT_CENTER_X_POSITION,DEFAULT_CENTER_Y_POSITION,
                DEFAULT_OUTER_CIRCLE_RADIUS,DEFAULT_INNER_CIRCLE_RADIUS,
                DEFAULT_CALC_STEP_COUNT,DEFAULT_SECTORS_ANGLE,DEFAULT_SECTORS_COLOR,
                DEFAULT_SECTORS_DESC);
    }

    public PieChart(PdfWriter pdfWriter, float centerXPosition, float centerYPosition
            , float outerCircleRadius, float innerCircleRadius, int calcStepCount,
                    float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc) {
        this(pdfWriter, centerXPosition, centerYPosition,
                outerCircleRadius, innerCircleRadius,
                calcStepCount, sectorsAngle, sectorsColor,
                sectorsDesc, DEFAULT_SECTOR_DESC_FONT_SIZE,
                DEFAULT_SETCTOR_DESC_FONT_STYLE,DEFAULT_SECTOR_DESC_FONT_COLOR);
    }

    public PieChart(PdfWriter pdfWriter, float centerXPosition, float centerYPosition
            , float outerCircleRadius, float innerCircleRadius, int calcStepCount,
                    float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc,
                    int sectorDescFontSize, int sectorDescFontStyle, BaseColor sectorDescFontColor){
        this(pdfWriter, centerXPosition, centerYPosition,
                outerCircleRadius, innerCircleRadius,
                calcStepCount, sectorsAngle,
                sectorsColor, sectorsDesc,
                sectorDescFontSize, sectorDescFontStyle, sectorDescFontColor,DEFAULT_IS_NEED_DRAW_DESC,DEFAULT_IS_NEED_DRAW_DESC);
    }

    public PieChart(PdfWriter pdfWriter, float centerXPosition, float centerYPosition
                    , float outerCircleRadius, float innerCircleRadius, int calcStepCount,
                    float[] sectorsAngle, BaseColor[] sectorsColor, String[] sectorsDesc,
                    int sectorDescFontSize, int sectorDescFontStyle, BaseColor sectorDescFontColor,
                    boolean isNeedDrawCover, boolean isNeedDrawDesc) {
        this.pdfWriter = pdfWriter;
        this.centerXPosition = centerXPosition;
        this.centerYPosition = centerYPosition;
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;
        this.calcStepCount = calcStepCount;
        this.sectorsAngle = sectorsAngle;
        this.sectorsColor = sectorsColor;
        this.sectorsDesc = sectorsDesc;

        this.sectorDescFontSize = sectorDescFontSize;
        this.sectorDescFontStyle = sectorDescFontStyle;
        this.sectorDescFontColor = sectorDescFontColor;

        this.isNeedDrawCover = isNeedDrawCover;
        this.isNeedDrawDesc = isNeedDrawDesc;
    }


    @Override
    public void draw() {
        PdfContentByte canvas = pdfWriter.getDirectContent();

        try {
            drawSectors(canvas);

            if(isNeedDrawCover){
                drawCover(canvas);
            }

            if(isNeedDrawDesc){
                // 如果当前扇区比较大，那么就会有足够的空间来在扇区内部画一个描述信息，否则就依次将描述信息画在饼图右侧指定区域由下往上计算的一系列矩形区域内。
                drawSectorsDesc();
                drawRemainingSectorsDesc();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawCover(PdfContentByte canvas) {
        //画出覆盖圆
        canvas.saveState();
        canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
        canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));
        canvas.setLineWidth(0.1f);

        canvas.circle(centerXPosition,centerYPosition, innerCircleRadius);
        canvas.closePathFillStroke();

        canvas.restoreState();
    }

    private void drawSectors(PdfContentByte canvas) {
        float currentSectorAngleOffset = 0;//(float) (Math.PI / 2) * 0;

        float currentSectorAngle;
        BaseColor currentSectorBGColor;
        Point[] sectorVertices = null;

        canvas.saveState();
        canvas.setLineWidth(0.1);
        canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));

        //画出表示比例的扇区
        for (int index = 0;index < sectorsAngle.length;index ++){
            currentSectorAngle = sectorsAngle[index];
            currentSectorBGColor = sectorsColor[index];

            sectorVertices = getSectorVertices(currentSectorAngleOffset,currentSectorAngle,
                    outerCircleRadius,centerXPosition,centerYPosition);

            //用于测试，画出所有顶点
            if (Debug.DEBUG_FLAG){
                testCalculatedVertices(pdfWriter,sectorVertices,sectorVertices.length);
            }

            canvas.setColorFill(currentSectorBGColor);
            canvas.moveTo(sectorVertices[0].x,sectorVertices[0].y);

            for(int position = 1;position < sectorVertices.length;position ++){
                canvas.lineTo(sectorVertices[position].x,sectorVertices[position].y);
            }

            canvas.closePathFillStroke();

            currentSectorAngleOffset += currentSectorAngle;
        }

        canvas.restoreState();
    }

    private Point[] getSectorVertices(float currentSectorAngleOffset, float currentSectorAngle,
                                      float outerCircleRadius, float centerXPosition, float centerYPosition) {
        //计算出画当前扇区需要的顶点的数目
        int sectorVerticesCount = Math.round(currentSectorAngle / 360 * DEFAULT_CALC_STEP_COUNT + 0.5f) + 1 + 1;

        Point[] sectorVertices = new Point[sectorVerticesCount];

        //初始化
        for(int index = 0;index < sectorVerticesCount;index ++){
            sectorVertices[index] = new Point(0,0);
        }

        sectorVertices[0].x = centerXPosition;
        sectorVertices[0].y = centerYPosition;

        for(int index = 1;index < sectorVerticesCount;index ++){
            sectorVertices[index].x = (float) (centerXPosition + outerCircleRadius
                                * Math.cos(
                    currentSectorAngleOffset * Math.PI * 2 / 360
                    + (index - 1) * 1.0f / (sectorVerticesCount - 2) * currentSectorAngle * Math.PI * 2 / 360));
            sectorVertices[index].y = (float) (centerYPosition + outerCircleRadius
                    * Math.sin(currentSectorAngleOffset * Math.PI * 2 / 360
                    + (index - 1) * 1.0f / (sectorVerticesCount - 2) * currentSectorAngle * Math.PI * 2 / 360));
        }

        return sectorVertices;
    }

    /**
     * 根据当前所画的描述信息的数目确定其应该处于的位置
     * @param currentDrawDescCount
     * @return
     */
    private Point getNextDescStrLeftVertex(int currentDrawDescCount){
        //得到右侧用于画具体描述信息的矩形区域左下角
        float llx = centerXPosition + outerCircleRadius + 20;
        float lly = centerYPosition - outerCircleRadius;

        float strHeight = DrawTextUtil.getStringHeight(sectorDescFontSize);

        Point referPoint = new Point(0,0);

        referPoint.x = llx;
        referPoint.y = lly + currentDrawDescCount * strHeight - strHeight / 2;

        return referPoint;
    }

    /*private void drawSectorsDesc() throws DocumentException {
        Point [] descHodeUpLinesVertices = getDescHodeUpLinesVertices(sectorsAngle,sectorsDesc,
                centerXPosition,centerYPosition,outerCircleRadius);

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(pdfWriter,descHodeUpLinesVertices,descHodeUpLinesVertices.length);
        }

        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();
        ColumnText columnText = null;
        for(int index = 0;index < descHodeUpLinesVertices.length / 3;index ++){
            canvas.moveTo(descHodeUpLinesVertices[index * 3 + 0].x,descHodeUpLinesVertices[index * 3 + 0].y);
            canvas.lineTo(descHodeUpLinesVertices[index * 3 + 1].x,descHodeUpLinesVertices[index * 3 + 1].y);
            canvas.lineTo(descHodeUpLinesVertices[index * 3 + 2].x,descHodeUpLinesVertices[index * 3 + 2].y);

            Phrase sectorDescPhrase = new Phrase(sectorsDesc[index],new Font(
                    FontUtils.baseFontChinese,sectorDescFontSize,sectorDescFontStyle,
                    sectorsColor[index]));
            columnText = new ColumnText(canvas);
            columnText.setSimpleColumn(new Rectangle(
                    descHodeUpLinesVertices[index * 3 + 1].x < descHodeUpLinesVertices[index * 3 + 2].x
                            ? descHodeUpLinesVertices[index * 3 + 1].x : descHodeUpLinesVertices[index * 3 + 2].x,
                    descHodeUpLinesVertices[index * 3 + 1].y,
                    descHodeUpLinesVertices[index * 3 + 1].x > descHodeUpLinesVertices[index * 3 + 2].x
                            ? descHodeUpLinesVertices[index * 3 + 1].x : descHodeUpLinesVertices[index * 3 + 2].x,
                    descHodeUpLinesVertices[index * 3 + 1].y + FontUtils.baseFontChinese.getWidthPoint("中文",sectorDescFontSize) / 2));
            columnText.setUseAscender(true);
            columnText.addText(sectorDescPhrase);
            columnText.go();

            canvas.setColorStroke(sectorsColor[index]);
            canvas.setLineWidth(0.1);
            canvas.stroke();
        }

        canvas.restoreState();

    }*/

    private void drawRemainingSectorsDesc() throws DocumentException {
        Point[] arcCenterVertices = getDescCenterVertices(sectorsAngle,sectorsDesc,
                centerXPosition,centerYPosition,outerCircleRadius,1f);

        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();
        ColumnText columnText = null;

        Point referPoint;
        int currentDescCountDrawn = 1;
        for(int index = 0;index < sectorsDesc.length;index ++){
            if(sectorsAngle[index] >= THRESHOLD_SECTOR_ANGLE){
                continue;
            }

            referPoint = getNextDescStrLeftVertex(currentDescCountDrawn);

            canvas.setLineWidth(0.2f);
            canvas.setColorStroke(sectorDescFontColor);
            canvas.moveTo(arcCenterVertices[index].x,arcCenterVertices[index].y);
            canvas.lineTo(referPoint.x - 5,referPoint.y);
            canvas.lineTo(referPoint.x,referPoint.y);
            canvas.stroke();


            Phrase sectorDescPhrase = new Phrase(sectorsDesc[index],new Font(
                    FontUtils.baseFontChinese,sectorDescFontSize,sectorDescFontStyle,
                    sectorDescFontColor));

            float strWidth = FontUtils.baseFontChinese.getWidthPoint(sectorsDesc[index],sectorDescFontSize);

            columnText = new ColumnText(canvas);
            columnText.setSimpleColumn(new Rectangle(
                    referPoint.x,
                    referPoint.y - DrawTextUtil.getStringHeight(sectorDescFontSize) / 2,
                    referPoint.x + strWidth,
                    referPoint.y + DrawTextUtil.getStringHeight(sectorDescFontSize) / 2));

            columnText.setUseAscender(true);
            columnText.addText(sectorDescPhrase);
            columnText.go();

            canvas.setColorStroke(sectorsColor[index]);
            canvas.setLineWidth(0.1);
            canvas.stroke();

            currentDescCountDrawn ++;
        }
        canvas.restoreState();
    }

    private void drawSectorsDesc() throws DocumentException {
        Point[] descCenterVertices = getDescCenterVertices(sectorsAngle,sectorsDesc,
                centerXPosition,centerYPosition,outerCircleRadius,isNeedDrawCover ? 1.2f : 0.8f);

        if(Debug.DEBUG_FLAG){
            testCalculatedVertices(pdfWriter,descCenterVertices,descCenterVertices.length);
        }

        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();
        ColumnText columnText = null;
        for(int index = 0;index < descCenterVertices.length;index ++){
            if(sectorsAngle[index] < THRESHOLD_SECTOR_ANGLE){
                continue;
            }

            Phrase sectorDescPhrase = new Phrase(sectorsDesc[index],new Font(
                    FontUtils.baseFontChinese,sectorDescFontSize,sectorDescFontStyle,
                    sectorDescFontColor));

            float strWidth = FontUtils.baseFontChinese.getWidthPoint(sectorsDesc[index],sectorDescFontSize);

            columnText = new ColumnText(canvas);
            columnText.setSimpleColumn(new Rectangle(
                    descCenterVertices[index ].x - strWidth / 2,
                    descCenterVertices[index].y - DrawTextUtil.getStringHeight(sectorDescFontSize) / 2,
                    descCenterVertices[index].x + strWidth / 2 ,
                    descCenterVertices[index].y + DrawTextUtil.getStringHeight(sectorDescFontSize) / 2));

            columnText.setUseAscender(true);
            columnText.addText(sectorDescPhrase);
            columnText.go();

            canvas.setColorStroke(sectorsColor[index]);
            canvas.setLineWidth(0.1);
            canvas.stroke();
        }

        canvas.restoreState();

    }

    private Point[] getDescHodeUpLinesVertices(float[] sectorsAngle, String[] sectorsDesc,
                                               float centerXPosition, float centerYPosition, float outerCircleRadius) {
        float [] sectorArcCenterVerticesAngle = getSectorArcCenterVerticesAngle(sectorsAngle);

        Point[] descHodeUpLinesVertices = new Point[sectorsDesc.length * 3];

        for(int index = 0;index < descHodeUpLinesVertices.length;index ++){
            descHodeUpLinesVertices[index] = new Point(0,0);
        }

        float currentVectorArcCenterXPosition ;
        float currentVectorArcCenterYPosition ;

        for(int index = 0;index < sectorsDesc.length;index ++){
            currentVectorArcCenterXPosition = (float) (centerXPosition +
                                outerCircleRadius * Math.cos(sectorArcCenterVerticesAngle[index]));

            currentVectorArcCenterYPosition = (float) (centerYPosition +
                    outerCircleRadius * Math.sin(sectorArcCenterVerticesAngle[index]));

            descHodeUpLinesVertices[index * 3 + 0].x = currentVectorArcCenterXPosition;
            descHodeUpLinesVertices[index * 3 + 0].y = currentVectorArcCenterYPosition;

            for(int position = 0;position < sectorArcCenterVerticesAngle.length;position ++){
                while (true){
                    if(sectorArcCenterVerticesAngle[position] > 0 && sectorArcCenterVerticesAngle[position] < Math.PI * 2){
                        break;
                    }

                    if (sectorArcCenterVerticesAngle[position] < 0){
                        sectorArcCenterVerticesAngle[position] += Math.PI * 2;
                    }

                    if(sectorArcCenterVerticesAngle[position] > Math.PI * 2){
                        sectorArcCenterVerticesAngle[position] -= Math.PI * 2;
                    }
                }
            }

            if(sectorArcCenterVerticesAngle[index] >= 0 && sectorArcCenterVerticesAngle[index] < Math.PI * 1/ 2){
                //第一象限
                descHodeUpLinesVertices[index * 3 + 1].x =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].x
                                + LENGTH_OF_HODE_UP_LINE * Math.cos(Math.PI / 4));
                descHodeUpLinesVertices[index * 3 + 1].y =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].y
                                + LENGTH_OF_HODE_UP_LINE * Math.sin(Math.PI / 4));
                descHodeUpLinesVertices[index * 3 + 2].x = descHodeUpLinesVertices[index * 3 + 1].x
                        + FontUtils.baseFontChinese.getWidthPoint(sectorsDesc[index],sectorDescFontSize) + EXTRA_PADDING_FOR_COLUMN_TEXT;
                descHodeUpLinesVertices[index * 3 + 2].y = descHodeUpLinesVertices[index * 3 + 1].y;
            }else if (sectorArcCenterVerticesAngle[index] >= Math.PI * 1 / 2 && sectorArcCenterVerticesAngle[index] < Math.PI * 2 / 2){
                //第二象限
                descHodeUpLinesVertices[index * 3 + 1].x =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].x
                                + LENGTH_OF_HODE_UP_LINE * Math.cos(Math.PI / 4 + Math.PI * 1 / 2));
                descHodeUpLinesVertices[index * 3 + 1].y =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].y
                                + LENGTH_OF_HODE_UP_LINE * Math.sin(Math.PI / 4 + Math.PI * 1 / 2));
                descHodeUpLinesVertices[index * 3 + 2].x = descHodeUpLinesVertices[index * 3 + 1].x
                        - FontUtils.baseFontChinese.getWidthPoint(sectorsDesc[index],sectorDescFontSize) - EXTRA_PADDING_FOR_COLUMN_TEXT;
                descHodeUpLinesVertices[index * 3 + 2].y = descHodeUpLinesVertices[index * 3 + 1].y;
            }else if (sectorArcCenterVerticesAngle[index] >= Math.PI * 2 / 2 && sectorArcCenterVerticesAngle[index] < Math.PI * 3 / 2){
                //第三象限
                descHodeUpLinesVertices[index * 3 + 1].x =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].x
                                + LENGTH_OF_HODE_UP_LINE * Math.cos(Math.PI / 4 + Math.PI * 2 / 2));
                descHodeUpLinesVertices[index * 3 + 1].y =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].y
                                + LENGTH_OF_HODE_UP_LINE * Math.sin(Math.PI / 4 + Math.PI * 2 / 2));
                descHodeUpLinesVertices[index * 3 + 2].x = descHodeUpLinesVertices[index * 3 + 1].x
                        - FontUtils.baseFontChinese.getWidthPoint(sectorsDesc[index],sectorDescFontSize) - EXTRA_PADDING_FOR_COLUMN_TEXT;
                descHodeUpLinesVertices[index * 3 + 2].y = descHodeUpLinesVertices[index * 3 + 1].y;
            }else if (sectorArcCenterVerticesAngle[index] >= Math.PI * 3 / 2 && sectorArcCenterVerticesAngle[index] < Math.PI * 4 / 2){
                //第四象限
                descHodeUpLinesVertices[index * 3 + 1].x =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].x
                                + LENGTH_OF_HODE_UP_LINE * Math.cos(Math.PI / 4 + Math.PI * 3 / 2));
                descHodeUpLinesVertices[index * 3 + 1].y =
                        (float) (descHodeUpLinesVertices[index * 3 + 0].y
                                + LENGTH_OF_HODE_UP_LINE * Math.sin(Math.PI / 4 + Math.PI * 3/ 2));
                descHodeUpLinesVertices[index * 3 + 2].x = descHodeUpLinesVertices[index * 3 + 1].x
                        + FontUtils.baseFontChinese.getWidthPoint(sectorsDesc[index],sectorDescFontSize) + EXTRA_PADDING_FOR_COLUMN_TEXT;
                descHodeUpLinesVertices[index * 3 + 2].y = descHodeUpLinesVertices[index * 3 + 1].y;
            }
        }

        return descHodeUpLinesVertices;
    }


    private Point[] getDescCenterVertices(float[] sectorsAngle, String[] sectorsDesc,
                                          float centerXPosition, float centerYPosition,
                                          float outerCircleRadius, float positionPercentage) {
        float [] sectorArcCenterVerticesAngle = getSectorArcCenterVerticesAngle(sectorsAngle);

        Point[] descCenterVertices = new Point[sectorsDesc.length];

        for(int index = 0;index < descCenterVertices.length;index ++){
            descCenterVertices[index] = new Point(0,0);
        }

        for(int index = 0;index < sectorsDesc.length;index ++){
            descCenterVertices[index].x = (float) (centerXPosition +
                    outerCircleRadius * positionPercentage * Math.cos(sectorArcCenterVerticesAngle[index]));;
            descCenterVertices[index].y = (float) (centerYPosition +
                    outerCircleRadius * positionPercentage * Math.sin(sectorArcCenterVerticesAngle[index]));;
        }

        return descCenterVertices;
    }


    public float[] getSectorArcCenterVerticesAngle(float [] sectorsAngle) {
        float [] sectorArcCenterVerticesAngle = new float[sectorsAngle.length];

        float sectorAngleOffset = (float) (Math.PI / 2) * 0;

        for(int index = 0;index < sectorsAngle.length;index ++){
            sectorArcCenterVerticesAngle[index] = (float) (sectorAngleOffset + sectorsAngle[index] * Math.PI * 2 / 360 /2);

            sectorAngleOffset += sectorsAngle[index] * Math.PI * 2 / 360;
        }

        return sectorArcCenterVerticesAngle;
    }

    public static void main(String [] args){
        try{
            String dest = "results/objects/test.pdf";
            File destFile = new File(dest);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));
            document.open();

            new PieChart(pdfWriter).draw();

            document.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
