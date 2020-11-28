package com.qingchi.base.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author qinkaiyuan
 * @date 2020-03-14 0:29
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TokenDTO {
    public String accessToken;


    public TokenDTO() {
    }

    public TokenDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
