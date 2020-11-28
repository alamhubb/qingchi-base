package com.qingchi.base.platform.weixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2019-10-24 16:25
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HttpResult {
    private Integer errcode;
    private String errmsg;
    public boolean hasError() {
        return this.errcode != null && this.errcode != 0;
    }
}
