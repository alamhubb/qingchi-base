package com.qingchi.base.service;

import com.qingchi.base.common.ResultVO;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.user.FollowDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.modelVO.FollowAddVO;
import com.qingchi.base.repository.follow.FollowRepository;
import com.qingchi.base.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2020-04-10 3:15
 */
@Service
public class FollowService {
    @Resource
    private FollowRepository followRepository;
    @Resource
    private UserRepository userRepository;

    public ResultVO<?> addFlow(UserDO user, UserDO beUser) {
        Optional<FollowDO> followDOOptional = followRepository.findFirstByUserIdAndBeUserIdOrderByIdDesc(user.getId(), beUser.getId());
        FollowDO followDO;
        //如果已经关注了
        if (followDOOptional.isPresent()) {
            followDO = followDOOptional.get();
            //已经关注
            if (CommonStatus.enable.equals(followDO.getStatus())) {
                return new ResultVO<>("已经关注过此用户了");
            } else {
                followDO.setStatus(CommonStatus.enable);
                user.setFollowNum(user.getFollowNum() + 1);
                beUser.setFansNum(beUser.getFansNum() + 1);
            }
        } else {
            followDO = FollowAddVO.toDO(user, beUser);
        }
        followDO.setUpdateTime(new Date());
        followRepository.save(followDO);
        return new ResultVO<>();
    }
}
