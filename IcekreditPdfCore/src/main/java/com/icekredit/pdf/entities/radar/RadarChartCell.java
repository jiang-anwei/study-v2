package com.icekredit.pdf.entities.radar;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 雷达图工具类的封装类，此类将雷达图实现封装到cell中，最终此cell会被添加到页面主体框架table中
 */
public class RadarChartCell extends AlignCenterCell implements PdfPCellEvent {
    private int excircleRadius = 0;    //雷达图外切圆的坐标
    private String[] radarAttributesNames = null;    //雷达图各个属性的名称
    private int[] radarAttributesValues = null;    //雷达图各个属性值
    private PdfWriter pdfWriter = null;

    private static final int DEFAULT_PADDING = 5;

    public RadarChartCell(int excircleRadius,
                          String[] radarAttributesNames, int[] radarAttributesValues, PdfWriter pdfWriter)
            throws IOException,DocumentException {
        this(excircleRadius, radarAttributesNames, radarAttributesValues, pdfWriter,DEFAULT_PADDING);
    }

    public RadarChartCell(int excircleRadius,
                          String[] radarAttributesNames, int[] radarAttributesValues, PdfWriter pdfWriter,
                          int padding) throws IOException,DocumentException {
        this(excircleRadius, radarAttributesNames, radarAttributesValues, pdfWriter,padding,
                DEFAULT_PADDING,DEFAULT_PADDING,DEFAULT_PADDING,DEFAULT_PADDING);
    }

    public RadarChartCell(int excircleRadius,
                      String[] radarAttributesNames, int[] radarAttributesValues, PdfWriter pdfWriter,
                          int padding,int paddingTop,int paddingBottom,int paddingLeft,int paddingRight)
            throws IOException,DocumentException {
        this.radarAttributesNames = radarAttributesNames;
        this.radarAttributesValues = radarAttributesValues;
        this.excircleRadius = excircleRadius;
        this.pdfWriter = pdfWriter;

        this.setPadding(padding);
        this.setPaddingTop(paddingTop);
        this.setPaddingBottom(paddingBottom);
        this.setPaddingLeft(paddingLeft);
        this.setPaddingRight(paddingRight);

        this.setFixedHeight(excircleRadius * 2 + DrawTextUtil.getStringHeight(FontUtils.fontSize) * 2 + this.getPaddingTop() + this.getPaddingBottom());

        this.setCellEvent(this);
    }

    public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentBytes){
        try{
            new RadarChart(
                    (rectangle.getRight() - this.getPaddingRight() + rectangle.getLeft() + this.getPaddingLeft()) / 2,
                    (rectangle.getTop() - this.getPaddingTop() + rectangle.getBottom() + this.getPaddingBottom()) / 2,
                    this.excircleRadius,
                    this.radarAttributesNames,
                    this.radarAttributesValues,
                    this.pdfWriter);
        }catch(Exception e){
            System.out.print("RadarChartCell:cellLayout:" + e.getMessage());
        }
    }

    public static void main(String [] args){
        String destPdfFilePath = "results/objects/radarChartCell.pdf";
        File file = new File(destPdfFilePath);
        file.getParentFile().mkdirs();

        Document document = null;
        try {
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            RadarChartCell radarChartCell = new RadarChartCell(100,
                    new String[]{"注册信息", "经营历史", "股东高管", "企业声誉", "无形资产","Another Attribute"},
                    new int[]{10, 20, 30, 40, 50,40}, writer);
            radarChartCell.setColspan(1);

            PdfPTable pdfPTable = new PdfPTable(1);
            pdfPTable.setWidthPercentage(100);

            pdfPTable.addCell(radarChartCell);

            document.add(pdfPTable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
