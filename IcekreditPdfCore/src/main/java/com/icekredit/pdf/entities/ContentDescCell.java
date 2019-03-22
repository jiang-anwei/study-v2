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
public class ContentDescCell extends BaseCell implements PdfPCellEvent{
    protected String contentAbstract;
    protected List<String> contentDescItems;
    protected String contentSummary;

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

    public ContentDescCell(String contentAbstract, List<String> contentDescItems, String contentSummary) {
        this(contentAbstract,contentDescItems,contentSummary,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR);
    }
    public ContentDescCell(String contentAbstract, List<String> contentDescItems,
                           String contentSummary, int fontSize, int fontStyle,
                           BaseColor fontColor){
        this(contentAbstract, contentDescItems, contentSummary, fontSize, fontStyle, fontColor,
                DEFAULT_BACKGROUND_COLOR,DEFAULT_BORDER_COLOR,1,12);
    }


    public ContentDescCell(String contentAbstract, List<String> contentDescItems,
                           String contentSummary, int fontSize, int fontStyle,
                           BaseColor fontColor, BaseColor backgroundColor, BaseColor borderColor,
                           int startColumn,int spanColumn) {
        this.contentAbstract = contentAbstract;
        this.contentDescItems = contentDescItems;
        this.contentSummary = contentSummary;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;

        Rectangle valueStrAvailableRectangle = new Rectangle(36,0,559,0);
        float pageWidth = valueStrAvailableRectangle.getWidth();
        valueStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        valueStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);
        int linesCountNeeded = getLinesCountNeeded(valueStrAvailableRectangle);

        this.setFixedHeight(DEFAULT_VISIBLE_CELL_HEIGHT * linesCountNeeded + DEFAULT_PADDING * 2 + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    /**
     * 计算出画整个单元格需要的高度
     * @return
     */
    private int getLinesCountNeeded(Rectangle valueStrAvailableRectangle) {
        int linesCountNeeded = 0;

        linesCountNeeded += TextUtil.getLinesCountNeeded(contentAbstract,DEFAULT_FONT_SIZE,valueStrAvailableRectangle);

        for(String contentDescItem:contentDescItems){
            linesCountNeeded += TextUtil.getLinesCountNeeded(contentDescItem,DEFAULT_FONT_SIZE,valueStrAvailableRectangle);
        }

        linesCountNeeded += TextUtil.getLinesCountNeeded(contentSummary,DEFAULT_FONT_SIZE,valueStrAvailableRectangle);

        return linesCountNeeded;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle availableRectangle = new Rectangle(position);
        /*availableRectangle.setLeft(availableRectangle.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        availableRectangle.setRight(availableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);*/
        availableRectangle.setTop(availableRectangle.getTop() - DEFAULT_PADDING - DEFAULT_MARGIN);
        availableRectangle.setBottom(availableRectangle.getBottom() + DEFAULT_PADDING + DEFAULT_MARGIN);

        if(Debug.DEBUG_FLAG){
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS],availableRectangle);
        }

        int currentLinesDrawn = 0;
        currentLinesDrawn += addStringCell(contentAbstract,availableRectangle, canvases[PdfPTable.BASECANVAS], availableRectangle,currentLinesDrawn,true,false,0);

        int index = 0;
        float paddingLeft = 20;

        Rectangle tempRectangle = new Rectangle(availableRectangle);
        tempRectangle.setLeft(availableRectangle.getLeft() + paddingLeft);
        for(String contentDescItem:contentDescItems){//
            currentLinesDrawn += addStringCell((index + 1) + ")" + contentDescItem,availableRectangle, canvases[PdfPTable.BASECANVAS],
                    tempRectangle,currentLinesDrawn,false,(index == contentDescItems.size() - 1) && contentSummary == null,paddingLeft);
            index ++;
        }

        currentLinesDrawn += addStringCell(contentSummary,availableRectangle, canvases[PdfPTable.BASECANVAS], availableRectangle,currentLinesDrawn,false,true,0);
    }

    /**
     *
     * @param str
     * @param position
     * @param canvas
     * @param noteStrAvailableRectangle
     */
    private int addStringCell(String str,Rectangle position, PdfContentByte canvas, Rectangle noteStrAvailableRectangle,
                               int currentLinesDrown,boolean isNeedDrawTopBorder,boolean isNeedDrawBottomBorder,float paddingLeft) {
        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(str,DEFAULT_FONT_SIZE,noteStrAvailableRectangle);

        for(int linesDrawnCount = 0;linesDrawnCount < linesCountNeedToDrawn;linesDrawnCount ++){
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setLLX(position.getLeft());
            cellChartConfig.setLLY(position.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown + 1));
            cellChartConfig.setURX(position.getRight());
            cellChartConfig.setURY(position.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown));

            cellChartConfig.setPaddingLeft(paddingLeft);

            cellChartConfig.setBorderWidthLeft(DEFAULT_BORDER_WIDTH);
            cellChartConfig.setBorderWidthRight(DEFAULT_BORDER_WIDTH);
            cellChartConfig.setBorderColorLeft(DEFAULT_BORDER_COLOR);
            cellChartConfig.setBorderColorRight(DEFAULT_BORDER_COLOR);

            if(isNeedDrawTopBorder){
                cellChartConfig.setBorderWidthTop(DEFAULT_BORDER_WIDTH);
                cellChartConfig.setBorderColorTop(DEFAULT_BORDER_COLOR);
                cellChartConfig.setMarginTop(DEFAULT_MARGIN);
            }


            if(isNeedDrawBottomBorder){
                cellChartConfig.setBorderWidthBottom(DEFAULT_BORDER_WIDTH);
                cellChartConfig.setBorderColorBottom(DEFAULT_BORDER_COLOR);
            }


            cellChartConfig.setBackgroundColor(backgroundColor);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

            cellChart.fragments.add(getStrSegment(cellChartConfig,str,linesDrawnCount,noteStrAvailableRectangle));

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

    public BaseColor getFontColor() {
        return fontColor;
    }

    public void setFontColor(BaseColor fontColor) {
        this.fontColor = fontColor;
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


            String [] testArray = new String[]{
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
            };
            ContentDescCell noteCell = new ContentDescCell("综合考虑该企业舆情风险程度低", Arrays.asList(testArray),"综合考虑该企业舆情风险程度低");
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
