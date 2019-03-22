package com.icekredit.pdf.entities.core;

import com.icekredit.pdf.entities.Point;
import com.icekredit.pdf.entities.View;
import com.icekredit.pdf.entities.mark.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.FontUtils;
import com.icekredit.pdf.utils.Md5Util;
import com.icekredit.pdf.utils.UrlUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Created by icekredit on 7/20/16.
 */
public class MessageConfig extends FragmentConfig {
    protected static final String message = "message";
    protected static final String extraLink = "extraLink";
    protected static final String fontSize = "fontSize";
    protected static final String fontStyle = "fontStyle";
    protected static final String fontColor = "fontColor";

    protected static final String identifierType = "identifierType";
    public static final int IDENTIFIER_TYPE_NONE = 0;
    public static final int IDENTIFIER_TYPE_MARK_RIGHT = 1;
    public static final int IDENTIFIER_TYPE_MARK_WRONG = 2;
    public static final int IDENTIFIER_TYPE_MARK_POSITIVE = 3;
    public static final int IDENTIFIER_TYPE_MARK_NEGETIVE = 4;
    public static final int IDENTIFIER_TYPE_MARK_UNKNOWN = 100;

    protected static final String identifierShowAs = "identifierShowAs";
    public static final int IDENTIFIER_SHOW_AS_RECTANGLE = 0;
    public static final int IDENTIFIER_SHOW_AS_CIRCLE = 1;
    public static final int IDENTIFIER_SHOW_AS_UNKNOWN = 100;

    protected static final String identifierForegroundColor = "identifierForegroundColor";
    protected static final String identifierBackgroundColor = "identifierBackgroundColor";

    protected static final String identifierMargin = "identifierMargin";

    private static final int POSITION_TO_LEFT = 0;  // 在指定参考点左侧画identifier
    private static final int POSITION_TO_RIGHT = 1;    //在指定参考点右侧画identifier

    public static MessageConfig newInstance(CellChartConfig parentCellChartConfig){
        return new MessageConfig(parentCellChartConfig);
    }

    public MessageConfig(CellChartConfig parentCellChartConfig) {
        super(parentCellChartConfig);

        this.segmentConfigJsonObj.put(MessageConfig.message, "目录");
        this.segmentConfigJsonObj.put(MessageConfig.extraLink, "");
        this.segmentConfigJsonObj.put(MessageConfig.fontSize, 8);
        this.segmentConfigJsonObj.put(MessageConfig.fontStyle, Font.NORMAL);
        this.segmentConfigJsonObj.put(MessageConfig.fontColor, "0xffffffff");
        this.segmentConfigJsonObj.put(MessageConfig.identifierType, MessageConfig.IDENTIFIER_TYPE_NONE);
        this.segmentConfigJsonObj.put(MessageConfig.identifierShowAs, MessageConfig.IDENTIFIER_SHOW_AS_CIRCLE);
        this.segmentConfigJsonObj.put(MessageConfig.identifierBackgroundColor, "0x59ca70ff");
        this.segmentConfigJsonObj.put(MessageConfig.identifierForegroundColor, "0xffffffff");
        this.segmentConfigJsonObj.put(MessageConfig.identifierMargin,0);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        super.draw(canvas);

        //第一步，取得当前cell中可用的矩形区域
        Rectangle availableRectangle = this.getAvailableRectangle();

        Font messageFont = new Font(FontUtils.baseFontChinese, this.getFontSize(), this.getFontStyle(), this.getFontColor());

        float messageWidth = FontUtils.baseFontChinese.getWidthPoint(this.getMessage(), this.getFontSize()) + 1;
        messageWidth = messageWidth > availableRectangle.getWidth() ? availableRectangle.getWidth() : messageWidth;

        float messageHeight = FontUtils.baseFontChinese.getWidthPoint("中文", this.getFontSize()) / 2;

        //画出message语句以及message语句设定的标识符
        switch (this.getLayoutGravity()) {
            //如果没有指定垂直布局信息，则默认垂直居中，如果没有指定水平布局信息，则默认水平居中
            case MessageConfig.LAYOUT_GRAVITY_TOP:
            case MessageConfig.LAYOUT_GRAVITY_TOP | MessageConfig.LAYOUT_GRAVITY_CENTER:
                /*drawIdentifier(this.getIdentifierType(),this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(),this.getIdentifierForegroundColor(),
                        new Point(availableRectangle));*/

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont,
                        new Rectangle(availableRectangle.getLeft(), availableRectangle.getTop() - messageHeight,
                                availableRectangle.getRight(), availableRectangle.getTop()), this.getGravity());

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_MIDDLE:
            case MessageConfig.LAYOUT_GRAVITY_CENTER:
            case MessageConfig.LAYOUT_GRAVITY_CENTER | MessageConfig.LAYOUT_GRAVITY_MIDDLE:
                /*drawIdentifier(this.getIdentifierType(),this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(),this.getIdentifierForegroundColor());*/

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getLeft(), (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 - messageHeight / 2,
                        availableRectangle.getRight(), (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 + messageHeight / 2
                ), this.getGravity());

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_BOTTOM:
            case MessageConfig.LAYOUT_GRAVITY_BOTTOM | MessageConfig.LAYOUT_GRAVITY_CENTER:
                /*drawIdentifier(this.getIdentifierType(),this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(),this.getIdentifierForegroundColor());*/

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getLeft(), availableRectangle.getBottom(),
                        availableRectangle.getRight(), availableRectangle.getBottom() + messageHeight
                ), this.getGravity());

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_TOP | MessageConfig.LAYOUT_GRAVITY_LEFT:
                drawIdentifier(canvas, this.getIdentifierType(), this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(), this.getIdentifierForegroundColor(),
                        new Point(availableRectangle.getLeft(), (availableRectangle.getTop() - (messageHeight / 2))),
                        POSITION_TO_RIGHT, messageHeight,this.getIdentifierMargin());

                availableRectangle = this.getAvailableRectangle();

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getLeft(),
                        availableRectangle.getTop() - messageHeight,
                        availableRectangle.getLeft() + messageWidth,
                        availableRectangle.getTop()
                ), this.getGravity(),false);

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_LEFT:
            case MessageConfig.LAYOUT_GRAVITY_MIDDLE | MessageConfig.LAYOUT_GRAVITY_LEFT:
                drawIdentifier(canvas, this.getIdentifierType(), this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(), this.getIdentifierForegroundColor(),
                        new Point(availableRectangle.getLeft(), (availableRectangle.getTop() + availableRectangle.getBottom()) / 2),
                        POSITION_TO_RIGHT, messageHeight,this.getIdentifierMargin());

                availableRectangle = this.getAvailableRectangle();

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getLeft(),
                        (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 - messageHeight / 2,
                        availableRectangle.getLeft() + messageWidth,
                        (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 + messageHeight / 2
                ), this.getGravity(),false);

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_BOTTOM | MessageConfig.LAYOUT_GRAVITY_LEFT:
                drawIdentifier(canvas, this.getIdentifierType(), this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(), this.getIdentifierForegroundColor(),
                        new Point(availableRectangle.getLeft(), (availableRectangle.getBottom() + (messageHeight / 2))),
                        POSITION_TO_RIGHT, messageHeight,this.getIdentifierMargin());

                availableRectangle = this.getAvailableRectangle();

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getLeft(),
                        availableRectangle.getBottom(),
                        availableRectangle.getLeft() + messageWidth,
                        availableRectangle.getBottom() + messageHeight
                ), this.getGravity(),false);

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_TOP | MessageConfig.LAYOUT_GRAVITY_RIGHT:

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getRight() - messageWidth,
                        availableRectangle.getTop() - messageHeight,
                        availableRectangle.getRight(),
                        availableRectangle.getTop()
                ), this.getGravity(),true);

                availableRectangle = this.getAvailableRectangle();

                drawIdentifier(canvas, this.getIdentifierType(), this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(), this.getIdentifierForegroundColor(),
                        new Point((availableRectangle.getRight()), (availableRectangle.getTop() - messageHeight / 2)),
                        POSITION_TO_LEFT, messageHeight,this.getIdentifierMargin());

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_RIGHT:
            case MessageConfig.LAYOUT_GRAVITY_MIDDLE | MessageConfig.LAYOUT_GRAVITY_RIGHT:
                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getRight() - messageWidth,
                        (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 - messageHeight / 2,
                        availableRectangle.getRight(),
                        (availableRectangle.getBottom() + availableRectangle.getTop()) / 2 + messageHeight / 2
                ), this.getGravity(),true);

                availableRectangle = this.getAvailableRectangle();

                drawIdentifier(canvas, this.getIdentifierType(), this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(), this.getIdentifierForegroundColor(),
                        new Point((availableRectangle.getRight()), (availableRectangle.getTop() + availableRectangle.getBottom()) / 2),
                        POSITION_TO_LEFT, messageHeight,this.getIdentifierMargin());

                availableRectangle = this.getAvailableRectangle();
                break;
            case MessageConfig.LAYOUT_GRAVITY_BOTTOM | MessageConfig.LAYOUT_GRAVITY_RIGHT:

                drawMessage(canvas, this.getMessage(),this.getExtraLink(), messageFont, new Rectangle(
                        availableRectangle.getRight() - messageWidth,
                        availableRectangle.getBottom(),
                        availableRectangle.getRight(),
                        availableRectangle.getBottom() + messageHeight
                ), this.getGravity(),true);

                availableRectangle = this.getAvailableRectangle();

                drawIdentifier(canvas, this.getIdentifierType(), this.getIdentifierShowAs(),
                        this.getIdentifierBackgroundColor(), this.getIdentifierForegroundColor(),
                        new Point((availableRectangle.getRight()), (availableRectangle.getBottom() + (messageHeight / 2))),
                        POSITION_TO_LEFT, messageHeight,this.getIdentifierMargin());

                availableRectangle = this.getAvailableRectangle();

                break;
        }
    }

    /**
     *  画一个消息到cell指定位置
     * @param canvas
     * @param message
     * @param link
     * @param messageFont
     * @param position
     * @param gravity
     */
    private void drawMessage(PdfContentByte canvas, String message,String link, Font messageFont, Rectangle position, int gravity,boolean ... args) {
        try {
            ColumnText columnText = new ColumnText(canvas);

            Chunk messageChunk = new Chunk(message, messageFont);
            if(UrlUtil.isValidLink(link)){
                if(UrlUtil.isDefaultLink(link)){
                    messageChunk.setAnchor(link);
                }

                if(UrlUtil.isLocalDestLink(link)){
                    messageChunk.setLocalDestination(Md5Util.MD5(UrlUtil.extractRealLink(link)));
                }

                if(UrlUtil.isLocalGotoLink(link)){
                    messageChunk.setLocalGoto(UrlUtil.extractRealLink(link));
                }

                if(UrlUtil.isRemoteLink(link)){
                    messageChunk.setAnchor(UrlUtil.extractRealLink(link));
                }
            }

            View.showPosition(canvas,position);

            columnText.setSimpleColumn(new Phrase(messageChunk),
                    position.getLeft(),
                    position.getBottom(),
                    position.getRight(),
                    position.getBottom() + position.getHeight() / 8,
                    0, Element.ALIGN_CENTER);

            columnText.go();

            if(args.length > 0){
                if(this.getWidth() != -1){
                    boolean consumeLeft = args[0];

                    if(!consumeLeft && position.getWidth() < this.getWidth()){
                        position.setRight(position.getLeft() + this.getWidth());
                    }

                    if(consumeLeft && position.getWidth() < this.getWidth()){
                        position.setLeft(position.getRight() - this.getWidth());
                    }
                }
            }

            updateAvailableRectangle(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void drawIdentifier(PdfContentByte canvas, int identifierType, int identifierShowAs,
                                BaseColor identifierBackgroundColor, BaseColor identifierForegroundColor,
                                Point referPoint, int positionTo, float outerRadius, float identifierMargin) {
        Rectangle position = null;

        outerRadius *= 0.45f;

        switch (positionTo) {
            case POSITION_TO_LEFT:
                position = new Rectangle(
                        referPoint.x - outerRadius * 2 - identifierMargin * 2,
                        referPoint.y - outerRadius / 2,
                        referPoint.x,
                        referPoint.y + outerRadius / 2);
                break;
            case POSITION_TO_RIGHT:
                position = new Rectangle(
                        referPoint.x,
                        referPoint.y - outerRadius / 2,
                        referPoint.x + identifierMargin * 2 + outerRadius * 2,
                        referPoint.y + outerRadius / 2);
                break;
        }

        switch (identifierType) {
            case IDENTIFIER_TYPE_NONE:
                break;
            case IDENTIFIER_TYPE_MARK_RIGHT:
                Mark rightMark = new RightMark((position.getLeft() + position.getRight()) / 2 + identifierMargin,
                        (position.getTop() + position.getBottom()) / 2,
                        outerRadius,identifierShowAs,identifierBackgroundColor,identifierForegroundColor);
                rightMark.draw(canvas);

                View.showPosition(canvas,position);

                updateAvailableRectangle(position);
                break;
            case IDENTIFIER_TYPE_MARK_WRONG:
                Mark wrongMark = new WrongMark((position.getLeft() + position.getRight()) / 2 + identifierMargin,
                        (position.getTop() + position.getBottom()) / 2,
                        outerRadius,identifierShowAs,identifierBackgroundColor,identifierForegroundColor);
                wrongMark.draw(canvas);

                View.showPosition(canvas,position);

                updateAvailableRectangle(position);
                break;
            case IDENTIFIER_TYPE_MARK_POSITIVE:
                Mark positiveMark = new PositiveMark((position.getLeft() + position.getRight()) / 2 + identifierMargin,
                        (position.getTop() + position.getBottom()) / 2,
                        outerRadius,identifierShowAs,identifierBackgroundColor,identifierForegroundColor);
                positiveMark.draw(canvas);

                View.showPosition(canvas,position);

                updateAvailableRectangle(position);
                break;
            case IDENTIFIER_TYPE_MARK_NEGETIVE:
                Mark negativeMark = new NegativeMark((position.getLeft() + position.getRight()) / 2 + identifierMargin,
                        (position.getTop() + position.getBottom()) / 2,
                        outerRadius,identifierShowAs,identifierBackgroundColor,identifierForegroundColor);
                negativeMark.draw(canvas);

                View.showPosition(canvas,position);

                updateAvailableRectangle(position);
                break;
            case IDENTIFIER_TYPE_MARK_UNKNOWN:
                Mark unknownMark = new UnknownMark((position.getLeft() + position.getRight()) / 2 + identifierMargin,
                        (position.getTop() + position.getBottom()) / 2,
                        outerRadius,identifierShowAs,identifierBackgroundColor,identifierForegroundColor);
                unknownMark.draw(canvas);

                View.showPosition(canvas,position);

                updateAvailableRectangle(position);
                break;
        }
    }

    public float getIdentifierMargin(){
        return (float) this.segmentConfigJsonObj.getDouble(MessageConfig.identifierMargin);
    }

    public void setIdentifierMargin(float identifierMargin){
        this.segmentConfigJsonObj.put(MessageConfig.identifierMargin,identifierMargin);
    }

    public String getMessage() {
        return this.segmentConfigJsonObj.getString(MessageConfig.message);
    }

    public void setMessage(String message) {
        this.segmentConfigJsonObj.put(MessageConfig.message, message);
    }

    public String getExtraLink() {
        return this.segmentConfigJsonObj.getString(MessageConfig.extraLink);
    }

    public void setExtraLink(String extraLink) {
        this.segmentConfigJsonObj.put(MessageConfig.extraLink, extraLink);
    }

    public int getFontSize() {
        return this.segmentConfigJsonObj.getInt(MessageConfig.fontSize);
    }

    public void setFontSize(int fontSize) {
        this.segmentConfigJsonObj.put(MessageConfig.fontSize, fontSize);
    }

    public int getFontStyle() {
        return this.segmentConfigJsonObj.getInt(MessageConfig.fontStyle);
    }

    public void setFontStyle(int fontStyle) {
        this.segmentConfigJsonObj.put(MessageConfig.fontStyle, fontStyle);
    }

    public BaseColor getFontColor() {
        return ColorUtil.strRGBAToColor(this.segmentConfigJsonObj.getString(MessageConfig.fontColor));
    }

    public void setFontColor(BaseColor fontColor) {
        this.segmentConfigJsonObj.put(MessageConfig.fontColor, ColorUtil.colorToStrRGBA(fontColor));
    }

    public int getIdentifierType() {
        return this.segmentConfigJsonObj.getInt(MessageConfig.identifierType);
    }

    public void setIdentifierType(int identifierType) {
        this.segmentConfigJsonObj.put(MessageConfig.identifierType, identifierType);
    }

    public int getIdentifierShowAs() {
        return this.segmentConfigJsonObj.getInt(MessageConfig.identifierShowAs);
    }

    public void setIdentifierShowAs(int identifierShowAs) {
        this.segmentConfigJsonObj.put(MessageConfig.identifierShowAs, identifierShowAs);
    }

    public BaseColor getIdentifierBackgroundColor() {
        return ColorUtil.strRGBAToColor(this.segmentConfigJsonObj.getString(MessageConfig.identifierBackgroundColor));
    }

    public void setIdentifierBackgroundColor(BaseColor identifierBackgroundColor) {
        this.segmentConfigJsonObj.put(MessageConfig.identifierBackgroundColor, ColorUtil.colorToStrRGBA(identifierBackgroundColor));
    }

    public BaseColor getIdentifierForegroundColor() {
        return ColorUtil.strRGBAToColor(this.segmentConfigJsonObj.getString(MessageConfig.identifierForegroundColor));
    }

    public void setIdentifierForegroundColor(BaseColor identifierForegroundColor) {
        this.segmentConfigJsonObj.put(MessageConfig.identifierBackgroundColor, ColorUtil.colorToStrRGBA(identifierForegroundColor));
    }
}
