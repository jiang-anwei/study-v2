package com.icekredit.pdf.entities.pie_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.EmptyCell;
import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 6/6/16.
 */
public class SuperPieChartCell extends AlignCenterCell {
    public static final int IDENTIFIER_SHOWN_AS_CIRCLE = 0;
    public static final int IDENTIFIER_SHOWN_AS_RECTANGLE = 1;


    public SuperPieChartCell(String pieChartTitle,
                             float[] sectorsAngle, BaseColor[] sectorsColor,
                             String[] sectorsDesc, int identifierShownAs,boolean isNeedDrawCover) {
        this.addElement(new SuperPieChart( pieChartTitle, sectorsAngle, sectorsColor, sectorsDesc, identifierShownAs,isNeedDrawCover));
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "results/object/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            SuperPieChartCell superPieChartCell = new SuperPieChartCell("", new float[]{48 + 90 + 80 + 70, 60, 2, 4, 6}, new BaseColor[]{
                    ColorUtil.strRGBAToColor("C1232BFF"),
                    ColorUtil.strRGBAToColor("B5C334FF"),
                    ColorUtil.strRGBAToColor("FCCE10FF"),
                    ColorUtil.strRGBAToColor("E87C25FF"),
                    ColorUtil.strRGBAToColor("27727BFF")
            }, new String[]{
                    "零售行业 医疗服务业",
                    "教育行业 批发行业",
                    "文化、体育和娱乐行业",
                    "汽车行业",
                    "住宿服务业"
            }, SuperPieChartCell.IDENTIFIER_SHOWN_AS_CIRCLE,false);
            superPieChartCell.setColspan(6);

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            mainFrame.addCell(superPieChartCell);
            mainFrame.addCell(new EmptyCell(6));

            document.add(mainFrame);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
