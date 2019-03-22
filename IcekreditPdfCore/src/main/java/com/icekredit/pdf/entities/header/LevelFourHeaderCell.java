package com.icekredit.pdf.entities.header;

import com.icekredit.pdf.entities.core.MessageConfig;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 9/9/16.
 */
public class LevelFourHeaderCell extends HeaderCell {
    public static final float DEFAULT_VISIBLE_CELL_HEIGHT = 26;
    public static final float DEFAULT_CELL_NOTE_HEIGHT = 16;

    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xf7,0xff,0xff,0xff);
    private static final BaseColor DEFAULT_VISIBLE_BORDER_COLOR_LEFT = new BaseColor(0xdf,0xf3,0xff,0xff);

    private static BaseColor DEFAULT_IDENTIFIER_FOREGROUND_COLOR = new BaseColor(0x59,0xca,0x70,0xff);
    private static BaseColor DEFAULT_IDENTIFIER_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    private static BaseColor LEVEL_THREE_HEADER_FONT_COLOR = new BaseColor(0x9b,0x9c,0x9d,0xff);

    public LevelFourHeaderCell(String headerName) {
        this(headerName, "");
    }

    public LevelFourHeaderCell(String headerName,String headerDesc){
        this(headerName,headerDesc, MessageConfig.IDENTIFIER_TYPE_NONE,"",MessageConfig.IDENTIFIER_TYPE_NONE,"");
    }

    public LevelFourHeaderCell(String headerName, String headerDesc,
                                int headerDescIdentifierType, String headerValue,
                                int headerValueIdentifierType,String headerNote) {
        this(headerName, headerDesc, headerDescIdentifierType, headerValue, headerValueIdentifierType,
                DEFAULT_IDENTIFIER_FOREGROUND_COLOR,DEFAULT_IDENTIFIER_BACKGROUND_COLOR,
                DEFAULT_IDENTIFIER_FOREGROUND_COLOR,DEFAULT_IDENTIFIER_BACKGROUND_COLOR,headerNote);
    }

    public LevelFourHeaderCell(String headerName, String headerDesc,
                               int headerDescIdentifierType, String headerValue,
                               int headerValueIdentifierType,
                               BaseColor headerDescIdentifierForegroundColor, BaseColor headerDescIdentifierBackgroundColor,
                               BaseColor headerValueIdentifierForegroundColor, BaseColor headerValueIdentifierBackgroundColor,
                               String headerNote) {
        this(headerName, headerDesc, headerDescIdentifierType, headerValue, headerValueIdentifierType,
                headerDescIdentifierForegroundColor,headerDescIdentifierBackgroundColor,
                headerValueIdentifierForegroundColor,headerValueIdentifierBackgroundColor,
                headerNote,DEFAULT_VISIBLE_CELL_HEIGHT,DEFAULT_CELL_NOTE_HEIGHT);
    }

    public LevelFourHeaderCell(String headerName, String headerDesc,
                                int headerDescIdentifierType, String headerValue,
                                int headerValueIdentifierType,
                                BaseColor headerDescIdentifierForegroundColor, BaseColor headerDescIdentifierBackgroundColor,
                                BaseColor headerValueIdentifierForegroundColor, BaseColor headerValueIdentifierBackgroundColor,
                                String headerNote,float cellHeight,float cellNoteHeight) {
        super(headerName, LEVEL_THREE_HEADER_FONT_COLOR,headerDesc, LEVEL_THREE_HEADER_FONT_COLOR,headerDescIdentifierType,
                headerValue, LEVEL_THREE_HEADER_FONT_COLOR,headerValueIdentifierType, CELL_BACKGROUND_COLOR,
                DEFAULT_VISIBLE_BORDER_COLOR_LEFT, headerDescIdentifierForegroundColor,
                headerDescIdentifierBackgroundColor,
                headerValueIdentifierForegroundColor, headerValueIdentifierBackgroundColor,
                headerNote,cellHeight,cellNoteHeight);
    }

    public static void main(String [] args){
        String destFileStr = "result/test.pdf";
        File destFile = new File(destFileStr);
        destFile.getParentFile().mkdirs();

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            LevelFourHeaderCell headerCell = new LevelFourHeaderCell("與情分数","分数");
            headerCell.setColspan(12);
            mainFrame.addCell(headerCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
