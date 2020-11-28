package com.qingchi.base.repository.chat;

import com.qingchi.base.model.chat.ChatUserDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * TODO〈一句话功能简述〉
 * TODO〈功能详细描述〉
 *
 * @author qinkaiyuan
 * @since TODO[起始版本号]
 */
public interface ChatUserRepository extends JpaRepository<ChatUserDO, Long> {
    //根据chat状态，chatuser状态，置顶倒序，置顶等级升序，时间倒序
/*
    @Query("select ChatUserDO from ChatUserDO u,ChatDO where Chatu.chatId=ChatDO.id and ChatDO.type not in (:chatTypes) and ChatDO.status =:chatStatus and Chatu.userId=:userId and Chatu.status =:status")
    List<ChatUserDO> findByChatTypeNotInAndChatStatusAndUserIdAndStatusOrderByTopFlagDescChatTopLevelAscUpdateTimeDesc(List<String> chatTypes, String chatStatus, Integer userId, String status);
*/

    Optional<ChatUserDO> findFirstByChatIdAndUserIdAndStatus(Long chatId, Integer userId, String status);

    Optional<ChatUserDO> findFirstByUserIdAndReceiveUserId(Integer userId, Integer receiveUserId);


    List<ChatUserDO> findByChatIdAndStatus(Long chatId, String status);
}