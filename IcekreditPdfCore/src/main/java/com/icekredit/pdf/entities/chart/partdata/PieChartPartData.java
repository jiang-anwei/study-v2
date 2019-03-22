package com.icekredit.pdf.entities.chart.partdata;

import com.icekredit.pdf.entities.chart.part.PieChartPart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;

/**
 * 饼图模块数据类，用来控制饼图模块PieChartPart的展示
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class PieChartPartData extends BasePartData {

    /**
     * 默认的扇区描述字体大小
     */
    public static final int DEFAULT_SECTOR_DESC_FONT_SIZE = 4;

    /**
     * 默认的扇区描述字体样式
     */
    public static final int DEFAULT_SECTOR_DESC_FONT_STYLE = Font.NORMAL;

    /**
     * 默认的扇区描述字体颜色
     */
    public static final BaseColor DEFAULT_SECTOR_DESC_FONT_COLOR = new BaseColor(0x55, 0x55, 0x55, 0xff);

    /**
     * 饼图外圆圆形半径
     */
    public float outerCircleRadius;

    /**
     * 饼图内元內圆半径，此属性主要用来控制环图覆盖圆的大小
     */
    public float innerCircleRadius;

    /**
     * 扇区角度数组
     */
    public float[] sectorsAngle;

    /**
     * 扇区颜色数组
     */
    public BaseColor[] sectorsColor;

    /**
     * 扇区描述数组
     */
    public String[] sectorsDesc;

    /**
     * 扇区描述字体大小
     */
    public int sectorDescFontSize;

    /**
     * 扇区描述字体样式
     */
    public int sectorDescFontStyle;

    /**
     * 扇区描述字体颜色
     */
    public BaseColor sectorDescFontColor;

    /**
     * 是否需要勾画扇区描述
     */
    public boolean isNeedDrawDesc;

    /**
     * 是否需要勾画饼图的cover
     */
    public boolean isNeedDrawCover;

    /**
     * 构造函数，本构造函数只提供部分参数，其他参数由默认值提供
     *
     * @param outerCircleRadius 饼图外圆圆形半径
     * @param innerCircleRadius 饼图内元內圆半径，此属性主要用来控制环图覆盖圆的大小
     * @param sectorsAngle      扇区角度数组
     * @param sectorsColor      扇区颜色数组
     * @param sectorsDesc       扇区描述数组
     * @param isNeedDrawDesc    是否需要勾画扇区描述
     * @param isNeedDrawCover   是否需要勾画饼图的cover
     */
    public PieChartPartData(float outerCircleRadius, float innerCircleRadius, float[] sectorsAngle,
                            BaseColor[] sectorsColor, String[] sectorsDesc, boolean isNeedDrawDesc,
                            boolean isNeedDrawCover) {
        this(outerCircleRadius,
                innerCircleRadius,
                sectorsAngle,
                sectorsColor,
                sectorsDesc,
                DEFAULT_SECTOR_DESC_FONT_SIZE,
                DEFAULT_SECTOR_DESC_FONT_STYLE,
                DEFAULT_SECTOR_DESC_FONT_COLOR,
                isNeedDrawDesc,
                isNeedDrawCover);
    }

    /**
     * CONSTRUCTOR_DESCRIPTION
     *
     * @param outerCircleRadius   饼图外圆圆形半径
     * @param innerCircleRadius   饼图内元內圆半径，此属性主要用来控制环图覆盖圆的大小
     * @param sectorsAngle        扇区角度数组
     * @param sectorsColor        扇区颜色数组
     * @param sectorsDesc         扇区描述数组
     * @param sectorDescFontSize  扇区描述字体大小
     * @param sectorDescFontStyle 扇区描述字体样式
     * @param sectorDescFontColor 扇区描述字体颜色
     * @param isNeedDrawDesc      是否需要勾画扇区描述
     * @param isNeedDrawCover     是否需要勾画饼图的cover
     */
    public PieChartPartData(float outerCircleRadius, float innerCircleRadius, float[] sectorsAngle,
                            BaseColor[] sectorsColor, String[] sectorsDesc, int sectorDescFontSize,
                            int sectorDescFontStyle, BaseColor sectorDescFontColor, boolean isNeedDrawDesc,
                            boolean isNeedDrawCover) {
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;
        this.sectorsAngle = sectorsAngle;
        this.sectorsColor = sectorsColor;
        this.sectorsDesc = sectorsDesc;
        this.sectorDescFontSize = sectorDescFontSize;
        this.sectorDescFontStyle = sectorDescFontStyle;
        this.sectorDescFontColor = sectorDescFontColor;
        this.isNeedDrawDesc = isNeedDrawDesc;
        this.isNeedDrawCover = isNeedDrawCover;
        this.chartPartClass = PieChartPart.class;
        this.positionRect = new Rectangle(10 + 40, 25 + 30, 90, 85);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
