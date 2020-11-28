package com.qingchi.base.platform.weixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2020-03-21 22:28
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PushValue {
    private String value;

    public PushValue(String value) {
        this.value = value;
    }
}
