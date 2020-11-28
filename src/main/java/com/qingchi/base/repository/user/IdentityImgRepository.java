package com.qingchi.base.repository.user;

import com.qingchi.base.config.redis.RedisKeysConst;
import com.qingchi.base.model.user.IdentityImgDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IdentityImgRepository extends JpaRepository<IdentityImgDO, Long> {
    @CacheEvict(cacheNames = RedisKeysConst.userById, key = "#identityImg.userId")
    IdentityImgDO save(IdentityImgDO identityImg);

    Optional<IdentityImgDO> findFirstByUserIdAndStatusOrderByIdDesc(Integer userId, String status);

    List<IdentityImgDO> findFirst20ByOrderByIdDesc();

//    List<IdentityImgDO> findFirst20ByUserGenderInOrderByUserFaceValueDesc(List<String> genders, Pageable pageable);

    List<IdentityImgDO> findFirst600ByOrderByIdDesc();
}

