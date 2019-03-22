package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
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
 * Created by icekredit on 7/27/16.
 */
public class DetailCell extends BaseCell implements PdfPCellEvent{
    protected List<String> detailContents;
    protected int fontSize;
    private static final int DEFAULT_FONT_SIZE = 10;
    protected int fontStyle;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;
    protected BaseColor fontColor;
    private static final BaseColor DEFAULT_FONT_COLOR = new BaseColor(0x00,0x45,0x85,0xff);

    private static final float DEFAULT_VISIBLE_CELL_HEIGHT = 24;
    private static final float DEFAULT_MARGIN = 2;
    private static final float DEFAULT_PADDING = 2;

    protected BaseColor backgroundColor;
    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    public DetailCell(List<String> detailContents) {
        this(detailContents,DEFAULT_FONT_SIZE,DEFAULT_FONT_STYLE,DEFAULT_FONT_COLOR,CELL_BACKGROUND_COLOR,1,12);
    }


    public DetailCell(List<String> detailContents, int fontSize, int fontStyle, BaseColor fontColor){
        this(detailContents, fontSize, fontStyle, fontColor,CELL_BACKGROUND_COLOR,1,12);
    }


    public DetailCell(List<String> detailContents, int fontSize, int fontStyle, BaseColor fontColor, BaseColor backgroundColor, int startColumn, int spanColumn) {
        this.detailContents = detailContents;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;

        Rectangle availableRectangle = new Rectangle(36,0,559,0);

        float pageWidth = availableRectangle.getWidth();

        availableRectangle.setLeft(36 + (startColumn - 1) * pageWidth / 12 + DEFAULT_PADDING + DEFAULT_MARGIN);
        availableRectangle.setRight(36 + (startColumn - 1) * pageWidth / 12 + spanColumn * pageWidth / 12 - DEFAULT_PADDING - DEFAULT_MARGIN);

        this.setFixedHeight(getLinesCountNeeded(availableRectangle) * DEFAULT_VISIBLE_CELL_HEIGHT
                + DEFAULT_PADDING * 2 + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    private int getLinesCountNeeded(Rectangle availableRectangle){
        int linesCountNeeded = 0;

        for(String detailContent:detailContents){
            linesCountNeeded += TextUtil.getLinesCountNeeded(detailContent,this.fontSize,availableRectangle);
        }

        return linesCountNeeded;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle availableRectangle = new Rectangle(position);

        availableRectangle.setLeft(position.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        availableRectangle.setRight(position.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);
        availableRectangle.setTop(position.getTop()  - DEFAULT_PADDING - DEFAULT_MARGIN);
        availableRectangle.setBottom(position.getBottom() + DEFAULT_PADDING + DEFAULT_MARGIN);

        int linesDrown = 0;

        for(String detailContent:detailContents){
            int linesCountNeeded = TextUtil.getLinesCountNeeded(detailContent,fontSize,availableRectangle);

            for(int currentLinesDrawn = 0;currentLinesDrawn < linesCountNeeded;currentLinesDrawn ++){
                CellChartConfig cellChartConfig = CellChartConfig.newInstance();
                cellChartConfig.setLLX(position.getLeft());
                cellChartConfig.setLLY(position.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (linesDrown + 1));
                cellChartConfig.setURX(position.getRight());
                cellChartConfig.setURY(position.getTop() - DEFAULT_VISIBLE_CELL_HEIGHT * (linesDrown));

                cellChartConfig.setPaddingLeft(DEFAULT_PADDING);
                cellChartConfig.setPaddingRight(DEFAULT_PADDING);

                cellChartConfig.setMarginRight(DEFAULT_MARGIN);
                cellChartConfig.setMarginLeft(DEFAULT_MARGIN);

                cellChartConfig.setBackgroundColor(backgroundColor);

                CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

                cellChart.fragments.add(getDetailStrSegment(detailContent,cellChartConfig,currentLinesDrawn,availableRectangle));

                cellChart.draw(canvases[PdfPTable.BASECANVAS]);

                linesDrown ++;
            }
        }
    }

    private Fragment getDetailStrSegment(String detailContent, CellChartConfig cellChartConfig, int linesDrawnCount, Rectangle valueStrAvailableRectangle) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(TextUtil.getSpecLineToDraw(detailContent,fontSize,linesDrawnCount,valueStrAvailableRectangle));
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
            mainFrame.getDefaultCell().setBorder(NO_BORDER);

            DetailCell noteCell = new DetailCell(Arrays.asList(new String[]{"华为技术有限\n公司是一家生产销售通信设备的民营通信科技公司,总部位于中国广东省深圳市龙岗区坂" +
                    "田华为基地。华为的产品主要涉及通信网络中的交换网络、传输网络、无线及有线固定接入网络和数据" +
                    "通信网络及无线终端产品,为世界各地通信运营商及专业网络拥有者提供硬件设备、软件、服务和解决" +
                    "方案。华为于1987年在中国深圳正式注册成立。华为的产品和解决方案已经应用于全球170多个国家," +
                    "服务全球运营商50强中的45家及全球1/3的人口。2014年《财富》世界500强中华为排行全球第285" +
                    "位,与上年相比上升三十位。2015年,评为新浪科技2014年度风云榜年度杰出企业。[1] 2016年,研" +
                    "究机构Millward Brown编制的BrandZ全球100个最具价值品牌排行榜中,华为从2015年的排名第70" +
                    "位上升到第50位。[2]"}));
            noteCell.setColspan(12);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
