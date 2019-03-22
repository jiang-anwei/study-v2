package com.icekredit.pdf.cover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.icekredit.pdf.event.PageEventImpl;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.ImageResourceLoader;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;

/**
 * 封底Part
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class BackCoverPart extends CoverPart {

    /**
     * 封底默认公司名称
     */
    protected static final String DEFAULT_COMPANY_NAME = "上海冰鉴信息科技有限公司";

    /**
     * 封底默认公司地址
     */
    protected static final String DEFAULT_COMPANY_ADDRESS = "地址:上海浦东新区商城路618号良友大厦四楼B207";

    /**
     * 封底默认公司联系方式
     */
    protected static final String DEFAULT_COMPANY_TELEPHONE = "电话:+86-21-68592015";

    /**
     * 封底默认公司email
     */
    protected static final String DEFAULT_COMPANY_EMAIL = "E-mail:info@icekredit.com";

    /**
     * 封底默认公司门户网站链接
     */
    protected static final String DEFAULT_COMPANY_WEBSITE = "Web:www.icekredit.com";

    /**
     * 封底默认公司logo
     */
    protected static final String DEFAULT_BACK_COVER_LOGO = "backcover_logo.png";

    /**
     * 公司名称
     */
    protected String companyName;

    /**
     * 公司地址
     */
    protected String companyAddress;

    /**
     * 公司联系方式
     */
    protected String companyTelephone;

    /**
     * 公司email
     */
    protected String companyEmail;

    /**
     * 公司网站
     */
    protected String companyWebsite;

    /**
     * 封底公司logo
     */
    protected String backCoverLogo;

    /**
     * 构造函数，本构造函数只需要较少参数，其他参数有默认值提供
     *
     * @param document 文档对象
     */
    public BackCoverPart(Document document) {
        this(document,
                DEFAULT_COMPANY_NAME,
                DEFAULT_COMPANY_ADDRESS,
                DEFAULT_COMPANY_TELEPHONE,
                DEFAULT_COMPANY_EMAIL,
                DEFAULT_COMPANY_WEBSITE,
                DEFAULT_BACK_COVER_LOGO);
    }

    /**
     * 构造函数
     *
     * @param document         文档对象
     * @param companyName      公司名称
     * @param companyAddress   公司地址
     * @param companyTelephone 公司联系方式
     * @param companyEmail     公司email
     * @param companyWebsite   公司网站
     * @param backCoverLogo    封底公司logo
     */
    public BackCoverPart(Document document, String companyName, String companyAddress, String companyTelephone,
                         String companyEmail, String companyWebsite, String backCoverLogo) {
        super(document);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyTelephone = companyTelephone;
        this.companyEmail = companyEmail;
        this.companyWebsite = companyWebsite;
        this.backCoverLogo = backCoverLogo;
    }

    /**
     * 画封底的背景图片
     *
     * @param canvas 画笔
     */
    private void drawBackground(PdfContentByte canvas) {
        try {
            Image image = Image.getInstance(ImageResourceLoader.getSingleInstance()
                    .loadImage("backcover_background.png"));
            float primitiveWidth = image.getPlainWidth();
            float primitiveHeight = image.getPlainHeight();
            float scaleWidth = 0;
            float scaleHeight = 0;
            float imageHWRatio = primitiveHeight / primitiveWidth;

            scaleWidth = (primitiveWidth > PageSize.A4.getWidth() * 0.9)
                    ? PageSize.A4.getWidth() * 0.9f
                    : scaleWidth;
            scaleHeight = scaleWidth * imageHWRatio;
            image.scaleAbsolute(scaleWidth, scaleHeight);
            image.setAbsolutePosition(PageSize.A4.getWidth() - scaleWidth * 0.88f,
                    PageSize.A4.getHeight() - scaleHeight * 0.83f);
            canvas.addImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 勾画公司地址字符串
     *
     * @param canvas 画笔
     */
    private void drawCompanyAddress(PdfContentByte canvas) {
        if (StringUtils.isEmpty(companyAddress)) {
            return;
        }

        float llx = 55;
        float lly = 139;
        Font font = new Font(FontUtils.baseFontChinese, 8, Font.NORMAL, new BaseColor(0xff, 0xff, 0xff, 0xff));

        DrawTextUtil.drawReportAttributeStr(canvas, companyAddress, llx, lly, font, false);
    }

    /**
     * 勾画公司email字符串
     *
     * @param canvas 画笔
     */
    private void drawCompanyEmail(PdfContentByte canvas) {
        if (StringUtils.isEmpty(companyEmail)) {
            return;
        }

        float llx = 55;
        float lly = 115;
        Font font = new Font(FontUtils.baseFontChinese, 8, Font.NORMAL, new BaseColor(0xff, 0xff, 0xff, 0xff));

        DrawTextUtil.drawReportAttributeStr(canvas, companyEmail, llx, lly, font, false);
    }

    /**
     * 勾画公司名称字符串
     *
     * @param canvas 画笔
     */
    private void drawCompanyName(PdfContentByte canvas) {
        if (StringUtils.isEmpty(companyName)) {
            return;
        }

        float llx = 55;
        float lly = 168;
        Font font = new Font(FontUtils.baseFontChinese, 11, Font.NORMAL, new BaseColor(0xff, 0xff, 0xff, 0xff));

        DrawTextUtil.drawReportAttributeStr(canvas, companyName, llx, lly, font, false);
    }

    /**
     * 勾画公司联系方式字符串
     *
     * @param canvas 画笔
     */
    private void drawCompanyTelephone(PdfContentByte canvas) {
        try {
            if (StringUtils.isEmpty(companyTelephone)) {
                return;
            }

            float llx = 55;
            float lly = 127;
            Font font = new Font(FontUtils.baseFontChinese,
                    8,
                    Font.NORMAL,
                    new BaseColor(0xff, 0xff, 0xff, 0xff));
            float strWidth = FontUtils.baseFontChinese.getWidthPoint(companyTelephone, font.getSize()) + 10;
            float strHeight = DrawTextUtil.getStringHeight((int) font.getSize());
            Phrase strPhrase = new Phrase(new Chunk(companyTelephone, font));
            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(new Rectangle(llx, lly - strHeight / 2, llx + strWidth, lly + strHeight / 2));
            columnText.setUseAscender(true);
            columnText.addText(strPhrase);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 勾画公司门户网站字符串
     *
     * @param canvas 画笔
     */
    private void drawCompanyWebSite(PdfContentByte canvas) {
        if (StringUtils.isEmpty(companyWebsite)) {
            return;
        }

        float llx = 55;
        float lly = 103;
        Font font = new Font(FontUtils.baseFontChinese, 8, Font.NORMAL, new BaseColor(0xff, 0xff, 0xff, 0xff));

        DrawTextUtil.drawReportAttributeStr(canvas, companyWebsite, llx, lly, font, false);
    }

    /**
     * 画公司logo
     *
     * @param canvas 画笔
     */
    private void drawLogo(PdfContentByte canvas) {
        try {
            if (StringUtils.isEmpty(backCoverLogo)) {
                return;
            }

            Image image = Image.getInstance(ImageResourceLoader.getSingleInstance().loadImage(backCoverLogo));
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
            image.setAbsolutePosition(80, 200);
            canvas.addImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            BackCoverPart coverPart = new BackCoverPart(document);

            coverPart.write(pdfWriter, mainFrame);
            document.add(mainFrame);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(PdfWriter pdfWriter, PdfPTable mainFrame) throws IOException, DocumentException {
        document.newPage();
        super.write(pdfWriter, mainFrame);

        PdfContentByte canvas = pdfWriter.getDirectContent();

        drawBackground(canvas);
        drawCompanyName(canvas);
        drawCompanyAddress(canvas);
        drawCompanyTelephone(canvas);
        drawCompanyEmail(canvas);
        drawCompanyWebSite(canvas);
        drawLogo(canvas);
        PageEventImpl.isLastPage = true;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
