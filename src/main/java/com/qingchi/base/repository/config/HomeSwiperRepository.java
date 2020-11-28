package com.qingchi.base.repository.config;

import com.qingchi.base.model.system.HomeSwiperDO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@CacheConfig(cacheNames = "homeSwipers")
public interface HomeSwiperRepository extends JpaRepository<HomeSwiperDO, Integer> {
    @Cacheable
    List<HomeSwiperDO> findAllByStatusOrderByTopLevelAscIdDesc(String status);
}

