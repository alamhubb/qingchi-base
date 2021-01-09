package com.qingchi.base.domain;

import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.common.ResultVO;
import com.qingchi.base.constant.*;
import com.qingchi.base.entity.ReportDetailUtils;
import com.qingchi.base.factory.ReportFactory;
import com.qingchi.base.model.BaseModelDO;
import com.qingchi.base.model.report.ReportAddVO;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.model.report.ReportDetailDO;
import com.qingchi.base.model.system.KeywordsDO;
import com.qingchi.base.model.system.KeywordsTriggerDetailDO;
import com.qingchi.base.model.user.JusticeValueOrderDO;
import com.qingchi.base.model.user.UserDO;
import com.qingchi.base.repository.keywords.KeywordsRepository;
import com.qingchi.base.repository.keywords.KeywordsTriggerDetailRepository;
import com.qingchi.base.repository.report.ReportDetailRepository;
import com.qingchi.base.repository.report.ReportRepository;
import com.qingchi.base.repository.user.JusticeValueOrderRepository;
import com.qingchi.base.repository.user.UserRepository;
import com.qingchi.base.service.BaseModelService;
import com.qingchi.base.service.KeywordsService;
import com.qingchi.base.service.KeywordsTriggerService;
import com.qingchi.base.utils.DateUtils;
import com.qingchi.base.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2020-03-19 20:05
 */
@Service
public class ReportDomain {
    @Resource
    private ReportDetailRepository reportDetailRepository;
    @Resource
    private JusticeValueOrderRepository justiceValueOrderRepository;
    @Resource
    private KeywordsTriggerDetailRepository keywordsTriggerDetailRepository;
    @Resource
    private ReportRepository reportRepository;
    @Resource
    private KeywordsTriggerService keywordsTriggerService;
    @Resource
    private UserRepository userRepository;

    @Resource
    private KeywordsRepository keywordsRepository;

    @Resource
    private BaseModelService baseModelService;


    @Resource
    private KeywordsService keywordsService;

    @Resource
    private ReportFactory reportFactory;

    //根据是否违规生成举报信息，只需要basedo基础信息就够
    @Transactional
    public void checkKeywordsCreateReport(BaseModelDO modelDO) throws IOException {
        //不为空才校验内容
        if (StringUtils.isNotEmpty(modelDO.getContent())) {
            //网易三方审查
//            AntispamDO antispamDO = WangYiUtil.checkWYContentSecPost(modelDO);

            // 校验是否触发关键词
            List<KeywordsTriggerDetailDO> keywordsTriggers = keywordsTriggerService
                    .checkContentTriggerKeywords(modelDO, modelDO.getReportContentType(), AppConfigConst.getKeywordDOs());

//            if (!CollectionUtils.isEmpty(keywordsTriggers) || antispamDO.hasViolate()) {
            //如果触发了关键词
            if (!CollectionUtils.isEmpty(keywordsTriggers)) {
                String reportCause;
                ReportDO reportDO;
            /*    if (antispamDO.hasViolate()) {
                    reportCause = antispamDO.getCause();
                    reportDO = reportFactory.createReportDO(reportCause, modelDO, ReportSourceType.antispam, antispamDO.getId());
                } else {*/
                reportCause = "系统自动审查";
                reportDO = reportFactory.createReportDO(reportCause, modelDO, ReportSourceType.systemAutoCheck);
//                }

                //这里之后才能校验
                // 设置model
                //可以放到 report的 store 中
                //保存数据
                reportDO = reportRepository.save(reportDO);
                //生成举报详情
                ReportDetailDO reportDetailDO =
                        new ReportDetailDO(reportCause, ViolateType.pornInfo, reportDO, modelDO);

                reportDetailRepository.save(reportDetailDO);


                Integer reportId = reportDO.getId();

                if (keywordsTriggers.size() > 0) {
                    //为触发记录关联 report
                    keywordsTriggers.forEach(keywordsTriggerDetailDO -> {
                        keywordsTriggerDetailDO.setReportId(reportId);
                    });

                    //保存触发记录
                    keywordsTriggerDetailRepository.saveAll(keywordsTriggers);
                }


                //更新 model
                modelDO.setStatus(CommonStatus.preAudit);
                modelDO.setUpdateTime(new Date());
                baseModelService.save(modelDO);
            }
        }
    }


    @Transactional
    public ResultVO<?> userReportContent(ReportAddVO reportAddVO, BaseModelDO modelDO, Integer requestUserId) {
        //这里之后才能校验

        // 设置model

        //这里之后才能校验
        // 设置model
        //可以放到 report的 store 中
        ReportDO reportDO = reportFactory.createReportDO(reportAddVO.getContent(), modelDO, ReportSourceType.userReport);

        //保存数据
        reportDO = reportRepository.save(reportDO);

        //生成举报详情
        ReportDetailDO reportDetailDO =
                new ReportDetailDO(reportAddVO.getContent(), reportAddVO.getReportType(), reportDO, modelDO, requestUserId);

        reportDetailRepository.save(reportDetailDO);


        ResultVO<String> resultVO = new ResultVO<>();

        //只有用户举报的才修改用户状态
        Integer receiveUserId = modelDO.getUserId();

        //系统自动审查，则只修改动态为预审查


        //用户举报其他用户的逻辑
        UserDO receiveUser = UserUtils.get(receiveUserId);


        Integer modelReportNum = modelDO.getReportNum() + 1;
        modelDO.setReportNum(modelReportNum);
        //被1个人举报就进入审核中,这里做判断是因为阀值以后可能会调整
        Integer reportCountHide = (Integer) AppConfigConst.appConfigMap.get(AppConfigConst.reportCountHideKey);
        //大于阀值，更改动态和用户状态，否则只增加举报此数
        if (modelReportNum >= reportCountHide) {
            modelDO.setStatus(CommonStatus.audit);
            resultVO.setData(ErrorMsg.reportSubmitHide);

            //如果被举报的用户是官方，则不修改官方的用户状态、只存在于官方自己举报自己时，也不能修改自己的用户状态
            if (!receiveUser.getType().equals(UserType.system)) {
                //只有用户为正常时，才改为待审核，如果用户已被封禁则不改变状态
                if (receiveUser.getStatus().equals(CommonStatus.enable)) {
                    receiveUser.setStatus(CommonStatus.audit);
                }
            }
            //记录用户的被举报此数
        } else {
            resultVO.setData(ErrorMsg.reportSubmit);
        }
        receiveUser.setReportNum(receiveUser.getReportNum() + 1);

        userRepository.save(receiveUser);

        //有关
        //必须要单独保存，涉及到缓存
        //被1个人举报就进入审核中,这里做判断是因为阀值以后可能会调整
        //更新 model
        modelDO.setUpdateTime(new Date());
        baseModelService.save(modelDO);
//     todo  测试，不在这里保存，使用 report 的级联保存是否可以
//        baseModelService.save(modelDO);

        // 校验是否触发关键词
        /*List<KeywordsTriggerDetailDO> keywordsTriggers = keywordsTriggerService
                .checkContentTriggerKeywords(modelDO, ReportContentType.comment);

        String reportCause = "系统自动审查";

        Integer reportId = reportDO.getId();

        //为触发记录关联 report
        keywordsTriggers.forEach(keywordsTriggerDetailDO -> {
            keywordsTriggerDetailDO.setReportId(reportId);
        });

        //保存触发记录
        keywordsTriggerDetailRepository.saveAll(keywordsTriggers);*/

        //必须要单独保存，涉及到缓存
        return resultVO;
    }


    /**
     * 动态被2人以上举报，则用户自己也看不见了。两人以下举报，用户可自行删除
     * <p>
     * 管理直接发现，未通过审核平台，
     * <p>
     * 首先确认talk 是否真实
     * <p>
     * 根据talk查询是否存在相关举报
     * <p>
     * 判断用户有没有举报记录，有举报记录，走举报，没有走直接封禁
     *
     * @param
     */
    public void reportPass(ReportDO reportDO, boolean isViolation) {
        if (ReportSourceType.userReport.equals(reportDO.getReportSourceType())) {

            Date curDate = new Date();
            //审核通过不再接受举报，前台点击举报时，提示已官方审核通过
            List<ReportDetailDO> reportDetailDOS = ReportDetailUtils.getAll(reportDO.getId());

            //变更detail
            for (ReportDetailDO reportDetailDO : reportDetailDOS) {
                UserDO detailUser = UserUtils.get(reportDetailDO.getUserId());

                //相同部分
                JusticeValueOrderDO justiceValueOrderDO = new JusticeValueOrderDO();

                //举报内容违规，加分
                if (isViolation) {
                    Date todayZero = DateUtils.getTodayZeroDate();
                    //查看用户待审核的举报数量
                    Integer reportSuccessCount = reportDetailRepository.countByUserIdAndStatusNotAndCreateTimeBetween(detailUser.getId(), CommonStatus.audit, todayZero, curDate);
                    //todo  缺少发送通知功能，等我精神好了在写
                    reportDetailDO.setStatus(CommonStatus.violation);
                    if (reportSuccessCount > 9) {
                        //todo 发送通知
                    } else {
                        justiceValueOrderDO.setJusticeValue(AppConfigConst.auditSuccessValue);
                        detailUser.setJusticeValue(detailUser.getJusticeValue() + AppConfigConst.auditSuccessValue);
                        //user加分
//                        if (reportDetailDO.getReportType().equals(auditVO.getReportType())) {
                        //目前不区分举报类型
                        //如果今天已经成功举报了10个以上，则不再发放奖励
                        //默认不奖励

                        /*} else {
                            detailUser.setJusticeValue(detailUser.getJusticeValue() + CommonConst.reportTypeMistakeValue);
                        }*/
                        //发送通知
                    }
                } else {
                    //如果今天已经成功举报了10个以上，则不再发放奖励
                    reportDetailDO.setStatus(CommonStatus.noViolation);
                    //错误的举报，user减分
                    justiceValueOrderDO.setJusticeValue(-AppConfigConst.reportErrorValue);
                    detailUser.setJusticeValue(detailUser.getJusticeValue() - AppConfigConst.reportErrorValue);
                }
                justiceValueOrderDO.setCreateTime(curDate);
                justiceValueOrderDO.setUserId(detailUser.getId());
                justiceValueOrderDO.setReportDetailId(reportDetailDO.getId());

                justiceValueOrderRepository.save(justiceValueOrderDO);


                //不允许重复举报
                //应该查询今天奖励了多少次了
                //如果类型一直加10分
                //


                //不允许重复举报
                //应该查询今天奖励了多少次了
                //如果类型一直加10分
                //
            }
        }
    }
}
