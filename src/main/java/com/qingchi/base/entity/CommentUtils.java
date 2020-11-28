package com.qingchi.base.entity;

import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.talk.CommentDO;
import com.qingchi.base.repository.talk.CommentRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class CommentUtils {
    private static CommentRepository commentRepository;

    @Resource
    public void setCommentRepository(CommentRepository commentRepository) {
        CommentUtils.commentRepository = commentRepository;
    }

    public static CommentDO get(Integer commentId) {
        Optional<CommentDO> commentDOOptional = commentRepository.findById(commentId);
        return commentDOOptional.get();
    }

    public static List<CommentDO> getAll(Integer talkId) {
        return commentRepository.findTop50ByTalkIdAndStatusInAndParentCommentIdIsNullOrderByUpdateTimeDesc(talkId, CommonStatus.selfCanSeeContentStatus);
    }
}
