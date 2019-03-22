package com.icekredit.pdf.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 时间日期转换格式化工具类
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
@SuppressWarnings("unchecked")
public class DateUtil {
    /**
     * 默认的日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认月份格式
     */
    public static final String DEFAULT_MONTH_FORMAT = "yyyy-MM";

    /**
     * 默认年份格式
     */
    public static final String DEFAULT_YEAR_FORMAT = "yyyy";

    /**
     * 默认的时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 默认的日期时间格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 如果字符串长度为8位，且都是字符串，那么我们认为这是一个yyyyMMdd格式的日期
     */
    private static final Pattern FULL_DATE_PATTERN = Pattern.compile("[0-9]{8}"); //20120101

    /**
     * 日期分隔符
     */
    private static final String DATE_NOTIFIER_PATTERN_STR = "[年月日\\-/.]+";

    /**
     * 时间分隔符
     */
    private static final String TIME_NOTIFIER_PATTERN_STR = "[时分秒点:]+";

    /**
     * 压缩的时间日期格式
     */
    private static final String DATA_TIME_FORMAT = "yyyyMMddHHmmss";

    /**
     * 压缩的日期格式
     */
    private static final String DATA_FORMAT = "yyyyMMdd";

    /**
     * 压缩的时间格式
     */
    private static final String TIME_FORMAT = "HHmmss";

    /**
     * ThreadLocal 对象,用于存储当前线程中使用的日期格式化对象
     */
    private static final ThreadLocal convertDataFormatThreadLocal = new ThreadLocal();

    private static Logger logger = LogManager.getLogger(DateUtil.class);

    /**
     * 使用指定格式化字符转换指定日期字符串
     *
     * @param dateTimeStr 指定日期字符串
     * @return 转换后得到的date
     */
    public static Date parse(String dateTimeStr) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }

        dateTimeStr = dateTimeStr.replaceAll("\\.\\d{3}", "");   //去除掉毫秒部分

        String dateStr = null;
        String timeStr = null;
        String[] dateTimeStrParts = dateTimeStr.split("[ \\n]");
        for (String dateTimeStrPart : dateTimeStrParts) {
            if (REGUtil.REG_PATTERN_FOR_DATE.matcher(dateTimeStrPart).matches()
                    || dateTimeStrPart.contains("年")
                    || dateTimeStrPart.contains("月")
                    || dateTimeStrPart.contains("日")
                    || dateTimeStrPart.contains("-")
                    || dateTimeStrPart.contains("/")
                    || dateTimeStrPart.contains(".")
//                    || DATE_IDENTIFIER_PATTERN.matcher(dateTimeStrParts[index]).matches()
                    || FULL_DATE_PATTERN.matcher(dateTimeStrPart).matches()
                    || dateTimeStrPart.length() == 4) {
                dateStr = dateTimeStrPart;
            } else {
                timeStr = dateTimeStrPart;
            }
        }

        StringBuilder dateBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(dateStr)) {
            String[] dateParts = dateStr.split(DATE_NOTIFIER_PATTERN_STR);
            for (String datePart : dateParts) {
                if (datePart.length() == 1) {
                    dateBuilder.append("0");
                }

                dateBuilder.append(datePart);
            }

            switch (dateBuilder.length()) {
                case 4:
                    dateBuilder.append("0101");
                    break;
                case 6:
                    dateBuilder.append("01");
                    break;
            }
        }


        StringBuilder timeBuilder = new StringBuilder();

        if (!StringUtils.isEmpty(timeStr)) {
            String[] timeParts = timeStr.split(TIME_NOTIFIER_PATTERN_STR);
            for (String timePart : timeParts) {
                if (timePart.length() == 1) {
                    timeBuilder.append("0");
                }

                timeBuilder.append(timePart);
            }

            switch (timeBuilder.length()) {
                case 2:
                    timeBuilder.append("0000");
                    break;
                case 4:
                    timeBuilder.append("00");
                    break;
            }
        }

        try {
            String formattedDateStr = dateBuilder.toString();
            String formattedTimeStr = timeBuilder.toString();

            if (StringUtils.isEmpty(formattedDateStr) && StringUtils.isEmpty(formattedTimeStr)) {
                return null;
            }

            if (StringUtils.isEmpty(formattedDateStr)) {
                return parse(TIME_FORMAT,formattedTimeStr);
            }

            if (StringUtils.isEmpty(formattedTimeStr)) {
                return parse(DATA_FORMAT,formattedDateStr);
            }

            return parse(DATA_TIME_FORMAT,formattedDateStr + formattedTimeStr);
        } catch (Exception e) {
            logger.error(ExceptionUtil.msg(e));
        }

        return null;
    }

    /**
     * 转换指定时间段为秒数
     *
     * @param timeStr 时间字符串  xx小时xx分xx秒
     * @return 指定时间字符串对应的秒数
     */
    public static double parseTimeInSeconds(String timeStr) {
        if (!REGUtil.TIME_STR_PATTERN.matcher(timeStr).matches()) {
            return 0;
        }

        String[] parts = timeStr.split("(-|小时|时|分钟|分|秒|:)");

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        for (int index = 0; index < parts.length; index++) {
            if (index == 0) {
                seconds = Integer.valueOf(parts[parts.length - index - 1]);
            }

            if (index == 1) {
                minutes = Integer.valueOf(parts[parts.length - index - 1]);
            }

            if (index == 2) {
                hours = Integer.valueOf(parts[parts.length - index - 1]);
            }
        }

        return hours * 3600 + minutes * 60 + seconds;
    }


    /**
     * 获取两个日期之间时间间隔天数
     *
     * @param firstDateStr  第一个日期字符串值
     * @param secondDateStr 第二个日期字符串值
     * @return 间隔天数
     */
    public static int getDaysBetween(String firstDateStr, String secondDateStr) {
        Date firstDate = parse(firstDateStr);
        Date secondDate = parse(secondDateStr);

        if (firstDate == null || secondDate == null) {
            return 0;
        }

        LocalDate firstLocalDate = LocalDate.fromDateFields(firstDate);
        LocalDate secondLocalDate = LocalDate.fromDateFields(secondDate);

        return Math.abs(Days.daysBetween(firstLocalDate, secondLocalDate).getDays());
    }

    /**
     * 获取线程的变量副本，如果不覆盖initialValue，第一次get返回null，故需要初始化一个SimpleDateFormat，并set到threadLocal中
     * @param pattern 格式化字符串
     * @return 格式化字符串对应的SimpleDateFormat格式化对象
     */
    private static SimpleDateFormat getDateFormat(String pattern) {
        Map<String,SimpleDateFormat> map = (Map<String, SimpleDateFormat>) convertDataFormatThreadLocal.get();

        if(map == null){
            map = new HashMap<>();

            map.put(pattern,new SimpleDateFormat(pattern));

            convertDataFormatThreadLocal.set(map);
        }

        if(!map.containsKey(pattern)){
            map.put(pattern,new SimpleDateFormat(pattern));
        }

        return map.get(pattern);
    }


    /**
     * 使用指定格式化字符转换指定日期字符串
     *
     * @param dateString 指定日期字符串
     * @param parseStr   指定格式化字符
     * @return 转换后得到的date
     */
    public static Date parse(String parseStr, String dateString) throws ParseException {
        if(StringUtils.isEmpty(parseStr) || StringUtils.isEmpty(dateString)){
            return null;
        }

        try {
            return getDateFormat(parseStr).parse(dateString);
        }catch (Exception e){
            logger.warn("转换指定日期字符串" + dateString + "失败:" + ExceptionUtil.msg(e));
        }

        return null;
    }

    /**
     * 按照指定字符串格式格式化指定日期
     * @param pattern 字符串格式
     * @param date 指定日期
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Date date) {
        if(StringUtils.isEmpty(pattern) || date == null){
            return null;
        }

        try {
            return getDateFormat(pattern).format(date);
        }catch (Exception e){
            logger.error(ExceptionUtil.msg(e));logger.warn("格式化指定日期对象" + date + "失败:" + ExceptionUtil.msg(e));
        }

        return null;
    }

    /**
     * 使用默认日期格式转换指定日期字符换
     *
     * @param dateString 指定日期字符串
     * @return 转换后得到的date
     */
    public static Date parseDate(String dateString) throws ParseException {
        return parse(DEFAULT_DATE_FORMAT, dateString);
    }

    /**
     * 使用默认日期格式格式化date
     *
     * @param date 指定date
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        return format(DEFAULT_DATE_FORMAT, date);
    }

    /**
     * 使用默认时间格式转换时间字符串
     *
     * @param dateString 时间字符串
     * @return 转换后得到的date
     */
    public static Date parseTime(String dateString) throws ParseException {
        return parse(DEFAULT_TIME_FORMAT, dateString);
    }

    /**
     * 使用默认时间格式格式化date
     *
     * @param date 指定date
     * @return 格式化后的字符串
     */
    public static String formatTime(Date date) {
        return format(DEFAULT_TIME_FORMAT, date);
    }

    /**
     * 使用默认格式转换指定日期时间字符串
     *
     * @param dateString 指定日期时间字符串
     * @return 转换后得到的date
     */
    public static Date parseDateTime(String dateString) throws ParseException {
        return parse(DEFAULT_DATETIME_FORMAT, dateString);
    }

    /**
     * 使用默认日期时间格式格式化date
     *
     * @param date 指定date
     * @return 格式化后的字符串
     */
    public static String formatDateTime(Date date) {
        return format(DEFAULT_DATETIME_FORMAT, date);
    }
}
