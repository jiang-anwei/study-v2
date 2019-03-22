package com.icekredit.pdf.entities.chart.partdata;

import java.util.List;

import com.icekredit.pdf.entities.chart.part.YAxisPart;

import com.itextpdf.text.Rectangle;

/**
 * y轴坐标模块数据，主要包括构建y轴需要的位置信息由基类BasePartData提供，以及构建y轴需要的刻度信息，用来控制y轴坐标模块 YAxisPart展示
 *
 * xAxisMin xAxisMax xAxisReferTo三个参数的说明：这三个参数主要用于控制y轴相对于x轴的位置，因为具体x轴刻度数据可能出现
 * [-,0]、[0,+]以及[-，+]的情况，此时就可以通过这三个参数控制y轴的位置，来实现展示范围的控制，比如x轴刻度数据分布在[-50,10]
 * 那么可以设置xAxisMin=-50 xAxisMax=10 xAxisReferTo=0,那么y轴会相对x轴5/6的宽度展示，左边用于-50～0的数据展示，
 * 右边用于0～10的数据展示
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class YAxisPartData extends BasePartData {

    /**
     * x轴所能够表示的最小值
     */
    public float xAxisMin;    //x轴所能够表示的最小值

    /**
     * x轴所能表示的最大值
     */
    public float xAxisMax;    //x轴所能表示的最大值

    /**
     * y轴参考的x轴位置
     */
    public float xAxisReferTo;    // y轴参考的x轴位置

    /**
     * y轴上每一个刻度区间对应的描述
     */
    public List<String> scaleDescs;    // y轴上每一个刻度区间对应的描述

    /**
     * y轴名称
     */
    public String yAxisDesc;

    /**
     * 构造函数
     *
     * @param xAxisMin     x轴所能够表示的最小值
     * @param xAxisMax     x轴所能表示的最大值
     * @param xAxisReferTo y轴参考的x轴位置
     * @param scaleDescs   y轴上每一个刻度区间对应的描述
     * @param yAxisDesc    y轴名称
     */
    public YAxisPartData(float xAxisMin, float xAxisMax, float xAxisReferTo, List<String> scaleDescs,
                         String yAxisDesc) {
        this.xAxisMin = xAxisMin;
        this.xAxisMax = xAxisMax;
        this.xAxisReferTo = xAxisReferTo;
        this.scaleDescs = scaleDescs;
        this.yAxisDesc = yAxisDesc;
        this.chartPartClass = YAxisPart.class;
        this.positionRect = new Rectangle(10, 25, 90, 85);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
