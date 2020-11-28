package com.qingchi.base.model.match;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qinkaiyuan
 * @date 2019-06-02 16:07
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "match_request", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "receiveUserId"}))
public class MatchRequestDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer receiveUserId;

    private String status;

    private Date createTime = new Date();

    private Date updateTime;
}
