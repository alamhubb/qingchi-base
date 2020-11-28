package com.qingchi.base.service;

import com.qingchi.base.config.ResultException;
import com.qingchi.base.model.BaseModelDO;
import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.repository.chat.MessageRepository;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.repository.report.ReportRepository;
import com.qingchi.base.model.talk.CommentDO;
import com.qingchi.base.repository.talk.CommentRepository;
import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.repository.talk.TalkRepository;
import com.qingchi.base.model.user.UserImgDO;
import com.qingchi.base.repository.user.UserImgRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class BaseModelService {
    @Resource
    private TalkRepository talkRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private UserImgRepository userImgRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private ReportRepository reportRepository;

    public BaseModelDO save(BaseModelDO model) {
        if (model instanceof TalkDO) {
            TalkDO talkDO = (TalkDO) model;
            return talkRepository.save(talkDO);
        } else if (model instanceof CommentDO) {
            CommentDO commentDO = (CommentDO) model;
            return commentRepository.save(commentDO);
        } else if (model instanceof MessageDO) {
            MessageDO messageDO = (MessageDO) model;
            return messageRepository.save(messageDO);
        } else if (model instanceof UserImgDO) {
            UserImgDO userImgDO = (UserImgDO) model;
            return userImgRepository.save(userImgDO);
        } else {
            throw new ResultException("错误的内容类型");
        }
    }

    public Optional<ReportDO> findReportByModel(BaseModelDO model) {
        if (model instanceof TalkDO) {
            TalkDO talkDO = (TalkDO) model;
            return reportRepository.findFirstOneByTalkId(talkDO.getId());
        } else if (model instanceof CommentDO) {
            CommentDO commentDO = (CommentDO) model;
            return reportRepository.findFirstOneByCommentId(commentDO.getId());
        } else if (model instanceof MessageDO) {
            MessageDO messageDO = (MessageDO) model;
            return reportRepository.findFirstOneByMessageId(messageDO.getId());
        } else if (model instanceof UserImgDO) {
            UserImgDO userImgDO = (UserImgDO) model;
            return reportRepository.findFirstOneByUserImgId(userImgDO.getId());
        } else {
            throw new ResultException("错误的内容类型");
        }
    }
}
