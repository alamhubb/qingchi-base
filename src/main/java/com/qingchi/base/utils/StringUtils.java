package com.qingchi.base.utils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2019-02-24 22:12
 */
public class StringUtils {
    //所有为空
    public static boolean isAllEmpty(String... stringList)  {
        //有一个不为空，则就不是全部为空
        return Arrays.stream(stringList).noneMatch(org.apache.commons.lang3.StringUtils::isNotEmpty);
    }

    //不是所有为空
    public static boolean isNotAllEmpty(String... stringList)  {
        //有一个不为空，则就不是全部为空
        return isAllEmpty(stringList);
    }

    //所有都不为空
    public static boolean isAllNotEmpty(String... stringList) {
        //没有为空的就是都不为空
        return Arrays.stream(stringList).noneMatch(org.apache.commons.lang3.StringUtils::isEmpty);
    }
}
