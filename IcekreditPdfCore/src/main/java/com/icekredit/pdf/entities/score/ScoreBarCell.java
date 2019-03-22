package com.icekredit.pdf.entities.score;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.core.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by icekredit on 9/26/16.
 */
public class ScoreBarCell extends BaseCell implements PdfPCellEvent {
    protected String scoreDesc;
    protected int scoreValue;
    protected String scoreComment;
    protected BaseColor scoreBarColor;

    protected int [] scoresRangeArray;
    protected String [] scoresCommentArray;
    protected BaseColor [] scoresColorArray;

    public static final int [] DEFAULT_SCORES_RANGE_ARRAY = new int []{300,350,450,580,620,750,850};
    public static final String [] DEFAULT_SCORES_COMMENT_ARRAY = new String[]{"高危","差","一般","良好","优秀","极佳"};
    public static final BaseColor [] DEFAULT_SCORES_COLOR_ARRAY = new BaseColor[]{
            ColorUtil.strRGBAToColor("0xf56567ff"),
            ColorUtil.strRGBAToColor("0xffbd78ff"),
            ColorUtil.strRGBAToColor("0x92bf74ff"),
            ColorUtil.strRGBAToColor("0x27d6bbff"),
            ColorUtil.strRGBAToColor("0x1daffcff"),
            ColorUtil.strRGBAToColor("0x0671ffff")
    };

    protected static final Font SCORE_DESC_FONT = new Font(FontUtils.baseFontChinese,FontUtils.fontSize,Font.NORMAL,new BaseColor(0x55,0x55,0x55,0xff));
    protected static final Font SCORE_COMMENT_FONT = new Font(FontUtils.baseFontChinese,FontUtils.fontSize,Font.NORMAL,new BaseColor(0x77,0x77,0x77,0xff));
    protected static final Font SCORE_VALUE_FONT = new Font(FontUtils.baseFontChinese,18,Font.NORMAL,new BaseColor(0x99,0x99,0x99,0xff));

    protected static final BaseColor DEFAULT_CELL_BACKGROUND_COLOR = new BaseColor(0xff,0xff,0xff,0xff);

    protected static final int DEFAULT_MARGIN = 2;
    protected static final int DEFAULT_PADDING = 2;

    protected static final float DEFAULT_CELL_HEIGHT = 30;
    protected static final float DEFAULT_CELL_BAR_STRONG_WIDTH = 4;
    protected static final float DEFAULT_CELL_BAR_THIN_WIDTH = 1;

    public ScoreBarCell(String scoreDesc, int scoreValue){
        this(scoreDesc,scoreValue,ColorUtil.getScoreChartComment(scoreValue,DEFAULT_SCORES_RANGE_ARRAY,DEFAULT_SCORES_COMMENT_ARRAY));
    }

    public ScoreBarCell(String scoreDesc, int scoreValue, String scoreComment){
        this(scoreDesc,scoreValue,scoreComment,ColorUtil.getScoreChartBgColor(scoreValue,DEFAULT_SCORES_RANGE_ARRAY,DEFAULT_SCORES_COLOR_ARRAY));
    }


    public ScoreBarCell(String scoreDesc, int scoreValue, String scoreComment, BaseColor scoreBarColor) {
        this(scoreDesc, scoreValue, scoreComment, scoreBarColor,DEFAULT_SCORES_RANGE_ARRAY,DEFAULT_SCORES_COMMENT_ARRAY,DEFAULT_SCORES_COLOR_ARRAY);
    }

    public ScoreBarCell(String scoreDesc, int scoreValue, String scoreComment, BaseColor scoreBarColor, int[] scoresRangeArray, String[] scoresCommentArray, BaseColor[] scoresColorArray) {
        this.scoreDesc = scoreDesc;
        this.scoreValue = scoreValue;
        this.scoreComment = scoreComment;
        this.scoreBarColor = scoreBarColor;
        this.scoresRangeArray = scoresRangeArray;
        this.scoresCommentArray = scoresCommentArray;
        this.scoresColorArray = scoresColorArray;

        this.setFixedHeight(DEFAULT_CELL_HEIGHT + DEFAULT_MARGIN * 2);
        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            CellChartConfig cellChartConfig = CellChartConfig.newInstance();
            cellChartConfig.setBackgroundColor(DEFAULT_CELL_BACKGROUND_COLOR);
            cellChartConfig.setLLX(position.getLeft() + DEFAULT_MARGIN);
            cellChartConfig.setLLY(position.getBottom() + DEFAULT_MARGIN);
            cellChartConfig.setURX(position.getRight() - DEFAULT_MARGIN);
            cellChartConfig.setURY(position.getTop() - DEFAULT_MARGIN);

            MessageConfig scoreDescMessage = new MessageConfig(cellChartConfig);
            scoreDescMessage.setMessage(scoreDesc);
            scoreDescMessage.setFontSize((int) SCORE_DESC_FONT.getSize());
            scoreDescMessage.setMarginTop(DEFAULT_PADDING);
            scoreDescMessage.setMarginLeft(DEFAULT_PADDING);
            scoreDescMessage.setFontStyle(SCORE_DESC_FONT.getStyle());
            scoreDescMessage.setFontColor(SCORE_DESC_FONT.getColor());
            scoreDescMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
            scoreDescMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_LEFT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);

            MessageConfig scoreValueMessage = new MessageConfig(cellChartConfig);
            scoreValueMessage.setMessage(String.valueOf(scoreValue));
            scoreValueMessage.setFontSize((int) SCORE_VALUE_FONT.getSize());
            scoreValueMessage.setMarginRight(DEFAULT_PADDING);
            scoreValueMessage.setMarginBottom(DEFAULT_PADDING + 2);
            scoreValueMessage.setFontStyle(SCORE_VALUE_FONT.getStyle());
            scoreValueMessage.setFontColor(SCORE_VALUE_FONT.getColor());
            scoreValueMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
            scoreValueMessage.setWidth(FontUtils.baseFontChinese.getWidthPoint("000",SCORE_VALUE_FONT.getSize()));    //设置分数值始终占据三个字符的宽度
            scoreValueMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_BOTTOM);

            MessageConfig scoreCommentMessage = new MessageConfig(cellChartConfig);
            scoreCommentMessage.setMessage(scoreComment);
            scoreCommentMessage.setFontSize((int) SCORE_COMMENT_FONT.getSize());
            scoreCommentMessage.setMarginRight(DEFAULT_PADDING);
            scoreCommentMessage.setMarginBottom(DEFAULT_PADDING + 2);
            scoreCommentMessage.setFontStyle(SCORE_COMMENT_FONT.getStyle());
            scoreCommentMessage.setFontColor(SCORE_COMMENT_FONT.getColor());
            scoreCommentMessage.setIdentifierShowAs(MessageConfig.IDENTIFIER_TYPE_NONE);
            scoreCommentMessage.setLayoutGravity(FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_BOTTOM);

            CellChart cellChart = new CellChart(new ArrayList<Fragment>(),cellChartConfig);
            cellChart.fragments.add(new Fragment(scoreDescMessage));
            cellChart.fragments.add(new Fragment(scoreValueMessage));
            cellChart.fragments.add(new Fragment(scoreCommentMessage));

            cellChart.draw(canvases[PdfPTable.BASECANVAS]);

            PdfContentByte canvas = canvases[PdfPTable.BASECANVAS];

            canvas.saveState();
            canvas.setLineWidth(0);
            canvas.setColorStroke(scoreBarColor);
            canvas.setColorFill(scoreBarColor);

            float width = position.getWidth() - DEFAULT_MARGIN * 2;

            canvas.rectangle(position.getLeft() + DEFAULT_MARGIN,position.getBottom() + DEFAULT_MARGIN,
                    width,DEFAULT_CELL_BAR_THIN_WIDTH);
            canvas.fillStroke();

            canvas.rectangle(position.getLeft() + DEFAULT_MARGIN,position.getBottom() + DEFAULT_MARGIN,
                    width * ((scoreValue - scoresRangeArray[0]) * 1.0 / (scoresRangeArray[scoresRangeArray.length - 1] - scoresRangeArray[0]))
                    ,DEFAULT_CELL_BAR_STRONG_WIDTH);
            canvas.fillStroke();

            canvas.restoreState();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);
            destFile.getParentFile().mkdirs();

            Document document = new Document();

            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);
            mainFrame.setWidthPercentage(100);

            ScoreBarCell scoreBarCell = new ScoreBarCell("个人信息分析",666);
            scoreBarCell.setColspan(6);

            mainFrame.addCell(scoreBarCell);
            mainFrame.addCell(scoreBarCell);


            document.add(mainFrame);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
