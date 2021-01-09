package com.qingchi.base.modelVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.constant.status.BaseStatus;
import com.qingchi.base.model.user.FollowDO;
import com.qingchi.base.model.user.UserDO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * TODO〈一句话功能简述〉
 * TODO〈功能详细描述〉
 *
 * @author qinkaiyuan
 * @since TODO[起始版本号]
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FollowAddVO {
    /**
     * 被关注的用户id
     */
    @NotNull(message = "入参为空异常")
    private Integer beUserId;

    public static FollowDO toDO(UserDO user, UserDO beUser) {
        FollowDO followDO = new FollowDO();
        followDO.setCreateTime(new Date());
        //两个用户粉丝和关注各加1
        user.setFollowNum(user.getFollowNum() + 1);
        beUser.setFansNum(beUser.getFansNum() + 1);
        followDO.setUserId(user.getId());
        followDO.setBeUserId(beUser.getId());
        followDO.setStatus(BaseStatus.enable);
        return followDO;
    }

}
