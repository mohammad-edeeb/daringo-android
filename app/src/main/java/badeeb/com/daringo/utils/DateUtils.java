package badeeb.com.daringo.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import badeeb.com.daringo.R;

/**
 * Created by meldeeb on 12/9/17.
 */

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static final SimpleDateFormat API_FORMAT;
    public static final SimpleDateFormat SYSTEM_FORMAT;

    static {
        API_FORMAT = new SimpleDateFormat(DATE_FORMAT);
        API_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        SYSTEM_FORMAT = new SimpleDateFormat(DATE_FORMAT);
    }

    public static Date toUTC(String dateString) {
        try {
            return API_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date toSystem(String dateString) {
        try {
            return SYSTEM_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateAndMonthAndYear(Calendar c) {
        return c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
                + " " + c.get(Calendar.DAY_OF_MONTH)
                + " " + c.get(Calendar.YEAR);
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Calendar addDays(Calendar calendar, int days) {
        Calendar result = Calendar.getInstance();
        result.setTime(calendar.getTime());
        result.add(Calendar.DATE, days);
        return result;
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Calendar addHours(Calendar calendar, int hours) {
        Calendar result = Calendar.getInstance();
        result.setTime(calendar.getTime());
        result.add(Calendar.HOUR_OF_DAY, hours);
        return result;
    }

    public static String formatWithSystemTimeZone(Calendar calendar){
        return SYSTEM_FORMAT.format(calendar.getTime());
    }

    public static String formatWithAPITimeZone(Calendar calendar){
        return API_FORMAT.format(calendar.getTime());
    }

    public static String getSystemTimeZoneString(){
        return Calendar.getInstance().getTimeZone().getID();
    }

    public static boolean datePassed(Date date){
        return date.before(new Date());
    }

    public static int getHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static String customFormat(Date date, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(date);
    }

    public static void setToDayStart(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setToHourStart(Calendar calendar){
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Returns absolute(Date - now) in milliseconds
     * Always positive value
     */
    public static long nowDiffInMs(Date date){
        Calendar now = Calendar.getInstance();
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        long diff = dateTime.getTimeInMillis() - now.getTimeInMillis();
        return diff;
    }


    public static String getTimeLeft(Date date) {
        long timeInMs = nowDiffInMs(date);
        return getTimeLeft(timeInMs);
    }

    public static String getTimeLeft(long timeInMs){
        int d = (int) TimeUnit.MILLISECONDS.toDays(timeInMs);
        int h = (int) TimeUnit.MILLISECONDS.toHours(timeInMs) % 24;
        int m = (int) TimeUnit.MILLISECONDS.toMinutes(timeInMs) % (60);
        int s = (int) TimeUnit.MILLISECONDS.toSeconds(timeInMs) % (60);
        if (d > 0) {
            return d + "|" + Calendar.DATE;
        }
        if (h > 0) {
            return h + "|" + Calendar.HOUR;
        }
        if (m > 0) {
            return m + "|" + Calendar.MINUTE;
        }
        if (s > 0) {
            return s + "|" + Calendar.SECOND;
        }
        return null;
    }

    public static Calendar toCal(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
