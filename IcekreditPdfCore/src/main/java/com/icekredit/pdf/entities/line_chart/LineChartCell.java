package com.icekredit.pdf.entities.line_chart;

import com.icekredit.pdf.entities.chart.SuperChartCell;
import com.icekredit.pdf.entities.chart.part.ItemDescPart;
import com.icekredit.pdf.entities.chart.part.LinesPart;
import com.icekredit.pdf.entities.chart.partdata.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.PdfConvertUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by icekredit on 6/3/16.
 */
public class LineChartCell extends SuperChartCell {
    private static final int PROPER_PERCENTAGES_TO_SHOW_LEVEL = 10;

    public LineChartCell(String chartTitle, List<Map<String, Object>> datas, Map<String, Object> nodeDescs,
                         List<String> dataKeys, List<BaseColor> linesColor, List<String> linesDesc,String yAxisDesc) {
        this(chartTitle, datas, nodeDescs, dataKeys, linesColor, linesDesc,yAxisDesc, Float.MAX_VALUE, -Float.MAX_VALUE);
    }


    public LineChartCell(String chartTitle, List<Map<String, Object>> datas, Map<String, Object> nodeDescs,
                         List<String> dataKeys, List<BaseColor> linesColor, List<String> linesDesc,String yAxisDesc, float min, float max) {
        this.setFixedHeight(200);

        List<BasePartData> basePartDataList = buildLinesChartBasePartDatas(chartTitle, datas, nodeDescs, dataKeys, linesColor, linesDesc, yAxisDesc, min, max, null);

        this.getBasePartDatas().addAll(basePartDataList);
    }

    public LineChartCell(String chartTitle, List<Map<String, Object>> datas, Map<String, Object> nodeDescs,
                         List<String> dataKeys, List<BaseColor> linesColor, List<String> linesDesc, List<String> percentageDesc,String yAxisDesc) {
        this.setFixedHeight(200);

        List<BasePartData> basePartDataList = buildLinesChartBasePartDatas(chartTitle, datas, nodeDescs, dataKeys, linesColor, linesDesc, yAxisDesc,0, 100,percentageDesc);

        this.getBasePartDatas().addAll(basePartDataList);
    }

    /**
     * 利用原始数据为线图构建子模块数据集合
     *
     * @param chartTitle
     * @param datas
     * @param nodeDescs
     * @param dataKeys
     * @param linesColor
     * @param linesDesc
     * @param yAxisDesc
     *@param min
     * @param max
     * @param percentageDesc    @return
     */
    public static List<BasePartData> buildLinesChartBasePartDatas(
            String chartTitle, List<Map<String, Object>> datas, Map<String, Object> nodeDescs, List<String> dataKeys, List<BaseColor> linesColor, List<String> linesDesc, String yAxisDesc, float min, float max, List<String> percentageDesc) {
        List<BasePartData> basePartDataList = new ArrayList<BasePartData>();

        TitlePartData titlePartData = new TitlePartData(chartTitle);

        BackgroundPartData backgroundPartData = new BackgroundPartData(10);

        XAxisPartData xAxisPartData = new XAxisPartData(
                0, 100, 0,
                PdfConvertUtil.mapToList(nodeDescs, dataKeys));

        YAxisPartData yAxisPartData = null;

        //如果传递的参数中包括，y轴scale描述，那么不需要通过计算来得到刻度，否则需要计算，传递下来的参数一般都是百分比描述
        if(percentageDesc == null || percentageDesc.size() == 0){
            yAxisPartData = new YAxisPartData(
                    0, 100, 0,
                    Arrays.asList(getProperPercentageToShow(datas, dataKeys, min, max)),yAxisDesc);
        }else {
            yAxisPartData = new YAxisPartData(
                    0, 100, 0,
                    percentageDesc,yAxisDesc);
        }

        LinesPartData linesPartData = new LinesPartData(datas, dataKeys, linesColor, min, max);

        ArrayList<ItemDescPart.PartDesc> partDescs = new ArrayList<ItemDescPart.PartDesc>();

        ItemDescPart.PartDesc partDesc = null;
        int index = 0;
        for (String lineDesc : linesDesc) {
            partDesc = new ItemDescPart.PartDesc(ItemDescPart.PartDesc.PART_TYPE_LINE, ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]), lineDesc);

            partDescs.add(partDesc);
            index++;
        }

        ItemDescPartData itemDescPartData = new ItemDescPartData(partDescs);

        basePartDataList.add(titlePartData);
        basePartDataList.add(backgroundPartData);
        basePartDataList.add(xAxisPartData);
        basePartDataList.add(yAxisPartData);
        basePartDataList.add(linesPartData);
        basePartDataList.add(itemDescPartData);
        return basePartDataList;
    }

    /**
     * 根据数据的范围得到合适的展示区域
     *
     * @param datas
     * @param dataKeys
     * @param min
     * @param max
     * @return
     */
    private static String[] getProperPercentageToShow(List<Map<String, Object>> datas, List<String> dataKeys, float min, float max) {
        String[] properPercentagesToShow = new String[PROPER_PERCENTAGES_TO_SHOW_LEVEL + 1];

        float percentageToShow = LinesPart.getMaxPercentageToShow(datas, dataKeys, max)
                - LinesPart.getMinPercentageToShow(datas, dataKeys, min);

        float percentageShowStep = percentageToShow / PROPER_PERCENTAGES_TO_SHOW_LEVEL;

        float currentShowPercentage = LinesPart.getMinPercentageToShow(datas, dataKeys, min);
        for (int index = 0; index < properPercentagesToShow.length; index++) {

            String currentShowPercentageStr = null;
            if(((int)currentShowPercentage) == currentShowPercentage){
                currentShowPercentageStr = String.valueOf((int)currentShowPercentage);
            } else {
                currentShowPercentageStr = String.valueOf(currentShowPercentage);
            }



            properPercentagesToShow[index] = currentShowPercentageStr.length() < 3 ? " " + currentShowPercentageStr : currentShowPercentageStr.substring(0,3);

            currentShowPercentage += percentageShowStep;
        }

        return properPercentagesToShow;
    }

    public static void main(String[] args) {
        try {
            String destStr = "results/object/test.pdf";
            File destFile = new File(destStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            List<String> dataKeys = new ArrayList<String>();
            dataKeys.add("key1");
            dataKeys.add("key2");
            dataKeys.add("key3");
            dataKeys.add("key4");
            dataKeys.add("key5");
            dataKeys.add("key6");
            dataKeys.add("key7");
            dataKeys.add("key8");
            dataKeys.add("key9");


            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(dataKeys.get(0), 30f);
            map.put(dataKeys.get(1), 23f);
            map.put(dataKeys.get(2), 45f);
            map.put(dataKeys.get(3), 45f);
            map.put(dataKeys.get(4), 45f);
            map.put(dataKeys.get(5), 45f);
            map.put(dataKeys.get(6), 45f);
            map.put(dataKeys.get(7), 45f);
            map.put(dataKeys.get(8), 45f);

            datas.add(map);

            Map<String, Object> dataDesc = new HashMap<String, Object>();
            dataDesc.put(dataKeys.get(0), "2011");
            dataDesc.put(dataKeys.get(1), "属性二");
            dataDesc.put(dataKeys.get(2), "属性三");
            dataDesc.put(dataKeys.get(3), "属性三");
            dataDesc.put(dataKeys.get(4), "属性三");
            dataDesc.put(dataKeys.get(5), "属性三");
            dataDesc.put(dataKeys.get(6), "属性三");
            dataDesc.put(dataKeys.get(7), "属性三");
            dataDesc.put(dataKeys.get(8), "属性三");

            List<BaseColor> linesColor = new ArrayList<BaseColor>();
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));
            linesColor.add(new BaseColor(0xff, 0xff, 0x00, 0xff));

            List<String> linesDesc = new ArrayList<String>();
            linesDesc.add("line one");
            linesDesc.add("line two");

            LineChartCell lineChartCell = new LineChartCell(
                    "Test Title", datas, dataDesc,
                    dataKeys, linesColor, linesDesc,"y轴描述");
            lineChartCell.setColspan(12);
            lineChartCell.setFixedHeight(200);

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            mainFrame.addCell(lineChartCell);

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
