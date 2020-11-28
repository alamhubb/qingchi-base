package com.qingchi.base.model.talk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hug", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "talkId"}),
        @UniqueConstraint(columnNames = {"userId", "commentId"})
})
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HugDO {
    //如果这个评论 有parent，就代表已经是一个子评论，就不用把他设置为parent而是用它的parentId，他是否有parent
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 赞的说说
     */
    private Integer talkId;

    /**
     * 赞的说说
     */
    private Integer commentId;

    /**
     * 哪个评论的
     */
    private Integer userId;
}
