package com.icekredit.pdf.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class RegularExpressionUtil {

    /** 检测是否是一个有效网络链接的正则表达式 */
    public static final Pattern URL_PATTERN = Pattern.compile("^[http:|https:]*//[\\w\\./]*$",
                                                              Pattern.CASE_INSENSITIVE);

    /**
     * 判断指定字符串是否是一个有效的商标图片链接
     *
     *
     * @param str 指定字符串
     *
     * @return 满足商标图片格式返回true否则返回false
     */
    public static boolean isTrademarkUrl(String str) {
        return str.indexOf("/api/companyAPI/trademark") != -1;
    }

    /**
     * 检测一个链接是否为格式正确的网络链接
     *
     * @param link   指定链接
     *
     * @return 满足网络链接格式，返回true，否则返回false
     */
    public static boolean isValidRemoteLink(String link) {
        return URL_PATTERN.matcher(link).matches();
    }

    public static void main(String[] args) {
        Debug.debug(RegularExpressionUtil.isValidRemoteLink("http://blog.csdn.net/duck_arrow/article/details/10209039")
                + "");
        Debug.debug(
                RegularExpressionUtil.isValidRemoteLink("https://www.blog.csdn.net/duck_arrow/article/details/10209039")
                        + "");
        Debug.debug(
                RegularExpressionUtil.isTrademarkUrl(
                        "http://localhost:8080/api/companyAPI/trademark?fileName=data/assets/trademark/dc533157a169e690ed602d7c03e60117.jpg&token_id=01F5E6400B743158A464A369339B18D6") + "");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
