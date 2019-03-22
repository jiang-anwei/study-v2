package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.entities.line_chart.Line;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icekredit on 9/26/16.
 */
public class PropertiesCell extends BaseCell implements PdfPCellEvent {
    protected List<Map<String,Object>> properties;

    public static final String PROPERTY_NAME = "property_name";
    public static final String PROPERTY_VALUE = "property_value";
    public static final String PROPERTY_NAME_FONT = "property_name_font";
    public static final String PROPERTY_VALUE_FONT = "property_value_font";

    protected int rowCount;
    protected static final float DEFAULT_CELL_HEIGHT = 20;

    protected static final float DEFAULT_MARGIN = 4;
    protected static final float DEFAULT_PADDING = 5;

    protected static final BaseColor DEFAULT_CELL_BACKGROUND_COLOR = ColorUtil.strRGBAToColor("0xf8f8f8fff");

    public static final Font DEFAULT_PROPERTY_NAME_FONT = new Font(FontUtils.baseFontChinese,FontUtils.fontSize,Font.NORMAL, ColorUtil.strRGBAToColor("0xbfbfbfff"));
    public static final Font DEFAULT_PROPERTY_VALUE_FONT = new Font(FontUtils.baseFontChinese,FontUtils.fontSize,Font.NORMAL, ColorUtil.strRGBAToColor(ColorUtil.WARNING_DESC_COLOR));


    public PropertiesCell(List<Map<String, Object>> properties, int rowCount) {
        this.properties = properties;
        this.rowCount = rowCount;

        this.setFixedHeight(rowCount * DEFAULT_CELL_HEIGHT + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            String propertyName = null;
            String propertyValue = null;
            Font propertyNameFont = null;
            Font propertyValueFont = null;

            int currentRowDrawn = 0;
            for(Map<String,Object> propertyMap:properties){
                propertyName = (String) propertyMap.get(PROPERTY_NAME);
                propertyValue = (String) propertyMap.get(PROPERTY_VALUE);
                propertyNameFont = (Font) propertyMap.get(PROPERTY_NAME_FONT);
                propertyValueFont = (Font) propertyMap.get(PROPERTY_VALUE_FONT);

                CellChartConfig cellChartConfig = CellChartConfig.newInstance();
                cellChartConfig.setBackgroundColor(DEFAULT_CELL_BACKGROUND_COLOR);
                cellChartConfig.setBorderColor(DEFAULT_CELL_BACKGROUND_COLOR);
                cellChartConfig.setLLX(position.getLeft() + DEFAULT_MARGIN);
                cellChartConfig.setURX(position.getRight() - DEFAULT_MARGIN);
                cellChartConfig.setLLY(position.getTop() - DEFAULT_CELL_HEIGHT * (currentRowDrawn + 1) - DEFAULT_MARGIN);
                cellChartConfig.setURY(position.getTop() - DEFAULT_CELL_HEIGHT * currentRowDrawn - DEFAULT_MARGIN);


                MessageConfig propertyNameMessage = new MessageConfig(cellChartConfig);
                propertyNameMessage.setMessage(propertyName);
                propertyNameMessage.setFontSize((int) propertyNameFont.getSize());
                propertyNameMessage.setMarginTop(DEFAULT_PADDING);
                propertyNameMessage.setMarginLeft(DEFAULT_PADDING);
                propertyNameMessage.setFontStyle(propertyNameFont.getStyle());
                propertyNameMessage.setFontColor(propertyNameFont.getColor());
                propertyNameMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
                propertyNameMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);


                MessageConfig propertyValueMessage = new MessageConfig(cellChartConfig);
                propertyValueMessage.setMessage(propertyValue);
                propertyValueMessage.setFontSize((int) propertyValueFont.getSize());
                propertyValueMessage.setMarginRight(DEFAULT_PADDING);
                propertyValueMessage.setMarginBottom(DEFAULT_PADDING);
                propertyValueMessage.setFontStyle(propertyValueFont.getStyle());
                propertyValueMessage.setFontColor(propertyValueFont.getColor());
                propertyValueMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
                propertyValueMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

                CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);
                cellChart.fragments.add(new Fragment(propertyNameMessage));
                cellChart.fragments.add(new Fragment(propertyValueMessage));

                cellChart.draw(canvases[PdfPTable.BASECANVAS]);

                if(currentRowDrawn == 0){
                    Line line = new Line(canvases[PdfPTable.LINECANVAS],
                            new Point[]{
                                    new Point(position.getLeft() + DEFAULT_MARGIN + 5,position.getTop() - DEFAULT_CELL_HEIGHT * (currentRowDrawn + 1) - DEFAULT_MARGIN),
                                    new Point(position.getRight() - DEFAULT_MARGIN - 5,position.getTop() - DEFAULT_CELL_HEIGHT * (currentRowDrawn + 1) - DEFAULT_MARGIN)
                            },10,0,0.5f,
                            ColorUtil.strRGBAToColor("0xcfcfcfff"),false,0);
                    line.draw();
                }

                currentRowDrawn ++;
            }

            while (currentRowDrawn < rowCount){
                CellChartConfig cellChartConfig = CellChartConfig.newInstance();
                cellChartConfig.setBackgroundColor(DEFAULT_CELL_BACKGROUND_COLOR);
                cellChartConfig.setBorderColor(DEFAULT_CELL_BACKGROUND_COLOR);
                cellChartConfig.setLLX(position.getLeft() + DEFAULT_MARGIN);
                cellChartConfig.setURX(position.getRight() - DEFAULT_MARGIN);
                cellChartConfig.setLLY(position.getTop() - DEFAULT_CELL_HEIGHT * (currentRowDrawn + 1) - DEFAULT_MARGIN);
                cellChartConfig.setURY(position.getTop() - DEFAULT_CELL_HEIGHT * currentRowDrawn - DEFAULT_MARGIN);

                CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);

                cellChart.draw(canvases[PdfPTable.BASECANVAS]);

                currentRowDrawn ++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            Map<String,Object> map = new HashMap<String, Object>();
            map.put(PropertiesCell.PROPERTY_NAME,"实名认证");
            map.put(PropertiesCell.PROPERTY_VALUE,"不匹配");
            map.put(PropertiesCell.PROPERTY_NAME_FONT,DEFAULT_PROPERTY_NAME_FONT);
            map.put(PROPERTY_VALUE_FONT,DEFAULT_PROPERTY_VALUE_FONT);

            List<Map<String,Object>> properties = new ArrayList<Map<String,Object>>();
            properties.add(map);
            properties.add(map);
            properties.add(map);

            PropertiesCell propertiesCell = new PropertiesCell(properties,3);
            propertiesCell.setColspan(6);
            mainFrame.addCell(propertiesCell);
            mainFrame.addCell(propertiesCell);


            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
