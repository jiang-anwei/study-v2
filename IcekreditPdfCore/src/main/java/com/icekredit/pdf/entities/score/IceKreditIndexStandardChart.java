package com.icekredit.pdf.entities.score;

import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by icekredit on 3/15/16.
 */
public class IceKreditIndexStandardChart {
    private PdfWriter pdfWriter = null;

    private String[] stageNames = null;

    private static final String[] DEFAULT_STAGES_NAMES = new String[]{"很差", "差", "中", "良", "优"};

    private int[] stagesValues = null;

    private static final int[] DEFAULT_STAGES_VALUES = new int[]{0,20, 60, 70, 90, 100};

    private int stageValueMin = 0;
    private static final int DEFAULT_STAGE_VALUE_MIN = 0;

    private int stageValueMax = 0;

    private static final int DEFAULT_STAGE_VALUE_MAX = 100;

    private int currentStageValue = 0;
    private static final int DEFAULT_CURRENT_STAGE_VALUE = 62;

    private BaseColor[] stagesRepresentColors = null;
    private static final BaseColor[] DEFAULT_STAGES_REPRESENT_COLORS = new BaseColor[]{
            ColorUtil.strRGBAToColor("0xf36367ff"),
            ColorUtil.strRGBAToColor("0xffbd78ff"),
            ColorUtil.strRGBAToColor("0x92bf54ff"),
            ColorUtil.strRGBAToColor("0x27d6bbff"),
            ColorUtil.strRGBAToColor("0x1daffcff"),
            ColorUtil.strRGBAToColor("0x0651ffff"),
    };

    private float llx;
    private float lly;

    private int stateBarMaxWidth = 0;
    private static final int DEFAULT_STATE_BAR_MAX_WIDTH = 400;

    private int stateBarMaxHeight = 0;
    private static final int DEFAULT_STATE_BAR_MAX_HEIGHT = 16;

    private int spacing = 5;


    public IceKreditIndexStandardChart(PdfWriter pdfWriter, int stateBarMaxWidth, int currentStageValue,int stateBarMaxHeight)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxWidth, stateBarMaxHeight,currentStageValue, DEFAULT_STAGES_NAMES, DEFAULT_STAGES_VALUES,
                DEFAULT_STAGE_VALUE_MAX, DEFAULT_STAGE_VALUE_MIN,DEFAULT_STAGES_REPRESENT_COLORS);
    }

    public IceKreditIndexStandardChart(PdfWriter pdfWriter, int stateBarMaxWidth,
                                       int currentStageValue, int stateBarMaxHeight, String[] stageNames, int[] stagesValues)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxWidth, currentStageValue,stateBarMaxHeight, stageNames, stagesValues,
                DEFAULT_STAGE_VALUE_MAX,DEFAULT_STAGE_VALUE_MIN, DEFAULT_STAGES_REPRESENT_COLORS);
    }

    public IceKreditIndexStandardChart(PdfWriter pdfWriter, int stateBarMaxWidth, int currentStageValue,
                                       int stateBarMaxHeight, String[] stageNames, int[] stagesValues,
                                       int stageValueMax,int stageValueMin) throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxWidth, stateBarMaxHeight, currentStageValue,stageNames, stagesValues,
                stageValueMax,stageValueMin, DEFAULT_STAGES_REPRESENT_COLORS);
    }

    public IceKreditIndexStandardChart(PdfWriter pdfWriter, int stateBarMaxWidth,
                                       int currentStageValue,int stateBarMaxHeight, String[] stageNames, int[] stagesValues,
                                       int stageValueMax,int stageValueMin, BaseColor[] stagesRepresentColors)
            throws IOException, DocumentException {
        this(pdfWriter, stateBarMaxWidth, currentStageValue,
                stateBarMaxHeight,stageNames, stagesValues,
                stageValueMax,stageValueMin, stagesRepresentColors,0,0);
    }

    public IceKreditIndexStandardChart(PdfWriter pdfWriter, int stateBarMaxWidth,
                                       int currentStageValue, int stateBarMaxHeight, String[] stageNames, int[] stagesValues,
                                       int stageValueMax, int stageValueMin,BaseColor[] stagesRepresentColors,float llx,float lly)
            throws IOException, DocumentException {
        this.pdfWriter = pdfWriter;
        this.stageNames = stageNames;
        this.stagesValues = stagesValues;
        this.stageValueMax = stageValueMax;
        this.stageValueMin = stageValueMin;
        this.stagesRepresentColors = stagesRepresentColors;

        this.stateBarMaxWidth = stateBarMaxWidth;
        this.currentStageValue = currentStageValue;
        this.stateBarMaxHeight = stateBarMaxHeight;

        if(this.currentStageValue < stageValueMin){
            this.currentStageValue = stageValueMin;
        }
        if(this.currentStageValue > stageValueMax){
            this.currentStageValue = stageValueMax;
        }

        this.llx = llx;
        this.lly = lly;

        this.generateIceKreditIndexStandardChart();
    }

    private void generateIceKreditIndexStandardChart()
            throws IOException, DocumentException {
        PdfContentByte canvas = pdfWriter.getDirectContent();
        ColumnText stateRangeDesc = null;
        ColumnText stateRageComment = null;

        float[] stateBarStateRectangleVertices = getStateBarStateRectangleVertices();

        float llx = 0;
        float lly = 0;
        float urx = 0;
        float ury = 0;

        int index = 0;
        Font chineseFont = new Font(FontUtils.baseFontChinese,8,Font.NORMAL,new BaseColor(0x9f,0xa0,0xa0,0xff));
        for (; index < stageNames.length; index++) {
            llx = stateBarStateRectangleVertices[index * 4 + 0];
            lly = stateBarStateRectangleVertices[index * 4 + 1];
            urx = stateBarStateRectangleVertices[index * 4 + 2];
            ury = stateBarStateRectangleVertices[index * 4 + 3];

            Rectangle stateRageRectangle = new Rectangle(llx, lly, urx, ury);
            stateRageRectangle.setBackgroundColor(stagesRepresentColors[index]);
            stateRageRectangle.setBorder(Rectangle.BOX);
            stateRageRectangle.setBorderWidth(0.2f);
            stateRageRectangle.setBorderColor(new GrayColor(0.9f));
            canvas.rectangle(stateRageRectangle);


            stateRangeDesc = new ColumnText(canvas);
            stateRangeDesc.setSimpleColumn(new Phrase(stagesValues[index] + "", chineseFont),
                    llx - ((index == 0) ? 0 : (stagesValues[index] + "").length() * FontUtils.fontSize / 2),
                    ury - spacing * 2 + stateBarMaxHeight,
                    urx + ((index == 0) ? 0 : (stagesValues[index] + "").length() * FontUtils.fontSize / 2),
                    ury - spacing * 2 + stateBarMaxHeight,
                    0, Element.ALIGN_LEFT);

            stateRangeDesc.go();

            float rangeCommentWidth = FontUtils.baseFontChinese.getWidthPoint(stageNames[index],chineseFont.getSize());

            stateRageComment = new ColumnText(canvas);
            stateRageComment.setSimpleColumn(new Phrase(stageNames[index], chineseFont),
                    (llx + urx) / 2 - rangeCommentWidth / 2,
                    lly - spacing * 2 - stateBarMaxHeight * 3 / 4,
                    (llx + urx) / 2 + rangeCommentWidth / 2,
                    ury - spacing * 2 - stateBarMaxHeight * 3 / 4, 0, Element.ALIGN_CENTER);

            stateRageComment.go();
        }

        stateRangeDesc = new ColumnText(canvas);
        stateRangeDesc.setSimpleColumn(new Phrase(stagesValues[index] + "", chineseFont),
                urx - (stagesValues[index] + "").length() * FontUtils.fontSize,
                ury - spacing * 2 + stateBarMaxHeight,
                urx,
                ury - spacing * 2 + stateBarMaxHeight,
                0, Element.ALIGN_RIGHT);
        stateRangeDesc.go();


        float [] currentStageIndicatorVertices = getCurrentStageIndicatorVertices();
        canvas.saveState();
        canvas.setColorFill(new BaseColor(0x55,0x55,0x55,0xff));
        canvas.setGrayStroke(0.9f);
        canvas.moveTo(currentStageIndicatorVertices[0],currentStageIndicatorVertices[1]);
        canvas.lineTo(currentStageIndicatorVertices[2],currentStageIndicatorVertices[3]);
        canvas.lineTo(currentStageIndicatorVertices[4],currentStageIndicatorVertices[5]);
        canvas.lineTo(currentStageIndicatorVertices[0],currentStageIndicatorVertices[1]);
        canvas.closePathFillStroke();
        canvas.restoreState();
    }

    public float[] getCurrentStageIndicatorVertices(){
        float[] currentStageIndicatorVertices = new float[6];   //llx lly urx ury

        float radius = 6;
        float centerXPosition = llx + (currentStageValue - stageValueMin) * 1.0f / (stageValueMax - stageValueMin) * stateBarMaxWidth;
        float centerYPosition = lly + stateBarMaxHeight * 1.2f + radius;


        currentStageIndicatorVertices[0] = centerXPosition;
        currentStageIndicatorVertices[1] = centerYPosition;

        float firstVertexAngleOffset = (float) - (Math.PI * 5 / 6);
        for (int index = 0; index < 2; index++) {
            currentStageIndicatorVertices[index * 2 + 2] = (float) (centerXPosition +
                    radius * Math.cos(firstVertexAngleOffset + index * Math.PI * 2 / 3));
            currentStageIndicatorVertices[index * 2 + 3] = (float) (centerYPosition +
                    radius * Math.sin(firstVertexAngleOffset + index * Math.PI * 2 / 3));
        }

        return currentStageIndicatorVertices;
    }

    public float[] getStateBarStateRectangleVertices() {
        if (stageNames.length == 0) {
            throw new RuntimeException("抱歉，用于构建statebar的属性名称为空！");
        } else if (stageNames.length != stagesValues.length - 1) {
            throw new RuntimeException("抱歉，用于构建statebar的键值对数目不匹配！");
        }

        float[] stateBarStateRectangleVertices = new float[stageNames.length * 4];   //llx lly urx ury

        for (int index = 0; index < stageNames.length; index++) {
            stateBarStateRectangleVertices[index * 4 + 0] = llx + ((index == 0) ? 0 : stateBarMaxWidth * (
                    (stagesValues[index] - stageValueMin) * 1.0f / (stageValueMax - stageValueMin)));
            stateBarStateRectangleVertices[index * 4 + 1] = lly + stateBarMaxHeight * 1.6f;
            stateBarStateRectangleVertices[index * 4 + 2] = llx + stateBarMaxWidth * (
                    (stagesValues[index + 1] - stageValueMin) * 1.0f / (stageValueMax - stageValueMin));
            stateBarStateRectangleVertices[index * 4 + 3] = lly + stateBarMaxHeight * 2f;
        }

        return stateBarStateRectangleVertices;
    }

    public static void main(String[] args) throws IOException,DocumentException{
        String destPdfFilePath = "results/object/test.pdf";
        File file = new File(destPdfFilePath);
        file.getParentFile().mkdirs();

        Document document = null;
        document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

        document.open();

        IceKreditIndexStandardChart chart = new IceKreditIndexStandardChart(writer,400,850,16,
                new String[]{"高危", "差", "一般", "良好", "优秀", "极佳"},
                new int[]{300, 490, 560, 640, 710, 740, 850},
                850,
                300,
                new BaseColor[]{
                        ColorUtil.strRGBAToColor("0xf36367ff"),
                        ColorUtil.strRGBAToColor("0xffbd78ff"),
                        ColorUtil.strRGBAToColor("0x92bf54ff"),
                        ColorUtil.strRGBAToColor("0x27d6bbff"),
                        ColorUtil.strRGBAToColor("0x1daffcff"),
                        ColorUtil.strRGBAToColor("0x0651ffff"),
                });

        document.close();
    }
}
