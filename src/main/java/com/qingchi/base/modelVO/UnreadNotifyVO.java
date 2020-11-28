package com.qingchi.base.modelVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qingchi.base.constant.NotifyType;
import com.qingchi.base.model.notify.NotifyDO;
import com.qingchi.base.model.talk.CommentDO;
import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.model.talk.TalkImgDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.repository.talk.CommentRepository;
import com.qingchi.base.repository.talk.TalkImgRepository;
import com.qingchi.base.repository.talk.TalkRepository;
import com.qingchi.base.utils.JsonUtils;
import com.qingchi.base.utils.UserUtils;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author qinkaiyuan
 * @date 2019-09-26 16:00
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Component
public class UnreadNotifyVO {
    private static CommentRepository commentRepository;
    private static TalkImgRepository talkImgRepository;
    private static TalkRepository talkRepository;

    @Resource
    public void setTalkImgRepository(TalkImgRepository talkImgRepository) {
        UnreadNotifyVO.talkImgRepository = talkImgRepository;
    }

    @Resource
    public void setCommentRepository(CommentRepository commentRepository) {
        UnreadNotifyVO.commentRepository = commentRepository;
    }

    @Resource
    public void setTalkRepository(TalkRepository talkRepository) {
        UnreadNotifyVO.talkRepository = talkRepository;
    }

    private Integer talkId;
    private Integer talkUserId;
    private String avatar;
    private String nickname;
    private String content;
    private String replyContent;
    private String replyImg;
    private Date createTime;
    private Boolean vipFlag;
    private Boolean hasRead;

    public UnreadNotifyVO() {
    }

    public UnreadNotifyVO(UserDO user) {
        if (user != null) {
            this.nickname = user.getNickname();
            this.avatar = user.getAvatar();
            this.hasRead = false;
            this.vipFlag = user.getVipFlag();
        }
    }

    public UnreadNotifyVO(NotifyDO notifyDO) {
        this(UserUtils.get(notifyDO.getUserId()));
        Integer commentId = notifyDO.getCommentId();

        Optional<CommentDO> optionalCommentDO = commentRepository.findById(commentId);
        CommentDO commentDO = optionalCommentDO.get();
        this.content = commentDO.getContent();
        this.createTime = commentDO.getCreateTime();


        this.talkId = commentDO.getTalkId();

        Optional<TalkDO> talkDOOptional = talkRepository.findById(talkId);
        TalkDO talk = talkDOOptional.get();
        this.talkUserId = talk.getUserId();
        this.hasRead = notifyDO.getHasRead();

        switch (notifyDO.getType()) {
            case NotifyType.talk_comment:
                List<TalkImgDO> talkImgDOS = talkImgRepository.findTop3ByTalkId(talkId);
//                List<TalkImgDO> talkImgDOS = talk.getImgs();
                if (talkImgDOS.size() > 0) {
                    this.replyImg = talkImgDOS.get(0).getSrc();
                } else {
                    this.replyContent = talk.getContent();
                }
                break;
            case NotifyType.comment_comment:
                Optional<CommentDO> optionalCommentDO1 = commentRepository.findById(commentDO.getParentCommentId());
                this.replyContent = optionalCommentDO1.get().getContent();
                break;
            case NotifyType.reply_comment:
                Optional<CommentDO> optionalCommentDO2 = commentRepository.findById(commentDO.getReplyCommentId());
                this.replyContent = optionalCommentDO2.get().getContent();
                break;
        }
    }

    public static List<UnreadNotifyVO> unreadNotifyDOToVOS(List<NotifyDO> notifyDOS) {
        return notifyDOS.stream().map(UnreadNotifyVO::new).collect(Collectors.toList());
    }

    public TextMessage toMessage() {
        try {
            return new TextMessage(JsonUtils.objectMapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
