package com.qingchi.base.utils;


import com.qingchi.base.constant.ErrorMsg;

import java.text.MessageFormat;
import java.util.Date;

public class ErrorMsgUtil {
    public static String getErrorCode605ContactServiceValue(Date date) {
        Integer manyDay = DateUtils.HowManyDaysFromNow(date);
        return MessageFormat.format(ErrorMsg.errorCode605ContactServiceValue, manyDay);
    }
}
