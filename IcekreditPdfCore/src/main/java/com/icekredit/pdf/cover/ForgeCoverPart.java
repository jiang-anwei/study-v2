package com.icekredit.pdf.cover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;

/**
 * 封面Part
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class ForgeCoverPart extends CoverPart {

    /**
     * 报告header子部分的类型
     */
    public static final String PART_TYPE = "part_type";

    /**
     * 报告header子部分的内容
     */
    public static final String PART_DESC = "part_desc";

    /**
     * 普通文本模块
     */
    public static final int PART_TYPE_NORMAL = 0;

    /**
     * 下标文本模块
     */
    public static final int PART_TYPE_SUBSCRIPT = 1;

    /**
     * 上标文本模块
     */
    public static final int PART_TYPE_SUPERSCRIPT = 2;

    /**
     * 默认报告名称
     */
    protected static final String DEFAULT_REPORT_NAME = "量化分析报告";

    /**
     * 默认报告公司名称
     */
    protected static final String DEFAULT_COMPANY_NAME = "上海冰鉴信息科技有限公司";

    /**
     * 封面咯logo资源文件名称
     */
    protected static final String DEFAULT_FORE_COVER_LOGO = "forecover_logo.png";

    /**
     * 报告名称
     */
    protected String reportName;

    /**
     * 公司名称
     */
    protected String companyName;

    /**
     * 封面logo文件名称
     */
    protected String foreCoverLogo;

    /**
     * 封面标题子模块列表
     */
    protected List<Map<String, Object>> pageHeaderParts;

    /**
     * 报告编号
     */
    protected String reportNumStr;

    /**
     * 报告创建时间
     */
    protected String reportCreatedDateTimeStr;

    /**
     * 构造函数，此幻术提供较少参数，其他参数有默认值提供
     *
     * @param document                 文档对象
     * @param reportNumStr             报告编号
     * @param reportCreatedDateTimeStr 报告生成时间
     * @param pageHeaderParts          报告header个模块list
     */
    public ForgeCoverPart(Document document, String reportNumStr, String reportCreatedDateTimeStr,
                          List<Map<String, Object>> pageHeaderParts) {
        this(document,
                reportNumStr,
                reportCreatedDateTimeStr,
                pageHeaderParts,
                DEFAULT_REPORT_NAME,
                DEFAULT_COMPANY_NAME,
                DEFAULT_FORE_COVER_LOGO);
    }

    /**
     * CONSTRUCTOR_DESCRIPTION
     *
     * @param document                 文档对象
     * @param reportNumStr             报告编号
     * @param reportCreatedDateTimeStr 报告生成时间
     * @param pageHeaderParts          报告header个模块list
     * @param reportName               报告名称
     * @param companyName              公司名称
     * @param foreCoverLogo            报告封面logo
     */
    public ForgeCoverPart(Document document, String reportNumStr, String reportCreatedDateTimeStr,
                          List<Map<String, Object>> pageHeaderParts, String reportName, String companyName,
                          String foreCoverLogo) {
        super(document);
        this.reportNumStr = reportNumStr;
        this.reportCreatedDateTimeStr = reportCreatedDateTimeStr;
        this.pageHeaderParts = pageHeaderParts;
        this.reportName = reportName;
        this.companyName = companyName;
        this.foreCoverLogo = foreCoverLogo;
    }

    /**
     * 画logo底部的白色背景
     *
     * @param canvas 画笔
     */
    private void drawBackground(PdfContentByte canvas) {
        canvas.saveState();
        canvas.setColorStroke(new BaseColor(0xff, 0xff, 0xff, 0xdd));
        canvas.setColorFill(new BaseColor(0xff, 0xff, 0xff, 0xdd));
        canvas.rectangle(495, 580, 100, 100);
        canvas.fillStroke();
        canvas.restoreState();
    }

    /**
     * 画背景图片
     *
     * @param pdfWriter pdfWriter对象
     */
    private void drawBackgroundIcon(PdfWriter pdfWriter) {
        try {
            PdfContentByte canvas = pdfWriter.getDirectContent();
            Image image = Image.getInstance(ImageResourceLoader.getSingleInstance()
                    .loadImage("forecover_background.png"));
            float primitiveWidth = image.getPlainWidth();
            float primitiveHeight = image.getPlainHeight();
            float scaleWidth = 0;
            float scaleHeight = 0;
            float imageHWRatio = primitiveHeight / primitiveWidth;

            scaleWidth = (primitiveWidth > (PageSize.A4.getWidth() * 1.0f))
                    ? (PageSize.A4.getWidth() * 1.0f)
                    : primitiveWidth;
            scaleHeight = scaleWidth * imageHWRatio;
            image.scaleAbsolute(scaleWidth, scaleHeight);
            image.setAbsolutePosition(0, 0);
            canvas.addImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画公司名称
     *
     * @param pdfWriter pdfWriter对象
     */
    private void drawCompanyName(PdfWriter pdfWriter) {
        if(StringUtils.isEmpty(companyName)){
            return;
        }

        float companyNameStrXPosition = 540;
        float companyNameStrYPosition = 52;
        Font companyNameFont = new Font(FontUtils.chineseFont);

        companyNameFont.setColor(new BaseColor(0xff, 0xff, 0xff, 0xff));
        companyNameFont.setSize(11);
        DrawTextUtil.drawReportAttributeStr(pdfWriter.getDirectContent(),
                companyName,
                companyNameStrXPosition,
                companyNameStrYPosition,
                companyNameFont,
                true);
    }

    /**
     * 画公司logo
     *
     * @param pdfWriter pdfWriter对象
     */
    private void drawLogo(PdfWriter pdfWriter) {
        if(StringUtils.isEmpty(foreCoverLogo)){
            return;
        }

        drawBackground(pdfWriter.getDirectContent());
        drawLogoIcon(pdfWriter.getDirectContent());
    }

    /**
     * 画封面Icon
     *
     * @param canvas 画笔
     */
    private void drawLogoIcon(PdfContentByte canvas) {
        try {
            if(StringUtils.isEmpty(foreCoverLogo)){
                return;
            }

            Image image = Image.getInstance(ImageResourceLoader.getSingleInstance().loadImage(foreCoverLogo));
            float primitiveWidth = image.getPlainWidth();
            float primitiveHeight = image.getPlainHeight();
            float scaleWidth = 0;
            float scaleHeight = 0;

            if (primitiveWidth > primitiveHeight) {
                float ratio = primitiveHeight / primitiveWidth;

                scaleWidth = (primitiveWidth > 80)
                        ? 80
                        : primitiveWidth;
                scaleHeight = scaleWidth * ratio;
            } else {
                float ratio = primitiveWidth / primitiveHeight;

                scaleHeight = (primitiveHeight > 80)
                        ? 80
                        : primitiveHeight;
                scaleWidth = scaleHeight * ratio;
            }

            image.scaleAbsolute(scaleWidth, scaleHeight);
            image.setAbsolutePosition(495 + 50 - scaleWidth / 2, 580 + 50 - scaleHeight / 2);
            canvas.addImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画报告名称额外信息
     *
     * @param pdfWriter pdfWriter对象
     */
    private void drawNameExtra(PdfWriter pdfWriter) {
        float reportNameExtraXPosition = 480;
        float reportNameExtraYPosition = 668;
        Font reportNameExtraFont = new Font(FontUtils.baseFontChinese,
                14,
                Font.NORMAL,
                new BaseColor(0xff, 0xff, 0xff, 0xff));

        drawNameExtraHelper(pdfWriter.getDirectContent(),
                reportNameExtraXPosition,
                reportNameExtraYPosition,
                reportNameExtraFont);
    }

    /**
     * 画报告名称
     *
     * @param canvas       画笔
     * @param strXPosition 字符串中点X坐标
     * @param strYPosition 字符串中点y坐标
     * @param strFont      字符串字体
     */
    private void drawNameExtraHelper(PdfContentByte canvas, float strXPosition, float strYPosition, Font strFont) {
        try {
            if(pageHeaderParts == null || pageHeaderParts.isEmpty()){
                return;
            }

            float strWidth = PdfConvertUtil.getPageHeaderWidth(pageHeaderParts, strFont.getSize());
            float strHeight = DrawTextUtil.getStringHeight((int) strFont.getSize());
            Phrase nameExtraPhrase = PdfConvertUtil.getHeaderPhrase(pageHeaderParts, strFont);
            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(new Rectangle(strXPosition - strWidth,
                    strYPosition - strHeight / 2,
                    strXPosition,
                    strYPosition + strHeight / 2));
            columnText.setUseAscender(true);
            columnText.addText(nameExtraPhrase);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画报告创建时间字符串
     *
     * @param pdfWriter pdfWriter对象
     */
    private void drawReportCreatedDateTimeStr(PdfWriter pdfWriter) {
        if(StringUtils.isEmpty(reportCreatedDateTimeStr)){
            return;
        }

        float reportCreatedDateTimeStrXPosition = 540;
        float reportCreatedDateTimeStrYPosition = 64;
        Font reportCreatedDateTimeFont = new Font(FontUtils.chineseFont);

        reportCreatedDateTimeFont.setColor(new BaseColor(0xff, 0xff, 0xff, 0xff));
        reportCreatedDateTimeFont.setSize(8);
        DrawTextUtil.drawReportAttributeStr(pdfWriter.getDirectContent(),
                reportCreatedDateTimeStr,
                reportCreatedDateTimeStrXPosition,
                reportCreatedDateTimeStrYPosition,
                reportCreatedDateTimeFont,
                true);
    }

    /**
     * 画报告名称
     *
     * @param pdfWriter PdfWriter对象
     */
    private void drawReportName(PdfWriter pdfWriter) {
        try {
            if(StringUtils.isEmpty(reportName)){
                return;
            }

            float reportNameXPosition = 485;
            float reportNameYPosition = 630;
            Font reportNameFont = new Font(FontUtils.baseFontChinese,
                    36,
                    Font.NORMAL,
                    new BaseColor(0xff, 0xff, 0xff, 0xff));

            DrawTextUtil.drawReportAttributeStr(pdfWriter.getDirectContent(),
                    reportName,
                    reportNameXPosition,
                    reportNameYPosition,
                    reportNameFont,
                    true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画报告编号字符串
     *
     * @param pdfWriter PdfWriter对象
     */
    private void drawReportNumStr(PdfWriter pdfWriter) {
        if(StringUtils.isEmpty(reportNumStr) || reportNumStr.equals(JSONAttrGetter.DEFAULT_STR_VALUE)){
            return;
        }

        float reportNumStrXPosition = 480;
        float reportNumStrYPosition = 586.5f;
        Font reportNumFont = new Font(FontUtils.chineseFont);

        reportNumFont.setColor(new BaseColor(0xff, 0xff, 0xff, 0xff));
        reportNumFont.setSize(12);
        DrawTextUtil.drawReportAttributeStr(pdfWriter.getDirectContent(),
                reportNumStr,
                reportNumStrXPosition,
                reportNumStrYPosition,
                reportNumFont,
                true);
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "results/test.pdf";
            File destFile = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);

            mainFrame.setWidthPercentage(100);

            List<Map<String, Object>> pageHeaderParts = new ArrayList<Map<String, Object>>();
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

            ForgeCoverPart coverPart = new ForgeCoverPart(document,
                    "报告编号：2012141463123",
                    "报告日期：2016-01-01",
                    pageHeaderParts);

            coverPart.write(pdfWriter, mainFrame);

            AlignCenterCell alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文", FontUtils.chineseFont)));

            alignCenterCell.setColspan(12);

            for (int index = 0; index < 100; index++) {
                mainFrame.addCell(alignCenterCell);
            }

            document.add(mainFrame);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(PdfWriter pdfWriter, PdfPTable mainFrame) throws IOException, DocumentException {
        super.write(pdfWriter, mainFrame);
        drawBackgroundIcon(pdfWriter);
        drawNameExtra(pdfWriter);
        drawReportName(pdfWriter);
        drawReportNumStr(pdfWriter);
        drawReportCreatedDateTimeStr(pdfWriter);
        drawCompanyName(pdfWriter);
        drawLogo(pdfWriter);
        document.newPage();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
