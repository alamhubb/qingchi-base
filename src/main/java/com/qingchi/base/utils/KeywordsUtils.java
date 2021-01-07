package com.qingchi.base.utils;

import com.qingchi.base.model.BaseModelDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordsUtils {

    public static String StringFilter(String str) {
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`_~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim().replaceAll(" ", "");
    }


    public static Map<String, Integer> chineseWordSegmentationGetKeywordsMap(List<BaseModelDO> contents) {
        Map<String, Integer> map = new HashMap<>();
        for (BaseModelDO content : contents) {
            KeywordsUtils.chineseWordSegmentationGetKeywordsMap(content.getContent());
        }
        return map;
    }

    public static Map<String, Integer> chineseWordSegmentationGetKeywordsMap(String content) {
        return KeywordsUtils.chineseWordSegmentationGetKeywordsMap(content, new HashMap<>());
    }

    public static Map<String, Integer> chineseWordSegmentationGetKeywordsMap(String content, Map<String, Integer> keyMap) {
        //删除空白字符和空格，去除特殊字符，升为大写
        String text = KeywordsUtils.StringFilter(content.trim().replaceAll("\\s*", "").toUpperCase());
        int textLength = text.length();
        //根据文本长度遍历，从第一个字开始遍历
        for (int i = 0; i < textLength; i++) {
            //最多6个字，0 - 5+2
            int keyMaxCount = 5;
            //最少两个字
            for (int j = 0; j < keyMaxCount; j++) {
                int endIndex = i + 2 + j;
                //如果结束索引大于文本索引，则退出
                if (endIndex > textLength) {
                    break;
                }
                //获取本次文本
                String key = text.substring(i, endIndex);
                //向map中求和
                keyMap.merge(key, 1, Integer::sum);
            }
        }
        return keyMap;
    }
}
