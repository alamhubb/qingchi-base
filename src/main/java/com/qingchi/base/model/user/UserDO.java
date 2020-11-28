package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phoneNum"})
})
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserDO implements Serializable {
    //必有
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    private String avatar;
    private String gender;
    private Integer age;
    //是否进行了本人照片认证
    //会在头像旁边显示
    private Boolean isSelfAuth;
    //是否为vip，
    private Boolean vipFlag;
    //已开vip多少个月
    private Boolean yearVipFlag;
    //经验等级
    private Integer gradeLevel;
    private Integer wealthLevel;

    //用户详情页展示
    private String location;
    private Integer fansNum;

    //设置 ：级联 保存/新建 操作 。新建 学校和学生 的时候，保存新建的学校那么新建的学生也同时被保存
    //查询 图片状态为已审核过和可用的，未认证时，正常图片为可用，认证后正常图片为已审核过
    //, fetch = FetchType.EAGER 因为设置了join所以不需要设置及时加载了，默认就是

    //仅在个人页面或者许改时展示，或者仅个人可见
    private String birthday;
    private Integer faceRank;
    private Integer faceRatio;
    private Integer baseFaceRatio;
    private Integer faceValue;
    private Integer followNum;
    //拥有的清池币数量
    private Integer qcb;
    //贝壳，单位毛
    private Integer shell;
    //单位等于分

    //爱心值、正义值、与登记相关
    private Integer loveValue;
    private Integer justiceValue;
    private String phoneNum;

    //从来不会在前台展示
    /**
     * 被人查看的次数
     */
    private Integer seeCount;
    /**
     * 被人喜欢的次数
     */
    private Integer likeCount;
    /**
     * 个性签名
     */
    private Date createTime;
    private Date updateTime;
    /**
     * 被人喜欢的次数
     */
    private boolean openMatch;
    //认证次数，3次以上需要咨询客服
    private Integer authNum;
    //身份证四个状态 初始，待审核，已认证，未通过认证
    private String idCardStatus;
    //用户状态，暂未使用正常，封禁
    private String status;
    //用户是否在线，
    private Boolean onlineFlag;

    //用户最后在线时间，
    private Date lastOnlineTime;

    //邀请码，你分享给别人的邀请码
    private String inviteCode;
    //注册码，别人分享给你的邀请码
    private String registerInviteCode;
    //邀请你的用户
    private Integer registerInviteUserId;

    private Integer reportNum;
    //用户类型，系统，通知，普通
    private String type;

    /**
     * 用户信息版本号
     */
    private Integer versionNo;
    /**
     * 被违规的次数，默认0
     */
    private Integer violationCount;

    /**
     * 违规原因
     */
    private String violationReason;

    /**
     * 上次展示广告的时间
     */
    private Date lastShowAdTime;


    //使用哪个平台注册的,上次使用哪个平台登陆的。用来判断往哪个平台推送通知
    //用来区分h5还是mp
    private String platform;
    //phoneCountryCode
    private String phoneCountryCode;

    //vip到期时间
    private Date vipEndDate;

    /**
     * 封禁开始时间
     */
    private Date violationStartTime;

    /**
     * 封禁结束时间
     */
    private Date violationEndTime;

    /**
     * 用户是否已认证
     *
     * @return
     */
    @JsonIgnore
    public boolean isCertified() {
        return Arrays.asList(CommonStatus.audit, CommonStatus.noViolation).contains(this.idCardStatus);
    }


}