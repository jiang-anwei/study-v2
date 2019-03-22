package com.icekredit.pdf.entities.chart.part;

import java.util.ArrayList;
import java.util.List;

import com.icekredit.pdf.entities.chart.partdata.BasePartData;

import com.itextpdf.text.pdf.PdfContentByte;

/**
 * 所有图表模块的基类
 *
 *
 * @version        1.0, 16/10/28
 * @author         wenchao
 */
public abstract class BasePart {

    /** 当前图表模块的左下角x坐标 */
    protected float llx;

    /** 当前图表模块左下角y坐标 */
    protected float lly;

    /** 当前图表模块的宽度 */
    protected float partWidth;

    /** 当前图表模块的高度 */
    protected float partHeight;

    /** 当前图表模块附属的父模块 */
    protected BasePart parentPart;

    /** 当前图表模块的子模块 */
    protected List<BasePart> subParts;

    /** 用来描述当前图表模块的数据类 */
    protected BasePartData basePartData;

    /**
     * 构造函数，通常用来构建root图表模块
     *
     */
    public BasePart() {
        this.subParts = new ArrayList<>();
    }

    /**
     * 构造函数，通常用来构造与指定模块数据basePartData绑定的子模块
     *
     *
     * @param basePartData 指定模块数据对象
     */
    public BasePart(BasePartData basePartData) {
        this.basePartData = basePartData;
        this.subParts     = new ArrayList<>();
    }

    /**
     * 构造函数，通常用来构建root根模块
     *
     *
     * @param llx 当前图表模块的左下角x坐标
     * @param lly 当前图表模块左下角y坐标
     * @param partWidth 当前图表模块的宽度
     * @param partHeight 当前图表模块的高度
     * @param basePartData 用来描述当前图表模块的数据类
     */
    public BasePart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        this(llx, lly, partWidth, partHeight, basePartData, null, new ArrayList<BasePart>());
        this.subParts = new ArrayList<>();
    }

    /**
     * 构造函数，用来构建包含指定下一级子模块的子模块
     *
     *
     * @param llx 当前图表模块的左下角x坐标
     * @param lly 当前图表模块左下角y坐标
     * @param partWidth 当前图表模块的宽度
     * @param partHeight 当前图表模块的高度
     * @param basePartData 用来描述当前图表模块的数据类
     * @param parentPart 当前图表模块附属的父模块
     * @param subParts 当前图表模块的子模块
     */
    public BasePart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                    BasePart parentPart, List<BasePart> subParts) {
        this.llx          = llx;
        this.lly          = lly;
        this.partWidth    = partWidth;
        this.partHeight   = partHeight;
        this.basePartData = basePartData;
        this.parentPart   = parentPart;
        this.subParts     = subParts;
        this.subParts     = new ArrayList<>();
    }

    /**
     * 想当前模块中添加一个子模块
     *
     *
     * @param basePart 需要添加当前模块中的子模块
     */
    public void appendPart(BasePart basePart) {
        basePart.setParentPart(this);
        this.subParts.add(basePart);
    }

    /**
     * 勾画当前模块以及附属子模块的接口，所有特定子模块都必须实现这个接口
     *
     *
     * @param canvas 画笔对象
     */
    public abstract void draw(PdfContentByte canvas);

    /**
     * 返回与当前模块关联的用于控制当前模块展示效果的模块数据对象
     *
     *
     * @return 当前模块关联的用于控制当前模块展示效果的模块数据对象
     */
    public BasePartData getBasePartData() {
        return basePartData;
    }

    /**
     * 设置与当前模块关联的用于控制当前模块展示效果的模块数据对象
     *
     *
     * @param basePartData 当前模块关联的用于控制当前模块展示效果的模块数据对象
     */
    public void setBasePartData(BasePartData basePartData) {
        this.basePartData = basePartData;
    }

    /**
     * 返回当前图表模块的左下角x位置
     *
     *
     * @return 当前图表模块的左下角x位置
     */
    public float getLlx() {
        return llx;
    }

    /**
     * 设置当前图表模块的左下角x位置
     *
     *
     * @param llx 当前图表模块的左下角x位置
     */
    public void setLlx(float llx) {
        this.llx = llx;
    }

    /**
     * 返回当前图表模块左下角y位置
     *
     *
     * @return 当前图表模块左下角y位置
     */
    public float getLly() {
        return lly;
    }

    /**
     * 设置当前图表模块左下角y位置
     *
     *
     * @param lly 当前图表模块左下角y位置
     */
    public void setLly(float lly) {
        this.lly = lly;
    }

    /**
     * 返回当前图表模块附属的父模块
     *
     *
     * @return 当前图表模块附属的父模块
     */
    public BasePart getParentPart() {
        return parentPart;
    }

    /**
     * 设置当前图表模块附属的父模块
     *
     *
     * @param parentPart 当前图表模块附属的父模块
     */
    public void setParentPart(BasePart parentPart) {
        this.parentPart = parentPart;
    }

    /**
     * 返回当前图表模块的高度
     *
     *
     * @return 当前图表模块的高度
     */
    public float getPartHeight() {
        return partHeight;
    }

    /**
     * 设置当前图表模块的高度
     *
     *
     * @param partHeight 当前图表模块的高度
     */
    public void setPartHeight(float partHeight) {
        this.partHeight = partHeight;
    }

    /**
     * 返回当前图表模块的宽度
     *
     *
     * @return 当前图表模块的宽度
     */
    public float getPartWidth() {
        return partWidth;
    }

    /**
     * 当前图表模块的宽度
     *
     *
     * @param partWidth 当前图表模块的宽度
     */
    public void setPartWidth(float partWidth) {
        this.partWidth = partWidth;
    }

    /**
     * 返回当前图表模块的子模块
     *
     *
     * @return 当前图表模块的子模块
     */
    public List<BasePart> getSubParts() {
        return subParts;
    }

    /**
     * 设置当前图表模块的子模块
     *
     *
     * @param subParts 当前图表模块的子模块
     */
    public void setSubParts(List<BasePart> subParts) {
        this.subParts = subParts;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
