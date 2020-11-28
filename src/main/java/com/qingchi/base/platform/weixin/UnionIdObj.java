package com.qingchi.base.platform.weixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2019-11-28 16:51
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UnionIdObj {
    private String unionid;
}
