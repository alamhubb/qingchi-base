package com.qingchi.base.utils;

/**
 * @author qinkaiyuan
 * @date 2019-10-24 17:28
 */
public class ImgUtil {
    public static String getUserImgUrl(Integer userId) {
        return "user/" + userId + "/*";
    }
}
