package com.icekredit.pdf.entities.member_level;

import com.icekredit.pdf.entities.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.PdfConvertUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icekredit on 9/13/16.
 */
public class MemberLevelInfoCell extends BaseCell implements PdfPCellEvent{
    protected float backgroundCornerRadius;
    protected BaseColor backgroundPanelColor;

    protected String currentLevelName;
    protected boolean enabled;

    public static final float BACKGROUND_PANEL_WIDTH_RATIO = 0.8f;
    public static final float BACKGROUND_PANEL_HEIGHT_RATIO = 0.8f;

    protected static final float LOGO_HEIGHT_PERCENTAGE = 0.8f;
    protected static final float NAME_HEIGHT_PERCENTAGE = 0.2f;

    public MemberLevelInfoCell(float backgroundCornerRadius, BaseColor backgroundPanelColor, String currentLevelName,boolean enabled) {
        this.backgroundCornerRadius = backgroundCornerRadius;
        this.backgroundPanelColor = backgroundPanelColor;
        this.currentLevelName = currentLevelName;
        this.enabled = enabled;

        this.setFixedHeight(85);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            Rectangle rectangle = new Rectangle(position);
            rectangle.setLeft(rectangle.getLeft() + position.getWidth() * (1 - BACKGROUND_PANEL_WIDTH_RATIO) / 2);
            rectangle.setRight(rectangle.getLeft() + position.getWidth() * BACKGROUND_PANEL_WIDTH_RATIO);
            rectangle.setBottom(rectangle.getBottom() + position.getHeight() * (1 - BACKGROUND_PANEL_HEIGHT_RATIO) / 2);
            rectangle.setTop(rectangle.getBottom() + position.getHeight() * BACKGROUND_PANEL_HEIGHT_RATIO);

            PdfContentByte canvas = canvases[PdfPTable.BASECANVAS];

            PdfConvertUtil.drawRoundCornerBackground(rectangle, canvas,backgroundPanelColor,backgroundCornerRadius);
            drawMemberLevelName(rectangle,canvas);
            drawMemberLevelLogo(rectangle,canvas);
            drawCover(rectangle,canvas);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawCover(Rectangle position, PdfContentByte canvas) {
        try {
            if(!enabled){
                PdfConvertUtil.drawRoundCornerBackground(position,canvas,ColorUtil.strRGBAToColor("0x00000044"),backgroundCornerRadius);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void drawMemberLevelLogo(Rectangle position, PdfContentByte canvas) {
    }

    private void drawMemberLevelName(Rectangle position, PdfContentByte canvas) {
        try {
            Font currentFont = new Font(FontUtils.baseFontChinese,10,Font.NORMAL,new BaseColor(0xff,0xff,0xff,0xff));

            float strWidth = FontUtils.baseFontChinese.getWidthPoint(currentLevelName,currentFont.getSize());
            float strHeight = DrawTextUtil.getStringHeight((int) currentFont.getSize());

            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(new Rectangle(
                    (position.getLeft() + position.getRight()) / 2 - strWidth / 2,
                    position.getBottom() + position.getHeight() * NAME_HEIGHT_PERCENTAGE / 2 - strHeight / 2,
                    (position.getLeft() + position.getRight()) / 2 + strWidth / 2,
                    position.getBottom() + position.getHeight() * NAME_HEIGHT_PERCENTAGE / 2 + strHeight / 2 + 10
            ));

            columnText.addElement(new Phrase(new Chunk(currentLevelName,currentFont)));

            columnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class Logo extends com.icekredit.pdf.entities.relative_chart.View{
        protected PdfContentByte canvas;

        protected float centerXPosition;
        protected float centerYPosition;

        protected float outerRadius;
        protected static final float INNER_CIRCLE_RADIUS_RATIO = 0.9f;

        protected float wingsRadius;
        protected float wingsSpanFromAngle;
        protected float wingsSpanToAngle;


        protected BaseColor circleBackgroundColor;
        protected BaseColor circleStrokeColor;
        protected BaseColor wingsBackgroundColor;

        public Logo(PdfContentByte canvas, float centerXPosition,
                    float centerYPosition, float outerRadius,
                    float wingsRadius, float wingsSpanFromAngle,float wingsSpanToAngle,
                    BaseColor circleBackgroundColor, BaseColor
                            circleStrokeColor, BaseColor wingsBackgroundColor) {
            this.canvas = canvas;
            this.centerXPosition = centerXPosition;
            this.centerYPosition = centerYPosition;
            this.outerRadius = outerRadius;
            this.wingsRadius = wingsRadius;
            this.wingsSpanFromAngle = wingsSpanFromAngle;
            this.wingsSpanToAngle = wingsSpanToAngle;
            this.circleBackgroundColor = circleBackgroundColor;
            this.circleStrokeColor = circleStrokeColor;
            this.wingsBackgroundColor = wingsBackgroundColor;
        }

        @Override
        public void draw() {
            canvas.saveState();
            canvas.setLineWidth(0);

            drawWings(canvas,centerXPosition,centerYPosition,wingsRadius,wingsSpanFromAngle,wingsSpanToAngle,wingsBackgroundColor);
            drawRoundCover(canvas,centerXPosition,centerYPosition,outerRadius);
            drawCircleCover(canvas,centerXPosition,centerYPosition,outerRadius * INNER_CIRCLE_RADIUS_RATIO);

            canvas.restoreState();
        }

        private void drawCircleCover(PdfContentByte canvas, float centerXPosition, float centerYPosition, float radius) {
            canvas.setLineWidth(0.5);
            canvas.setColorStroke(circleStrokeColor);
            canvas.circle(centerXPosition,centerYPosition,radius);
            canvas.stroke();
        }

        private void drawRoundCover(PdfContentByte canvas, float centerXPosition, float centerYPosition, float outerRadius) {
            canvas.setColorStroke(circleBackgroundColor);
            canvas.setColorFill(circleBackgroundColor);
            canvas.circle(centerXPosition,centerYPosition,outerRadius);
            canvas.fillStroke();
        }

        private void drawWings(PdfContentByte canvas,
                               float centerXPosition, float centerYPosition,
                               float wingsRadius, float wingsSpanFromAngle, float wingsSpanToAngle,
                               BaseColor wingsBackgroundColor) {
            canvas.setColorFill(wingsBackgroundColor);
            canvas.setColorStroke(wingsBackgroundColor);

            List<com.icekredit.pdf.entities.Point> points = getWingsVertices(centerXPosition,centerYPosition,wingsRadius,wingsSpanFromAngle,wingsSpanToAngle);

            canvas.moveTo(points.get(0).x,points.get(0).y);
            for(int index = 0;index < points.size();index ++){
                canvas.lineTo(points.get(points.size() - 1 - index).x,points.get(points.size() - 1 - index).y);
            }
            canvas.fillStroke();
        }

        private List<com.icekredit.pdf.entities.Point> getWingsVertices(float centerXPosition, float centerYPosition, float wingsRadius, float wingsSpanFromAngle, float wingsSpanToAngle) {
            List<com.icekredit.pdf.entities.Point> points = new ArrayList<com.icekredit.pdf.entities.Point>();

            points.add(new com.icekredit.pdf.entities.Point(centerXPosition,centerYPosition));

            points.add(new com.icekredit.pdf.entities.Point(
                    (float) (centerXPosition + wingsRadius * 0.9 * Math.cos(Math.PI / 2 - wingsSpanToAngle / 2)),
                    (float)(centerYPosition + wingsRadius * 0.9 * Math.sin(Math.PI / 2 - wingsSpanToAngle / 2))
            ));

            points.add(new com.icekredit.pdf.entities.Point(
                    (float) (centerXPosition + wingsRadius * Math.cos(Math.PI / 2 - wingsSpanFromAngle / 2)),
                    (float)(centerYPosition + wingsRadius * Math.sin(Math.PI / 2 - wingsSpanFromAngle / 2))
            ));

            points.add(new com.icekredit.pdf.entities.Point(centerXPosition,centerYPosition));

            points.add(new com.icekredit.pdf.entities.Point(
                    (float) (centerXPosition + wingsRadius * Math.cos(Math.PI / 2 + wingsSpanFromAngle / 2)),
                    (float)(centerYPosition + wingsRadius * Math.sin(Math.PI / 2 + wingsSpanFromAngle / 2))
            ));

            points.add(new com.icekredit.pdf.entities.Point(
                    (float) (centerXPosition + wingsRadius * 0.9 * Math.cos(Math.PI / 2 + wingsSpanToAngle / 2)),
                    (float)(centerYPosition + wingsRadius * 0.9 * Math.sin(Math.PI / 2 + wingsSpanToAngle / 2))
            ));

            points.get(1).x = points.get(2).x;
            points.get(5).x = points.get(4).x;

            return points;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

            MemberLevelInfoCell memberLevelInfoCell = new MemberLevelInfoCell(
                    5, ColorUtil.strRGBAToColor("0xff8000ff"),"黄金会员",false
            );
            memberLevelInfoCell.setColspan(12);
            mainFrame.addCell(memberLevelInfoCell);

            document.add(mainFrame);

            document.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
