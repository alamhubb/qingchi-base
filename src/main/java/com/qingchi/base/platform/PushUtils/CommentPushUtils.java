package com.qingchi.base.platform.PushUtils;

import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.constant.ProviderType;
import com.qingchi.base.entity.CommentUtils;
import com.qingchi.base.entity.TalkUtils;
import com.qingchi.base.model.notify.NotifyDO;
import com.qingchi.base.model.talk.CommentDO;
import com.qingchi.base.model.talk.TalkDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.modelVO.PushNotifyVO;
import com.qingchi.base.platform.PushMsgDTO;
import com.qingchi.base.platform.qq.QQConst;
import com.qingchi.base.platform.weixin.PushValue;
import com.qingchi.base.platform.weixin.WxConst;
import com.qingchi.base.utils.DateUtils;
import com.qingchi.base.utils.UserUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * @author qinkaiyuan
 * @date 2020-03-22 2:04
 */
public class CommentPushUtils {
    //动态评论通知
    public static PushMsgDTO getCommentPushDTO(String platform, NotifyDO notify, UserDO requestUser) {
        CommentDO comment = CommentUtils.get(notify.getCommentId());

        TalkDO talk = TalkUtils.getTalkById(comment.getTalkId());
        CommentDO replyComment;
        if (comment.getReplyCommentId() == null) {
            replyComment = CommentUtils.get(comment.getParentCommentId());
        } else {
            replyComment = CommentUtils.get(comment.getReplyCommentId());
        }

        PushNotifyVO pushNotifyVO = new PushNotifyVO();
        //构建基础数据
        pushNotifyVO.setContent(new PushValue(StringUtils.substring(comment.getContent(), 0, 20)));
        pushNotifyVO.setNickname(new PushValue(requestUser.getNickname()));
        pushNotifyVO.setDate(new PushValue(DateUtils.simpleTimeFormat.format(comment.getCreateTime())));
        pushNotifyVO.setBeContent(new PushValue(StringUtils.substring(replyComment.getContent(), 0, 20)));
        pushNotifyVO.setBeNickname(new PushValue(UserUtils.get(replyComment.getUserId()).getNickname()));

        HashMap<String, Object> data = new HashMap<>();
        PushMsgDTO pushMsgDTO = null;
        String page = AppConfigConst.notify_skip_page + talk.getId();
        if (platform.equals(ProviderType.qq)) {
            //评论内容
            data.put("keyword5", pushNotifyVO.getContent());
            //评论用户
            data.put("keyword3", pushNotifyVO.getNickname());
            //评论时间
            data.put("keyword4", pushNotifyVO.getDate());
            //帖子内容
            data.put("keyword1", pushNotifyVO.getBeContent());
            //评论者
            data.put("keyword2", pushNotifyVO.getBeNickname());

            pushMsgDTO = new PushMsgDTO(QQConst.comment_template_id, page, data, "keyword5.DATA");
        } else if (platform.equals(ProviderType.wx)) {
            //评论内容
            data.put("thing2", pushNotifyVO.getContent());
            //评论用户
            data.put("thing3", pushNotifyVO.getNickname());
            //评论时间
            data.put("time4", pushNotifyVO.getDate());
            //帖子内容
            data.put("thing1", pushNotifyVO.getBeContent());

            pushMsgDTO = new PushMsgDTO(WxConst.comment_template_id, page, data);
        }
        return pushMsgDTO;
    }
}
