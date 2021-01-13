package com.qingchi.base.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class TokenUtils {
    //    public static final Key key = MacProvider.generateKey();
    //key 这样可以保证每次请求生成的token一致，方便测试
    private static String tokenKey;

    @Value("${config.tokenKey}")
    public void setTokenKey(String tokenKey) {
        TokenUtils.tokenKey = tokenKey;
    }

    public final static String TOKEN_NAME = "token";


    public static String getToken() {
        HttpServletRequest request = RequestUtils.getRequest();
        return request.getHeader(TOKEN_NAME);
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 传入用户名获取token
     *
     * @param userId
     * @return
     */
    public static String generateToken(Integer userId) {
        return Jwts.builder()//返回的字符串便是我们的jwt串了
                .setSubject(userId + "_" + getUUID())//设置主题
                .signWith(SignatureAlgorithm.HS256, tokenKey)//设置算法（必须）
                .compact();//这个是全部设置完成后拼成jwt串的方法
    }


    //使用websocket时无法获取request必须传入token
    public static String getIdByToken(String token) {
        if (TokenUtils.isCorrect(token)) {
            try {
                return Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token).getBody().getSubject().split("_")[0];
            } catch (MalformedJwtException e) {
                QingLogger.logger.info(e.getMessage());
            }
        }
        return null;
    }

    //判断是否有效token
    public static boolean isCorrect(String token) {
        return StringUtils.isNotEmpty(token)
                && !"undefined".equals(token)
                && !"null".equals(token)
//                && !"\"[object Null]\"".equals(token)
                && !"[object Null]".equals(token)
                && token.length() != 32;
    }

    public static Boolean notCorrect(String token) {
        return !TokenUtils.isCorrect(token);
    }
}