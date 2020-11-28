package com.qingchi.base.repository.match;

import com.qingchi.base.config.redis.RedisKeysConst;
import com.qingchi.base.model.match.MatchRequestDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRequestRepository extends JpaRepository<MatchRequestDO, Integer> {
    @CacheEvict(cacheNames = RedisKeysConst.userById, key = "#matchRequest.receiveUserId")
    MatchRequestDO save(MatchRequestDO matchRequest);

    MatchRequestDO queryMatchRequestByUserIdAndReceiveUserIdAndStatus(Integer userId, Integer receiveUserId, String status);

    MatchRequestDO queryMatchRequestByUserIdAndReceiveUserId(Integer userId, Integer receiveUserId);
}