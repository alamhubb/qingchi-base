package com.qingchi.base.store;

import com.qingchi.base.constant.status.ReportStatus;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.repository.report.ReportRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ReportStore {
    @Resource
    ReportRepository reportRepository;

    public List<ReportDO> queryUserOtherWaitAuditContent(Integer userId) {
        return reportRepository.findByReceiveUserIdAndStatusIn(userId, ReportStatus.auditStatus);
    }
}
