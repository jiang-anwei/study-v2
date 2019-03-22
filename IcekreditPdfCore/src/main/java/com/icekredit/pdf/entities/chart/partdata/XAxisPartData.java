package com.icekredit.pdf.entities.chart.partdata;

import java.util.List;

import com.icekredit.pdf.entities.chart.part.XAxisPart;

import com.itextpdf.text.Rectangle;

/**
 * x 轴坐标模块数据，主要包括构建x轴需要的位置信息又基类BasePartData提供，以及构建x轴需要的刻度信息，用来控制图表x 轴坐标模块XAxisPart展示
 *
 * yAxisMin yAxisMax yAxisReferTo三个参数的说明：这三个参数主要用于控制x轴相对于y轴的位置，因为具体y轴刻度数据可能出现
 * [-,0]、[0,+]以及[-，+]的情况，此时就可以通过这三个参数控制x轴的位置，来实现展示范围的控制，比如y轴刻度数据分布在[-50,10]
 * 那么可以设置yAxisMin=-50 yAxisMax=10 yAxisReferTo=0,那么x轴会相对y轴5/6的高度展示，下方用于-50～0的数据展示，
 * 上方用于0～10的数据展示
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class XAxisPartData extends BasePartData {

    /** 默认的x轴描述旋转角度 */
    public static final float DEFAULT_SCALE_DESC_SHOWN_ANGLE = 0;    // + 顺时针 - 逆时针，0水平

    /** y轴所能够表示的最小值 */
    public float yAxisMin;    // y轴所能够表示的最小值

    /** y轴所能表示的最大值 */
    public float yAxisMax;    // y轴所能表示的最大值

    /** x轴参考的y轴位置 */
    public float yAxisReferTo;    // x轴参考的y轴位置

    /** x轴上每一个刻度区间对应的描述 */
    public List<String> scaleDescs;    // x轴上每一个刻度区间对应的描述

    /** 此变量用于控制x轴刻度的旋转角度 */
    public float scaleDescShownAngle;    // 此变量用于控制旋转角度

    /**
     * 构造函数，此函数提供较少的参数，部分参数由默认值提供
     *
     *
     * @param yAxisMin y轴所能够表示的最小值
     * @param yAxisMax y轴所能表示的最大值
     * @param yAxisReferTo x轴参考的y轴位置
     * @param scaleDescs x轴上每一个刻度区间对应的描述
     */
    public XAxisPartData(float yAxisMin, float yAxisMax, float yAxisReferTo, List<String> scaleDescs) {
        this(yAxisMin, yAxisMax, yAxisReferTo, scaleDescs, DEFAULT_SCALE_DESC_SHOWN_ANGLE);
    }

    /**
     * 构造函数
     *
     *
     * @param yAxisMin y轴所能够表示的最小值
     * @param yAxisMax y轴所能表示的最大值
     * @param yAxisReferTo x轴参考的y轴位置
     * @param scaleDescs x轴上每一个刻度区间对应的描述
     * @param scaleDescShownAngle 此变量用于控制x轴刻度的旋转角度
     */
    public XAxisPartData(float yAxisMin, float yAxisMax, float yAxisReferTo, List<String> scaleDescs,
                         float scaleDescShownAngle) {
        this.yAxisMin            = yAxisMin;
        this.yAxisMax            = yAxisMax;
        this.yAxisReferTo        = yAxisReferTo;
        this.scaleDescs          = scaleDescs;
        this.scaleDescShownAngle = scaleDescShownAngle;
        this.chartPartClass      = XAxisPart.class;
        this.positionRect        = new Rectangle(10, 25, 90, 85);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
