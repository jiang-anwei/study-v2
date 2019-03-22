package com.icekredit.pdf.entities.histogram_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.chart.SuperChartCell;
import com.icekredit.pdf.entities.chart.part.ItemDescPart;
import com.icekredit.pdf.entities.chart.part.PillarsPart;
import com.icekredit.pdf.entities.chart.partdata.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by icekredit on 4/28/16.
 */
public class HistogramCell extends SuperChartCell {
    private static final int PROPER_PERCENTAGES_TO_SHOW_LEVEL = 10;

    public HistogramCell(String chartTitle, List<Map<String, Float>> groupsPartsData, List<BaseColor> partsColor,
                         List<String> groupsDesc, List<String> partsDesc, List<List<String>> pillarsPartsKey,String yAxisDesc) {
        this(chartTitle, groupsPartsData, partsColor, groupsDesc, partsDesc, pillarsPartsKey, yAxisDesc,Float.MAX_VALUE, -Float.MAX_VALUE);
    }

    public HistogramCell(String chartTitle, List<Map<String, Float>> groupsPartsData, List<BaseColor> partsColor,
                         List<String> groupsDesc, List<String> partsDesc, List<List<String>> pillarsPartsKey, String yAxisDesc,float min, float max) {

        List<BasePartData> basePartDataList = buildHistogramBasePartDatas(chartTitle, groupsPartsData,
                partsColor, groupsDesc, partsDesc, pillarsPartsKey, yAxisDesc,min, max);

        this.getBasePartDatas().addAll(basePartDataList);
    }

    public HistogramCell(String chartTitle, List<Map<String, Float>> groupsPartsData, List<BaseColor> partsColor,
                         List<String> groupsDesc,float scaleDescShownAngle, List<String> partsDesc, List<List<String>> pillarsPartsKey, String yAxisDesc,float min, float max) {

        List<BasePartData> basePartDataList = buildHistogramBasePartDatas(chartTitle, groupsPartsData,
                partsColor, groupsDesc,scaleDescShownAngle, partsDesc, pillarsPartsKey, yAxisDesc,min, max);

        this.getBasePartDatas().addAll(basePartDataList);
    }

    /**
     * 利用原始数据为柱状图构建子模块数据集合
     *
     * @param chartTitle
     * @param groupsPartsData
     * @param partsColor
     * @param groupsDesc
     * @param partsDesc
     * @param pillarsPartsKey
     * @param yAxisDesc
     *@param min
     * @param max   @return
     */
    public static List<BasePartData> buildHistogramBasePartDatas(
            String chartTitle, List<Map<String, Float>> groupsPartsData,
            List<BaseColor> partsColor, List<String> groupsDesc,
            List<String> partsDesc, List<List<String>> pillarsPartsKey, String yAxisDesc, float min, float max) {
        List<BasePartData> basePartDataList = new ArrayList<BasePartData>();

        TitlePartData titlePartData = new TitlePartData(chartTitle);

        BackgroundPartData backgroundPartData = new BackgroundPartData(10);

        XAxisPartData xAxisPartData = new XAxisPartData(
                0, 100, 0,
                groupsDesc);
        YAxisPartData yAxisPartData = new YAxisPartData(
                0, 100, 0,
                Arrays.asList(getProperPercentageToShow(groupsPartsData, pillarsPartsKey, PROPER_PERCENTAGES_TO_SHOW_LEVEL, min, max)),yAxisDesc);

        List<PillarsPart.PillarItemPart> pillarItemDatas = new ArrayList<PillarsPart.PillarItemPart>();

        PillarsPart.PillarItemPart pillarItemPart = null;
        for (Map<String, Float> groupPartsData : groupsPartsData) {

            int index = 0;
            for (List<String> pillarPartsKey : pillarsPartsKey) {

                int position = 0;
                for (String pillarPartKey : pillarPartsKey) {
                    if (index == 0 && position == 0) {
                        pillarItemPart = new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_GROUP, groupPartsData.get(pillarPartKey));
                    } else if (index != 0 && position == 0) {
                        pillarItemPart = new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_PILLAR, groupPartsData.get(pillarPartKey));
                    } else {
                        pillarItemPart = new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_CURRENT_PILLAR, groupPartsData.get(pillarPartKey));
                    }

                    pillarItemDatas.add(pillarItemPart);

                    position++;
                }

                index++;
            }
        }

        PillarsPartData pillarsPartData = new PillarsPartData(pillarItemDatas, partsColor);

        ArrayList<ItemDescPart.PartDesc> partDescs = new ArrayList<ItemDescPart.PartDesc>();
        ItemDescPart.PartDesc partDesc = null;
        int index = 0;
        for (String partDescStr : partsDesc) {
            partDesc = new ItemDescPart.PartDesc(ItemDescPart.PartDesc.PART_TYPE_PILLAR,
                    partsColor.get(index), partDescStr);

            partDescs.add(partDesc);

            index++;
        }
        ItemDescPartData itemDescPartData = new ItemDescPartData(partDescs);

        basePartDataList.add(titlePartData);
        basePartDataList.add(backgroundPartData);
        basePartDataList.add(xAxisPartData);
        basePartDataList.add(yAxisPartData);
        basePartDataList.add(pillarsPartData);
        basePartDataList.add(itemDescPartData);

        return basePartDataList;
    }

    /**
     * 利用原始数据为柱状图构建子模块数据集合
     *
     * @param chartTitle
     * @param groupsPartsData
     * @param partsColor
     * @param groupsDesc
     * @param partsDesc
     * @param pillarsPartsKey
     * @param yAxisDesc
     *@param min
     * @param max   @return
     */
    public static List<BasePartData> buildHistogramBasePartDatas(
            String chartTitle, List<Map<String, Float>> groupsPartsData,
            List<BaseColor> partsColor, List<String> groupsDesc,float xScaleDescShownAngle,
            List<String> partsDesc, List<List<String>> pillarsPartsKey, String yAxisDesc, float min, float max) {
        List<BasePartData> basePartDataList = new ArrayList<BasePartData>();

        TitlePartData titlePartData = new TitlePartData(chartTitle);

        BackgroundPartData backgroundPartData = new BackgroundPartData(10);

        XAxisPartData xAxisPartData = new XAxisPartData(
                0, 100, 0,
                groupsDesc,xScaleDescShownAngle);
        YAxisPartData yAxisPartData = new YAxisPartData(
                0, 100, 0,
                Arrays.asList(getProperPercentageToShow(groupsPartsData, pillarsPartsKey, PROPER_PERCENTAGES_TO_SHOW_LEVEL, min, max)),yAxisDesc);

        List<PillarsPart.PillarItemPart> pillarItemDatas = new ArrayList<PillarsPart.PillarItemPart>();

        PillarsPart.PillarItemPart pillarItemPart = null;
        for (Map<String, Float> groupPartsData : groupsPartsData) {

            int index = 0;
            for (List<String> pillarPartsKey : pillarsPartsKey) {

                int position = 0;
                for (String pillarPartKey : pillarPartsKey) {
                    if (index == 0 && position == 0) {
                        pillarItemPart = new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_GROUP, groupPartsData.get(pillarPartKey));
                    } else if (index != 0 && position == 0) {
                        pillarItemPart = new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_PILLAR, groupPartsData.get(pillarPartKey));
                    } else {
                        pillarItemPart = new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_CURRENT_PILLAR, groupPartsData.get(pillarPartKey));
                    }

                    pillarItemDatas.add(pillarItemPart);

                    position++;
                }

                index++;
            }
        }

        PillarsPartData pillarsPartData = new PillarsPartData(pillarItemDatas, partsColor);

        ArrayList<ItemDescPart.PartDesc> partDescs = new ArrayList<ItemDescPart.PartDesc>();
        ItemDescPart.PartDesc partDesc = null;
        int index = 0;
        for (String partDescStr : partsDesc) {
            partDesc = new ItemDescPart.PartDesc(ItemDescPart.PartDesc.PART_TYPE_PILLAR,
                    partsColor.get(index), partDescStr);

            partDescs.add(partDesc);

            index++;
        }
        ItemDescPartData itemDescPartData = new ItemDescPartData(partDescs);

        basePartDataList.add(titlePartData);
        basePartDataList.add(backgroundPartData);
        basePartDataList.add(xAxisPartData);
        basePartDataList.add(yAxisPartData);
        basePartDataList.add(pillarsPartData);
        basePartDataList.add(itemDescPartData);

        return basePartDataList;
    }

    protected static String[] getProperPercentageToShow(
            List<Map<String, Float>> groupsPartsData, List<List<String>> pillarsPartsKey,
            int properShownLevel, float min, float max) {
        String[] percentageDesc = new String[properShownLevel + 1];

        int maxPillarHeight = PillarsPart.getProperMaxPillarHeight(groupsPartsData, pillarsPartsKey, max);
        int minPillarHeight = PillarsPart.getProperMinPillarHeight(groupsPartsData, pillarsPartsKey, min);

        int step = (maxPillarHeight - minPillarHeight) / properShownLevel;

        for (int index = 0; index <= properShownLevel; index++) {
            percentageDesc[index] = getProperAmountLevel(minPillarHeight + index * step, String.valueOf(maxPillarHeight).length());
        }

        return percentageDesc;
    }

    protected static String getProperAmountLevel(int number, int length) {
        String str = String.valueOf(number);

        while (str.length() < length) {
            str = " " + str;
        }

        return str;
    }

    public static void main(String[] args) {
        try {
            String dest = "result/test.pdf";
            File destFile = new File(dest);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(2);
            mainFrame.setWidthPercentage(100);

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
            Map<String, Float> map = new HashMap<String, Float>();
            for (List<String> partsKey : pillarsPartsKey) {
                for (String partKey : partsKey) {
                    map.put(partKey, 10f);
                }
            }

            for (int index = 0; index < 5; index++) {
                groupsPartsData.add(map);
            }

            HistogramCell histogramCell = new HistogramCell("标题", groupsPartsData, partsColor,
                    groupsDesc, partsDesc, pillarsPartsKey,"y轴描述");
            histogramCell.setColspan(2);
            histogramCell.setFixedHeight(180);

            mainFrame.addCell(histogramCell);
            mainFrame.addCell(histogramCell);

            AlignCenterCell alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("sss")));
            alignCenterCell.setColspan(2);
            mainFrame.addCell(alignCenterCell);

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
