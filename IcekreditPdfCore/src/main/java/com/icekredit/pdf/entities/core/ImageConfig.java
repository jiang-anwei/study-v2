package com.icekredit.pdf.entities.core;

import com.icekredit.pdf.entities.View;
import com.icekredit.pdf.utils.Debug;
import com.icekredit.pdf.utils.ImageTools;
import com.icekredit.pdf.utils.RegularExpressionUtil;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;

/**
 * Created by icekredit on 7/20/16.
 */
public class ImageConfig extends FragmentConfig {
    protected static final String imageDataStr = "";
    protected static final String extraLink = "extraLink";

    protected static final String imageMargin = "imageMargin";
    protected static final String imageMarginLeft = "imageMarginLeft";
    protected static final String imageMarginRight = "imageMarginRight";
    protected static final String imageMarginTop = "imageMarginTop";
    protected static final String imageMarginBottom = "imageMarginBottom";


    protected static final String scaleToWidth = "scaleToWidth";
    protected static final String scaleToHeight = "scaleToHeight";

    public static ImageConfig newInstance(CellChartConfig parentCellChartConfig){
        return new ImageConfig(parentCellChartConfig);
    }

    private ImageConfig(CellChartConfig parentCellChartConfig) {
        super(parentCellChartConfig);

        this.segmentConfigJsonObj.put(ImageConfig.imageDataStr,"http://tm-image.qichacha.com/80fa10a0c06a03043be2d5fbc0e5db89.jpg@100h_160w_1l_50q");
        this.segmentConfigJsonObj.put(ImageConfig.extraLink,"#");
        this.segmentConfigJsonObj.put(ImageConfig.imageMargin,2);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginLeft,2);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginRight,2);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginTop,2);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginBottom,2);

        this.segmentConfigJsonObj.put(ImageConfig.scaleToWidth,20);
        this.segmentConfigJsonObj.put(ImageConfig.scaleToHeight,12);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        super.draw(canvas);

        drawImage(canvas);
    }

    private void drawImage(PdfContentByte canvas) {
        try {
            Image image = null;

            if(RegularExpressionUtil.isTrademarkUrl(this.getImageDataStr())){
                image = Image.getInstance(this.getImageDataStr());
            }else {
                image = Image.getInstance(ImageTools.generateImage(this.getImageDataStr()));
            }

            float primitiveImageWidth = image.getWidth();
            float primitiveImageHeight = image.getHeight();
            float imageHWRatio = primitiveImageHeight / primitiveImageWidth;

            /*float imageWidth = image.getWidth();
            float imageHeight = image.getHeight();*/

            Rectangle availableRectangle = this.getAvailableRectangle();

            /*imageWidth = imageWidth > (availableRectangle.getWidth() - this.getImageMarginLeft() - this.getImageMarginRight())
                    ? (availableRectangle.getWidth() - this.getImageMarginLeft() - this.getImageMarginRight()) : imageWidth;
            imageHeight = imageHeight > (availableRectangle.getHeight() - this.getImageMarginBottom() - this.getImageMarginTop())
                    ? (availableRectangle.getHeight() - this.getImageMarginBottom() - this.getImageMarginTop()) : imageHeight;

            imageHeight = imageHeight > imageWidth * imageHWRatio ? imageWidth * imageHWRatio : imageHeight;
            imageWidth = imageWidth > imageHeight / imageHWRatio ? imageHeight / imageHWRatio : imageWidth;*/

            float imageWHRatio = primitiveImageWidth / primitiveImageHeight;
            float imageHeight = primitiveImageHeight > Math.round(availableRectangle.getHeight() - this.getImageMarginBottom() - this.getImageMarginTop())
                    ? Math.round(availableRectangle.getHeight() - this.getImageMarginBottom() - this.getImageMarginTop()) : primitiveImageHeight;
            float imageWidth = imageHeight * imageWHRatio;

            /*imageWidth = imageWidth > (availableRectangle.getWidth() - this.getImageMarginLeft() - this.getImageMarginRight())
                    ? (availableRectangle.getWidth() - this.getImageMarginLeft() - this.getImageMarginRight()) : imageWidth;
            imageHeight = imageWidth / imageWHRatio;*/

            /*imageHeight *= 1.1;
            imageWidth *= 1.1;*/

            image.scaleAbsolute(imageWidth,imageHeight);

            ColumnText columnText = new ColumnText(canvas);
            Rectangle position = null;
            switch (this.getLayoutGravity()) {
                //如果没有指定垂直布局信息，则默认垂直居中，如果没有指定水平布局信息，则默认水平居中
                case LAYOUT_GRAVITY_TOP:
                case LAYOUT_GRAVITY_TOP | LAYOUT_GRAVITY_CENTER:
                    position = new Rectangle((availableRectangle.getLeft() + availableRectangle.getRight()) / 2 - imageWidth / 2,
                            availableRectangle.getTop() - this.getImageMarginTop() - imageHeight,
                            (availableRectangle.getLeft() + availableRectangle.getRight()) / 2 + imageWidth / 2,
                            availableRectangle.getTop() - this.getImageMarginTop());

                    View.showPosition(canvas,position);

                    columnText.setSimpleColumn(position);
                    columnText.addElement(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getLeft(),
                            availableRectangle.getBottom(),
                            (availableRectangle.getLeft() + availableRectangle.getRight()) / 2 + imageWidth / 2 + this.getImageMarginRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_MIDDLE:
                case LAYOUT_GRAVITY_CENTER:
                case LAYOUT_GRAVITY_CENTER | LAYOUT_GRAVITY_MIDDLE:
                    position = new Rectangle((availableRectangle.getLeft() + availableRectangle.getRight()) / 2 - imageWidth / 2,
                            (availableRectangle.getTop() + availableRectangle.getBottom()) / 2 - imageHeight / 2,
                            (availableRectangle.getLeft() + availableRectangle.getRight()) / 2 + imageWidth / 2,
                            (availableRectangle.getTop() + availableRectangle.getBottom()) / 2 + imageHeight / 2);

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    /*System.out.println(position.toString() + " " + position.getLeft() + " " + position.getBottom() + " "
                            + position.getRight() + " " + position.getTop() + " " + position.getWidth() + " " + position.getHeight() + " " +
                            image.getWidth() + " " + image.getHeight() + " ");*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getLeft(),
                            availableRectangle.getBottom(),
                            (availableRectangle.getLeft() + availableRectangle.getRight()) / 2 + imageWidth / 2 + this.getImageMarginRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_BOTTOM:
                case LAYOUT_GRAVITY_BOTTOM | LAYOUT_GRAVITY_CENTER:
                    position = new Rectangle((availableRectangle.getLeft() + availableRectangle.getRight()) / 2 - imageWidth / 2,
                            availableRectangle.getBottom() + this.getImageMarginBottom(),
                            (availableRectangle.getLeft() + availableRectangle.getRight()) / 2 + imageWidth / 2,
                            availableRectangle.getBottom() + this.getImageMarginBottom() + imageHeight);

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getLeft(),
                            availableRectangle.getBottom(),
                            (availableRectangle.getLeft() + availableRectangle.getRight()) / 2 + imageWidth / 2 + this.getImageMarginRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_TOP | LAYOUT_GRAVITY_LEFT:
                    position = new Rectangle(availableRectangle.getLeft() + this.getImageMarginLeft(),
                            availableRectangle.getTop() - this.getImageMarginTop() - imageHeight,
                            availableRectangle.getLeft() + this.getImageMarginLeft() + imageWidth,
                            availableRectangle.getTop() - this.getImageMarginTop());

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);


                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getLeft(),
                            availableRectangle.getBottom(),
                            availableRectangle.getLeft() + this.getImageMarginLeft() + imageWidth + this.getImageMarginRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_LEFT:
                case LAYOUT_GRAVITY_MIDDLE | LAYOUT_GRAVITY_LEFT:
                    position = new Rectangle(availableRectangle.getLeft() + this.getImageMarginLeft(),
                            (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 - imageHeight / 2,
                            availableRectangle.getLeft() + this.getImageMarginLeft() + imageWidth,
                            (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 + imageHeight / 2);

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getLeft(),
                            availableRectangle.getBottom(),
                            availableRectangle.getLeft() + this.getImageMarginLeft() + imageWidth + this.getImageMarginRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_BOTTOM | LAYOUT_GRAVITY_LEFT:
                    position = new Rectangle(availableRectangle.getLeft() + this.getImageMarginLeft(),
                            availableRectangle.getBottom() + this.getImageMarginBottom(),
                            availableRectangle.getLeft() + this.getImageMarginLeft() + imageWidth,
                            availableRectangle.getBottom() + this.getImageMarginBottom() + imageHeight);

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                        availableRectangle.getLeft(),
                            availableRectangle.getBottom(),
                            availableRectangle.getLeft() + this.getImageMarginLeft() + imageWidth + this.getImageMarginRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_TOP | LAYOUT_GRAVITY_RIGHT:
                    position = new Rectangle(availableRectangle.getRight() - this.getImageMarginRight() - imageWidth,
                            availableRectangle.getTop() - this.getImageMarginTop() - imageHeight,
                            availableRectangle.getRight() - this.getImageMarginRight(),
                            availableRectangle.getTop() - this.getImageMarginTop());

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getRight() - this.getImageMarginRight() - imageWidth - this.getImageMarginLeft(),
                            availableRectangle.getBottom(),
                            availableRectangle.getRight(),
                            availableRectangle.getTop()
                    ));

                    break;
                case LAYOUT_GRAVITY_RIGHT:
                case LAYOUT_GRAVITY_MIDDLE | LAYOUT_GRAVITY_RIGHT:
                    position = new Rectangle(availableRectangle.getRight() - this.getImageMarginRight() - imageWidth,
                            (availableRectangle.getTop() + availableRectangle.getBottom()) / 2 - imageHeight / 2,
                            availableRectangle.getRight(),
                            (availableRectangle.getTop() + availableRectangle.getBottom()) / 2 + imageHeight / 2);

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getRight() - this.getImageMarginRight() - imageWidth - this.getImageMarginLeft(),
                            availableRectangle.getBottom(),
                            availableRectangle.getRight(),
                            availableRectangle.getTop()
                    ));
                    break;
                case LAYOUT_GRAVITY_BOTTOM | LAYOUT_GRAVITY_RIGHT:
                    position = new Rectangle(availableRectangle.getRight() - this.getImageMarginRight() - imageWidth,
                            availableRectangle.getBottom() + this.getImageMarginBottom(),
                            availableRectangle.getRight() - this.getImageMarginRight(),
                            availableRectangle.getBottom() + this.getImageMarginBottom() + imageHeight);

                    View.showPosition(canvas,position);

                    /*columnText.setSimpleColumn(position);
                    columnText.addElement(image);*/

                    image.setAbsolutePosition(position.getLeft(),position.getBottom());
                    canvas.addImage(image);

                    updateAvailableRectangle(new Rectangle(
                            availableRectangle.getRight() - this.getImageMarginRight() - imageWidth - this.getImageMarginLeft(),
                            availableRectangle.getBottom(),
                            availableRectangle.getRight(),
                            availableRectangle.getTop()));
                    break;
            }
//            columnText.go();
        }catch (ConnectException e){
            Debug.debug("ConnectException:" + e.getMessage() + ",caused by:"+ e.getCause() + ":" + this.getImageDataStr());
        }catch (FileNotFoundException e){
            Debug.debug("FileNotFoundException:"  + e.getMessage() + ",caused by:"+ e.getCause() + ":" + this.getImageDataStr());
        }catch (IOException e){
            Debug.debug("IOException:"  + e.getMessage() + ",caused by:"+ e.getCause() + ":" + this.getImageDataStr());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getImageDataStr() {
        return this.segmentConfigJsonObj.getString(ImageConfig.imageDataStr);
    }

    public void setImageDataStr(String imageDataStr) {
        this.segmentConfigJsonObj.put(ImageConfig.imageDataStr,imageDataStr);
    }

    public String getExtraLink() {
        return this.segmentConfigJsonObj.getString(ImageConfig.extraLink);
    }

    public void setExtraLink(String extraLink) {
        this.segmentConfigJsonObj.put(ImageConfig.extraLink,extraLink);
    }

    public float getImageMargin() {
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.imageMargin);
    }

    public void setImageMargin(float imageMargin) {
        this.segmentConfigJsonObj.put(ImageConfig.imageMargin,imageMargin);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginLeft,imageMargin);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginRight,imageMargin);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginTop,imageMargin);
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginBottom,imageMargin);
    }

    public float getImageMarginLeft() {
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.imageMarginLeft);
    }

    public void setImageMarginLeft(float imageMarginLeft) {
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginLeft,imageMarginLeft);
    }

    public float getImageMarginRight() {
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.imageMarginRight);
    }

    public void setImageMarginRight(float imageMarginRight) {
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginRight,imageMarginRight);
    }

    public float getImageMarginTop() {
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.imageMarginTop);
    }

    public void setImageMarginTop(float imageMarginTop) {
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginTop,imageMarginTop);
    }

    public float getImageMarginBottom() {
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.imageMarginBottom);
    }

    public void setImageMarginBottom(float imageMarginBottom) {
        this.segmentConfigJsonObj.put(ImageConfig.imageMarginBottom,imageMarginBottom);
    }

    public float getScaleToWidth(){
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.scaleToWidth);
    }

    public void setScaleToWidth(float scaleToWidth){
        this.segmentConfigJsonObj.put(ImageConfig.scaleToWidth,scaleToWidth);
    }

    public float getScaleToHeight(){
        return (float) this.segmentConfigJsonObj.getDouble(ImageConfig.scaleToHeight);
    }

    public void setScaleToHeight(){
        this.segmentConfigJsonObj.put(ImageConfig.scaleToHeight,scaleToHeight);
    }
}
