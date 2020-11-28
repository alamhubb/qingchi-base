package com.qingchi.base.utils;


import com.qingchi.base.config.ResultException;
import com.qingchi.base.entity.CommentUtils;
import com.qingchi.base.entity.TalkUtils;
import com.qingchi.base.model.*;
import com.qingchi.base.constant.ReportContentType;
import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.model.talk.CommentDO;
import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.model.user.UserImgDO;
import com.qingchi.base.repository.chat.MessageRepository;
import com.qingchi.base.repository.user.UserImgRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BaseModelUtils<T> {
    private static MessageRepository messageRepository;
    private static UserImgRepository userImgRepository;

    @Resource
    public void setMessageRepository(MessageRepository messageRepository) {
        BaseModelUtils.messageRepository = messageRepository;
    }

    @Resource
    public void setUserImgRepository(UserImgRepository userImgRepository) {
        BaseModelUtils.userImgRepository = userImgRepository;
    }

    public static BaseModelDO getModelByReport(ReportDO reportDO) {
        String reportContentType = reportDO.getReportContentType();
        if (!ReportContentType.reportContentTypeTypes.contains(reportContentType)) {
            throw new ResultException("不存在的内容类型");
        }
        BaseModelDO modelDO;
        if (reportContentType.equals(ReportContentType.talk)) {
            modelDO = TalkUtils.getTalkById(reportDO.getTalkId());
        } else if (reportContentType.equals(ReportContentType.comment)) {
            modelDO = CommentUtils.get(reportDO.getCommentId());
        } else if (reportContentType.equals(ReportContentType.message)) {
            modelDO = messageRepository.findById(reportDO.getMessageId()).get();
        } else if (reportContentType.equals(ReportContentType.userImg)) {
            modelDO = userImgRepository.findById(reportDO.getUserImgId()).get();
        } else if (reportContentType.equals(ReportContentType.user)) {
            modelDO = messageRepository.findById(reportDO.getMessageId()).get();
        } else {
            throw new ResultException("不存在的内容类型");
        }
        return modelDO;
    }

    public static <T extends BaseModelParentDO> void setBaseModel(T baseModelParentDO, BaseModelDO model) {
        if (model instanceof TalkDO) {
            TalkDO talkDO = BaseModelUtils.getModelByClass(model);
            baseModelParentDO.setTalkId(talkDO.getId());
        } else if (model instanceof CommentDO) {
            CommentDO commentDO = BaseModelUtils.getModelByClass(model);
            baseModelParentDO.setCommentId(commentDO.getId());
        } else if (model instanceof MessageDO) {
            MessageDO messageDO = BaseModelUtils.getModelByClass(model);
            baseModelParentDO.setMessageId(messageDO.getId());
        } else if (model instanceof UserImgDO) {
            UserImgDO userImgDO = BaseModelUtils.getModelByClass(model);
            baseModelParentDO.setUserImgId(userImgDO.getId());
        } else {
            throw new ResultException("错误的内容类型");
        }
    }

    public static ReportDO setBaseModelId(ReportDO reportDO, BaseModelDO baseModelDO) {
        switch (baseModelDO.getReportContentType()) {
            case ReportContentType.userImg:
                reportDO.setUserImgId(baseModelDO.getDbId());
                break;
            case ReportContentType.talk:
                reportDO.setTalkId(baseModelDO.getDbId());
                break;
            case ReportContentType.comment:
                reportDO.setCommentId(baseModelDO.getDbId());
                break;
            case ReportContentType.message:
                reportDO.setMessageId(baseModelDO.getDbId().longValue());
                break;
            default:
                throw new ResultException("错误的内容类型");
        }
        return reportDO;
    }

    public static <T> T getModelByClass(BaseModelDO model) {
        return (T) model;
    }

    public static <T> T getModelByClass(BaseModelDO model, Class<T> childClass) {
        return (T) model;
    }
}
