package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qinkaiyuan
 * @date 2019-12-22 18:46
 * <p>
 * 记录自己的formid，因为只能给自己发送通知，所以需要获取到自己的formid
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user_formId")
public class UserFormIdDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private String formId;
    private String status;
    private Integer notifyId;
    private Date createTime;
    private Date updateTime;

    public UserFormIdDO() {
    }
}
