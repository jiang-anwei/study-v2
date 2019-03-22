package com.icekredit.pdf.utils;

import java.io.*;

import java.util.*;
import java.util.List;

import com.icekredit.pdf.cover.ForgeCoverPart;
import com.icekredit.pdf.entities.*;
import com.icekredit.pdf.entities.core.MessageConfig;
import com.icekredit.pdf.entities.header.LevelOneHeaderCell;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import net.sf.json.JSONArray;

/**
 * CLASS_DESCRIPTION
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class PdfConvertUtil {

    /**
     * 当报告数据比较多时，只展示部分数据，并且以脚注的形式展示这条描述
     */
    public static final String DEFAULT_FOOTER_NOTICE = "* 此信息较多，为保证报告的可读性，此处显示10条，详情请前往网页端查看";

    /**
     * 当报告中没有指定数据时，展示当前条目标题，并且以脚注的形式展示此条信息
     */
    public static final String NO_DATA_NOTICE = "截至yyyy年M月d号，在本次调查中冰鉴数据中心通过查询对应数据源均未能发现该公司的相关记录。";

    /**
     * 系统异常提示语句
     */
    public static final String NOTICE_FOR_SYSTEM_ABNORMAL = "系统异常，请联系工作人员。";

    /**
     * 为页面添加高度为15的空白空间
     *
     * @param mainFrame 页面主框架
     */
    public static void addEmptySpace(PdfPTable mainFrame) {
        EmptyCell emptyCell = new EmptyCell(12);

        emptyCell.setFixedHeight(15);
        mainFrame.addCell(emptyCell);
    }

    /**
     * 为页面添加一个默认的脚注DEFAULT_FOOTER_NOTICE
     *
     * @param mainFrame 页面主框架
     */
    public static void addFooterNotice(PdfPTable mainFrame) {
        addFooterNotice(mainFrame, DEFAULT_FOOTER_NOTICE);
    }

    /**
     * 为页面添加指定脚注
     *
     * @param mainFrame    页面主框架
     * @param footerNotice 脚注信息字符串
     */
    public static void addFooterNotice(PdfPTable mainFrame, String footerNotice) {
        try {
            AlignLeftCell footerNoticeCell = new AlignLeftCell(new Phrase(new Chunk(footerNotice,
                    new Font(FontUtils.baseFontChinese,
                            FontUtils.fontSize,
                            FontUtils.fontStyle,
                            new BaseColor(0x77,0x77,0x77,0xff)))));

            footerNoticeCell.setBorder(PdfPCell.NO_BORDER);
            footerNoticeCell.setColspan(12);
            mainFrame.addCell(footerNoticeCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 无数据时显示描述单元格
     *
     * @param mainFrame 页面主框架
     * @param noDataNotice 无数据时显示描述信息
     */
    public static void addNoDataCell(PdfPTable mainFrame, String noDataNotice) {
        try {
            ContentMultiDescCell noDataCell = new ContentMultiDescCell(Arrays.asList(noDataNotice),
                    FontUtils.fontSize,
                    FontUtils.fontStyle,
                    ColorUtil.strRGBAToColor(
                            ColorUtil.WARNING_DESC_COLOR));

            noDataCell.setColspan(12);
            mainFrame.addCell(noDataCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 无数据时现显示默认无数据描述信息
     *
     * @param mainFrame 页面主框架
     */
    public static void addNoDataNotice(PdfPTable mainFrame) {
        addNoDataNotice(mainFrame, NO_DATA_NOTICE);
    }

    /**
     * 无数据时现显示指定无数据描述信息
     *
     * @param mainFrame    页面主框架
     * @param noDataNotice 特定的无数据描述信息
     */
    public static void addNoDataNotice(PdfPTable mainFrame, String noDataNotice) {
        try {
            String dateNotice = DateUtil.format(noDataNotice,new Date());

            dateNotice = dateNotice == null ? "" : dateNotice;

            AlignLeftCell noDataNoticeCell = new AlignLeftCell(new Phrase(new Chunk(dateNotice,
                    new Font(FontUtils.baseFontChinese,
                            FontUtils.fontSize,
                            FontUtils.fontStyle,
                            new BaseColor(0x77,0x77,0x77,0xff)))));

            noDataNoticeCell.setBorder(PdfPCell.NO_BORDER);
            noDataNoticeCell.setColspan(12);
            mainFrame.addCell(noDataNoticeCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向页面主框架中添加一个无法评估Header
     *
     * @param mainFrame  PARAM_DESC
     * @param headerName PARAM_DESC
     * @param result     PARAM_DESC
     * @param reason     PARAM_DESC
     */
    public static void addUnEvaluatableCell(PdfPTable mainFrame, String headerName, String result, String reason) {
        LevelOneHeaderCell headerCell = new LevelOneHeaderCell(headerName,
                result,
                MessageConfig.IDENTIFIER_TYPE_NONE,
                "",
                MessageConfig.IDENTIFIER_TYPE_NONE,
                reason);

        headerCell.setColspan(12);
        headerCell.setHeaderDescColor(ColorUtil.strRGBAToColor("0xca4223ff"));
        headerCell.setHeaderNoteColor(ColorUtil.strRGBAToColor("0xcc6d67ff"));
        mainFrame.addCell(headerCell);
    }

    /**
     * 将一个map按照知道key值集合转换为列表
     *
     * @param map  带转换的map对象
     * @param keys 指定Key值集合
     *
     * @return 转换后的列表对象
     */
    public static List<String> mapToList(Map<String, Object> map, List<String> keys) {
        List<String> list = new ArrayList<String>();

        for (String key : keys) {
            list.add((String) map.get(key));
        }

        return list;
    }

    /**
     * 合并多个pdf流到目标pdf流中
     *
     * @param parts        各个子模块pdf列表，基本是读取或者生成的pdf中间缓存数据，格式为ByteArrayOutputStream
     * @param outputStream 目标pdf本地文件输出流或者网络response输出流
     * @throws DocumentException PdfCopy对象关联当前文档到目标输出流时可能发生的DocumentException
     */
    public static void mergePdfs(ByteArrayOutputStream[] parts, OutputStream outputStream) throws DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, outputStream);

        document.open();

        try {
            for (ByteArrayOutputStream part : parts) {
                PdfReader partReader = new PdfReader(new ByteArrayInputStream(part.toByteArray()));

                copy.addDocument(partReader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.close();
    }

    /**
     * 合并多个pdf文件到目标pdf文件中
     *
     * @param pdfFilesStrToMerge 待合并的pdf文件路径
     * @param destFileStr        目标pdf文件路径
     * @throws Exception PdfCopy对象关联当前文档到目标输出流时可能发生的Exception，文件不存在时的FileNotFoundException
     */
    public static void mergePdfs(String[] pdfFilesStrToMerge, String destFileStr) throws Exception {
        for (String pdfFileStrToMerge : pdfFilesStrToMerge) {
            if (!new File(pdfFileStrToMerge).exists()) {
                throw new FileNotFoundException("抱歉用于合并的pdf源文件" + pdfFileStrToMerge + "不存在");
            }
        }

        File destFile = new File(destFileStr);

        destFile.getParentFile().mkdirs();

        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(destFile));

        document.open();

        for (String pdfFileStrToMerge : pdfFilesStrToMerge) {
            PdfReader pdfPartReader = new PdfReader(pdfFileStrToMerge);

            copy.addDocument(pdfPartReader);
            pdfPartReader.close();
        }

        document.close();
    }

    /**
     * 合并两个pdf文件到目标pdf文件
     *
     * @param firstPdfFileStr 第一个待合并的pdf文件
     * @param secondPdfFileStr 第二个待合并的pdf文件
     * @param destFileStr 目标pdf文件
     * @throws Exception PdfCopy对象关联当前文档到目标输出流时可能发生的DocumentException ,文件不存在时的FileNotFoundException
     */
    public static void mergePdfs(String firstPdfFileStr, String secondPdfFileStr, String destFileStr)
            throws Exception {
        if (!new File(firstPdfFileStr).exists()) {
            throw new FileNotFoundException("抱歉用于合并的pdf源文件" + firstPdfFileStr + "不存在");
        }

        if (!new File(secondPdfFileStr).exists()) {
            throw new FileNotFoundException("抱歉用于合并的pdf源文件" + secondPdfFileStr + "不存在");
        }

        File destFile = new File(destFileStr);

        destFile.getParentFile().mkdirs();

        PdfReader cover = new PdfReader(firstPdfFileStr);
        PdfReader reader = new PdfReader(secondPdfFileStr);
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(destFile));

        document.open();
        copy.addDocument(cover);
        copy.addDocument(reader);
        document.close();
        cover.close();
        reader.close();
    }

    /**
     * 根据传递过来的描述信息，得到分数标准图的评论信息
     *
     * @param scopeJsonObjs 由模型定义的分数段信息
     *
     * @return 从分数段信息中提取的分数段评论信息
     */
    public static String[] getCommentsArray(JSONArray scopeJsonObjs) {
        List<String> comments = JSONAttrGetter.getAll(scopeJsonObjs, "comment");
        String[] commentsArray = new String[comments.size()];

        for (int index = 0; index < comments.size(); index++) {
            commentsArray[index] = comments.get(index);
        }

        return commentsArray;
    }

    /**
     * 根据当前货币单位得到转换后的货币单位
     *
     * @param currency 当前货币单位
     * @return 转换后的货币单位
     */
    public static String getCurrency(String currency) {
        if (currency.indexOf("美元") != -1) {
            return "万美元";
        }

        return "";
    }

    /**
     * 根据制订header的各个部分得到一个Phrase对象
     *
     * @param pageHeaderParts 用来描述一个页眉标题各个部分的数据
     * @param strFont         用来画页眉的字体
     * @return 组装后的Phrase对象
     */
    public static Phrase getHeaderPhrase(List<Map<String, Object>> pageHeaderParts, Font strFont) {
        Phrase nameExtraPhrase = new Phrase();
        Chunk chunk = null;

        for (Map<String, Object> pageHeaderPart : pageHeaderParts) {
            switch ((int) pageHeaderPart.get(ForgeCoverPart.PART_TYPE)) {
                case ForgeCoverPart.PART_TYPE_NORMAL:
                    chunk = new Chunk((String) pageHeaderPart.get(ForgeCoverPart.PART_DESC), strFont);
                    nameExtraPhrase.add(chunk);
                    break;

                case ForgeCoverPart.PART_TYPE_SUBSCRIPT:
                    chunk = new Chunk((String) pageHeaderPart.get(ForgeCoverPart.PART_DESC),
                            new Font(FontUtils.baseFontChinese,
                                    (int) ((strFont.getSize() + 1) / 2),
                                    Font.NORMAL,
                                    strFont.getColor()));
                    chunk.setTextRise(-((int) (strFont.getSize() / 2)));
                    nameExtraPhrase.add(chunk);

                    break;

                case ForgeCoverPart.PART_TYPE_SUPERSCRIPT:
                    chunk = new Chunk((String) pageHeaderPart.get(ForgeCoverPart.PART_DESC),
                            new Font(FontUtils.baseFontChinese,
                                    (int) ((strFont.getSize() + 1) / 2),
                                    Font.NORMAL,
                                    strFont.getColor()));
                    chunk.setTextRise((int) (strFont.getSize() / 2));
                    nameExtraPhrase.add(chunk);

                    break;
            }
        }

        return nameExtraPhrase;
    }

    /**
     * 计算字符串数组中最长字符串占据的页面宽度
     *
     * @param names 目标字符串数组
     * @return 字符串数组中最长字符串占据的页面宽度
     */
    public static float getMaxNameWidth(String[] names) {
        float maxNameWidth = -Float.MAX_VALUE;

        for (String name : names) {
            float currentNameWidth = FontUtils.baseFontChinese.getWidthPoint(name, FontUtils.fontSize);

            maxNameWidth = Math.max(maxNameWidth, currentNameWidth);
        }

        return maxNameWidth;
    }

    /**
     * 根据坐标轴上面显示的最大值来确定坐标轴的刻度大小
     *
     * @param maxScaleToShow 坐标轴所要保存的最大值
     * @return 坐标轴刻度大小
     */
    public static int getNormalizeScale(float maxScaleToShow) {
        int normalizeScale = 0;

        if ((maxScaleToShow > 0) && (maxScaleToShow <= 5)) {
            normalizeScale = 1;
        } else if (maxScaleToShow <= 10) {
            normalizeScale = 2;
        } else if (maxScaleToShow <= 50) {
            normalizeScale = 10;
        } else if (maxScaleToShow <= 100) {
            normalizeScale = 20;
        } else if (maxScaleToShow <= 250) {
            normalizeScale = 50;
        } else if (maxScaleToShow <= 500) {
            normalizeScale = 100;
        } else if (maxScaleToShow <= 2500) {
            normalizeScale = 500;
        } else if (maxScaleToShow <= 5000) {
            normalizeScale = 1000;
        } else if (maxScaleToShow <= 25000) {
            normalizeScale = 5000;
        } else if (maxScaleToShow <= 50000) {
            normalizeScale = 10000;
        } else if (maxScaleToShow <= 250000) {
            normalizeScale = 50000;
        } else if (maxScaleToShow <= 500000) {
            normalizeScale = 100000;
        } else if (maxScaleToShow <= 2500000) {
            normalizeScale = 500000;
        } else if (maxScaleToShow <= 5000000) {
            normalizeScale = 1000000;
        } else if (maxScaleToShow <= 2500000) {
            normalizeScale = 5000000;
        } else if (maxScaleToShow <= 5000000) {
            normalizeScale = 10000000;
        } else {
            normalizeScale = (int) Math.ceil(maxScaleToShow  * 1.0 / 5);
        }

        return normalizeScale;
    }

    /**
     * 根据页眉header的各个部分计算出也没header需要的宽度，用于页眉header布局（左对齐，或者右对齐）
     *
     * @param pageHeaderParts 页眉header的各个部分
     * @param fontSize        页眉的字体大小
     *
     * @return 勾画整个页眉需要的宽度
     */
    public static float getPageHeaderWidth(List<Map<String, Object>> pageHeaderParts, float fontSize) {
        float pageHeaderWidth = 0;

        for (Map<String, Object> pageHeaderPart : pageHeaderParts) {
            pageHeaderWidth +=
                    FontUtils.baseFontChinese.getWidthPoint((String) pageHeaderPart.get(ForgeCoverPart.PART_DESC),
                            fontSize);
        }

        return pageHeaderWidth;
    }

    /**
     *将最大显示数值 按照指定刻度规模 向上上取整到制定刻度规模的整数倍
     *
     * @param maxPercentageToShow 当前最大显示数值
     * @return 向上取整后的最大显示数值
     */
    public static float getProperMaxPercentageToShow(float maxPercentageToShow) {
        int normalizeScale = PdfConvertUtil.getNormalizeScale(maxPercentageToShow);

        return (int) ((Math.round(maxPercentageToShow * 1.0f / normalizeScale + 0.5)) * normalizeScale);
    }

    /**
     * 计算显示的级别，比如显示为5级或者十级
     *
     * @param normalizeScale 显示的刻度
     * @param maxShownScale  最大显示数值
     * @return 显示的级别
     */
    public static int getProperShowLevel(int normalizeScale, int maxShownScale) {
        return (int) Math.ceil(maxShownScale / normalizeScale);
    }

    /**
     * 根据传递过来的范围描述信息，得到分数标准图的分数段
     *
     * @param scopeJsonObjs 分数段描述信息
     *
     * @return 分数段信息
     */
    public static int[] getRangePointsIntInts(JSONArray scopeJsonObjs) {
        List<String> startPoints = JSONAttrGetter.getAll(scopeJsonObjs, "start");
        List<String> endPoints = JSONAttrGetter.getAll(scopeJsonObjs, "end");

        for (String endPoint : endPoints) {
            if (startPoints.contains(endPoint)) {
                continue;
            }

            startPoints.add(endPoint);
        }

        Collections.sort(startPoints,new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });

        int[] rangePointsInt = new int[startPoints.size()];
        int index = 0;

        for (String startPoint : startPoints) {
            rangePointsInt[index] = Integer.parseInt(startPoint);
            index++;
        }

        return rangePointsInt;
    }

    /**
     * 根据传递过来的分数，然后根据默认的分数段以及分数段对应的评论信息，得到当前分数对应的分数评论
     *
     * @param score 当前分数
     *
     * @return 分数评论信息如 710&lt;score&lt;850 返回极佳
     */
    public static String getScoreComment(int score) {
        List<String> comments = Arrays.asList(new String[]{
                "高危", "差", "一般", "良好", "优秀", "极佳"
        });
        int[] scoresStandard = new int[]{
                300, 400, 530, 630, 680, 710, 850
        };

        return getScoreComment(score, comments, scoresStandard);
    }

    /**
     * 根据传递过来的分数，然后根据指定的分数段以及指定分数段对应的评论信息，得到当前分数对应的分数评论
     *
     * @param score          当前分数
     * @param comments       指定分数段对应的评论信息
     * @param scoresStandard 指定的分数段
     * @return 分数评论信息如 710&lt;score&lt;850 返回极佳，如果当前分数不在任何一个分数段内，返回 -
     */
    public static String getScoreComment(int score, List<String> comments, int[] scoresStandard) {
        for (int index = 0; index < scoresStandard.length - 1; index++) {
            if (score == 300) {
                return comments.get(0);
            }

            if ((score > scoresStandard[index]) && (score <= scoresStandard[index + 1])) {
                return comments.get(index);
            }
        }

        return "-";
    }

    /**
     * 在指定位置勾画一个圆角矩形背景
     *
     * @param canvas 画笔
     */
    public static void drawRoundCornerBackground(Rectangle rectangle, PdfContentByte canvas,
                                                 BaseColor backgroundPanelColor, float backgroundCornerRadius) {
        canvas.saveState();

        canvas.setLineWidth(0);

        canvas.setColorFill(backgroundPanelColor);
        canvas.setColorStroke(backgroundPanelColor);

        backgroundCornerRadius = (float) Math.min(backgroundCornerRadius,rectangle.getWidth() * 0.1);
        backgroundCornerRadius = (float) Math.min(backgroundCornerRadius,rectangle.getHeight() * 0.1);

        List<com.icekredit.pdf.entities.Point> points = new ArrayList<com.icekredit.pdf.entities.Point>();

        points.addAll(getArcVertices(rectangle.getLeft() + backgroundCornerRadius,
                rectangle.getTop() - backgroundCornerRadius,backgroundCornerRadius,Math.PI / 2 * 1, Math.PI / 2));
        points.addAll(getArcVertices(rectangle.getLeft() + backgroundCornerRadius,
                rectangle.getBottom() + backgroundCornerRadius,backgroundCornerRadius,Math.PI / 2 * 2, Math.PI / 2));
        points.addAll(getArcVertices(rectangle.getRight() - backgroundCornerRadius,
                rectangle.getBottom() + backgroundCornerRadius,backgroundCornerRadius,Math.PI / 2 * 3, Math.PI / 2));
        points.addAll(getArcVertices(rectangle.getRight() - backgroundCornerRadius,
                rectangle.getTop() - backgroundCornerRadius,backgroundCornerRadius,Math.PI / 2 * 0, Math.PI / 2));

        canvas.moveTo(rectangle.getLeft() + backgroundCornerRadius,rectangle.getTop());
        for(com.icekredit.pdf.entities.Point point:points){
            canvas.lineTo(point.x,point.y);
        }
        canvas.closePathFillStroke();

        canvas.restoreState();
    }

    /**
     * 计算出指定圆心位置，指定半径指定开始角度，指定跨越角度所对应的圆弧顶点信息
     *
     * @param centerXPosition 圆心x位置
     * @param centerYPosition 圆心y位置
     * @param backgroundCornerRadius 矩形背景圆角半径
     * @param startAngle 开始角度
     * @param spanAngle 跨越角度
     * @return 计算得到的圆弧顶点信息
     */
    public static List<Point> getArcVertices(float centerXPosition, float centerYPosition,
                                                                        float backgroundCornerRadius, double startAngle, double spanAngle) {
        float stepAngle = (float) (Math.PI / 72);

        List<com.icekredit.pdf.entities.Point> points = new ArrayList<>();

        float currentAngle = (float) startAngle;

        while (currentAngle < startAngle + spanAngle){
            points.add(new com.icekredit.pdf.entities.Point((float)(centerXPosition + backgroundCornerRadius * Math.cos(currentAngle)),
                    (float)(centerYPosition + backgroundCornerRadius * Math.sin(currentAngle))));

            currentAngle += stepAngle;
        }

        return points;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
