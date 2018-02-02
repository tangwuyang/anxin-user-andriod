package com.anxin.kitchen.utils;

public class Log {
    private final String TAG;
    public static int LOG_LEVEL = 1;// 设置不同的值控制log的显示,0不显示log，5 显示全部log
    private static final int ERROR = 1;
    private static final int WARN = 2;
    private static final int INFO = 3;
    private static final int DEBUG = 4;
    private static final int VERBOSE = 5;

    public static Log getLog(Class<?> c) {
        return new Log(shortClassName(c));
    }

    public static Log getLog() {
        StackTraceElement[] s = new RuntimeException().getStackTrace();
        return getLog(StringUtils.substringAfterLastDot(s[1].getClassName()));
    }

    public static Log getLog(String logTag) {
        return new Log(logTag);
    }

    private Log(String logTag) {
        this.TAG = "SMANOS" + '/' + logTag;
    }

    public void v(String msg) {
        if (LOG_LEVEL >= VERBOSE)
            android.util.Log.v(TAG, msg);
    }

    public void v(String msg, Throwable tr) {
        if (LOG_LEVEL >= VERBOSE) {
            android.util.Log.v(TAG, msg, tr);
            
        }
    }

    public void d(String msg) {
        if (LOG_LEVEL >= DEBUG)
            android.util.Log.d(TAG, msg);
    }

    public void d(String msg, Throwable tr) {
        if (LOG_LEVEL >= DEBUG)
            android.util.Log.d(TAG, msg, tr);
    }

    public void i(String msg) {
        if (LOG_LEVEL >= INFO)
            android.util.Log.i(TAG, msg);
    }

    public void i(String msg, Throwable tr) {
        if (LOG_LEVEL >= INFO)
            android.util.Log.i(TAG, msg, tr);
    }

    public void w(String msg) {
        if (LOG_LEVEL >= WARN)
            android.util.Log.w(TAG, msg);
    }

    public void w(String msg, Throwable tr) {
        if (LOG_LEVEL >= WARN)
            android.util.Log.w(TAG, msg, tr);
    }

    public void e(String msg) {
        if (LOG_LEVEL >= ERROR)
            android.util.Log.e(TAG, msg);
    }
    public void e(String msg, Throwable tr) {
        if (LOG_LEVEL >= ERROR)
            android.util.Log.e(TAG, msg, tr);
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL >= ERROR)
            android.util.Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL >= WARN)
            android.util.Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL >= INFO)
            android.util.Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL >= DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL >= VERBOSE)
            android.util.Log.v(tag, msg);
    }

    private static String shortClassName(Class<?> c) {
        String className = c.getName();
        return StringUtils.substringAfterLastDot(className);
    }
}
