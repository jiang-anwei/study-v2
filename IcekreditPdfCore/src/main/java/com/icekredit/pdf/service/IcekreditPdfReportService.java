package com.icekredit.pdf.service;

/**
 *  冰鉴PdfReportService
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public abstract class IcekreditPdfReportService extends PdfReportService {

    /**
     * 构造函数，完成私有属性的初始化
     *
     */
    public IcekreditPdfReportService() {
        reportName       = "量化分析报告";
        foreCoverLogo    = "forecover_logo.png";
        companyName      = "上海冰鉴信息科技有限公司";
        companyAddress   = "地址:上海浦东新区商城路618号良友大厦四楼B207";
        companyTelephone = "电话:+86-21-68592015";
        companyEmail     = "E-mail:info@icekredit.com";
        companyWebsite   = "Web:www.icekredit.com";
        backCoverLogo    = "backcover_logo.png";
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
