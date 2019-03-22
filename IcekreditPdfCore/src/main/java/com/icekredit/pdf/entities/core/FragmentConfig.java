package com.icekredit.pdf.entities.core;

import com.icekredit.pdf.utils.ColorUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import net.sf.json.JSONObject;

/**
 * Created by icekredit on 7/19/16.
 */
public class FragmentConfig implements BaseConfig {
    protected JSONObject segmentConfigJsonObj;

    public static final String layoutGravity = "layoutGravity";
    public static final String gravity = "gravity";

    public static final int LAYOUT_GRAVITY_LEFT = 1;
    public static final int LAYOUT_GRAVITY_CENTER = 2;
    public static final int LAYOUT_GRAVITY_RIGHT = 4;
    public static final int LAYOUT_GRAVITY_TOP = 8;
    public static final int LAYOUT_GRAVITY_MIDDLE = 16;
    public static final int LAYOUT_GRAVITY_BOTTOM = 32;

    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_CENTER = 2;
    public static final int GRAVITY_RIGHT = 4;
    public static final int GRAVITY_TOP = 8;
    public static final int GRAVITY_MIDDLE = 16;
    public static final int GRAVITY_BOTTOM = 32;

    //定义当前segment在布局时的优先级
    protected static final String priority = "priority";
    public static final int MAX_PRIORITY = 1;
    public static final int MIN_PRIORITY = 10;

    protected static final String margin = "margin";
    protected static final String marginLeft = "marginLeft";
    protected static final String marginRight = "marginRight";
    protected static final String marginTop = "marginTop";
    protected static final String marginBottom = "marginBottom";

    protected static final String backgroundColor = "backgroundColor";
    protected static final String foregroundColor = "foregroundColor";

    protected static final String width = "width";
    protected static final String height = "height";


    protected CellChartConfig parentCellChartConfig;

    public static FragmentConfig newInstance(CellChartConfig parentCellChartConfig){
        return new FragmentConfig(parentCellChartConfig);
    }


    protected FragmentConfig(CellChartConfig parentCellChartConfig){
        this.parentCellChartConfig = parentCellChartConfig;

        segmentConfigJsonObj = new JSONObject();

        segmentConfigJsonObj.put(FragmentConfig.layoutGravity, FragmentConfig.LAYOUT_GRAVITY_RIGHT | FragmentConfig.LAYOUT_GRAVITY_MIDDLE);
        segmentConfigJsonObj.put(FragmentConfig.gravity, FragmentConfig.GRAVITY_CENTER | FragmentConfig.GRAVITY_MIDDLE);
        segmentConfigJsonObj.put(FragmentConfig.priority, FragmentConfig.MAX_PRIORITY);

        segmentConfigJsonObj.put(FragmentConfig.margin,0);
        segmentConfigJsonObj.put(FragmentConfig.marginLeft,2);
        segmentConfigJsonObj.put(FragmentConfig.marginRight,2);
        segmentConfigJsonObj.put(FragmentConfig.marginTop,0);
        segmentConfigJsonObj.put(FragmentConfig.marginBottom,0);

        segmentConfigJsonObj.put(FragmentConfig.width,-1);
        segmentConfigJsonObj.put(FragmentConfig.height,10);

        segmentConfigJsonObj.put(FragmentConfig.backgroundColor,"ffffffff");
        segmentConfigJsonObj.put(FragmentConfig.foregroundColor,"ff8000ff");
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            //data for test
            /*Rectangle availableRetcangle = new Rectangle(parentCellChartConfig.getAvailableLLX(),
                    parentCellChartConfig.getAvailableLLY(),
                    parentCellChartConfig.getAvailableURX(),
                    parentCellChartConfig.getAvailableURY());

            canvas.saveState();

            canvas.setLineWidth(0);
            canvas.setColorFill(this.getBackgroundColor());
            canvas.setColorStroke(this.getBackgroundColor());

            canvas.rectangle(parentCellChartConfig.getAvailableLLX(),
                    parentCellChartConfig.getAvailableLLY(),
                    this.getWidth(),this.getHeight());
            canvas.fillStroke();

            canvas.restoreState();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Rectangle getAvailableRectangle(){
        if(parentCellChartConfig.getAvailableLLX() == -1 ||
                parentCellChartConfig.getAvailableURX() == -1 ||
                parentCellChartConfig.getAvailableLLY() == -1 ||
                parentCellChartConfig.getAvailableURY() == -1){
            setAvailableRectangle(new Rectangle(
                    parentCellChartConfig.getLLX() + parentCellChartConfig.getMarginLeft()
                            + parentCellChartConfig.getBorderWidthLeft() + parentCellChartConfig.getPaddingLeft(),
                    parentCellChartConfig.getLLY() + parentCellChartConfig.getMarginBottom()
                            + parentCellChartConfig.getBorderWidthBottom() + parentCellChartConfig.getPaddingBottom(),
                    parentCellChartConfig.getURX() - parentCellChartConfig.getMarginRight()
                            - parentCellChartConfig.getBorderWidthRight() - parentCellChartConfig.getPaddingRight(),
                    parentCellChartConfig.getURY() - parentCellChartConfig.getMarginTop()
                            - parentCellChartConfig.getBorderWidthTop() - parentCellChartConfig.getPaddingTop()));
        }

        Rectangle availableRetcangle = new Rectangle(parentCellChartConfig.getAvailableLLX(),
                parentCellChartConfig.getAvailableLLY(),
                parentCellChartConfig.getAvailableURX(),
                parentCellChartConfig.getAvailableURY());

        return availableRetcangle;
    }

    public void setAvailableRectangle(Rectangle availableRetcangle){
        this.parentCellChartConfig.setAvailableLLX(availableRetcangle.getLeft());
        this.parentCellChartConfig.setAvailableURX(availableRetcangle.getRight());
        this.parentCellChartConfig.setAvailableLLY(availableRetcangle.getBottom());
        this.parentCellChartConfig.setAvailableURY(availableRetcangle.getTop());
    }

    public JSONObject getSegmentConfigJsonObj() {
        return segmentConfigJsonObj;
    }

    public void setSegmentConfigJsonObj(JSONObject segmentConfigJsonObj) {
        this.segmentConfigJsonObj = segmentConfigJsonObj;
    }

    /**
     * 每次用完空间用于勾画控件之后就将使用过的空间从整体可用空间中减去
     *
     * @param position
     */
    protected void updateAvailableRectangle(Rectangle position) {
        Rectangle availableRectangle = this.getAvailableRectangle();

        if(position.getRight() < this.getAvailableRectangle().getLeft()
                && position.getRight() < this.getAvailableRectangle().getLeft()){
            //整个用掉的空间不再可用空间之中
        }else if(position.getLeft() > this.getAvailableRectangle().getRight() &&
                position.getRight() > this.getAvailableRectangle().getRight()){
            //整个用掉的空间不再可用空间之中
        }else if(position.getLeft() < this.getAvailableRectangle().getLeft()
                && position.getRight() > this.getAvailableRectangle().getLeft()){
            availableRectangle.setLeft(position.getRight());
            this.setAvailableRectangle(availableRectangle);
        }else if(position.getLeft() == this.getAvailableRectangle().getLeft()
                && position.getRight() > this.getAvailableRectangle().getLeft()){
            availableRectangle.setLeft(position.getRight());
            this.setAvailableRectangle(availableRectangle);
        }else if(position.getLeft() > this.getAvailableRectangle().getLeft() &&
                position.getRight() < this.getAvailableRectangle().getRight()){
            availableRectangle.setLeft(position.getRight());
            this.setAvailableRectangle(availableRectangle);
        }else if(position.getLeft() < this.getAvailableRectangle().getRight()
                && position.getRight() == this.getAvailableRectangle().getRight()){
            availableRectangle.setRight(position.getLeft());
            this.setAvailableRectangle(availableRectangle);
        }else if(position.getLeft() < this.getAvailableRectangle().getRight() &&
                position.getRight() > this.getAvailableRectangle().getRight()){
            availableRectangle.setRight(position.getLeft());
            this.setAvailableRectangle(availableRectangle);
        }
    }

    public int getGravity() {
        return this.segmentConfigJsonObj.getInt(FragmentConfig.gravity);
    }

    public int getLayoutGravity() {
        return this.segmentConfigJsonObj.getInt(FragmentConfig.layoutGravity);
    }

    public void setGravity(int gravity) {
        this.segmentConfigJsonObj.put(FragmentConfig.gravity,gravity);
    }

    public void setLayoutGravity(int layoutGravity) {
        this.segmentConfigJsonObj.put(FragmentConfig.layoutGravity,layoutGravity);
    }

    public float getMargin() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.margin);
    }

    public void setMargin(float margin) {
        this.segmentConfigJsonObj.put(FragmentConfig.margin,margin);
        this.segmentConfigJsonObj.put(FragmentConfig.marginLeft,margin);
        this.segmentConfigJsonObj.put(FragmentConfig.marginRight,margin);
        this.segmentConfigJsonObj.put(FragmentConfig.marginTop,margin);
        this.segmentConfigJsonObj.put(FragmentConfig.marginBottom,margin);
    }

    public CellChartConfig getParentCellChartConfig() {
        return parentCellChartConfig;
    }

    public void setParentCellChartConfig(CellChartConfig parentCellChartConfig) {
        this.parentCellChartConfig = parentCellChartConfig;
    }

    public int getPriority() {
        return this.segmentConfigJsonObj.getInt(FragmentConfig.priority);
    }

    public void setPriority(int priority) {
        this.segmentConfigJsonObj.put(FragmentConfig.priority,priority);
    }

    public float getMarginLeft() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.marginLeft);
    }

    public void setMarginLeft(float marginLeft) {
        this.segmentConfigJsonObj.put(FragmentConfig.marginLeft,marginLeft);
    }

    public float getMarginRight() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.marginRight);
    }

    public void setMarginRight(float marginRight) {
        this.segmentConfigJsonObj.put(FragmentConfig.marginRight,marginRight);
    }

    public float getMarginTop() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.marginTop);
    }

    public void setMarginTop(float marginTop) {
        this.segmentConfigJsonObj.put(FragmentConfig.marginTop,marginTop);
    }

    public float getMarginBottom() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.marginBottom);
    }

    public void setMarginBottom(float marginBottom) {
        this.segmentConfigJsonObj.put(FragmentConfig.marginBottom,marginBottom);
    }

    public BaseColor getBackgroundColor() {
        return ColorUtil.strRGBAToColor(this.segmentConfigJsonObj.getString(FragmentConfig.backgroundColor));
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.segmentConfigJsonObj.put(FragmentConfig.backgroundColor,ColorUtil.colorToStrRGBA(backgroundColor));
    }

    public BaseColor getForegroundColor() {
        return ColorUtil.strRGBAToColor(this.segmentConfigJsonObj.getString(FragmentConfig.foregroundColor));
    }

    public void setForegroundColor(BaseColor foregroundColor) {
        this.segmentConfigJsonObj.put(FragmentConfig.foregroundColor,ColorUtil.colorToStrRGBA(foregroundColor));
    }

    public float getWidth() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.width);
    }

    public void setWidth(float width) {
        this.segmentConfigJsonObj.put(FragmentConfig.width,width);
    }

    public float getHeight() {
        return (float) this.segmentConfigJsonObj.getDouble(FragmentConfig.height);
    }

    public void setHeight(float height) {
        this.segmentConfigJsonObj.put(FragmentConfig.height,height);
    }
}
