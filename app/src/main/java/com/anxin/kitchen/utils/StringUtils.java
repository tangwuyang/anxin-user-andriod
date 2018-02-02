package com.anxin.kitchen.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by joshua on 15-5-1.
 */
public class StringUtils {
    private static final String POSITIVE_INTEGER_REGEX = "[0-9]+";
    private static final String INTEGER_REGEX = "-?" + POSITIVE_INTEGER_REGEX;

    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile(POSITIVE_INTEGER_REGEX);
    private static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);
    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

    public static String getSubstringAfter(String s, char c) {
        return s.substring(s.lastIndexOf(c) + 1).trim();
    }

    public static Set<String> stringToSet(String string) {
        Set<String> res = new HashSet<String>();
        if (string != null && !string.equals("")) {
            res.addAll(Arrays.asList(string.split(" ")));
        }
        return res;
    }

    public static String setToString(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String setBigIntegerToString(String set) {//十进制 ---> 二进制
        BigInteger src = new BigInteger(set);
        return src.toString(2);
    }

    public static String setStringToBigInteger(String set) {//二进制 ---> 十进制
        BigInteger src = new BigInteger(set, 2);
        return src.toString();
    }

    public static String ipIntToString(int ip) {
        // @formatter:off
        return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
        // @formatter:on
    }

    public static String[] toStringArray(int[] intArray) {
        String[] res = new String[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            res[i] = Integer.toString(intArray[i]);
        }
        return res;
    }

    public static String shorten(String string, int maxSize) {
        String res;
        if (string.length() < maxSize) {
            res = string;
        } else {
            res = string.substring(0, maxSize) + "...";
        }
        return res;
    }

    public static final boolean isPositiveInteger(String s) {
        return POSITIVE_INTEGER_PATTERN.matcher(s).matches();
    }

    public static final boolean isInteger(String s) {
        return INTEGER_PATTERN.matcher(s).matches();
    }

    public static final String humandReadableByteCount(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "iB";
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), pre);
    }

    public static int countMatches(String haystack, char c) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == c)
                count++;
        }
        return count;
    }

    /**
     * - * Returns the substring after the last dot ('.'). "bar" = - *
     * substringAfterLastDot("my.foo.bar"); - * - * @param string - * @return
     * The substring after the last dot. -
     */

    public static String substringAfterLastDot(String string) {
        return getSubstringAfter(string, '.');
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String byteToHex(byte b) {
        return String.format("x%02X", b);
    }

    public static String byteToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length * 3);
        for (byte b : byteArray) {
            sb.append(byteToHex(b));
        }
        return sb.toString();
    }

    /**
     * List转换String
     *
     * @param list :需要转换的List
     * @return String转换后的字符串
     */
    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return "L" + sb.toString();
    }

    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1 + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return "M" + sb.toString();
    }

    /**
     * String转换Map
     *
     * @param mapText          :需要转换的字符串
     * @param KeySeparator     :字符串中的分隔符每一个key与value中的分割
     * @param ElementSeparator :字符串中每个元素的分割
     * @return Map<?,?>
     */
    public static Map<String, Object> StringToMap(String mapText) {

        if (mapText == null || mapText.equals("")) {
            return null;
        }
        mapText = mapText.substring(1);

        mapText = mapText;

        Map<String, Object> map = new HashMap<String, Object>();
        String[] text = mapText.split("\\" + SEP2); // 转换为数组
        for (String str : text) {
            String[] keyText = str.split(SEP3); // 转换key与value的数组
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0]; // key
            String value = keyText[1]; // value
            if (value.charAt(0) == 'M') {
                Map<?, ?> map1 = StringToMap(value);
                map.put(key, map1);
            } else if (value.charAt(0) == 'L') {
                List<?> list = StringToList(value);
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * String转换List
     *
     * @param listText :需要转换的文本
     * @return List<?>
     */
    public static List<Object> StringToList(String listText) {
        if (listText == null || listText.equals("") || listText.length() == 0) {
            return null;
        }
        listText = listText.substring(1);

        listText = listText;

        List<Object> list = new ArrayList<Object>();
        String[] text = listText.split(SEP1);
        if (text == null || text.length == 0) {
            return null;
        }
        for (String str : text) {
            if (str == null || str.length() == 0) {
                return list;
            }
            if (str.charAt(0) == 'M') {
                Map<?, ?> map = StringToMap(str);
                list.add(map);
            } else if (str.charAt(0) == 'L') {
                List<?> lists = StringToList(str);
                list.add(lists);
            } else {
                list.add(str);
            }
        }
        return list;
    }

    /**
     * 保留2位小数
     *
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        return decimalFormat.format(d);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        long i = Long.valueOf(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /**
     * 判断软键盘是否弹出
     */
    public static boolean isSHowKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }
}
