package com.icekredit.pdf.entities.relative_chart;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.entities.AlignLeftCell;
import com.icekredit.pdf.entities.AlignRightCell;
import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icekredit on 4/20/16.
 */
public class EnterpriseRelativeChart extends PdfPTable implements PdfPTableEvent{
    private static final String TAG = "EnterpriseRelativeChart";

    private static final int DEFAULT_TABLE_COLUMN_COUNT = 33;

    private static final int STOCKHOLDER_TYPE_ENTERPRISE = 0;
    private static final int STOCKHOLDER_TYPE_INDIVIDUAL = 1;
    private static final int STOCKHOLDER_TYPE_EXTRA_INFO = 2;

    private static final int INVEST_ENTERPRISE_TYPE_ENTERPRISE = 0;
    private static final int INVEST_ENTERPRISE_TYPE_INDIVIDUAL = 1;
    private static final int INVEST_ENTERPRISE_TYPE_EXTRA_INFO = 2;


    private String enterpriseName;
    private static final String DEFAULT_ENTERPRISE_NAME = "南京同创信息产业集团有限公司";
    private String individual;
    private static final String DEFAULT_INDIVIDUAL = "自然人";
    private String enterprise;
    private static final String DEFAULT_ENTERPRISE = "企业";

    private List<Map<String,Object>> stockHoldersInfo;
    private List<Map<String,Object>> investEnterprisesInfo;

    private String notice;
    private static final String DEFAULT_NOTICE = "详情请前往网页端查看";
    private static final String DEFAULT_NOTICE_INVEST = "您查询的企业对外投资信息数量较多，请前往网页端查看详情";
    private static final String DEFAULT_NOTICE_STOCKHOLDER = "您查询的企业股东信息数量较多，请前往网页端查看详情";
    private static final String DEFAULT_NOTICE_INVEST_AND_STOCKHOLDER = "您查询的企业股东信息以及对外投资信息数量较多，请前往网页端查看详情";


    private static final int MIDDLE = 5;

    private PdfWriter pdfWriter ;
    //    private BaseColor [] individualIdentifierIconColors;
    private static final BaseColor [] DEFAULT_INDIVIDUAL_IDENTIFIER_ICON_COLORS = new BaseColor[]{
            new BaseColor(0xaa,0xaa,0xaa,0xff),
            new BaseColor(0x61,0xf0,0x61,0xff)
    };

    //    private BaseColor [] enterpriseIdentifierIconColors;
    private static final BaseColor [] DEFAULT_ENTERPRISE_IDENTIFIER_ICON_COLORS = new BaseColor[]{
            new BaseColor(0xaa,0xaa,0xaa,0xff),
            new BaseColor(0x61,0xf0,0x61,0xff)
    };
    private BaseColor [] individualIdentifierIconColors = new BaseColor[]{
            new BaseColor(0xaa,0xaa,0xaa,0xff),
            new BaseColor(0x61,0xf0,0x61,0xff)
    };

    private BaseColor [] enterpriseIdentifierIconColors = new BaseColor[]{
            new BaseColor(0xaa,0xaa,0xaa,0xff),
            new BaseColor(0x61,0xf0,0x61,0xff)
    };

    private BaseColor [] currentEnterpriseIdentifierIconColors = new BaseColor[]{
            new BaseColor(0xaa,0xaa,0xaa,0xff),
            new BaseColor(0x40,0x78,0xd0,0xff)
    };

    private BaseColor arrowColor = new BaseColor(0xe0,0xe0,0xe0,0xff);


    private List<IdentifierCell> stockholderIdentifierCells = new ArrayList<IdentifierCell>();
    private List<IdentifierCell> investingEnterpriseIdentifierCells = new ArrayList<IdentifierCell>();

    private List<ArrowCell> stockholderArrowCells = new ArrayList<ArrowCell>();
    private List<ArrowCell> investingEnterpriseArrowCells = new ArrayList<ArrowCell>();
    private IdentifierForCurrentCompanyCell identifierForCurrentCompanyCell;


    public EnterpriseRelativeChart(PdfWriter pdfWriter,String enterpriseName,
                                   List<Map<String, Object>> stockHoldersInfo,
                                   List<Map<String, Object>> investEnterprisesInfo) {
        this(pdfWriter,enterpriseName,stockHoldersInfo,investEnterprisesInfo,
                DEFAULT_INDIVIDUAL_IDENTIFIER_ICON_COLORS,
                DEFAULT_ENTERPRISE_IDENTIFIER_ICON_COLORS,
                getFooterNotice(stockHoldersInfo.size(),investEnterprisesInfo.size()));
    }

    private static String getFooterNotice(int stockHoldersCount, int investEnterprisesCount){
        if(stockHoldersCount > 10){
            if(investEnterprisesCount > 10){
                return DEFAULT_NOTICE_INVEST_AND_STOCKHOLDER;
            }else {
                return DEFAULT_NOTICE_STOCKHOLDER;
            }
        }else {
            if(investEnterprisesCount > 10){
                return DEFAULT_NOTICE_INVEST;
            }else {
                return DEFAULT_NOTICE;
            }
        }
    }

    public EnterpriseRelativeChart(PdfWriter pdfWriter,String enterpriseName,
                                   List<Map<String, Object>> stockHoldersInfo,
                                   List<Map<String, Object>> investEnterprisesInfo,
                                   BaseColor[] individualIdentifierIconColors,
                                   BaseColor[] enterpriseIdentifierIconColors,
                                   String notice) {
        this.pdfWriter = pdfWriter;
        this.enterpriseName = enterpriseName;
        this.stockHoldersInfo = stockHoldersInfo;
        this.investEnterprisesInfo = investEnterprisesInfo;
        this.individualIdentifierIconColors = individualIdentifierIconColors;
        this.enterpriseIdentifierIconColors = enterpriseIdentifierIconColors;
        this.notice = notice;

        this.enterprise = DEFAULT_ENTERPRISE;
        this.individual = DEFAULT_INDIVIDUAL;

        initEnterpriseRelativeChart();
        this.setTableEvent(this);
    }

    @Override
    public void tableLayout(PdfPTable table, float[][] widths,
                            float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
        Point startPoint;
        Point endPoint;

        float middle = (stockholderIdentifierCells.size() - 1) * 1.0f / 2;

        SinCosDrawCurveController controller = new SinCosDrawCurveController();
        if(stockholderIdentifierCells.size() % 2 == 0){
            for(int position = 1;position <= stockholderIdentifierCells.size() / 2;position ++){
                endPoint = stockholderIdentifierCells.get((int)Math.ceil(middle - position)).getRelatedAsStockholderVertex();
                startPoint = stockholderArrowCells.get((int)Math.ceil(middle - position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                endPoint = stockholderArrowCells.get((int)Math.ceil(middle - position)).getArrowPointToVertex();
                startPoint = identifierForCurrentCompanyCell.getStockHolderBranchesVertices()[MIDDLE + position];

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);


                endPoint = stockholderIdentifierCells.get((int)Math.floor(middle + position)).getRelatedAsStockholderVertex();
                startPoint = stockholderArrowCells.get((int)Math.floor(middle + position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                endPoint = stockholderArrowCells.get((int)Math.floor(middle + position)).getArrowPointToVertex();
                startPoint = identifierForCurrentCompanyCell.getStockHolderBranchesVertices()[MIDDLE - position];

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);
            }
        }else {
            endPoint = stockholderIdentifierCells.get(stockholderIdentifierCells.size() / 2).getRelatedAsStockholderVertex();
            startPoint = stockholderArrowCells.get(stockholderIdentifierCells.size() / 2).getArrowPointFromVertex();
            controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

            endPoint = stockholderArrowCells.get(stockholderIdentifierCells.size() / 2).getArrowPointToVertex();
            startPoint = identifierForCurrentCompanyCell.getStockHolderBranchesVertices()[MIDDLE];

            controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

            for(int position = 1;position <= (stockholderIdentifierCells.size() - 1) / 2;position ++){
                endPoint = stockholderIdentifierCells.get((int)Math.floor(middle - position)).getRelatedAsStockholderVertex();
                startPoint = stockholderArrowCells.get((int)Math.floor(middle - position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                endPoint = stockholderArrowCells.get((int)Math.floor(middle - position)).getArrowPointToVertex();
                startPoint = identifierForCurrentCompanyCell.getStockHolderBranchesVertices()[MIDDLE + position];

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);


                endPoint = stockholderIdentifierCells.get((int)Math.floor(middle + position)).getRelatedAsStockholderVertex();
                startPoint = stockholderArrowCells.get((int)Math.floor(middle + position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                endPoint = stockholderArrowCells.get(Math.round(middle + position)).getArrowPointToVertex();
                startPoint = identifierForCurrentCompanyCell.getStockHolderBranchesVertices()[MIDDLE - position];

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);
            }
        }

        middle = (investingEnterpriseIdentifierCells.size() - 1) * 1.0f / 2;

        if(investingEnterpriseIdentifierCells.size() % 2 == 0){
            for(int position = 1;position <= investingEnterpriseIdentifierCells.size() / 2;position ++){
                startPoint = identifierForCurrentCompanyCell.getInvestBranchVertices()[MIDDLE - position];
                endPoint = investingEnterpriseArrowCells.get((int)Math.ceil(middle - position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                startPoint = investingEnterpriseArrowCells.get((int)Math.ceil(middle - position)).getArrowPointToVertex();
                endPoint = investingEnterpriseIdentifierCells.get((int)Math.ceil(middle - position)).getRelatedAsInvestVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                startPoint = identifierForCurrentCompanyCell.getInvestBranchVertices()[MIDDLE + position];
                endPoint = investingEnterpriseArrowCells.get((int)Math.floor(middle + position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                startPoint = investingEnterpriseArrowCells.get((int)Math.floor(middle + position)).getArrowPointToVertex();
                endPoint = investingEnterpriseIdentifierCells.get((int)Math.floor(middle + position)).getRelatedAsInvestVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);
            }
        }else {

            startPoint = investingEnterpriseIdentifierCells.get(
                    investingEnterpriseIdentifierCells.size() / 2).getRelatedAsInvestVertex();
            endPoint = investingEnterpriseArrowCells.get(investingEnterpriseArrowCells.size() / 2).getArrowPointFromVertex();
            controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

            startPoint = investingEnterpriseArrowCells.get(investingEnterpriseArrowCells.size() / 2).getArrowPointToVertex();
            endPoint = identifierForCurrentCompanyCell.getInvestBranchVertices()[MIDDLE];

            controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

            for(int position = 1;position <= (investingEnterpriseIdentifierCells.size() - 1) / 2;position ++){
                startPoint = identifierForCurrentCompanyCell.getInvestBranchVertices()[MIDDLE - position];
                endPoint = investingEnterpriseArrowCells.get((int)Math.floor(middle - position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                startPoint = investingEnterpriseArrowCells.get((int)Math.floor(middle - position)).getArrowPointToVertex();
                endPoint = investingEnterpriseIdentifierCells.get((int)Math.floor(middle - position)).getRelatedAsInvestVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);


                startPoint = identifierForCurrentCompanyCell.getInvestBranchVertices()[MIDDLE + position];
                endPoint = investingEnterpriseArrowCells.get((int)Math.floor(middle + position)).getArrowPointFromVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);

                startPoint = investingEnterpriseArrowCells.get((int)Math.floor(middle + position)).getArrowPointToVertex();
                endPoint = investingEnterpriseIdentifierCells.get((int)Math.floor(middle + position)).getRelatedAsInvestVertex();

                controller.drawCurve(pdfWriter.getDirectContent(),startPoint,endPoint);
            }
        }
    }

    public void initEnterpriseRelativeChart(){
        this.resetColumnCount(DEFAULT_TABLE_COLUMN_COUNT);

        this.setWidthPercentage(100);

        //添加表头信息，包括当前公司名称，标识符样例
        addTableHeader();

        addStockIdentifiers(stockHoldersInfo.size());

        addStockholderArrows(stockHoldersInfo.size());

        addCurrentCompanyIdentifier();

        addInvestEnterpriseArrows(investEnterprisesInfo.size());

        addInvestEnterpriseIdentifiers(investEnterprisesInfo.size());

        addTableFooter();
    }

    private void addTableHeader() {
        PdfPCell enterpriseNameCell = new AlignLeftCell(new Phrase(new Chunk(enterpriseName, FontUtils.chineseFont)));
        enterpriseNameCell.setColspan(25);

        PdfPCell individualIdentifierIconCell = new IdentifierForRelativeIndividualCell(pdfWriter,individualIdentifierIconColors);
        individualIdentifierIconCell.setColspan(1);
        PdfPCell individualIdentifierDescCell = new AlignLeftCell(new Phrase(new Chunk(individual, FontUtils.chineseFont)));
        individualIdentifierDescCell.setColspan(3);

        PdfPCell enterpriseIdentifierIconCell = new IdentifierForRelativeCompanyCell(pdfWriter,true,
                new BaseColor(0xff,0xff,0xff,0xff),enterpriseIdentifierIconColors);
        enterpriseIdentifierIconCell.setColspan(1);
        PdfPCell enterpriseIdentifierDescCell = new AlignLeftCell(new Phrase(new Chunk(enterprise, FontUtils.chineseFont)));
        enterpriseIdentifierDescCell.setColspan(3);

        this.addCell(enterpriseNameCell);
        this.addCell(individualIdentifierIconCell);
        this.addCell(individualIdentifierDescCell);
        this.addCell(enterpriseIdentifierIconCell);
        this.addCell(enterpriseIdentifierDescCell);
    }

    private void addInvestEnterpriseIdentifiers(int investEnterpriseCount) {
        //如果没有企业投资信息，那么不执行此段代码，否则会进行一些没有必要的，空单元格对齐操作
        if(investEnterpriseCount == 0){
            return;
        }

        IdentifierCell identifierIconForInvestEnterpriseCell = null;
        PdfPCell identifierDescForInvestEnterpriseCell = null;

        addAlignmentEmptyCell(investEnterpriseCount,true);

        Map<String,Object> investEnterpriseInfo;
        for(int index = 0;index < investEnterpriseCount;index ++){
            investEnterpriseInfo = investEnterprisesInfo.get(index);

            if((Integer)investEnterpriseInfo.get("enterprise_type") == STOCKHOLDER_TYPE_ENTERPRISE){
                identifierIconForInvestEnterpriseCell = new IdentifierForRelativeCompanyCell(pdfWriter,
                        (Boolean) investEnterpriseInfo.get("has_cascade_info"),
                        new BaseColor(0xff,0xff,0xff,0xff),enterpriseIdentifierIconColors);
            }else if((Integer)investEnterpriseInfo.get("enterprise_type") == STOCKHOLDER_TYPE_INDIVIDUAL){
                identifierIconForInvestEnterpriseCell = new IdentifierForRelativeIndividualCell(pdfWriter
                        ,enterpriseIdentifierIconColors);
            }else if((Integer)investEnterpriseInfo.get("enterprise_type") == STOCKHOLDER_TYPE_EXTRA_INFO){
                identifierIconForInvestEnterpriseCell = new IdentifierForExtraInfoCell(pdfWriter
                        ,enterpriseIdentifierIconColors);
            }

            identifierIconForInvestEnterpriseCell.setColspan(1);

            investingEnterpriseIdentifierCells.add(index,identifierIconForInvestEnterpriseCell);

            identifierDescForInvestEnterpriseCell = new AlignCenterCell(new Phrase(new Chunk(
                    (String) investEnterpriseInfo.get("enterprise_name"), FontUtils.chineseFont)));
            identifierDescForInvestEnterpriseCell.setColspan(2);

            this.addCell(identifierIconForInvestEnterpriseCell);
            this.addCell(identifierDescForInvestEnterpriseCell);

            if(investEnterpriseCount % 2 == 0 && index == investEnterpriseCount / 2 - 1){
                PdfPCell emptyCell = new AlignCenterCell();
                emptyCell.setColspan(1);
                this.addCell(emptyCell);
            }
        }

        addAlignmentEmptyCell(investEnterpriseCount,false);
    }

    private void addInvestEnterpriseArrows(int investEnterpriseCount) {
        //如果没有企业对外投资信息，那么不执行此段代码，否则会进行一些没有必要的，空单元格对齐操作
        if(investEnterpriseCount == 0){
            return;
        }

        addAlignmentEmptyCell(investEnterpriseCount,true);
        addArrows(investingEnterpriseArrowCells,investEnterpriseCount);
        addAlignmentEmptyCell(investEnterpriseCount,false);
    }

    private void addArrows(List<ArrowCell> arrowCells, int arrowCount) {
        ArrowCell investEnterprisrArrowCell;
        PdfPCell emptyCell;
        for(int index = 0; index < arrowCount; index ++){
            investEnterprisrArrowCell = new ArrowCell(pdfWriter,arrowColor,100, Arrow.POINT_TO_DIRECTION_DOWN);
            investEnterprisrArrowCell.setColspan(1);

            emptyCell = new AlignCenterCell();
            emptyCell.setColspan(2);

            arrowCells.add(index,investEnterprisrArrowCell);

            this.addCell(investEnterprisrArrowCell);
            this.addCell(emptyCell);

            //如果需要画的箭头的数量是偶数个，那么在最中间画一个空白cell以保持两边的对称
            if(arrowCount % 2 == 0 && index == arrowCount / 2 - 1){
                emptyCell = new AlignCenterCell();
                emptyCell.setColspan(1);
                this.addCell(emptyCell);
            }
        }
    }

    private void addCurrentCompanyIdentifier() {
        PdfPCell emptyCell;
        emptyCell = new AlignCenterCell();
        emptyCell.setColspan(14);

        identifierForCurrentCompanyCell = new IdentifierForCurrentCompanyCell(pdfWriter, Identifier.SHOW_AS_CIRCLE,currentEnterpriseIdentifierIconColors);
        identifierForCurrentCompanyCell.setColspan(3);
        identifierForCurrentCompanyCell.setFixedHeight(100);

        PdfPCell currentCompanyNameCell = new AlignLeftCell(new Phrase(new Chunk(enterpriseName, FontUtils.chineseFont)));
        currentCompanyNameCell.setColspan(16);

        this.addCell(emptyCell);
        this.addCell(identifierForCurrentCompanyCell);
        this.addCell(currentCompanyNameCell);
    }

    private void addStockholderArrows(int stockholderCount) {
        //如果没有企业股东信息，那么不执行此段代码，否则会进行一些没有必要的，空单元格对齐操作
        if(stockholderCount == 0){
            return;
        }

        addAlignmentEmptyCell(stockholderCount,true);

        addArrows(stockholderArrowCells,stockholderCount);

        addAlignmentEmptyCell(stockholderCount,false);
    }

    private void addStockIdentifiers(int stockholderCount) {
        //如果没有企业股东信息，那么不执行此段代码，否则会进行一些没有必要的，空单元格对齐操作
        if(stockholderCount == 0){
            return ;
        }

        IdentifierCell identifierIconForStockholderCell = null;
        PdfPCell identifierDescForStockholderCell = null;

        addAlignmentEmptyCell(stockholderCount,true);

        Map<String,Object> stockholderInfo;
        for(int index = 0;index < stockholderCount;index ++){
            stockholderInfo = stockHoldersInfo.get(index);

            if((Integer)stockholderInfo.get("stockholder_type") == STOCKHOLDER_TYPE_ENTERPRISE){
                identifierIconForStockholderCell = new IdentifierForRelativeCompanyCell(pdfWriter,
                        (Boolean) stockholderInfo.get("has_cascade_info"),
                        new BaseColor(0xff,0xff,0xff,0xff),enterpriseIdentifierIconColors);
            }else if((Integer)stockholderInfo.get("stockholder_type") == STOCKHOLDER_TYPE_INDIVIDUAL){
                identifierIconForStockholderCell = new IdentifierForRelativeIndividualCell(pdfWriter
                        ,enterpriseIdentifierIconColors);
            }else if((Integer)stockholderInfo.get("stockholder_type") == STOCKHOLDER_TYPE_EXTRA_INFO){
                identifierIconForStockholderCell = new IdentifierForExtraInfoCell(pdfWriter
                        ,enterpriseIdentifierIconColors);
            }

            identifierIconForStockholderCell.setColspan(1);

            identifierDescForStockholderCell = new AlignCenterCell(new Phrase(new Chunk(
                    (String)stockholderInfo.get("stockholder_name"), FontUtils.chineseFont)));
            identifierDescForStockholderCell.setColspan(2);

            stockholderIdentifierCells.add(index,identifierIconForStockholderCell);

            this.addCell(identifierIconForStockholderCell);
            this.addCell(identifierDescForStockholderCell);

            if(stockholderCount % 2 == 0 && index == stockholderCount / 2 - 1){
                PdfPCell emptyCell = new AlignCenterCell();
                emptyCell.setColspan(1);
                this.addCell(emptyCell);
            }
        }


        addAlignmentEmptyCell(stockholderCount,false);
    }

    private void addTableFooter() {
        try {
            AlignRightCell footerNoticeCell = new AlignRightCell(new Phrase(new Chunk(notice,new Font(
                    FontUtils.baseFontChinese, FontUtils.fontSize, FontUtils.fontStyle,new BaseColor(0x77,0x77,0x77,0xff)))));

            footerNoticeCell.setBorder(PdfPCell.NO_BORDER);
            footerNoticeCell.setColspan(33);
            this.addCell(footerNoticeCell);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addAlignmentEmptyCell(int identifierCount,boolean isLeft){
        int extraColumnCount = DEFAULT_TABLE_COLUMN_COUNT - identifierCount * 3;

        if(extraColumnCount <= 2){
            return;
        }

        PdfPCell emptyCell = new AlignCenterCell();
        emptyCell.setColspan(extraColumnCount / 2);

        this.addCell(emptyCell);
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

            /*for(int index = 0;index < 4;index ++){
                stockholderInfo = new HashMap<String,Object>();
                stockholderInfo.put("has_cascade_info",true);
                stockholderInfo.put("stockholder_name","上海冰鉴信息科技技术有限公司");
                stockholderInfo.put("stockholder_type",STOCKHOLDER_TYPE_ENTERPRISE);

                stockholdersInfo.add(stockholderInfo);
            }
            stockholdersInfo.get(0).put("stockholder_type",STOCKHOLDER_TYPE_INDIVIDUAL);
            stockholdersInfo.get(stockholdersInfo.size() - 1).put("stockholder_type",STOCKHOLDER_TYPE_EXTRA_INFO);
            stockholdersInfo.get(stockholdersInfo.size() - 1).put("stockholder_name","");*/


            Map<String,Object> investEnterpriseInfo;

            for(int index = 0;index < 4;index ++){
                investEnterpriseInfo = new HashMap<String,Object>();
                investEnterpriseInfo.put("has_cascade_info",true);
                investEnterpriseInfo.put("enterprise_name","上海冰鉴信息科技技术有限公司");
                investEnterpriseInfo.put("enterprise_type",INVEST_ENTERPRISE_TYPE_ENTERPRISE);

                investEnterprisesInfo.add(investEnterpriseInfo);
            }



            investEnterprisesInfo.get(0).put("enterprise_type",INVEST_ENTERPRISE_TYPE_INDIVIDUAL);
            investEnterprisesInfo.get(investEnterprisesInfo.size() - 1).put("enterprise_type",INVEST_ENTERPRISE_TYPE_EXTRA_INFO);
            investEnterprisesInfo.get(investEnterprisesInfo.size() - 1).put("enterprise_name","");

            PdfPTable enterpriseRelativeChart = new EnterpriseRelativeChart(writer,enterpriseName,stockholdersInfo,investEnterprisesInfo);
            enterpriseRelativeChart.setWidthPercentage(100);

            PdfPCell enterpriseRelativeChartCell = new AlignLeftCell();
            enterpriseRelativeChartCell.addElement(enterpriseRelativeChart);
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
