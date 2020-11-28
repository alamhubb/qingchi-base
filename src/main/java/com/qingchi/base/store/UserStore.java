package com.qingchi.base.store;

import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.model.monitoring.ErrorLogDO;
import com.qingchi.base.repository.user.UserRepository;
import com.qingchi.base.entity.ErrorLogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class UserStore {
    public static final Logger logger = LoggerFactory.getLogger(UserStore.class);

    @Resource
    private UserRepository userRepository;

    // 封装判空逻辑
    public UserDO getUserById(Integer userId) {
        Optional<UserDO> optionalUserDO = this.getUserOpt(userId);
        return optionalUserDO.orElse(null);
    }

    public Optional<UserDO> getUserOpt(Integer userId) {
        Optional<UserDO> optionalUserDO = userRepository.findById(userId);
        if (!optionalUserDO.isPresent()) {
            logger.error("不应该出现此种状况，Optional根据UserId查不到User");
            ErrorLogUtils.save(new ErrorLogDO(userId, "Optional根据UserId查不到User", userId));
        }
        return optionalUserDO;
    }
}
