package com.dc.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT_1 = "yyyy/MM/dd";

    /**
     * 获取当前日期时间字符串
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }

    /**
     * 获取当前日期字符串
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * 获取当前时间字符串
     */
    public static String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    }

    /**
     * 格式化日期时间
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期时间字符串
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 计算两个日期之间的天数差
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个日期时间之间的小时差
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 获取指定日期的开始时间
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    /**
     * 获取指定日期的结束时间
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 判断是否为今天
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }

    /**
     * 判断是否为昨天
     */
    public static boolean isYesterday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now().minusDays(1));
    }

    /**
     * 获取几天前的日期
     */
    public static LocalDate getDaysAgo(int days) {
        return LocalDate.now().minusDays(days);
    }

    /**
     * 获取几天后的日期
     */
    public static LocalDate getDaysLater(int days) {
        return LocalDate.now().plusDays(days);
    }

    public static String dateFormat(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
