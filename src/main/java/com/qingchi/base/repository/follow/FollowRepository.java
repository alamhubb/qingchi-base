package com.qingchi.base.repository.follow;

import com.qingchi.base.config.redis.RedisKeysConst;
import com.qingchi.base.model.user.FollowDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
public interface FollowRepository extends JpaRepository<FollowDO, Integer> {
    //关注后用户缓存修改，一人+粉丝，一人+关注
    @Caching(evict = {
            //新增一条数据肯定所有数据清空，数据的显示数据变了
            @CacheEvict(cacheNames = RedisKeysConst.userById, key = "#follow.userId"),
            @CacheEvict(cacheNames = RedisKeysConst.userById, key = "#follow.beUserId")
    })
    FollowDO save(FollowDO follow);

    /**
     * 查询一个用户是否已经关注了另一个用户
     *
     * @param user
     * @param beUser
     * @return
     */
    Integer countByUserIdAndBeUserIdAndStatus(Integer userId, Integer beUserId, String status);

    //查出来以后在外面判断的是否为生效
    Optional<FollowDO> findFirstByUserIdAndBeUserIdOrderByIdDesc(Integer userId, Integer beUserId);

    //查询他的关注
    List<FollowDO> findTop30ByUserIdAndStatusOrderByUpdateTimeDesc(Integer userId, String status);
    //查询他的粉丝
    List<FollowDO> findTop30ByBeUserIdAndStatusOrderByUpdateTimeDesc(Integer beUserId, String status);

    //查询他的粉丝
    List<FollowDO> findAllByBeUserIdAndStatus(Integer userId, String status);
}


