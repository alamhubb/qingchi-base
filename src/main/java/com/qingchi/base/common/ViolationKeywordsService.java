package com.qingchi.base.common;

import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.model.system.IllegalWordDO;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.system.KeywordsDO;
import com.qingchi.base.repository.keywords.IllegalWordRepository;
import com.qingchi.base.repository.keywords.KeywordsRepository;
import com.qingchi.base.repository.notify.NotifyRepository;
import com.qingchi.base.service.NotifyService;
import com.qingchi.base.repository.talk.CommentRepository;
import com.qingchi.base.repository.talk.TalkRepository;
import com.qingchi.base.service.ViolationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinkaiyuan
 * @date 2020-03-17 16:36
 */
@Service
public class ViolationKeywordsService {
    @Resource
    private IllegalWordRepository illegalWordRepository;
    @Resource
    private TalkRepository talkRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private NotifyService notifyService;
    @Resource
    private NotifyRepository notifyRepository;
    @Resource
    private ViolationService violationService;

    @Resource
    private KeywordsRepository keywordsRepository;

    public void refreshKeywords() {
        List<IllegalWordDO> illegalWordDOS = illegalWordRepository.findAllByStatus(CommonStatus.normal);
        List<String> illegals = illegalWordDOS.stream().map(IllegalWordDO::getWord).collect(Collectors.toList());
        AppConfigConst.setIllegals(illegals);

        List<KeywordsDO> keywordsDOS= keywordsRepository.findAllByStatusIsNull();
        AppConfigConst.setKeywordDOs(keywordsDOS);
        //目前不需要全部检索然后删除了，已经使用了举报机制
        /*for (String illegal : illegals) {
            if (StringUtils.isNotEmpty(illegal)) {
                String illegalSql = "%" + illegal + "%";
                //系统管理员,告知别人谁发的通知
                UserDO systemUser = UserUtils.getSystemUser();
                //删除所有违规的talk
                List<TalkDO> talkDOS = talkRepository.findAllByContentLikeAndStatus(illegalSql, CommonStatus.enable);
                for (Integer talkIdDO : talkDOS) {
                    Logger.logger.info("违规内容----：" + talkDO.getContent());
                    Logger.logger.info("违规关键词----：" + illegalSql);
                    //删除talk和封禁用户处理
                    violationService.talkViolationHandler(talkDO, ErrorMsg.CONTENT_VIOLATION, true);
                    //给用户发送被封通知
                    //推送消息
                    NotifyDO notifyDO = notifyRepository.save(new NotifyDO(systemUser, talkDO.getUser(), talkDO));
                    notifyService.sendNotify(notifyDO);
                }
                //删除所有违规的评论
                List<CommentDO> commentDOS = commentRepository.findAllByContentLikeAndStatus(illegalSql, CommonStatus.enable);
                for (Integer commentIdDO : commentDOS) {
                    Logger.logger.info("违规内容----：" + commentDO.getContent());
                    Logger.logger.info("违规关键词----：" + illegalSql);
                    //删除talk和封禁用户处理
                    violationService.commentViolationHandler(commentDO, ErrorMsg.CONTENT_VIOLATION, true);
                    //给用户发送被封通知
                    NotifyDO notifyDO = notifyRepository.save(new NotifyDO(systemUser, commentDO.getUser(), commentDO, NotifyType.delete_comment));
                    notifyService.sendNotify(notifyDO);
                }
            }
        }*/
    }
}
