package com.qingchi.base.entity;

import com.qingchi.base.model.report.ReportDetailDO;
import com.qingchi.base.repository.report.ReportDetailRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ReportDetailUtils {
    private static ReportDetailRepository reportDetailRepository;

    @Resource
    public void setReportDetailRepository(ReportDetailRepository reportDetailRepository) {
        ReportDetailUtils.reportDetailRepository = reportDetailRepository;
    }

    public static List<ReportDetailDO> getAll(Integer reportId) {
        return reportDetailRepository.findAllByReportId(reportId);
    }

    public static List<ReportDetailDO> saveAll(List<ReportDetailDO> reportDetailDOS) {
        return reportDetailRepository.saveAll(reportDetailDOS);
    }

    public static Integer count(Integer reportId) {
        return reportDetailRepository.countByReportId(reportId);
    }
}
