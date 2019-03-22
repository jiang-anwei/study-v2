package com.icekredit.pdf.entities.chart.partdata;

import com.icekredit.pdf.utils.Debug;

import com.itextpdf.text.Rectangle;

/**
 * 各模块描述数据基类
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public class BasePartData {

    /** 与当前模块描述数据类关联的图形模块class对象 比如 LinesPartData 同 LinesPart相关联 */
    protected Class chartPartClass;

    /** 与当前模块描述数据类关联的图形模块展示的位置 */
    protected Rectangle positionRect;

    /**
     * 检查是否有可用空间来放置当前part
     *
     *
     * @param totalRectangle 可用的全部空间
     *
     * @return 是否有足够空间放置当前part
     */
    public boolean checkSpace(Rectangle totalRectangle) {
        if ((positionRect.getRight() < totalRectangle.getLeft())
                || (positionRect.getLeft() > totalRectangle.getRight())
                || (positionRect.getTop() < totalRectangle.getBottom())
                || (positionRect.getBottom() > totalRectangle.getTop())) {
            return false;
        }

        return true;
    }

    /**
     * 每当放置完一个模块后，就在总的可用空间中减去当前模块占据的空间
     *
     *
     * @param totalRectangle 总的可用空间
     */
    public void consume(Rectangle totalRectangle) {
        try {
            if ((positionRect.getRight() < totalRectangle.getLeft())
                    || (positionRect.getLeft() > totalRectangle.getRight())
                    || (positionRect.getTop() < totalRectangle.getBottom())
                    || (positionRect.getBottom() > totalRectangle.getTop())) {
                Debug.debug("对不起，已经没有足够的空间在此处放置控件了！");
            }

            if (positionRect.getLeft() < totalRectangle.getRight()) {
                totalRectangle.setRight(positionRect.getLeft());
            }

            if (positionRect.getRight() > totalRectangle.getLeft()) {
                totalRectangle.setLeft(positionRect.getRight());
            }

            if (positionRect.getTop() > totalRectangle.getBottom()) {
                totalRectangle.setBottom(positionRect.getTop());
            }

            if (positionRect.getBottom() < totalRectangle.getTop()) {
                totalRectangle.setTop(positionRect.getBottom());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "BasePartData{" + "chartPartClass=" + chartPartClass + ", positionRect=" + positionRect + '}';
    }

    /**
     * 返回与当前模块描述数据类关联的图形模块class对象
     *
     *
     * @return 与当前模块描述数据类关联的图形模块class对象
     */
    public Class getChartPartClass() {
        return chartPartClass;
    }

    /**
     * 设置与当前模块描述数据类关联的图形模块class对象
     *
     *
     * @param chartPartClass 当前模块描述数据类关联的图形模块class对象
     */
    public void setChartPartClass(Class chartPartClass) {
        this.chartPartClass = chartPartClass;
    }

    /**
     * 返回与当前模块描述数据类关联的图形模块展示的位置
     *
     *
     * @return 与当前模块描述数据类关联的图形模块展示的位置
     */
    public Rectangle getPositionRect() {
        return positionRect;
    }


    /**
     * 设置与当前模块描述数据类关联的图形模块展示的位置
     *
     *
     * @param positionRect 与当前模块描述数据类关联的图形模块展示的位置
     */
    public void setPositionRect(Rectangle positionRect) {
        this.positionRect = positionRect;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
