package com.icekredit.pdf.entities;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 默认内部内容自动向左对齐的单元格
 */
public class AlignLeftCell extends PdfPCell {
    public AlignLeftCell() {
        initAlignCenterCellConfig();
    }

    public AlignLeftCell(Phrase phrase) {
        super(phrase);
        initAlignCenterCellConfig();
    }

    public AlignLeftCell(Image image) {
        super(image);
        initAlignCenterCellConfig();
    }

    public AlignLeftCell(Image image, boolean fit) {
        super(image, fit);
        initAlignCenterCellConfig();
    }

    public AlignLeftCell(PdfPTable table) {
        super(table);
        initAlignCenterCellConfig();
    }

    public AlignLeftCell(PdfPTable table, PdfPCell style) {
        super(table, style);
        initAlignCenterCellConfig();
    }

    public AlignLeftCell(PdfPCell cell) {
        super(cell);
        initAlignCenterCellConfig();
    }

    private void initAlignCenterCellConfig(){
        this.setHorizontalAlignment(Element.ALIGN_LEFT);
        this.setVerticalAlignment(Element.ALIGN_MIDDLE);
        this.setNoWrap(false);
        this.setBorder(PdfPCell.NO_BORDER);
        this.setPadding(5);
    }
}
