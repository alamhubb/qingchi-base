package com.qingchi.base.repository.log;

import com.qingchi.base.model.monitoring.ErrorLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLogDO, Long> {

}
