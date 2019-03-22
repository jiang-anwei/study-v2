package com.icekredit.pdf.entities.member_level;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 9/13/16.
 */
public class MemberLevelDiamondInfoCell extends MemberLevelInfoCell {
    public static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0xcc,0x97,0xff,0xff);
    public static final float DEFAULT_BACKGROUND_CORNER_RADIUS = 5;
    public static final String DEFAULT_CURRENT_LEVEL_NAME = "钻石会员";
    public static final boolean DEFAULT_ENABLED = true;

    public MemberLevelDiamondInfoCell(){
        this(DEFAULT_BACKGROUND_CORNER_RADIUS,DEFAULT_BACKGROUND_COLOR,DEFAULT_CURRENT_LEVEL_NAME,DEFAULT_ENABLED);
    }

    public MemberLevelDiamondInfoCell(float backgroundCornerRadius, BaseColor backgroundPanelColor, String currentLevelName, boolean enabled) {
        super(backgroundCornerRadius, backgroundPanelColor, currentLevelName, enabled);
    }

    @Override
    protected void drawMemberLevelLogo(Rectangle position, PdfContentByte canvas) {
        try {
            float diamondModelRadius = 50;
            float diamondModelSpanAngle = (float) (80 * Math.PI / 180);

            float centerXPosition = (position.getLeft() + position.getRight()) / 2;
            float centerYPosition = position.getBottom() + position.getHeight() * NAME_HEIGHT_PERCENTAGE
                    + position.getHeight() * LOGO_HEIGHT_PERCENTAGE / 2 - diamondModelRadius / 2;


            Point[] frameVertices = new Point[5];
            Point [] innerTriangleVertices = new Point[3];

            for(int index = 0;index < 5;index ++){
                frameVertices[index] = new Point(0,0);
            }

            for(int index = 0;index < 3;index ++){
                innerTriangleVertices[index] = new Point(0,0);
            }

            frameVertices[0].x = centerXPosition;
            frameVertices[0].y = centerYPosition;

            frameVertices[1].x = (float) (centerXPosition + diamondModelRadius * 0.8f * Math.cos(Math.PI / 2 - diamondModelSpanAngle / 2));
            frameVertices[1].y = (float) (centerYPosition + diamondModelRadius * 0.8f * Math.sin(Math.PI / 2 - diamondModelSpanAngle / 2));

            frameVertices[2].x = (float) (centerXPosition + diamondModelRadius * Math.cos(Math.PI / 2 - diamondModelSpanAngle / 4));
            frameVertices[2].y = (float) (centerYPosition + diamondModelRadius * Math.sin(Math.PI / 2 - diamondModelSpanAngle / 4));

            frameVertices[3].x = (float) (centerXPosition + diamondModelRadius * Math.cos(Math.PI / 2 + diamondModelSpanAngle / 4));
            frameVertices[3].y = (float) (centerYPosition + diamondModelRadius * Math.sin(Math.PI / 2 + diamondModelSpanAngle / 4));

            frameVertices[4].x = (float) (centerXPosition + diamondModelRadius * 0.8f * Math.cos(Math.PI / 2 + diamondModelSpanAngle / 2));
            frameVertices[4].y = (float) (centerYPosition + diamondModelRadius * 0.8f * Math.sin(Math.PI / 2 + diamondModelSpanAngle / 2));

            innerTriangleVertices[0].x = (float) (centerXPosition + diamondModelRadius * 0.6f * Math.cos(Math.PI / 2 - diamondModelSpanAngle * 3 / 8));
            innerTriangleVertices[0].y = (float) (centerYPosition + diamondModelRadius * 0.6f * Math.sin(Math.PI / 2 - diamondModelSpanAngle * 3 / 8));

            innerTriangleVertices[1].x = (float) (centerXPosition + diamondModelRadius * 0.9f * Math.cos(Math.PI / 2));
            innerTriangleVertices[1].y = (float) (centerYPosition + diamondModelRadius * 0.9f * Math.sin(Math.PI / 2));

            innerTriangleVertices[2].x = (float) (centerXPosition + diamondModelRadius * 0.6f * Math.cos(Math.PI / 2 + diamondModelSpanAngle * 3 / 8));
            innerTriangleVertices[2].y = (float) (centerYPosition + diamondModelRadius * 0.6f * Math.sin(Math.PI / 2 + diamondModelSpanAngle * 3 / 8));

            canvas.saveState();
            canvas.setLineWidth(0);
            canvas.setColorFill(ColorUtil.strRGBAToColor("0xffffff77"));
            canvas.setColorStroke(ColorUtil.strRGBAToColor("0xffffff77"));

            canvas.moveTo(frameVertices[0].x,frameVertices[0].y);
            for(int index = 0;index < frameVertices.length;index++){
                canvas.lineTo(frameVertices[frameVertices.length - 1 - index].x,frameVertices[frameVertices.length - 1 - index].y);
            }
            canvas.fillStroke();


            canvas.setColorFill(ColorUtil.strRGBAToColor("0xffffff77"));
            canvas.setColorStroke(ColorUtil.strRGBAToColor("0xffffff77"));
            canvas.moveTo(frameVertices[0].x,frameVertices[0].y);
            canvas.lineTo(innerTriangleVertices[0].x,innerTriangleVertices[0].y);
            canvas.lineTo(frameVertices[2].x,frameVertices[2].y);
            canvas.lineTo(frameVertices[3].x,frameVertices[3].y);
            canvas.lineTo(innerTriangleVertices[2].x,innerTriangleVertices[2].y);
            canvas.lineTo(frameVertices[0].x,frameVertices[0].y);
            canvas.closePathFillStroke();

            canvas.setColorFill(ColorUtil.strRGBAToColor("0xffffff99"));
            canvas.setColorStroke(ColorUtil.strRGBAToColor("0xffffff99"));
            canvas.moveTo(innerTriangleVertices[0].x,innerTriangleVertices[0].y);
            canvas.lineTo(innerTriangleVertices[1].x,innerTriangleVertices[1].y);
            canvas.lineTo(innerTriangleVertices[2].x,innerTriangleVertices[2].y);
            canvas.closePathFillStroke();

            canvas.moveTo(innerTriangleVertices[1].x,innerTriangleVertices[1].y);
            canvas.lineTo(frameVertices[2].x,frameVertices[2].y);
            canvas.lineTo(frameVertices[3].x,frameVertices[3].y);
            canvas.closePathFillStroke();

            canvas.restoreState();
        }catch (Exception e){
            e.printStackTrace();
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

            MemberLevelInfoCell memberLevelInfoCell = new MemberLevelDiamondInfoCell();
            memberLevelInfoCell.setColspan(12);
            mainFrame.addCell(memberLevelInfoCell);

            document.add(mainFrame);

            document.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
