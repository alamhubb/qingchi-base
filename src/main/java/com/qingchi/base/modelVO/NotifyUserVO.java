package com.qingchi.base.modelVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.model.user.UserDO;
import lombok.Data;

/**
 * @author qinkaiyuan 查询结果可以没有set和空构造，前台传值可以没有get
 * @date 2019-08-13 23:34
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NotifyUserVO {
    private Integer id;
    private String nickname;
    private String avatar;
    private Boolean vipFlag;
    public Boolean hasRead;

    public NotifyUserVO() {
    }

    public NotifyUserVO(UserDO user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.avatar = user.getAvatar();
        this.vipFlag = user.getVipFlag();
        this.hasRead = false;
    }
}
