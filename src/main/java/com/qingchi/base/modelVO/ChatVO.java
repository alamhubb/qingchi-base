package com.qingchi.base.modelVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.ChatType;
import com.qingchi.base.constant.CommonConst;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.constant.LoadMoreType;
import com.qingchi.base.constant.status.ChatStatus;
import com.qingchi.base.constant.status.ChatUserStatus;
import com.qingchi.base.constant.status.MessageStatus;
import com.qingchi.base.model.chat.ChatDO;
import com.qingchi.base.model.chat.ChatUserDO;
import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.model.chat.MessageReceiveDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.repository.chat.ChatRepository;
import com.qingchi.base.repository.chat.MessageReceiveRepository;
import com.qingchi.base.repository.chat.MessageRepository;
import com.qingchi.base.repository.follow.FollowRepository;
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
//chatdo+chatUserdo
public class ChatVO {
    private static MessageReceiveRepository messageReceiveRepository;

    private static MessageRepository messageRepository;

    private static ChatRepository chatRepository;

    private static FollowRepository followRepository;

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

    @Resource
    public void setFollowRepository(FollowRepository followRepository) {
        ChatVO.followRepository = followRepository;
    }

    private Long id;
    private String nickname;
    //暂时未用，未来有用的，参考微信私聊右上角，会从那里可以进入用户的详情
    private Integer receiveUserId;
    private String avatar;
    private String type;
    //为chatUser的status
    private String status;
    private Long updateTime;
    private List<MessageVO> messages;
    private Integer unreadNum;
    private Integer topType;
    private Integer topLevel;
    private Boolean topFlag;
    private Boolean vipFlag;
    private String loadMore;
    private String lastContent;

    //只有当对方未关注你，且还不是你的好友，才需要使用这个字段判断。
    //需要支付开启会话
    //只有为私聊，待开启，未关注的时候才需要为true
    private Boolean needPayOpen;

    public ChatVO() {
    }

    //chat

    public ChatVO(ChatDO chatDO) {
        this.id = chatDO.getId();
        //根据类型区分不同nick和ava
        this.type = chatDO.getType();
        //如果群聊则直接使用
        if (ChatType.systemChats.contains(this.type)) {
            this.nickname = chatDO.getNickname();
            this.avatar = chatDO.getAvatar();
        }
        //如果没有chatUser为不置顶， 未登录时不置顶
        this.topFlag = false;
        this.status = chatDO.getStatus();
        //chat的最后一条消息时间大家都一样，把最后一条删除也是最后一条的时间
        this.updateTime = chatDO.getUpdateTime().getTime();
        this.topType = chatDO.getTopLevel();
        this.topLevel = chatDO.getTopLevel();
        this.unreadNum = 0;
        this.messages = new ArrayList<>();
        this.loadMore = LoadMoreType.noMore;
        this.needPayOpen = false;
        this.lastContent = "会话已开启";
    }

    //初始查询的时候为99
    //用户未登录的情况，和群聊的情况会触发这里
    public ChatVO(ChatDO chatDO, Integer userId) {
        this(chatDO);
        //查询用户这个chatUser下的消息
        //已经确认过chat为可用的
        List<MessageDO> messageDOS = messageRepository.findTop31ByChatIdAndStatusAndIdNotInOrderByIdDesc(chatDO.getId(), ChatStatus.enable, CommonConst.emptyLongIds);
        if (messageDOS.size() > 30) {
            messageDOS.subList(1, 31);
            this.loadMore = LoadMoreType.more;
        }
        this.messages = MessageVO.messageDOToVOS(messageDOS, userId);
        //最后一个的content
        if (this.messages.size() > 0) {
            this.lastContent = this.messages.get(this.messages.size() - 1).getContent();
        }
    }

    //初始查询的时候为99
    /*public ChatVO(ChatDO chatDO, Integer userId) {
        this(chatDO);
        //查询用户这个chatUser下的消息
        //已经确认过chat为可用的
//        List<MessageDO> messageDOS = messageRepository.findTop30ByChatAndChatStatusAndStatusInOrderByCreateTimeDescIdDesc(chatDO, CommonStatus.normal, CommonStatus.otherCanSeeContentStatus);
        List<MessageDO> messageDOS = new ArrayList<>();
        this.messages = MessageVO.messageDOToVOS(messageDOS, userId);
    }*/


    //chatuser
    //推消息时，查列表时  的基础
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
            //不为系统群聊才有记录了未读数量，才有未读数量
            this.unreadNum = chatUserDO.getUnreadNum();
        }
        //chat的最后一条消息时间大家都一样，把最后一条删除也是最后一条的时间
        this.topFlag = chatUserDO.getTopFlag();
        this.updateTime = chatUserDO.getUpdateTime().getTime();
        this.status = chatUserDO.getStatus();
        //只有为待开启才判断是否需要支付开启
        if (this.status.equals(ChatUserStatus.waitOpen)) {
            this.lastContent = "会话待开启";
            //查询对方是否关注了自己，只有未关注的情况，才能支付
            Integer followCount = followRepository.countByUserIdAndBeUserIdAndStatus(this.receiveUserId, chatUserDO.getUserId(), CommonStatus.enable);
            if (followCount < 1) {
                //只在这一个地方判断，只有私聊的时候，且只有私聊的这里才会触发
                this.needPayOpen = true;
            }
        }
    }

    //3个地方使用，开启会话时，如果是一个已关闭的则获取之前的，所以需要获取列表
    //初始查询列表时需要列表，
    // 还有查看用户详情页面,查看时有时候不为已开启，所以需要判断
    public ChatVO(ChatDO chatDO, ChatUserDO chatUserDO, boolean queryMsgFlag) {
        this(chatDO, chatUserDO);
        //系统群聊读取message表
        //查询用户这个chatUser下的消息
        List<MessageReceiveDO> messageReceiveDOS = messageReceiveRepository.findTop31ByChatUserIdAndChatUserStatusAndStatusAndMessageIdNotInOrderByIdDesc(chatUserDO.getId(), ChatUserStatus.enable, MessageStatus.enable, CommonConst.emptyLongIds);
        if (messageReceiveDOS.size() > 30) {
            messageReceiveDOS.subList(1, 31);
            this.loadMore = LoadMoreType.more;
        }
        //        List<MessageReceiveDO> messageReceiveDOS = new ArrayList<>();
        this.messages = MessageVO.messageReceiveDOToVOS(messageReceiveDOS);
        //最后一个的content
        if (this.messages.size() > 0) {
            this.lastContent = this.messages.get(this.messages.size() - 1).getContent();
        }
    }


    public static List<ChatVO> chatUserDOToVOS(List<ChatUserDO> chatUsers) {
        //查询的时候chat列表展示不为当前用户的
        return chatUsers.stream().map((ChatUserDO chatUserDO) -> {
            ChatDO chat = chatUserDO.getChat();
            if (chat.getType().equals(ChatType.system_group)) {
                return new ChatVO(chat, chatUserDO.getUserId());
            } else {
                return new ChatVO(chat, chatUserDO, true);
            }
        }).collect(Collectors.toList());
    }

    //用户未登陆时
    public static List<ChatVO> chatDOToVOS(List<ChatDO> chats) {
        Integer userId = null;
        //查询的时候chat列表展示不为当前用户的
        return chats.stream().map((ChatDO chatDO) -> new ChatVO(chatDO, userId)).collect(Collectors.toList());
    }

    /*public static List<ChatVO> chatDOToVOS(List<ChatDO> chats, Integer userId) {
        //查询的时候chat列表展示不为当前用户的
        return chats.stream().map((ChatDO chatDO) -> new ChatVO(chatDO, userId)).collect(Collectors.toList());
    }*/


    //推送部分

    //给用户推送消息
    public ChatVO(ChatDO chat, MessageDO messageDO) {
        this(chat);
        //没user ，没记录未读数量，所以设置为1
        this.unreadNum = 1;
        this.messages = Collections.singletonList(new MessageVO(messageDO, false));
        this.lastContent = this.messages.get(0).getContent();
    }

    //推送单个消息的chat，推送单个消息
    public ChatVO(ChatDO chat, ChatUserDO chatUser, MessageReceiveDO messageReceiveDO) {
        this(chat, chatUser);
        this.messages = Collections.singletonList(new MessageVO(messageReceiveDO));
        this.lastContent = this.messages.get(0).getContent();
        //todo 不能推送所有未读的，因为有些未读的可能已经推送过了，但用户没看而已，再推送就会重复，解决这个问题需要标识哪些是已经推送过了，websocket中记录，目前前台通过重连充重新查询chats解决
//        List<MessageReceiveDO> messageReceiveDOS = messageReceiveRepository.findByChatUserAndMessageStatusAndReceiveUserAndStatusAndIsReadFalseOrderByCreateTimeDescIdDesc(messageReceiveDO.getChatUser(), CommonStatus.enable, messageReceiveDO.getReceiveUser(), CommonStatus.enable);
//        this.messages = MessageVO.messageDOToVOS(messageReceiveDOS);
    }

}
