package com.icekredit.pdf.entities.core;

import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Created by icekredit on 7/20/16.
 */
public interface BaseConfig {
    void draw(PdfContentByte canvas);
}
