package com.qingchi.base.utils;

import com.qingchi.base.constant.status.UserStatus;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.repository.user.TokenRepository;
import com.qingchi.base.store.UserStore;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class UserUtils {

    private static TokenRepository tokenRepository;

    private static UserStore userStore;

    private static Integer systemId;

    @Value("${config.systemId}")
    public void setSystemId(Integer systemId) {
        UserUtils.systemId = systemId;
    }

    @Resource
    public void setTokenRepository(TokenRepository tokenRepository) {
        UserUtils.tokenRepository = tokenRepository;
    }

    @Resource
    public void setUserStore(UserStore userStore) {
        UserUtils.userStore = userStore;
    }

    public static UserDO get(Integer userId) {
        return userStore.getUserById(userId);
    }

    public static Integer getUserId() {
        Integer userId = null;
        UserDO user = UserUtils.getUserByDBAllStatus();
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }

    public static Optional<UserDO> getUserOpt(Integer userId) {
        return userStore.getUserOpt(userId);
    }

    //从缓存或redis中取user
    public static UserDO getUserByDBNoViolation() {
        String token = TokenUtils.getToken();
        return UserUtils.getUserByDBNoViolation(token);
    }

    public static UserDO getUserByDBAllStatus() {
        String token = TokenUtils.getToken();
        return UserUtils.getUserByDBAllStatus(token);
    }

    public static UserDO getUserByDBAllStatus(String token) {
        if (token != null) {
            if (TokenUtils.isCorrect(token)) {
                String userId = TokenUtils.getIdByToken(token);
                if (StringUtils.isNotEmpty(userId)) {
                    Integer userIdInt = Integer.parseInt(userId);
                    //todo 这里需要校验有效期吧
                    String tokenCode = tokenRepository.findFirstOneTokenCodeByUserId(userIdInt);
                    //token与用户token相等
                    if (token.equals(tokenCode)) {
                        Optional<UserDO> userDOOptional = UserUtils.getUserOpt(userIdInt);
                        if (userDOOptional.isPresent()) {
                            return userDOOptional.get();
                        }
                    }
                }
            }
//        暂时注释掉错误日志，要考虑token为空的情况 iscorrect方法中的那几种情况
//        ErrorLogUtils.save(new ErrorLogDO(null, "有人使用错误的token来破解系统", token, ErrorLevel.urgency));
        }
        return null;
    }

    //两个地方用到 websocket和request分别不同的地方存的token
    public static UserDO getUserByDBNoViolation(String token) {
        UserDO user = getUserByDBAllStatus(token);
        if (user != null) {
            if (!user.getStatus().equals(UserStatus.violation)) {
                return user;
            }
        }
        return null;
    }

    public static UserDO getSystemUser() {
        UserDO user = new UserDO();
        //系统管理员,告知别人谁发的通知
        user.setId(systemId);
        return user;
    }

    public static Integer getSystemId() {
        return systemId;
    }
}
