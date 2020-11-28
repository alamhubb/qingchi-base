package com.qingchi.base.platform.weixin.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author qinkaiyuan
 * @date 2019-02-14 22:03
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MPBindPhoneVO {
    private String code;
    private String loginType;
    @NotBlank(message = "参数异常")
    private String encryptedData;
    @NotBlank(message = "参数异常")
    private String iv;
    @NotNull(message = "参数异常")
    private Boolean sessionEnable;
}
