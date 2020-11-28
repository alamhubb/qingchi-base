package com.qingchi.base.entity;

import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.repository.talk.TalkRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class TalkUtils {
    private static TalkRepository talkRepository;

    @Resource
    public void setTalkRepository(TalkRepository talkRepository) {
        TalkUtils.talkRepository = talkRepository;
    }

    public static TalkDO getTalkById(Integer talkId) {
        Optional<TalkDO> talkDOOptional = talkRepository.findById(talkId);
        return talkDOOptional.get();
    }
}
