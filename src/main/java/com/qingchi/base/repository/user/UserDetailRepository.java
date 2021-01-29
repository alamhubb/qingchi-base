package com.qingchi.base.repository.user;

import com.qingchi.base.model.user.UserDetailDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetailDO, Integer> {
    @Cacheable(cacheNames = "userDetail", key = "#userId")
    Optional<UserDetailDO> findFirstOneByUserId(Integer userId);

    @CachePut(cacheNames = "userDetail", key = "#userDetail.userId")
    UserDetailDO save(UserDetailDO userDetail);
}

