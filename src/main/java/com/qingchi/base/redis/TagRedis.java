package com.qingchi.base.redis;

import com.qingchi.base.constant.CommonConst;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.talk.TagDO;
import com.qingchi.base.model.talk.TagTypeDO;
import com.qingchi.base.repository.tag.TagRepository;
import com.qingchi.base.repository.tag.TagTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TagRedis {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private TagRepository tagRepository;
    @Resource
    private TagTypeRepository tagTypeRepository;

    //新增一个清空所有，把当前的缓存
    @Caching(
            evict = {@CacheEvict(cacheNames = "tagsAll")},
            put = {@CachePut(cacheNames = {"tagId"}, key = "#tag.id")}
    )
    public TagDO save(TagDO tag) {
        return tagRepository.save(tag);
    }

    @Cacheable(cacheNames = "tagById", key = "#tagId")
    public Optional<TagDO> findById(Integer tagId) {
        return tagRepository.findByIdAndStatus(tagId, CommonStatus.normal);
    }

    //获取talk下的
    @Cacheable(cacheNames = "tagIdsByTalkId", key = "#talkId")
    public List<Integer> getTagIdsByTalkId(Integer talkId) {
        return tagRepository.findTagIdsByTalkIdAndStatus(talkId, CommonStatus.normal);
    }

    /**
     * 查询热门下包含子节点，和所有省份不包含子节点
     *
     * @return
     */
    @Cacheable(cacheNames = "tagsHot")
    public List<TagVO> getHotTags() {
        List<TagDO> tagDOS = tagRepository.findTop10ByStatusOrderByCountDesc(CommonStatus.normal, PageRequest.of(0, 10));
        return TagVO.tagDOToVOS(tagDOS);
    }

    @Cacheable(cacheNames = "tagsAll")
    public List<TagVO> getAllTags() {
        List<TagDO> tagDOS = tagRepository.findAllByStatusOrderByCountDesc(CommonStatus.normal);
        return TagVO.tagDOToVOS(tagDOS);
    }

    @Cacheable(cacheNames = "tagTypesHot")
    public List<TagTypeVO> getHotTagTypes() {
        List<TagTypeVO> initTagTypes = new ArrayList<>();
        //插入一个热门类别
        initTagTypes.add(getHotTagType());
        //插入一个热门类别
        List<TagTypeDO> tagTypes = getTagTypes();
        initTagTypes.addAll(TagTypeVO.tagDOToVOS(tagTypes));
        return initTagTypes;
    }

    @Cacheable(cacheNames = "tagTypesAll")
    public List<TagTypeVO> getAllTageTypes() {
        List<TagTypeVO> initTagTypes = new ArrayList<>();
        //插入一个热门类别
        initTagTypes.add(getHotTagType());
        //查询全部，但是不查询子节点
        List<TagTypeDO> tagTypes = getTagTypes();
        initTagTypes.addAll(tagTypesSetTags(tagTypes));
        return initTagTypes;
    }


    //获取热门类型的tagtype
    private TagTypeVO getHotTagType() {
        //插入一个热门类别
        TagTypeVO hotTagTypeVO = new TagTypeVO();
        hotTagTypeVO.setId(9999);
        hotTagTypeVO.setName("热门");

        hotTagTypeVO.setTags(getHotTags());
        return hotTagTypeVO;
    }


    private List<TagTypeDO> getTagTypes() {
        return tagTypeRepository.findByStatusAndTalkCountGreaterThanOrderByTalkCountDesc(CommonStatus.normal, CommonConst.zero);
    }

    private List<TagTypeVO> tagTypesSetTags(List<TagTypeDO> DOs) {
        return DOs.stream().map(tagTypeDO -> {
            TagTypeVO tagTypeVO = new TagTypeVO(tagTypeDO);
            tagTypeVO.setTags(getTagsByTagTypeId(tagTypeDO.getId()));
            return tagTypeVO;
        }).collect(Collectors.toList());
    }


    //根据typeid获取所有
    private List<TagVO> getTagsByTagTypeId(Integer tagTypeId) {
        List<TagDO> list = tagRepository.findByTagTypeIdAndStatusOrderByCountDesc(tagTypeId, CommonStatus.normal);
        // 从数据库中获取tag列表
        return TagVO.tagDOToVOS(list);
    }
}