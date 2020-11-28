package com.qingchi.base.repository.talk;

import com.qingchi.base.model.talk.TalkImgDO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkImgRepository extends JpaRepository<TalkImgDO, Integer> {
    @Cacheable(cacheNames = "talkImgsTalkId", key = "#talkId")
    List<TalkImgDO> findTop3ByTalkId(Integer talkId);

//    @CachePut(cacheNames = "talkImgsTalkId", key = "#imgs[0].talkId")
//    List<TalkImgDO> saveAll(List<TalkImgDO> imgs);
}
