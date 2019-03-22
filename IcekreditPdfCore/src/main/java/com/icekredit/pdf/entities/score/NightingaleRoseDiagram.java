package com.icekredit.pdf.entities.score;

import com.icekredit.pdf.entities.Point;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 南丁格尔玫瑰图
 *
 * @author wenchao
 */
public class NightingaleRoseDiagram extends PdfPCell implements PdfPCellEvent {
    private BaseColor bgColor;

    private static final float DEFAULT_HEIGHT_EXPAND_RATIO = 1.2f;

    private float innerRadius;
    private static final float MIN_INNER_RADIUS = 0;
    private static final float MAX_INNER_RADIUS_RATIO = 0.8f;

    private float outerRadius;
    private static final float MIN_OUTER_RADIUS = 50;
    private static final float MAX_OUTER_RADIUS = 100;

    private List<RoseDiagramItem> roseDiagramItems;

    private static final int DEFAULT_PADDING = 2;

    /**
     * 用多少步数来画一个整个的外圆圆弧
     */
    private static final int DEFAULT_CALC_STEP_COUNT = 180;

    public NightingaleRoseDiagram(BaseColor bgColor, float outerRadius, float innerRadius, List<RoseDiagramItem> roseDiagramItems) {
        this.bgColor = bgColor;
        this.outerRadius = normalize(outerRadius, MIN_OUTER_RADIUS, MAX_OUTER_RADIUS);
        this.innerRadius = normalize(innerRadius, MIN_INNER_RADIUS, MAX_INNER_RADIUS_RATIO * this.outerRadius);
        this.roseDiagramItems = roseDiagramItems;

        this.setFixedHeight(this.outerRadius * 2 * DEFAULT_HEIGHT_EXPAND_RATIO);

        for (RoseDiagramItem roseDiagramItem : roseDiagramItems) {
            roseDiagramItem.normalize(this.innerRadius, this.outerRadius);
        }

        disableTheDefaultFeature();

        this.setCellEvent(this);    //将cellEvent绑定到当前单元格
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        //按照单元格宽度进行伸缩
        float oldOuterRadius = outerRadius;
        outerRadius = Math.min(outerRadius,position.getWidth() / 2 - 20);
        innerRadius = Math.min(innerRadius,MAX_INNER_RADIUS_RATIO * this.outerRadius);
        for (RoseDiagramItem roseDiagramItem : roseDiagramItems) {
            roseDiagramItem.scale(outerRadius / oldOuterRadius);
        }

        //勾画背景色
        drawBackGround(position, canvases[PdfPTable.BACKGROUNDCANVAS], bgColor);

        //勾画外围圆
        drawOuterCircle(position, canvases[PdfPTable.BACKGROUNDCANVAS], outerRadius, roseDiagramItems.size());

        //勾画扇区
        drawSectors(position, canvases[PdfPTable.BACKGROUNDCANVAS], roseDiagramItems);

        //勾画扇区描述
        drawSectorsDesc(position, canvases[PdfPTable.TEXTCANVAS], roseDiagramItems);

        //勾画内部圆
        drawInnerCircle(position, canvases[PdfPTable.BACKGROUNDCANVAS], innerRadius);
    }

    private void drawBackGround(Rectangle position, PdfContentByte canvas, BaseColor bgColor) {
        Rectangle backgroundRectangle = new Rectangle(position);

        //预留空白padding
        backgroundRectangle.setLeft(backgroundRectangle.getLeft() + DEFAULT_PADDING);
        backgroundRectangle.setRight(backgroundRectangle.getRight() - DEFAULT_PADDING);
        backgroundRectangle.setBottom(backgroundRectangle.getBottom() + DEFAULT_PADDING);
        backgroundRectangle.setTop(backgroundRectangle.getTop() - DEFAULT_PADDING);

        //勾画背景
        canvas.saveState();
        canvas.setLineWidth(0);
        canvas.setColorStroke(bgColor);
        canvas.setColorFill(bgColor);
        canvas.rectangle(backgroundRectangle.getLeft(), backgroundRectangle.getBottom(),
                backgroundRectangle.getWidth(), backgroundRectangle.getHeight());
        canvas.closePathFillStroke();
        canvas.restoreState();
    }

    private void drawOuterCircle(Rectangle position, PdfContentByte canvas, float outerRadius, int sectorCount) {
        float centerX = (position.getLeft() + position.getRight()) / 2;
        float centerY = (position.getBottom() + position.getTop()) / 2;

        int defaultScale = 5;
        canvas.saveState();
        canvas.setColorStroke(BaseColor.WHITE);
        canvas.setLineWidth(0.1);
        for (int index = 0; index < defaultScale; index++) {
            canvas.circle(centerX, centerY, outerRadius * (index + 1) / defaultScale);
            canvas.stroke();
        }

        float sectorAngleOffset = (float) (Math.PI * 2);
        double sectorIncrement = Math.PI * 2 / sectorCount;
        for (int index = 0; index < sectorCount; index++) {
            canvas.moveTo(centerX, centerY);
            canvas.lineTo(
                    centerX + outerRadius * Math.cos(sectorAngleOffset),
                    centerY + outerRadius * Math.sin(sectorAngleOffset)
            );

            sectorAngleOffset += sectorIncrement;
            canvas.stroke();
        }

        canvas.restoreState();
    }

    private void drawSectors(Rectangle position, PdfContentByte canvas, List<RoseDiagramItem> roseDiagramItems) {
        if (roseDiagramItems.isEmpty()) {
            return;
        }

        int sectorCount = roseDiagramItems.size();
        float centerX = (position.getLeft() + position.getRight()) / 2;
        float centerY = (position.getBottom() + position.getTop()) / 2;

        float currentSectorAngleOffset = 90;

        float currentSectorAngle;
        BaseColor currentSectorBGColor;
        Point[] sectorVertices;

        canvas.saveState();
        canvas.setLineWidth(0.1);

        //画出表示比例的扇区
        for (RoseDiagramItem roseDiagramItem : roseDiagramItems) {
            currentSectorAngle = 360f / sectorCount;
            currentSectorBGColor = roseDiagramItem.bgColor;

            sectorVertices = getSectorVertices(currentSectorAngleOffset, currentSectorAngle,
                    centerX, centerY, roseDiagramItem.radius);

            canvas.setColorFill(currentSectorBGColor);
            canvas.setColorStroke(currentSectorBGColor);
            canvas.moveTo(sectorVertices[0].x, sectorVertices[0].y);

            for (int index = 1; index < sectorVertices.length; index++) {
                canvas.lineTo(sectorVertices[index].x, sectorVertices[index].y);
            }

            canvas.closePathFillStroke();

            currentSectorAngleOffset += currentSectorAngle;
        }

        canvas.restoreState();
    }

    /**
     * 计算每一个扇区对应的顶点信息
     *
     * @param currentSectorAngleOffset 扇区偏移角度
     * @param currentSectorAngle       当前扇区角度
     * @param centerXPosition          圆心x坐标
     * @param centerYPosition          圆心y坐标
     * @param radius                   扇区半径
     * @return 扇区对应顶点信息
     */
    private Point[] getSectorVertices(
            float currentSectorAngleOffset, float currentSectorAngle, float centerXPosition, float centerYPosition, float radius) {
        //计算出画当前扇区需要的顶点的数目
        int sectorVerticesCount = Math.round(currentSectorAngle / 360 * DEFAULT_CALC_STEP_COUNT + 0.5f) + 1;

        Point[] sectorVertices = new Point[sectorVerticesCount];

        //初始化
        for (int index = 0; index < sectorVerticesCount; index++) {
            sectorVertices[index] = new Point(0, 0);
        }

        sectorVertices[0].x = centerXPosition;
        sectorVertices[0].y = centerYPosition;

        for (int index = 1; index < sectorVerticesCount; index++) {
            sectorVertices[index].x = (float) (centerXPosition + radius
                    * Math.cos(currentSectorAngleOffset * Math.PI * 2 / 360
                    + (index - 1) * 1.0f / (sectorVerticesCount - 2) * currentSectorAngle * Math.PI * 2 / 360));
            sectorVertices[index].y = (float) (centerYPosition + radius
                    * Math.sin(currentSectorAngleOffset * Math.PI * 2 / 360
                    + (index - 1) * 1.0f / (sectorVerticesCount - 2) * currentSectorAngle * Math.PI * 2 / 360));
        }

        return sectorVertices;
    }

    private void drawSectorsDesc(Rectangle position, PdfContentByte canvas, List<RoseDiagramItem> roseDiagramItems) {
        float centerX = (position.getLeft() + position.getRight()) / 2;
        float centerY = (position.getBottom() + position.getTop()) / 2;

        double expand = 10;

        double sectorIncrement = Math.PI * 2 / roseDiagramItems.size();
        double sectorAngleOffset = sectorIncrement / 2 + (float) (Math.PI / 2);     //扇区描述文字初始偏移量为扇区角度的一半
        double sectorBoundCenterX;
        double sectorBoundCenterY;
        double sectorAngleCos;
        double sectorAngleSin;
        float descStrWidth;
        float descStrHeight;
        for (RoseDiagramItem roseDiagramItem : roseDiagramItems) {
            sectorAngleCos = Math.cos(sectorAngleOffset);
            sectorAngleSin = Math.sin(sectorAngleOffset);

            sectorBoundCenterX = centerX + (outerRadius + expand) * sectorAngleCos;
            sectorBoundCenterY = centerY + (outerRadius + expand) * sectorAngleSin;

            descStrWidth = roseDiagramItem.descriptionFont.getBaseFont().getWidthPoint(
                    roseDiagramItem.description,
                    roseDiagramItem.descriptionFont.getSize()
            );
            descStrHeight = roseDiagramItem.descriptionFont.getBaseFont().getWidthPoint(
                    "AA",
                    roseDiagramItem.descriptionFont.getSize()
            );

            //如果是
            if (sectorAngleCos > 0) {
                ColumnText.showTextAligned(
                        canvas,
                        Element.ALIGN_CENTER,
                        new Phrase(new Chunk(
                                roseDiagramItem.description,
                                roseDiagramItem.descriptionFont
                        )),
                        (float) (sectorBoundCenterX + descStrWidth / 2),
                        (float) (sectorBoundCenterY - descStrHeight / 2),
                        0
                );
            } else if (sectorAngleCos < 0) {
                ColumnText.showTextAligned(
                        canvas,
                        Element.ALIGN_CENTER,
                        new Phrase(new Chunk(
                                roseDiagramItem.description,
                                roseDiagramItem.descriptionFont
                        )),
                        (float) (sectorBoundCenterX - descStrWidth / 2),
                        (float) (sectorBoundCenterY - descStrHeight / 2),
                        0
                );
            } else {
                // 不会出现等于0的情况
            }

            sectorAngleOffset += sectorIncrement;
        }
    }

    private void drawInnerCircle(Rectangle position, PdfContentByte canvas, float innerRadius) {
        float centerX = (position.getLeft() + position.getRight()) / 2;
        float centerY = (position.getBottom() + position.getTop()) / 2;

        canvas.saveState();
        canvas.setLineWidth(0);
        canvas.setColorStroke(bgColor);
        canvas.circle(centerX,centerY,innerRadius);
        canvas.fillStroke();
        canvas.restoreState();
    }

    /**
     * 禁用控件自定义边框以及背景色
     */
    private void disableTheDefaultFeature() {
        this.setBorder(PdfPCell.NO_BORDER);
        this.setBackgroundColor(BaseColor.WHITE);
    }

    public static class RoseDiagramItem {
        private String description;
        private Font descriptionFont;
        private float radius;
        private BaseColor bgColor;

        public RoseDiagramItem(String description, Font descriptionFont, float radius, BaseColor bgColor) {
            this.description = description;
            this.descriptionFont = descriptionFont;
            this.radius = radius;
            this.bgColor = bgColor;
        }

        public void normalize(float innerRadius, float outerRadius) {
            this.radius = NightingaleRoseDiagram.normalize(radius, innerRadius, outerRadius);
        }

        public void scale(float scale) {
            this.radius *= scale;
        }
    }

    /**
     * 按照最大最小值对指定半径做处理
     *
     * @param radius    当前半径
     * @param minRadius 最小半径
     * @param maxRadius 最大半径
     * @return 如果当前半径小于最小半径，返回最小半径，如果当前半径大于最大半径，返回最大半径，否则返回当前半径
     */
    private static float normalize(float radius, float minRadius, float maxRadius) {
        if (radius < minRadius) {
            return minRadius;
        }

        if (radius > maxRadius) {
            return maxRadius;
        }

        return radius;
    }

    public static void main(String[] args) throws Exception {
        String destFileStr = "result/NightingaleRoseDiagram.pdf";
        File destFile = new File(destFileStr);
        destFile.getParentFile().mkdirs();

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(destFile));

        document.open();

        int partCount = 8;
        String[] sectorsDesc = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        float[] scoresValue = new float[]{20, 100, 90, 40, 90, 30, 90, 70};
        BaseColor[] sectorsColors = new BaseColor[]{
                new BaseColor(0xff016895),
                new BaseColor(0xffe30c81),
                new BaseColor(0xffd1dc2a),
                new BaseColor(0xffff841a),
                new BaseColor(0xff07a858),
                new BaseColor(0xff01adf9),
                new BaseColor(0xfff50100),
                new BaseColor(0xff960200)
        };

        Font font = new Font(BaseFont.createFont(), 8, Font.NORMAL, new BaseColor(0xff333333));

        RoseDiagramItem roseDiagramItem;
        List<RoseDiagramItem> roseDiagramItems = new ArrayList<>();
        for (int index = 0; index < partCount; index++) {
            roseDiagramItem = new RoseDiagramItem(
                    sectorsDesc[index],
                    font,
                    scoresValue[index],
                    sectorsColors[index]
            );

            roseDiagramItems.add(roseDiagramItem);
        }

        BaseColor diagramBgColor = new BaseColor(0xffe5e5e5);
        NightingaleRoseDiagram nightingaleRoseDiagram = new NightingaleRoseDiagram(
                diagramBgColor,
                100,
                5,
                roseDiagramItems
        );

        PdfPTable mainFrame = new PdfPTable(12);
        mainFrame.setWidthPercentage(100);

        //单个单元格
        nightingaleRoseDiagram.setColspan(12);
        mainFrame.addCell(nightingaleRoseDiagram);

        //多单元格
        /*nightingaleRoseDiagram.setColspan(3);
        mainFrame.addCell(nightingaleRoseDiagram);
        mainFrame.addCell(nightingaleRoseDiagram);
        mainFrame.addCell(nightingaleRoseDiagram);
        mainFrame.addCell(nightingaleRoseDiagram);*/

        document.add(mainFrame);

        document.close();
    }
}
