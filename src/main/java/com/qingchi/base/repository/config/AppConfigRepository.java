package com.qingchi.base.repository.config;

import com.qingchi.base.model.system.AppConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppConfigRepository extends JpaRepository<AppConfigDO, Integer> {
    List<AppConfigDO> findAllByStatusOrderByCreateTimeDesc(String status);
}

