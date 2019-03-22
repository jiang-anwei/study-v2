package com.icekredit.pdf.entities.table;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icekredit on 4/27/16.
 */
public class TableUTD2DCell extends AlignCenterCell {
    public TableUTD2DCell(List<String> headerHorizontalDatas,
                          List<String> headerVerticalDatas,List<List<String>> contentRowsDatas){
        this(headerHorizontalDatas, headerVerticalDatas, contentRowsDatas,false);
    }

    public TableUTD2DCell(List<String> headerHorizontalDatas,
                          List<String> headerVerticalDatas,List<List<String>> contentRowsDatas,boolean isNeedSubHeader){
        this(headerHorizontalDatas,headerVerticalDatas,
                contentRowsDatas,TableUTD2D.DEFAULT_TABLE_HEADER_BG_COLOR,TableUTD2D.DEFAULT_TABLE_ROW_BG_COLOR,isNeedSubHeader);
    }

    public TableUTD2DCell(List<String> headerHorizontalDatas,
                      List<String> headerVerticalDatas,List<List<String>> contentRowsDatas,
                      BaseColor tableHeaderBGColor,BaseColor tableRowBGColor,boolean isNeedSubHeader){

        TableUTD2D tableUTD2D = new TableUTD2D(headerHorizontalDatas,headerVerticalDatas,
                contentRowsDatas,tableHeaderBGColor,tableRowBGColor,isNeedSubHeader);

        this.addElement(tableUTD2D);
    }

    public static void main(String [] args){
        try{
            String dest = "results/objects/test.pdf";
            File destFile = new File(dest);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            List<String> tableHorizontalHeaders = new ArrayList<String>();
            List<String> tableVerticalHeaders = new ArrayList<String>();
            List<List<String>> tableBodyRowsDatas= new ArrayList<List<String>>();

            for(int index = 0;index < 10;index ++){
                tableHorizontalHeaders.add("Header" + (index + ""));
                tableVerticalHeaders.add("Header" + (index + ""));
            }

            List<String> tableBodyRowDatas = null;
            for(int x = 0;x < 10;x ++){
                tableBodyRowDatas = new ArrayList<String>();

                for(int y = 0;y < 10;y++){
                    tableBodyRowDatas.add("Body Data" + (x * 10 + y));
                }

                tableBodyRowsDatas.add(tableBodyRowDatas);
            }

            TableUTD2DCell tableUTD2DCell = new TableUTD2DCell(tableHorizontalHeaders,tableVerticalHeaders,tableBodyRowsDatas);

            PdfPTable mainFrame = new PdfPTable(1);
            mainFrame.setWidthPercentage(100);
            mainFrame.addCell(tableUTD2DCell);

            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
