package com.qingchi.base.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qingchi.base.model.notify.NotifyDO;
import com.qingchi.base.model.notify.PushMessageDO;
import com.qingchi.base.repository.notify.PushMessageRepository;
import com.qingchi.base.platform.weixin.HttpResult;
import com.qingchi.base.utils.JsonUtils;
import com.qingchi.base.utils.QingLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinkaiyuan
 * @date 2020-03-21 21:45
 */
@Component
public class PushMessageUtils {
    private static PushMessageRepository pushMessageRepository;

    @Resource
    public void setPushMessageRepository(PushMessageRepository pushMessageRepository) {
        PushMessageUtils.pushMessageRepository = pushMessageRepository;
    }

    public static void savePushMsg(NotifyDO notify, PushMsgDTO pushMsgDTO, HttpResult result, String platform) {
        String pushMsgDTOString = null;
        String resultString = null;
        try {
            pushMsgDTOString = JsonUtils.objectMapper.writeValueAsString(pushMsgDTO);
            resultString = JsonUtils.objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            QingLogger.logger.error("发送消息，json格式化：{},{},{}", pushMsgDTO, result, e);
        }
        PushMessageDO pushMessageDO = new PushMessageDO(notify.getId(), pushMsgDTOString, resultString, platform);
        pushMessageRepository.save(pushMessageDO);
    }
}
