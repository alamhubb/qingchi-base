package com.qingchi.base.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 登录相关，只有登录时才用得到的表
 * 自己表示字段，其他表示关联的表内字段
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId"}),
        @UniqueConstraint(columnNames = {"qqMpOpenId"}),
        @UniqueConstraint(columnNames = {"qqAppOpenId"}),
        @UniqueConstraint(columnNames = {"qqUnionId"}),
        @UniqueConstraint(columnNames = {"wxMpOpenId"}),
        @UniqueConstraint(columnNames = {"wxAppOpenId"}),
        @UniqueConstraint(columnNames = {"wxUnionId"})
})
public class AccountDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phoneNum;
    //使用哪个平台注册的
    private String platform;
    private String provider;
    //邀请你的用户
    private Integer userId;
    private String sessionKey;
    private Date createTime;
    private Date updateTime;
    //登陆方式
    //qq小程序
    private String qqMpOpenId;
    //qq-app登陆
    private String qqAppOpenId;
    //qq统一Id
    private String qqUnionId;
    //wx小程序
    private String wxMpOpenId;
    //wx-app登陆
    private String wxAppOpenId;
    //wx统一Id
    private String wxUnionId;
    //app推送使用
    private String clientid;

    public AccountDO() {
    }

    public AccountDO(String platform, String provider, Integer userId) {
        this.platform = platform;
        this.provider = provider;
        this.userId = userId;
        Date curDate = new Date();
        this.createTime = curDate;
        this.updateTime = curDate;
    }
}