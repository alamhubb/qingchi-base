package com.qingchi.base.factory;

import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.constant.ReportSourceType;
import com.qingchi.base.constant.status.ReportStatus;
import com.qingchi.base.model.BaseModelDO;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.repository.talk.CommentRepository;
import com.qingchi.base.service.ReportService;
import com.qingchi.base.utils.BaseModelUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class ReportFactory {
    @Resource
    private CommentRepository commentRepository;

    @Resource
    private ReportService reportService;

    public ReportDO createReportDO(
            String cause,
            BaseModelDO model,
            String reportSourceType
    ) {
        // 设置model
        ReportDO reportDO = new ReportDO();

        reportDO.setCause(cause);
        reportDO.setReportContent(model.getContent());
        if (ReportSourceType.userReport.equals(reportSourceType)) {
            reportDO.setStatus(ReportStatus.audit);
        } else {
            reportDO.setStatus(ReportStatus.preAudit);
        }
        //第一次被举报设置默认值
        reportDO.setSupportRatio(100);
        reportDO.setSupportRatio(0);
        reportDO.setHasReview(false);
        Date curDate = new Date();
        reportDO.setCreateTime(curDate);
        reportDO.setUpdateTime(curDate);
        //内容来源
        reportDO.setReportContentType(model.getReportContentType());
        //举报来源
        reportDO.setReportSourceType(reportSourceType);
        //设置被举报用户
        reportDO.setReceiveUserId(model.getUserId());
        reportDO.setContentId(model.getDbId());
        return BaseModelUtils.setBaseModelId(reportDO, model);
    }

    public ReportDO createReportDO(
            String cause,
            BaseModelDO model,
            String reportSourceType,
            Integer antispamId
    ) {
        // 设置model
        ReportDO reportDO = this.createReportDO(cause, model, reportSourceType);
        reportDO.setAntispamId(antispamId);
        return reportDO;
    }
}