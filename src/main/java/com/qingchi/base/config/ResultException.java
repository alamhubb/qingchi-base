package com.qingchi.base.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qinkaiyuan
 * @date 2019-02-14 22:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResultException extends RuntimeException {
    private final Integer errorCode = ErrorCode.SYSTEM_ERROR;
    private String errorMsg;

    public ResultException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
