package com.qingchi.base.model.talk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author qinkaiyuan
 * @date 2019-11-07 15:20
 */

@Entity
@Table(name = "talk_tag")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TalkTagDO implements Serializable {
    //此类为talk子类，只能包含基础数据类型
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer talkId;
    private Integer tagId;
}
