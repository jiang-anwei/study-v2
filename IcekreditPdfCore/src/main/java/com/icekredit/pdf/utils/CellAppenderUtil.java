package com.icekredit.pdf.utils;

import java.util.List;

import com.icekredit.pdf.entities.*;
import com.icekredit.pdf.entities.core.MessageConfig;
import com.icekredit.pdf.entities.pie_chart.SectorDescChartCell;
import com.icekredit.pdf.entities.pie_chart.SuperPieChartCell;
import com.icekredit.pdf.entities.table.TableHeaderCell;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 向页面主框架中append单元格、表格的工具类
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class CellAppenderUtil {

    /**
     * 数据表格使用的字体大小
     */
    private static final int FONT_SIZE = 8;

    /**
     * 向主框架中添加键值对单元格
     *
     * @param mainFrame 主框架
     * @param names     键值对键名称
     * @param values    键值对键值
     */
    public static void appendKeyValueCells(PdfPTable mainFrame, List<String> names, List<String> values) {
        try {
            int index = 0;
            KeyValueCell itemCell = null;

            for (String name : names) {
                if ((name == null) || name.trim().equals("")) {
                    mainFrame.addCell(new EmptyCell(6));
                    index++;

                    continue;
                }

                itemCell = new KeyValueCell(name,
                        values.get(index),
                        "",
                        "",
                        (index % 2) * 6 + 1,
                        6,
                        FontUtils.baseFontChinese.getWidthPoint(name + "  ", FONT_SIZE));
                itemCell.setColspan(6);
                itemCell.setFontSize(FONT_SIZE);
                mainFrame.addCell(itemCell);
                index++;
            }

            // 对齐
            while (index % 2 != 0) {
                mainFrame.addCell(new EmptyCell(6));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照指定列数向主框架中添加键值对单元格，列数只能是2/3/4，因为主框架表格类书为12列
     *
     * @param mainFrame   主框架
     * @param names       键值对键名称
     * @param values      键值对键值
     * @param columnCount 列数
     */
    public static void appendKeyValueCells(PdfPTable mainFrame, List<String> names, List<String> values,
                                           int columnCount) {
        try {
            if ((columnCount != 2) && (columnCount != 3) && (columnCount != 4)) {
                throw new Exception("Invalid Column count for CellAppendUtil");
            }

            int index = 0;
            KeyValueCell itemCell = null;

            for (String name : names) {
                if ((name == null) || name.trim().equals("")) {
                    mainFrame.addCell(new EmptyCell(12 / columnCount));
                    index++;

                    continue;
                }

                itemCell = new KeyValueCell(name,
                        values.get(index),
                        "",
                        "",
                        (index % columnCount) * 12 / columnCount + 1,
                        12 / columnCount,
                        FontUtils.baseFontChinese.getWidthPoint(name + "  ", FONT_SIZE));
                itemCell.setColspan(12 / columnCount);
                itemCell.setFontSize(FONT_SIZE);
                mainFrame.addCell(itemCell);
                index++;
            }

            // 对齐
            while (index % columnCount != 0) {
                mainFrame.addCell(new EmptyCell(12 / columnCount));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 过时的，不赞成使用此方法，建议使用PartDescItem来构建线段/圆柱/扇形等的描述信息
     *
     * @param pdfWriter  Deprecated
     * @param mainFrame  Deprecated
     * @param descs      Deprecated
     * @param descColors Deprecated
     */
    @Deprecated
    public static void appendPillarAndLineDescCell(PdfWriter pdfWriter, PdfPTable mainFrame, List<String> descs,
                                                   List<BaseColor> descColors) {
        int index = 0;
        SectorDescChartCell sectorDescChartCell = null;

        for (String desc : descs) {
            if (index % 4 == 0) {
                mainFrame.addCell(new EmptyCell(2));
            }

            sectorDescChartCell = new SectorDescChartCell(pdfWriter,
                    desc,
                    descColors.get(index),
                    SuperPieChartCell.IDENTIFIER_SHOWN_AS_CIRCLE);
            sectorDescChartCell.setColspan(2);
            mainFrame.addCell(sectorDescChartCell);

            if (index % 4 == 3) {
                mainFrame.addCell(new EmptyCell(2));
            }

            index++;
        }

        while (index % 4 != 0) {
            mainFrame.addCell(new EmptyCell(2));
            index++;
        }

        mainFrame.addCell(new EmptyCell(2));
    }

    /**
     * 向主框架中添加一个嵌套（模拟）的表格，此表格只能是2/3/4列，因为主框架是12列
     *
     * @param mainFrame   主框架
     * @param headers     表格标题
     * @param rowsDatas   表格行数据
     * @param columnCount 列数
     */
    public static void appendTable(PdfPTable mainFrame, List<String> headers, List<List<String>> rowsDatas,
                                   int columnCount) {
        if ((columnCount != 4) && (columnCount != 3) && (columnCount != 2)) {
            return;
        }

        try {
            TableHeaderCell tableHeaderCell = null;

            for (String header : headers) {
                tableHeaderCell = new TableHeaderCell(header,
                        ColorUtil.strRGBAToColor("0x9fa0a0ff"),
                        MessageConfig.IDENTIFIER_TYPE_NONE);
                tableHeaderCell.setColspan(12 / columnCount);
                mainFrame.addCell(tableHeaderCell);
            }

            KeyValueCell itemCell = null;
            int index = 0;

            for (List<String> rowDatas : rowsDatas) {
                index = 0;

                for (String data : rowDatas) {
                    itemCell = new KeyValueCell(data,
                            "",
                            "",
                            "",
                            (index % columnCount) * (12 / columnCount) + 1,
                            12 / columnCount,
                            FontUtils.baseFontChinese.getWidthPoint(data + "  ", FONT_SIZE));
                    itemCell.setColspan(12 / columnCount);
                    itemCell.setFontSize(FONT_SIZE);
                    mainFrame.addCell(itemCell);
                    index++;
                }
            }

            // 对齐
            while (index % columnCount != 0) {
                mainFrame.addCell(new EmptyCell(12 / columnCount));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加top10主叫城市信息
     *
     * @param mainFrame        主框架
     * @param header           表格大标题
     * @param threeMonthCities 最近三月主叫城市
     * @param sixMonthCities   最近六月主叫城市
     */
    public static void appendTop10CallCitiesInfo(PdfPTable mainFrame, String header, List<String> threeMonthCities,
                                                 List<String> sixMonthCities) {
        int threeMonthRowCount = (threeMonthCities.size() + 1) / 2;
        int sixMonthRowCount = (sixMonthCities.size() + 1) / 2;
        int maxRowCount = Math.max(threeMonthRowCount, sixMonthRowCount);
        KeyValueCell keyValueCell = null;

        for (int index = 0; index < maxRowCount; index++) {
            if (index == 0) {
                keyValueCell = new KeyValueCell(header,
                        "",
                        "",
                        "",
                        1,
                        4,
                        FontUtils.baseFontChinese.getWidthPoint(header, FONT_SIZE));
                keyValueCell.setColspan(4);
                keyValueCell.setFontSize(FONT_SIZE);
                mainFrame.addCell(keyValueCell);
            } else {
                mainFrame.addCell(new EmptyCell(4));
            }

            if (index < threeMonthRowCount) {
                keyValueCell = new KeyValueCell(getCurrentRowCities(threeMonthCities, index),
                        "",
                        "",
                        "",
                        5,
                        4,
                        FontUtils.baseFontChinese.getWidthPoint("", FONT_SIZE));
                keyValueCell.setColspan(4);
                keyValueCell.setFontSize(FONT_SIZE);
                mainFrame.addCell(keyValueCell);
            } else {
                mainFrame.addCell(new EmptyCell(4));
            }

            if (index < sixMonthRowCount) {
                keyValueCell = new KeyValueCell(getCurrentRowCities(sixMonthCities, index),
                        "",
                        "",
                        "",
                        5,
                        4,
                        FontUtils.baseFontChinese.getWidthPoint("", FONT_SIZE));
                keyValueCell.setColspan(4);
                keyValueCell.setFontSize(FONT_SIZE);
                mainFrame.addCell(keyValueCell);
            } else {
                mainFrame.addCell(new EmptyCell(4));
            }
        }
    }

    /**
     * 以指定列数想主框架中添加一个表格，表格列数随意
     *
     * @param mainFrame   主框架
     * @param headers     表格header
     * @param rowsDatas   表格行数据
     * @param columnCount 表格列数
     */
    public static void appendWrapperTable(PdfPTable mainFrame, List<String> headers, List<List<String>> rowsDatas,
                                          int columnCount) {
        PdfPTable wrapperTable = new PdfPTable(columnCount);

        wrapperTable.setWidthPercentage(100);

        try {
            TableHeaderCell tableHeaderCell = null;

            for (String header : headers) {
                tableHeaderCell = new TableHeaderCell(header,
                        ColorUtil.strRGBAToColor("0x9fa0a0ff"),
                        MessageConfig.IDENTIFIER_TYPE_NONE);
                tableHeaderCell.setColspan(1);
                wrapperTable.addCell(tableHeaderCell);
            }

            KeyValueCell itemCell = null;
            int index = 0;

            for (List<String> rowDatas : rowsDatas) {
                index = 0;

                for (String data : rowDatas) {
                    itemCell = new KeyValueCell(data,
                            "",
                            "",
                            "",
                            index + 1,
                            1,
                            FontUtils.baseFontChinese.getWidthPoint(data + "  ", FONT_SIZE));
                    itemCell.setColspan(1);
                    itemCell.setFontSize(FONT_SIZE);
                    wrapperTable.addCell(itemCell);
                    index++;
                }
            }

            // 对齐
            while (index % columnCount != 0) {
                wrapperTable.addCell(new EmptyCell(12 / columnCount));
                index++;
            }

            AlignCenterCell wrapperCell = new AlignCenterCell();

            wrapperCell.setColspan(12);
            wrapperCell.addElement(wrapperTable);
            mainFrame.addCell(wrapperCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以指定列数指定分割规则向单元格中添加一个嵌套的表格
     *
     * @param mainFrame   主框架
     * @param headers     表格headers
     * @param rowsDatas   表格行数据
     * @param columnCount 表格列数
     * @param columnRules 表格列数分配规则
     */
    public static void appendWrapperTable(PdfPTable mainFrame, List<String> headers, List<List<String>> rowsDatas,
                                          int columnCount, int[] columnRules) {
        PdfPTable wrapperTable = new PdfPTable(columnCount);

        wrapperTable.setWidthPercentage(100);

        try {
            TableHeaderCell tableHeaderCell = null;
            int index = 0;

            for (String header : headers) {
                tableHeaderCell = new TableHeaderCell(header,
                        ColorUtil.strRGBAToColor("0x9fa0a0ff"),
                        MessageConfig.IDENTIFIER_TYPE_NONE);
                tableHeaderCell.setColspan(columnRules[index]);
                wrapperTable.addCell(tableHeaderCell);
                index++;
            }

            KeyValueCell itemCell = null;

            for (List<String> rowDatas : rowsDatas) {
                index = 0;

                int columnUsed = 0;

                for (String data : rowDatas) {
                    itemCell = new KeyValueCell(data,
                            "",
                            "",
                            "",
                            columnUsed + 1,
                            columnRules[index],
                            FontUtils.baseFontChinese.getWidthPoint(data + "  ", FONT_SIZE));
                    itemCell.setColspan(columnRules[index]);
                    itemCell.setFontSize(FONT_SIZE);
                    wrapperTable.addCell(itemCell);
                    columnUsed += columnRules[index];
                    index++;
                }
            }

            // 对齐
            while (index % columnCount != 0) {
                wrapperTable.addCell(new EmptyCell(12 / columnCount));
                index++;
            }

            AlignCenterCell wrapperCell = new AlignCenterCell();

            wrapperCell.setColspan(12);
            wrapperCell.addElement(wrapperTable);
            mainFrame.addCell(wrapperCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得当前行的两个城市信息
     *
     * @param cities 城市集合
     * @param index  行索引
     * @return 当前行的两个城市信息
     */
    private static String getCurrentRowCities(List<String> cities, int index) {
        StringBuilder builder = new StringBuilder();

        if (index * 2 + 0 < cities.size()) {
            builder.append(cities.get(index * 2 + 0));
        }

        if (index * 2 + 1 < cities.size()) {
            while (true) {
                if (builder.length() < 10) {
                    builder.append(" ");
                } else {
                    break;
                }
            }

            builder.append(cities.get(index * 2 + 1));
        }

        return builder.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
