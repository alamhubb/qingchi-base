package com.qingchi.base.store;

import com.qingchi.base.model.chat.MessageDO;
import com.qingchi.base.repository.chat.MessageRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class MessageStoreUtils {
    public static MessageDO findById(Long tagId) {
        Optional<MessageDO> tagDOOptional = messageRepository.findById(tagId);
        return tagDOOptional.orElse(null);
    }

    private static MessageRepository messageRepository;

    @Resource
    public void setMessageRepository(MessageRepository messageRepository) {
        MessageStoreUtils.messageRepository = messageRepository;
    }
}