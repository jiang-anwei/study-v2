package com.icekredit.pdf.entities.composite_chart;

import java.io.File;
import java.io.FileOutputStream;

import java.util.*;

import com.icekredit.pdf.entities.EmptyCell;
import com.icekredit.pdf.entities.chart.SuperChartCell;
import com.icekredit.pdf.entities.chart.partdata.*;
import com.icekredit.pdf.entities.histogram_chart.HistogramCell;
import com.icekredit.pdf.entities.line_chart.LineChartCell;
import com.icekredit.pdf.utils.ColorUtil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;

/**
 *   复合图，比如线图和柱状图复合图
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class CompositeChartCell extends SuperChartCell {

    /**
     * FIELD_DESCRIPTION
     */
    private static final int PROPER_PERCENTAGES_TO_SHOW_LEVEL = 10;

    /**
     * FIELD_DESCRIPTION
     */
    protected String chartTitle;

    /**
     * FIELD_DESCRIPTION
     */
    protected float[][] pillarGroupsDatas;

    /**
     * FIELD_DESCRIPTION
     */
    protected BaseColor[] pillarsItemColors;

    /**
     * FIELD_DESCRIPTION
     */
    protected String[] pillarGroupsDesc;

    /**
     * FIELD_DESCRIPTION
     */
    protected String[] pillarItemsDesc;

    /**
     * FIELD_DESCRIPTION
     */
    protected BaseColor[] lineColors;

    /**
     * FIELD_DESCRIPTION
     */
    protected List<Map<String, Object>> datas;

    /**
     * FIELD_DESCRIPTION
     */
    protected List<String> dataKeys;

    /**
     * FIELD_DESCRIPTION
     */
    protected List<BaseColor> linesColor;

    /**
     * FIELD_DESCRIPTION
     */
    protected String[] properPercentagesToShow;

    /**
     * FIELD_DESCRIPTION
     */
    protected int properShownLevel;

    /**
     * FIELD_DESCRIPTION
     */
    protected String leftCoordinateDesc;

    /**
     * FIELD_DESCRIPTION
     */
    protected String rightCoordinateDesc;

    /**
     * CONSTRUCTOR_DESCRIPTION
     *
     * @param chartTitle         PARAM_DESC
     * @param groupsPartsData    PARAM_DESC
     * @param partsColor         PARAM_DESC
     * @param groupsDesc         PARAM_DESC
     * @param partsDesc          PARAM_DESC
     * @param pillarsPartsKey    PARAM_DESC
     * @param histogramYAxisDesc PARAM_DESC
     * @param datas              PARAM_DESC
     * @param nodeDescs          PARAM_DESC
     * @param dataKeys           PARAM_DESC
     * @param linesColor         PARAM_DESC
     * @param linesDesc          PARAM_DESC
     * @param lineYAxisDesc      PARAM_DESC
     * @param percentageDesc     PARAM_DESC
     */
    public CompositeChartCell(String chartTitle, List<Map<String, Float>> groupsPartsData, List<BaseColor> partsColor,
                              List<String> groupsDesc, List<String> partsDesc, List<List<String>> pillarsPartsKey,
                              String histogramYAxisDesc, List<Map<String, Object>> datas,
                              Map<String, Object> nodeDescs, List<String> dataKeys, List<BaseColor> linesColor,
                              List<String> linesDesc, String lineYAxisDesc, List<String> percentageDesc) {
        this(chartTitle,
                groupsPartsData,
                partsColor,
                groupsDesc,
                partsDesc,
                pillarsPartsKey,
                histogramYAxisDesc,
                Float.MAX_VALUE,
                -Float.MAX_VALUE,
                datas,
                nodeDescs,
                dataKeys,
                linesColor,
                linesDesc,
                lineYAxisDesc,
                Float.MAX_VALUE,
                -Float.MAX_VALUE,
                percentageDesc);
    }

    /**
     * CONSTRUCTOR_DESCRIPTION
     *
     * @param chartTitle              PARAM_DESC
     * @param pillarGroupsDatas       PARAM_DESC
     * @param pillarsItemColors       PARAM_DESC
     * @param pillarGroupsDesc        PARAM_DESC
     * @param pillarItemsDesc         PARAM_DESC
     * @param lineColors              PARAM_DESC
     * @param datas                   PARAM_DESC
     * @param dataKeys                PARAM_DESC
     * @param linesColor              PARAM_DESC
     * @param properPercentagesToShow PARAM_DESC
     * @param properShownLevel        PARAM_DESC
     * @param leftCoordinateDesc      PARAM_DESC
     * @param rightCoordinateDesc     PARAM_DESC
     * @param min                     PARAM_DESC
     * @param max                     PARAM_DESC
     */
    public CompositeChartCell(String chartTitle, float[][] pillarGroupsDatas, BaseColor[] pillarsItemColors,
                              String[] pillarGroupsDesc, String[] pillarItemsDesc, BaseColor[] lineColors,
                              List<Map<String, Object>> datas, List<String> dataKeys, List<BaseColor> linesColor,
                              String[] properPercentagesToShow, int properShownLevel, String leftCoordinateDesc,
                              String rightCoordinateDesc, float min, float max) {
        this.chartTitle = chartTitle;
        this.pillarGroupsDatas = pillarGroupsDatas;
        this.pillarsItemColors = pillarsItemColors;
        this.pillarGroupsDesc = pillarGroupsDesc;
        this.pillarItemsDesc = pillarItemsDesc;
        this.lineColors = lineColors;
        this.datas = datas;
        this.dataKeys = dataKeys;
        this.linesColor = linesColor;
        this.properPercentagesToShow = properPercentagesToShow;
        this.properShownLevel = properShownLevel;
        this.leftCoordinateDesc = leftCoordinateDesc;
        this.rightCoordinateDesc = rightCoordinateDesc;
        this.setFixedHeight(150);
        this.setCellEvent(this);
    }

    /**
     * CONSTRUCTOR_DESCRIPTION
     *
     * @param chartTitle         PARAM_DESC
     * @param groupsPartsData    PARAM_DESC
     * @param partsColor         PARAM_DESC
     * @param groupsDesc         PARAM_DESC
     * @param partsDesc          PARAM_DESC
     * @param pillarsPartsKey    PARAM_DESC
     * @param histogramYAxisDesc PARAM_DESC
     * @param histogramMin       PARAM_DESC
     * @param histogramMax       PARAM_DESC
     * @param datas              PARAM_DESC
     * @param nodeDescs          PARAM_DESC
     * @param dataKeys           PARAM_DESC
     * @param linesColor         PARAM_DESC
     * @param linesDesc          PARAM_DESC
     * @param lineYAxisDesc      PARAM_DESC
     * @param lineChartMin       PARAM_DESC
     * @param lineChartMax       PARAM_DESC
     * @param percentageDesc     PARAM_DESC
     */
    public CompositeChartCell(String chartTitle, List<Map<String, Float>> groupsPartsData, List<BaseColor> partsColor,
                              List<String> groupsDesc, List<String> partsDesc, List<List<String>> pillarsPartsKey,
                              String histogramYAxisDesc, float histogramMin, float histogramMax,
                              List<Map<String, Object>> datas, Map<String, Object> nodeDescs, List<String> dataKeys,
                              List<BaseColor> linesColor, List<String> linesDesc, String lineYAxisDesc,
                              float lineChartMin, float lineChartMax, List<String> percentageDesc) {
        List<BasePartData> histogramBasePartDataList = HistogramCell.buildHistogramBasePartDatas(chartTitle,
                groupsPartsData,
                partsColor,
                groupsDesc,
                partsDesc,
                pillarsPartsKey,
                histogramYAxisDesc,
                histogramMin,
                histogramMax);
        List<BasePartData> linesChartBasePartDataList = null;

        // 如果传递的参数中包括，y轴scale描述，那么不需要通过计算来得到刻度，否则需要计算，传递下来的参数一般都是百分比描述
        if ((percentageDesc == null) || (percentageDesc.size() == 0)) {
            linesChartBasePartDataList = LineChartCell.buildLinesChartBasePartDatas(chartTitle,
                    datas,
                    nodeDescs,
                    dataKeys,
                    linesColor,
                    linesDesc,
                    lineYAxisDesc,
                    lineChartMin,
                    lineChartMax,
                    null);
        } else {
            linesChartBasePartDataList = LineChartCell.buildLinesChartBasePartDatas(chartTitle,
                    datas,
                    nodeDescs,
                    dataKeys,
                    linesColor,
                    linesDesc,
                    lineYAxisDesc,
                    lineChartMin,
                    lineChartMax,
                    percentageDesc);
        }

        BasePartData basePartData = null;

        for (int index = 0; index < linesChartBasePartDataList.size(); index++) {
            basePartData = linesChartBasePartDataList.get(index);

            if (basePartData instanceof YAxisPartData) {
                YAxisPartData yAxisPartData = (YAxisPartData) basePartData;

                // 线图的y轴居右显示
                yAxisPartData.xAxisReferTo = yAxisPartData.xAxisMax;
            }

            if (basePartData instanceof XAxisPartData) {

                // 组合图中只需要一个x轴，线图的x轴去掉
                linesChartBasePartDataList.remove(index);
                index--;
            }

            if (basePartData instanceof BackgroundPartData) {
                linesChartBasePartDataList.remove(index);
                index--;
            }

            if (basePartData instanceof TitlePartData) {
                linesChartBasePartDataList.remove(index);
                index--;
            }

            if (basePartData instanceof ItemDescPartData) {
                boolean hasBeenTransferred = false;

                for (BasePartData histogramBasePartData : histogramBasePartDataList) {
                    if (histogramBasePartData instanceof ItemDescPartData) {
                        ItemDescPartData itemDescPartData = (ItemDescPartData) histogramBasePartData;

                        itemDescPartData.partsDescList.addAll(((ItemDescPartData) basePartData).partsDescList);
                        hasBeenTransferred = true;
                    }
                }

                if (hasBeenTransferred) {
                    linesChartBasePartDataList.remove(index);
                    index--;
                }
            }
        }

        this.getBasePartDatas().addAll(histogramBasePartDataList);
        this.getBasePartDatas().addAll(linesChartBasePartDataList);
    }

    public static void main(String[] args) {
        try {
            String dest = "result/test.pdf";
            File destFile = new File(dest);

            destFile.getParentFile().mkdirs();

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

            List<List<String>> pillarsPartsKey = new ArrayList<List<String>>();
            List<String> pillarPartsKey = new ArrayList<String>();

            pillarPartsKey.add("key1");
            pillarPartsKey.add("key2");
            pillarsPartsKey.add(pillarPartsKey);
            pillarPartsKey = new ArrayList<String>();
            pillarPartsKey.add("key3");
            pillarPartsKey.add("key4");
            pillarsPartsKey.add(pillarPartsKey);
            pillarPartsKey = new ArrayList<String>();
            pillarPartsKey.add("key5");
            pillarsPartsKey.add(pillarPartsKey);

            List<BaseColor> partsColor = new ArrayList<BaseColor>();

            partsColor.add(ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[0]));
            partsColor.add(ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[1]));
            partsColor.add(ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[2]));
            partsColor.add(ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[3]));
            partsColor.add(ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[4]));

            List<String> groupsDesc = new ArrayList<String>();

            groupsDesc.add("第一组");
            groupsDesc.add("第二组");
            groupsDesc.add("第三组");
            groupsDesc.add("第四组");
            groupsDesc.add("第五组");

            List<String> partsDesc = new ArrayList<String>();

            partsDesc.add("第一部分");
            partsDesc.add("第二部分");
            partsDesc.add("第三部分");
            partsDesc.add("第四部分");
            partsDesc.add("第五部分");

            List<Map<String, Float>> groupsPartsData = new ArrayList<Map<String, Float>>();
            Map<String, Float> map1 = new HashMap<String, Float>();

            for (List<String> partsKey : pillarsPartsKey) {
                for (String partKey : partsKey) {
                    map1.put(partKey, 10f);
                }
            }

            for (int index = 0; index < 5; index++) {
                groupsPartsData.add(map1);
            }

            CompositeChartCell compositeChartCell = new CompositeChartCell("标题",
                    groupsPartsData,
                    partsColor,
                    groupsDesc,
                    partsDesc,
                    pillarsPartsKey,
                    "柱状图",
                    datas,
                    dataDesc,
                    dataKeys,
                    linesColor,
                    linesDesc,
                    "线图",
                    Arrays.asList("100%",
                            "90%",
                            "80%",
                            "70%",
                            "60%",
                            "50%",
                            "40%",
                            "30%",
                            "20%",
                            "10%",
                            "0%"));
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);

            mainFrame.setWidthPercentage(100);
            compositeChartCell.setColspan(8);
            mainFrame.addCell(new EmptyCell(2));
            mainFrame.addCell(compositeChartCell);
            mainFrame.addCell(new EmptyCell(2));
            document.add(mainFrame);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
