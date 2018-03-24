package com.anxin.kitchen.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 by hpf
 */
public class DateUtils {

    // 返回当前年月日
    public static String getNowDate() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
        return nowDate;
    }

    // 返回当前年月日
    public static String getDate() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("yyyyMMdd").format(date);
        return nowDate;
    }

    // 返回当前年份
    public static int getYear() {
        Date date = new Date();
        String year = new SimpleDateFormat("yyyy").format(date);
        return Integer.parseInt(year);
    }

    // 返回当前月份
    public static int getMonth() {
        Date date = new Date();
        String month = new SimpleDateFormat("MM").format(date);
        return Integer.parseInt(month);
    }

    // 返回当前日期
    public static int getDay() {
        Date date = new Date();
        String month = new SimpleDateFormat("dd").format(date);
        return Integer.parseInt(month);
    }

    // 返回当前小时
    public static String getHour() {
        Date date = new Date();
        String month = new SimpleDateFormat("HH").format(date);
        return month;
    }

    // 返回当前分钟
    public static String getMinute() {
        Date date = new Date();
        String month = new SimpleDateFormat("mm").format(date);
        return month;
    }

    // 返回星期
    public static int getWeek(int year, int month, int day) {
        int week = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String time = year + "-" + month + "-" + day;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK);
    }

    // 判断闰年
    public static boolean isLeap(int year) {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))
            return true;
        else
            return false;
    }

    // 返回当月天数
    public static int getDays(int year, int month) {
        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

    // 返回当月星期天数
    public static int getSundays(int year, int month) {
        int sundays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Calendar setDate = Calendar.getInstance();
        // 从第一天开始
        int day;
        for (day = 1; day <= getDays(year, month); day++) {
            setDate.set(Calendar.DATE, day);
            String str = sdf.format(setDate.getTime());
            if (str.equals("星期日")) {
                sundays++;
            }
        }
        return sundays;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s, String type) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        long lt = new Long(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        String res = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = simpleDateFormat.parse(s);
            long ts = date.getTime() / 1000;
            res = String.valueOf(ts);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        DateUtils du = new DateUtils();
        System.out.println("今天日期是：" + du.getNowDate());
        System.out.println("本月有" + du.getDays(du.getYear(), du.getMonth()) + "天");
        System.out.println("本月有" + du.getSundays(du.getYear(), du.getMonth()) + "个星期天");
    }
}