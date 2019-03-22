package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.Debug;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 8/4/16.
 */
public class ImageCell extends BaseCell implements PdfPCellEvent{
    private static final float DEFAULT_PADDING = 2;
    private static final float DEFAULT_MARGIN = 2;

    private static final float DEFAULT_CELL_HEIGHT = 80;
    public static final float DEFAULT_VISIBLE_BORDER_WIDTH_BOTTOM = 0.5f;
    private static final BaseColor DEFAULT_VISIBLE_BORDER_COLOR_BOTTOM = new BaseColor(0xef, 0xef, 0xef, 0xff);
    private static final BaseColor CELL_BACKGROUND_COLOR = new BaseColor(0xff, 0xff, 0xff, 0xff);

    private static final BaseColor NAME_STR_COLOR = new BaseColor(0x9f, 0xa0, 0xa0, 0xff);
    private static final int DEFAULT_FONT_SIZE = 10;
    private static final int DEFAULT_FONT_STYLE = Font.NORMAL;

    private String imageName;
    private String imageUrl;

    public ImageCell(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;

        this.setFixedHeight(DEFAULT_CELL_HEIGHT + DEFAULT_PADDING * 2 + DEFAULT_MARGIN * 2);

        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Rectangle availableRectangle = new Rectangle(position);
        availableRectangle.setTop(position.getTop() - DEFAULT_PADDING - DEFAULT_MARGIN);
        availableRectangle.setBottom(position.getBottom() + DEFAULT_PADDING + DEFAULT_MARGIN);

        if (Debug.DEBUG_FLAG) {
            View.showPosition(canvases[PdfPTable.BACKGROUNDCANVAS], availableRectangle);
        }

        addImageCellCell(imageName,imageUrl,availableRectangle,canvases[PdfPTable.BASECANVAS]);
    }

    protected void addImageCellCell( String imageName,String imageUrl,
                                   Rectangle position, PdfContentByte canvas) {
        CellChartConfig cellChartConfig = CellChartConfig.newInstance();
        cellChartConfig.setLLX(position.getLeft());
        cellChartConfig.setLLY(position.getBottom());
        cellChartConfig.setURX(position.getRight());
        cellChartConfig.setURY(position.getTop());

        cellChartConfig.setMarginRight(DEFAULT_MARGIN);
        cellChartConfig.setBackgroundColor(CELL_BACKGROUND_COLOR);
        cellChartConfig.setBorderColorBottom(DEFAULT_VISIBLE_BORDER_COLOR_BOTTOM);
        cellChartConfig.setBorderWidthBottom(DEFAULT_VISIBLE_BORDER_WIDTH_BOTTOM);

        CellChart cellChart = new CellChart(new ArrayList<Fragment>(), cellChartConfig);
        cellChart.fragments.add(getImageNameStrSegment(cellChartConfig,imageName));
        cellChart.fragments.add(getImageSegment(cellChartConfig, imageUrl));

        cellChart.draw(canvas);
    }

    private Fragment getImageNameStrSegment(CellChartConfig cellChartConfig, String imageName) {
        MessageConfig messageConfig = new MessageConfig(cellChartConfig);

        messageConfig.setMessage(imageName);
        messageConfig.setFontSize(DEFAULT_FONT_SIZE);
        messageConfig.setFontStyle(DEFAULT_FONT_STYLE);
        messageConfig.setFontColor(NAME_STR_COLOR);

        messageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_TOP);

        return new Fragment(messageConfig);
    }

    protected Fragment getImageSegment(CellChartConfig cellChartConfig, String imageUrl) {
        ImageConfig imageConfig = ImageConfig.newInstance(cellChartConfig);
        imageConfig.setImageDataStr(imageUrl);

        imageConfig.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

        return new Fragment(imageConfig);
    }


    public static void main(String[] args) {
        String destFileStr = "result/test.pdf";
        File destFile = new File(destFileStr);
        destFile.getParentFile().mkdirs();

        try {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);
            mainFrame.getDefaultCell().setBorder(Rectangle.NO_BORDER);

//            String str = "http://tm-image.qichacha.com/80fa10a0c06a03043be2d5fbc0e5db89.jpg@100h_160w_1l_50q";

            File jsonFile = new File("/home/icekredit/Documents/workplace/ent-v3/target/classes/total_info.json");
            FileInputStream inputStream = new FileInputStream(jsonFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte [] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }

            String str = JSONObject.fromObject(new String(outputStream.toByteArray())).getJSONObject("FaceCompare").getString("originalPhoto");

            ImageCell imageCell = new ImageCell("商标图片",str);
            imageCell.setColspan(12);
            mainFrame.addCell(imageCell);

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
