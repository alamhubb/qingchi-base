package com.qingchi.base.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 置顶类型
 *
 * @author qinkaiyuan
 * @date 2018-09-16 10:58
 */
public class ChatTopType {
    //系统公告
    public static final Integer system = 1;
    //通知
    public static final Integer notify = 3;
    //官方群聊
    public static final Integer system_group = 6;
    //普通
    public static final Integer simple = 10;

    public static final Map<String, Integer> chatTopTypeMap = new HashMap<String, Integer>() {
        {
            put(ChatType.system_group, system_group);
            put(ChatType.single, simple);
            put(ChatType.match, simple);
        }
    };
}

