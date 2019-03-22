package com.icekredit.pdf.entities.chart.partdata;

import com.icekredit.pdf.entities.chart.part.TitlePart;

import com.itextpdf.text.Rectangle;

/**
 * 标题模块数据类，用来控制图表标题模块TitlePart的展示
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class TitlePartData extends BasePartData {

    /** 标题 */
    public String title;

    /**
     * 标题模块数据构造函数
     *
     *
     * @param title 标题名称
     */
    public TitlePartData(String title) {
        this.title          = title;
        this.chartPartClass = TitlePart.class;
        this.positionRect   = new Rectangle(10, 90, 90, 100);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
