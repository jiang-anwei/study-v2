package com.icekredit.pdf.entities.chart;

import java.io.File;
import java.io.FileOutputStream;

import java.util.*;

import com.icekredit.pdf.entities.BaseCell;
import com.icekredit.pdf.entities.EmptyCell;
import com.icekredit.pdf.entities.chart.part.BasePart;
import com.icekredit.pdf.entities.chart.part.ItemDescPart.PartDesc;
import com.icekredit.pdf.entities.chart.part.PillarsPart;
import com.icekredit.pdf.entities.chart.partdata.*;
import com.icekredit.pdf.utils.ColorUtil;
import com.icekredit.pdf.utils.Debug;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

/**
 * 超级图表模块单元格，利用一个图表模块容器来控制所有子模块的展示
 *
 * @author wenchao
 * @version 1.0, 16/10/28
 */
public class SuperChartCell extends BaseCell implements PdfPCellEvent {

    /**
     * 子模块集合所对应的子模块数据集合
     */
    protected List<BasePartData> basePartDatas;

    /**
     * 无参构造函数
     */
    public SuperChartCell() {
        this(new ArrayList<BasePartData>());
    }

    /**
     * 构造函数
     *
     * @param basePartDatas 子模块集合所对应的子模块数据集合
     */
    public SuperChartCell(List<BasePartData> basePartDatas) {
        this.basePartDatas = basePartDatas;
        this.setFixedHeight(160);
        this.setCellEvent(this);
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            SuperChart parentPart = new SuperChart();
            float llx = position.getLeft();
            float lly = position.getBottom();
            float width = position.getWidth();
            float height = position.getHeight();

            parentPart.setLlx(llx);
            parentPart.setLly(lly);
            parentPart.setPartWidth(width);
            parentPart.setPartHeight(height);
            System.out.println(position.getTop());
            Rectangle availableSpace = new Rectangle(0, 0, 100, 100);

            for (BasePartData basePartData : basePartDatas) {

                // 在配置控件字模块之前，先检测是否有足够的空间
                if (!basePartData.checkSpace(availableSpace)) {
                    Debug.debug("无法为当前类型的控件分配足够的空间：" + basePartData.getClass().getName());
//                     continue;
                }

                // 配置一个控件子模块到当前面板中
                BasePart basePart = (BasePart) basePartData.getChartPartClass().newInstance();

                basePart.setLlx(basePartData.getPositionRect().getLeft() * width / 100.0f);
                basePart.setLly(basePartData.getPositionRect().getBottom() * height / 100.0f);
                basePart.setPartWidth(width * basePartData.getPositionRect().getWidth() / 100.0f);
                basePart.setPartHeight(height * basePartData.getPositionRect().getHeight() / 100.0f);
                basePart.setBasePartData(basePartData);
                parentPart.appendPart(basePart);

                // 配置完控件子模块之后，记录消费的空间，并从总的面板中减去
                basePartData.consume(availableSpace);
            }

            Debug.debug(parentPart.getSubParts().size() + "");
            parentPart.draw(canvases[PdfPTable.BASECANVAS]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String destFileStr = "result/test.pdf";
            File destFile = new File(destFileStr);

            destFile.getParentFile().mkdirs();

            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(destFile));

            document.open();

            PdfPTable mainFrame = new PdfPTable(12);

            mainFrame.setWidthPercentage(100);

            SuperChartCell mixedChartCell = new SuperChartCell();

            mixedChartCell.setFixedHeight(160);

            List<PillarsPart.PillarItemPart> pillarItemPartList = new ArrayList<PillarsPart.PillarItemPart>();

            for (int index = 0; index < 28; index++) {
                if (index % 4 == 0) {
                    pillarItemPartList.add(
                            new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_GROUP,
                                    10));
                } else if (index % 4 == 1) {
                    pillarItemPartList.add(
                            new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_CURRENT_PILLAR,
                                    10));
                } else if (index % 4 == 2) {
                    pillarItemPartList.add(
                            new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_PILLAR,
                                    10));
                } else if (index % 4 == 3) {
                    pillarItemPartList.add(
                            new PillarsPart.PillarItemPart(PillarsPart.PillarItemPart.PART_TYPE_NEW_PILLAR,
                                    10));
                }
            }

            PillarsPartData pillarsPartData = new PillarsPartData(pillarItemPartList,
                    Arrays.asList(new BaseColor[]{
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[0]),
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[1]),
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[2]),
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[3]),
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[4]),
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[5]),
                            ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[6])
                    }));
            BackgroundPartData backgroundPartData = new BackgroundPartData(10);
            XAxisPartData xAxisPartData = new XAxisPartData(0,
                    100,
                    0,
                    Arrays.asList("星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一"));
            YAxisPartData yAxisPartData = new YAxisPartData(0,
                    100,
                    0,
                    Arrays.asList("星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一",
                            "星期一"),
                    "新奇洗洗");
            YAxisPartData yAxisPartData1 = new YAxisPartData(0,
                    100,
                    100,
                    Arrays.asList("0",
                            "10",
                            "20",
                            "30",
                            "40",
                            "50",
                            "60",
                            "40",
                            "50",
                            "60",
                            "60"),
                    "百分比");
            List<String> dataKeys = new ArrayList<String>();

            dataKeys.add("key1");
            dataKeys.add("key2");
            dataKeys.add("key3");
            dataKeys.add("key4");
            dataKeys.add("key5");
            dataKeys.add("key6");
            dataKeys.add("key7");

            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();

            map.put(dataKeys.get(0), 30f);
            map.put(dataKeys.get(1), 23f);
            map.put(dataKeys.get(2), 45f);
            map.put(dataKeys.get(3), 45f);
            map.put(dataKeys.get(4), 45f);
            map.put(dataKeys.get(5), 45f);
            map.put(dataKeys.get(6), 45f);
            datas.add(map);

            List<BaseColor> linesColor = new ArrayList<BaseColor>();

            linesColor.add(ColorUtil.strRGBAToColor("0xf39800ff"));

            LinesPartData linesPartData = new LinesPartData(datas, dataKeys, linesColor);
            PieChartPartData pieChartPartData = new PieChartPartData(40,
                    30,
                    new float[]{90f, 90f, 90f, 90f},
                    new BaseColor[]{
                            ColorUtil.strRGBAToColor(
                                    ColorUtil.DEFAUTL_COLOR_ARRAY[0]),
                            ColorUtil.strRGBAToColor(
                                    ColorUtil.DEFAUTL_COLOR_ARRAY[1]),
                            ColorUtil.strRGBAToColor(
                                    ColorUtil.DEFAUTL_COLOR_ARRAY[2]),
                            ColorUtil.strRGBAToColor(
                                    ColorUtil.DEFAUTL_COLOR_ARRAY[3])},
                    new String[]{"ss", "ss", "ss", "ssssss"},
                    true,
                    false);
            ArrayList<PartDesc> partDescs = new ArrayList<PartDesc>();
            PartDesc partDesc = null;

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_PILLAR,
                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                        "张三");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_LINE,
                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                        "张三不知里斯");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_SECTOR,
                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                        "张三里斯里斯");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_PILLAR,
                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                        "张三里斯");
                partDescs.add(partDesc);
            }

            for (int index = 0; index < 3; index++) {
                partDesc = new PartDesc(PartDesc.PART_TYPE_LINE,
                        ColorUtil.strRGBAToColor(ColorUtil.DEFAUTL_COLOR_ARRAY[index]),
                        "张三里斯");
                partDescs.add(partDesc);
            }

            ItemDescPartData itemDescPartData = new ItemDescPartData(partDescs);

            mixedChartCell.getBasePartDatas().add(backgroundPartData);
            mixedChartCell.getBasePartDatas().add(xAxisPartData);
            mixedChartCell.getBasePartDatas().add(yAxisPartData);
            mixedChartCell.getBasePartDatas().add(yAxisPartData1);
            mixedChartCell.getBasePartDatas().add(pillarsPartData);
            mixedChartCell.getBasePartDatas().add(pieChartPartData);
            mixedChartCell.getBasePartDatas().add(itemDescPartData);
            mixedChartCell.getBasePartDatas().add(linesPartData);

            mixedChartCell.setColspan(8);
            mainFrame.addCell(new EmptyCell(2));
            mainFrame.addCell(mixedChartCell);
            mainFrame.addCell(new EmptyCell(2));
            document.add(mainFrame);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回子模块集合所对应的子模块数据集合
     *
     * @return 子模块集合所对应的子模块数据集合
     */
    public List<BasePartData> getBasePartDatas() {
        return basePartDatas;
    }

    /**
     * 设置子模块集合所对应的子模块数据集合
     *
     * @param basePartDatas 子模块集合所对应的子模块数据集合
     */
    public void setBasePartDatas(List<BasePartData> basePartDatas) {
        this.basePartDatas = basePartDatas;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
