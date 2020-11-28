package com.qingchi.base.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.model.user.TokenDO;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2019-02-19 22:27
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TokenVO {
    private String tokenCode;

    public TokenVO(TokenDO tokenDO) {
        this.tokenCode = tokenDO.getTokenCode();
    }
}
