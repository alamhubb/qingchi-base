package com.qingchi.base.model.openData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 登录相关，只有登录时才用得到的表
 * 自己表示字段，其他表示关联的表内字段
 */
@Data
@Entity
@Table(name = "content_union_id")
/*, uniqueConstraints = {
@UniqueConstraint(columnNames = {"contentUnionType", "developerId", "userId"})
}*/
public class ContentUnionIdDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String unionId;
    //内容类型
    private String contentUnionType;
    //开发者id
    private String developerId;
    //用户id，分配给哪个用户的
    private Integer userId;
    //内容的唯一id
    private Integer contentId;

    //启用，还是失效
    private String status;
    //创建时间
    private Date createTime;
    //有效周期，数量+timeUnitType，时间单位周期
    private String validTime;
    //失效时间
    private Date loseTime;

    //内容类型，id，关联商户，关联用户id
}