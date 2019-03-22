package com.icekredit.pdf.entities.chart.part;

import java.util.List;

import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.TitlePartData;
import com.icekredit.pdf.utils.DrawTextUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * 图表标题模块
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class TitlePart extends BasePart {

    /**
     * 构造函数
     *
     */
    public TitlePart() {}

    /**
     * 带有图表标题模块数据的构造函数
     *
     *
     * @param basePartData 图表标题模块构造数据
     */
    public TitlePart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 图表标题模块左下角x坐标
     * @param lly 图表标题模块左下角y坐标
     * @param partWidth 图表标题模块宽度
     * @param partHeight 图表标题模块高度
     * @param basePartData 图表标题模块数据对象
     */
    public TitlePart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     *
     * @param llx 图表标题模块左下角x坐标
     * @param lly 图表标题模块左下角y坐标
     * @param partWidth 图表标题模块宽度
     * @param partHeight 图表标题模块高度
     * @param basePartData 图表标题模块数据对象
     * @param parentPart 当前标题模块依附夫人父模块
     * @param subParts 当前标题模块从属的子模块
     */
    public TitlePart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                     BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            TitlePartData titlePartData = (TitlePartData) basePartData;

            this.llx += this.getParentPart().llx;
            this.lly += this.getParentPart().lly;

            Font titleFont = new Font(FontUtils.baseFontChinese,
                                      10,
                                      Font.NORMAL,
                                      new BaseColor(0x59, 0x57, 0x57, 0xff));
            float      strWidth   = FontUtils.baseFontChinese.getWidthPoint(titlePartData.title,
                                                                            titleFont.getSize()) + 1;
            float      strHeight  = DrawTextUtil.getStringHeight((int) titleFont.getSize());
            Phrase     strPhrase  = new Phrase(new Chunk(titlePartData.title, titleFont));
            ColumnText columnText = new ColumnText(canvas);

            columnText.setSimpleColumn(new Rectangle(llx + partWidth / 2 - strWidth / 2,
                                                     lly + partHeight / 2 - strHeight / 2,
                                                     llx + partWidth / 2 + strWidth / 2,
                                                     lly + partHeight / 2 + strHeight / 2));
            columnText.setUseAscender(true);
            columnText.addText(strPhrase);
            columnText.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
