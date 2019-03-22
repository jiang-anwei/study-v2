package com.icekredit.pdf.entities.table;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by icekredit on 4/27/16.
 */
public class TableUTD2D extends PdfPTable {
    protected BaseColor tableHeaderBGColor;
    protected static final BaseColor DEFAULT_TABLE_HEADER_BG_COLOR = new BaseColor(0xef,0xef,0xef,0xff);
    protected BaseColor tableRowBGColor;
    protected static final BaseColor DEFAULT_TABLE_ROW_BG_COLOR = new BaseColor(0xf8,0xff,0xff,0xff);

    protected List<String> headerHorizontalDatas;
    protected List<String> headerVerticalDatas;
    protected List<List<String>> contentRowsDatas;

    protected boolean isNeedSubHeader;

    public TableUTD2D(List<String> headerHorizontalDatas,
                      List<String> headerVerticalDatas,List<List<String>> contentRowsDatas){
        this(headerHorizontalDatas, headerVerticalDatas, contentRowsDatas, DEFAULT_TABLE_HEADER_BG_COLOR,DEFAULT_TABLE_ROW_BG_COLOR);
    }

    public TableUTD2D(List<String> headerHorizontalDatas,
                      List<String> headerVerticalDatas,List<List<String>> contentRowsDatas,
                      BaseColor tableHeaderBGColor,BaseColor tableRowBGColor){
        this(headerHorizontalDatas, headerVerticalDatas, contentRowsDatas, tableHeaderBGColor, tableRowBGColor,false);
    }

    public TableUTD2D(List<String> headerHorizontalDatas,
                      List<String> headerVerticalDatas,List<List<String>> contentRowsDatas,
                      BaseColor tableHeaderBGColor,BaseColor tableRowBGColor,boolean isNeedSubHeader){
        this.headerHorizontalDatas = headerHorizontalDatas;
        this.headerVerticalDatas = headerVerticalDatas;
        this.contentRowsDatas = contentRowsDatas;
        this.tableHeaderBGColor = tableHeaderBGColor;
        this.tableRowBGColor = tableRowBGColor;

        this.isNeedSubHeader = isNeedSubHeader;

        assertTableDataValid();

        this.setWidthPercentage(100);
        this.resetColumnCount(getProperColumnCount());

        initTableHeader();
        initTableBody();
        initTableFooter();
    }

    private void assertTableDataValid(){
        //断言传入的数据数目是否正确

        /*try {
            if (this.headerHorizontalDatas.size() == 0){
                throw new Exception("抱歉，水平表头信息不能为空！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

        /*if(this.headerVerticalDatas.size() == 0){
            throw new Exception("抱歉，垂直表头信息不能为空");
        }

        if (this.headerVerticalDatas.size() != contentRowsDatas.size()){
            throw new Exception("抱歉，表格体内容数据与垂直表头长度信息不一致！");
        }*/
    }

    protected void initTableHeader() {

    }

    protected void initTableBody() {
        /*Debug.debug(headerHorizontalDatas.toString());
        Debug.debug(headerVerticalDatas.toString());
        Debug.debug(contentRowsDatas.toString());
        Debug.debug("\n");*/

        if(headerVerticalDatas != null && headerVerticalDatas.size() != 0){
            //首先添加一个表格左上角的空白单元格
            AlignCenterCell occupyTableHeaderCell = new AlignCenterCell();
            occupyTableHeaderCell.setBackgroundColor(tableHeaderBGColor);
            occupyTableHeaderCell.setColspan(1);
            this.addCell(occupyTableHeaderCell);
        }

        PdfPCell headerCell = null;
        for(int index = 0;index < headerHorizontalDatas.size();index ++){
            headerCell = getHorizontalCell(headerHorizontalDatas.get(index),isNeedSubHeader ? headerHorizontalDatas.get(++index) : null);

            this.addCell(headerCell);
        }

        /*if(contentRowsDatas.size() == 0){
            AlignCenterCell emptyCell = new AlignCenterCell(new Phrase("暂无", FontUtils.chineseFont));
            emptyCell.setColspan(getProperColumnCount());
            this.addCell(emptyCell);

            return;
        }*/

        if(headerVerticalDatas != null && headerVerticalDatas.size() != 0){
            int index = 0;

            for(String verticalHeader:headerVerticalDatas){
                headerCell = new AlignCenterCell(new Phrase(verticalHeader, FontUtils.chineseFont));
                headerCell.setColspan(1);
                headerCell.setBackgroundColor(tableHeaderBGColor);

                this.addCell(headerCell);

                addRow(contentRowsDatas.get(index));

                index ++;
            }
        }else {
            for(int index = 0;index < contentRowsDatas.size();index ++){
                addRow(contentRowsDatas.get(index));
            }
        }
    }

    protected PdfPCell getHorizontalCell(String horizontalHeader,String subHeader) {
        PdfPCell headerCell = null;

        if(!isNeedSubHeader){
            headerCell = new AlignCenterCell(new Phrase(new Phrase(horizontalHeader, FontUtils.chineseFont)));
            headerCell.setColspan((int) Math.ceil(4 * 1.0 / headerHorizontalDatas.size()));
            headerCell.setBackgroundColor(tableHeaderBGColor);
        }else {
            if(subHeader == null || subHeader.trim().equals("")){
                headerCell = new CustomHeaderCell(horizontalHeader);
            }else {
                headerCell = new CustomHeaderCell(horizontalHeader,subHeader);
            }

            headerCell.setColspan((int) Math.ceil(8 * 1.0 / headerHorizontalDatas.size()));
        }


        return headerCell;
    }

    protected void addRow(List<String> contentRowDatas) {
        AlignCenterCell tableRowCell;
        for(String tableRowData:contentRowDatas){
            tableRowCell = new AlignCenterCell(new Phrase(new Chunk(tableRowData, FontUtils.chineseFont)));
            tableRowCell.setColspan((int) Math.ceil(4 * 1.0 / contentRowDatas.size()));
            tableRowCell.setBackgroundColor(tableRowBGColor);

            this.addCell(tableRowCell);
        }
    }

    protected void initTableFooter() {

    }

    public int getProperColumnCount() {
        int horizontalColumnSize = this.headerHorizontalDatas.size() == 0 ?  1 : this.headerHorizontalDatas.size();

        horizontalColumnSize = isNeedSubHeader ? (horizontalColumnSize / 2) : horizontalColumnSize;

        int minColumnCount = (int) Math.ceil(4 * 1.0 / horizontalColumnSize) * horizontalColumnSize;

        int properColumnCount = horizontalColumnSize > minColumnCount ? horizontalColumnSize : minColumnCount
                + ((this.headerVerticalDatas == null || this.headerVerticalDatas.size() == 0) ? 0 : 1);

        return properColumnCount == 0 ? 1 : properColumnCount;
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
                //tableVerticalHeaders.add("Header" + (index + ""));
            }

            List<String> tableBodyRowDatas = null;
            for(int x = 0;x < 10;x ++){
                tableBodyRowDatas = new ArrayList<String>();

                for(int y = 0;y < 10;y++){
                    tableBodyRowDatas.add("Body Data" + (x * 10 + y));
                }

                tableBodyRowsDatas.add(tableBodyRowDatas);
            }

            TableUTD2D tableUTD2D = new TableUTD2D(tableHorizontalHeaders,tableVerticalHeaders,tableBodyRowsDatas);

            document.add(tableUTD2D);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
