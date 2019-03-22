package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
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
 *
 * 具体详细内容单元格
 */
public class ContentMultiDescCell extends BaseCell implements PdfPCellEvent{
    protected List<String> contentDescItems;

    protected int fontSize;
    private static final int DEFAULT_FONT_SIZE = 10;
    protected int fontStyle;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;
    protected BaseColor fontColor;
    private static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0x00,0x45,0x85,0xff);

    protected BaseColor backgroundColor;
    private static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(0xf7,0xff,0xff,0xff);

    protected BaseColor borderColor;
    private static final BaseColor DEFAULT_BORDER_COLOR = new BaseColor(0xdf,0xf3,0xff,0xff);
    private static final float DEFAULT_BORDER_WIDTH = 0.3f;

    private static final int DEFAULT_VISIBLE_CELL_HEIGHT = 16;
    private static final int DEFAULT_PADDING = 2;
    private static final int DEFAULT_MARGIN = 5;

    public ContentMultiDescCell(List<String> contentDescItems) {
        this(contentDescItems,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR);
    }

    public ContentMultiDescCell(List<String> contentDescItems,int fontSize, int fontStyle,BaseColor fontColor){
        this(contentDescItems, fontSize, fontStyle, fontColor,
                DEFAULT_BACKGROUND_COLOR,DEFAULT_BORDER_COLOR,1,12);
    }

    public ContentMultiDescCell(List<String> contentDescItems,int fontSize, int fontStyle,
                                BaseColor fontColor, BaseColor backgroundColor, BaseColor borderColor,
                                int startColumn, int spanColumn) {
        this.contentDescItems = contentDescItems;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;

        Rectangle availableRectangle = new Rectangle(36,0,559,0);
        float pageWidth = availableRectangle.getWidth();
        availableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        availableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);
        int linesCountNeeded = getLinesCountNeeded(availableRectangle);

        this.setFixedHeight(DEFAULT_VISIBLE_CELL_HEIGHT * linesCountNeeded + DEFAULT_PADDING *  + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    /**
     * 计算出画整个单元格需要的高度
     * @return
     */
    private int getLinesCountNeeded(Rectangle availableRectangle) {
        int linesCountNeeded = 0;

        for(String contentDescItem:contentDescItems){
            linesCountNeeded += TextUtil.getLinesCountNeeded(contentDescItem,fontSize,availableRectangle);
        }

        return linesCountNeeded;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle cellSpaceRectangle = new Rectangle(position);
        cellSpaceRectangle.setLeft(cellSpaceRectangle.getLeft() + DEFAULT_MARGIN);
        cellSpaceRectangle.setRight(cellSpaceRectangle.getRight() - DEFAULT_MARGIN);
        cellSpaceRectangle.setTop(cellSpaceRectangle.getTop() - DEFAULT_MARGIN);
        cellSpaceRectangle.setBottom(cellSpaceRectangle.getBottom() + DEFAULT_MARGIN);

        Rectangle validSpaceRectangle = new Rectangle(cellSpaceRectangle);
        validSpaceRectangle.setLeft(validSpaceRectangle.getLeft() + DEFAULT_PADDING);
        validSpaceRectangle.setRight(validSpaceRectangle.getRight() - DEFAULT_PADDING);
        validSpaceRectangle.setTop(validSpaceRectangle.getTop() - DEFAULT_PADDING);
        validSpaceRectangle.setBottom(validSpaceRectangle.getBottom() + DEFAULT_PADDING);

        if(Debug.DEBUG_FLAG){
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS],cellSpaceRectangle);
        }

        int currentLinesDrawn = 0;

        int index = 0;
        for(String contentDescItem:contentDescItems){//(index + 1) + ")" +
            currentLinesDrawn += addStringCell(
                    contentDescItem,canvases[PdfPTable.BASECANVAS],
                    cellSpaceRectangle,validSpaceRectangle,
                    currentLinesDrawn,index == 0,index == contentDescItems.size() - 1,0);

            index ++;
        }
    }

    /**
     *
     * @param str
     * @param canvas
     * @param cellSpaceRectangle
     */
    private int addStringCell(String str, PdfContentByte canvas, Rectangle cellSpaceRectangle,Rectangle validSpaceRectangle,
                               int currentLinesDrown,boolean isNeedDrawTopBorder,boolean isNeedDrawBottomBorder,float paddingLeft) {
        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(str,fontSize,validSpaceRectangle);

        for(int linesDrawnCount = 0;linesDrawnCount < linesCountNeedToDrawn;linesDrawnCount ++){
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setLLX(cellSpaceRectangle.getLeft());
            cellChartConfig.setLLY(cellSpaceRectangle.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown + 1));
            cellChartConfig.setURX(cellSpaceRectangle.getRight());
            cellChartConfig.setURY(cellSpaceRectangle.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown));

            cellChartConfig.setPaddingLeft(paddingLeft);

            cellChartConfig.setBorderWidthLeft(DEFAULT_BORDER_WIDTH);
            cellChartConfig.setBorderWidthRight(DEFAULT_BORDER_WIDTH);
            cellChartConfig.setBorderColorLeft(DEFAULT_BORDER_COLOR);
            cellChartConfig.setBorderColorRight(DEFAULT_BORDER_COLOR);

            cellChartConfig.setPadding(DEFAULT_PADDING);

            if(isNeedDrawTopBorder && linesDrawnCount == 0){
                cellChartConfig.setBorderWidthTop(DEFAULT_BORDER_WIDTH);
                cellChartConfig.setBorderColorTop(DEFAULT_BORDER_COLOR);
            }

            if(isNeedDrawBottomBorder && linesDrawnCount == linesCountNeedToDrawn - 1){
                cellChartConfig.setBorderWidthBottom(DEFAULT_BORDER_WIDTH);
                cellChartConfig.setBorderColorBottom(DEFAULT_BORDER_COLOR);
            }

            cellChartConfig.setBackgroundColor(backgroundColor);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

            cellChart.fragments.add(getStrSegment(cellChartConfig,str,linesDrawnCount,validSpaceRectangle));

            cellChart.draw(canvas);

            currentLinesDrown ++;
        }

        return linesCountNeedToDrawn;
    }

    private Fragment getStrSegment(CellChartConfig cellChartConfig, String str,
                                   int linesDrawnCount, Rectangle valueStrAvailableRectangle) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(TextUtil.getSpecLineToDraw(str,fontSize,linesDrawnCount,valueStrAvailableRectangle));

        messageConfig.setExtraLink("");
        messageConfig.setFontSize(fontSize);
        messageConfig.setFontStyle(fontStyle);
        messageConfig.setFontColor(fontColor);

        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        return new Fragment(messageConfig);
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


            /*String [] testArray = new String[]{
                    "过去一年中,该申请者的银联卡总计消费13810.0元,消费金额在全市排名前35%,在全国排名前35%,消费能力较强"
            };
            ContentMultiDescCell noteCell = new ContentMultiDescCell(Arrays.asList(testArray));
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);*/

            ContentMultiDescCell contentMultiDescCell = new ContentMultiDescCell(
                    Arrays.asList(new String[]{
                                   "过去一年中，该申请者的银联卡总计消费7970.00元，消费金额在全市排名前45%，在全国排名前45%，消费能力一般"
                    })
            );
            contentMultiDescCell.setColspan(12);
            mainFrame.addCell(contentMultiDescCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
