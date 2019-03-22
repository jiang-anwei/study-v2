package com.icekredit.pdf.entities;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 默认内容自动靠右对齐的单元格
 */
public class AlignRightCell extends PdfPCell {
    public AlignRightCell() {
        initAlignCenterCellConfig();
    }

    public AlignRightCell(Phrase phrase) {
        super(phrase);
        initAlignCenterCellConfig();
    }

    public AlignRightCell(Image image) {
        super(image);
        initAlignCenterCellConfig();
    }

    public AlignRightCell(Image image, boolean fit) {
        super(image, fit);
        initAlignCenterCellConfig();
    }

    public AlignRightCell(PdfPTable table) {
        super(table);
        initAlignCenterCellConfig();
    }

    public AlignRightCell(PdfPTable table, PdfPCell style) {
        super(table, style);
        initAlignCenterCellConfig();
    }

    public AlignRightCell(PdfPCell cell) {
        super(cell);
        initAlignCenterCellConfig();
    }

    private void initAlignCenterCellConfig(){
        this.setHorizontalAlignment(Element.ALIGN_RIGHT);
        this.setVerticalAlignment(Element.ALIGN_MIDDLE);
        this.setNoWrap(false);
        this.setBorder(PdfPCell.NO_BORDER);
        this.setPadding(5);
    }
}
