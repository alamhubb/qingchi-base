package com.qingchi.base.repository.chat;

import com.qingchi.base.model.chat.MessageReceiveDO;
import org.springframework.data.jpa.repository.JpaRepository;

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


}