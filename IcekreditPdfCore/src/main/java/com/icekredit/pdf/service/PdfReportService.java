package com.icekredit.pdf.service;

import java.io.OutputStream;

import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.JSONAttrGetter;

import net.sf.json.JSONObject;

/**
 * Pdf报告服务抽象类，所有Pdf service都实现createReport(OutputStream outputStream, JSONObject jsonObject)函数
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public abstract class PdfReportService {

    /**
     * 此字段用于提前加载字体
     */
    private FontUtils fontUtils;    // 在此处就加载字体

    /**
     * 报告名称
     */
    public String reportName;

    /**
     * 报告封面Logo
     */
    public String foreCoverLogo;

    /**
     * 报告封底公司名称
     */
    public String companyName;

    /**
     * 报告封底公司地址
     */
    public String companyAddress;

    /**
     * 报告封底公司联系电话
     */
    public String companyTelephone;

    /**
     * 报告封底公司email
     */
    public String companyEmail;

    /**
     * 报告封底公司网站信息
     */
    public String companyWebsite;

    /**
     * 报告封底公司logo
     */
    public String backCoverLogo;

    /**
     * 无参构造函数
     */
    public PdfReportService() {
        reportName = JSONAttrGetter.DEFAULT_STR_VALUE;
        foreCoverLogo = JSONAttrGetter.DEFAULT_STR_VALUE;
        companyName = JSONAttrGetter.DEFAULT_STR_VALUE;
        companyAddress = JSONAttrGetter.DEFAULT_STR_VALUE;
        companyTelephone = JSONAttrGetter.DEFAULT_STR_VALUE;
        companyEmail = JSONAttrGetter.DEFAULT_STR_VALUE;
        companyWebsite = JSONAttrGetter.DEFAULT_STR_VALUE;
        backCoverLogo = JSONAttrGetter.DEFAULT_STR_VALUE;
    }

    /**
     * 创建报告
     *
     * @param outputStream 输出流
     * @param jsonObject   构造报告的json对象
     */
    public abstract void createReport(OutputStream outputStream, JSONObject jsonObject);
}


//~ Formatted by Jindent --- http://www.jindent.com
