package com.anxin.kitchen.utils;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * 类型转换
 *
 * @author zwq
 */
public class UtilHandler {

    private static UtilHandler uh = null;

    public synchronized static UtilHandler getInstance() {
        if (uh == null) {
            uh = new UtilHandler();
        }
        return uh;
    }

    /**
     * 得到单个字的高度
     *
     * @param paint 画笔
     * @return 高度
     */
    public float getPaintFontHeight(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }


    public double toDouble(String str, double d) {
        try {
            return Double.parseDouble(str.trim());
        } catch (Exception e) {
            return d;
        }
    }

    public float toFloat(String str, float f) {
        try {
            return Float.parseFloat(str.trim());
        } catch (Exception e) {
            return f;
        }
    }

    public int toInt(String str, int toI) {
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            return toI;
        }
    }

    public long toLong(String str, long l) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            // TODO: handle exception
            return l;
        }
    }

    private DecimalFormat df, dfLonLat;

    /**
     * 转换数字，保留一位小数
     *
     * @param object
     * @return
     */
    public double toDf(Object object) {
        if (df == null) {
            df = new DecimalFormat("#.0");
            df.getRoundingMode();
        }
        try {
            return Double.parseDouble(df.format(object));
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 转换数字，保留N位小数
     *
     * @param object
     * @param str    填0 ，一个0代表一位小数  2个0代表两位小数
     * @return
     */
    public double toDfSum(Object object, String str) {
        DecimalFormat dfSumD = new DecimalFormat("#." + str);
        dfSumD.getRoundingMode();
        try {
            return Double.parseDouble(dfSumD.format(object));
        } catch (Exception e) {
        }
        return 0;
    }

    public String toDfStr(Object object, String str) {
        DecimalFormat dfSumD = new DecimalFormat("#." + str);
        dfSumD.getRoundingMode();
        try {
            String format = dfSumD.format(object);
            return (format.startsWith(".") ? "0" : "") + format;
        } catch (Exception e) {
        }
        return "0";
    }

    /**
     * 转换数字，保留6位小数
     *
     * @return
     */
    public double toDfLl(double latOrLon) {
        if (dfLonLat == null) {
            dfLonLat = new DecimalFormat("#.000000");
            dfLonLat.getRoundingMode();
        }
        try {
            String format = dfLonLat.format(latOrLon);
            while (format.endsWith("0")) {
                format = format.substring(0, format.length() - 1);
            }
            return Double.parseDouble(format);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    public String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    /**
     * 获取字节的MD5值
     *
     * @return
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
