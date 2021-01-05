package com.qingchi.base.service;

import com.qingchi.base.common.ResultVO;
import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.constant.ErrorMsg;
import com.qingchi.base.model.system.IllegalWordDO;
import com.qingchi.base.platform.qq.QQConst;
import com.qingchi.base.repository.keywords.IllegalWordRepository;
import com.qingchi.base.utils.QingLogger;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IllegalWordService {

    @Resource
    IllegalWordRepository illegalWordRepository;

    public ResultVO checkHasIllegals(String content) {
        List<IllegalWordDO> illegals = AppConfigConst.getIllegals();

        List<IllegalWordDO> triggerWords = new ArrayList<>();

        StringBuilder words = new StringBuilder();

        //如果有触发违规关键词
        for (IllegalWordDO illegal : illegals) {
            String wordText = illegal.getWord();
            //关键词不为空，且包含
            if (StringUtils.isNotEmpty(wordText) && StringUtils.containsIgnoreCase(content, wordText)) {
                illegal.setTriggerCount(illegal.getTriggerCount() + 1);
                illegal.setUpdateTime(new Date());
                words.append(illegal.getWord()).append("，");
                triggerWords.add(illegal);
            }
        }
        //保存触发的，更新他们的次数
        if (triggerWords.size() > 0) {
            illegalWordRepository.saveAll(triggerWords);
            String wordMsg = words.substring(0, words.length() - 1);
            String errorMsg = MessageFormat.format(ErrorMsg.illegalWordMsg, wordMsg);
            return new ResultVO(errorMsg);
        }
        return new ResultVO();
    }
}
