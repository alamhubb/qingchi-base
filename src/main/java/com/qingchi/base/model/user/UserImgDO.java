package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.model.BaseModelDO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user_img")
public class UserImgDO implements BaseModelDO, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String src;

    private Double aspectRatio;

    private Date createTime;

    private Date updateTime;

    private String status;

    private Integer reportNum;
    private String deleteReason;

    //压缩率
    private Double quality;
    //图片大小
    private Integer size;

    //是否已认证
    private Boolean isSelfAuth;

    private String content;

    private String violateType;
    private String reportContentType;

    @Override
    @JsonIgnore
    public Integer getDbId() {
        return this.getId();
    }
}
