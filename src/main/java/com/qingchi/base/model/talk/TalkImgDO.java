package com.qingchi.base.model.talk;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "talk_img")
@Data
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TalkImgDO implements Serializable {
    //此类为talk子类，只能包含基础数据类型
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer talkId;

    private String src;

    private Double aspectRatio;

    private Date createTime;
    //压缩率
    private Double quality;

    private Integer size;
}
