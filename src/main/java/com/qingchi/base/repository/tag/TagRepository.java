package com.qingchi.base.repository.tag;

import com.qingchi.base.model.talk.TagDO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TagRepository extends JpaRepository<TagDO, Integer> {
    Optional<TagDO> findByIdAndStatus(Integer tagId, String status);

    Optional<TagDO> findOneByName(String name);

    @Query("select t.id from TagDO t,TalkTagDO tt where t.id = tt.tagId and tt.talkId =:talkId and t.status =:status")
    List<Integer> findTagIdsByTalkIdAndStatus(Integer talkId, String status);

    List<TagDO> findByTagTypeIdAndStatusOrderByCountDesc(Integer tagTypeId, String status);

    List<TagDO> findAllByStatusOrderByCountDesc(String status);

    List<TagDO> findTop10ByStatusOrderByCountDesc(String status, Pageable pageable);
}