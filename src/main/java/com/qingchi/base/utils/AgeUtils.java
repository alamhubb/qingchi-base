package com.qingchi.base.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author qinkaiyuan
 * @date 2019-02-24 22:12
 */
public class AgeUtils {

    public static int getAgeByBirth(String birthDayStr) {
        Date birthDay = new Date();
        try {
            birthDay = DateUtils.simpleDateFormat.parse(birthDayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int age = 1;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            return age;
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }
}
