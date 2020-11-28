package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user_modify_history")
public class UserHistoryDO {
    /**
     * 记录用户可被修改的内容
     * <p>
     * 用户信息一天不能修改20次以上
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    private String phoneNum;
    private String gender;
    private String location;
    private Integer age;
    private String birthday;
    private Date createTime;
    private Integer userImgId;
    /**
     * 修改资料，上传你照片，删除照片
     */
    private String userModifyType;
    private Integer versionNo;
}