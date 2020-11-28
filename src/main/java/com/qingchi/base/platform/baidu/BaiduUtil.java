package com.qingchi.base.platform.baidu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingchi.base.platform.TokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author qinkaiyuan
 * @date 2019-10-24 13:30
 */
@Component
public class BaiduUtil {
    private static RestTemplate restTemplate;
    private static ObjectMapper objectMapper;
    private static String tokenUrl;

    @Value("${config.tokenUrl}")
    public void setTokenUrl(String tokenUrl) {
        BaiduUtil.tokenUrl = tokenUrl;
    }

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        BaiduUtil.restTemplate = restTemplate;
    }


    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        BaiduUtil.objectMapper = objectMapper;
    }

    /**
     * 获取百度token
     *
     * @return
     */
    public static String getAccessToken() {
        ResponseEntity<TokenDTO> responseEntity = restTemplate.getForEntity(tokenUrl + "getBdSession", TokenDTO.class);
        return Objects.requireNonNull(responseEntity.getBody()).getAccessToken();
    }

    public static String refreshAccessToken() {
        ResponseEntity<TokenDTO> responseEntity = restTemplate.getForEntity(tokenUrl + "refreshBdSession", TokenDTO.class);
        return Objects.requireNonNull(responseEntity.getBody()).getAccessToken();
    }
}
