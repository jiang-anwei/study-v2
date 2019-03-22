package com.icekredit.pdf.event;

import java.util.List;
import java.util.Map;

import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.ImageResourceLoader;
import com.icekredit.pdf.utils.PdfConvertUtil;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;

/**
 * PageEventImpl，此类被设计用来统一处理所有页面都需要用到的特性，比如页眉里面的header，水印等，每个页面都需要重复用到
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class PageEventImpl extends PdfPageEventHelper {

    /** 默认的水印字体 */
    protected static final Font DEFAULT_WATERMARK_FONT = new Font(FontUtils.baseFontChinese,
                                                                40,
                                                                Font.BOLD,
                                                                new BaseColor(0xcc, 0xcc, 0xcc, 0x77));

    /** 默认的水印内容 */
    protected static final String DEFAULT_WATERMARK_CONTENT = "上海冰鉴信息科技有限公司";

    /** 默认的页脚公司门户网站地址 */
    protected static final String DEFAULT_FOOTER_WEBSITE = "www.icekredit.com";

    /** 默认的页眉现实的header icon（右上角的图标） */
    protected static final String DEFAULT_HEADER_ICON = "header_icon.png";

    /** 默认的页眉页脚文字字体 */
    protected static final Font HEADER_FOOTER_FONT = new Font(FontUtils.baseFontChinese,
                                                              5,
                                                              Font.NORMAL,
                                                              new BaseColor(0x72, 0x71, 0x71, 0xff));

    /** 默认的页眉header icon（右上角的图标）高度 */
    protected static final float DEFAULT_MAX_HEADER_ICON_HEIGHT = 20;

    /** 默认的页眉页脚分隔线宽度 */
    protected static final float SEPARATE_LINE_WIDTH = 0.1f;

    /** 默认的页眉页脚分割线背景颜色 */
    protected static final BaseColor SEPARATE_LINE_BACKGROUND_COLOR = new BaseColor(0xcc, 0xcc, 0xcc, 0xff);

    /** 默认页边距 */
    protected static final float PAGE_MARGIN = 36;

    /** 是否是最后一页 */
    public static boolean isLastPage = false;

    /** 水印内容 */
    protected String waterMark;

    /** 页脚的公司门户网站链接 */
    protected String footerWebsite;

    /** 页眉的Header的各个部分（因为页眉有些部分有subscript或者superscript的需求） */
    protected List<Map<String, Object>> pageHeaderParts;

    /** 页眉header icon（右上角的图标） */
    protected String headerLogo;

    /**
     * 构造函数，此构造函数传入较少的参数，大部分参数由默认值提供
     *
     *
     * @param pageHeaderParts 页眉的Header的各个部分
     */
    public PageEventImpl(List<Map<String, Object>> pageHeaderParts) {
        this(DEFAULT_WATERMARK_CONTENT, DEFAULT_FOOTER_WEBSITE, pageHeaderParts, DEFAULT_HEADER_ICON);
    }

    /**
     * 构造函数
     *
     *
     * @param waterMark 水印信息
     * @param footerWebsite 页脚的公司门户网站链接
     * @param pageHeaderParts 页眉的Header的各个部分
     * @param headerLogo 页眉header icon（右上角的图标）
     */
    public PageEventImpl(String waterMark, String footerWebsite, List<Map<String, Object>> pageHeaderParts,
                         String headerLogo) {
        this.waterMark       = waterMark;
        this.footerWebsite   = footerWebsite;
        this.pageHeaderParts = pageHeaderParts;
        this.headerLogo      = headerLogo;
    }

    /**
     * 向当前页面中添加页脚
     *
     *
     * @param writer pdfWriter 对象，用于取出PdfContentByte对象并提供画图功能
     * @param document document文档对象，用于控制页脚的页码document.getPageNumber()
     */
    private void addFooter(PdfWriter writer, Document document) {
        try {
            PdfContentByte canvas = writer.getDirectContent();

            addFooterSeparateLine(canvas);
            addFooterName(canvas);
            addFooterPageNumber(canvas, document.getPageNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加页脚网站链接
     *
     *
     * @param canvas 画笔
     */
    private void addFooterName(PdfContentByte canvas) {
        try {
            if(StringUtils.isEmpty(footerWebsite)){
                return;
            }

            ColumnText strColumnText    = new ColumnText(canvas);
            Phrase     footerNamePhrase = new Phrase();
            Chunk      footerNameChunk  = new Chunk(footerWebsite, HEADER_FOOTER_FONT).setAnchor(footerWebsite);

            footerNamePhrase.add(footerNameChunk);
            strColumnText.setSimpleColumn(footerNamePhrase,
                                          PageSize.A4.getLeft() + PAGE_MARGIN,
                                          PageSize.A4.getBottom() + PAGE_MARGIN / 2
                                          - DrawTextUtil.getStringHeight((int) HEADER_FOOTER_FONT.getSize()) / 2,
                                          PageSize.A4.getLeft() + PAGE_MARGIN
                                          + FontUtils.baseFontChinese.getWidthPoint(footerWebsite,
                                                                                    HEADER_FOOTER_FONT.getSize()) + 1,
                                          PageSize.A4.getBottom() + PAGE_MARGIN / 2 + DrawTextUtil.getStringHeight((int) HEADER_FOOTER_FONT.getSize()) / 2,
                                          0,
                                          Element.ALIGN_CENTER);
            strColumnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为页脚添加页码
     *
     *
     * @param canvas 画笔
     * @param pageNumber 当前页页码
     */
    private void addFooterPageNumber(PdfContentByte canvas, int pageNumber) {
        try {
            ColumnText strColumnText    = new ColumnText(canvas);
            Phrase     pageNumberPhrase = new Phrase();
            Chunk      pageNumberChunk  = new Chunk("-" + pageNumber + "-", HEADER_FOOTER_FONT);

            pageNumberPhrase.add(pageNumberChunk);
            strColumnText.setSimpleColumn(pageNumberPhrase,
                                          PageSize.A4.getRight() - PAGE_MARGIN
                                          - FontUtils.baseFontChinese.getWidthPoint("  -" + pageNumber + "-  ",
                                                                                    HEADER_FOOTER_FONT.getSize()),
                                          PageSize.A4.getBottom() + PAGE_MARGIN / 2 - DrawTextUtil.getStringHeight((int) HEADER_FOOTER_FONT.getSize()) / 2,
                                          PageSize.A4.getRight() - PAGE_MARGIN,
                                          PageSize.A4.getBottom() + PAGE_MARGIN / 2 + DrawTextUtil.getStringHeight((int) HEADER_FOOTER_FONT.getSize()) / 2,
                                          0,
                                          Element.ALIGN_CENTER);
            strColumnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为页脚添加一个分割线
     *
     *
     * @param canvas 画笔
     */
    private void addFooterSeparateLine(PdfContentByte canvas) {
        try {
            canvas.saveState();
            canvas.setLineWidth(0);
            canvas.setColorStroke(SEPARATE_LINE_BACKGROUND_COLOR);
            canvas.setColorFill(SEPARATE_LINE_BACKGROUND_COLOR);
            canvas.rectangle(PageSize.A4.getLeft() + PAGE_MARGIN,
                             PageSize.A4.getBottom() + PAGE_MARGIN - SEPARATE_LINE_WIDTH,
                             PageSize.A4.getWidth() - PAGE_MARGIN * 2,
                             SEPARATE_LINE_WIDTH);
            canvas.fillStroke();
            canvas.restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加页眉
     *
     *
     * @param writer pdfWriter 对象，用于取出PdfContentByte对象并提供画图功能
     * @param document document文档对象
     */
    private void addHeader(PdfWriter writer, Document document) {
        try {
            PdfContentByte canvas = writer.getDirectContent();

            addHeaderName(canvas);
            addHeaderIcon(canvas);
            addHeaderSeparateLine(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *为页眉添加图标
     *
     *
     * @param canvas 画笔
     */
    private void addHeaderIcon(PdfContentByte canvas){
        try {
            if(StringUtils.isEmpty(headerLogo)){
                return;
            }

            Image image                =
                Image.getInstance(ImageResourceLoader.getSingleInstance().loadImage(headerLogo));
            float primitiveImageWidth  = image.getWidth();
            float primitiveImageHeight = image.getHeight();
            float imageWHRatio         = primitiveImageWidth / primitiveImageHeight;
            float imageWidth           = image.getWidth();
            float imageHeight          = image.getHeight();

            imageHeight = (imageHeight > DEFAULT_MAX_HEADER_ICON_HEIGHT)
                          ? DEFAULT_MAX_HEADER_ICON_HEIGHT
                          : imageHeight;
            imageWidth  = (imageWidth > imageHeight * imageWHRatio)
                          ? imageHeight * imageWHRatio
                          : imageWidth;
            image.scaleAbsolute(imageWidth, imageHeight);

            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(PageSize.A4.getRight() - PAGE_MARGIN - imageWidth,
                                       PageSize.A4.getTop() - PAGE_MARGIN,
                                       PageSize.A4.getRight() - PAGE_MARGIN,
                                       PageSize.A4.getTop() - PAGE_MARGIN + imageHeight);
            columnText.addElement(image);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为页眉添加header
     *
     *
     * @param canvas 画笔
     */
    private void addHeaderName(PdfContentByte canvas) {
        try {
            if(pageHeaderParts == null || pageHeaderParts.isEmpty()){
                return;
            }

            ColumnText strColumnText    = new ColumnText(canvas);
            Phrase     headerNamePhrase = PdfConvertUtil.getHeaderPhrase(pageHeaderParts, HEADER_FOOTER_FONT);

            strColumnText.setSimpleColumn(headerNamePhrase,
                                          PageSize.A4.getLeft() + PAGE_MARGIN,
                                          PageSize.A4.getTop() - PAGE_MARGIN,
                                          PageSize.A4.getLeft() + PAGE_MARGIN
                                          + PdfConvertUtil.getPageHeaderWidth(pageHeaderParts,
                                                                              HEADER_FOOTER_FONT.getSize()),
                                          PageSize.A4.getTop() - PAGE_MARGIN + DrawTextUtil.getStringHeight((int) HEADER_FOOTER_FONT.getSize()),
                                          0,
                                          Element.ALIGN_CENTER);
            strColumnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为页眉添加分割线
     *
     *
     * @param canvas 画笔
     */
    private void addHeaderSeparateLine(PdfContentByte canvas) {
        try {
            canvas.saveState();
            canvas.setLineWidth(0);
            canvas.setColorStroke(SEPARATE_LINE_BACKGROUND_COLOR);
            canvas.setColorFill(SEPARATE_LINE_BACKGROUND_COLOR);
            canvas.rectangle(PageSize.A4.getLeft() + PAGE_MARGIN,
                             PageSize.A4.getTop() - PAGE_MARGIN,
                             PageSize.A4.getWidth() - PAGE_MARGIN * 2,
                             SEPARATE_LINE_WIDTH);
            canvas.fillStroke();
            canvas.restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为页面添加水印
     *
     *
     * @param writer PdfWriter对象，用于取出PdfContentByte对象并提供画图功能
     * @param document document对象用于测量页面高度
     */
    private void addWatermark(PdfWriter writer, Document document) {
        try {
            if(StringUtils.isEmpty(waterMark)){
                return;
            }

            Phrase         watermark = new Phrase(waterMark, DEFAULT_WATERMARK_FONT);
            PdfContentByte canvas    = writer.getDirectContent();
            float          height    = document.top() - document.bottom();

            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, height / 2, 45);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            if ((document.getPageNumber() != 1) &&!isLastPage) {
                addHeader(writer, document);
                addWatermark(writer, document);
                addFooter(writer, document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
