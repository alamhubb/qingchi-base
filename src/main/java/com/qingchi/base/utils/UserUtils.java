package com.qingchi.base.utils;

import com.qingchi.base.constant.CommonStatus;
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

    public static Optional<UserDO> getUserOpt(Integer userId) {
        return userStore.getUserOpt(userId);
    }

    //从缓存或redis中取user
    public static UserDO getUserByDB() {
        String token = TokenUtils.getToken();
        return UserUtils.getUserByDB(token);
    }

    //两个地方用到 websocket和request分别不同的地方存的token
    public static UserDO getUserByDB(String token) {
        if (token != null) {
            if (TokenUtils.isCorrect(token)) {
                String userId = TokenUtils.getIdByToken(token);
                if (StringUtils.isNotEmpty(userId)) {
                    Integer userIdInt = Integer.parseInt(userId);
                    String tokenCode = tokenRepository.findFirstOneTokenCodeByUserId(userIdInt);
                    //token与用户token相等
                    if (token.equals(tokenCode)) {
                        Optional<UserDO> userDOOptional = UserUtils.getUserOpt(userIdInt);
                        if (userDOOptional.isPresent()) {
                            UserDO user = userDOOptional.get();
                            //不为违规被封禁
                            if (!user.getStatus().equals(CommonStatus.violation)) {
                                return user;
                            }
                        }
                    }
                }
            }
//        暂时注释掉错误日志，要考虑token为空的情况 iscorrect方法中的那几种情况
//        ErrorLogUtils.save(new ErrorLogDO(null, "有人使用错误的token来破解系统", token, ErrorLevel.urgency));
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
