package com.icekredit.pdf.entities.chart.partdata;

import com.icekredit.pdf.entities.chart.part.BackgroundPart;

import com.itextpdf.text.Rectangle;

/**
 * 背景模块数据类，用于控制背景模块BackgroundPart的展示效果
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class BackgroundPartData extends BasePartData {

    /** 背景展示的级别 */
    public int properShownLevel;

    /**
     * 构造函数
     *
     *
     * @param properShownLevel 背景展示的级别
     */
    public BackgroundPartData(int properShownLevel) {
        this.properShownLevel = properShownLevel;
        this.chartPartClass   = BackgroundPart.class;
        this.positionRect     = new Rectangle(10, 25, 90, 85);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
