package com.qingchi.base.common;

import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.constant.status.BaseStatus;
import com.qingchi.base.model.system.AppConfigDO;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.constant.ConfigValueType;
import com.qingchi.base.repository.config.AppConfigRepository;
import com.qingchi.base.utils.QingLogger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2020-03-20 20:42
 */
@Service
public class ConfigMapRefreshService {
    @Resource
    private AppConfigRepository appConfigRepository;

    public void refreshConfigMap() {
        List<AppConfigDO> appConfigDOS = appConfigRepository.findAllByStatusOrderByCreateTimeDesc(BaseStatus.enable);
        for (AppConfigDO appConfigDO : appConfigDOS) {
            if (ConfigValueType.stringType.equals(appConfigDO.getValueType())) {
                AppConfigConst.appConfigMap.put(appConfigDO.getConfigKey(), appConfigDO.getStringValue());
            } else if (ConfigValueType.number.equals(appConfigDO.getValueType())) {
                AppConfigConst.appConfigMap.put(appConfigDO.getConfigKey(), appConfigDO.getNumberValue());
            } else if (ConfigValueType.booleanType.equals(appConfigDO.getValueType())) {
                AppConfigConst.appConfigMap.put(appConfigDO.getConfigKey(), appConfigDO.getBooleanValue());
            }
        }
        Integer talkAdInterval = (Integer) AppConfigConst.appConfigMap.get(AppConfigConst.talkShowAdIntervalKey);
        Integer talkShowAdCount = (Integer) AppConfigConst.appConfigMap.get(AppConfigConst.talkShowAdCountKey);
        if (talkAdInterval < 6) {
            QingLogger.logger.error("广告展示频率不能低于6");
        } else if (talkAdInterval > 30) {
            QingLogger.logger.warn("广告展示频率不建议大于30，没有意义");
        }
        List<Integer> showAdList = new ArrayList<>();
        showAdList.add(talkAdInterval - 1);
        for (int i = 0; i < talkShowAdCount - 1; i++) {
            int a = talkAdInterval * (i + 2) - 1;
            if (i < 4) {
                a = a + (((i + 1) * i) / 2) * talkAdInterval / 2;
            } else {
                a = a + (4 * (i - 1) - 2) * talkAdInterval / 2;
            }
            showAdList.add(a);
        }
        AppConfigConst.appConfigMap.put(AppConfigConst.talkShowAdIndexListKey, showAdList);
        QingLogger.logger.info("系统配置表数据：{}", AppConfigConst.appConfigMap);
    }
}
