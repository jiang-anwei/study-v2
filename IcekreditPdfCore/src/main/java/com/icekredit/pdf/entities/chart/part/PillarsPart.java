package com.icekredit.pdf.entities.chart.part;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.icekredit.pdf.entities.chart.partdata.BasePartData;
import com.icekredit.pdf.entities.chart.partdata.PillarsPartData;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.PdfConvertUtil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 圆柱模块
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class PillarsPart extends BasePart {

    /*
     * protected List<PillarItemPart> pillarItemDatas;
     * protected List<BaseColor> pillarsPartColors;
     *
     * protected int groupCount;    //提供的数据中共有多少个组
     * protected int itemCount;    //提供的数据中每个组有多少个pillar
     * protected int partCount;    //提供的数据中每个pillar有多少个组成部分
     */

    // protected List<List<List<Integer>>> partsRule;     //定义画图时如何消费数据，如[[[1,2],[1]]] 表示第一个pillar消费1 + 2份数据并且两个部分，第二个消费1份，且这两个pillar组成一个组

    /**
     * 构造函数
     */
    public PillarsPart() {
    }

    /**
     * 构造函数
     *
     * @param basePartData 圆柱图表数据
     */
    public PillarsPart(BasePartData basePartData) {
        super(basePartData);
    }

    /**
     * 构造函数
     *
     * @param llx          圆柱图表左下角x坐标
     * @param lly          圆柱图表左下角y坐标
     * @param partWidth    圆柱图表宽度
     * @param partHeight   圆柱图表高度
     * @param basePartData 圆柱图表数据
     */
    public PillarsPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData) {
        super(llx, lly, partWidth, partHeight, basePartData);
    }

    /**
     * 构造函数
     *
     * @param llx          圆柱图表左下角x坐标
     * @param lly          圆柱图表左下角y坐标
     * @param partWidth    圆柱图表宽度
     * @param partHeight   圆柱图表高度
     * @param basePartData 圆柱图表数据
     * @param parentPart   圆柱图表依附夫人父模块
     * @param subParts     圆柱图表附属的子模块数据
     */
    public PillarsPart(float llx, float lly, float partWidth, float partHeight, BasePartData basePartData,
                       BasePart parentPart, List<BasePart> subParts) {
        super(llx, lly, partWidth, partHeight, basePartData, parentPart, subParts);
    }

    @Override
    public void draw(PdfContentByte canvas) {
        try {
            if (this.getParentPart() != null) {
                this.llx += this.getParentPart().llx;
                this.lly += this.getParentPart().lly;
            }

            PillarsPartData pillarsPartData = (PillarsPartData) basePartData;

            drawPillars(canvas,
                    pillarsPartData.pillarItemDatas,
                    getGroupCount(pillarsPartData.pillarItemDatas),
                    getPillarCount(pillarsPartData.pillarItemDatas),
                    pillarsPartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为柱形图添加圆柱元素
     *
     * @param canvas               画笔
     * @param pillarItemDatas      柱形图圆柱数据
     * @param pillarGroupCount     圆柱组数
     * @param pillarsCountForGroup 每一组的圆柱数目
     * @param pillarsPartData      柱形图数据对象
     */
    protected void drawPillars(PdfContentByte canvas, List<PillarItemPart> pillarItemDatas, int pillarGroupCount,
                             int pillarsCountForGroup, PillarsPartData pillarsPartData) {
        float pillarWidth = partWidth / (pillarGroupCount * pillarsCountForGroup + (pillarGroupCount - 1) * 2 + 2);
        float currentXOffset = llx + pillarWidth;

        canvas.saveState();

        boolean isFirstOne = true;
        int index = 0;
        float currentYOffset = 0;

        for (PillarItemPart pillarItemPart : pillarItemDatas) {
            if (pillarItemPart.partType == PillarItemPart.PART_TYPE_CURRENT_PILLAR) {

                // 如果这个part就是当前pillar的一部分，说明当前pillar可能会继续
            } else if (pillarItemPart.partType == PillarItemPart.PART_TYPE_NEW_PILLAR) {

                // 如果这个part是一个新的pillar，那么重启一个新的pillar画
                currentXOffset += pillarWidth;
                currentYOffset = 0;
            } else if (pillarItemPart.partType == PillarItemPart.PART_TYPE_NEW_GROUP) {

                // 如果是一个新的分组，那么重启一个新的分组画
                index = 0;
                currentYOffset = 0;

                if (isFirstOne) {
                    isFirstOne = false;
                } else {
                    currentXOffset += (pillarWidth * 3);

                    // 每次画一个新的分组时，就将x轴的偏移量加上两个圆柱的宽度
                }
            }

            canvas.setColorFill(pillarsPartData.pillarsPartColors.get(index));
            canvas.setLineWidth(0);
            canvas.setColorStroke(GrayColor.LIGHT_GRAY);
            canvas.rectangle(currentXOffset,
                    lly + currentYOffset,
                    pillarWidth,
                    partHeight * getProperPillarHeightRatio(pillarItemPart.partValue, pillarsPartData));
            canvas.closePathFillStroke();
            currentYOffset += partHeight * getProperPillarHeightRatio(pillarItemPart.partValue, pillarsPartData);
            index++;
        }

        canvas.restoreState();
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            List<PillarItemPart> pillarItemPartList = new ArrayList<PillarItemPart>();

            for (int index = 0; index < 21; index++) {
                if (index % 3 == 0) {
                    pillarItemPartList.add(new PillarItemPart(PillarItemPart.PART_TYPE_NEW_GROUP, 10));
                } else if (index % 3 == 1) {
                    pillarItemPartList.add(new PillarItemPart(PillarItemPart.PART_TYPE_CURRENT_PILLAR, 10));
                } else if (index % 3 == 2) {
                    pillarItemPartList.add(new PillarItemPart(PillarItemPart.PART_TYPE_NEW_PILLAR, 10));
                }
            }

            BasePart pillarsPart = new PillarsPart(new PillarsPartData(pillarItemPartList, Arrays.asList(new BaseColor[]{
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[0]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[1]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[2]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[3]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[4]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[5]),
                    ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[6])
            })));

            pillarsPart.setLlx(0);
            pillarsPart.setLly(100);
            pillarsPart.setPartWidth(PageSize.A4.getWidth());
            pillarsPart.setPartHeight(200);
            pillarsPart.draw(pdfWriter.getDirectContent());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取圆柱组数
     *
     * @param pillarItemDatas 圆柱数据
     *
     * @return 圆柱数据包含的圆柱组数
     */
    protected int getGroupCount(List<PillarItemPart> pillarItemDatas) {
        int groupCount = 0;

        for (PillarItemPart pillarItemPart : pillarItemDatas) {
            if (pillarItemPart.partType == PillarItemPart.PART_TYPE_NEW_GROUP) {
                groupCount++;
            }
        }

        return groupCount;
    }

    /**
     * 获取所有组中最多的圆柱数目
     *
     * @param pillarItemDatas 圆柱数据
     *
     * @return 所有组中最多的圆柱数目
     */
    protected int getPillarCount(List<PillarItemPart> pillarItemDatas) {
        int maxPillarCount = 0;
        int currentPillarsCount = 0;

        for (PillarItemPart pillarItemPart : pillarItemDatas) {
            if (pillarItemPart.partType == PillarItemPart.PART_TYPE_NEW_GROUP) {
                currentPillarsCount = 1;

                if (currentPillarsCount > maxPillarCount) {
                    maxPillarCount = currentPillarsCount;
                }
            }

            if (pillarItemPart.partType == PillarItemPart.PART_TYPE_NEW_PILLAR) {
                currentPillarsCount++;

                if (currentPillarsCount > maxPillarCount) {
                    maxPillarCount = currentPillarsCount;
                }
            }
        }

        return maxPillarCount;
    }

    /**
     * 得到所有pillar最高的高度
     *
     * @param pillarItemDatas 圆柱数据
     *
     * @return 所有pillar最高的高度
     */
    public static int getProperMaxPillarHeight(List<PillarItemPart> pillarItemDatas) {
        float maxPillarHeight = -Float.MAX_VALUE;
        float currentPillarHeight = 0;

        for (PillarItemPart pillarItemData : pillarItemDatas) {
            if (pillarItemData.partType == PillarItemPart.PART_TYPE_CURRENT_PILLAR) {
                currentPillarHeight += pillarItemData.partValue;
            } else if ((pillarItemData.partType == PillarItemPart.PART_TYPE_NEW_PILLAR)
                    || (pillarItemData.partType == PillarItemPart.PART_TYPE_NEW_GROUP)) {
                if (maxPillarHeight < currentPillarHeight) {
                    maxPillarHeight = currentPillarHeight;
                }

                currentPillarHeight = pillarItemData.partValue;
            }
        }

        if (currentPillarHeight > maxPillarHeight) {
            maxPillarHeight = currentPillarHeight;
        }

        int normalizeScale = PdfConvertUtil.getNormalizeScale(maxPillarHeight);

        return (int) ((Math.round(maxPillarHeight * 1.0f / normalizeScale + 0.5)) * normalizeScale);
    }

    /**
     * 根据最高的圆柱高度得到合适的图表展示圆柱最高高度
     *
     * @param groupsPartsData 圆柱数据
     * @param pillarsPartsKey 圆柱数据parts key
     * @param max             默认提供的最大展示高度
     * @return 如果未提供默认大展示高度（max == -Float.MAX_VALUE）返回计算得到的大展示高度，否则返回默认大展示高度
     */
    public static int getProperMaxPillarHeight(List<Map<String, Float>> groupsPartsData,
                                               List<List<String>> pillarsPartsKey, float max) {
        try {
            if (max != -Float.MAX_VALUE) {
                return (int) max;
            }

            float maxPillarHeight = -Float.MAX_VALUE;

            for (Map<String, Float> groupPartsData : groupsPartsData) {
                for (List<String> pillarPartsKey : pillarsPartsKey) {
                    float currentPillarHeight = 0;

                    for (String pillarPartKey : pillarPartsKey) {
                        currentPillarHeight += groupPartsData.get(pillarPartKey);
                    }

                    if (maxPillarHeight < currentPillarHeight) {
                        maxPillarHeight = currentPillarHeight;
                    }
                }
            }

            int normalizeScale = PdfConvertUtil.getNormalizeScale(maxPillarHeight);

            return (int) ((Math.round(maxPillarHeight * 1.0f / normalizeScale + 0.5)) * normalizeScale);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 根据最矮的圆柱高度得到合适的图表展示圆柱最低高度
     *
     * @param pillarItemsDatas 圆柱数据
     *
     * @return 合适的图表展示圆柱最低高度
     */
    public static int getProperMinPillarHeight(List<PillarItemPart> pillarItemsDatas) {
        return 0;
    }

    /**
     *  根据最矮的圆柱高度得到合适的图表展示圆柱最低高度
     *
     * @param groupsPartsData 圆柱数据
     * @param pillarsPartsKey 遍历数据使用的key
     * @param min  默认提供的最小展示高度
     *
     * @return 如果未提供默认最小展示高度 （min == Float.MAX_VALUE）返回计算得到的最小展示高度 ，否则返回默认最小展示高度
     */
    public static int getProperMinPillarHeight(List<Map<String, Float>> groupsPartsData,
                                               List<List<String>> pillarsPartsKey, float min) {
        if (min != Float.MAX_VALUE) {
            return (int) min;
        }

        return 0;
    }

    /**
     * 将柱状图小条目的数值换算成柱状图指定范围中的恰当比例
     *
     * @param pillarData
     * @param pillarsPartData PARAM_DESC
     * @return
     */
    protected float getProperPillarHeightRatio(float pillarData, PillarsPartData pillarsPartData) {
        float minPillarHeight = getProperMinPillarHeight(pillarsPartData.pillarItemDatas);
        float maxPillarHeight = getProperMaxPillarHeight(pillarsPartData.pillarItemDatas);

        if (pillarData < minPillarHeight) {
            pillarData = minPillarHeight;
        }

        if (pillarData > maxPillarHeight) {
            pillarData = maxPillarHeight;
        }

        return (pillarData - minPillarHeight) / (maxPillarHeight - minPillarHeight);
    }

    /**
     * 每一个圆柱条目Part的描述类
     *
     * @author wenchao
     * @version 1.0, 16/10/28
     */
    public static class PillarItemPart {

        /**
         * 表示当前圆柱子部分从属于当前圆柱
         */
        public static final int PART_TYPE_CURRENT_PILLAR = 0;

        /**
         * 表示当前圆柱子部分属于一个新的圆柱
         */
        public static final int PART_TYPE_NEW_PILLAR = 1;

        /**
         * 表示当前圆柱子部分属于一个新的分组
         */
        public static final int PART_TYPE_NEW_GROUP = 2;

        /**
         * 圆柱Part的类型
         */
        public int partType;

        /**
         * 圆柱Part的数值
         */
        public float partValue;

        /**
         * 构造函数
         *
         * @param partType  圆柱Part类型
         * @param partValue 圆柱part数据
         */
        public PillarItemPart(int partType, float partValue) {
            this.partType = partType;
            this.partValue = partValue;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
