package com.icekredit.pdf.utils;

/**
 * Url工具类，主要用于将原始链接转换为特定格式的链接和从特定格式的链接中取出原始链接信息
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class UrlUtil {

    /** 远程链接头部 */
    public static final String REMOTE_LINK_PREFIX = "online:";

    /** localGoto链接头部 */
    public static final String LOCAL_LINK_GOTO_PREFIX = "local_goto:";

    /** localDest链接头部 */
    public static final String LOCAL_LINK_DEST_PREFIX = "local_dest:";

    /**
     * 从特定格式的自定义链接中取出实际链接
     *
     * @param primitiveLink    格式化的链接，拥有指定头部，如LOCAL_LINK_GOTO_PREFIX
     *
     * @return    提取的实际链接
     */
    public static String extractRealLink(String primitiveLink) {
        if (isRemoteLink(primitiveLink)) {
            return primitiveLink.substring(REMOTE_LINK_PREFIX.length());
        }

        if (isLocalGotoLink(primitiveLink)) {
            return primitiveLink.substring(LOCAL_LINK_GOTO_PREFIX.length());
        }

        if (isLocalDestLink(primitiveLink)) {
            return primitiveLink.substring(LOCAL_LINK_DEST_PREFIX.length());
        }

        return "";
    }

    /**
     * 将一个LocalGoto链接组装成系统内部可识别的LocalDest链接，加上头部LOCAL_LINK_DEST_PREFIX
     *
     *
     * @param realLink 原始LocalDest链接，一般是一个一二三级标题
     *
     * @return    组装后的链接字符串
     */
    public static String generateLocalDestLink(String realLink) {
        return LOCAL_LINK_DEST_PREFIX + realLink;
    }

    /**
     * 将一个LocalGoto链接组装成系统内部可识别的LocalGoto链接，加上头部 LOCAL_LINK_GOTO_PREFIX
     *
     *
     * @param realLink 原始LocalGoto链接
     *
     * @return    组装后的链接字符串
     */
    public static String generateLocalGotoLink(String realLink) {
        return LOCAL_LINK_GOTO_PREFIX + realLink;
    }

    /**
     * 将一个网络链接组装成系统内部可识别的网络链接，加上头部 REMOTE_LINK_PRFFIX
     *
     *
     * @param realLink 原始网络链接
     *
     * @return    组装后的链接字符串
     */
    public static String generateRemoteLink(String realLink) {
        return REMOTE_LINK_PREFIX + realLink;
    }

    /**
     * 判断一个链接是否是一个默认的链接 #
     *
     *
     * @param link 链接地址字符串
     *
     * @return    link字符串equals（“#”） 返回true，否则返回false
     */
    public static boolean isDefaultLink(String link) {
        return link.equals("#");
    }

    /**
     * 判断是否是一个有效的LocalDestination链接
     *
     *
     * @param link 链接地址字符串
     *
     * @return    链接地址字符串不为空且以LOCAL_LINK_DEST_PREFIX开头 返回true 否则返回false
     */
    public static boolean isLocalDestLink(String link) {
        if ((link == null) || link.trim().equals("")) {
            return false;
        }

        if (!link.startsWith(LOCAL_LINK_DEST_PREFIX)) {
            return false;
        }

        if (link.substring(LOCAL_LINK_DEST_PREFIX.length()).trim().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否是一个有效的LocalGoto链接
     * @param link    链接地址字符串
     * @return   链接地址字符串不为空且以LOCAL_LINK_GOTO_PREFIX开头 返回true 否则返回false
     */
    public static boolean isLocalGotoLink(String link) {
        if ((link == null) || link.trim().equals("")) {
            return false;
        }

        if (!link.startsWith(LOCAL_LINK_GOTO_PREFIX)) {
            return false;
        }

        if (link.substring(LOCAL_LINK_GOTO_PREFIX.length()).trim().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否是一个有效的网络链接
     * @param link    链接地址字符串
     * @return    是一个格式正确的网络链接 如http://www.somewhere.com 返回true，否则false
     */
    public static boolean isRemoteLink(String link) {
        if ((link == null) || link.trim().equals("")) {
            return false;
        }

        if (!link.startsWith(REMOTE_LINK_PREFIX)) {
            return false;
        }

        if (link.substring(REMOTE_LINK_PREFIX.length()).trim().equals("")) {
            return false;
        }

        return RegularExpressionUtil.isValidRemoteLink(link);
    }

    /**
     * 判断链接是否为正确的链接，目前实现需要改进                                                                                                                                                                                                                                                                                                                             5
     * @param link   link :网络链接:online:http://www.somewhere.com local:someHeader
     * @return    是默认链接或者远程网络链接或者本地itext pdf local goto链接或者本地itext pdf local dest链接返回true，否则返回false
     */
    public static boolean isValidLink(String link) {
        return isDefaultLink(link) || isLocalGotoLink(link) || isLocalDestLink(link) || isRemoteLink(link);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
