package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.LinesPartData;
import com.icekredit.pdf.entities.line_chart.Line;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.PdfConvertUtil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 折线图图表模块
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class LinesPart extends BasePart {

    /**
     * 构造函数
     */
    public LinesPart() {
    }

    /**
     * 提供了一个与当前折线图图表模块关联的折线图图表模块数据对象的构造函数
     *
     * @param basePartData 折线图图表模块数据对象
     */
    public LinesPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     * @param llx          折线图图表模块左下角x位置
     * @param lly          折线图图表模块左下角y位置
     * @param partWidth    折线图图表模块图表宽度
     * @param partHeight   折线图图表模块图表高度
     * @param basePartData 折线图图表模块数据对象
     */
    public LinesPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     * @param llx          折线图图表模块左下角x位置
     * @param lly          折线图图表模块左下角y位置
     * @param partWidth    折线图图表模块图表宽度
     * @param partHeight   折线图图表模块图表高度
     * @param basePartData 折线图图表模块数据对象
     * @param parentPart   折线图图表模块父图表模块
     * @param subParts     折线图图表模块子模块
     */
    public LinesPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                     BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            if (this.getParentPart() != null) {
                this.llx += this.getParentPart().llx;
                this.lly += this.getParentPart().lly;
            }

            LinesPartData linesPartData = (LinesPartData) basePartData;

            drawLines(canvas, linesPartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画线图图表内容
     *
     * @param canvas        画笔
     * @param linesPartData 折线图图表模块数据对象
     */
    protected void drawLines(PdfContentByte canvas, LinesPartData linesPartData) {
        Point[] lineVertices;
        Line line;
        int index = 0;

        for (Map<String, Object> map : linesPartData.datas) {
            lineVertices = getLineVertices(map.size(),
                    getMaxPercentageToShow(linesPartData.datas,
                            linesPartData.dataKeys,
                            linesPartData.max),
                    getMinPercentageToShow(linesPartData.datas,
                            linesPartData.dataKeys,
                            linesPartData.min),
                    mapToFloatArray(map, linesPartData.dataKeys));
            line = new Line(canvas,
                    lineVertices,
                    10,
                    0,
                    1,
                    linesPartData.linesColor.get(index),
                    true,
                    Line.NODE_SHOWN_AS_CIRCLE);
            line.draw();
            index++;
        }
    }

    public static void main(String[] args) {
        try {
            String dest = "results/objects/test.pdf";
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
            dataKeys.add("key10");
            dataKeys.add("key11");
            dataKeys.add("key12");
            dataKeys.add("key13");

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
            map.put(dataKeys.get(9), 30f);
            map.put(dataKeys.get(10), 23f);
            map.put(dataKeys.get(11), 45f);
            map.put(dataKeys.get(12), 90f);
            datas.add(map);

            List<BaseColor> linesColor = new ArrayList<BaseColor>();

            linesColor.add(ColorUtil.strRGBAToColor("0xf39800ff"));

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            LinesPart linesPart = new LinesPart(new LinesPartData(datas, dataKeys, linesColor));

            linesPart.setLlx(36);
            linesPart.setLly(36);
            linesPart.setPartWidth(500);
            linesPart.setPartHeight(100);
            linesPart.draw(pdfWriter.getDirectContent());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个map根据指定的key值集合转换为一个数组
     *
     * @param map      待转换的map
     * @param dataKeys 指定的key值集合
     * @return 转换后的数据数组
     */
    protected float[] mapToFloatArray(Map<String, Object> map, List<String> dataKeys) {
        float[] datas = new float[map.size()];
        int index = 0;

        for (String key : dataKeys) {
            datas[index] = (float) map.get(key);
            index++;
        }

        return datas;
    }

    /**
     * 根据显示规模将需要显示的数值规划到合理的范围中
     *
     * @param percentageToShow 最大的显示数值
     * @return 规范后的显示数值
     */
    public static float normalizePercentageToShow(float percentageToShow) {
        int normalizeScale = PdfConvertUtil.getNormalizeScale(percentageToShow) * 5;

        // 此行代码将需要显示的百分比规划到合理的范围中
        return normalizeScale;
    }

    /**
     * 根据折线图的数据计算出某条折线在图表中展示的每个顶点的位置
     *
     * @param nodeGroupCount      节点所在的分组位置
     * @param maxPercentageToShow 最大展示数值
     * @param minPercentageToShow 最小展示数值
     * @param lineDatas           原始线段数据
     * @return 计算得到的折线各个顶点数据
     */
    protected Point[] getLineVertices(int nodeGroupCount, float maxPercentageToShow, float minPercentageToShow,
                                      float[] lineDatas) {
        Point[] lineVertices;

        lineVertices = new Point[nodeGroupCount];

        for (int index = 0; index < nodeGroupCount; index++) {
            lineVertices[index] = new Point(0, 0);
        }

        for (int index = 0; index < nodeGroupCount; index++) {
            if (lineDatas[index] < minPercentageToShow) {
                lineDatas[index] = minPercentageToShow;
            }

            if (lineDatas[index] > maxPercentageToShow) {
                lineDatas[index] = maxPercentageToShow;
            }

            lineVertices[index].x = llx + (partWidth) * (index + 0.5f) / nodeGroupCount;
            lineVertices[index].y = lly
                    + (lineDatas[index] - minPercentageToShow)
                    / (maxPercentageToShow - minPercentageToShow) * partHeight;
        }

        return lineVertices;
    }

    /**
     * 根据折线数据计算出最大的展示数值
     *
     * @param datas    原始的折线图数据
     * @param dataKeys 用于取出数据的数据key
     * @param max      默认的最大数值
     * @return 如果提供了默认值即默认最大值不等于 -Float.MAX_VALUE将直接返回默认值，否则返回计算得到的值
     */
    public static float getMaxPercentageToShow(List<Map<String, Object>> datas, List<String> dataKeys, float max) {
        if (max != -Float.MAX_VALUE) {
            return max;
        }

        float maxPercentageToShow = -Float.MAX_VALUE;

        for (Map<String, Object> map : datas) {
            for (String key : dataKeys) {
                if (maxPercentageToShow < (Float) map.get(key)) {
                    maxPercentageToShow = (Float) map.get(key);
                }
            }
        }

        /*
         * if(maxPercentageToShow < 0){
         *   return 0;
         * }else if(maxPercentageToShow <= 5){
         *   return 5;
         * }else if(maxPercentageToShow <= 10){
         *   return 10;
         * }else if(maxPercentageToShow <= 50){
         *   return 50;
         * }else if(maxPercentageToShow <= 100){
         *   return 100;
         * }else if(maxPercentageToShow <= 500){
         *   return 500;
         * }else if(maxPercentageToShow <= 1000){
         *   return 1000;
         * }else{
         *   int scale = 1000;
         *
         *   while (maxPercentageToShow > scale){
         *       scale += 500;
         *   }
         *
         *   return scale;
         * }
         */
        int normalizeScale = PdfConvertUtil.getNormalizeScale(maxPercentageToShow);

        return (int) ((Math.round(maxPercentageToShow * 1.0f / normalizeScale + 0.5)) * normalizeScale);

//      return (float) Math.ceil(maxPercentageToShow);
        // return  PdfConvertUtil.getNormalizeScale(maxPercentageToShow) * 5;
    }

    /**
     * 根据折线数据计算出最小的展示数值
     *
     * @param datas    原始的折线图数据
     * @param dataKeys 用于取出数据的数据key
     * @param min      默认最小数值
     * @return 如果提供了默认值即默认最小值不等于 Float.MAX_VALUE将直接返回默认值，否则返回计算得到的值
     */
    public static float getMinPercentageToShow(List<Map<String, Object>> datas, List<String> dataKeys, float min) {
        if (min != Float.MAX_VALUE) {
            return min;
        }

        float minPercentageToShow = Float.MAX_VALUE;

        for (Map<String, Object> map : datas) {
            for (String key : dataKeys) {
                if (minPercentageToShow > (Float) map.get(key)) {
                    minPercentageToShow = (Float) map.get(key);
                }
            }
        }

        return 0;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
