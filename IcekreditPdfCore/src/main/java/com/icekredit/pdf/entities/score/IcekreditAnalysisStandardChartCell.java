package com.icekredit.pdf.entities.score;

import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 冰鉴分析冰鉴总分数参考标准的图表模型
 */
public class IcekreditAnalysisStandardChartCell extends AlignCenterCell implements PdfPCellEvent{
    private PdfWriter pdfWriter = null;

    private String[] stageNames = null;

    private static final String[] DEFAULT_STAGES_NAMES = new String[]{"很差", "差", "中", "良", "优"};

    private int[] stagesValues = null;

    private static final int[] DEFAULT_STAGES_VALUES = new int[]{0,20, 60, 70, 90, 100};

    private int stageValueMax = 0;
    private int stageValueMin = 0;

    private static final int DEFAULT_STAGE_VALUE_MIN = 100;
    private static final int DEFAULT_STAGE_VALUE_MAX = 100;

    private static final int DEFAULT_PADDING = 5;

    private int currentStageValue;

    private BaseColor[] stagesRepresentColors = null;
    private static final BaseColor[] DEFAULT_STAGES_REPRESENT_COLORS = new BaseColor[]{
            new BaseColor(0xbf, 0x80, 0x00, 0xff),
            new BaseColor(0x98, 0xbf, 0x21, 0xff),
            new BaseColor(0xbf, 0x80, 0x00, 0xff),
            new BaseColor(0x98, 0xbf, 0x21, 0xff),
            new BaseColor(0xbf, 0x80, 0x00, 0xff)
    };

    private int stateBarMaxHeight = 0;

    public IcekreditAnalysisStandardChartCell(PdfWriter pdfWriter, int stateBarMaxHeight, int currentStageValue)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxHeight,currentStageValue, DEFAULT_STAGES_NAMES, DEFAULT_STAGES_VALUES,
                DEFAULT_STAGE_VALUE_MAX,DEFAULT_STAGE_VALUE_MIN, DEFAULT_STAGES_REPRESENT_COLORS);
    }

    public IcekreditAnalysisStandardChartCell(PdfWriter pdfWriter, int stateBarMaxHeight, int currentStageValue, String[] stageNames, int[] stagesValues)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxHeight,currentStageValue, stageNames, stagesValues,
                DEFAULT_STAGE_VALUE_MAX, DEFAULT_STAGE_VALUE_MIN,DEFAULT_STAGES_REPRESENT_COLORS);
    }

    public IcekreditAnalysisStandardChartCell(PdfWriter pdfWriter, int stateBarMaxHeight, int currentStageValue, String[] stageNames, int[] stagesValues,
                                       int stageValueMax,int stageValueMin) throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxHeight, currentStageValue,stageNames, stagesValues,
                stageValueMax,stageValueMin, DEFAULT_STAGES_REPRESENT_COLORS);
    }

    public IcekreditAnalysisStandardChartCell(PdfWriter pdfWriter,int stateBarMaxHeight,
                                              int currentStageValue, String[] stageNames, int[] stagesValues,
                                       int stageValueMax,int stageValueMin, BaseColor[] stagesRepresentColors)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxHeight,currentStageValue, stageNames, stagesValues,
                stageValueMax,stageValueMin, stagesRepresentColors,DEFAULT_PADDING);
    }

    public IcekreditAnalysisStandardChartCell(PdfWriter pdfWriter,int stateBarMaxHeight,
                                              int currentStageValue, String[] stageNames, int[] stagesValues,
                                              int stageValueMax,int stageValueMin, BaseColor[] stagesRepresentColors,int padding)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxHeight,currentStageValue, stageNames, stagesValues,
                stageValueMax, stageValueMin,stagesRepresentColors,
                padding,DEFAULT_PADDING,DEFAULT_PADDING,DEFAULT_PADDING,DEFAULT_PADDING);
    }

    public IcekreditAnalysisStandardChartCell(PdfWriter pdfWriter,int stateBarMaxHeight, int currentStageValue, String[] stageNames, int[] stagesValues,
                                              int stageValueMax,int stageValueMin, BaseColor[] stagesRepresentColors,
                                              int padding,int paddingTop,int paddingBottom,int paddingLeft,int paddingRight)
            throws IOException, DocumentException {
        this.pdfWriter = pdfWriter;
        this.stageNames = stageNames;
        this.stagesValues = stagesValues;
        this.stageValueMin = stageValueMin;
        this.stageValueMax = stageValueMax;

        this.currentStageValue = currentStageValue;

        this.stagesRepresentColors = stagesRepresentColors;

        this.stateBarMaxHeight = stateBarMaxHeight;

        this.setPadding(padding);
        this.setPaddingTop(paddingTop);
        this.setPaddingBottom(paddingBottom);
        this.setPaddingLeft(paddingLeft);
        this.setPaddingRight(paddingRight);

        this.setFixedHeight(this.stateBarMaxHeight * 3 + this.getPaddingTop() + this.getPaddingBottom());

        this.setCellEvent(this);
        this.setHorizontalAlignment(Element.ALIGN_CENTER);
        this.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] canvases) {
        try{
            new IceKreditIndexStandardChart(canvases[PdfPTable.BACKGROUNDCANVAS].getPdfWriter(),
                    (int)(rectangle.getWidth()- this.getPaddingLeft() - this.getPaddingRight()),
                    this.currentStageValue,
                    this.stateBarMaxHeight,
                            this.stageNames,
                            this.stagesValues,
                            this.stageValueMax,
                            this.stageValueMin,
                            this.stagesRepresentColors,
                            rectangle.getLeft() + this.getPaddingLeft(),
                            rectangle.getBottom() + this.getPaddingBottom());
        }catch (Exception e){
            System.out.print("IceKreditIndexStandardChartCell:cellLayout:" + e.getMessage());
        }
    }

    public static void main(String [] args){
        String destPdfFilePath = "results/object/test.pdf";
        File file = new File(destPdfFilePath);
        file.getParentFile().mkdirs();

        Document document = null;
        try {
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);
            pdfPTable.setWidthPercentage(100);

            IcekreditAnalysisStandardChartCell chartCell = new IcekreditAnalysisStandardChartCell(writer,16,500,
                    new String[]{"高危","差","一般","良好","优秀","极佳"},
                    new int[]{300, 400, 530, 630, 680, 710, 850},
                    850,
                    300,
                    new BaseColor[]{
                            ColorUtil.strRGBAToColor("0xf36367ff"),
                            ColorUtil.strRGBAToColor("0xffbd78ff"),
                            ColorUtil.strRGBAToColor("0x92bf54ff"),
                            ColorUtil.strRGBAToColor("0x27d6bbff"),
                            ColorUtil.strRGBAToColor("0x1daffcff"),
                            ColorUtil.strRGBAToColor("0x0651ffff")
                    });
            chartCell.setColspan(1);
            chartCell.setNoWrap(false);

            pdfPTable.addCell(chartCell);

            PdfPCell pdfPCell = new PdfPCell(new Phrase(new Chunk("Test", FontUtils.chineseFont)));
            pdfPCell.setColspan(12);
            pdfPTable.addCell(pdfPCell);

            document.add(pdfPTable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
