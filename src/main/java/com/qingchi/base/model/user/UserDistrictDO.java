package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qinkaiyuan
 * @date 2019-10-30 14:08
 * <p>
 * 记录用户最近使用的行政区
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user_district", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "districtId"})
})
public class UserDistrictDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer districtId;
    private String districtAdCode;
    private Date createTime;
    private Date updateTime;
    private String status;
    private Integer count;
}
