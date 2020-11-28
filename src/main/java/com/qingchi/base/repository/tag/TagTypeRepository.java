package com.qingchi.base.repository.tag;

import com.qingchi.base.model.talk.TagTypeDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagTypeRepository extends JpaRepository<TagTypeDO, Integer> {

    List<TagTypeDO> findByStatusAndTalkCountGreaterThanOrderByTalkCountDesc(String status, Integer zeroCount);
}