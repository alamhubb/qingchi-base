package com.qingchi.base.repository.chat;

import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.model.chat.MessageReceiveDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TODO〈一句话功能简述〉
 * TODO〈功能详细描述〉
 *
 * @author qinkaiyuan
 * @since TODO[起始版本号]
 */
public interface MessageReceiveRepository extends JpaRepository<MessageReceiveDO, Long> {

    //将ids的未读变为已读，查询出来更改
//    List<MessageReceiveDO> findByChatUserIdAndMessageStatusInAndStatusAndIsReadFalseAndIdInOrderByCreateTimeDescIdDesc(Integer chatUserId, List<String> msgStatus, String status, List<Long> ids);
    List<MessageReceiveDO> findByChatUserIdAndStatusAndIsReadFalse(Long chatUserId, String status);


    //查询消息列表，根据chatUserId、msgReceiveStatus、msgIds 按照msgReceiveStatus 倒序排序
    List<MessageReceiveDO> findTop31ByChatUserIdAndStatusAndMessageIdNotInOrderByIdDesc(Long chatUserId, String msgReceiveStatus, List<Long> ids);

    List<MessageReceiveDO> findTop30ByChatUserIdAndStatusAndMessageIdNotInOrderByIdDesc(Long chatUserId, String msgReceiveStatus, List<Long> ids);
}