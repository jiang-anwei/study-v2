package com.icekredit.pdf.entities;

import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.Md5Util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by icekredit on 7/24/16.
 *
 * 目录单元格
 */
public class TOCCell extends BaseCell implements PdfPCellEvent{
    protected List<String> tocItems;
    private static final int TOC_ITEM_FONT_SIZE_MIN = 5;
    private static final int TOC_ITEM_FONT_SIZE_MAX = 7;
    private static final int MARGIN_LEFT = 2;

    private static final int MARGIN_RIGHT = 2;
    private static final int PADDING_LEFT = 2;

    private static final int PADDING_RIGHT = 2;
    protected BaseColor backgroundColor;

    private static final BaseColor DEFAULT_BACKGROUND_COLOR  =new BaseColor(0x66,0xc6,0xfc,0xff);

    private static final int DEFAULT_VISIABLE_CELL_HEIGHT = 14;

    public TOCCell(List<String> tocItems) {
        this(tocItems,DEFAULT_BACKGROUND_COLOR);
    }

    public TOCCell(List<String> tocItems,BaseColor backgroundColor) {
        this.tocItems = tocItems;
        this.backgroundColor = backgroundColor;

        this.setFixedHeight(DEFAULT_VISIABLE_CELL_HEIGHT);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        int currentFontSize = TOC_ITEM_FONT_SIZE_MAX;

        float spaceWidthAvailable = position.getWidth();

        //得到一个合适的最大字体
        float spaceRemaining = 0;
        while (currentFontSize >= TOC_ITEM_FONT_SIZE_MIN){
            spaceRemaining = getSpaceRemaining(spaceWidthAvailable,currentFontSize);

            if(spaceRemaining != -1){
                break;
            }

            currentFontSize --;
        }

        float extraPadding = 0;
        if(spaceRemaining == -1){
            Debug.debug("Sorry! it seem that there is not have enough space to place the whole TOC!");
        } else {
            extraPadding = spaceRemaining / tocItems.size() / 2;
        }

        Font tocItemFont = new Font(FontUtils.baseFontChinese,currentFontSize,Font.NORMAL,new BaseColor(0xff,0xff,0xff,0xff));
        float tocItemHeight = FontUtils.baseFontChinese.getWidthPoint("中文",currentFontSize) / 2;

        float spaceWidthConsumed = 0;
        for (int index = 0;index < tocItems.size();index ++){
            if(index != 0){
                spaceWidthConsumed += MARGIN_LEFT;
            }

            spaceWidthConsumed += extraPadding + PADDING_LEFT + FontUtils.baseFontChinese.getWidthPoint(tocItems.get(index),currentFontSize) + PADDING_RIGHT + extraPadding;

            if(spaceWidthConsumed <= spaceWidthAvailable + 0.1){
                drawBackground(canvases[PdfPTable.BASECANVAS],new Rectangle(
                        position.getLeft() + spaceWidthConsumed -
                                (extraPadding + PADDING_LEFT + FontUtils.baseFontChinese.getWidthPoint(tocItems.get(index),currentFontSize) + PADDING_RIGHT + extraPadding),
                        position.getBottom(),
                        position.getLeft() + spaceWidthConsumed,
                        position.getTop()
                ));

                drawTOCItem(canvases[PdfPTable.BASECANVAS],tocItems.get(index),new Rectangle(
                        position.getLeft() + spaceWidthConsumed -
                                (extraPadding + PADDING_LEFT + FontUtils.baseFontChinese.getWidthPoint(tocItems.get(index),currentFontSize) + PADDING_RIGHT + extraPadding),
                        (position.getBottom() + position.getTop()) / 2 - tocItemHeight / 2,
                        position.getLeft() + spaceWidthConsumed,
                        (position.getBottom() + position.getTop()) / 2 + tocItemHeight / 2
                ),tocItemFont);
            }

            if(index != tocItems.size() - 1){
                spaceWidthConsumed += MARGIN_RIGHT;
            }
        }
    }

    private void drawBackground(PdfContentByte canvas, Rectangle rectangle) {
        canvas.saveState();

        canvas.setLineWidth(0);
        canvas.setColorStroke(backgroundColor);
        canvas.setColorFill(backgroundColor);
        canvas.rectangle(rectangle.getLeft(),
                rectangle.getBottom(),
                rectangle.getWidth(),
                rectangle.getHeight());
        canvas.fillStroke();

        canvas.restoreState();
    }

    private void drawTOCItem(PdfContentByte canvas, String tocItemStr, Rectangle position, Font tocItemFont) {
        try {
            ColumnText columnText = new ColumnText(canvas);

            Chunk tocItemChunk = new Chunk(tocItemStr,tocItemFont);
            tocItemChunk.setLocalGoto(Md5Util.MD5(tocItemStr));

            columnText.setSimpleColumn(new Phrase(tocItemChunk),
                    position.getLeft(),
                    position.getBottom(),
                    position.getRight(),
                    position.getBottom() + position.getHeight() / 8,
                    0, ALIGN_CENTER);

            columnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取画完整个目录后剩余的空间，如果不够的话就直接丢弃。
     *
     * @return
     */
    private float getSpaceRemaining(float spaceAvailableWidth,float currentFontSize) {
        float spaceConsumed = 0;
        for(int index = 0;index < tocItems.size();index ++){
            if(index != 0){
                spaceConsumed += MARGIN_LEFT;
            }

            spaceConsumed += PADDING_LEFT + FontUtils.baseFontChinese.getWidthPoint(tocItems.get(index),currentFontSize) + PADDING_RIGHT;

            if(index != tocItems.size() - 1){
                spaceConsumed += MARGIN_RIGHT;
            }
        }


        return (spaceConsumed > spaceAvailableWidth) ? -1 : (spaceAvailableWidth - spaceConsumed);
    }

    public static void main(String [] args){
        String destFileStr = "result/test.pdf";
        File destFile = new File(destFileStr);
        destFile.getParentFile().mkdirs();

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            String [] tocItems = new String[]{"基本信息及摘要" ,
                    "风险分析" ,
                    "工商信息" ,
                    "关联信息" ,
                    "合规情况" ,
                    "无形资产" ,
                    "无形资产" ,
                    "无形资产" ,
                    "无形资产" ,
                    "无形资产" ,
                    "公共信息" ,
                    "行业分析" ,
                    "附录" ,
                    "声明"};
            
            TOCCell headerCell = new TOCCell(Arrays.asList(tocItems),new BaseColor(0x66,0xc6,0xfc,0xff));
            headerCell.setColspan(12);
            mainFrame.addCell(headerCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
