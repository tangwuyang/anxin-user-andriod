package com.anxin.kitchen.utils;

import android.text.TextUtils;

import com.anxin.kitchen.bean.ContactEntity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 唐午阳 on 2018/4/27.
 */

public class LocalContactSearch {
    /**
     * 按群号-群名拼音搜索
     *
     * @param str
     */
    public static ArrayList<ContactEntity> searchContact(CharSequence str,
                                                       ArrayList<ContactEntity> allContacts) {
        ArrayList<ContactEntity> groupList = new ArrayList<ContactEntity>();
        // 如果搜索条件以0 1 +开头则按号码搜索
        if (str.toString().startsWith("0") || str.toString().startsWith("1")
                || str.toString().startsWith("+")) {
            for (ContactEntity contact : allContacts) {
                if (getGroupName(contact) != null
                        && contact.getMobile() + "" != null) {
                    if ((contact.getMobile() + "").contains(str)
                            || contact.getName().contains(str)) {
                        groupList.add(contact);
                    }
                }
            }
            return groupList;
        }
        CharacterParser finder = CharacterParser.getInstance();

        String result = "";
        for (ContactEntity contact : allContacts) {
            // 先将输入的字符串转换为拼音
            finder.setResource(str.toString());
            result = finder.getSpelling();
            if (contains(contact, result)) {
                groupList.add(contact);
            } else if ((contact.getName() + "").contains(str)) {
                groupList.add(contact);
            }
        }
        return groupList;
    }

    /**
     * 根据拼音搜索
     *
     * @param
     *
     * @param
     *
     * @param
     * *搜索条件是否大于6个字符
     * @return
     */
    private static boolean contains(ContactEntity contactEntity, String search) {
        if (TextUtils.isEmpty(contactEntity.getName())
               ) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
           /* String firstLetters = FirstLetterUtil
                    .getFirstLetter(getGroupName(contactEntity));
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();*/
        }

        if (true) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            CharacterParser finder = CharacterParser.getInstance();
            finder.setResource(getGroupName(contactEntity));
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
            flag = matcher2.find();
        }

        return flag;
    }

    private static String getGroupName(ContactEntity contact) {
        String strName = null;
        if (!TextUtils.isEmpty(contact.getName())) {
            strName = contact.getName();
        } else if (!TextUtils.isEmpty(contact.getMobile())) {
            strName = contact.getMobile();
        } else {
            strName = "";
        }

        return strName;
    }
}
