package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.List;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.chart.partdata.BackgroundPartData;
import com.icekredit.pdf.entities.chart.partdata.BasePartData;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 图表背景模块实现类，图表背景模块由深浅相间的矩形来表示
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class BackgroundPart extends BasePart {

    /**
     * 无参构造函数
     *
     */
    public BackgroundPart() {}

    /**
     * 带参构造函数
     *
     *
     * @param basePartData 与当前图表背景模块对应的图表背景模块数据对象
     */
    public BackgroundPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 图表背景模块左下角x坐标位置
     * @param lly 图表背景模块左下角y坐标位置
     * @param partWidth 图表背景模块宽度
     * @param partHeight 图表背景模块高度
     * @param basePartData 与当前图表背景模块对应的图表背景模块数据对象
     */
    public BackgroundPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 图表背景模块左下角x坐标位置
     * @param lly 图表背景模块左下角y坐标位置
     * @param partWidth 图表背景模块宽度
     * @param partHeight 图表背景模块高度
     * @param basePartData 与当前图表背景模块对应的图表背景模块数据对象
     * @param parentPart 当前图表背景模块依附的父模块
     * @param subParts 当前图表背景模块附属的子模块（应该为空，当前图表背景模块不存在子模块）
     */
    public BackgroundPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                          BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        if (this.getParentPart() != null) {
            this.llx += this.getParentPart().llx;
            this.lly += this.getParentPart().lly;
        }

        BackgroundPartData backgroundPartData           = (BackgroundPartData) basePartData;
        BaseColor          backgroundColorDeep          = new BaseColor(0xe7, 0xf9, 0xff, 0xff);
        BaseColor          backgroundColorShallow       = new BaseColor(0xff, 0xff, 0xff, 0xff);
        Point[]            backgroundRectanglesVertices =
            getBackgroundRectanglesVertices(backgroundPartData.properShownLevel);

        canvas.saveState();

        for (int index = 0; index < backgroundPartData.properShownLevel; index++) {
            if (index % 2 == (backgroundPartData.properShownLevel % 2)) {
                canvas.setLineWidth(0.1f);
                canvas.setColorStroke(backgroundColorShallow);
                canvas.setColorFill(backgroundColorShallow);
            } else {
                canvas.setLineWidth(0.1f);
                canvas.setColorStroke(backgroundColorShallow);
                canvas.setColorFill(backgroundColorDeep);
            }

            canvas.rectangle(backgroundRectanglesVertices[index * 2 + 0].x,
                             backgroundRectanglesVertices[index * 2 + 0].y,
                             backgroundRectanglesVertices[index * 2 + 1].x,
                             backgroundRectanglesVertices[index * 2 + 1].y);
            canvas.closePathFillStroke();
        }

        canvas.closePathFillStroke();
        canvas.restoreState();
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File   destFile    = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document  document  = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            BackgroundPart backgroundPart = new BackgroundPart(new BackgroundPartData(10));

            backgroundPart.setLlx(0);
            backgroundPart.setLly(0);
            backgroundPart.setPartWidth(PageSize.A4.getWidth());
            backgroundPart.setPartHeight(100);
            backgroundPart.draw(pdfWriter.getDirectContent());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前图表背景模块由深浅相间的矩形来表示
     *
     *
     * @param properShownLevel 图表背景模块展示的等级
     *
     * @return 根据展示等级以及图表背景模块当前位置计算得到的每一个背景矩形的顶点坐标
     */
    protected Point[] getBackgroundRectanglesVertices(int properShownLevel) {
        Point[] backgroundRectanglesVertices = new Point[properShownLevel * 2];

        for (int index = 0; index < properShownLevel * 2; index++) {
            backgroundRectanglesVertices[index] = new Point(0, 0);
        }

        float rectangleHeight = partHeight / properShownLevel;

        for (int index = 0; index < backgroundRectanglesVertices.length / 2; index++) {
            backgroundRectanglesVertices[index * 2 + 0].x = llx;
            backgroundRectanglesVertices[index * 2 + 0].y = lly + (index + 0) * rectangleHeight;
            backgroundRectanglesVertices[index * 2 + 1].x = partWidth;
            backgroundRectanglesVertices[index * 2 + 1].y = rectangleHeight;
        }

        return backgroundRectanglesVertices;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
