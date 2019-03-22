package com.icekredit.pdf.entities.member_level;

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
public class MemberLevelSilverInfoCell extends MemberLevelInfoCell {
    public static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0xcd,0xcf,0xd3,0xff);
    public static final float DEFAULT_BACKGROUND_CORNER_RADIUS = 5;
    public static final String DEFAULT_CURRENT_LEVEL_NAME = "银牌会员";
    public static final boolean DEFAULT_ENABLED = true;

    public MemberLevelSilverInfoCell(){
        this(DEFAULT_BACKGROUND_CORNER_RADIUS,DEFAULT_BACKGROUND_COLOR,DEFAULT_CURRENT_LEVEL_NAME,DEFAULT_ENABLED);
    }

    public MemberLevelSilverInfoCell(float backgroundCornerRadius, BaseColor backgroundPanelColor, String currentLevelName, boolean enabled) {
        super(backgroundCornerRadius, backgroundPanelColor, currentLevelName, enabled);
    }

    @Override
    protected void drawMemberLevelLogo(Rectangle position, PdfContentByte canvas) {
        Logo logo = new Logo(canvas,
                (position.getLeft() + position.getRight()) / 2,
                position.getBottom() + position.getHeight() * NAME_HEIGHT_PERCENTAGE + position.getHeight() * LOGO_HEIGHT_PERCENTAGE * 2 / 5,
                20,
                45,
                (float)(100 * Math.PI / 180),
                (float)(140 * Math.PI / 180),
                new BaseColor(0xff,0xff,0xff,0xff),
                new BaseColor(0xcd,0xcf,0xd3,0xff),
                new BaseColor(0x50,0x9e,0xd8,0xff));
        logo.draw();
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

            MemberLevelInfoCell memberLevelInfoCell = new MemberLevelSilverInfoCell();
            memberLevelInfoCell.setColspan(12);
            mainFrame.addCell(memberLevelInfoCell);

            document.add(mainFrame);

            document.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
