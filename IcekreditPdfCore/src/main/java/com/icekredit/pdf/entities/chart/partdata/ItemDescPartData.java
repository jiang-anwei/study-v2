package com.icekredit.pdf.entities.chart.partdata;

import java.util.List;

import com.icekredit.pdf.entities.chart.part.ItemDescPart;

import com.itextpdf.text.Rectangle;

/**
 * 子部分描述模块数据类。子部分描述主要包括展示类型、颜色、描述信息，用来控制子条目描述模块ItemDescPart的展示效果
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class ItemDescPartData extends BasePartData {

    /** 子部分描述模块数据 */
    public List<ItemDescPart.PartDesc> partsDescList;

    /**
     * 构造函数
     *
     *
     * @param partsDescList 子部分描述模块数据
     */
    public ItemDescPartData(List<ItemDescPart.PartDesc> partsDescList) {
        this.partsDescList  = partsDescList;
        this.chartPartClass = ItemDescPart.class;
        this.positionRect   = new Rectangle(10, 0, 90, 20);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
