package com.qingchi.base.repository.log;

import com.qingchi.base.model.system.OperateLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperateLogRepository extends JpaRepository<OperateLogDO, Long> {
}

