package com.qingchi.base.repository.shell;

import com.qingchi.base.config.redis.RedisKeysConst;
import com.qingchi.base.model.user.UserContactDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
public interface UserContactRepository extends JpaRepository<UserContactDO, Integer> {
    Optional<UserContactDO> findFirstByUserIdAndBeUserIdAndStatus(Integer userId, Integer beUserId, String status, String type);
}


