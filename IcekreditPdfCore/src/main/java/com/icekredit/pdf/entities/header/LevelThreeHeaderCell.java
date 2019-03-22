package com.icekredit.pdf.entities.header;

import com.icekredit.pdf.entities.core.MessageConfig;
import com.icekredit.pdf.outline.OutlinePart;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 7/24/16.
 *
 * 三级标题单元格
 */
public class LevelThreeHeaderCell extends HeaderCell {
    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xef,0xef,0xef,0xff);
    private static final BaseColor DEFAULT_VISIBLE_BORDER_COLOR_LEFT = new BaseColor(0x9f,0xa0,0xa0,0xff);

    private static BaseColor DEFAULT_IDENTIFIER_FOREGROUND_COLOR = new BaseColor(0x59,0xca,0x70,0xff);
    private static BaseColor DEFAULT_IDENTIFIER_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    private static BaseColor LEVEL_THREE_HEADER_FONT_COLOR = new BaseColor(0x59,0x57,0x57,0xff);

    public LevelThreeHeaderCell(String headerName, String headerDesc) {
        this(headerName, headerDesc, 0, "", 0, "");
    }

    public LevelThreeHeaderCell(String headerName) {
        super(headerName, LEVEL_THREE_HEADER_FONT_COLOR,"",LEVEL_THREE_HEADER_FONT_COLOR, MessageConfig.IDENTIFIER_TYPE_NONE,
                "", LEVEL_THREE_HEADER_FONT_COLOR, MessageConfig.IDENTIFIER_TYPE_NONE, CELL_BACKGROUND_COLOR,
                DEFAULT_VISIBLE_BORDER_COLOR_LEFT, DEFAULT_IDENTIFIER_FOREGROUND_COLOR,
                DEFAULT_IDENTIFIER_BACKGROUND_COLOR,
                DEFAULT_IDENTIFIER_FOREGROUND_COLOR, DEFAULT_IDENTIFIER_BACKGROUND_COLOR,"");

        OutlinePart.getOutlinePart(String.valueOf(Thread.currentThread().getId())).appendLevelThreeOutlines(headerName,headerName);
    }

    public LevelThreeHeaderCell(String headerName, String headerDesc,
                              int headerDescIdentifierType, String headerValue,
                              int headerValueIdentifierType,String headerNote) {
        super(headerName, LEVEL_THREE_HEADER_FONT_COLOR,headerDesc,LEVEL_THREE_HEADER_FONT_COLOR, headerDescIdentifierType,
                headerValue, LEVEL_THREE_HEADER_FONT_COLOR,headerValueIdentifierType, CELL_BACKGROUND_COLOR,
                DEFAULT_VISIBLE_BORDER_COLOR_LEFT, DEFAULT_IDENTIFIER_FOREGROUND_COLOR,
                DEFAULT_IDENTIFIER_BACKGROUND_COLOR,
                DEFAULT_IDENTIFIER_FOREGROUND_COLOR, DEFAULT_IDENTIFIER_BACKGROUND_COLOR,headerNote);

        OutlinePart.getOutlinePart(String.valueOf(Thread.currentThread().getId())).appendLevelThreeOutlines(headerName,headerName);
    }


    public LevelThreeHeaderCell(String headerName, String headerDesc,
                              int headerDescIdentifierType, String headerValue,
                              int headerValueIdentifierType,
                              BaseColor headerDescIdentifierForegroundColor, BaseColor headerDescIdentifierBackgroundColor,
                              BaseColor headerValueIdentifierForegroundColor, BaseColor headerValueIdentifierBackgroundColor,
                                String headerNote) {
        super(headerName, LEVEL_THREE_HEADER_FONT_COLOR,headerDesc, LEVEL_THREE_HEADER_FONT_COLOR,headerDescIdentifierType,
                headerValue, LEVEL_THREE_HEADER_FONT_COLOR,headerValueIdentifierType, CELL_BACKGROUND_COLOR,
                DEFAULT_VISIBLE_BORDER_COLOR_LEFT, headerDescIdentifierForegroundColor,
                headerDescIdentifierBackgroundColor,
                headerValueIdentifierForegroundColor, headerValueIdentifierBackgroundColor,headerNote);

        OutlinePart.getOutlinePart(String.valueOf(Thread.currentThread().getId())).appendLevelThreeOutlines(headerName,headerName);
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

            LevelThreeHeaderCell headerCell = new LevelThreeHeaderCell("與情分数","分数", MessageConfig.IDENTIFIER_TYPE_MARK_NEGETIVE,"666", MessageConfig.IDENTIFIER_TYPE_NONE,"");
            headerCell.setColspan(12);
            mainFrame.addCell(headerCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
