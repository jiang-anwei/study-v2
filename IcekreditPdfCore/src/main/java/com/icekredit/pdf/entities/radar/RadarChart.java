package com.icekredit.pdf.entities.radar;

import com.icekredit.pdf.utils.FontUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 用于生成雷达图的工具类,最终会通过pdfpcellevent中的docelllayout方法封装到pdfpcell中
 */
public class RadarChart {
    private float centerXPosition = 0;    //雷达图中心点x坐标
    private float centerYPosition = 0;    //雷达图中心点y坐标
    private int excircleRadius = 0;    //雷达图外切圆的坐标

    private String[] radarAttributesNames = null;    //雷达图各个属性的名称

    //定义用于控制属性名称布局以及对其的常量
    private static final int ALIGN_TOP_CENTER = 1;    //顶端靠下，水平居中对其
    private static final int ALIGN_LEFT_RIGHT_MIDDLE = 2;    //左端靠右，垂直居中
    private static final int ALIGN_BOTTOM_CENTER = 3;    //底部靠上，水平居中
    private static final int ALIGN_RIGHT_LEFT_MIDDLE = 4;    //右边靠左，处置居中

    private int[] radarAttributesValues = null;    //雷达图各个属性值
    private static final int MAX_ATTRIBUTE_VALUE = 50;

    private static final int RADAR_CHART_LEVEL = 5;
    private static final int[][] RADAR_CHART_LEVEL_FILL_COLORS = new int[][]{
            {0x68, 0xb0, 0xf8, 0xdd},
            {0x68, 0xb0, 0xf8, 0xbb},
            {0x68, 0xb0, 0xf8, 0x99},
            {0x68, 0xb0, 0xf8, 0x77},
            {0x68, 0xb0, 0xf8, 0x55}
    };
    private static final int[] RADAR_CHART_ATTRIBUTE_FILL_COLOR = new int[]{
            0x1a, 0x7a, 0xd9, 0x77
    };

    private static final float RADAR_CHART_ATTRIBUTE_CIRCLE_RADIUS = 1f;

    private int fontSize = 8;
    private PdfWriter pdfWriter = null;

    public RadarChart() {
    }

    public RadarChart(float centerXPosition, float centerYPosition, int excircleRadius,
                      String[] radarAttributesNames, int[] radarAttributesValues, PdfWriter pdfWriter)
            throws IOException,DocumentException {
        this.centerXPosition = centerXPosition;
        this.radarAttributesNames = radarAttributesNames;
        this.radarAttributesValues = radarAttributesValues;
        this.excircleRadius = excircleRadius;
        this.centerYPosition = centerYPosition;
        this.pdfWriter = pdfWriter;

        this.generateRadarModel();
        this.generateAttributeNamesModel();
        this.generateAttributeValuesModel();
    }

    /**
     * 生成雷达属性名称TextView
     */
    private void generateAttributeNamesModel() throws IOException,DocumentException {
        PdfContentByte canvas = this.pdfWriter.getDirectContent();

        int[] attributesNamesVertices = getAttributesNamesVertices(this.centerXPosition, this.centerYPosition,
                this.excircleRadius, this.radarAttributesNames);

        int index = 0;
        while (index < radarAttributesNames.length) {
            ColumnText columnText = new ColumnText(canvas);

            //根据属性名称布局的位置来打印text
            if (attributesNamesVertices[index * 3 + 2] == ALIGN_TOP_CENTER) {    //顶部向下靠，水平居中
                columnText.setSimpleColumn(new Phrase(radarAttributesNames[index], FontUtils.chineseFont),
                        attributesNamesVertices[index * 3 + 0] - radarAttributesNames[index].length() * fontSize / 2,
                        attributesNamesVertices[index * 3 + 1] - fontSize / 2,
                        attributesNamesVertices[index * 3 + 0] + radarAttributesNames[index].length() * fontSize / 2,
                        attributesNamesVertices[index * 3 + 1] + fontSize - fontSize / 2,
                        0, Element.ALIGN_JUSTIFIED);
            } else if (attributesNamesVertices[index * 3 + 2] == ALIGN_BOTTOM_CENTER) {    //底部向上靠，水平居中
                columnText.setSimpleColumn(new Phrase(radarAttributesNames[index],FontUtils.chineseFont),
                        attributesNamesVertices[index * 3 + 0] - radarAttributesNames[index].length() * fontSize / 2,
                        attributesNamesVertices[index * 3 + 1] - fontSize - fontSize,
                        attributesNamesVertices[index * 3 + 0] + radarAttributesNames[index].length() * fontSize / 2,
                        attributesNamesVertices[index * 3 + 1] - fontSize,
                        0, Element.ALIGN_JUSTIFIED);
            } else if (attributesNamesVertices[index * 3 + 2] == ALIGN_LEFT_RIGHT_MIDDLE) {   //左边向右靠，垂直居中
                columnText.setSimpleColumn(new Phrase(radarAttributesNames[index], FontUtils.chineseFont),
                        attributesNamesVertices[index * 3 + 0] - (radarAttributesNames[index].length() + 1) * fontSize,
                        attributesNamesVertices[index * 3 + 1] - fontSize / 2 - fontSize,
                        attributesNamesVertices[index * 3 + 0] - 1 * fontSize,
                        attributesNamesVertices[index * 3 + 1] + fontSize / 2 - fontSize,
                        0, Element.ALIGN_RIGHT);
            } else if (attributesNamesVertices[index * 3 + 2] == ALIGN_RIGHT_LEFT_MIDDLE) {    //右边向左靠，垂直居中
                columnText.setSimpleColumn(new Phrase(radarAttributesNames[index],FontUtils.chineseFont),
                        attributesNamesVertices[index * 3 + 0] + 1 * fontSize,
                        attributesNamesVertices[index * 3 + 1] - fontSize / 2 - fontSize,
                        attributesNamesVertices[index * 3 + 0] + (radarAttributesNames[index].length() + 1) * fontSize,
                        attributesNamesVertices[index * 3 + 1] + fontSize / 2 - fontSize,
                        0, Element.ALIGN_LEFT);
            }
            columnText.go();

            index++;
        }
    }

    /**
     * 生成雷达属性值曲线
     */
    public void generateAttributeValuesModel() {
        PdfContentByte canvas = this.pdfWriter.getDirectContent();

        canvas.saveState();

        float[] attributesValuesVertices = getAttributesValuesVertices(this.centerXPosition, this.centerYPosition,
                this.excircleRadius, this.radarAttributesValues);

        BaseColor backgroundColor = null;
        backgroundColor = new BaseColor(RADAR_CHART_ATTRIBUTE_FILL_COLOR[0],
                RADAR_CHART_ATTRIBUTE_FILL_COLOR[1],
                RADAR_CHART_ATTRIBUTE_FILL_COLOR[2],
                RADAR_CHART_ATTRIBUTE_FILL_COLOR[3]
        );

        canvas.setColorStroke(new BaseColor(0x10,0x5b,0xb3,0xff));
        canvas.setColorFill(backgroundColor);

        int index = 0;
        canvas.moveTo(attributesValuesVertices[index * 2 + 0],
                attributesValuesVertices[index * 2 + 1]);
        while (index < this.radarAttributesValues.length - 1) {
            canvas.lineTo(attributesValuesVertices[(index + 1) * 2 + 0],
                    attributesValuesVertices[(index + 1) * 2 + 1]);

            index++;
        }

        //封口，一构成一个完整的雷达信息曲线
        index = 0;
        canvas.lineTo(attributesValuesVertices[index * 2 + 0],
                attributesValuesVertices[index * 2 + 1]);

        index = 0;
        while (index < this.radarAttributesValues.length){
            canvas.circle(attributesValuesVertices[index * 2 + 0],
                    attributesValuesVertices[index * 2 + 1],
                    RADAR_CHART_ATTRIBUTE_CIRCLE_RADIUS);

            index ++;
        }

        canvas.closePathFillStroke();
        canvas.restoreState();
    }

    /**
     * 生成基本的雷达图信息
     *
     * @throws IOException
     * @throws DocumentException
     */
    public void generateRadarModel() throws IOException, DocumentException {
        PdfContentByte canvas = this.pdfWriter.getDirectContent();
        canvas.saveState();

        float[] radarVertices = getRadarVertices(this.centerXPosition, this.centerYPosition, this.excircleRadius,
                this.radarAttributesNames.length, this.radarAttributesValues.length);

        //画出雷达图的基本格式,从最外面向最里面画
        int level = 0;
        for (level = RADAR_CHART_LEVEL; level > 0; level--) {
            generateRadarChartModelHelper(pdfWriter, canvas, radarVertices, level);
            canvas.closePathFillStroke();
        }

        canvas.restoreState();
    }

    private void generateRadarChartModelHelper(PdfWriter pdfWriter, PdfContentByte canvas, float[] radarVertices, int level) {
        BaseColor backgroundColor = null;
        backgroundColor = new BaseColor(RADAR_CHART_LEVEL_FILL_COLORS[level - 1][0],
                RADAR_CHART_LEVEL_FILL_COLORS[level - 1][1],
                RADAR_CHART_LEVEL_FILL_COLORS[level - 1][2],
                RADAR_CHART_LEVEL_FILL_COLORS[level - 1][3]
        );

        //shading model
/*      //设置当前画布用于勾画指定区域是所用的渐变颜色（实际上使用的不是渐变色而是flat）
        PdfShading simpleAxial = null;
        PdfShadingPattern pdfShadingPattern = null;
        simpleAxial = PdfShading.simpleAxial(pdfWriter,
                centerXPosition - excircleRadius, centerYPosition - excircleRadius,
                centerXPosition + excircleRadius, centerYPosition + excircleRadius,
                backgroundColor, backgroundColor);

        pdfShadingPattern = new PdfShadingPattern(simpleAxial);
        canvas.setShadingFill(pdfShadingPattern);*/

        canvas.setColorFill(backgroundColor);
        canvas.setFlatness(1.0f);
        canvas.setGrayStroke(0.9f);

       /* //画出当前级别雷达信息曲线
        int index = 0;
        while (index < radarAttributesValues.length - 1) {
            canvas.moveTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 0],
                    radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 1]);

            canvas.lineTo(centerXPosition, centerYPosition);

            canvas.lineTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + (index + 1) * 2 + 0],
                    radarVertices[(level - 1) * radarAttributesValues.length * 2 + (index + 1) * 2 + 1]);

            index++;
        }
        canvas.moveTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 0],
                radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 1]);

        canvas.lineTo(centerXPosition, centerYPosition);

        index = 0;
        canvas.lineTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 0],
                radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 1]);*/


        //画出当前级别雷达信息曲线
        int index = 0;
        canvas.moveTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 0],
                radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 1]);
        while (index < radarAttributesValues.length - 1) {
            canvas.lineTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + (index + 1) * 2 + 0],
                    radarVertices[(level - 1) * radarAttributesValues.length * 2 + (index + 1) * 2 + 1]);

            index++;
        }

        //封口，一构成一个完整的雷达信息曲线
        index = 0;
        canvas.lineTo(radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 0],
                radarVertices[(level - 1) * radarAttributesValues.length * 2 + index * 2 + 1]);
    }

    /**
     * @param centerXPosition
     * @param centerYPosition
     * @param excircleRadius
     * @param radarAttributesNames
     * @return
     */
    private int[] getAttributesNamesVertices(float centerXPosition, float centerYPosition,
                                             int excircleRadius, String[] radarAttributesNames) {
        int[] radarAttributesNamesEndVertices = new int[radarAttributesNames.length * 3];

        float firstAngleOffset = 0;
        if (radarAttributesNames.length % 2 == 1) {   //如果属性数目为奇数，第一个顶点在y轴上，偏移角度为零
            firstAngleOffset = (float) (Math.PI / 2);
        } else {    //如果属性数目为偶数，第一个顶点偏移角度等于2 * Math.PI / attributeNamesLength / 2
            firstAngleOffset = (float) (2 * Math.PI / radarAttributesNames.length);
        }

        int index = 0;
        while (index < radarAttributesNames.length) {
            double cosValueOfCurrentAngle = Math.cos(firstAngleOffset +
                    index * 2 * Math.PI / radarAttributesNames.length);
            double sinValueOfCurrentAngle = Math.sin(firstAngleOffset +
                    index * 2 * Math.PI / radarAttributesNames.length);


            radarAttributesNamesEndVertices[index * 3 + 0] = (int) (centerXPosition +
                    (float) (excircleRadius * cosValueOfCurrentAngle));
            radarAttributesNamesEndVertices[index * 3 + 1] = (int) (centerYPosition +
                    (float) (excircleRadius * sinValueOfCurrentAngle));

            if (((int) (cosValueOfCurrentAngle * 1000)) == 0) {
                if (((int) (sinValueOfCurrentAngle * 1000)) > 0) {
                    radarAttributesNamesEndVertices[index * 3 + 2] = ALIGN_TOP_CENTER;
                } else if (((int) (sinValueOfCurrentAngle * 1000)) < 0f) {
                    radarAttributesNamesEndVertices[index * 3 + 2] = ALIGN_BOTTOM_CENTER;
                }
            } else if (((int) (cosValueOfCurrentAngle * 1000)) < 0) {
                radarAttributesNamesEndVertices[index * 3 + 2] = ALIGN_LEFT_RIGHT_MIDDLE;
            } else {
                radarAttributesNamesEndVertices[index * 3 + 2] = ALIGN_RIGHT_LEFT_MIDDLE;
            }

            index++;
        }

        return radarAttributesNamesEndVertices;
    }


    /**
     * 获取用于标识雷达图具体属性值的坐标
     *
     * @param centerXPosition
     * @param centerYPosition
     * @param excircleRadius
     * @param radarAttributesValues
     * @return
     * @throws RuntimeException
     */
    private float[] getAttributesValuesVertices(float centerXPosition, float centerYPosition, int excircleRadius,
                                                int[] radarAttributesValues) throws RuntimeException {
        //数据校验
        int index = 0;
        while (index < radarAttributesValues.length) {
            if (radarAttributesValues[index] < 0) {
                throw new RuntimeException("抱歉！用于构建雷达图的基础属性值不能小于零！");
            }

            index++;
        }

        float[] attributesValuesVertices = new float[radarAttributesValues.length * 2];


        //计算出顺时针第一个顶点的偏移角度
        float firstAngleOffset = 0;
        if (radarAttributesValues.length % 2 == 1) {   //如果属性数目为奇数，第一个顶点在y轴上，偏移角度为零
            firstAngleOffset = (float) (Math.PI / 2);
        } else {    //如果属性数目为偶数，第一个顶点偏移角度等于2 * Math.PI / attributeNamesLength / 2
            firstAngleOffset = (float) (2 * Math.PI / radarAttributesValues.length);
        }

        index = 0;
        while (index < radarAttributesValues.length) {
            attributesValuesVertices[index * 2 + 0] = centerXPosition + (float) (excircleRadius * Math.cos(
                    firstAngleOffset + index * 2 * Math.PI / radarAttributesValues.length) *
                    (radarAttributesValues[index] * 1.0 / MAX_ATTRIBUTE_VALUE));
            attributesValuesVertices[index * 2 + 1] = centerYPosition + (float) (excircleRadius * Math.sin(
                    firstAngleOffset + index * 2 * Math.PI / radarAttributesValues.length) *
                    (radarAttributesValues[index] * 1.0 / MAX_ATTRIBUTE_VALUE));

            index++;
        }

        return attributesValuesVertices;
    }

    /**
     * 获取用于构建了雷达图的顶点坐标
     *
     * @param centerXPosition
     * @param centerYPosition
     * @param excircleRadius
     * @param attributeNamesLength
     * @param attributeValuesLength
     * @return
     * @throws RuntimeException
     */
    private float[] getRadarVertices(float centerXPosition, float centerYPosition,
                                     int excircleRadius, int attributeNamesLength,
                                     int attributeValuesLength) throws RuntimeException {
        if (attributeNamesLength < 3 || attributeValuesLength < 3) {
            throw new RuntimeException("抱歉，构建雷达图需要提供至少三个属性值！");
        }

        if (attributeNamesLength != attributeValuesLength) {
            throw new RuntimeException("抱歉，用于构建雷达图的属性键值对数目不匹配！");
        }

        float[] vertices = new float[RADAR_CHART_LEVEL * attributeNamesLength * 2];

        //计算出顺时针第一个顶点的偏移角度
        float firstAngleOffset = 0;
        if (attributeNamesLength % 2 == 1) {   //如果属性数目为奇数，第一个顶点在y轴上，偏移角度为零
            firstAngleOffset = (float) (Math.PI / 2);
        } else {    //如果属性数目为偶数，第一个顶点偏移角度等于2 * Math.PI / attributeNamesLength / 2
            firstAngleOffset = (float) (2 * Math.PI / attributeNamesLength);
        }


        int level = 0;
        while (level < RADAR_CHART_LEVEL) {
            int index = 0;
            while (index < attributeNamesLength) {
                //循环attributeNamesLength * RADAR_CHART_LEVEL次计算出画雷达图需要的顶点
                //相邻两个数值表示一个顶点的坐标
                vertices[level * attributeNamesLength * 2 + index * 2 + 0] = centerXPosition + (float) (excircleRadius * Math.cos(
                        firstAngleOffset + index * 2 * Math.PI / attributeNamesLength)
                        * (level + 1) / RADAR_CHART_LEVEL);
                vertices[level * attributeNamesLength * 2 + index * 2 + 1] = centerYPosition + (float) (excircleRadius * Math.sin(
                        firstAngleOffset + index * 2 * Math.PI / attributeNamesLength)
                        * (level + 1) / RADAR_CHART_LEVEL);

                index++;
            }

            level++;
        }

        return vertices;
    }

    public static void main(String[] args) throws IOException, DocumentException {
        String destPdfFilePath = "results/objects/radarChart.pdf";
        File file = new File(destPdfFilePath);
        file.getParentFile().mkdirs();

        Document document = null;
        try {
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPdfFilePath));

            document.open();
            RadarChart radarChart = new RadarChart(150, 250, 100,
                    new String[]{"注册信息", "经营历史", "股东高管", "企业声誉", "无形资产","Another Attribute"},
                    new int[]{10, 20, 30, 40, 50,40}, writer);

            RadarChart radarChart1 = new RadarChart(400, 400, 100,
                    new String[]{"注册信息", "经营历史", "股东高管", "企业声誉", "无形资产"},
                    new int[]{10, 20, 30, 40, 50}, writer);

            RadarChart radarChart2 = new RadarChart(200, 600, 100,
                    new String[]{"注册信息", "经营历史", "股东高管", "企业声誉", "无形资产","注册信息", "经营历史", "股东高管", "企业声誉", "无形资产"},
                    new int[]{10, 20, 30, 40, 50,10, 20, 30, 40, 50}, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
