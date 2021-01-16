package com.qingchi.base.platform.weixin.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2019-02-14 22:03
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoginDataVO {
    //qq、wx、phone,前台叫provider
    private String loginType;
    //todo，provider plateform好像还是有点乱，需要确认，涉及到数据库用户信息和常量类型
    private String provider;
    //h5、mp、app//先判断平台，然后判断provider
    private String platform;

    //app平台兑换openid unionid使用
    private String accessToken;
    //小程序平台兑换openid unionid使用
    private String code;
    private String openId;
    private String unionId;
    //app端登陆才有
    private String clientid;

    private String nickName;
    private String avatarUrl;
    private Integer gender;
    //出生日期
    private String birthday;
    private String city;

    //手机号登录使用
    private String phoneNum;
    private String authCode;
    private Boolean sessionEnable;

    //百度小程序使用
    private String encryptedData;
    private String iv;

    //暂未使用
    private String inviteCode;
}
