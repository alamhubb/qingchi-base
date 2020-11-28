package com.qingchi.base.repository.hug;

import com.qingchi.base.model.talk.HugDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HugRepository extends JpaRepository<HugDO, Integer> {
    Integer countByTalkIdAndUserId(Integer talkId, Integer userId);
}