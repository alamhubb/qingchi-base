package com.qingchi.base.model.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "operate_log", indexes = {@Index(columnList = "uri")})
public class OperateLogDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //邀请你的用户
    private Integer userId;
    private String ip;
    private Date createTime;
    private Date updateTime;
    private Date endTime;
    private String uri;
    private Long spendTime;
}
