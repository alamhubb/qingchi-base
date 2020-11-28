package com.qingchi.base.store;

import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.redis.TalkRedis;
import com.qingchi.base.repository.talk.TalkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TalkQueryRepository {
    public static final Logger logger = LoggerFactory.getLogger(TalkQueryRepository.class);
    @Resource
    private TalkRepository talkRepository;
    @Resource
    private TalkRedis talkRedis;

    public List<TalkDO> queryTalksTop10ByUser(List<Integer> talkIds, Integer userId) {
        List<Integer> ids = talkRepository.queryTalkIdsTop10ByUser(
                talkIds, userId, CommonStatus.otherCanSeeContentStatus, PageRequest.of(0, 10));
        return this.queryTalksByIds(ids);
    }

    //查看自己的动态，能查看到预审查状态的
    public List<TalkDO> queryTalksTop10ByMine(List<Integer> talkIds, Integer userId) {
        List<Integer> ids = talkRepository.queryTalkIdsTop10ByUser(
                talkIds, userId, CommonStatus.selfCanSeeContentStatus, PageRequest.of(0, 10));
        return this.queryTalksByIds(ids);
    }

    public List<TalkDO> queryTalksTop10ByUserFollow(List<Integer> talkIds, Integer userId) {
        List<Integer> ids = talkRepository.queryTalkIdsTop10ByUserFollow(
                talkIds, userId, CommonStatus.preAudit, CommonStatus.otherCanSeeContentStatus, CommonStatus.normal, PageRequest.of(0, 10));
        return this.queryTalksByIds(ids);
    }

    public List<TalkDO> queryTalksTop10ByGender(List<Integer> talkIds, Integer userId, List<String> genders) {
        List<Integer> ids = talkRepository.queryTalkIdsTop10ByGender(
                talkIds, userId, CommonStatus.preAudit, CommonStatus.otherCanSeeContentStatus, genders, PageRequest.of(0, 10));
        return this.queryTalksByIds(ids);
    }

    public List<TalkDO> queryTalksTop10ByGenderAgeAndLikeAdCode(List<Integer> talkIds, Integer userId, List<String> genders, Integer minAge, Integer maxAge, String adCode) {
        List<Integer> ids = talkRepository.queryTalkIdsTop10ByGenderAgeAndLikeAdCode(
                talkIds, userId, CommonStatus.preAudit, CommonStatus.otherCanSeeContentStatus, genders, minAge, maxAge, adCode, PageRequest.of(0, 10));
        return this.queryTalksByIds(ids);
    }

    public List<TalkDO> queryTalksTop10ByGenderAgeAndLikeAdCodeAndTagIds(List<Integer> talkIds, Integer userId, List<String> genders, Integer minAge, Integer maxAge, String adCode, List<Integer> tagIds) {
        List<Integer> ids = talkRepository.queryTalkIdsTop10ByGenderAgeAndLikeAdCodeAndTagIds(
                talkIds, userId, CommonStatus.preAudit, CommonStatus.otherCanSeeContentStatus, genders, minAge, maxAge, adCode, tagIds, PageRequest.of(0, 10));
        return this.queryTalksByIds(ids);
    }

    //根据id列表从缓存中读取talk列表
    public List<TalkDO> queryTalksByIds(List<Integer> ids) {
        List<TalkDO> talkDOS = new ArrayList<>();
        for (Integer id : ids) {
            Optional<TalkDO> talkDOOptional = talkRepository.findById(id);
            talkDOOptional.ifPresent(talkDOS::add);
//            TalkDO talkDO = talkRedis.findById(id);
//            talkDOS.add(talkDO);
        }
        return talkDOS;
    }
}