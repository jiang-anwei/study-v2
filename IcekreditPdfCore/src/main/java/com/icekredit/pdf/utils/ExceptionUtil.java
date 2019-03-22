package com.icekredit.pdf.utils;

/**
 * 异常堆栈打印工具类
 */
public class ExceptionUtil {
    /**
     * 格式化指定异常信息调用堆栈
     *
     * @param e 指定异常对象
     * @return 调用对堆栈字符串信息
     */
    public static String msg(Exception e) {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] elements = e.getStackTrace();
        builder.append(e.getClass());
        builder.append(":");
        builder.append(e.getMessage());
        builder.append("\n");

        for (StackTraceElement element : elements) {
            String className = element.getClassName();
            if (className != null && className.startsWith("com.icekredit")) {
                builder.append("\t");
                builder.append(className);
                builder.append("#");
                builder.append(element.getMethodName());
                builder.append("(");
                builder.append(element.getFileName());
                builder.append(":");
                builder.append(element.getLineNumber());
                builder.append(")\n");
            }
        }

        return builder.toString();
    }
}
