package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icekredit on 4/21/16.
 */
public class EnterpriseRelativeChartCell extends AlignCenterCell {
    public EnterpriseRelativeChartCell(PdfWriter pdfWriter, String enterpriseName,
                                       List<Map<String, Object>> stockHoldersInfo,
                                       List<Map<String, Object>> investEnterprisesInfo,
                                       BaseColor[] individualIdentifierIconColors,
                                       BaseColor[] enterpriseIdentifierIconColors,
                                       String notice) {

        EnterpriseRelativeChart enterpriseRelativeChart = new EnterpriseRelativeChart(
                pdfWriter,enterpriseName,
                stockHoldersInfo,investEnterprisesInfo,
                individualIdentifierIconColors,enterpriseIdentifierIconColors,
                notice);
        enterpriseRelativeChart.setWidthPercentage(100);

        this.addElement(enterpriseRelativeChart);
    }

    public EnterpriseRelativeChartCell(PdfWriter pdfWriter, String enterpriseName,
                                       List<Map<String, Object>> stockHoldersInfo,
                                       List<Map<String, Object>> investEnterprisesInfo) {
        AlignCenterCell chartWrapperCell = new AlignCenterCell();
        chartWrapperCell.setColspan(12);

        PdfPTable chartWrapperTable = new PdfPTable(1);
        chartWrapperTable.setWidthPercentage(100);
        chartWrapperTable.setSplitLate(true);
        chartWrapperTable.setSplitRows(false);


        EnterpriseRelativeChart enterpriseRelativeChart = new EnterpriseRelativeChart(
                pdfWriter,enterpriseName,
                stockHoldersInfo,investEnterprisesInfo);
        enterpriseRelativeChart.setWidthPercentage(100);

        chartWrapperCell.addElement(enterpriseRelativeChart);
        chartWrapperTable.addCell(chartWrapperCell);

        this.addElement(chartWrapperTable);
    }

    public static void main(String [] args){
        try{
            String destPdfFilePath = "results/objects/test.pdf";
            File file = new File(destPdfFilePath);
            file.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            String enterpriseName = "上海冰鉴信息科技技术有限公司";
            List<Map<String,Object>> stockholdersInfo = new ArrayList<Map<String,Object>>();
            List<Map<String,Object>> investEnterprisesInfo = new ArrayList<Map<String,Object>>();

            Map<String,Object> stockholderInfo;

            for(int index = 0;index < 4;index ++){
                stockholderInfo = new HashMap<String,Object>();
                stockholderInfo.put("has_cascade_info",true);
                stockholderInfo.put("stockholder_name","上海冰鉴信息科技技术有限公司");
                stockholderInfo.put("stockholder_type",0);

                stockholdersInfo.add(stockholderInfo);
            }
            stockholdersInfo.get(0).put("stockholder_type",1);
            stockholdersInfo.get(stockholdersInfo.size() - 1).put("stockholder_type",2);
            stockholdersInfo.get(stockholdersInfo.size() - 1).put("stockholder_name","");


            Map<String,Object> investEnterpriseInfo;

            for(int index = 0;index < 4;index ++){
                investEnterpriseInfo = new HashMap<String,Object>();
                investEnterpriseInfo.put("has_cascade_info",true);
                investEnterpriseInfo.put("enterprise_name","上海冰鉴信息科技技术有限公司");
                investEnterpriseInfo.put("enterprise_type",0);

                investEnterprisesInfo.add(investEnterpriseInfo);
            }
            investEnterprisesInfo.get(0).put("enterprise_type",1);
            investEnterprisesInfo.get(investEnterprisesInfo.size() - 1).put("enterprise_type",2);
            investEnterprisesInfo.get(investEnterprisesInfo.size() - 1).put("enterprise_name","");


            PdfPCell enterpriseRelativeChartCell = new EnterpriseRelativeChartCell(
                    writer,enterpriseName,stockholdersInfo,investEnterprisesInfo);
            enterpriseRelativeChartCell.setColspan(12);

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            mainFrame.addCell(enterpriseRelativeChartCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
