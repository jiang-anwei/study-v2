package com.icekredit.pdf.entities.score;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icekredit on 9/27/16.
 */
public class RoseChartCell extends BaseCell implements PdfPCellEvent {
    protected float outerCircleRadius;
    protected List<RoseChartSectorAttr> sectorsAttr;
    protected float min;
    protected float max;
    protected boolean isNeedDrawCover;

    protected int columnCount;

    private static final int DEFAULT_CALC_STEP_COUNT = 72;//用多少步数来画一个整个的外圆圆弧

    protected static final float DEFAULT_CELL_HEIGHT = 240;
    protected static final float DEFAULT_MARGIN = 4;
    protected static final float DEFAULT_PADDING = 4;

    protected static final float SECTOR_DESC_ITEM_HEIGHT = 20;

    public RoseChartCell(float outerCircleRadius, List<RoseChartSectorAttr> sectorsAttr, float min, float max, boolean isNeedDrawCover) {
        this.outerCircleRadius = outerCircleRadius;
        this.sectorsAttr = sectorsAttr;
        this.min = min;
        this.max = max;
        this.isNeedDrawCover = isNeedDrawCover;

        this.columnCount = 2;

        this.setFixedHeight(DEFAULT_CELL_HEIGHT + getSectorDescRowCount(sectorsAttr,columnCount) * SECTOR_DESC_ITEM_HEIGHT);
        this.setCellEvent(this);
    }

    private int getSectorDescRowCount(List<RoseChartSectorAttr> sectorsAttr,int columnCount) {
        return (sectorsAttr.size() + 2) / columnCount;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try{
            PdfContentByte canvas = canvases[PdfPTable.BASECANVAS];

            float centerXPosition = (position.getLeft() + position.getRight()) / 2;
            float centerYPosition = position.getTop() - DEFAULT_CELL_HEIGHT / 2;

            /**
             * 画外部的边界圆框
             */
            if(true){
                canvas.saveState();

                canvas.setLineWidth(1);
                canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
                canvas.setColorStroke(new BaseColor(0xee,0xee,0xee,0xff));

                canvas.circle(centerXPosition,centerYPosition,outerCircleRadius);
                canvas.stroke();

                canvas.restoreState();
            }

            drawSectors(canvas,centerXPosition,centerYPosition,sectorsAttr);

            if(isNeedDrawCover){
                canvas.saveState();

                canvas.setLineWidth(0);
                canvas.setColorFill(new BaseColor(0xff,0xff,0xff,0xff));
                canvas.setColorStroke(new BaseColor(0xff,0xff,0xff,0xff));

                canvas.circle(centerXPosition,centerYPosition,outerCircleRadius * min * 0.95f / max);
                canvas.closePathFillStroke();

                canvas.restoreState();
            }

            /*if(sectorsAttr.size() <= 6){
                drawSectorsDesc(canvas,sectorsAttr,centerXPosition,centerYPosition);
            } else {*/
                drawSectorsDescPanel(canvas,sectorsAttr,position.getTop() - DEFAULT_CELL_HEIGHT,
                        position.getLeft() + position.getWidth() * 0,
                        position.getRight() - position.getWidth() * 0,columnCount);
            //}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawSectorsDescPanel(PdfContentByte canvas, List<RoseChartSectorAttr> sectorsAttr, float top,float left,float right,int columnCount) {
        try {
            String sectorDesc = null;
            BaseColor sectorBgColor = null;

            float descItemWidth = (right - left) / columnCount;

            for(int index = 0;index < sectorsAttr.size();index ++){
                sectorDesc = sectorsAttr.get(index).sectorDesc;
                sectorBgColor = sectorsAttr.get(index).sectorBgColor;

                drawSectorDescItem(canvas,sectorDesc,sectorBgColor,
                        new Rectangle(
                                left + index % columnCount * descItemWidth,
                                top - (index / columnCount + 1) * SECTOR_DESC_ITEM_HEIGHT,
                                left + (index % columnCount + 1) * descItemWidth,
                                top - (index / columnCount) * SECTOR_DESC_ITEM_HEIGHT));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawSectorDescItem(PdfContentByte canvas, String sectorDesc, BaseColor sectorBgColor, Rectangle position) {
        try {
            int marginLeft = 2;
            int marginRight = 5;
            float radius = position.getHeight() * 0.6f / 2;

            canvas.saveState();

            canvas.setLineWidth(0);
            canvas.setColorStroke(sectorBgColor);
            canvas.setColorFill(sectorBgColor);
            canvas.circle(position.getLeft() + marginLeft + radius,(position.getTop() + position.getBottom()) / 2,radius);
            canvas.fillStroke();

            float strHeight = DrawTextUtil.getStringHeight(FontUtils.fontSize);

            ColumnText columnText = new ColumnText(canvas);
            columnText.setSimpleColumn(new Rectangle(
                    position.getLeft() + marginLeft + radius * 2 + marginRight,
                    (position.getBottom() + position.getTop()) / 2 - strHeight,
                    position.getLeft() + marginLeft + radius * 2 + marginRight + FontUtils.baseFontChinese.getWidthPoint(sectorDesc,FontUtils.fontSize),
                    (position.getBottom() + position.getTop()) / 2 + strHeight));
            columnText.addElement(new Phrase(new Chunk(sectorDesc,FontUtils.chineseFont)));
            columnText.go();

            canvas.restoreState();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawSectors(PdfContentByte canvas, float centerXPosition,float centerYPosition,List<RoseChartSectorAttr> sectorsAttr) {
        float currentSectorAngleOffset = 0;//(float) (Math.PI / 2) * 0;

        float currentSectorAngle;
        BaseColor currentSectorBGColor;
        Point[] sectorVertices = null;

        canvas.saveState();
        canvas.setLineWidth(0.1);

        //画出表示比例的扇区
        for (int index = 0;index < sectorsAttr.size();index ++){
            currentSectorAngle = sectorsAttr.get(index).sectorAngle;
            currentSectorBGColor = sectorsAttr.get(index).sectorBgColor;

            sectorVertices = getSectorVertices(currentSectorAngleOffset,currentSectorAngle,
                    outerCircleRadius,centerXPosition,centerYPosition,max,sectorsAttr.get(index).sectorAttrValue);

            canvas.setColorFill(currentSectorBGColor);
            canvas.setColorStroke(currentSectorBGColor);
            canvas.moveTo(sectorVertices[0].x,sectorVertices[0].y);

            for(int position = 1;position < sectorVertices.length;position ++){
                canvas.lineTo(sectorVertices[position].x,sectorVertices[position].y);
            }

            canvas.closePathFillStroke();

            currentSectorAngleOffset += currentSectorAngle;
        }

        canvas.restoreState();
    }

    private Point[] getSectorVertices(
            float currentSectorAngleOffset, float currentSectorAngle,
            float outerCircleRadius, float centerXPosition, float centerYPosition,
            float max,float sectorAttrValue) {
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
            sectorVertices[index].x = (float) (centerXPosition + outerCircleRadius * (sectorAttrValue / max)
                    * Math.cos(
                    currentSectorAngleOffset * Math.PI * 2 / 360
                            + (index - 1) * 1.0f / (sectorVerticesCount - 2) * currentSectorAngle * Math.PI * 2 / 360));
            sectorVertices[index].y = (float) (centerYPosition + outerCircleRadius * (sectorAttrValue / max)
                    * Math.sin(currentSectorAngleOffset * Math.PI * 2 / 360
                    + (index - 1) * 1.0f / (sectorVerticesCount - 2) * currentSectorAngle * Math.PI * 2 / 360));
        }

        return sectorVertices;
    }

    private void drawSectorsDesc(PdfContentByte canvas, List<RoseChartSectorAttr> roseChartSectorAttrs,float centerXPosition,float centerYPosition) throws DocumentException {
        Point[] descCenterVertices = getDescCenterVertices(roseChartSectorAttrs,centerXPosition,centerYPosition,outerCircleRadius,1.35f);

        canvas.saveState();
        ColumnText columnText = null;
        for(int index = 0;index < descCenterVertices.length;index ++){
            Phrase sectorDescPhrase = new Phrase(roseChartSectorAttrs.get(index).sectorDesc,new Font(
                    FontUtils.baseFontChinese,roseChartSectorAttrs.get(index).sectorDescFontSize,roseChartSectorAttrs.get(index).sectorDescFontStyle,
                    roseChartSectorAttrs.get(index).sectorDescFontColor));

            float strWidth = FontUtils.baseFontChinese.getWidthPoint(roseChartSectorAttrs.get(index).sectorDesc,roseChartSectorAttrs.get(index).sectorDescFontSize);

            columnText = new ColumnText(canvas);
            columnText.setSimpleColumn(new Rectangle(
                    descCenterVertices[index ].x - strWidth / 2 - 5,
                    descCenterVertices[index].y - DrawTextUtil.getStringHeight((int) roseChartSectorAttrs.get(index).sectorDescFontSize) / 2,
                    descCenterVertices[index].x + strWidth / 2 + 5,
                    descCenterVertices[index].y + DrawTextUtil.getStringHeight((int) roseChartSectorAttrs.get(index).sectorDescFontSize) / 2));

            columnText.setUseAscender(true);
            columnText.addText(sectorDescPhrase);
            columnText.go();

            canvas.setColorStroke(roseChartSectorAttrs.get(index).sectorBgColor);
            canvas.setLineWidth(0.1);
            canvas.stroke();
        }

        canvas.restoreState();

    }

    private Point[] getDescCenterVertices(List<RoseChartSectorAttr> roseChartSectorAttrs,float centerXPosition,float centerYPosition,
                                                                                    float outerCircleRadius, float positionPercentage) {
        float [] sectorArcCenterVerticesAngle = getSectorArcCenterVerticesAngle(roseChartSectorAttrs);

        Point[] descCenterVertices = new Point[roseChartSectorAttrs.size()];

        for(int index = 0;index < descCenterVertices.length;index ++){
            descCenterVertices[index] = new Point(0,0);
        }

        for(int index = 0;index < roseChartSectorAttrs.size();index ++){
            descCenterVertices[index].x = (float) (centerXPosition +
                    outerCircleRadius * positionPercentage * Math.cos(sectorArcCenterVerticesAngle[index]));
            descCenterVertices[index].y = (float) (centerYPosition +
                    outerCircleRadius * positionPercentage * Math.sin(sectorArcCenterVerticesAngle[index]));
        }

        return descCenterVertices;
    }

    public float[] getSectorArcCenterVerticesAngle(List<RoseChartSectorAttr> roseChartSectorAttrs) {
        float [] sectorArcCenterVerticesAngle = new float[roseChartSectorAttrs.size()];

        float sectorAngleOffset = (float) (Math.PI / 2) * 0;

        for(int index = 0;index < roseChartSectorAttrs.size();index ++){
            sectorArcCenterVerticesAngle[index] = (float) (sectorAngleOffset + roseChartSectorAttrs.get(index).sectorAngle * Math.PI * 2 / 360 /2);

            sectorAngleOffset += roseChartSectorAttrs.get(index).sectorAngle * Math.PI * 2 / 360;
        }

        return sectorArcCenterVerticesAngle;
    }


    public static class RoseChartSectorAttr{
        public float sectorAngle;
        public float sectorAttrValue;
        public String sectorDesc;
        public BaseColor sectorBgColor;

        public float sectorDescFontSize;
        public int sectorDescFontStyle;
        public BaseColor sectorDescFontColor;

        public RoseChartSectorAttr(float sectorAngle, float sectorAttrValue, String sectorDesc, BaseColor sectorBgColor) {
            this(sectorAngle, sectorAttrValue, sectorDesc, sectorBgColor,FontUtils.fontSize,FontUtils.fontStyle,FontUtils.fontColor);
        }

        public RoseChartSectorAttr(float sectorAngle, float sectorAttrValue, String sectorDesc, BaseColor sectorBgColor,
                                   float sectorDescFontSize, int sectorDescFontStyle, BaseColor sectorDescFontColor) {
            this.sectorAngle = sectorAngle;
            this.sectorAttrValue = sectorAttrValue;
            this.sectorDesc = sectorDesc;
            this.sectorBgColor = sectorBgColor;
            this.sectorDescFontSize = sectorDescFontSize;
            this.sectorDescFontStyle = sectorDescFontStyle;
            this.sectorDescFontColor = sectorDescFontColor;
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            List<RoseChartSectorAttr> roseChartSectorAttrs = new ArrayList<RoseChartSectorAttr>();

            RoseChartSectorAttr roseChartSectorAttr = null;

            int partCount = 6;
            String [] sectorsDesc = new String[]{"个人信息分析","通话数据分析","银联消费分析","短信金融分析","信贷数据分析","电商数据分析—支付宝","(支付宝)"};
            float [] scoresValue = new float[]{333,444,555,666,777,850,850};

            for(int index = 0;index < partCount;index ++){
                roseChartSectorAttr = new RoseChartSectorAttr(360 * 1.0f / partCount,scoresValue[index],
                        sectorsDesc[index], ColorUtil.strRGBAToColor(ColorUtil.ROSE_CHART_COLOR_ARRAY[index]));
                roseChartSectorAttrs.add(roseChartSectorAttr);
            }

            RoseChartCell roseChartCell = new RoseChartCell(80,roseChartSectorAttrs,300,850,true);
            roseChartCell.setColspan(12);
            mainFrame.addCell(roseChartCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
