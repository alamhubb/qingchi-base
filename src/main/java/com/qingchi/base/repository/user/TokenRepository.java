package com.qingchi.base.repository.user;

import com.qingchi.base.model.user.TokenDO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
public interface TokenRepository extends JpaRepository<TokenDO, Integer> {
    @Cacheable(cacheNames = "getTokenCodeByUserId", key = "#userId")
    @Query(nativeQuery = true, value = "select t.token_code from token t where t.user_id =:userId order by id desc limit 1")
    String findFirstOneTokenCodeByUserId(Integer userId);

    TokenDO save(TokenDO tokenDO);
}


