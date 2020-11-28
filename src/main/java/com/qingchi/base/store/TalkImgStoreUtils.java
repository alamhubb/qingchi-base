package com.qingchi.base.store;

import com.qingchi.base.model.talk.TalkImgDO;
import com.qingchi.base.repository.talk.TalkImgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TalkImgStoreUtils {
    public static final Logger logger = LoggerFactory.getLogger(TalkImgStoreUtils.class);
    private static TalkImgRepository talkImgRepository;

    @Resource
    public  void setTalkImgRepository(TalkImgRepository talkImgRepository) {
        TalkImgStoreUtils.talkImgRepository = talkImgRepository;
    }

    //根据id列表从缓存中读取talk列表
    public static List<TalkImgDO> findTop3ByTalkId(Integer talkId) {
        return talkImgRepository.findTop3ByTalkId(talkId);
    }
}