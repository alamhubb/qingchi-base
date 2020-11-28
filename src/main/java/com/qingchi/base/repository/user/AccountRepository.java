package com.qingchi.base.repository.user;

import com.qingchi.base.config.redis.RedisKeysConst;
import com.qingchi.base.model.account.AccountDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountDO, Integer> {
    @CacheEvict(cacheNames = RedisKeysConst.userById, key = "#account.userId")
    AccountDO save(AccountDO account);

    Optional<AccountDO> findFirstOneByQqMpOpenIdOrderByIdAsc(String qqMpOpenId);
    Optional<AccountDO> findFirstOneByWxMpOpenIdOrderByIdAsc(String wxMpOpenId);

    Optional<AccountDO> findFirstOneByQqUnionIdOrderByIdAsc(String unionId);

    Optional<AccountDO> findFirstOneByWxUnionIdOrderByIdAsc(String unionId);

    Optional<AccountDO> findOneByUserId(Integer userId);

}

