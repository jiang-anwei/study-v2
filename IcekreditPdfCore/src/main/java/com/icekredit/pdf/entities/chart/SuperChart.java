package com.icekredit.pdf.entities.chart;

import com.icekredit.pdf.entities.chart.part.BasePart;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * 所有图表模块的容器,其也是图表模块基类的一个实现类
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class SuperChart extends BasePart {
    @Override
    public void draw(PdfContentByte canvas) {
        for (BasePart subPart : subParts) {
            subPart.draw(canvas);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
