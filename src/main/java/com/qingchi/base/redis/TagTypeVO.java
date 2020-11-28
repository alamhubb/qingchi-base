package com.qingchi.base.redis;

import com.qingchi.base.model.talk.TagTypeDO;
import com.qingchi.base.store.TagStoreUtils;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinkaiyuan
 * @date 2019-11-07 15:20
 */

@Data
@Component
public class TagTypeVO {
    private static TagStoreUtils tagQueryRepository;

    @Resource
    public void setTagQueryRepository(TagStoreUtils tagQueryRepository) {
        TagTypeVO.tagQueryRepository = tagQueryRepository;
    }

    public Integer id;
    public String name;
    public Boolean selected;
    private Integer talkCount;
    private Integer count;
    private List<TagVO> tags;

    public TagTypeVO() {
    }

    public TagTypeVO(TagTypeDO tagDO) {
        this.id = tagDO.getId();
        this.name = tagDO.getName();
        this.count = tagDO.getCount();
        this.talkCount = tagDO.getTalkCount();
    }


    public static List<TagTypeVO> tagDOToVOS(List<TagTypeDO> DOs) {
        return DOs.stream().map(TagTypeVO::new).collect(Collectors.toList());
    }
}
