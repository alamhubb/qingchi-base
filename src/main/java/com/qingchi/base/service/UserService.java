package com.qingchi.base.service;

import com.qingchi.base.model.chat.ChatDO;
import com.qingchi.base.model.chat.ChatUserDO;
import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.constant.*;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.model.user.IdCardDO;
import com.qingchi.base.model.user.UserDetailDO;
import com.qingchi.base.repository.chat.ChatRepository;
import com.qingchi.base.repository.chat.ChatUserRepository;
import com.qingchi.base.repository.shell.VipOrderRepository;
import com.qingchi.base.repository.shell.VipSaleRepository;
import com.qingchi.base.repository.user.IdCardRepository;
import com.qingchi.base.repository.user.UserDetailRepository;
import com.qingchi.base.repository.user.UserImgRepository;
import com.qingchi.base.repository.user.UserRepository;
import com.qingchi.base.utils.AgeUtils;
import com.qingchi.base.utils.GenerateNickname;
import com.qingchi.base.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2019-06-25 19:59
 */
@Service
public class UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserDetailRepository userDetailRepository;
    @Resource
    private EntityManager entityManager;
    @Resource
    private UserImgRepository userImgRepository;
    @Resource
    private IdCardRepository idCardRepository;
    @Resource
    private VipSaleRepository vipSaleRepository;
    @Resource
    private VipOrderRepository vipOrderRepository;
    @Value("${config.initAvatar}")
    private String initAvatar;
    @Resource
    private FollowService followService;

    @Resource
    private VipService vipService;
    @Resource
    private ChatRepository chatRepository;
    @Resource
    private ChatUserRepository chatUserRepository;

    public void setUserOnlineTrue(String userId) {
        Optional<UserDO> optionalUserDO = UserUtils.getUserOpt(Integer.parseInt(userId));
        if (optionalUserDO.isPresent()) {
            UserDO user = optionalUserDO.get();
            user.setOnlineFlag(true);
            user.setLastOnlineTime(new Date());
            userRepository.save(user);
        }
    }

    public void setUserOnlineFalse(String userId) {
        Optional<UserDO> optionalUserDO = UserUtils.getUserOpt(Integer.parseInt(userId));
        if (optionalUserDO.isPresent()) {
            UserDO user = optionalUserDO.get();
            user.setOnlineFlag(false);
            user.setLastOnlineTime(new Date());
            userRepository.save(user);
        }
    }

    public UserDO createUser(String nickname, String avatar, String gender, String city, String platform) {
        return this.createUser(nickname, avatar, gender, CommonConst.initBirthday, city, platform);
    }

    public UserDO createUser(String nickname, String avatar, String gender, String birthday, String city, String platform) {
        List<String> illegals = AppConfigConst.illegals;
        for (String illegal : illegals) {
            if (StringUtils.isNotEmpty(illegal) && StringUtils.containsIgnoreCase(nickname, illegal)) {
                nickname = "未设置昵称";
            }
        }
        UserDO user = new UserDO();
        user.setPlatform(platform);

        if (StringUtils.isNotEmpty(nickname)) {
            user.setNickname(StringUtils.substring(nickname, 0, 6));
        } else {
            user.setNickname("未设置昵称");
        }
        user.setAvatar(avatar);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setLocation(city);
        return this.createUser(user);
    }

    public UserDO createUser(String phoneNum, String platform) {
        UserDO user = new UserDO();
        user.setPhoneNum(phoneNum);
        user.setPlatform(platform);

        user.setNickname(GenerateNickname.getGirlName());
        user.setAvatar(initAvatar);
        user.setGender(CommonConst.girl);
        user.setBirthday(CommonConst.initBirthday);
        user.setLocation("北京");
        return this.createUser(user);
    }

    public UserDO createUser(UserDO user) {
        if (StringUtils.isEmpty(user.getNickname())) {
            user.setNickname(GenerateNickname.getGirlName());
        }
        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar(initAvatar);
        }
        if (StringUtils.isEmpty(user.getGender())) {
            //默认设置为女生，因为女生不介意当男生，而男生介意当女生，所以男生会主动修改，
            // 且体现女士优先，让女士更方便
            user.setGender(CommonConst.girl);
        }
        if (StringUtils.isEmpty(user.getBirthday())) {
            user.setBirthday(CommonConst.initBirthday);
        }
        if (StringUtils.isEmpty(user.getLocation())) {
            user.setLocation("北京");
        }
        Date curDate = new Date();
        // 初始都为0
        user.setFaceRatio(0);
        user.setBaseFaceRatio(0);
        user.setLikeCount(0);
        user.setSeeCount(MatchConstants.INIT_FACE_SEE_NUM);
        //新用户注册，颜值为最低，0
        user.setFaceValue(0);
        user.setFaceRank(0);
        user.setAge(AgeUtils.getAgeByBirth(user.getBirthday()));
        user.setIdCardStatus(CommonStatus.init);
        //初始为正常用户
        user.setVipFlag(false);
        user.setYearVipFlag(false);
        user.setQcb(0);
        //经验等级
        user.setGradeLevel(0);
        //经验值
        user.setShell(0);
        //财富等级
        user.setWealthLevel(0);
        user.setStatus(CommonStatus.enable);
        user.setViolationCount(0);
        user.setFansNum(0);
        user.setFollowNum(0);
        user.setCreateTime(curDate);
        user.setUpdateTime(curDate);
        user.setLastOnlineTime(curDate);
        user.setLastShowAdTime(curDate);
        user.setType(UserType.personal);
        user.setVersionNo(1);
        user.setLoveValue(0);
        user.setJusticeValue(0);
        user.setReportNum(0);
        user.setAuthNum(0);
        user.setIsSelfAuth(false);
        userRepository.save(user);
        entityManager.clear();
        Optional<UserDO> userDOOptional = userRepository.findById(user.getId());
        if (userDOOptional.isPresent()) {
            user = userDOOptional.get();
        }
        /*vipService.addVipOrder(VipGiftType.register, user, user.getRegisterInviteCode(), user.getRegisterInviteUser());
        if (user.getRegisterInviteUser() != null) {
            vipService.addVipOrder(VipGiftType.invite, user, user.getRegisterInviteCode(), user.getRegisterInviteUser());
        }*/
        //设置个人邀请码
        String userInviteCode = "0000000" + user.getId().toString();
        user.setInviteCode(userInviteCode.substring(userInviteCode.length() - 8));
        user = userRepository.save(user);
        //注释掉圈子功能
        List<ChatDO> chatDOS = chatRepository.findByTypeAndStatus(ChatType.system_group, CommonStatus.enable);
        for (ChatDO chat : chatDOS) {
            ChatUserDO chatUserDO = new ChatUserDO(chat, user.getId());
            chatUserRepository.save(chatUserDO);
        }

        Optional<UserDO> systemUserOptional = userRepository.findById(UserUtils.getSystemId());

        if (systemUserOptional.isPresent()) {
            UserDO sysUser = systemUserOptional.get();
            //所有人注册默认关注系统用户
            followService.addFlow(user, sysUser);
        }
        //初始化用户详情
        UserDetailDO userDetailDO = new UserDetailDO(user);
        userDetailRepository.save(userDetailDO);
        return userRepository.findById(user.getId()).get();
    }

    public UserDO addIdCard(UserDO user, IdCardDO idCardDO) {
        //首先将之前的所有idcard改为禁用，目前不存在多次认证的情况
        /*List<IdCardDO> idCardDOS = idCardRepository.findByUserAndStatus(user, CommonStatus.enable);
        for (IdCardDO cardDO : idCardDOS) {
            cardDO.setStatus(CommonStatus.disable);
        }
        idCardRepository.saveAll(idCardDOS);*/
        // 新idcard信息
        idCardDO.setUserId(user.getId());
        idCardRepository.save(idCardDO);
        // 用户身份证状态改为审核中状态
        user.setIdCardStatus(CommonStatus.audit);
        user.setUpdateTime(new Date());
        //提交认证，更新颜值分
        this.updateFaceContent(user);
        userRepository.save(user);
        entityManager.clear();
        return userRepository.getOne(user.getId());
    }


    //更新用户颜值相关内容
    public void updateUserListFaceContent(List<UserDO> users) {
        for (UserDO user : users) {
            updateFaceContent(user);
        }
        userRepository.saveAll(users);
    }

    //更新用户颜值相关内容
    public void updateFaceContent(UserDO user) {
        Integer[] faceValues;
        if (user.getGender().equals(CommonConst.boy)) {
            faceValues = AppConfigConst.getBoysFaceValueList();
        } else {
            faceValues = AppConfigConst.getGirlsFaceValueList();
        }
        //用户被查看，喜欢次数+1
        user.setSeeCount(user.getSeeCount() + 1);
        //根据用户的查看次数和喜欢次数，计算用户的颜值
        long faceValue = user.getLikeCount() * MatchConstants.FACE_VALUE_BASE_MULTIPLE / user.getSeeCount();
        user.setFaceValue((int) faceValue);
        //有了用户颜值
        Integer userFaceValue = user.getFaceValue();
        int faceValueLength = faceValues.length;
        //颜值从小到大排序的
        if (faceValues.length > 0) {
            //默认最后一名
            int faceRank = binary(faceValues, userFaceValue);
            Long setFaceRatio;
            if (faceRank >= faceValueLength) {
                user.setFaceRank(1);
                setFaceRatio = MatchConstants.FACE_VALUE_BASE_MULTIPLE;
            } else {
                user.setFaceRank(faceValueLength - faceRank);
                setFaceRatio = faceRank * MatchConstants.FACE_VALUE_BASE_MULTIPLE / faceValueLength;
            }
            user.setBaseFaceRatio(setFaceRatio.intValue());
            if (user.getVipFlag() != null && user.getVipFlag()) {
                //普通会员加20分
                setFaceRatio += MatchConstants.FACE_VALUE_VIP_APPEND_MULTIPLE;
            }
            //根据这个排名除以总男数得到百分比数
            //认证增加30分
            /*if (user.isCertified()) {
                setFaceRatio += MatchConstants.FACE_VALUE_APPEND_MULTIPLE;
            }*/
            //年会员加30分
            /*if (user.getYearVipFlag() != null && user.getYearVipFlag()) {
                setFaceRatio += MatchConstants.FACE_VALUE_YEAR_VIP_APPEND_MULTIPLE;
            } else if (user.getVipFlag() != null && user.getVipFlag()) {
                //普通会员加20分
                setFaceRatio += MatchConstants.FACE_VALUE_VIP_APPEND_MULTIPLE;
            }*/
            //100个清池币1颜值
            /*if (user.getQcb() != null && user.getQcb() > 0) {
                setFaceRatio += user.getQcb() * MatchConstants.QCB_FACE_VALUE_RATIO;
            }*/
            user.setFaceRatio(setFaceRatio.intValue());
        }
    }

    //## 标题二分法查找,返回插入点索引
    public static int binary(Integer[] arr, Integer n) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid].equals(n)) {
                return mid;
            } else if (arr[mid] > n) {
                high = mid - 1;
            } else if (arr[mid] < n) {
                low = mid + 1;
            }
        }
        return mid + 1;
    }
}
