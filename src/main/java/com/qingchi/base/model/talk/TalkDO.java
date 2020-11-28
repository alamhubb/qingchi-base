package com.qingchi.base.model.talk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qingchi.base.model.BaseModelDO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "talk", indexes = {@Index(columnList = "updateTime")})  //为
@Data
public class TalkDO implements BaseModelDO, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //内容主体
    private String content;
    //未来可以修改，但要有记录，修改过就显示已修改，显示修改记录
    private Date createTime;
    //新增评论等会更新此时间
    private Date updateTime;

    /**
     * 评论数量
     */
    private Integer commentNum;
    /**
     * 抱抱次数
     */
    private Integer hugNum;
    /**
     * 举报次数
     */
    private Integer reportNum;

    /**
     * 状态 正常，违规，删除
     */
    private String status;

    private Integer userId;

    /**
     * 全局置顶标识，默认0，数越大级别越高
     */
    private Integer globalTop;

    /**
     * 删除原因
     */
    private String deleteReason;
    private String reportContentType;
    private String violateType;

    private String adCode;
    private String adName;
    private String cityName;
    //省
    private String provinceName;
    //区县
    private String districtName;

    /*
     *  经度 Longitude 简写Lng
     * 纬度范围-90~90，经度范围-180~180
     */
    private Double lon;
    /*
     * 纬度 Latitude 简写Lat
     */
    private Double lat;

    //设置 ：级联 保存/新建 操作 。新建 学校和学生 的时候，保存新建的学校那么新建的学生也同时被保存
    //无论何时，只要查询了talk就会查询talkimg，所以设为及时加载
   /* @Fetch(FetchMode.JOIN)
    //, fetch = FetchType.EAGER 因为设置了join所以不需要设置及时加载了，默认就是
    @OneToMany(mappedBy = "talkId", cascade = CascadeType.PERSIST)
    private List<TalkImgDO> imgs;


    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "talkId")
    private Set<TalkTagDO> tagIds;*/


    public TalkDO() {
    }

    public TalkDO(Integer id) {
        this.id = id;
    }

    @Override
    @JsonIgnore
    public Integer getDbId() {
        return this.getId();
    }
}
