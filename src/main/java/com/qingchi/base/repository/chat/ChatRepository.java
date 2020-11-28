package com.qingchi.base.repository.chat;

import com.qingchi.base.model.chat.ChatDO;
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
public interface ChatRepository extends JpaRepository<ChatDO, Long> {

    List<ChatDO> findByStatusAndTypeInOrderByTopFlagDescTopLevelAscUpdateTimeDesc(String status, List<String> types);

    //查询对应的chat
    Optional<ChatDO> findFirstByIdAndStatus(Long id, String status);

    //查询系统群聊
    Optional<ChatDO> findFirstOneByTypeAndStatusOrderByCreateTime(String type, String status);
}