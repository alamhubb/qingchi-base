package com.qingchi.base.entity;

import com.qingchi.base.model.monitoring.ErrorLogDO;
import com.qingchi.base.repository.log.ErrorLogRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ErrorLogUtils {
    private static ErrorLogRepository errorLogRepository;

    @Resource
    public void setErrorLogRepository(ErrorLogRepository errorLogRepository) {
        ErrorLogUtils.errorLogRepository = errorLogRepository;
    }


    public static void save(ErrorLogDO errorLogDO) {
        errorLogRepository.save(errorLogDO);
    }
}
