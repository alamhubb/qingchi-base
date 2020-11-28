package com.qingchi.base.redis;

import com.qingchi.base.model.talk.TagDO;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinkaiyuan
 * @date 2019-11-07 15:20
 */

@Data
public class TagVO {
    public Integer id;
    public String name;
    public String avatar;
    public String description;
    public Boolean selected;
    private Integer talkCount;
    private Integer count;

    public TagVO() {
    }

    public TagVO(TagDO tagDO) {
        this.id = tagDO.getId();
        this.name = StringUtils.substring(tagDO.getName(), 0, 4);
        this.selected = false;
        this.count = tagDO.getCount();
        this.talkCount = tagDO.getTalkCount();
        this.avatar = tagDO.getAvatar();
        this.description = tagDO.getDescription();
    }

    public static List<TagVO> tagDOToVOS(List<TagDO> DOs) {
        return DOs.stream().map(TagVO::new).collect(Collectors.toList());
    }
}
