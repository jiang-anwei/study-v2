package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.TextUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.w3c.dom.css.Rect;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icekredit on 7/26/16.
 */
public class AlterItemCell extends BaseCell implements PdfPCellEvent{
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


    public AlterItemCell(String alterFrom, List<Map<String, String>> alterToList){
        this(alterFrom,alterToList,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR,
                DEFAULT_BACKGROUND_COLOR,DEFAULT_BORDER_COLOR,1,12);
    }

    public AlterItemCell(String alterFrom, List<Map<String, String>> alterToList,
                         int fontSize, int fontStyle, BaseColor fontColor,
                         BaseColor backgroundColor, BaseColor borderColor,int startColumn,int spanColumn) {
        this.alterFrom = alterFrom;
        this.alterToList = alterToList;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;

        Rectangle valueStrAvailableRectangle = new Rectangle(36,0,559,0);
        float pageWidth = valueStrAvailableRectangle.getWidth();
        valueStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        valueStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        this.setFixedHeight(getAlterInfoTotalLinesCount(alterFrom,alterToList,valueStrAvailableRectangle) * DEFAULT_VISIBLE_CELL_HEIGHT + DEFAULT_MARGIN * 2 + DEFAULT_PADDING * 2);

        this.setCellEvent(this);
    }

    /**
     *   由于变更信息条目内容可能比骄多，所以此处在向页面主框架中添加变更信息条目时会先检查空间是否足够
     * @param alterFrom
     * @param alterToList
     * @param startColumn
     * @param spanColumn
     * @return
     */
    public static boolean isSpaceEnough(String alterFrom,List<Map<String,String>> alterToList,int startColumn,int spanColumn){
        Rectangle valueStrAvailableRectangle = new Rectangle(36,0,559,0);
        float pageWidth = valueStrAvailableRectangle.getWidth();
        valueStrAvailableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        valueStrAvailableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        float heightNeeded = getAlterInfoTotalLinesCount(alterFrom,alterToList, valueStrAvailableRectangle) * DEFAULT_VISIBLE_CELL_HEIGHT + DEFAULT_MARGIN * 2 + DEFAULT_PADDING * 2;
        float maxHeightAvailable = PageSize.A4.getHeight() - 72;

        if(heightNeeded > maxHeightAvailable){
            return false;
        }

        return true;
    }

    /**
     * 此处用于计算当前单元格总共需要的宽度
     * @param alterFrom
     * @param alterToList
     * @param valueStrAvailableRectangle
     * @return
     */
    private static int getAlterInfoTotalLinesCount(String alterFrom, List<Map<String, String>> alterToList, Rectangle valueStrAvailableRectangle) {
        Rectangle availableRectangle = new Rectangle(36,0, PageSize.A4.getWidth(),0);
        availableRectangle.setLeft(availableRectangle.getLeft() + ALTER_TYPE_WIDTH);
        availableRectangle.setRight(availableRectangle.getRight() - ALTER_TIME_WIDTH);

        int totalLinesNeeded = 0;

        totalLinesNeeded += TextUtil.getLinesCountNeeded(alterFrom,DEFAULT_FONT_SIZE,availableRectangle);

        for(Map<String,String> alterToItem:alterToList){
            totalLinesNeeded += TextUtil.getLinesCountNeeded(alterToItem.get(ALTER_CONTENT),DEFAULT_FONT_SIZE,availableRectangle);
        }

        return totalLinesNeeded;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle cellSpaceRectangle = new Rectangle(position);
        cellSpaceRectangle.setLeft(cellSpaceRectangle.getLeft() + DEFAULT_MARGIN);
        cellSpaceRectangle.setRight(cellSpaceRectangle.getRight() - DEFAULT_MARGIN);
        cellSpaceRectangle.setTop(cellSpaceRectangle.getTop()  - DEFAULT_MARGIN);
        cellSpaceRectangle.setBottom(cellSpaceRectangle.getBottom() + DEFAULT_MARGIN);

        Rectangle validSpaceRectangle = new Rectangle(cellSpaceRectangle);
        validSpaceRectangle.setLeft(validSpaceRectangle.getLeft() + DEFAULT_PADDING);
        validSpaceRectangle.setRight(validSpaceRectangle.getRight() - DEFAULT_PADDING);
        validSpaceRectangle.setTop(validSpaceRectangle.getTop() - DEFAULT_PADDING);
        validSpaceRectangle.setBottom(validSpaceRectangle.getBottom() + DEFAULT_PADDING);

        //可用的用于勾画变更信息具体内容的矩形区域
        Rectangle validAlterContentSpaceRectangle = new Rectangle(position);
        validAlterContentSpaceRectangle.setLeft(validAlterContentSpaceRectangle.getLeft() + ALTER_TYPE_WIDTH);
        validAlterContentSpaceRectangle.setRight(validAlterContentSpaceRectangle.getRight() - ALTER_TIME_WIDTH);

        int currentLinesDrown = 0;
        if(alterFrom != null && !alterFrom.trim().equals("")){
            int linesCountNeeded = TextUtil.getLinesCountNeeded(alterFrom,DEFAULT_FONT_SIZE,validAlterContentSpaceRectangle);

            for (int linesCountAlreadyDrawn = 0;linesCountAlreadyDrawn < linesCountNeeded;linesCountAlreadyDrawn ++){
                addCell(canvases[PdfPTable.BASECANVAS],
                        linesCountAlreadyDrawn == 0 ? "从:" : "",
                        TextUtil.getSpecLineToDraw(alterFrom,DEFAULT_FONT_SIZE,linesCountAlreadyDrawn,validAlterContentSpaceRectangle),
                        "",
                        "",
                        cellSpaceRectangle,
                        currentLinesDrown,
                        linesCountAlreadyDrawn == 0,
                        alterToList.size() == 0 && currentLinesDrown == linesCountNeeded);
                currentLinesDrown ++;
            }
        }

        int totalLinesNeeded = getAlterInfoTotalLinesCount(alterFrom,alterToList, cellSpaceRectangle);

        String alterToItem = null;
        for(Map<String,String> map:alterToList){
            alterToItem = map.get(ALTER_CONTENT);

            int linesCountNeeded = TextUtil.getLinesCountNeeded(alterToItem,DEFAULT_FONT_SIZE,validAlterContentSpaceRectangle);

            for(int linesCountAlreadyDrawn = 0;linesCountAlreadyDrawn < linesCountNeeded;linesCountAlreadyDrawn ++){
                addCell(canvases[PdfPTable.BASECANVAS],
                        linesCountAlreadyDrawn == 0 ? "变更至:" : "",
                        TextUtil.getSpecLineToDraw(alterToItem,DEFAULT_FONT_SIZE,linesCountAlreadyDrawn,validAlterContentSpaceRectangle),
                        linesCountAlreadyDrawn == (linesCountNeeded - 1) ? "变更日期:" : "",
                        linesCountAlreadyDrawn == (linesCountNeeded - 1) ? map.get(ALTER_TIME) : "",
                        cellSpaceRectangle,
                        currentLinesDrown,
                        false,
                        currentLinesDrown == (totalLinesNeeded - 1));

                currentLinesDrown ++;
            }
        }
    }

    private void addCell(PdfContentByte canvas, String alterType, String alterContent, String alterTimeDesc, String alterTime,
                         Rectangle cellSpaceRectangle, int currentLinesDrown,
                         boolean isNeedDrawTopBorder, boolean isNeedDrawBottomBorder) {
        CellChartConfig cellChartConfig = CellChartConfig.newInstance();
        cellChartConfig.setLLX(cellSpaceRectangle.getLeft());
        cellChartConfig.setLLY(cellSpaceRectangle.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown + 1));
        cellChartConfig.setURX(cellSpaceRectangle.getRight());
        cellChartConfig.setURY(cellSpaceRectangle.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (currentLinesDrown));

        cellChartConfig.setBorderWidthLeft(DEFAULT_BORDER_WIDTH);
        cellChartConfig.setBorderWidthRight(DEFAULT_BORDER_WIDTH);
        cellChartConfig.setBorderColorLeft(DEFAULT_BORDER_COLOR);
        cellChartConfig.setBorderColorRight(DEFAULT_BORDER_COLOR);

        cellChartConfig.setPadding(DEFAULT_PADDING);

        if(isNeedDrawTopBorder){
            cellChartConfig.setBorderWidthTop(DEFAULT_BORDER_WIDTH);
            cellChartConfig.setBorderColorTop(DEFAULT_BORDER_COLOR);
        }

        if(isNeedDrawBottomBorder){
            cellChartConfig.setBorderWidthBottom(DEFAULT_BORDER_WIDTH);
            cellChartConfig.setBorderColorBottom(DEFAULT_BORDER_COLOR);
        }

        cellChartConfig.setBackgroundColor(backgroundColor);

        CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

        cellChart.fragments.add(getStrSegment(cellChartConfig,alterType, FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE,ALTER_TYPE_WIDTH));
        cellChart.fragments.add(getStrSegment(cellChartConfig,alterContent, FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE,-1));
        cellChart.fragments.add(getStrSegment(cellChartConfig,alterTime, FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE,-1));
        cellChart.fragments.add(getStrSegment(cellChartConfig,alterTimeDesc, FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE,-1));

        cellChart.draw(canvas);
    }

    private Fragment getStrSegment(CellChartConfig cellChartConfig, String str, int layoutGravity, float minWidth) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(str);
        messageConfig.setExtraLink("");
        messageConfig.setFontSize(DEFAULT_FONT_SIZE);
        messageConfig.setFontStyle(DEFAULT_FONT_STYLE);
        messageConfig.setFontColor(DEFAULT_FONT_COLOR);

        if(minWidth != -1){
            messageConfig.setWidth(minWidth);
        }

        messageConfig.setLayoutGravity(layoutGravity);

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
            mainFrame.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            List<Map<String,String>> data = new ArrayList<Map<String,String>>();
            Map<String,String> map = new HashMap<String,String>();
            map.put(ALTER_CONTENT,"A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)A公司100(50%),B公司100(50%)");
            map.put(ALTER_TIME,"2005年7月1日");
            data.add(map);
            data.add(map);

            AlterItemCell noteCell = new AlterItemCell("A公司40(40%),B公司60(60%)",data);
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
