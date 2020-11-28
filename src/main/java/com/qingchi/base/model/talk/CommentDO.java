package com.qingchi.base.model.talk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.model.BaseModelDO;
import com.qingchi.base.model.notify.NotifyDO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment", indexes = {@Index(columnList = "updateTime")})
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CommentDO implements BaseModelDO, Serializable {
    //如果这个评论 有parent，就代表已经是一个子评论，就不用把他设置为parent而是用它的parentId，他是否有parent
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer no;
    /**
     * 评论时间
     */
    private Date createTime;
    /**
     * 更新时间，被赞，被评论，被回复都会更新
     */
    private Date updateTime;
    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论的说说
     */
    private Integer talkId;

    /**
     * 2级评论
     */
    private Integer parentCommentId;

    /**
     * 回复的哪个评论
     */
    private Integer replyCommentId;

    /**
     * 哪个评论的
     */
    private Integer userId;

    /**
     * 需要发送的通知
     */
    @Transient
    private List<NotifyDO> notifies;

    /**
     * 评论数量，子评论数量
     */
    private Integer childCommentNum;
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

    /**
     * 删除原因
     */
    private String deleteReason;
    private String violateType;
    private String reportContentType;

    @Override
    @JsonIgnore
    public Integer getDbId() {
        return this.getId();
    }
}
