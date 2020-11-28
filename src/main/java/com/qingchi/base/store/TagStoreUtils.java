package com.qingchi.base.store;

import com.qingchi.base.model.talk.TagDO;
import com.qingchi.base.redis.TagRedis;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TagStoreUtils {

    //获取talk下的
    public static List<TagDO> getTagsByTalkId(Integer talkId) {
        List<Integer> tagIds = tagRedis.getTagIdsByTalkId(talkId);
        return findTagsByIds(tagIds);
    }

    public static TagDO findById(Integer tagId) {
        Optional<TagDO> tagDOOptional = tagRedis.findById(tagId);
        return tagDOOptional.orElse(null);
    }

    public static TagDO save(TagDO tagDO) {
        return tagRedis.save(tagDO);
    }


    private static TagRedis tagRedis;

    @Resource
    public void setTagRedis(TagRedis tagRedis) {
        TagStoreUtils.tagRedis = tagRedis;
    }

    private static List<TagDO> findTagsByIds(List<Integer> ids) {
        List<TagDO> tagDOS = new ArrayList<>();
        for (Integer id : ids) {
            tagDOS.add(findById(id));
        }
        return tagDOS;
    }
}