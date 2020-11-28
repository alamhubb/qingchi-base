package com.qingchi.base.store;

import com.qingchi.base.model.user.TokenDO;
import com.qingchi.base.repository.user.TokenRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
@Component
public class TokenPlusRepository {
    @Resource
    private TokenRepository tokenRepository;

    @CachePut(cacheNames = "getTokenCodeByUserId", key = "#tokenDO.userId")
    public String saveTokenDO(TokenDO tokenDO) {
        tokenDO = tokenRepository.save(tokenDO);
        return tokenDO.getTokenCode();
    }
}


