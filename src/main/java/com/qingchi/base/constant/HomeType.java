package com.qingchi.base.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2019-09-20 21:47
 */
public class HomeType {
    //全国
    public static final String all = "all";
    public static final String home = "home";
    public static final String city = "city";
    public static final String tag = "tag";
    //喜欢我的
    public static final String likeMe = "likeMe";
    //我喜欢的
    public static final String iLike = "iLike";

    public static final String follow = "follow";
    public static final String follow_name = "关注";
    //这里还是要使用英文名匹配，要考虑到这个名字以后是否会变动，比如首页可能改为广场
    public static final String home_name = "首页";
    public static final String city_name = "同城";
    public static final String tag_name = "话题";

    public static final List<String> homeTypes = Arrays.asList(follow_name, home_name, city_name);
}
