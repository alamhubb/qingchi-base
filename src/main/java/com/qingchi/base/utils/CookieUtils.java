package com.qingchi.base.utils;

import javax.servlet.http.Cookie;

public class CookieUtils {

    //给请求的返回内容中写入cookie
    public static void writeCookie(String value) {
        Cookie cookie = new Cookie(TokenUtils.TOKEN_NAME, value);
        cookie.setPath("/");
        //60*24*60*60 60天
        cookie.setMaxAge(5184000);
        RequestUtils.getResponse().addCookie(cookie);
    }
}

   /* public static Cookie getCookieByName(HttpServletRequest request,String cookieName) throws UnsupportedEncodingException {
        Cookie[] cookies=request.getCookies();
        if(cookies == null){
            return null;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieName)){
                return new Cookie(cookie.getName(), URLDecoder.decode(cookie.getValue(), "UTF-8"));
            }
        }
        return null;
    }*/