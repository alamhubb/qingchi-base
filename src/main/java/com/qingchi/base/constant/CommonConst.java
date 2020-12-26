package com.qingchi.base.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2019-06-22 22:23
 */

//什么算是公共常量，就是放到任何项目都可以用的
public class CommonConst {
    public static final List<Integer> emptyIds = Collections.singletonList(0);
    public static final List<Long> emptyLongIds = Collections.singletonList(0L);

    public static final int zero = 0;
    public static final String boy = "男";
    public static final String girl = "女";
    public static final Boolean yes = true;
    public static final Boolean no = false;

    public static final String initBirthday = "1999-01-01";
    //30个清池币1个月会员
    public static final Integer VIP_QCB_VALUE = 30;

    public static final Integer second = 1000;
    public static final Integer minute = 60 * second;
    public static final Integer hour = 60 * minute;
    public static final Integer day = 24 * hour;
    public static final Integer week = 7 * hour;
    public static final Long month = (long) 32 * day;
    //默认系统用户为 11个9
    public static final String systemUserPhoneNum = "99999999999";
    public static final String chinaDistrictCode = "100000";
    public static final String initAdCode = "100001";
    public static final String positionAdCode = "100002";
}
