package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.TextUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 7/22/16.
 */
public class KeyValueCell extends BaseCell implements PdfPCellEvent {
    public float cellHeight;
    public static final float DEFAULT_VISIBLE_BORDER_WIDTH_BOTTOM = 0.5f;
    protected boolean isNeededShowBorderBottom;
    private static final boolean DEFAULT_IS_NEED_SHOW_BORDER_BOTTOM = true;

    public static final float DEFAULT_VISIBLE_CELL_HEIGHT = 18f;

    protected BaseColor keyStrColor;
    private static final BaseColor KEY_STR_COLOR = new BaseColor(0x9f, 0xa0, 0xa0, 0xff);
    protected BaseColor valueStrColor;
    private static final BaseColor VALUE_STR_COLOR = new BaseColor(0x72, 0x71, 0x71, 0xff);

    private int fontSize = 10;
    private static final int DEFAULT_FONT_SIZE = 10;

    private int fontStyle;

    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;

    private static final BaseColor DEFAULT_VISIBLE_BORDER_COLOR_BOTTOM = new BaseColor(0xef, 0xef, 0xef, 0xff);

    protected String keyStr;
    protected String valueStr;

    protected String keyStrExtraLink;
    protected String valueStrExtraLink;

    protected float keyStrWidth;
    protected int startColumn;
    protected int spanColumn;

    private static final float DEFAULT_PADDING = 2;
    private static final float DEFAULT_MARGIN = 2;

    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xff, 0xff, 0xff, 0xff);

    public KeyValueCell(String keyStr, String valueStr) {
        this(keyStr, valueStr, "", "", 1, 12, 100);
    }

    public KeyValueCell(String keyStr, String valueStr,
                        String keyStrExtraLink, String valueStrExtraLink,
                        int startColumn, int spanColumn, float keyStrWidth) {
        this(keyStr, valueStr, keyStrExtraLink, valueStrExtraLink, startColumn, spanColumn, keyStrWidth,
                DEFAULT_VISIBLE_CELL_HEIGHT, DEFAULT_IS_NEED_SHOW_BORDER_BOTTOM);
    }


    public KeyValueCell(String keyStr, String valueStr,
                        String keyStrExtraLink, String valueStrExtraLink,
                        int startColumn, int spanColumn, float keyStrWidth, float cellHeight,
                        boolean isNeededShowBorderBottom) {
        this.keyStr = keyStr;
        this.keyStrColor = KEY_STR_COLOR;
        this.valueStr = valueStr;
        this.valueStrColor = VALUE_STR_COLOR;
        this.keyStrExtraLink = keyStrExtraLink;
        this.valueStrExtraLink = valueStrExtraLink;
        this.keyStrWidth = keyStrWidth;

        this.cellHeight = cellHeight;
        this.isNeededShowBorderBottom = isNeededShowBorderBottom;

        this.fontSize = DEFAULT_FONT_SIZE;
        this.fontStyle = DEFAULT_FONT_STYLE;
        this.startColumn = startColumn;
        this.spanColumn = spanColumn;

        int linesCountNeeded = getLinesCount();

        this.setFixedHeight(cellHeight * linesCountNeeded + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    public int getLinesCount(){
        Rectangle valueStrAvailableRectangle = new Rectangle(36, 0, 559, 0);

        float pageWidth = valueStrAvailableRectangle.getWidth();

        valueStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN + keyStrWidth);
        valueStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        int linesCountNeeded = TextUtil.getLinesCountNeeded(valueStr, fontSize, valueStrAvailableRectangle);

        return linesCountNeeded;
    }

    public float getCellHeight(){
        Rectangle valueStrAvailableRectangle = new Rectangle(36, 0, 559, 0);

        float pageWidth = valueStrAvailableRectangle.getWidth();

        valueStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN + keyStrWidth);
        valueStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        int linesCountNeeded = TextUtil.getLinesCountNeeded(valueStr, fontSize, valueStrAvailableRectangle);

        return cellHeight * linesCountNeeded + DEFAULT_MARGIN * 2;
    }

    @Override
    public void cellLayout(PdfPCell pdfPCell, Rectangle position, PdfContentByte[] pdfContentBytes) {
        Rectangle valueStrAvailableRectangle = new Rectangle(position);
        valueStrAvailableRectangle.setLeft(valueStrAvailableRectangle.getLeft() + keyStrWidth + DEFAULT_PADDING + DEFAULT_MARGIN);
        valueStrAvailableRectangle.setRight(valueStrAvailableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);
        valueStrAvailableRectangle.setTop(position.getTop() - DEFAULT_MARGIN);
        valueStrAvailableRectangle.setBottom(position.getBottom() + DEFAULT_MARGIN);

        if (Debug.DEBUG_FLAG) {
            View.showPosition(pdfContentBytes[PdfPTable.BACKGROUNDCANVAS], valueStrAvailableRectangle);
        }

        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(valueStr, fontSize, valueStrAvailableRectangle);

        boolean isFirstOne = true;
        for (int linesDrawnCount = 0; linesDrawnCount < linesCountNeedToDrawn; linesDrawnCount++) {
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setLLX(position.getLeft());
            cellChartConfig.setLLY(position.getBottom() + cellHeight * (linesCountNeedToDrawn - linesDrawnCount - 1));
            cellChartConfig.setURX(position.getRight());
            cellChartConfig.setURY(position.getBottom() + cellHeight * (linesCountNeedToDrawn - linesDrawnCount));

            cellChartConfig.setBackgroundColor(CELL_BACKGROUND_COLOR);

            showBorders(cellChartConfig);

            cellChartConfig.setPadding(DEFAULT_PADDING);

            cellChartConfig.setBackgroundColor(CELL_BACKGROUND_COLOR);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(), cellChartConfig);

            if (isFirstOne) {
                cellChart.fragments.add(getKeyStrSegment(cellChartConfig, keyStr,position));
                isFirstOne = false;
            } else {
                cellChart.fragments.add(getKeyStrSegment(cellChartConfig, "",position));
            }

            cellChart.fragments.add(getValueStrSegment(cellChartConfig, linesDrawnCount, valueStrAvailableRectangle, linesCountNeedToDrawn > 1));

            cellChart.draw(pdfContentBytes[PdfPTable.BASECANVAS]);
        }
    }

    protected void showBorders(CellChartConfig cellChartConfig) {
        if (isNeededShowBorderBottom) {
            cellChartConfig.setBorderWidthBottom(DEFAULT_VISIBLE_BORDER_WIDTH_BOTTOM);
            cellChartConfig.setBorderColorBottom(DEFAULT_VISIBLE_BORDER_COLOR_BOTTOM);
        }
    }

    private Fragment getKeyStrSegment(CellChartConfig cellChartConfig, String keyStr, Rectangle valueStrAvailableRectangle) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);
        messageConfig.setMessage(TextUtil.getSpecLineToDraw(keyStr,fontSize,0,valueStrAvailableRectangle));
        messageConfig.setExtraLink(keyStrExtraLink);
        messageConfig.setFontSize(fontSize);
        messageConfig.setFontStyle(fontStyle);

        if(valueStr == null || valueStr.trim().equals("")){
            messageConfig.setFontColor(valueStrColor);
        } else {
            messageConfig.setFontColor(keyStrColor);
        }

        messageConfig.setWidth(keyStrWidth);

        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        return new Fragment(messageConfig);
    }


    private Fragment getValueStrSegment(CellChartConfig cellChartConfig, int linesDrawnCount, Rectangle valueStrAvailableRectangle, boolean isMultipleLine) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        String currentMessageToDraw = TextUtil.getSpecLineToDraw(valueStr, fontSize, linesDrawnCount, valueStrAvailableRectangle);

        messageConfig.setMessage(currentMessageToDraw);
        messageConfig.setExtraLink(valueStrExtraLink);
        messageConfig.setFontSize(fontSize);
        messageConfig.setFontStyle(fontStyle);
        messageConfig.setFontColor(valueStrColor);
        messageConfig.setWidth(FontUtils.baseFontChinese.getWidthPoint(currentMessageToDraw,fontSize));

        if (isMultipleLine) {
            messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);
        } else {
            messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);
        }

        return new Fragment(messageConfig);
    }

    public boolean isNeededShowBorderBottom() {
        return isNeededShowBorderBottom;
    }

    public void setNeededShowBorderBottom(boolean neededShowBorderBottom) {
        isNeededShowBorderBottom = neededShowBorderBottom;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public static void main(String[] args) {
        String destFileStr = "result/test.pdf";
        File destFile = new File(destFileStr);
        destFile.getParentFile().mkdirs();

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);
            mainFrame.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPCell headerCell = new KeyValueCell("基本信息", "");
            headerCell.setColspan(6);

            PdfPCell nameCell = new KeyValueCell("姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名姓名", "");
            nameCell.setColspan(6);

            KeyValueCell birthdayCell = new KeyValueCell("出生日期",
                    "出生日期出生日期出生日期出生日期出生日期出生日期出生日期出生日期出生日期出生日期出生日期出生日期出生日期"
            ,"","",1,6, FontUtils.baseFontChinese.getWidthPoint("出生日期",10));
            birthdayCell.setNeededShowBorderBottom(true);
            birthdayCell.setColspan(6);
            birthdayCell.setBorder(PdfPCell.NO_BORDER);

            mainFrame.addCell(headerCell);
            mainFrame.addCell(nameCell);
            mainFrame.addCell(birthdayCell);
            mainFrame.addCell(new EmptyCell(6));
//            PdfPCell genderCell = new KeyValueCell("性别", " 男");
//            genderCell.setColspan(6);
//            mainFrame.addCell(genderCell);
//            mainFrame.addCell(genderCell);
//
//            PdfPCell anotherCell = new KeyValueCell("员工", "");
//            anotherCell.setColspan(1);
//
//            for (int index = 0; index < 12; index++) {
//                mainFrame.addCell(anotherCell);
//            }

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
