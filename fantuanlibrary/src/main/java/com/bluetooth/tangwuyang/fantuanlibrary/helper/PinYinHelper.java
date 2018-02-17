package com.bluetooth.tangwuyang.fantuanlibrary.helper;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.regex.Pattern;

/**
 * @author: tangwuyang on 2018/2/12
 */
public class PinYinHelper {
    private static final String PATTERN_LETTER = "^[a-zA-Z]+$";

    /**
     * 将中文转换为pinyin
     */
    public static String getPingYin(String inputString) {

        char[] input = inputString.trim().toCharArray();
        String output = "";
        for (int i = 0; i < input.length; i++) {
            output += Pinyin.toPinyin(input[i]);
        }
        return output.toLowerCase();
    }

    /**
     * 是否为字母
     * @param inputString
     * @return
     */
    public static boolean isLetter(String inputString) {

        return Pattern.matches(PATTERN_LETTER, inputString);
    }
}
