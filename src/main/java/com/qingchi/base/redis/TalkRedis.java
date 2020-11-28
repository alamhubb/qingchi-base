package com.qingchi.base.redis;

import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.repository.talk.TalkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;


@Repository
public class TalkRedis {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private TalkRepository talkRepository;


    @Cacheable(cacheNames = "talkById", key = "#id")
    public TalkDO findById(Integer id) {
        Optional<TalkDO> talkDOOptional = talkRepository.findById(id);
        return talkDOOptional.orElse(null);
    }
}


