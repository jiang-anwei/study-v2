package com.icekredit.pdf.entities.core;

import com.icekredit.pdf.entities.View;
import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认内部内容自动居中的pdf table cell
 */
public class CellChart extends View {
    public List<Fragment> fragments;
    public CellChartConfig cellChartConfig;

    public CellChart(List<Fragment> fragments, CellChartConfig cellChartConfig) {
        this.fragments = fragments;
        this.cellChartConfig = cellChartConfig;
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            //先画这个单元格的大体框架
            this.cellChartConfig.draw(canvas);

            //然后再画这个单元格里面的每一个小的控件
            for(Fragment fragment : fragments){
                fragment.draw(canvas);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        CellChartConfig baseCellChartConfig = CellChartConfig.newInstance();
        baseCellChartConfig.setLLX(0);
        baseCellChartConfig.setLLY(100);
        baseCellChartConfig.setURX(PageSize.A4.getWidth());
        baseCellChartConfig.setURY(126);
        baseCellChartConfig.setBackgroundColor(ColorUtil.strRGBAToColor("66c6fcff"));
        baseCellChartConfig.setPadding(0);
        baseCellChartConfig.setMargin(0);
        baseCellChartConfig.setBorderWidth(0);
        baseCellChartConfig.setBorderWidthLeft(4);
        baseCellChartConfig.setBorderColor(ColorUtil.strRGBAToColor("66c6fcff"));
        baseCellChartConfig.setBorderColorLeft(ColorUtil.strRGBAToColor("00a0e9ff"));

        CellChart baseCellChart = new CellChart(new ArrayList<Fragment>(),baseCellChartConfig);
        MessageConfig messageConfig = MessageConfig.newInstance(baseCellChart.cellChartConfig);
        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT);
        messageConfig.setFontSize(16);
        messageConfig.setIdentifierType(MessageConfig.IDENTIFIER_TYPE_MARK_RIGHT);
        baseCellChart.fragments.add(new Fragment(messageConfig));
        /*baseCellChart.segments.add(new Segment(MessageConfig.newInstance(baseCellChart.cellChartConfig)));
        Segment segment = new Segment(MessageConfig.newInstance(baseCellChart.cellChartConfig));
        segment.getSegmentConfig().setLayoutGravity(SegmentConfig.LAYOUT_GRAVITY_LEFT | SegmentConfig.LAYOUT_GRAVITY_MIDDLE);
        baseCellChart.segments.add(segment);

        segment = new Segment(MessageConfig.newInstance(baseCellChart.cellChartConfig));
        segment.getSegmentConfig().setLayoutGravity(SegmentConfig.LAYOUT_GRAVITY_LEFT | SegmentConfig.LAYOUT_GRAVITY_MIDDLE);
        baseCellChart.segments.add(segment);

        segment = new Segment(ImageConfig.newInstance(baseCellChart.cellChartConfig));
        segment.getSegmentConfig().setLayoutGravity(SegmentConfig.LAYOUT_GRAVITY_RIGHT| SegmentConfig.LAYOUT_GRAVITY_MIDDLE);
        baseCellChart.segments.add(segment);*/

        try {
            String destFileStr = "results/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfContentByte canvas = pdfWriter.getDirectContent();
            baseCellChart.draw(canvas);

            document.add(new Paragraph(new Phrase(new Chunk("dasdasdas"))));

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}