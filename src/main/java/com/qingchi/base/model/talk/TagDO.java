package com.qingchi.base.model.talk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author qinkaiyuan
 * @date 2019-11-07 15:20
 */

@Entity
@Table(name = "tag", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TagDO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer tagTypeId;

    private String name;

    private String status;
    private String avatar;

    //访问+发帖次数
    private Integer count;
    //本标签共有多少帖子
    private Integer talkCount;

    /**
     * 申请人
     */
    private Integer applyUserId;

    private Date createTime;

    private Date updateTime;

    private String description;

    //是否显示微信号,处对象，处微友类型显示微信账号
    private Boolean showWxAccount;

    //是否显示在主页，仅显示在话题下，比如炫富话题不适合上热门首页
    private Boolean showInHome;
}
