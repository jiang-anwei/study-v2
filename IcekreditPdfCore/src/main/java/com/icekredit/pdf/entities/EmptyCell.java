package com.icekredit.pdf.entities;

import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 内容为空的单元格，偶尔用来做占位用的cell
 */
public class EmptyCell extends BaseCell {
    public EmptyCell(int defaultColumnSpan) {
        initDefaultCellConfig(defaultColumnSpan);
    }

    public EmptyCell(Phrase phrase, int defaultColumnSpan) {
        super(phrase);
        initDefaultCellConfig(defaultColumnSpan);
    }

    private void initDefaultCellConfig(int defaultColumnSpan){

        this.setBorderWidth(0);
        this.setColspan(defaultColumnSpan);
    }
}
