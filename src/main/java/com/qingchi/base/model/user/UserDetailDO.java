package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.model.system.DistrictDO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qinkaiyuan
 * @date 2019-10-31 13:31
 * 用户详细信息，只有查看用户详情时才需要的信息
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user_detail")
public class UserDetailDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 微信账户，用户可以关联微信账户，方便添加好友
     */
    private String wxAccount;
    private String qqAccount;
    private String wbAccount;
    private String contactAccount;
    //开放联系方式
    private Boolean openContact;
    //经验值。以后不再需要正义值和爱心值。
    private Integer experience;
    //他人向你发起消息需要支付贝壳数量
    private Integer beSponsorMsgShell;
    //邀请你的用户
    private Integer userId;

    private Integer talkQueryDistrictId;
    private Integer talkAddDistrictId;
    //用户当前是否启用了为附近功能 0初始，1使用，2不使用
    //暂时不使用这个字段
//    private Integer useNearby;

    private Date createTime;
    private Date updateTime;

    public UserDetailDO() {
    }

    public UserDetailDO(UserDO user) {
        this.setUserId(user.getId());
        DistrictDO districtDO = new DistrictDO();
        districtDO.setId(1);
        Date curDate = new Date();
        //设置为中国
        this.setTalkQueryDistrictId(districtDO.getId());
        this.setTalkAddDistrictId(districtDO.getId());
        this.setCreateTime(curDate);
        this.setUpdateTime(curDate);
        //经验值
        this.setExperience(0);
        //默认5贝壳
        this.setBeSponsorMsgShell(5);
        //设置为初始
        this.setWxAccount("");
        this.setQqAccount("");
        this.setWbAccount("");
        this.setContactAccount("");
        this.setOpenContact(true);
    }
}
