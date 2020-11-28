package com.qingchi.base.utils;

import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.model.user.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 当前线程的request工具类，从request中获取内容
 */
public class RequestUtils {
    private final Logger log = LoggerFactory.getLogger(getClass());

    //从session中获取user，不需要再读redis和数据库了
    public static UserDO getUser(HttpServletRequest req) {
        return (UserDO) req.getAttribute(AppConfigConst.appUserKey);
    }

    public static UserDO getUser() {
        HttpServletRequest request = RequestUtils.getRequest();
        return (UserDO) request.getAttribute(AppConfigConst.appUserKey);
    }

    //获取到当前线程绑定的请求对象
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    //暂未使用此方法
    public static Cookie[] getCookies() {
        return getRequest().getCookies();
    }

    //根据cookie名获取cookie值
    public static String getCookie(String cookieName) {
        Cookie[] cookies = getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
