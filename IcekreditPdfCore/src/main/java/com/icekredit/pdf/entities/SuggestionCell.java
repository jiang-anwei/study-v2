package com.icekredit.pdf.entities;

import com.icekredit.pdf.entities.core.CellChartConfig;
import com.icekredit.pdf.utils.ColorUtil;

/**
 * Created by icekredit on 8/5/16.
 */
public class SuggestionCell extends KeyValueCell {
    public SuggestionCell(String keyStr, String valueStr) {
        super(keyStr, valueStr);

        this.keyStrColor = ColorUtil.strRGBAToColor("0x595757ff");
        this.valueStrColor = ColorUtil.strRGBAToColor("0x036eb8ff");
        this.cellHeight = 30;
        this.setFixedHeight(cellHeight);
    }

    @Override
    protected void showBorders(CellChartConfig cellChartConfig) {
        super.showBorders(cellChartConfig);

        cellChartConfig.setBorderColor(ColorUtil.strRGBAToColor("0xdff3ffff"));
        cellChartConfig.setBorderWidth(DEFAULT_VISIBLE_BORDER_WIDTH_BOTTOM);
        cellChartConfig.setMargin(2);
    }
}
