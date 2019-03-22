package com.icekredit.pdf.entities.core;

import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import net.sf.json.JSONObject;

/**
 * Created by icekredit on 7/19/16.
 */
public class CellChartConfig implements BaseConfig {
    private JSONObject cellConfigJsonObj;

    public static final String llx = "llx";
    public static final String lly = "lly";
    public static final String urx = "urx";
    public static final String ury = "ury";

    public static final String availableLLX = "availableLLX";
    public static final String availableURX = "availableURX";
    public static final String availableLLY = "availableLLY";
    public static final String availableURY = "availableURY";

    public static final String padding = "padding";
    public static final String paddingLeft = "paddingLeft";
    public static final String paddingRight = "paddingRight";
    public static final String paddingTop = "paddingTop";
    public static final String paddingBottom = "paddingBottom";

    public static final String margin = "margin";
    public static final String marginLeft = "marginLeft";
    public static final String marginRight = "marginRight";
    public static final String marginTop = "marginTop";
    public static final String marginBottom = "marginBottom";

    public static final String borderWidth = "borderWidth";
    public static final String borderWidthLeft = "borderWidthLeft";
    public static final String borderWidthRight = "borderWidthRight";
    public static final String borderWidthTop = "borderWidthTop";
    public static final String borderWidthBottom = "borderWidthBottom";


    public static final String borderColor = "borderColor";
    public static final String borderColorLeft = "borderColorLeft";
    public static final String borderColorRight = "borderColorRight";
    public static final String borderColorTop = "borderColorTop";
    public static final String borderColorBottom = "borderColorBottom";

    public static final String backgroundColor = "backgroundColor";

    public static final float DEFAULT_PAGE_PADDING_LEFT = 36;

    public static CellChartConfig newInstance() {
        return new CellChartConfig();
    }

    private CellChartConfig() {
        cellConfigJsonObj = new JSONObject();

        cellConfigJsonObj.put(CellChartConfig.llx, 0f);
        cellConfigJsonObj.put(CellChartConfig.lly, 0f);
        cellConfigJsonObj.put(CellChartConfig.urx, 0f);
        cellConfigJsonObj.put(CellChartConfig.ury, 0f);

        cellConfigJsonObj.put(CellChartConfig.availableLLX, -1f);
        cellConfigJsonObj.put(CellChartConfig.availableURX, -1f);
        cellConfigJsonObj.put(CellChartConfig.availableLLY, -1f);
        cellConfigJsonObj.put(CellChartConfig.availableURY, -1f);

        cellConfigJsonObj.put(CellChartConfig.padding, 0f);
        cellConfigJsonObj.put(CellChartConfig.paddingLeft, 0f);
        cellConfigJsonObj.put(CellChartConfig.paddingRight, 0f);
        cellConfigJsonObj.put(CellChartConfig.paddingTop, 0f);
        cellConfigJsonObj.put(CellChartConfig.paddingBottom, 0f);

        cellConfigJsonObj.put(CellChartConfig.margin, 0f);
        cellConfigJsonObj.put(CellChartConfig.marginLeft, 0f);
        cellConfigJsonObj.put(CellChartConfig.marginRight, 0f);
        cellConfigJsonObj.put(CellChartConfig.marginTop, 0f);
        cellConfigJsonObj.put(CellChartConfig.marginBottom, 0f);

        cellConfigJsonObj.put(CellChartConfig.borderWidth, 0f);
        cellConfigJsonObj.put(CellChartConfig.borderWidthLeft, 0f);
        cellConfigJsonObj.put(CellChartConfig.borderWidthRight, 0f);
        cellConfigJsonObj.put(CellChartConfig.borderWidthTop, 0f);
        cellConfigJsonObj.put(CellChartConfig.borderWidthBottom, 0f);

        cellConfigJsonObj.put(CellChartConfig.borderColor, "ffffffff");
        cellConfigJsonObj.put(CellChartConfig.borderColorLeft, "ffffffff");
        cellConfigJsonObj.put(CellChartConfig.borderColorRight, "ffffffff");
        cellConfigJsonObj.put(CellChartConfig.borderColorTop, "ffffffff");
        cellConfigJsonObj.put(CellChartConfig.borderColorBottom, "ffffffff");

        cellConfigJsonObj.put(CellChartConfig.backgroundColor, "ffffffff");
    }

    public float getLLX() {
        return (float) cellConfigJsonObj.getDouble(CellChartConfig.llx);
    }

    public void setLLX(float llx) {
        this.cellConfigJsonObj.put(CellChartConfig.llx,llx);
    }

    public float getLLY() {
        return (float) cellConfigJsonObj.getDouble(CellChartConfig.lly);
    }

    public void setLLY(float lly) {
        this.cellConfigJsonObj.put(CellChartConfig.lly,lly);
    }

    public float getAvailableURX() {
        if((float) cellConfigJsonObj.getDouble(CellChartConfig.availableURX) == -1){
            this.cellConfigJsonObj.put(CellChartConfig.availableURX,
                    this.getURX() - this.getMarginRight() - this.getPaddingRight());
        }

        return (float) cellConfigJsonObj.getDouble(CellChartConfig.availableURX);
    }

    public void setAvailableURX(float availableURX) {
        this.cellConfigJsonObj.put(CellChartConfig.availableURX,availableURX);
    }

    public float getAvailableLLX() {
        if((float) cellConfigJsonObj.getDouble(CellChartConfig.availableLLX) == -1){
            this.cellConfigJsonObj.put(CellChartConfig.availableLLX,
                    this.getLLX() + this.getMarginLeft() + this.getPaddingLeft());
        }

        return (float) cellConfigJsonObj.getDouble(CellChartConfig.availableLLX);
    }

    public void setAvailableLLX(float availableURX) {
        this.cellConfigJsonObj.put(CellChartConfig.availableLLX,availableURX);
    }

    public float getURX() {
        return (float) cellConfigJsonObj.getDouble(CellChartConfig.urx);
    }

    public void setURX(float urx) {
        this.cellConfigJsonObj.put(CellChartConfig.urx,urx);
    }

    public float getURY() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.ury);
    }

    public void setURY(float ury) {
        this.cellConfigJsonObj.put(CellChartConfig.ury,ury);
    }

    public float getAvailableLLY() {
        if((float) this.cellConfigJsonObj.getDouble(CellChartConfig.availableLLY) == -1){
            this.cellConfigJsonObj.put(CellChartConfig.availableLLY,
                    this.getLLY() + this.getMarginBottom() + this.getBorderWidthBottom() + this.getPaddingBottom());
        }

        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.availableLLY);
    }

    public float getAvailableURY() {
        if((float) this.cellConfigJsonObj.getDouble(CellChartConfig.availableURY) == -1){
            this.cellConfigJsonObj.put(CellChartConfig.availableURY,
                    this.getURY() - this.getMarginTop() - this.getBorderWidthTop() - this.getPaddingTop());
        }

        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.availableURY);
    }

    public void setAvailableLLY(float availableLLY) {
        this.cellConfigJsonObj.put(CellChartConfig.availableLLY,availableLLY);
    }

    public void setAvailableURY(float availableURY) {
        this.cellConfigJsonObj.put(CellChartConfig.availableURY,availableURY);
    }

    public float getPadding() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.padding);
    }


    public void setPadding(float padding) {
        this.cellConfigJsonObj.put(CellChartConfig.padding,padding);
        this.cellConfigJsonObj.put(CellChartConfig.paddingLeft,padding);
        this.cellConfigJsonObj.put(CellChartConfig.paddingRight,padding);
        this.cellConfigJsonObj.put(CellChartConfig.paddingTop,padding);
        this.cellConfigJsonObj.put(CellChartConfig.paddingRight,padding);
    }


    public float getPaddingLeft() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.paddingLeft);
    }


    public void setPaddingLeft(float paddingLeft) {
        this.cellConfigJsonObj.put(CellChartConfig.paddingLeft,paddingLeft);
    }


    public float getPaddingRight() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.paddingRight);
    }


    public void setPaddingRight(float paddingRight) {
        this.cellConfigJsonObj.put(CellChartConfig.paddingRight,paddingRight);
    }


    public float getPaddingTop() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.paddingTop);
    }


    public void setPaddingTop(float paddingTop) {
        this.cellConfigJsonObj.put(CellChartConfig.paddingTop,paddingTop);
    }


    public float getPaddingBottom() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.paddingBottom);
    }


    public void setPaddingBottom(float paddingBottom) {
        this.cellConfigJsonObj.put(CellChartConfig.paddingBottom,paddingBottom);
    }

    public float getMargin() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.margin);
    }

    public void setMargin(float margin) {
        this.cellConfigJsonObj.put(CellChartConfig.margin,margin);
        this.cellConfigJsonObj.put(CellChartConfig.marginLeft,margin);
        this.cellConfigJsonObj.put(CellChartConfig.marginRight,margin);
        this.cellConfigJsonObj.put(CellChartConfig.marginTop,margin);
        this.cellConfigJsonObj.put(CellChartConfig.marginBottom,margin);
    }


    public float getMarginBottom() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.marginBottom);
    }

    public void setMarginBottom(float marginBottom) {
        this.cellConfigJsonObj.put(CellChartConfig.marginBottom,marginBottom);
    }

    public float getMarginLeft() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.marginLeft);
    }

    public void setMarginLeft(float marginLeft) {
        this.cellConfigJsonObj.put(CellChartConfig.marginLeft,marginLeft);
    }

    public float getMarginRight() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.marginRight);
    }

    public void setMarginRight(float marginRight) {
        this.cellConfigJsonObj.put(CellChartConfig.marginRight,marginRight);
    }

    public float getMarginTop() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.marginTop);
    }

    public void setMarginTop(float marginTop) {
        this.cellConfigJsonObj.put(CellChartConfig.marginTop,marginTop);
    }


    public float getBorderWidth() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.borderWidth);
    }


    public void setBorderWidth(float borderWidth) {
        this.cellConfigJsonObj.put(CellChartConfig.borderWidth,borderWidth);
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthLeft,borderWidth);
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthRight,borderWidth);
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthTop,borderWidth);
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthBottom,borderWidth);
    }


    public float getBorderWidthLeft() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.borderWidthLeft);
    }


    public void setBorderWidthLeft(float borderWidthLeft) {
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthLeft,borderWidthLeft);
    }


    public float getBorderWidthRight() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.borderWidthRight);
    }


    public void setBorderWidthRight(float borderWidthRight) {
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthRight,borderWidthRight);
    }


    public float getBorderWidthTop() {
        return (float) this.cellConfigJsonObj.getDouble(CellChartConfig.borderWidthTop);
    }


    public void setBorderWidthTop(float borderWidthTop) {
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthTop,borderWidthTop);
    }


    public float getBorderWidthBottom() {
        return (float) this.cellConfigJsonObj.getDouble(borderWidthBottom);
    }


    public void setBorderWidthBottom(float borderWidthBottom) {
        this.cellConfigJsonObj.put(CellChartConfig.borderWidthBottom,borderWidthBottom);
    }

    public BaseColor getBorderColor() {
        return ColorUtil.strRGBAToColor(this.cellConfigJsonObj.getString(CellChartConfig.borderColor));
    }

    public void setBorderColor(BaseColor borderColor) {
        this.cellConfigJsonObj.put(CellChartConfig.borderColor, ColorUtil.colorToStrRGBA(borderColor));
        this.cellConfigJsonObj.put(CellChartConfig.borderColorLeft, ColorUtil.colorToStrRGBA(borderColor));
        this.cellConfigJsonObj.put(CellChartConfig.borderColorRight, ColorUtil.colorToStrRGBA(borderColor));
        this.cellConfigJsonObj.put(CellChartConfig.borderColorTop, ColorUtil.colorToStrRGBA(borderColor));
        this.cellConfigJsonObj.put(CellChartConfig.borderColorBottom, ColorUtil.colorToStrRGBA(borderColor));
    }

    public BaseColor getBorderColorLeft() {
        return ColorUtil.strRGBAToColor(this.cellConfigJsonObj.getString(CellChartConfig.borderColorLeft));
    }

    public void setBorderColorLeft(BaseColor borderColorLeft) {
        this.cellConfigJsonObj.put(CellChartConfig.borderColorLeft, ColorUtil.colorToStrRGBA(borderColorLeft));
    }

    public BaseColor getBorderColorRight() {
        return ColorUtil.strRGBAToColor(this.cellConfigJsonObj.getString(CellChartConfig.borderColorRight));
    }

    public void setBorderColorRight(BaseColor borderColorRight) {
        this.cellConfigJsonObj.put(CellChartConfig.borderColorRight, ColorUtil.colorToStrRGBA(borderColorRight));
    }

    public BaseColor getBorderColorTop() {
        return ColorUtil.strRGBAToColor(this.cellConfigJsonObj.getString(CellChartConfig.borderColorTop));
    }

    public void setBorderColorTop(BaseColor borderColorTop) {
        this.cellConfigJsonObj.put(CellChartConfig.borderColorTop, ColorUtil.colorToStrRGBA(borderColorTop));
    }

    public BaseColor getBorderColorBottom() {
        return ColorUtil.strRGBAToColor(this.cellConfigJsonObj.getString(CellChartConfig.borderColorBottom));
    }

    public void setBorderColorBottom(BaseColor borderColorBottom) {
        this.cellConfigJsonObj.put(CellChartConfig.borderColorBottom, ColorUtil.colorToStrRGBA(borderColorBottom));
    }

    public BaseColor getBackgroundColor() {
        return ColorUtil.strRGBAToColor(this.cellConfigJsonObj.getString(CellChartConfig.backgroundColor));
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.cellConfigJsonObj.put(CellChartConfig.backgroundColor, ColorUtil.colorToStrRGBA(backgroundColor));
    }

    public JSONObject getCellConfigJsonObj() {
        return cellConfigJsonObj;
    }

    public void setCellConfigJsonObj(JSONObject cellConfigJsonObj) {
        this.cellConfigJsonObj = cellConfigJsonObj;
    }

    @Override
    public void draw(PdfContentByte canvas) {
        canvas.saveState();

        canvas.setLineWidth(0);

        //第一步，根据宽度画整个背景
        canvas.setColorFill(this.getBackgroundColor());
        canvas.setColorStroke(this.getBackgroundColor());
        canvas.rectangle(this.getLLX() + this.getMarginLeft(),
                this.getLLY() + this.getMarginBottom(),
                this.getURX() - this.getMarginRight() - this.getLLX() - this.getMarginLeft(),
                this.getURY() - this.getMarginTop() - this.getLLY() - this.getMarginBottom());
        canvas.fillStroke();

        //第二步，画border(上边框，下边框，左边框，右边框)
        canvas.setLineWidth(0);
        canvas.setColorFill(this.getBorderColorTop());
        canvas.setColorStroke(this.getBorderColorTop());
        canvas.rectangle(
                this.getLLX() + this.getMarginLeft(),
                this.getURY() - this.getBorderWidthTop() - this.getMarginTop(),
                this.getURX() - this.getMarginRight() - this.getLLX() - this.getMarginLeft(),
                this.getBorderWidthTop());
        canvas.fillStroke();

        canvas.setLineWidth(0);
        canvas.setColorFill(this.getBorderColorBottom());
        canvas.setColorStroke(this.getBorderColorBottom());
        canvas.rectangle(
                this.getLLX() + this.getMarginLeft(),
                this.getLLY() + this.getMarginTop(),
                this.getURX() - this.getMarginRight() - this.getLLX() - this.getMarginLeft(),
                this.getBorderWidthBottom());
        canvas.fillStroke();

        canvas.setLineWidth(0);
        canvas.setColorFill(this.getBorderColorLeft());
        canvas.setColorStroke(this.getBorderColorLeft());
        canvas.rectangle(
                this.getLLX() + this.getMarginLeft(),
                this.getLLY() + this.getMarginBottom(),
                this.getBorderWidthLeft(),
                this.getURY() - this.getMarginTop() - this.getLLY() - this.getMarginBottom());
        canvas.fillStroke();

        canvas.setLineWidth(0);
        canvas.setColorFill(this.getBorderColorRight());
        canvas.setColorStroke(this.getBorderColorRight());
        canvas.rectangle(
                this.getURX() - this.getMarginRight() - this.getBorderWidthRight(),
                this.getLLY() + this.getMarginBottom(),
                this.getBorderWidthRight(),
                this.getURY() - this.getMarginTop() - this.getLLY() - this.getMarginBottom());
        canvas.fillStroke();

        canvas.restoreState();
    }
}
