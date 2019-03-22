package com.icekredit.pdf.producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icekredit.pdf.ICKPdfPart;
import com.icekredit.pdf.cover.ForgeCoverPart;
import com.icekredit.pdf.utils.JSONAttrGetter;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *报告生成器，所有子报告生成器都实现appendPart(ICKPdfPart pdfPart)和void produce()两个接口
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public abstract class ReportProducer {

    /** 报告页眉标题的各个部分 */
    public List<Map<String, Object>> pageHeaderParts = null;

    /** 与当前报告生成器关联的Pdf Document对象 */
    protected Document document;

    /** 页面框架，每个pdf子模块执行write方法之后，都会向页面主框架中添加一些表格或者单元格，最终这个主框架会被添加到document中 */
    protected PdfPTable mainFrame;

    /** PdfWriter对象，其与Document以及一个Pdf文档输出流相关联 */
    protected PdfWriter writer;

    /** Pdf子模块列表 */
    protected List<ICKPdfPart> partList;

    /** 公司名称 */
    public String companyName;

    /** 页脚里面需要添加的公司门户网站地址 */
    public String footerWebsiteStr;

    /** 页面页眉需要添加的头部图图标资源文件名称 */
    public String headerIconStr;

    /**
     * 构造函数
     *
     */
    public ReportProducer() {
        pageHeaderParts = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(ForgeCoverPart.PART_TYPE, ForgeCoverPart.PART_TYPE_NORMAL);
        map.put(ForgeCoverPart.PART_DESC, JSONAttrGetter.DEFAULT_STR_VALUE);
        pageHeaderParts.add(map);
        companyName      = JSONAttrGetter.DEFAULT_STR_VALUE;
        footerWebsiteStr = JSONAttrGetter.DEFAULT_STR_VALUE;
        headerIconStr    = JSONAttrGetter.DEFAULT_STR_VALUE;
    }

    /**
     * 向当前Producer的Pdf子模块列表属性中添加一个Pdf子模块
     *
     *
     * @param pdfPart 需要添加的Pdf子模块
     */
    public abstract void appendPart(ICKPdfPart pdfPart);

    /**
     * 生产函数，用于循环遍历当前Producer的子模块列表，调用每一个子模块的write方法，向页面主框架中添加PdfPCell或者PdfPTable子元素，最终将页面主框架添加到Document中，完成Pdf生成
     *
     */
    public abstract void produce();

    /**
     * Document对象的Getter
     *
     *
     * @return
     */
    public Document getDocument() {
        return document;
    }

    /**
     * document对象的setter
     *
     *
     * @param document 要设置的document对象
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * 页面主框架对象mainFrame的getter
     *
     *
     * @return
     */
    public PdfPTable getMainFrame() {
        return mainFrame;
    }

    /**
     * 页面主框架对象mainFrame的setter
     *
     *
     * @param mainFrame 要设置的页面主框架对象
     */
    public void setMainFrame(PdfPTable mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * 页面pdf子模块列表getter
     *
     *
     * @return 页面pdf子模块列表
     */
    public List<ICKPdfPart> getPartList() {
        return partList;
    }

    /**
     * 页面pdf子模块列表setter
     *
     *
     * @param partList 需要设置的页面pdf子模块列表
     */
    public void setPartList(List<ICKPdfPart> partList) {
        this.partList = partList;
    }

    /**
     * producer PdfWriter 属性getter
     *
     *
     * @return producer PdfWriter 属性
     */
    public PdfWriter getWriter() {
        return writer;
    }

    /**
     * producer PdfWriter 属性setter
     *
     *
     * @param writer 要设置的producer PdfWriter 属性
     */
    public void setWriter(PdfWriter writer) {
        this.writer = writer;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
