package com.icekredit.pdf.entities.chart.partdata;

import java.util.List;

import com.icekredit.pdf.entities.chart.part.PillarsPart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

/**
 * 柱状图每个圆柱数据模块数据类，用来控制柱状图模块PillarsPart的展示
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class PillarsPartData extends BasePartData {

    /** 柱状图每个圆柱各个部分数据 */
    public List<PillarsPart.PillarItemPart> pillarItemDatas;

    /** 每个圆柱part颜色 */
    public List<BaseColor> pillarsPartColors;

    /**
     * 构造函数
     *
     *
     * @param pillarItemDatas 柱状图每个圆柱各个部分数据
     * @param pillarsPartColors 每个圆柱part颜色
     */
    public PillarsPartData(List<PillarsPart.PillarItemPart> pillarItemDatas, List<BaseColor> pillarsPartColors) {
        this.pillarItemDatas   = pillarItemDatas;
        this.pillarsPartColors = pillarsPartColors;
        this.chartPartClass    = PillarsPart.class;
        this.positionRect      = new Rectangle(10, 25, 90, 85);
    }

    @Override
    public String toString() {
        return "PillarsPartData{" + "pillarItemDatas=" + pillarItemDatas.size() + ", pillarsPartColors="
               + pillarsPartColors.size() + '}';
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
