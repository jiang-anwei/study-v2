package com.icekredit.pdf.utils;

/**
 * Debug工具类
 *
 *
 * @version        1.0, 16/10/27
 * @author         wenchao
 */
public class Debug {

    /** 标签参数所在的参数列表索引 */
    private static final int TAG_INDEX = 0;

    /** 分隔符参数所在的参数列表索引 */
    private static final int SEPARATOR_INDEX = 1;

    /** 默认展示的标签 */
    private static final String DEFAULT_TAG = "Debug";

    /** 默认展示的分隔符 */
    private static final String DEFAULT_SEPARATOR = "---->";

    /** DEBUG_FLAG 用于控制debug信息的展示 */
    public static final boolean DEBUG_FLAG = false;

    /**
     * 打印debug信息的函数
     *
     *
     * @param msg 具体的debug信息
     * @param extraArgs 额外参数，如果出现，第一个是展示的标签名称，第二个是展示的分隔符
     */
    public static void debug(String msg, String... extraArgs) {
        if (!DEBUG_FLAG) {
            return;
        }

        if (extraArgs.length == 0) {
            System.out.println(DEFAULT_TAG + ":" + DEFAULT_SEPARATOR + msg);
        } else if (extraArgs.length == 1) {
            System.out.println(extraArgs[TAG_INDEX] + ":" + DEFAULT_SEPARATOR + msg);
        } else if (extraArgs.length == 2) {
            System.out.println(extraArgs[TAG_INDEX] + ":" + extraArgs[SEPARATOR_INDEX] + msg);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
