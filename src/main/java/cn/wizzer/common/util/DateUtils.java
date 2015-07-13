package cn.wizzer.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.nutz.ioc.loader.annotation.IocBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Wizzer.cn on 2015/7/13.
 */
@IocBean
public class DateUtils {
    private static final Locale DEFAULT_LOCALE = Locale.CHINA;

    /**
     * 获取当前日期(yyyy-MM-dd)
     *
     * @return
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 获取当前日期(指定格式)
     *
     * @param pattern
     * @return
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern, DEFAULT_LOCALE);
    }

    /**
     * 获取当前时间(HH:mm:ss)
     *
     * @return
     */
    public static String getTime() {
        return DateFormatUtils.format(new Date(), "HH:mm:ss", DEFAULT_LOCALE);
    }

    /**
     * 获取当前时间(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static String getDateTime() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE);
    }

    /**
     * 转换日期格式(yyyy-MM-dd HH:mm:ss)
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE);
    }

    /**
     * 转换日期格式(yyyy-MM-dd)
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return DateFormatUtils.format(date, "yyyy-MM-dd", DEFAULT_LOCALE);
    }

    /**
     * 转换日期格式(HH:mm:ss)
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        if (date == null) return "";
        return DateFormatUtils.format(date, "HH:mm:ss", DEFAULT_LOCALE);
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return DateFormatUtils.format(new Date(), "yyyy", DEFAULT_LOCALE);
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return DateFormatUtils.format(new Date(), "MM", DEFAULT_LOCALE);
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return DateFormatUtils.format(new Date(), "dd", DEFAULT_LOCALE);
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return DateFormatUtils.format(new Date(), "E", DEFAULT_LOCALE);
    }


}
