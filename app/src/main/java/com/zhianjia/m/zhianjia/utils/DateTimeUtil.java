package com.zhianjia.m.zhianjia.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class DateTimeUtil {
    /**
     * 时间戳转换日期
     *
     * @param timestamp 时间戳
     * @return 返回Date类型时间
     */
    public static String timeStampToDate(Long timestamp) {
        // SimpleDateFormat format = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        String d = format.format(new Date(timestamp * 1000));
        // Date date = null;
        // try {
        // date = format.parse(d);
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        return d;
    }

    /**
     * 时间戳转长时间格式
     *
     * @param timestamp 时间戳
     * @return 返回长时间格式
     */
    public static String timeStampToDateLong(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String d = format.format(new Date(timestamp * 1000));
        // Date date = null;
        // try {
        // date = format.parse(d);
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        return d;
    }

    /**
     * @param timestamp    以秒为单位的时间戳
     * @param formatString 格式化字符串
     * @return 返回相应的时间格式
     */
    public static String timeStampToDateByFormat(Long timestamp, String formatString) {
        // SimpleDateFormat format = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String d = format.format(new Date(timestamp * 1000));
        return d;
    }

    /**
     * date格式时间格式化时间字符串
     *
     * @param date         时间
     * @param formatString 格式化字符串
     * @return 格式化后的时间格式
     */
    public static String dateToStringByFormat(Date date, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(date);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException 抛出解析错误
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
