package com.icekredit.pdf;

import java.util.ArrayList;
import java.util.List;

/**
 * Pdf子模块父类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class BasePart {


    /** 当前模块的子模块 */
    protected List<BasePart> subParts = new ArrayList<BasePart>();

    /**
     *   判断当前模块是否包含子模块
     *
     *
     * @return   subParts.size() > 0
     */
    public boolean hasContent() {
        return subParts.size() > 0;
    }

    /**
     * subParts 属性Getter
     *
     *
     * @return   当前模块的子模块
     */
    public List<BasePart> getSubParts() {
        return subParts;
    }

    /**
     * subParts属性Setter
     *
     *
     * @param subParts subParts
     */
    public void setSubParts(List<BasePart> subParts) {
        this.subParts = subParts;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
