package com.icekredit.pdf.entities.chart.partdata;

import com.icekredit.pdf.entities.chart.part.LinesPart;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

import java.util.List;
import java.util.Map;

/**
 * 折线图模块数据类，用来控制折线图模块LinesPart的展示
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class LinesPartData extends BasePartData {

    /** 折线图模块数据 */
    public List<Map<String, Object>> datas;

    /** 用于遍历每一根线的数据的key值集合 */
    public List<String> dataKeys;

    /** 所有折线使用的颜色数组 */
    public List<BaseColor> linesColor;

    /** 线图y轴坐标展示的最小值 如果为默认值 Float.MAX_VALUE 表明此值应该由程序经datas数据部分推断得来*/
    public float min;

    /** 线图y轴坐标展示的最大值 如果为默认值 -Float.MAX_VALUE 表明此值应该由程序经datas数据部分推断得来*/
    public float max;

    /**
     * 构造函数，本构造函数提供部分参数，其他参数由默认参数提供
     *
     *
     * @param datas 折线图模块数据
     * @param dataKeys 用于遍历每一根线的数据的key值集合
     * @param linesColor 所有折线使用的颜色数组
     */
    public LinesPartData(List<Map<String, Object>> datas, List<String> dataKeys, List<BaseColor> linesColor) {
        this(datas, dataKeys, linesColor, Float.MAX_VALUE, -Float.MAX_VALUE);
    }

    /**
     * 构造函数
     *
     *
     * @param datas 折线图模块数据
     * @param dataKeys 用于遍历每一根线的数据的key值集合
     * @param linesColor 所有折线使用的颜色数组
     * @param min 线图y轴坐标展示的最小值
     * @param max 线图y轴坐标展示的最大值
     */
    public LinesPartData(List<Map<String, Object>> datas, List<String> dataKeys, List<BaseColor> linesColor, float min,
                         float max) {
        this.datas          = datas;
        this.dataKeys       = dataKeys;
        this.linesColor     = linesColor;
        this.min            = min;
        this.max            = max;
        this.chartPartClass = LinesPart.class;
        this.positionRect   = new Rectangle(10, 25, 90, 85);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
