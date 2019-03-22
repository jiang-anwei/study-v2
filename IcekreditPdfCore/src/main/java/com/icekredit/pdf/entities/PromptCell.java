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
public class PromptCell extends BaseCell implements PdfPCellEvent{
    protected String promptHeader;
    protected List<String> promptItems;

    protected int fontSize;
    private static final int DEFAULT_FONT_SIZE = 10;
    protected int fontStyle;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;
    protected BaseColor fontColor;
    private static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0xcc,0x6d,0x67,0xff);

    private static final int DEFAULT_VISIBLE_CELL_HEIGHT = 16;
    private static final int DEFAULT_PADDING = 2;
    private static final int DEFAULT_MARGIN = 5;

    public PromptCell(String promptHeader, List<String> promptItems) {
        this(promptHeader, promptItems,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR,1,12);
    }

    public PromptCell(String promptHeader, List<String> promptItems,int fontSize, int fontStyle,
                      BaseColor fontColor,int startColumn, int spanColumn) {
        this.promptHeader = promptHeader;
        this.promptItems = promptItems;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;

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

        linesCountNeeded += TextUtil.getLinesCountNeeded(promptHeader,DEFAULT_FONT_SIZE,valueStrAvailableRectangle);

        for(String contentDescItem: promptItems){
            linesCountNeeded += TextUtil.getLinesCountNeeded(contentDescItem,DEFAULT_FONT_SIZE,valueStrAvailableRectangle);
        }

        return linesCountNeeded;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle noteStrAvailableRectangle = new Rectangle(position);
        noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(noteStrAvailableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);
        noteStrAvailableRectangle.setTop(position.getTop()  - DEFAULT_PADDING - DEFAULT_MARGIN);
        noteStrAvailableRectangle.setBottom(position.getBottom() + DEFAULT_PADDING + DEFAULT_MARGIN);


        if(Debug.DEBUG_FLAG){
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS],noteStrAvailableRectangle);
        }

        int currentLinesDrawn = 0;
        currentLinesDrawn += addStringCell(promptHeader,position, canvases[PdfPTable.BASECANVAS], noteStrAvailableRectangle,currentLinesDrawn);

        noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft());
        for(String contentDescItem: promptItems){
            currentLinesDrawn += addStringCell(contentDescItem,position, canvases[PdfPTable.BASECANVAS],
                    noteStrAvailableRectangle,currentLinesDrawn);
        }
    }

    /**
     *
     * @param str
     * @param position
     * @param canvas
     * @param noteStrAvailableRectangle
     */
    private int addStringCell(String str,Rectangle position, PdfContentByte canvas, Rectangle noteStrAvailableRectangle,
                              int currentLinesDrown) {
        int linesCountNeedToDrawn = TextUtil.getLinesCountNeeded(str,DEFAULT_FONT_SIZE,noteStrAvailableRectangle);

        for(int linesDrawnCount = 0;linesDrawnCount < linesCountNeedToDrawn;linesDrawnCount ++){
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setLLX(position.getLeft());
            cellChartConfig.setLLY(position.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown + 1));
            cellChartConfig.setURX(position.getRight());
            cellChartConfig.setURY(position.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown));

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

        messageConfig.setMessage(TextUtil.getSpecLineToDraw(str,DEFAULT_FONT_SIZE,linesDrawnCount,valueStrAvailableRectangle));

        messageConfig.setExtraLink("");
        messageConfig.setFontSize(DEFAULT_FONT_SIZE);
        messageConfig.setFontStyle(DEFAULT_FONT_STYLE);
        messageConfig.setFontColor(DEFAULT_FONT_COLOR);

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
            mainFrame.getDefaultCell().setBorder(NO_BORDER);


            String [] testArray = new String[]{
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
            };
            PromptCell noteCell = new PromptCell("重要提示:", Arrays.asList(testArray));
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
