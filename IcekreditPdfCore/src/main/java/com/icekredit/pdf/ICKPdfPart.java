package com.icekredit.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * pdf子模块父类接口，所有子模块将统一实现接口void write(PdfWriter pdfWriter, PdfPTable mainFrame) throws IOException, DocumentException;
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public interface ICKPdfPart {

    /**
     * 向主框架中绘制当前模块
     * @param pdfWriter    和当前Document文档对象以及一个OutPutStream关联的PdfWriter对象
     * @param mainFrame    页面主框架，是一个占据100%宽度，被划分为十二列的表格对象
     *
     * @throws DocumentException 向mainFrame中添加子元素可能引发DocumentException
     * @throws IOException 针对流的IOException
     */
    void write(PdfWriter pdfWriter, PdfPTable mainFrame) throws IOException, DocumentException;
}


//~ Formatted by Jindent --- http://www.jindent.com
