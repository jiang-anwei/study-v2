package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.RegularExpressionUtil;
import com.icekredit.pdf.utils.TextUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by icekredit on 7/24/16.
 * <p/>
 * 基本信息描述单元格
 */
public class InformationCell extends BaseCell implements PdfPCellEvent {
    protected String informationHeader;
    protected int informationHeaderFontSize;
    public static final int INFO_HEADER_FONT_SIZE = 10;
    protected int informationHeaderFontStyle;
    private static final int INFO_HEADER_FONT_STYLE = Font.BOLD;
    protected BaseColor informationHeaderFontColor;
    private static BaseColor INFO_HEADER_FONT_COLOR = new BaseColor(0x72, 0x71, 0x71, 0xff);

    protected List<String> informationItemsName;
    protected List<String> informationItemsValue;

    protected int informationItemFontSize;
    private static final int INFO_ITEM_FONT_SIZE = 10;
    protected int informationItemFontStyle;
    private static final int INFO_ITEM_FONT_STYLE = Font.NORMAL;
    protected BaseColor informationItemFontColor;
    private static final BaseColor INFO_ITEM_FONT_COLOR = new BaseColor(0x9f, 0xa0, 0xa0);

    protected BaseColor cellBackgroundColor;
    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xef, 0xef, 0xef, 0xff);

    protected BaseColor cellBorderColor;

    public static final int DEFAULT_CELL_HEIGHT = 20;
    public static final int DEFAULT_CELL_BORDER_WIDTH = 1;

    protected static final float DEFAULT_PADDING = 4;
    protected static final float DEFAULT_MARGIN = 2;

    protected float nameSegmentWidth;

    public InformationCell(String informationHeader, List<String> informationItemsName,
                           List<String> informationItemsValue, float nameSegmentWidth) {
        this(informationHeader, informationItemsName, informationItemsValue, CELL_BACKGROUND_COLOR, nameSegmentWidth, 1, 12);
    }

    public InformationCell(String informationHeader, List<String> informationItemsName,
                           List<String> informationItemsValue, BaseColor cellBackgroundColor, float nameSegmentWidth,
                           int startColumn, int spanColumn) {
        this(informationHeader, INFO_HEADER_FONT_SIZE, INFO_HEADER_FONT_STYLE, INFO_HEADER_FONT_COLOR,
                informationItemsName, informationItemsValue, INFO_ITEM_FONT_SIZE, INFO_ITEM_FONT_STYLE, INFO_ITEM_FONT_COLOR,
                cellBackgroundColor, nameSegmentWidth, startColumn, spanColumn);
    }

    public InformationCell(String informationHeader, int informationHeaderFontSize,
                           int informationHeaderFontStyle, BaseColor informationHeaderFontColor,
                           List<String> informationItemsName, List<String> informationItemsValue,
                           int informationItemFontSize, int informationItemFontStyle,
                           BaseColor informationItemFontColor, BaseColor cellBackgroundColor,
                           float nameSegmentWidth, int startColumn, int spanColumn) {
        this(informationHeader, informationHeaderFontSize,
                informationHeaderFontStyle, informationHeaderFontColor,
                informationItemsName, informationItemsValue,
                informationItemFontSize, informationItemFontStyle,
                informationItemFontColor, cellBackgroundColor, cellBackgroundColor,
                nameSegmentWidth, startColumn, spanColumn);
    }

    public InformationCell(String informationHeader, int informationHeaderFontSize,
                           int informationHeaderFontStyle, BaseColor informationHeaderFontColor,
                           List<String> informationItemsName, List<String> informationItemsValue,
                           int informationItemFontSize, int informationItemFontStyle,
                           BaseColor informationItemFontColor, BaseColor cellBackgroundColor, BaseColor cellBorderColor,
                           float nameSegmentWidth, int startColumn, int spanColumn) {
        this.informationHeader = informationHeader;
        this.informationHeaderFontSize = informationHeaderFontSize;
        this.informationHeaderFontStyle = informationHeaderFontStyle;
        this.informationHeaderFontColor = informationHeaderFontColor;
        this.informationItemsName = informationItemsName;
        this.informationItemsValue = informationItemsValue;
        this.informationItemFontSize = informationItemFontSize;
        this.informationItemFontStyle = informationItemFontStyle;
        this.informationItemFontColor = informationItemFontColor;
        this.cellBackgroundColor = cellBackgroundColor;
        this.cellBorderColor = cellBorderColor;
        this.nameSegmentWidth = nameSegmentWidth;

        Rectangle valueStrAvailableRectangle = new Rectangle(36, 0, 559, 0);

        float pageWidth = valueStrAvailableRectangle.getWidth();

        valueStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN + nameSegmentWidth);
        valueStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        this.setFixedHeight(DEFAULT_CELL_HEIGHT * getLinesCountNeeded(valueStrAvailableRectangle)
                + DEFAULT_PADDING * 2 + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }


    /**
     * 获取画基本信息单元格需要的行数
     * @param valueStrAvailableRectangle
     * @return
     */
    public int getLinesCountNeeded(Rectangle valueStrAvailableRectangle) {
        int linesCountNeeded = 0;

        linesCountNeeded += TextUtil.getLinesCountNeeded(informationHeader, informationHeaderFontSize, valueStrAvailableRectangle);

        for (String informationItemValue : informationItemsValue) {
            if (RegularExpressionUtil.isTrademarkUrl(informationItemValue)) {
                linesCountNeeded += 2;
            } else {
                linesCountNeeded += TextUtil.getLinesCountNeeded(informationItemValue, informationItemFontSize, valueStrAvailableRectangle);
            }
        }

        return linesCountNeeded;
    }


    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle noteStrAvailableRectangle = new Rectangle(position);
        /*noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(noteStrAvailableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);*/
        noteStrAvailableRectangle.setTop(position.getTop() - DEFAULT_PADDING - DEFAULT_MARGIN);
        noteStrAvailableRectangle.setBottom(position.getBottom() + DEFAULT_PADDING + DEFAULT_MARGIN);

        if (Debug.DEBUG_FLAG) {
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS], noteStrAvailableRectangle);
        }

        boolean isTopBorderShown = false;
        boolean isBottomBorderShown = false;

        int currentLinesDrawn = 0;

        if (informationHeader != null && !informationHeader.trim().equals("")) {
            currentLinesDrawn += addStringCell("", informationHeader, noteStrAvailableRectangle, canvases[PdfPTable.BASECANVAS],
                    noteStrAvailableRectangle, currentLinesDrawn,
                    informationHeaderFontSize, informationHeaderFontStyle, informationHeaderFontColor, 0, 0,!isTopBorderShown,informationItemsValue.isEmpty());
        }

        int index = 0;
        for (String informationItemValue : informationItemsValue) {
            currentLinesDrawn += addStringCell(index < informationItemsName.size() ? informationItemsName.get(index) : ""
                    , informationItemValue, noteStrAvailableRectangle, canvases[PdfPTable.BASECANVAS],
                    noteStrAvailableRectangle, currentLinesDrawn,
                    informationItemFontSize, informationItemFontStyle, informationItemFontColor, 10, nameSegmentWidth,
                    false,!isBottomBorderShown && index == informationItemsValue.size() - 1);
            index++;
        }
    }

    /**
     * @param informationItemValue
     * @param position
     * @param canvas
     * @param noteStrAvailableRectangle
     */
    protected int addStringCell(String informationItemName, String informationItemValue, Rectangle position, PdfContentByte canvas, Rectangle noteStrAvailableRectangle,
                                int currentLinesDrown, int fontSize, int fontStyle, BaseColor fontColor, int paddingLeft, float nameSegmentWidth,
                                boolean isNeedShowTopBorder,boolean isNeedShowBottomBorder) {
        Rectangle availableRect = new Rectangle(noteStrAvailableRectangle);
        availableRect.setLeft(availableRect.getLeft() + paddingLeft + nameSegmentWidth);
        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(informationItemValue, fontSize, availableRect);

        noteStrAvailableRectangle = new Rectangle(noteStrAvailableRectangle);
        noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + paddingLeft);

        for (int linesDrawnCount = 0; linesDrawnCount < linesCountNeedToDrawn; linesDrawnCount++) {
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setLLX(position.getLeft());
            cellChartConfig.setLLY(position.getTop() - DEFAULT_CELL_HEIGHT * (currentLinesDrown + 1));
            cellChartConfig.setURX(position.getRight());
            cellChartConfig.setURY(position.getTop() - DEFAULT_CELL_HEIGHT * (currentLinesDrown));

            cellChartConfig.setPaddingLeft(paddingLeft);

            cellChartConfig.setMarginRight(DEFAULT_MARGIN);

            cellChartConfig.setBackgroundColor(cellBackgroundColor);

            cellChartConfig.setBorderWidth(DEFAULT_CELL_BORDER_WIDTH);
            cellChartConfig.setBorderColor(cellBorderColor);

            if(!isNeedShowTopBorder && linesDrawnCount == 0){
                cellChartConfig.setBorderColorTop(cellBackgroundColor);
                cellChartConfig.setBorderWidthTop(0);
            }

            if(!isNeedShowBottomBorder && linesDrawnCount == linesCountNeedToDrawn - 1){
                cellChartConfig.setBorderColorBottom(cellBackgroundColor);
                cellChartConfig.setBorderWidthBottom(0);
            }

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(), cellChartConfig);

            if (linesDrawnCount == 0) {
                cellChart.fragments.add(getStrSegment(cellChartConfig, informationItemName, fontSize, fontStyle, fontColor,
                        linesDrawnCount, noteStrAvailableRectangle, nameSegmentWidth));

                noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + nameSegmentWidth);

                if (informationItemValue != null || !informationItemValue.trim().equals("")) {
                    cellChart.fragments.add(getStrSegment(cellChartConfig, informationItemValue, fontSize, fontStyle, fontColor,
                            linesDrawnCount, noteStrAvailableRectangle, -1));
                }
            } else {
                cellChart.fragments.add(getStrSegment(cellChartConfig, "", fontSize, fontStyle, fontColor,
                        linesDrawnCount, noteStrAvailableRectangle, nameSegmentWidth));

                if (informationItemValue != null || !informationItemValue.trim().equals("")) {
                    cellChart.fragments.add(getStrSegment(cellChartConfig, informationItemValue, fontSize, fontStyle, fontColor,
                            linesDrawnCount, noteStrAvailableRectangle, -1));
                }
            }

            cellChart.draw(canvas);

            currentLinesDrown++;
        }

        return linesCountNeedToDrawn;
    }

    protected Fragment getStrSegment(CellChartConfig cellChartConfig, String str, int fontSize, int fontStyle, BaseColor fontColor,
                                     int linesDrawnCount, Rectangle valueStrAvailableRectangle, float width) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(TextUtil.getSpecLineToDraw(str, fontSize, linesDrawnCount, valueStrAvailableRectangle));
        messageConfig.setExtraLink("");
        messageConfig.setFontSize(fontSize);
        messageConfig.setFontStyle(fontStyle);
        messageConfig.setFontColor(fontColor);

        if (width != -1) {
            messageConfig.setWidth(width);
            messageConfig.setMarginLeft(DEFAULT_MARGIN);
        }

        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        return new Fragment(messageConfig);
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
            mainFrame.getDefaultCell().setBorder(NO_BORDER);


            String[] testArray = new String[]{
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
            };

            String[] testArray1 = new String[]{"注册号", "注册号", "注册号", "注册号", "注册号", "法人"};

            InformationCell noteCell = new InformationCell("上海冰鉴信息科技有限公司上海冰鉴信息科技有限公司上海冰鉴信息科技有限公司上海冰鉴信息科技有限公司上海冰鉴信息科技有限公司", Arrays.asList(testArray1),
                    Arrays.asList(testArray), 100);
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseColor getCellBorderColor() {
        return cellBorderColor;
    }

    public void setCellBorderColor(BaseColor cellBorderColor) {
        this.cellBorderColor = cellBorderColor;
    }

    public BaseColor getCellBackgroundColor() {
        return cellBackgroundColor;
    }

    public void setCellBackgroundColor(BaseColor cellBackgroundColor) {
        this.cellBackgroundColor = cellBackgroundColor;
    }
}
