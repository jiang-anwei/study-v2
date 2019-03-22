package com.icekredit.pdf.utils;

import com.icekredit.pdf.entities.core.MessageConfig;

import com.itextpdf.text.BaseColor;

/**
 * pdf 颜色相关工具类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class ColorUtil {

    /** 默认header描述颜色 */
    public static final String DEFAULT_DESC_COLOR = "0x036eb8ff";

    /** 警告header描述颜色 */
    public static final String WARNING_DESC_COLOR = "0xca4223ff";

    /** 柱状图圆柱颜色 */
    public static final String[] DEFAULT_PILLAR_COLOR_ARRAY = new String[] { "00aaffff" };

    /** 玫瑰图扇区颜色数组 */
    public static final String[] ROSE_CHART_COLOR_ARRAY = new String[] {
        "0x86dbefff", "0xe0b0f2ff", "0xd9e29fff", "0xfff0aaff", "0xffbaa6ff", "0xdcd8bdff", "0xdee8deff", "0x8de0aaff",
        "0xffc872ff", "0x9cbdfcff", "0xbaff8bff", "0xd6b88bff"
    };

    /** 默认颜色数组 */
    public static final String[] DEFAUTL_COLOR_ARRAY = new String[] {
        "ff7f50ff", "87cefaff", "da70d6ff", "32cd32ff", "6495edff", "ff69b4ff", "ba55d3ff", "cd5c5cff", "ffa500ff",
        "40e0d0ff", "1e90ffff", "ff6347ff", "7b68eeff", "00fa9aff", "ffd700ff", "6b8e23ff", "ff00ffff", "3cb371ff",
        "b8860bff", "30e0e0ff", "00aaffff", "0064ffff", "0000ffff", "0000c8ff", "000082ff", "000046ff", "003250ff",
        "005050ff", "007850ff", "50a050ff", "50c850ff", "50ff50ff", "beff50ff", "ffff00ff", "ffc800ff", "ff8c00ff",
        "ff5000ff", "ff0000ff", "a00000ff", "c8008cff", "c800ffff", "8c00ffff", "5500b4ff", "55008cff", "64005aff",
        "8c005aff", "b4005aff", "dc005aff", "dc8ca0ff", "dcb4a0ff", "dcd282ff", "b4b482ff", "dc8c64ff", "6e6e50ff",
        "553c00ff", "555000ff", "5f6400ff", "697800ff", "738c00ff", "738c82ff", "738cc3ff", "82b9d7ff", "96d2faff",
        "96ebfaff", "50ebfaff", "00c8e6ff", "00a0c8ff", "0082a0ff", "00508cff", "00001eff", "C1232BFF", "B5C334FF",
        "FCCE10FF", "E87C25FF", "27727BFF", "FE8463FF", "9BCA63FF", "FAD860FF", "F3A43BFF", "60C0DDFF", "D7504BFF",
        "C6E579FF", "F4E001FF", "F0805AFF", "26C0C0FF", "C4C4C4FF", "C2C2C2FF", "C1FFC1FF", "C1CDCDFF", "C1CDC1FF",
        "C1C1C1FF", "C0FF3EFF", "BFEFFFFF", "BFBFBFFF", "BF3EFFFF", "BEBEBEFF", "BDBDBDFF", "BDB76BFF", "BCEE68FF",
        "BCD2EEFF", "BC8F8FFF", "BBFFFFFF", "BABABAFF", "BA55D3FF", "B9D3EEFF", "B8B8B8FF", "B8860BFF", "B7B7B7FF",
        "B5B5B5FF", "B4EEB4FF", "B4CDCDFF", "B452CDFF", "B3EE3AFF", "B3B3B3FF", "B2DFEEFF", "B23AEEFF", "B22222FF",
        "B0E2FFFF", "B0E0E6FF", "B0C4DEFF", "B0B0B0FF", "B03060FF", "AEEEEEFF", "ADFF2FFF", "ADD8E6FF", "ADADADFF",
        "ABABABFF", "AB82FFFF", "AAAAAAFF", "A9A9A9FF", "A8A8A8FF", "A6A6A6FF", "A52A2AFF", "A4D3EEFF", "A3A3A3FF",
        "A2CD5AFF", "A2B5CDFF", "A1A1A1FF", "A0522DFF", "A020F0FF", "9FB6CDFF", "9F79EEFF", "9E9E9EFF", "9C9C9CFF",
        "9BCD9BFF", "9B30FFFF", "9AFF9AFF", "9ACD32FF", "9AC0CDFF", "9A32CDFF", "999999FF", "9932CCFF", "98FB98FF",
        "98F5FFFF", "97FFFFFF", "96CDCDFF", "969696FF", "949494FF", "9400D3FF", "9370DBFF", "C0FF3EFF"
    };

    /**
     * 将颜色对象转换为十六进制0x开头的字符串
     *
     *
     * @param baseColor 待转换的颜色对象
     *
     * @return 转换后的颜色字符串，如果转换失败返回0xffffffff
     */
    public static String colorToStrRGBA(BaseColor baseColor) {
        try {
            return "0x" + toHexString(baseColor.getRed()) + toHexString(baseColor.getGreen())
                   + toHexString(baseColor.getBlue()) + toHexString(baseColor.getAlpha());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0xffffffff";
    }

    /**
     * 将颜色字符串转换为对应的颜色对象
     *
     *
     * @param colorRGBAStr 颜色字符串
     *
     * @return 转换后的颜色对象，如果转换失败返回BaseColor.WHITE
     */
    public static BaseColor strRGBAToColor(String colorRGBAStr) {
        try {
            int offset = 0;

            if (colorRGBAStr.trim().startsWith("0x")) {
                offset = 2;
            }

            return new BaseColor(Integer.parseInt(colorRGBAStr.substring(offset + 0, offset + 2), 16),
                                 Integer.parseInt(colorRGBAStr.substring(offset + 2, offset + 4), 16),
                                 Integer.parseInt(colorRGBAStr.substring(offset + 4, offset + 6), 16),
                                 Integer.parseInt(colorRGBAStr.substring(offset + 6, offset + 8), 16));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BaseColor.WHITE;
    }

    /**
     * 将一个int颜色值（0-255）转换为对应的十六进制字符串
     *
     *
     * @param colorValue int颜色值（0-255）
     *
     * @return 对应的十六进制字符串,如果发生异常返回"ff"
     */
    public static String toHexString(int colorValue) {
        try {
            if(colorValue < 0){
                colorValue = 0;
            }

            if(colorValue > 255){
                colorValue = 255;
            }

            String hexStr = Integer.toHexString(colorValue);

            if (hexStr.length() == 1) {
                return "0" + hexStr;
            }

            return hexStr;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "ff";
    }

    /**
     * 根据标识符的类型获取标识符背景色
     *
     *
     * @param identifierType 标识符类型
     *
     * @return 对应的标识符背景色，如果不存在对应映射，返回BaseColor.WHITE;
     */
    public static BaseColor getIdentifierColorByIdentifierType(int identifierType) {
        switch (identifierType) {
        case MessageConfig.IDENTIFIER_TYPE_NONE :
            break;

        case MessageConfig.IDENTIFIER_TYPE_MARK_RIGHT :
            return strRGBAToColor("0x59ca70ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_WRONG :
            return strRGBAToColor("0xff5200ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_POSITIVE :
            return strRGBAToColor("0x1daffcff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_NEGETIVE :
            return strRGBAToColor("0xff9300ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_UNKNOWN :
            break;
        }

        return BaseColor.WHITE;
    }

    /**
     * 根据当前分数，以及分数段标准，获取分数单元格的背景色
     *
     *
     * @param score 分数值
     * @param scoresStandard 分数段标准
     *
     * @return 当前分数对应的单元格的背景色，如果不存在对应映射，返回BaseColor.WHITE;
     */
    public static BaseColor getScoreChartBgColor(int score, int[] scoresStandard) {
        BaseColor[] scoresChartBgColors = new BaseColor[] {
            ColorUtil.strRGBAToColor("0xf36367ff"), ColorUtil.strRGBAToColor("0xff8d78ff"),
            ColorUtil.strRGBAToColor("0x92bf54ff"), ColorUtil.strRGBAToColor("0x27d6bbff"),
            ColorUtil.strRGBAToColor("0x1daffcff"), ColorUtil.strRGBAToColor("0x0651ffff")
        };

        return getScoreChartBgColor(score, scoresStandard, scoresChartBgColors);
    }

    // 人脸识别无数据标识符颜色55cede 人脸识别无数据文字颜色00959f

    /**
     * 根据当前分数，以及分数段标准，以及分数段颜色标准，获取分数单元格的背景色
     *
     * @param score 当前分数
     * @param scoresStandard 分数段标准
     * @param scoresChartBgColors 分数段颜色标准
     *
     * @return 当前分数对应的单元格的背景色，如果不存在对应映射，返回BaseColor.WHITE;
     */
    public static BaseColor getScoreChartBgColor(int score, int[] scoresStandard, BaseColor[] scoresChartBgColors) {
        if (score < 300) {
            score = 300;
        }

        if (score > 850) {
            score = 850;
        }

        for (int index = 0; index < scoresStandard.length - 1; index++) {
            if (score == 300) {
                return scoresChartBgColors[0];
            }

            if ((score > scoresStandard[index]) && (score <= scoresStandard[index + 1])) {
                return scoresChartBgColors[index];
            }
        }

        return ColorUtil.strRGBAToColor("0xffffffff");
    }

    /**
     * 根据分数以及分数段标准，获取对应的分数评论
     *
     *
     * @param score 当前分数
     * @param scoresStandard 当前分数标准
     *
     * @return 当前分数对应的分数标准，如果发生异常返回JSONAttrGetter.DEFAULT_STR_VALUE
     */
    public static String getScoreChartComment(int score, int[] scoresStandard) {
        String[] scoresChartComments = new String[] {
            "高危", "差", "一般", "良好", "优秀", "极佳"
        };

        return getScoreChartComment(score, scoresStandard, scoresChartComments);
    }

    // 人脸识别无数据标识符颜色55cede 人脸识别无数据文字颜色00959f

    /**
     * 根据分数以及分数段标准以及分数评论标准，获取对应的分数评论
     * @param score 当前分数
     * @param scoresStandard 当前分数标准
     * @param scoresChartComments 分数评论标准
     * @return 当前分数对应的分数标准，如果发生异常返回JSONAttrGetter.DEFAULT_STR_VALUE
     */
    public static String getScoreChartComment(int score, int[] scoresStandard, String[] scoresChartComments) {
        if (score < 300) {
            score = 300;
        }

        if (score > 850) {
            score = 850;
        }

        for (int index = 0; index < scoresStandard.length - 1; index++) {
            if (score == 300) {
                return scoresChartComments[0];
            }

            if ((score > scoresStandard[index]) && (score <= scoresStandard[index + 1])) {
                return scoresChartComments[index];
            }
        }

        return JSONAttrGetter.DEFAULT_STR_VALUE;
    }

    /**
     * 根据标识符的类型获取与标识符相关联的描述信息的字体颜色
     *
     *
     * @param identifierType 标识符类型
     *
     * @return 与标识符相关联的描述信息的字体颜色，如果不存在指定映射，返回BaseColor.WHITE
     */
    public static BaseColor getTextColorByIdentifierType(int identifierType) {
        switch (identifierType) {
        case MessageConfig.IDENTIFIER_TYPE_NONE :
            break;

        case MessageConfig.IDENTIFIER_TYPE_MARK_RIGHT :
            return strRGBAToColor("0x006934ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_WRONG :
            return strRGBAToColor("0xca4223ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_POSITIVE :
            return strRGBAToColor("0x00bed9ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_NEGETIVE :
            return strRGBAToColor("0xfea832ff");

        case MessageConfig.IDENTIFIER_TYPE_MARK_UNKNOWN :
            break;
        }

        return BaseColor.WHITE;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
