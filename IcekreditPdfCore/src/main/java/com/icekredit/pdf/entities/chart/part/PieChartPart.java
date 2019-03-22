package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.List;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.PieChartPartData;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 饼图模块，用于在整个图表中添加一个饼图，包括饼图的各个扇区，各个扇区的描述等
 *
 * PS：展示扇区描述的规则，如果扇区足够大，那么扇区描述是可以直接画到扇区上面的，
 * 但是如果扇区过小，扇区描述是无法直接画到扇区上面，此时将通过一根过当前扇区圆弧
 * 中点的连线连接到图表右下角的位置进行放置。
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class PieChartPart extends BasePart {

    /** 定义的用于区分大小扇区的阈值 */
    protected static final float THRESHOLD_SECTOR_ANGLE = 36;

    /** 用72步数来画一个整个的外圆圆弧 */
    protected static final int DEFAULT_CALC_STEP_COUNT = 72;    // 用多少步数来画一个整个的外圆圆弧

    /** 饼图圆心x坐标 */
    protected float centerXPosition;

    /** 饼图圆心的y坐标 */
    protected float centerYPosition;

    /**
     * 构造函数
     *
     */
    public PieChartPart() {}

    /**
     * 带有饼图图表数据的构造函数
     *
     *
     * @param basePartData 饼图图表数据对象
     */
    public PieChartPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 饼图图表左下角x坐标
     * @param lly 饼图图表左下角y坐标
     * @param partWidth 饼图图表宽度
     * @param partHeight 饼图图表高度
     * @param basePartData 饼图图表数据对象
     */
    public PieChartPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 饼图图表左下角x坐标
     * @param lly 饼图图表左下角y坐标
     * @param partWidth 饼图图表宽度
     * @param partHeight 饼图图表高度
     * @param basePartData 饼图图表数据对象
     * @param parentPart 当前饼图图表模块依附的父模块
     * @param subParts 当前饼图图表模块附属的子模块
     */
    public PieChartPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                        BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            centerXPosition = llx;
            centerYPosition = lly;

            if (this.getParentPart() != null) {
                centerXPosition += this.getParentPart().llx;
                centerYPosition += this.getParentPart().lly;
            }

            PieChartPartData pieChartPartData = (PieChartPartData) basePartData;

            drawSectors(canvas, pieChartPartData);

            if (pieChartPartData.isNeedDrawCover) {
                drawCover(canvas, pieChartPartData);
            }

            if (pieChartPartData.isNeedDrawDesc) {

                // 如果当前扇区比较大，那么就会有足够的空间来在扇区内部画一个描述信息，否则就依次将描述信息画在饼图右侧指定区域由下往上计算的一系列矩形区域内。
                drawSectorsDesc(canvas, pieChartPartData);
                drawRemainingSectorsDesc(canvas, pieChartPartData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为饼图添加一个cover，将饼图变成一个环图
     *
     *
     * @param canvas 画笔
     * @param pieChartPartData 与当前饼图模块关联的饼图数据对象
     */
    protected void drawCover(PdfContentByte canvas, PieChartPartData pieChartPartData) {

        // 画出覆盖圆
        canvas.saveState();
        canvas.setColorFill(new BaseColor(0xff, 0xff, 0xff, 0xff));
        canvas.setColorStroke(new BaseColor(0xee, 0xee, 0xee, 0xff));
        canvas.setLineWidth(0.1f);
        canvas.circle(centerXPosition, centerYPosition, pieChartPartData.innerCircleRadius);
        canvas.closePathFillStroke();
        canvas.restoreState();
    }

    /**
     * 画剩下的扇区角度小于指定阈值的扇区描述
     *
     *
     * @param canvas 画笔
     * @param pieChartPartData 与当前饼图模块关联的饼图数据对象
     */
    protected void drawRemainingSectorsDesc(PdfContentByte canvas, PieChartPartData pieChartPartData) {
        try {
            Point[] arcCenterVertices = getDescCenterVertices(pieChartPartData.sectorsAngle,
                    pieChartPartData.sectorsDesc,
                    centerXPosition,
                    centerYPosition,
                    pieChartPartData.outerCircleRadius,
                    1f);

            canvas.saveState();

            ColumnText columnText = null;
            Point      referPoint;
            int        currentDescCountDrawn = 1;

            for (int index = 0; index < pieChartPartData.sectorsDesc.length; index++) {
                if (pieChartPartData.sectorsAngle[index] >= THRESHOLD_SECTOR_ANGLE) {
                    continue;
                }

                referPoint = getNextDescStrLeftVertex(currentDescCountDrawn, pieChartPartData);
                canvas.setLineWidth(0.2f);
                canvas.setColorStroke(pieChartPartData.sectorDescFontColor);
                canvas.moveTo(arcCenterVertices[index].x, arcCenterVertices[index].y);
                canvas.lineTo(referPoint.x - 5, referPoint.y);
                canvas.lineTo(referPoint.x, referPoint.y);
                canvas.stroke();

                Phrase sectorDescPhrase = new Phrase(pieChartPartData.sectorsDesc[index],
                        new Font(FontUtils.baseFontChinese,
                                pieChartPartData.sectorDescFontSize,
                                pieChartPartData.sectorDescFontStyle,
                                pieChartPartData.sectorDescFontColor));
                float strWidth = FontUtils.baseFontChinese.getWidthPoint(pieChartPartData.sectorsDesc[index],
                        pieChartPartData.sectorDescFontSize) + 1;

                columnText = new ColumnText(canvas);
                columnText.setSimpleColumn(new Rectangle(referPoint.x,
                        referPoint.y
                                - DrawTextUtil.getStringHeight(
                                pieChartPartData.sectorDescFontSize) / 2,
                        referPoint.x + strWidth,
                        referPoint.y
                                + DrawTextUtil.getStringHeight(
                                pieChartPartData.sectorDescFontSize) / 2));
                columnText.setUseAscender(true);
                columnText.addText(sectorDescPhrase);
                columnText.go();
                canvas.setColorStroke(pieChartPartData.sectorsColor[index]);
                canvas.setLineWidth(0.1);
                canvas.stroke();
                currentDescCountDrawn++;
            }

            canvas.restoreState();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  画饼图的扇区
     *
     *
     * @param canvas 画笔
     * @param pieChartPartData 与当前饼图模块关联的饼图数据对象
     */
    protected void drawSectors(PdfContentByte canvas, PieChartPartData pieChartPartData) {
        float     currentSectorAngleOffset = 0;    // (float) (Math.PI / 2) * 0;
        float     currentSectorAngle;
        BaseColor currentSectorBGColor;
        Point[]   sectorVertices = null;

        canvas.saveState();
        canvas.setLineWidth(0.1);
        canvas.setColorStroke(new BaseColor(0xee, 0xee, 0xee, 0xff));

        // 画出表示比例的扇区
        for (int index = 0; index < pieChartPartData.sectorsAngle.length; index++) {
            currentSectorAngle   = pieChartPartData.sectorsAngle[index];
            currentSectorBGColor = pieChartPartData.sectorsColor[index];
            sectorVertices       = getSectorVertices(currentSectorAngleOffset,
                                                     currentSectorAngle,
                                                     pieChartPartData.outerCircleRadius,
                                                     centerXPosition,
                                                     centerYPosition);

            // 用于测试，画出所有顶点
            if (Debug.DEBUG_FLAG) {

                // testCalculatedVertices(pdfWriter,sectorVertices,sectorVertices.length);
            }

            canvas.setColorFill(currentSectorBGColor);
            canvas.moveTo(sectorVertices[0].x, sectorVertices[0].y);

            for (int position = 1; position < sectorVertices.length; position++) {
                canvas.lineTo(sectorVertices[position].x, sectorVertices[position].y);
            }

            canvas.closePathFillStroke();
            currentSectorAngleOffset += currentSectorAngle;
        }

        canvas.restoreState();
    }

    /**
     * 画扇区角度大于指定阈值的扇区的描述
     *
     *
     * @param canvas 画笔
     * @param pieChartPartData 与当前饼图模块关联的饼图数据对象
     */
    protected void drawSectorsDesc(PdfContentByte canvas, PieChartPartData pieChartPartData){
        try {
            Point[] descCenterVertices = getDescCenterVertices(pieChartPartData.sectorsAngle,
                    pieChartPartData.sectorsDesc,
                    centerXPosition,
                    centerYPosition,
                    pieChartPartData.outerCircleRadius,
                    pieChartPartData.isNeedDrawCover
                            ? 1.2f
                            : 0.8f);

            if (Debug.DEBUG_FLAG) {

                // testCalculatedVertices(pdfWriter,descCenterVertices,descCenterVertices.length);
            }

            canvas.saveState();

            ColumnText columnText = null;

            for (int index = 0; index < descCenterVertices.length; index++) {
                if (pieChartPartData.sectorsAngle[index] < THRESHOLD_SECTOR_ANGLE) {
                    continue;
                }

                Phrase sectorDescPhrase = new Phrase(pieChartPartData.sectorsDesc[index],
                        new Font(FontUtils.baseFontChinese,
                                pieChartPartData.sectorDescFontSize,
                                pieChartPartData.sectorDescFontStyle,
                                pieChartPartData.sectorDescFontColor));
                float strWidth = FontUtils.baseFontChinese.getWidthPoint(pieChartPartData.sectorsDesc[index],
                        pieChartPartData.sectorDescFontSize);

                columnText = new ColumnText(canvas);
                columnText.setSimpleColumn(new Rectangle(descCenterVertices[index].x - strWidth / 2 - 5,
                        descCenterVertices[index].y
                                - DrawTextUtil.getStringHeight(
                                pieChartPartData.sectorDescFontSize) / 2,
                        descCenterVertices[index].x + strWidth / 2 + 5,
                        descCenterVertices[index].y
                                + DrawTextUtil.getStringHeight(
                                pieChartPartData.sectorDescFontSize) / 2));
                columnText.setUseAscender(true);
                columnText.addText(sectorDescPhrase);
                columnText.go();
                canvas.setColorStroke(pieChartPartData.sectorsColor[index]);
                canvas.setLineWidth(0.1);
                canvas.stroke();
            }

            canvas.restoreState();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File   destFile    = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document  document  = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PieChartPart pieChartPart = new PieChartPart(new PieChartPartData(100,
                                                                              70,
                                                                              new float[] { 72f, 72f, 72f, 72f, 72f},
                                                                              new BaseColor[] {
                                                                                  ColorUtil.strRGBAToColor(
                                                                                      ColorUtil.DEFAUTL_COLOR_ARRAY[0]),
                                                                                  ColorUtil.strRGBAToColor(
                                                                                      ColorUtil.DEFAUTL_COLOR_ARRAY[1]),
                                                                                  ColorUtil.strRGBAToColor(
                                                                                      ColorUtil.DEFAUTL_COLOR_ARRAY[2]),
                                                                                  ColorUtil.strRGBAToColor(
                                                                                      ColorUtil.DEFAUTL_COLOR_ARRAY[2]),
                                                                                  ColorUtil.strRGBAToColor(
                                                                                      ColorUtil.DEFAUTL_COLOR_ARRAY[1]), },
                                                                              new String[] { "ss", "ss", "ss","ss",
                                                                                             "ssssss" },
                                                                              true,
                                                                              true));

            pieChartPart.setLlx(200);
            pieChartPart.setLly(300);
            pieChartPart.setPartWidth(100);
            pieChartPart.setPartHeight(100);
            pieChartPart.draw(pdfWriter.getDirectContent());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算出描述字符串中点坐标，然后以此为参考点向左右居中画一个字符串
     *
     *
     * @param sectorsAngle 扇区角度
     * @param sectorsDesc 扇区描述
     * @param centerXPosition 饼图的圆心x坐标
     * @param centerYPosition 饼图的圆心y坐标
     * @param outerCircleRadius 饼图半径
     * @param positionPercentage 用于控制描述是展示在圆内部还是圆的外侧
     *
     * @return 计算得到的需要画在扇区上面的描述信息的中点
     */
    protected Point[] getDescCenterVertices(float[] sectorsAngle, String[] sectorsDesc, float centerXPosition,
                                          float centerYPosition, float outerCircleRadius, float positionPercentage) {
        float[] sectorArcCenterVerticesAngle = getSectorArcCenterVerticesAngle(sectorsAngle);
        Point[] descCenterVertices           = new Point[sectorsDesc.length];

        for (int index = 0; index < descCenterVertices.length; index++) {
            descCenterVertices[index] = new Point(0, 0);
        }

        for (int index = 0; index < sectorsDesc.length; index++) {
            descCenterVertices[index].x = (float) (centerXPosition
                                                   + outerCircleRadius * positionPercentage
                                                     * Math.cos(sectorArcCenterVerticesAngle[index]));;
            descCenterVertices[index].y = (float) (centerYPosition
                                                   + outerCircleRadius * positionPercentage
                                                     * Math.sin(sectorArcCenterVerticesAngle[index]));;
        }

        return descCenterVertices;
    }

    /**
     * 根据当前所画的描述信息的数目确定其应该处于的位置，在画剩余不能直接展示在
     * 扇区上的描述信息时，放置策略是在右下角由下往上画描述
     *
     * @param currentDrawDescCount 当前已经勾画的描述信息数目，用于计算当前坐标的y轴位置
     * @param pieChartPartData 饼图数据对象
     *
     * @return 计算后的描述信息中点坐标
     */
    protected Point getNextDescStrLeftVertex(int currentDrawDescCount, PieChartPartData pieChartPartData) {

        // 得到右侧用于画具体描述信息的矩形区域左下角
        float llx        = centerXPosition + pieChartPartData.outerCircleRadius + 20;
        float lly        = centerYPosition - pieChartPartData.outerCircleRadius;
        float strHeight  = DrawTextUtil.getStringHeight(pieChartPartData.sectorDescFontSize);
        Point referPoint = new Point(0, 0);

        referPoint.x = llx;
        referPoint.y = lly + currentDrawDescCount * strHeight - strHeight / 2;

        return referPoint;
    }

    /**
     * 计算每一个圆弧中点所在的角度
     *
     *
     * @param sectorsAngle 扇区角度
     *
     * @return 圆弧中点所在的角度
     */
    public float[] getSectorArcCenterVerticesAngle(float[] sectorsAngle) {
        float[] sectorArcCenterVerticesAngle = new float[sectorsAngle.length];
        float   sectorAngleOffset            = (float) (Math.PI / 2) * 0;

        for (int index = 0; index < sectorsAngle.length; index++) {
            sectorArcCenterVerticesAngle[index] = (float) (sectorAngleOffset
                                                           + sectorsAngle[index] * Math.PI * 2 / 360 / 2);
            sectorAngleOffset += sectorsAngle[index] * Math.PI * 2 / 360;
        }

        return sectorArcCenterVerticesAngle;
    }

    /**
     * 计算画一个扇区需要的扇区顶点数据
     *
     *
     * @param currentSectorAngleOffset 当前扇区角度偏移
     * @param currentSectorAngle 当前扇区角度
     * @param outerCircleRadius 饼图半径
     * @param centerXPosition 饼图圆心x坐标
     * @param centerYPosition 饼图圆心y坐标
     *
     * @return 画一个扇区需要的扇区顶点数据
     */
    protected Point[] getSectorVertices(float currentSectorAngleOffset, float currentSectorAngle,
                                      float outerCircleRadius, float centerXPosition, float centerYPosition) {

        // 计算出画当前扇区需要的顶点的数目
        int     sectorVerticesCount = Math.round(currentSectorAngle / 360 * DEFAULT_CALC_STEP_COUNT + 0.5f) + 1 + 1;
        Point[] sectorVertices      = new Point[sectorVerticesCount];

        // 初始化
        for (int index = 0; index < sectorVerticesCount; index++) {
            sectorVertices[index] = new Point(0, 0);
        }

        sectorVertices[0].x = centerXPosition;
        sectorVertices[0].y = centerYPosition;

        for (int index = 1; index < sectorVerticesCount; index++) {
            sectorVertices[index].x = (float) (centerXPosition
                                               + outerCircleRadius
                                                 * Math.cos(currentSectorAngleOffset * Math.PI * 2 / 360
                                                            + (index - 1) * 1.0f / (sectorVerticesCount - 2)
                                                              * currentSectorAngle * Math.PI * 2 / 360));
            sectorVertices[index].y = (float) (centerYPosition
                                               + outerCircleRadius
                                                 * Math.sin(currentSectorAngleOffset * Math.PI * 2 / 360
                                                            + (index - 1) * 1.0f / (sectorVerticesCount - 2)
                                                              * currentSectorAngle * Math.PI * 2 / 360));
        }

        return sectorVertices;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
