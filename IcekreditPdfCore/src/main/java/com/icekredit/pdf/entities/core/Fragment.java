package com.icekredit.pdf.entities.core;

import com.icekredit.pdf.entities.View;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Created by icekredit on 7/19/16.
 */
public class Fragment extends View {
    private FragmentConfig fragmentConfig;

    public Fragment(FragmentConfig fragmentConfig) {
        this.fragmentConfig = fragmentConfig;
    }

    @Override
    public void draw(PdfContentByte canvas) {
        fragmentConfig.draw(canvas);
    }

    public FragmentConfig getFragmentConfig() {
        return fragmentConfig;
    }

    public void setFragmentConfig(FragmentConfig fragmentConfig) {
        this.fragmentConfig = fragmentConfig;
    }
}
