package com.anxin.kitchen.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取时间
 *
 * @author zwq
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    private static TimeUtil tu = null;
    //每次调用的次数
    private static int frequency = 0;

    public synchronized static TimeUtil getInstance() {
        frequency = 0;
        if (tu == null) {
            tu = new TimeUtil();
        }
        return tu;
    }

    /**
     * 获取MMddHHmmssSSS
     *
     * @return
     */
    public String getNowTime() {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            Thread.sleep(1);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("MMddHHmmssSSS");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return getNowTime();
        }
    }

    /**
     * 获取MMddHHmmss
     *
     * @return
     */
    public String getNowTimeN() {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            Thread.sleep(1);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("MMddHHmmss");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return getNowTimeN();
        }
    }

    /**
     * 获取yyyyMMddHHmmssSSS
     *
     * @return
     */
    public String getNowTimeYear() {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            Thread.sleep(1);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyyMMddHHmmssSSS");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return getNowTimeYear();
        }
    }

    /**
     * 获取yyyyMMddHHmmss
     *
     * @return
     */
    public String getNowTimeYear_second() {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            Thread.sleep(1);
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyyMMddHHmmss");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return getNowTimeYear();
        }
    }

    /**
     * 获取yyyy-MM-dd HH:mm:ss.SSS
     *
     * @return
     */
    public String getNowTimeGis() {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return getNowTimeGis();
        }

    }

    /**
     * 获取yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public String getNowTimeSS() {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return getNowTimeSS();
        }

    }

    /**
     * 获取当前时间 年-月-日 时：分：秒
     *
     * @return
     */
    public String getNowTimeSS(long times) {
        //frequency++;
        //if (frequency >= 3) {
        //    return "";
        //}
        //try {
         //   SimpleDateFormat sdf = new SimpleDateFormat();
        //    sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        //    return sdf.format(times);
       // } catch (Exception e) {
        //    return getNowTimeSS(times);
       // }

        return DateUtils.stampToDate(times+"","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间 年-月-日
     * yyyy-MM-dd
     *
     * @return
     */
    public String getNowTime(long times) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd");
            return sdf.format(times);
        } catch (Exception e) {
            return getNowTime(times);
        }

    }

    /**
     * 获取 时：分：秒
     * HH:mm:ss
     *
     * @return
     */
    public String getTimeH(long times) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("HH:mm:ss");
            return sdf.format(times);
        } catch (Exception e) {
            return getTimeH(times);
        }
    }

    /**
     * 获取 HH:mm:ss
     *
     * @param time
     * @return
     */
    public String Long2Time(long time) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("HH:mm:ss");
            return sdf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return Long2Time(time);
        }
    }

    /**
     * 获取   yyyy年MM月dd日 E HH时mm分ss秒
     *
     * @param time
     * @return
     */
    public String Long2AllInfo_CN(long time) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy年MM月dd日 E HH时mm分ss秒");
            return sdf.format(time);
        } catch (Exception e) {
            return Long2AllInfo_CN(time);
        }
    }

    /**
     * yyyy年M月d日
     *
     * @param time
     * @return
     */
    public String Long2String(long time) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy年M月d日");
            return sdf.format(time);
        } catch (Exception e) {
            return Long2String(time);
        }
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    private Date stringToDate(String strTime, String formatType)
            throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式 yyyy-MM-dd HH:mm:ss
    // strTime的时间格式和formatType的时间格式必须相同
    public long stringToLong(String strTime) {
        frequency++;
        if (frequency >= 3) {
            return 0;
        }
        try {
            Date date = stringToDate(strTime, "yyyy-MM-dd HH:mm:ss"); // String类型转成date类型
            if (date == null) {
                return 0;
            } else {
                long currentTime = date.getTime(); // date类型转成long类型
                return currentTime;
            }
        } catch (Exception e) {
            return stringToLong(strTime);
        }
    }

    /**
     * @param strTime
     * @param strType yyyy-MM-dd HH:mm:ss  / yyyy-MM-dd
     * @return
     */
    public long stringToLong(String strTime, String strType) {
        frequency++;
        if (frequency >= 3) {
            return 0;
        }
        try {
            Date date = stringToDate(strTime, strType); // String类型转成date类型
            if (date != null) {
                return date.getTime(); // date类型转成long类型
            }
        } catch (Exception e) {

        }
        return stringToLong(strTime, strType);
    }

    private Calendar mCalendar;

    /**
     * 取当前时间加减day天时间
     *
     * @param day
     * @return
     */
    public String getDataTime(int day) {
        frequency++;
        if (frequency >= 3) {
            return getNowTime(System.currentTimeMillis());
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd");
            mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.DATE, day);
            return sdf.format(mCalendar.getTime());
        } catch (Exception e) {
            return getDataTime(day);
        }
    }

    /**
     * 取当前时间加减day天时间
     *
     * @param day
     * @return
     */
    public long getDataTimeToLong(int day) {
        frequency++;
        if (frequency >= 3) {
            return 0;
        }
        try {
            mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.DATE, day);
            return mCalendar.getTimeInMillis();
        } catch (Exception e) {
            return getDataTimeToLong(day);
        }
    }

    /**
     * 取当前时间加减day天时间
     *
     * @param day
     * @return
     */
    public String getDataTime(int day, String timeType) {
        frequency++;
        if (frequency >= 3) {
            return getNowTime(System.currentTimeMillis());
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(timeType);
            mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.DATE, day);
            return sdf.format(mCalendar.getTime());
        } catch (Exception e) {
            return getDataTime(day, timeType);
        }
    }

    /**
     * 获取时间格式是否错误，若错误取上一次时间+1秒
     *
     * @param nowTimes
     * @param lastTime
     * @return
     */
    public String isYesTims(String nowTimes, String lastTime) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        String strTimes;
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
            strTimes = sdf.format(getTimeLast(nowTimes));
            if (strTimes.endsWith(".000")) {
                strTimes = strTimes.substring(0, strTimes.length() - 4);
            }
            return strTimes;
        } catch (Exception e) {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
            Date timeLast = getTimeLast(lastTime);
            if (timeLast == null) {
                strTimes = sdf.format(System.currentTimeMillis());
                if (strTimes.endsWith(".000")) {
                    strTimes = strTimes.substring(0, strTimes.length() - 4);
                }
                return strTimes;
            } else {
                mCalendar = Calendar.getInstance();
                mCalendar.add(Calendar.SECOND, 1);
                strTimes = sdf.format(timeLast);
                if (strTimes.endsWith(".000")) {
                    strTimes = strTimes.substring(0, strTimes.length() - 4);
                }
                return strTimes;
            }
        }
    }

    private Date getTimeLast(String lastTime) {
        frequency++;
        if (frequency >= 3) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.parse(lastTime);
        } catch (Exception e) {
            return getTimeLast(lastTime);
        }
    }

    /**
     * 取startTime时间加1秒
     *
     * @param startTime
     * @return
     */
    public String getTraverseTime(String startTime) {
        frequency++;
        if (frequency >= 3) {
            return "00:00:00";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            if (mCalendar == null) {
                mCalendar = Calendar.getInstance();
            }
            sdf.applyPattern("HH:mm:ss");
            mCalendar.setTime(sdf.parse(startTime));
            mCalendar.add(Calendar.SECOND, 1);
            return sdf.format(mCalendar.getTime());
        } catch (Exception e) {
            return getTraverseTime(startTime);
        }
    }

    /**
     * 从00开始加N秒后
     *
     * @param second
     * @return
     */
    public String getTraverseTimeSum(int second) {
        frequency++;
        if (frequency >= 3) {
            return "00:00:00";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            if (mCalendar == null) {
                mCalendar = Calendar.getInstance();
            }
            sdf.applyPattern("HH:mm:ss");
            mCalendar.setTime(sdf.parse("00:00:00"));
            mCalendar.add(Calendar.SECOND, second);
            return sdf.format(mCalendar.getTime());
        } catch (Exception e) {
            return getTraverseTimeSum(second);
        }
    }

    /**
     * 获取当前时间 年
     * yyyy-MM-dd
     *
     * @return
     */
    public String getNowYear(long times) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy");
            return sdf.format(times);
        } catch (Exception e) {
            return getNowTime(times);
        }

    }

    /**
     * 获取当前时间 月
     * yyyy-MM-dd
     *
     * @return
     */
    public String getNowMonth(long times) {
        frequency++;
        if (frequency >= 3) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM");
            return sdf.format(times);
        } catch (Exception e) {
            return getNowTime(times);
        }

    }

    /**
     *
     * @return
     */
    public String getNowMonthFirstDay() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormater.format(cal.getTime()) + "";
    }

    public String getNowMonthLastDay() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime()) + "";
    }

}
