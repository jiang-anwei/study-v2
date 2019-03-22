package com.icekredit.pdf.producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.icekredit.pdf.ICKPdfPart;
import com.icekredit.pdf.cover.ForgeCoverPart;

/**
 * 冰鉴报告生成器实现类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class IcekreditReportProducer extends ReportProducer {

    /**
     * 构造函数，完成各种私有属性初始化
     *
     */
    public IcekreditReportProducer() {
        pageHeaderParts = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(ForgeCoverPart.PART_TYPE, ForgeCoverPart.PART_TYPE_NORMAL);
        map.put(ForgeCoverPart.PART_DESC, "冰鉴");
        pageHeaderParts.add(map);
        map = new HashMap<String, Object>();
        map.put(ForgeCoverPart.PART_TYPE, ForgeCoverPart.PART_TYPE_SUPERSCRIPT);
        map.put(ForgeCoverPart.PART_DESC, "TM");
        pageHeaderParts.add(map);
        map = new HashMap<String, Object>();
        map.put(ForgeCoverPart.PART_TYPE, ForgeCoverPart.PART_TYPE_NORMAL);
        map.put(ForgeCoverPart.PART_DESC, "量化查询服务");
        pageHeaderParts.add(map);
        companyName      = "上海冰鉴信息科技有限公司";
        footerWebsiteStr = "www.icekredit.com";
        headerIconStr    = "header_icon.png";
    }

    @Override
    public void appendPart(ICKPdfPart pdfPart) {}

    @Override
    public void produce() {}
}


//~ Formatted by Jindent --- http://www.jindent.com
