package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.RegularExpressionUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by icekredit on 7/27/16.
 */
public class BrandInfoCell extends InformationCell {
    public BrandInfoCell(String infomationHeader, List<String> informationItemsName, List<String> informationItemsValue, float nameSegmentWidth) {
        super(infomationHeader, informationItemsName, informationItemsValue, nameSegmentWidth);
    }

    public BrandInfoCell(String infomationHeader, List<String> informationItemsName, List<String> informationItemsValue, BaseColor backgroundColor, float nameSegmentWidth, int startColumn, int spanColumn) {
        super(infomationHeader, informationItemsName, informationItemsValue, backgroundColor, nameSegmentWidth, startColumn, spanColumn);
    }

    public BrandInfoCell(String infomationHeader, int informationHeaderFontSize, int informationHeaderFontStyle,
                         BaseColor informationHeaderFontColor, List<String> informationItemsName, List<String> informationItemsValue,
                         int informationItemFontSize, int informationItemFontStyle, BaseColor informationItemFontColor, BaseColor backgroundColor,
                         float nameSegmentWidth, int startColumn, int spanColumn) {
        super(infomationHeader, informationHeaderFontSize, informationHeaderFontStyle, informationHeaderFontColor,
                informationItemsName, informationItemsValue, informationItemFontSize, informationItemFontStyle,
                informationItemFontColor, backgroundColor, nameSegmentWidth, startColumn, spanColumn);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle noteStrAvailableRectangle = new Rectangle(position);
        /*noteStrAvailableRectangle.setLeft(noteStrAvailableRectangle.getLeft() + DEFAULT_PADDING + DEFAULT_MARGIN);
        noteStrAvailableRectangle.setRight(noteStrAvailableRectangle.getRight() - DEFAULT_PADDING - DEFAULT_MARGIN);*/
        noteStrAvailableRectangle.setTop(position.getTop()  - DEFAULT_PADDING - DEFAULT_MARGIN);
        noteStrAvailableRectangle.setBottom(position.getBottom() + DEFAULT_PADDING + DEFAULT_MARGIN);

        if (Debug.DEBUG_FLAG) {
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS], noteStrAvailableRectangle);
        }

        int currentLinesDrawn = 0;
        currentLinesDrawn += addStringCell(informationHeader, "", noteStrAvailableRectangle, canvases[PdfPTable.BASECANVAS],
                noteStrAvailableRectangle, currentLinesDrawn,
                informationHeaderFontSize, informationHeaderFontStyle, informationHeaderFontColor, 0,0,false,false);

        int index = 0;
        for (String informationItemValue : informationItemsValue) {
            if(RegularExpressionUtil.isTrademarkUrl(informationItemValue)){
                currentLinesDrawn += addImageCellCell(informationItemValue,"",noteStrAvailableRectangle,canvases[PdfPTable.BASECANVAS],
                        currentLinesDrawn);
            }else {
                currentLinesDrawn += addStringCell(index < informationItemsName.size() ? informationItemsName.get(index) : ""
                        , informationItemValue, noteStrAvailableRectangle, canvases[PdfPTable.BASECANVAS],
                        noteStrAvailableRectangle, currentLinesDrawn,
                        informationItemFontSize, informationItemFontStyle, informationItemFontColor, 10,nameSegmentWidth,false,false);
            }
            index++;
        }
    }

    protected int addImageCellCell(String imageUrl, String imageLink,
                                   Rectangle position, PdfContentByte canvas,
                                   int currentLinesDrown) {

        CellChartConfig cellChartConfig = CellChartConfig.newInstance();
        cellChartConfig.setLLX(position.getLeft());
        cellChartConfig.setLLY(position.getTop() - DEFAULT_CELL_HEIGHT * (currentLinesDrown + 2));
        cellChartConfig.setURX(position.getRight());
        cellChartConfig.setURY(position.getTop() - DEFAULT_CELL_HEIGHT * (currentLinesDrown));

        cellChartConfig.setMarginRight(DEFAULT_MARGIN);

        cellChartConfig.setBackgroundColor(cellBackgroundColor);

        CellChart cellChart = new CellChart(new ArrayList<Fragment>(), cellChartConfig);
        cellChart.fragments.add(getImageSegment(cellChartConfig, imageUrl, imageLink));

        cellChart.draw(canvas);

        currentLinesDrown++;

        return 2;
    }

    protected Fragment getImageSegment(CellChartConfig cellChartConfig, String imageUrl, String imageLink) {
        ImageConfig imageConfig = ImageConfig.newInstance(cellChartConfig);
        imageConfig.setImageDataStr(imageUrl);
        imageConfig.setExtraLink(imageLink);

        imageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_CENTER | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        return new Fragment(imageConfig);
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
            mainFrame.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            String [] testArray = new String[]{
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;该公司及关联关系获取到的舆情数据丰富,曝光程度高,公司活跃度高;",
                    "http://120.26.110.79:54322/api/companyAPI/trademark?token_id=278446ea8c77ad49cbc6&fileName=data/assets/trademark/34cd66dbc49c927bfbadd029afdd8c62.jpg"
            };

            String [] testArray1 = new String[]{"注册号","注册号","注册号","注册号","注册号","注册号","法人"};

            BrandInfoCell noteCell = new BrandInfoCell("上海冰鉴信息科技有限公司", Arrays.asList(testArray1),
                    Arrays.asList(testArray),new BaseColor(0xef,0xef,0xef,0xff),
                    60,1,12);
            noteCell.setColspan(12);
            noteCell.setPaddingBottom(5);
            mainFrame.addCell(noteCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
