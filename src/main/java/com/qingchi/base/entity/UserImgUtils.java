package com.qingchi.base.entity;

import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.user.UserImgDO;
import com.qingchi.base.repository.user.UserImgRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserImgUtils {
    private static UserImgRepository userImgRepository;

    @Resource
    public void setUserImgRepository(UserImgRepository userImgRepository) {
        UserImgUtils.userImgRepository = userImgRepository;
    }

    public static List<UserImgDO> getImgs(Integer userId) {
        return userImgRepository.findTop3ByUserIdAndStatusInOrderByCreateTimeDesc(userId, CommonStatus.otherCanSeeContentStatus);
    }

    public static UserImgDO find(Integer imgId) {
        return userImgRepository.findById(imgId).get();
    }
}
