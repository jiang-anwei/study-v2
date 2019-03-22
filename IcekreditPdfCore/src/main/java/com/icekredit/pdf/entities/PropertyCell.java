package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 9/26/16.
 */
public class PropertyCell extends BaseCell implements PdfPCellEvent {
    protected String propertyName;
    protected String propertyValue;
    protected Font propertyNameFont;
    protected Font propertyValueFont;

    protected static final float DEFAULT_CELL_HEIGHT = 60;
    protected static final float DEFAULT_MARGIN = 4;
    protected static final float DEFAULT_PADDING = 5;

    protected static final BaseColor DEFAULT_CELL_BACKGROUND_COLOR = ColorUtil.strRGBAToColor("0xf8f8f8fff");

    protected static final Font PROPERTY_NAME_FONT = new Font(FontUtils.baseFontChinese,FontUtils.fontSize,Font.NORMAL, ColorUtil.strRGBAToColor("0xbfbfbfff"));
    protected static final Font PROPERTY_VALUE_FONT = new Font(FontUtils.baseFontChinese,24,Font.NORMAL, ColorUtil.strRGBAToColor(ColorUtil.WARNING_DESC_COLOR));

    public PropertyCell(String propertyName, String propertyValue){
        this(propertyName, propertyValue,PROPERTY_VALUE_FONT);
    }

    public PropertyCell(String propertyName, String propertyValue,Font propertyValueFont) {
        this(propertyName,propertyValue,PROPERTY_NAME_FONT,propertyValueFont);
    }

    public PropertyCell(String propertyName, String propertyValue, Font propertyNameFont, Font propertyValueFont) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.propertyNameFont = propertyNameFont;
        this.propertyValueFont = propertyValueFont;

        this.setFixedHeight(DEFAULT_CELL_HEIGHT + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setBackgroundColor(DEFAULT_CELL_BACKGROUND_COLOR);
            cellChartConfig.setLLX(position.getLeft() + DEFAULT_MARGIN);
            cellChartConfig.setLLY(position.getBottom() + DEFAULT_MARGIN);
            cellChartConfig.setURX(position.getRight() - DEFAULT_MARGIN);
            cellChartConfig.setURY(position.getTop() - DEFAULT_MARGIN);

            MessageConfig propertyNameMessage = new MessageConfig(cellChartConfig);
            propertyNameMessage.setMessage(propertyName);
            propertyNameMessage.setFontSize((int) propertyNameFont.getSize());
            propertyNameMessage.setMarginTop(DEFAULT_PADDING);
            propertyNameMessage.setMarginLeft(DEFAULT_PADDING);
            propertyNameMessage.setFontStyle(propertyNameFont.getStyle());
            propertyNameMessage.setFontColor(propertyNameFont.getColor());
            propertyNameMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
            propertyNameMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_TOP);


            MessageConfig propertyValueMessage = new MessageConfig(cellChartConfig);
            propertyValueMessage.setMessage(propertyValue);
            propertyValueMessage.setFontSize((int) propertyValueFont.getSize());
            propertyValueMessage.setMarginRight(DEFAULT_PADDING);
            propertyValueMessage.setMarginBottom(DEFAULT_PADDING);
            propertyValueMessage.setFontStyle(propertyValueFont.getStyle());
            propertyValueMessage.setFontColor(propertyValueFont.getColor());
            propertyValueMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
            propertyValueMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_BOTTOM);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);
            cellChart.fragments.add(new Fragment(propertyNameMessage));
            cellChart.fragments.add(new Fragment(propertyValueMessage));

            cellChart.draw(canvases[PdfPTable.BASECANVAS]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            String destFileStr = "results/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            PropertyCell propertyCell = new PropertyCell("典型的欺诈行为","85%");
            propertyCell.setColspan(6);
            mainFrame.addCell(propertyCell);
            mainFrame.addCell(new EmptyCell(6));

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
