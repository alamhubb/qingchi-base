package com.qingchi.base.repository.log;

import com.qingchi.base.model.user.UserLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLogDO, Long> {

}

