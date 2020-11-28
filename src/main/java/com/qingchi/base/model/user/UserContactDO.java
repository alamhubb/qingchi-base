package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 获取用户联系方式的记录，记录谁获取的谁
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "user_contact", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "beUserId"}))
@Entity
public class UserContactDO implements Serializable {
    //必有
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer beUserId;

    private Date createTime;
    private Date updateTime;

    //用户状态，暂未使用正常，封禁
    private String status;

    public UserContactDO() {
        Date curDate = new Date();
        this.createTime = curDate;
        this.updateTime = curDate;
        this.status = CommonStatus.normal;
    }

    public UserContactDO(Integer userId) {
        this();
        this.userId = userId;
    }

    public UserContactDO(Integer userId, Integer beUserId) {
        this();
        this.userId = userId;
        this.beUserId = beUserId;
    }
}