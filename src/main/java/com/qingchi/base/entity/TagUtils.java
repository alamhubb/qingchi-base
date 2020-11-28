package com.qingchi.base.entity;

import com.qingchi.base.model.talk.TagDO;
import com.qingchi.base.repository.tag.TagRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TagUtils {
    private static TagRepository tagRepository;

    @Resource
    public void setTagRepository(TagRepository tagRepository) {
        TagUtils.tagRepository = tagRepository;
    }

    public static List<TagDO> save(List<TagDO> tagDOS) {
        return tagRepository.saveAll(tagDOS);
    }
}
