package com.qingchi.base.constant.status;

import com.qingchi.base.constant.CommonStatus;

/**
 * 会话聊天的状态，暂不使用，全局使用一样的，全局统一，避免出现文字不一致问题
 */
public class ConstStatus {
    //启用
    //待开启，需要有一方发送一条消息后，才改为开启状态
    public static final String waitOpen = "待开启";

    public static final String close = "已关闭";

    public static final String beClose = "被关闭";

}

