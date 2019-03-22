package com.icekredit.pdf.entities;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * Created by icekredit on 7/25/16.
 */
public class BaseCell extends PdfPCell {
    public BaseCell() {
        initAlignCenterCellConfig();
    }

    public BaseCell(Phrase phrase) {
        super(phrase);

        initAlignCenterCellConfig();
    }

    private void initAlignCenterCellConfig(){
        this.setHorizontalAlignment(Element.ALIGN_CENTER);
        this.setVerticalAlignment(Element.ALIGN_MIDDLE);
        this.setNoWrap(false);
        this.setBorder(PdfPCell.NO_BORDER);
        this.setPadding(0);
    }
}
