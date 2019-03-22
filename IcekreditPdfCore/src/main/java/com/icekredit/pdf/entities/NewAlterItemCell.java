package com.icekredit.pdf.entities;

import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

/**
 * Created by icekredit on 7/26/16.
 */
public class NewAlterItemCell extends BaseCell{
    protected String alterFrom;
    protected List<Map<String,String>> alterToList;

    protected int fontSize;
    private static final int DEFAULT_FONT_SIZE = 10;
    protected int fontStyle;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;
    protected BaseColor fontColor;
    private static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0x00,0x45,0x85,0xff);

    public static final String ALTER_CONTENT = "alter_content";
    public static final String ALTER_TIME = "alter_time";

    protected BaseColor backgroundColor;
    private static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0xf7,0xff,0xff,0xff);

    protected BaseColor borderColor;
    private static final BaseColor DEFAULT_BORDER_COLOR = new BaseColor(0xdf,0xf3,0xff,0xff);
    private static final float DEFAULT_BORDER_WIDTH = 0.4f;

    private static final int DEFAULT_VISIBLE_CELL_HEIGHT = 16;
    private static final int DEFAULT_PADDING = 2;
    private static final int DEFAULT_MARGIN = 5;

    private static final float ALTER_TYPE_WIDTH = FontUtils.baseFontChinese.getWidthPoint("变更至: ",DEFAULT_FONT_SIZE);
    private static final float ALTER_TIME_WIDTH = FontUtils.baseFontChinese.getWidthPoint("----变更日期: yyyy-MM-dd",DEFAULT_FONT_SIZE);


    public NewAlterItemCell(String alterFrom, List<Map<String, String>> alterToList){
        this(alterFrom,alterToList,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR,
                DEFAULT_BACKGROUND_COLOR,DEFAULT_BORDER_COLOR);
    }

    public NewAlterItemCell(String alterFrom, List<Map<String, String>> alterToList,
                            int fontSize, int fontStyle, BaseColor fontColor,
                            BaseColor backgroundColor, BaseColor borderColor) {
        this.alterFrom = alterFrom;
        this.alterToList = alterToList;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;

        this.setNoWrap(false);

        initNewAlterItemCell();
    }

    /**
     * 初始化变更信息单元格
     */
    private void initNewAlterItemCell() {
        try {
            PdfPTable wrapperTable = new PdfPTable(12);
            wrapperTable.setWidthPercentage(100);


            BaseBorderedCell baseBorderedCell = new BaseBorderedCell("从:",
                    BaseBorderedCell.DEFAULT_BORDER_WIDTH,borderColor,
                    true,false,true,false,backgroundColor);
            baseBorderedCell.setColspan(1);
            baseBorderedCell.setVerticalAlignment(Element.ALIGN_TOP);
            baseBorderedCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            baseBorderedCell.setNoWrap(false);
            wrapperTable.addCell(baseBorderedCell);

            baseBorderedCell = new BaseBorderedCell(alterFrom,
                    BaseBorderedCell.DEFAULT_BORDER_WIDTH,borderColor,
                    true,false,false,false,backgroundColor);
            baseBorderedCell.setColspan(8);
            baseBorderedCell.setVerticalAlignment(Element.ALIGN_CENTER);
            baseBorderedCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            baseBorderedCell.setNoWrap(false);
            wrapperTable.addCell(baseBorderedCell);

            baseBorderedCell = new BaseBorderedCell("",
                    BaseBorderedCell.DEFAULT_BORDER_WIDTH,borderColor,
                    true,false,false,true,backgroundColor);
            baseBorderedCell.setColspan(3);
            baseBorderedCell.setVerticalAlignment(Element.ALIGN_TOP);
            baseBorderedCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            baseBorderedCell.setNoWrap(false);
            wrapperTable.addCell(baseBorderedCell);

            int rowIndex = 0;
            for(Map alterTo:alterToList){
                baseBorderedCell = new BaseBorderedCell("变更至:",
                        BaseBorderedCell.DEFAULT_BORDER_WIDTH,borderColor,
                        false,rowIndex == alterToList.size() - 1,true,false,backgroundColor);
                baseBorderedCell.setColspan(1);
                baseBorderedCell.setVerticalAlignment(Element.ALIGN_TOP);
                baseBorderedCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                baseBorderedCell.setNoWrap(false);
                wrapperTable.addCell(baseBorderedCell);

                baseBorderedCell = new BaseBorderedCell((String) alterTo.get(ALTER_CONTENT),
                        BaseBorderedCell.DEFAULT_BORDER_WIDTH,borderColor,
                        false,rowIndex == alterToList.size() - 1,false,false,backgroundColor);
                baseBorderedCell.setColspan(8);
                baseBorderedCell.setVerticalAlignment(Element.ALIGN_CENTER);
                baseBorderedCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                baseBorderedCell.setNoWrap(false);
                wrapperTable.addCell(baseBorderedCell);

                baseBorderedCell = new BaseBorderedCell("变更时间：" + (String) alterTo.get(ALTER_TIME),
                        BaseBorderedCell.DEFAULT_BORDER_WIDTH,borderColor,
                        false,rowIndex == alterToList.size() - 1,false,true,backgroundColor);
                baseBorderedCell.setColspan(3);
                baseBorderedCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                baseBorderedCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                baseBorderedCell.setNoWrap(false);
                wrapperTable.addCell(baseBorderedCell);

                rowIndex ++;
            }

            this.addElement(wrapperTable);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            mainFrame.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            List<Map<String,String>> data = new ArrayList<Map<String,String>>();
            Map<String,String> map = new HashMap<String,String>();
            map.put(ALTER_CONTENT,"A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)");
            map.put(ALTER_TIME,"2005年7月1日");
            data.add(map);
            data.add(map);

            NewAlterItemCell noteCell = new NewAlterItemCell("A公司40(40%),B公司60(60%)",data);
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
