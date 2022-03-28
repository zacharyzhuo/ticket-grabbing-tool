package com.zachary.ticketgrabbingtool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final ThreadLocal<SimpleDateFormat> defaultSDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> dateAndTimeSDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> integrateSDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> hourSDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> emailSDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    public static String defaultDateToString(Date date) {
        return defaultSDF.get().format(date);
    }

    public static String dateAndTimeDateToString(Date date) {
        return dateAndTimeSDF.get().format(date);
    }

    public static String integrateDateToString(Date date) {
        return integrateSDF.get().format(date);
    }

    public static String hourDateToString(Date date) {
        return hourSDF.get().format(date);
    }

    public static String emailDateToString(Date date) {
        return emailSDF.get().format(date);
    }

    public static Date defaultStringToDate(String dateStr) throws ParseException {
        return defaultSDF.get().parse(dateStr);
    }

    public static Date dateAndTimeStringToDate(String dateStr) throws ParseException {
        return dateAndTimeSDF.get().parse(dateStr);
    }

    public static Date integrateStringToDate(String dateStr) throws ParseException {
        return integrateSDF.get().parse(dateStr);
    }

    public static Date hourStringToDate(String dateStr) throws ParseException {
        return hourSDF.get().parse(dateStr);
    }

    public static Date emailStringToDate(String dateStr) throws ParseException {
        return emailSDF.get().parse(dateStr);
    }

    public static boolean isNight() {
        Date now = new Date();

        int hour = Integer.parseInt(hourSDF.get().format(now));

        return hour < 8 || hour >= 22;
    }

}
