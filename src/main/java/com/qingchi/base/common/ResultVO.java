package com.qingchi.base.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.constant.ErrorCode;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2019-02-14 22:10
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResultVO<T> {
    //0成功，1，系统异常，2业务错误，3自定义错误
    private Integer errorCode = 0;
    private String errorMsg;
    private T data;

    public ResultVO() {
    }

    public ResultVO(ResultVO<?> resultVO) {
        this(resultVO.getErrorCode(), resultVO.getErrorMsg());
    }

    public ResultVO(T data) {
        this.data = data;
    }

    public ResultVO(Integer errorCode) {
        this.errorCode = errorCode;
        if (errorCode.equals(ErrorCode.SYSTEM_ERROR)) {
            this.errorMsg = (String) AppConfigConst.appConfigMap.get(AppConfigConst.errorCode604System);
        }
    }

    public ResultVO(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ResultVO(String errorMsg) {
        this.errorCode = ErrorCode.BUSINESS_ERROR;
        this.errorMsg = errorMsg;
    }

    public ResultVO(Integer errorCode, T data) {
        this(errorCode);
        this.data = data;
    }

    public ResultVO(Integer errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public boolean isCorrect() {
        return errorCode == 0;
    }

    public boolean hasError() {
        return errorCode != 0;
    }
}
