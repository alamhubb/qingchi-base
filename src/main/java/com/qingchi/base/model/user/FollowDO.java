package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "follow", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "beUserId"})
})
public class FollowDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主用户
     */
    private Integer userId;

    /**
     * 被关联用户
     */
    private Integer beUserId;

    private String status;

    private Date createTime;
    private Date updateTime;
}
