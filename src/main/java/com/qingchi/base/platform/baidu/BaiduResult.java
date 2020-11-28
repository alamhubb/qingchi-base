package com.qingchi.base.platform.baidu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * @author qinkaiyuan
 * @date 2019-10-24 16:25
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BaiduResult {
    private Integer errno;
    private String errmsg;
    private Map<String, String> data;

    public boolean hasError() {
        return this.errno != null && this.errno != 0;
    }
}
