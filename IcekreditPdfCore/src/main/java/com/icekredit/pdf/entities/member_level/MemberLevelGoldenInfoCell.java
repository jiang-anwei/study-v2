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
public class MemberLevelGoldenInfoCell extends MemberLevelInfoCell {
    public static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0xfb,0xc8,0x3c,0xff);
    public static final float DEFAULT_BACKGROUND_CORNER_RADIUS = 5;
    public static final String DEFAULT_CURRENT_LEVEL_NAME = "金牌会员";
    public static final boolean DEFAULT_ENABLED = true;

    public MemberLevelGoldenInfoCell(){
        this(DEFAULT_BACKGROUND_CORNER_RADIUS,DEFAULT_BACKGROUND_COLOR,DEFAULT_CURRENT_LEVEL_NAME,DEFAULT_ENABLED);
    }

    public MemberLevelGoldenInfoCell(float backgroundCornerRadius, BaseColor backgroundPanelColor, String currentLevelName, boolean enabled) {
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
                new BaseColor(0xfe,0xf6,0x5b,0xff),
                new BaseColor(0xfb,0xc8,0x3c,0xff),
                new BaseColor(0xa0,0x6d,0xcb,0xff));
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

            MemberLevelInfoCell memberLevelInfoCell = new MemberLevelGoldenInfoCell();
            memberLevelInfoCell.setColspan(12);
            mainFrame.addCell(memberLevelInfoCell);

            document.add(mainFrame);

            document.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
