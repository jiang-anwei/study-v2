package com.icekredit.pdf.outline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icekredit.pdf.BasePart;
import com.icekredit.pdf.ICKPdfPart;
import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.Md5Util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Pdf大纲字模块
 * 本模块主要完成Pdf大纲模块的设计以及实现，基本思路，在页面添加一二三级标题的时候，系统会记录一二三级
 * 标题添加的次序，然后利用这个次序生成大纲。基本原则，每一个来的下一级标签都从属与当前标签，每一个到来的同等
 * 级别的或者上一级别的标签标签都会建立一个新的书签分支，如此往复，知道缓存的所有标签顺序被消费完
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class OutlinePart extends BasePart implements ICKPdfPart {

    /**
     * 大纲名称
     */
    public static final String OUTLINE_NAME = "outline_name";

    /**
     * 大纲所指向的页面anchor或者说是Itext pdf 里面的 LocalDestination
     */
    public static final String OUTLINE_GOTO = "outline_goto";

    /**
     * 当前系统中所有的大纲模块，key为执行当前pdf生成的线程Id，值为当前pdf的大纲信息
     */
    private static Map<String, OutlinePart> outlineParts;

    /**
     * 一级大纲，其是一个一维的列表
     */
    public List<Map<String, String>> levelOneOutlines;

    /**
     * 二级大纲其是一个二维的列表
     */
    public List<List<Map<String, String>>> levelTwoOutlines;

    /**
     * 三级大纲，其是一个三维的列表
     */
    public List<List<List<Map<String, String>>>> levelThreeOutlines;

    /**
     * 构造函数
     */
    public OutlinePart() {
        this(new ArrayList<Map<String, String>>(),
                new ArrayList<List<Map<String, String>>>(),
                new ArrayList<List<List<Map<String, String>>>>());
    }

    /**
     * 带参数的构造函数
     *
     * @param levelOneOutlines   一级大纲
     * @param levelTwoOutlines   二级大纲
     * @param levelThreeOutlines 三级大纲
     */
    public OutlinePart(List<Map<String, String>> levelOneOutlines, List<List<Map<String, String>>> levelTwoOutlines,
                       List<List<List<Map<String, String>>>> levelThreeOutlines) {
        this.levelOneOutlines = levelOneOutlines;
        this.levelTwoOutlines = levelTwoOutlines;
        this.levelThreeOutlines = levelThreeOutlines;
    }

    /**
     * 向当前系统所有大纲模块Map中添加一个大纲模块
     *
     * @param threadId    线程Id
     * @param outlinePart threadId指定的线程创建的Pdf的大纲信息
     */
    public static void addOutlinePart(String threadId, OutlinePart outlinePart) {
        if (outlineParts == null) {
            outlineParts = new HashMap<String, OutlinePart>();
        }

        outlineParts.put(threadId, outlinePart);
    }

    /**
     * 向当前大纲中添加一个一级大纲标题
     *
     * @param outlineName 大纲名称
     * @param outlineGoto 大纲指向的页面anchor或者说itext localDestination
     */
    public void appendLevelOneOutlines(String outlineName, String outlineGoto) {
        try {
            Map<String, String> outlineItem = new HashMap<String, String>();

            outlineItem.put(OUTLINE_NAME, outlineName);
            outlineItem.put(OUTLINE_GOTO, outlineGoto);

            // 为一级大纲添加一个条目
            levelOneOutlines.add(levelOneOutlines.size(), outlineItem);

            // 为一级大纲分配一个用于存储二级大纲的容器
            levelTwoOutlines.add(new ArrayList<Map<String, String>>());
            levelThreeOutlines.add(new ArrayList<List<Map<String, String>>>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向当前系统中添加一个三级大纲信息
     *
     * @param outlineName 大纲名称
     * @param outlineGoto 大纲指向的页面anchor或者说itext localDestination
     */
    public void appendLevelThreeOutlines(String outlineName, String outlineGoto) {
        try {
            Map<String, String> outlineItem = new HashMap<String, String>();

            outlineItem.put(OUTLINE_NAME, outlineName);
            outlineItem.put(OUTLINE_GOTO, outlineGoto);

            // 为三级大纲添加一个条目
            levelThreeOutlines.get(levelOneOutlines.size() - 1)
                    .get(levelTwoOutlines.get(levelOneOutlines.size() - 1).size() - 1)
                    .add(outlineItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向系统中添加一个二级大纲信息
     *
     * @param outlineName 大纲名称
     * @param outlineGoto 大纲指向的页面anchor或者说itext localDestination
     */
    public void appendLevelTwoOutlines(String outlineName, String outlineGoto) {
        try {
            Map<String, String> outlineItem = new HashMap<String, String>();

            outlineItem.put(OUTLINE_NAME, outlineName);
            outlineItem.put(OUTLINE_GOTO, outlineGoto);

            // 为二级大纲添加一个条目
            levelTwoOutlines.get(levelOneOutlines.size() - 1).add(outlineItem);

            // 为二级大纲分配一个用于存储三级大纲的容器
            levelThreeOutlines.get(levelOneOutlines.size() - 1).add(new ArrayList<Map<String, String>>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化函数，初始化每一级大纲的存储结构
     */
    public void initialize() {
        if ((this.levelOneOutlines.size() != 0)
                || (this.levelTwoOutlines.size() != 0)
                || (this.levelThreeOutlines.size() != 0)) {
            return;
        }

        this.levelOneOutlines = new ArrayList<Map<String, String>>();
        this.levelTwoOutlines = new ArrayList<List<Map<String, String>>>();
        this.levelThreeOutlines = new ArrayList<List<List<Map<String, String>>>>();
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            OutlinePart outlinePart = new OutlinePart();
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

//            pdfWriter.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            document.open();
            outlinePart.appendLevelOneOutlines("一级标题", "一级标题");
            outlinePart.appendLevelOneOutlines("一级标题1", "一级标题1");
            outlinePart.write(pdfWriter, null);
            document.add(new Paragraph(new Phrase(new Chunk("neirong111111111111111", FontUtils.chineseFont).setLocalDestination(Md5Util.MD5("一级标题")))));
            document.newPage();
            document.add(new Paragraph(new Phrase(new Chunk("neirong333333333333333", FontUtils.chineseFont).setLocalDestination(Md5Util.MD5("一级标题1")))));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从当前系统所有大纲信息中移除指定线程Id对应的大纲信息
     *
     * @param threadId 指定线程Id
     */
    public static void removeOutlinePart(String threadId) {
        if ((outlineParts == null) || (outlineParts.size() == 0)) {
            return;
        }

        if (outlineParts.containsKey(threadId)) {
            outlineParts.remove(threadId);
        }
    }

    @Override
    public void write(PdfWriter pdfWriter, PdfPTable mainFrame) throws IOException, DocumentException {
        try {
            PdfOutline rootOutline = pdfWriter.getRootOutline();

            rootOutline.setOpen(false);

            PdfOutline currentLevelOneOutline;
            PdfOutline currentLevelTwoOutline;
            PdfOutline currentLevelThreeOutline;

            /*
             * PdfOutline titleOutLine = new PdfOutline(rootOutline,
             *       PdfAction.gotoLocalPage(new String("量化分析报告"),false),
             *       "量化分析报告");
             */
            for (int levelOneIndex = 0; levelOneIndex < levelOneOutlines.size(); levelOneIndex++) {
                currentLevelOneOutline = new PdfOutline(rootOutline,
                        PdfAction.gotoLocalPage(
                                Md5Util.MD5(levelOneOutlines.get(levelOneIndex).get(OUTLINE_GOTO)),false),
                        levelOneOutlines.get(levelOneIndex).get(OUTLINE_NAME));

                for (int levelTwoIndex = 0; levelTwoIndex < levelTwoOutlines.get(levelOneIndex).size();
                     levelTwoIndex++) {
                    currentLevelTwoOutline = new PdfOutline(currentLevelOneOutline,
                            PdfAction.gotoLocalPage(
                                    Md5Util.MD5(levelTwoOutlines.get(levelOneIndex).get(levelTwoIndex).get(OUTLINE_GOTO)),false),
                            levelTwoOutlines.get(levelOneIndex).get(levelTwoIndex).get(
                                    OUTLINE_NAME));

                    for (int levelThreeIndex = 0;
                         levelThreeIndex < levelThreeOutlines.get(levelOneIndex).get(levelTwoIndex).size();
                         levelThreeIndex++) {
                        currentLevelThreeOutline = new PdfOutline(currentLevelTwoOutline,
                                PdfAction.gotoLocalPage(
                                        Md5Util.MD5(levelThreeOutlines.get(levelOneIndex).get(levelTwoIndex).get(levelThreeIndex).get(OUTLINE_GOTO)),false),
                                levelThreeOutlines.get(levelOneIndex).get(
                                        levelTwoIndex).get(levelThreeIndex).get(
                                        OUTLINE_NAME));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据线程id从当前系统中保存的所有大纲信息模块Map中取出指定大纲信息
     *
     *
     * @param threadId 执行线程Id
     * @return 指定线程id对应的大纲信息
     */
    public static OutlinePart getOutlinePart(String threadId) {
        if (outlineParts == null) {
            outlineParts = new HashMap<String, OutlinePart>();
        }

        return outlineParts.get(threadId);
    }

    /**
     * 所有大纲map outlineParts属性的Getter
     *
     * @return  所有大纲map outlineParts属性
     */
    public static Map<String, OutlinePart> getOutlineParts() {
        return outlineParts;
    }

    /**
     * 所有大纲map outlineParts属性的 setter
     *
     * @param outlineParts 需要设置的outlineParts属性
     */
    public static void setOutlineParts(Map<String, OutlinePart> outlineParts) {
        OutlinePart.outlineParts = outlineParts;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
