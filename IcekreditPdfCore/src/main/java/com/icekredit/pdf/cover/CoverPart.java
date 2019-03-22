package com.icekredit.pdf.cover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.icekredit.pdf.BasePart;
import com.icekredit.pdf.ICKPdfPart;
import com.icekredit.pdf.entities.AlignCenterCell;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 封面基类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class CoverPart extends BasePart implements ICKPdfPart {

    /** 文档对象 */
    protected Document document;

    /**
     * 构造函数
     *
     *
     * @param document 文档对象
     */
    public CoverPart(Document document) {
        this.document = document;
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "results/test.pdf";
            File   destFile    = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document  document  = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);

            mainFrame.setWidthPercentage(100);

            CoverPart coverPart = new CoverPart(document);

            coverPart.write(pdfWriter, mainFrame);

            AlignCenterCell alignCenterCell = new AlignCenterCell(new Phrase(new Chunk("中文", FontUtils.chineseFont)));

            alignCenterCell.setColspan(12);

            for (int index = 0; index < 100; index++) {
                mainFrame.addCell(alignCenterCell);
            }

            document.add(mainFrame);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(PdfWriter pdfWriter, PdfPTable mainFrame) throws IOException, DocumentException {
        PdfContentByte canvas = pdfWriter.getDirectContent();

        canvas.saveState();
        canvas.setColorStroke(ColorUtil.strRGBAToColor("0x63b8ffff"));
        canvas.setColorFill(ColorUtil.strRGBAToColor("0x63b8ffff"));
        canvas.rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight());
        canvas.fillStroke();
        canvas.restoreState();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
