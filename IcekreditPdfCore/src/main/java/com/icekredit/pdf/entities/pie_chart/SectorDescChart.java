package com.icekredit.pdf.entities.pie_chart;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.relative_chart.View;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by icekredit on 6/6/16.
 */
public class SectorDescChart extends View {
    protected PdfWriter pdfWriter;
    protected String desc;
    protected BaseColor descIconColor;

    protected float llx;
    protected float lly;

    protected float chartWidth;
    protected float chartHeight;

    protected static final float GOLD_SPLIT_RATE = 0.618f;

    protected static final float SPLIT_COLUMN_COUNT = 12;

    protected int identifierShownAs;

    protected int sectorDescFontSize;
    protected static final int DEFAULT_SECTOR_DESC_FONT_SIZE = 6;
    protected int sectorDescFontStyle;
    protected static final int DEFAULT_SETCTOR_DESC_FONT_STYLE = Font.NORMAL;
    protected BaseColor sectorDescFontColor;
    protected static final BaseColor DEFAULT_SECTOR_DESC_FONT_COLOR = new BaseColor(0x55,0x55,0x55,0xff);

    public SectorDescChart(PdfWriter pdfWriter, String desc, BaseColor descIconColor, float llx, float lly,
                           float chartWidth, float chartHeight,int identifierShownAs){
        this(pdfWriter, desc, descIconColor, llx, lly, chartWidth, chartHeight, identifierShownAs,
                DEFAULT_SECTOR_DESC_FONT_SIZE,DEFAULT_SETCTOR_DESC_FONT_STYLE,DEFAULT_SECTOR_DESC_FONT_COLOR);
    }

    public SectorDescChart(PdfWriter pdfWriter, String desc, BaseColor descIconColor, float llx, float lly,
                           float chartWidth, float chartHeight,int identifierShownAs,
                           int sectorDescFontSize,int sectorDescFontStyle,BaseColor sectorDescFontColor) {
        this.pdfWriter = pdfWriter;
        this.desc = desc;
        this.descIconColor = descIconColor;
        this.identifierShownAs = identifierShownAs;

        this.llx = llx;
        this.lly = lly;

        this.chartWidth = chartWidth;
        this.chartHeight = chartHeight;

        this.sectorDescFontSize = sectorDescFontSize;
        this.sectorDescFontStyle = sectorDescFontStyle;
        this.sectorDescFontColor = sectorDescFontColor;
    }

    @Override
    public void draw() {
        PdfContentByte canvas = pdfWriter.getDirectContent();

        drawDescIcon(canvas);
        drawDescText(canvas);
    }

    private void drawDescIcon(PdfContentByte canvas) {
        float descIconSize = getProperDescIconSize();
        Point position = getProperDescIconPosition(descIconSize);

        if (Debug.DEBUG_FLAG) {
            testCalculatedVertices(pdfWriter, new Point[]{position}, 1);
        }


        canvas.saveState();
        canvas.setLineWidth(0.1f);
        canvas.setColorStroke(new BaseColor(0xee, 0xee, 0xee, 0xff));
        canvas.setColorFill(descIconColor);

        switch (identifierShownAs){
            case SuperPieChartCell.IDENTIFIER_SHOWN_AS_CIRCLE:
                canvas.circle(position.x + descIconSize / 2, position.y + descIconSize / 2, descIconSize / 2);
                break;
            case SuperPieChartCell.IDENTIFIER_SHOWN_AS_RECTANGLE:
                canvas.rectangle(position.x, position.y, descIconSize, descIconSize);
                break;
        }

        canvas.fillStroke();
        canvas.restoreState();
    }

    private void drawDescText(PdfContentByte canvas) {
        try {
            Phrase percentageDescPhrase = null;
            ColumnText columnText = null;

            Font chineseFont = new Font(FontUtils.baseFontChinese,sectorDescFontSize,sectorDescFontStyle,sectorDescFontColor);

            percentageDescPhrase = new Phrase(desc, chineseFont);

            float chineseCharactorWidth = FontUtils.baseFontChinese.getWidthPoint("中文",sectorDescFontSize) / 2;

            columnText = new ColumnText(canvas);
            columnText.setSimpleColumn(new Rectangle(llx + chartWidth / SPLIT_COLUMN_COUNT,
                    lly + chartHeight / 2 - chineseCharactorWidth / 2,
                    llx + chartWidth,
                    lly + chartHeight / 2 + chineseCharactorWidth / 2));

            columnText.setUseAscender(true);
            columnText.addText(percentageDescPhrase);

            columnText.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取颜色图标矩形方块左下角的位置
     *
     * @param descIconSize
     * @return
     */
    private Point getProperDescIconPosition(float descIconSize) {
        Point position = new Point(0, 0);

        position.x = llx + chartWidth / SPLIT_COLUMN_COUNT / 2 - descIconSize / 2;
        position.y = lly + chartHeight / 2 - descIconSize / 2;

        return position;
    }

    /**
     * 获取一个比较合适的颜色图标大小
     *
     * @return
     */
    public float getProperDescIconSize() {
        float properDescIconSize;

        float descHeight = chartHeight * GOLD_SPLIT_RATE;
        float descWidth = chartWidth / SPLIT_COLUMN_COUNT * GOLD_SPLIT_RATE;

        properDescIconSize = descHeight;
        if (properDescIconSize > descWidth) {
            properDescIconSize = descWidth;
        }

        if(properDescIconSize > 10){
            properDescIconSize = 10;
        }

        if(properDescIconSize < 5){
            properDescIconSize = 5;
        }

        return properDescIconSize;
    }

    public static void main(String [] args){
        try{
            String destFileStr = "results/object/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            SectorDescChart sectorDescChart = new SectorDescChart(pdfWriter,"张三",new BaseColor(0x98,0xbf,0x21,0xff),0,0,400,10, SuperPieChartCell.IDENTIFIER_SHOWN_AS_CIRCLE);
            sectorDescChart.draw();

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
