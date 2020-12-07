package com.qingchi.base.modelVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.ChatType;
import com.qingchi.base.model.chat.ChatDO;
import com.qingchi.base.model.chat.ChatUserDO;
import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.model.chat.MessageReceiveDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.repository.chat.ChatRepository;
import com.qingchi.base.repository.chat.MessageReceiveRepository;
import com.qingchi.base.repository.chat.MessageRepository;
import com.qingchi.base.utils.UserUtils;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qinkaiyuan
 * @date 2018-11-18 20:48
 */
@Component
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChatVO {
    private static MessageReceiveRepository messageReceiveRepository;

    private static MessageRepository messageRepository;

    private static ChatRepository chatRepository;

    @Resource
    public void setMessageReceiveRepository(MessageReceiveRepository messageReceiveRepository) {
        ChatVO.messageReceiveRepository = messageReceiveRepository;
    }

    @Resource
    public void setMessageRepository(MessageRepository messageRepository) {
        ChatVO.messageRepository = messageRepository;
    }

    @Resource
    public void setChatRepository(ChatRepository chatRepository) {
        ChatVO.chatRepository = chatRepository;
    }

    private Long id;
    private String nickname;
    private Integer receiveUserId;
    private String avatar;
    private String type;
    private String status;
    private Date updateTime;
    private List<MessageVO> messages;
    private Integer unreadNum;
    private Integer topType;
    private Integer topLevel;
    private Boolean topFlag;
    private String lastContent;
    private Boolean vipFlag;

    //只有当对方未关注你，且还不是你的好友，才需要使用这个字段判断。
    //需要支付开启会话
    private Boolean needPayOpen;

    public ChatVO() {
    }

    public ChatVO(ChatDO chatDO) {
        this.id = chatDO.getId();
        //根据类型区分不同nick和ava
        this.type = chatDO.getType();
        //如果群聊则直接使用
        if (ChatType.systemChats.contains(this.type)) {
            this.nickname = chatDO.getNickname();
            this.avatar = chatDO.getAvatar();
        }
        this.topFlag = chatDO.getTopFlag();
        //chat的最后一条消息时间大家都一样，把最后一条删除也是最后一条的时间
        this.updateTime = chatDO.getUpdateTime();
        this.topType = chatDO.getTopLevel();
        this.topLevel = chatDO.getTopLevel();
        this.lastContent = chatDO.getLastContent();
        this.unreadNum = 0;
        this.messages = new ArrayList<>();
        this.needPayOpen = true;
    }

    //初始查询的时候为99
    public ChatVO(ChatDO chatDO, boolean queryMsgFlag) {
        this(chatDO);
        //查询用户这个chatUser下的消息
        //已经确认过chat为可用的
//        List<MessageDO> messageDOS = messageRepository.findTop30ByChatAndChatStatusAndStatusInOrderByCreateTimeDescIdDesc(chatDO, CommonStatus.normal, CommonStatus.otherCanSeeContentStatus);
        List<MessageDO> messageDOS = new ArrayList<>();
        this.messages = MessageVO.messageDOToVOS(messageDOS);
    }

    //初始查询的时候为99
    public ChatVO(ChatDO chatDO, Integer userId) {
        this(chatDO);
        //查询用户这个chatUser下的消息
        //已经确认过chat为可用的
//        List<MessageDO> messageDOS = messageRepository.findTop30ByChatAndChatStatusAndStatusInOrderByCreateTimeDescIdDesc(chatDO, CommonStatus.normal, CommonStatus.otherCanSeeContentStatus);
        List<MessageDO> messageDOS = new ArrayList<>();
        this.messages = MessageVO.messageDOToVOS(messageDOS, userId);
    }

    public ChatVO(ChatDO chat, ChatUserDO chatUserDO) {
        this(chat);

        //暂时不支持删除系统群聊
        //不使用chatUserDO的id是因为 推送message时，需要赋值chatVoId，此时获取chatUserDO的id比较麻烦。每次推送都要读一次数据库，不如在操作chatUser时读取一次好
        //可以使用了是因为，解决了notifyvo获取chatUserId的问题
        //不能使用，因为发送消息时，不知道这个chatId是chatUser还是chat，统一按chat处理
//        this.id = chatUserDO.getId();
        //根据类型区分不同nick和ava
        //如果群聊则直接使用
        if (!ChatType.systemChats.contains(this.type)) {
            UserDO receiveUser = UserUtils.get(chatUserDO.getReceiveUserId());
            this.nickname = receiveUser.getNickname();
            this.avatar = receiveUser.getAvatar();
            this.vipFlag = receiveUser.getVipFlag();
            this.receiveUserId = receiveUser.getId();
        }
        //chat的最后一条消息时间大家都一样，把最后一条删除也是最后一条的时间
        this.topFlag = chatUserDO.getTopFlag();
        this.unreadNum = chatUserDO.getUnreadNum();
        this.updateTime = chatUserDO.getUpdateTime();
        this.lastContent = chatUserDO.getLastContent();
        this.status = chatUserDO.getStatus();
    }

    public ChatVO(ChatDO chatDO, ChatUserDO chatUserDO, boolean queryMsgFlag) {
        this(chatDO, chatUserDO);
        //查询用户这个chatUser下的消息
//        List<MessageReceiveDO> messageReceiveDOS = messageReceiveRepository.findTop30ByChatUserIdAndMessageStatusInAndStatusOrderByCreateTimeDescIdDesc(chatUserDO.getId(), CommonStatus.otherCanSeeContentStatus, CommonStatus.normal);
        List<MessageReceiveDO> messageReceiveDOS = new ArrayList<>();
        this.messages = MessageVO.messageReceiveDOToVOS(messageReceiveDOS);
    }

    public ChatVO(ChatDO chat, MessageDO messageDO) {
        this(chat);
        this.unreadNum = 1;
        this.messages = Collections.singletonList(new MessageVO(messageDO, false));
    }

    public ChatVO(MessageReceiveDO messageReceiveDO, ChatUserDO chatUser, ChatDO chat) {
        this(chat, chatUser);
        Optional<MessageDO> optionalMessageDO = messageRepository.findById(messageReceiveDO.getMessageId());
        this.messages = Collections.singletonList(new MessageVO(messageReceiveDO, optionalMessageDO.get()));
        //todo 不能推送所有未读的，因为有些未读的可能已经推送过了，但用户没看而已，再推送就会重复，解决这个问题需要标识哪些是已经推送过了，websocket中记录，目前前台通过重连充重新查询chats解决
//        List<MessageReceiveDO> messageReceiveDOS = messageReceiveRepository.findByChatUserAndMessageStatusAndReceiveUserAndStatusAndIsReadFalseOrderByCreateTimeDescIdDesc(messageReceiveDO.getChatUser(), CommonStatus.enable, messageReceiveDO.getReceiveUser(), CommonStatus.enable);
//        this.messages = MessageVO.messageDOToVOS(messageReceiveDOS);
    }

    public static List<ChatVO> chatUserDOToVOS(List<ChatUserDO> chatUsers) {
        //查询的时候chat列表展示不为当前用户的
        return chatUsers.stream().map((ChatUserDO chatUserDO) -> {
            Optional<ChatDO> chatDOOptional = chatRepository.findById(chatUserDO.getChatId());
            return new ChatVO(chatDOOptional.get(), chatUserDO, true);
        }).collect(Collectors.toList());
    }

    public static List<ChatVO> chatDOToVOS(List<ChatDO> chats) {
        //查询的时候chat列表展示不为当前用户的
        return chats.stream().map((ChatDO chatDO) -> new ChatVO(chatDO, true)).collect(Collectors.toList());
    }

    public static List<ChatVO> chatDOToVOS(List<ChatDO> chats, Integer userId) {
        //查询的时候chat列表展示不为当前用户的
        return chats.stream().map((ChatDO chatDO) -> new ChatVO(chatDO, userId)).collect(Collectors.toList());
    }
}
