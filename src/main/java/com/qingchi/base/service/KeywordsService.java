package com.qingchi.base.service;

import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.model.system.KeywordsDO;
import com.qingchi.base.model.system.KeywordsTriggerDetailDO;
import com.qingchi.base.repository.keywords.KeywordsRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qinkaiyuan
 * @date 2020-03-15 22:05
 */
@Service
public class KeywordsService {

    @Resource
    private KeywordsRepository keywordsRepository;


    //计算关键词违规率，全匹配违规率，变种匹配率
    public void calculateViolateRatioByReportStatus(String auditResult, KeywordsTriggerDetailDO keywordsTriggerDetailDO, KeywordsDO wordDO) {
        //关键词触发总量+1
        Integer totalNum = wordDO.getTotalNum() + 1;
        wordDO.setTotalNum(totalNum);
        //查看是否为变种匹配
        if (keywordsTriggerDetailDO.getHasVariation()) {
            //违规
            if (CommonStatus.violation.equals(auditResult)) {
                //关键词违规总量+1
                Integer violateNum = wordDO.getViolateNum() + 1;
                wordDO.setViolateNum(violateNum);
                //计算关键词总违规比，和误触比
                wordDO.setViolateRatio(violateNum.doubleValue() / totalNum.doubleValue());
                wordDO.setNormalRatio(1 - wordDO.getViolateRatio());

                //计算主体违规率，总数+1
                Integer pinyinTotalNum = wordDO.getVariationTotalNum() + 1;
                wordDO.setVariationTotalNum(pinyinTotalNum);

                //违规+1
                Integer pinyinViolateNum = wordDO.getVariationViolateNum() + 1;
                wordDO.setVariationViolateNum(pinyinViolateNum);

                //计算主体违规和误触比
                wordDO.setVariationViolateRatio(pinyinViolateNum.doubleValue() / pinyinTotalNum.doubleValue());
                wordDO.setVariationNormalRatio(1 - wordDO.getVariationViolateRatio());
            } else {
                //误触
                //计算主体误触率，总数+1
                Integer pinyinTotalNum = wordDO.getVariationTotalNum() + 1;
                wordDO.setVariationTotalNum(pinyinTotalNum);
                //误触+1
                Integer pinyinNormalNum = wordDO.getVariationNormalNum() + 1;
                wordDO.setVariationNormalNum(pinyinNormalNum);
                //计算主体违规和误触比
                wordDO.setVariationNormalRatio(pinyinNormalNum.doubleValue() / pinyinTotalNum.doubleValue());
                wordDO.setVariationViolateRatio(1 - wordDO.getVariationNormalRatio());
            }
        } else {
            //关键词全匹配
            //违规
            if (CommonStatus.violation.equals(auditResult)) {
                //关键词违规总量+1
                Integer violateNum = wordDO.getViolateNum() + 1;
                wordDO.setViolateNum(violateNum);
                //计算关键词总违规比，和误触比
                wordDO.setViolateRatio(violateNum.doubleValue() / totalNum.doubleValue());
                wordDO.setNormalRatio(1 - wordDO.getViolateRatio());

                //计算主体违规率，总数+1
                Integer textTotalNum = wordDO.getTextTotalNum() + 1;
                wordDO.setTextTotalNum(textTotalNum);
                //违规+1
                Integer textViolateNum = wordDO.getTextViolateNum() + 1;
                wordDO.setTextViolateNum(textViolateNum);
                //计算主体违规和误触比
                wordDO.setTextViolateRatio(textViolateNum.doubleValue() / textTotalNum.doubleValue());
                wordDO.setTextNormalRatio(1 - wordDO.getTextViolateRatio());
            } else {
                //误触
                //关键词误触总量+1
                Integer normalNum = wordDO.getNormalNum() + 1;
                wordDO.setNormalNum(normalNum);
                //计算关键词总违规比，和误触比
                wordDO.setNormalRatio(normalNum.doubleValue() / totalNum.doubleValue());
                wordDO.setViolateRatio(1 - wordDO.getNormalRatio());

                //计算主体误触率，总数+1
                Integer textTotalNum = wordDO.getTextTotalNum() + 1;
                wordDO.setTextTotalNum(textTotalNum);
                //误触+1
                Integer TextNormalNum = wordDO.getTextNormalNum() + 1;
                wordDO.setTextNormalNum(TextNormalNum);
                //计算主体违规和误触比
                wordDO.setTextNormalRatio(TextNormalNum.doubleValue() / textTotalNum.doubleValue());
                wordDO.setTextViolateRatio(1 - wordDO.getTextNormalRatio());
            }
        }
    }
}
