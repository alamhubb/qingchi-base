package com.qingchi.base.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2019-09-28 10:06
 */
public class GenderType {
    //全国
    public static final String all = "全部";
    public static final String male = "男";
    public static final String female = "女";
    public static final List<String> allGenders = Arrays.asList(all, male, female);
    public static final List<String> genders = Arrays.asList(male, female);
    public static final List<String> maleAry = Collections.singletonList(male);
    public static final List<String> femaleAry = Collections.singletonList(female);
}
