package com.icekredit.pdf.entities;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 默认内部内容自动居中的pdf table cell
 */
public class AlignCenterCell extends PdfPCell {
    public AlignCenterCell() {
        super();
        initAlignCenterCellConfig();
    }

    public AlignCenterCell(Phrase phrase) {
        super(phrase);
        initAlignCenterCellConfig();
    }

    public AlignCenterCell(Image image) {
        super(image);
        initAlignCenterCellConfig();
    }

    public AlignCenterCell(Image image, boolean fit) {
        super(image, fit);
        initAlignCenterCellConfig();
    }

    public AlignCenterCell(PdfPTable table) {
        super(table);
        initAlignCenterCellConfig();
    }

    public AlignCenterCell(PdfPTable table, PdfPCell style) {
        super(table, style);
        initAlignCenterCellConfig();
    }

    public AlignCenterCell(PdfPCell cell) {
        super(cell);
        initAlignCenterCellConfig();
    }

    private void initAlignCenterCellConfig(){
        this.setHorizontalAlignment(Element.ALIGN_CENTER);
        this.setVerticalAlignment(Element.ALIGN_MIDDLE);
        this.setNoWrap(false);
        this.setBorder(PdfPCell.NO_BORDER);
        this.setPadding(5);
    }
}
