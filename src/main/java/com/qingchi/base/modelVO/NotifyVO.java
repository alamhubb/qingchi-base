package com.qingchi.base.modelVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qingchi.base.constant.NotifyType;
import com.qingchi.base.model.chat.ChatDO;
import com.qingchi.base.model.chat.ChatUserDO;
import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.model.chat.MessageReceiveDO;
import com.qingchi.base.model.notify.NotifyDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.utils.JsonUtils;
import lombok.Data;
import org.springframework.web.socket.TextMessage;

/**
 * @author qinkaiyuan
 * @date 2019-06-12 22:21
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NotifyVO {
    //加入一列作为外键
    private ChatVO chat;

    private NotifyUserVO user;
    private Integer receiveUserId;

    private String type;

    public NotifyVO() {
    }

    public NotifyVO(UserDO user) {
        //此处没有给content赋值是因为推送，不需要显示，推送后点击未读列表查询就行
        this.user = new NotifyUserVO(user);
        this.type = NotifyType.comment;
    }

    public NotifyVO(NotifyDO notify, UserDO user, MessageReceiveDO messageReceive, ChatUserDO chatUser, ChatDO chatDO) {
        this(user);
        this.chat = new ChatVO(chatDO, chatUser, messageReceive);
        this.receiveUserId = notify.getReceiveUserId();
        this.type = notify.getType();
        //此处没有给content赋值是因为推送，不需要显示，推送后点击未读列表查询就行
    }

    public NotifyVO(ChatDO chat, UserDO user, MessageDO message) {
        this(user);
        this.chat = new ChatVO(chat, message);
        this.type = NotifyType.message;
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
