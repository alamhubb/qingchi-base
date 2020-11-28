package com.qingchi.base.config;

import com.qingchi.base.common.ConfigMapRefreshService;
import com.qingchi.base.common.ViolationKeywordsService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinkaiyuan
 * @date 2019-10-24 11:57
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Resource
    private ConfigMapRefreshService configMapRefreshService;
    @Resource
    private ViolationKeywordsService violationKeywordsService;

    @Override
    public void run(ApplicationArguments args) {
        configMapRefreshService.refreshConfigMap();
        violationKeywordsService.refreshKeywords();
    }
}
