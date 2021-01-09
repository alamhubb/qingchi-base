package com.qingchi.base.service;

import com.github.promeg.pinyinhelper.Pinyin;
import com.qingchi.base.config.AppConfigConst;
import com.qingchi.base.model.BaseModelDO;
import com.qingchi.base.model.system.KeywordsDO;
import com.qingchi.base.model.system.KeywordsTriggerDetailDO;
import com.qingchi.base.utils.KeywordsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qinkaiyuan
 * @date 2020-03-15 22:05
 */
@Service
public class KeywordsTriggerService {
    @Resource
    private KeywordsService keywordsService;


    public List<KeywordsTriggerDetailDO> checkContentTriggerKeywords(
            BaseModelDO baseModelDO,
            String contentType,
            List<KeywordsDO> keywordDOs
    ) {

        List<KeywordsTriggerDetailDO> keywordsTriggers = new ArrayList<>();
        int matchContentLength = 6;
        boolean modifyReportStatusFlag = false;

        String baseModelContent = baseModelDO.getContent();
        Integer baseModelId = baseModelDO.getDbId();

        //记录这条内容是否有触发关键词
        //记录触发的关键词列表
        //获取文本主要主体
        //不为null
        if (baseModelContent != null) {
            //删除空白字符和空格，去除特殊字符，升为大写
            String contentFormat = KeywordsUtils.StringFilter(baseModelContent.trim().replaceAll("\\s*", "").toUpperCase());
            //文本不为空,拼音和文本都需要判空
            if (StringUtils.isNotEmpty(contentFormat)) {
                //以下逻辑，为获取每个字在拼音中的位置， 拼音与文字位置对应，做的辅助功能

                //转为拼音先有分割，记录位置使用
                String contentVariationHasEmpty = Pinyin.toPinyin(contentFormat, " ");
                //替换分割，改为无分割，并转为大写
                String contentVariation = contentVariationHasEmpty.replaceAll(" ", "").toUpperCase();
                //一组拼音对应一个字
                String[] contentVariationAry = contentVariationHasEmpty.split(" ");
                //存储字体索引对应的 拼音字数结束位置
                List<Integer> contentWordIndexList = new ArrayList<>();
                contentWordIndexList.add(0);
                int sum = 0;
                //遍历拼音数组，得到每个字在拼音中的位置
                for (String s : contentVariationAry) {
                    //位置叠加
                    sum += s.length();
                    //记录自己这个字在拼音中的结束位置
                    contentWordIndexList.add(sum);
                }
                //遍历关键词
                for (KeywordsDO keywordsDO : keywordDOs) {
                    //这部分可以优化，不为文字以后才弄拼音
                    String keywordsText = keywordsDO.getText();
                    //判空
                    if (StringUtils.isNotEmpty(keywordsText)) {
                        //记录这个关键词是否触发
                        KeywordsTriggerDetailDO keywordsTriggerDetailDO = null;

                        //如果打开了关键词打开了文本匹配，且文本包含关键词主体
                        if (keywordsDO.getOpenText() && contentFormat.contains(keywordsText)) {
                            //截取主要违规内容
                            int subStartIndex = contentFormat.indexOf(keywordsText);

                            //截取主要违规内容
                            String matchText = StringUtils.substring(contentFormat, subStartIndex, subStartIndex + matchContentLength);

                            //如果文本不违规次数小于20，或者 违规率大于0.3
                            if (keywordsDO.getTextNormalNum() < 20 || keywordsDO.getTextViolateRatio() > 0.3) {
                                modifyReportStatusFlag = true;
                                //reportstatus因为会有两种状态初始审查，是初始，预审核，
                                keywordsTriggerDetailDO = new KeywordsTriggerDetailDO(
                                        baseModelContent,
                                        baseModelId,
                                        contentType,
                                        keywordsDO.getId(),
                                        keywordsDO.getTextShow(),
                                        matchText,
                                        false
                                );
                            }
                            //如果打开了变种匹配
                        } else if (keywordsDO.getOpenVariation()) {
//                            keywordsTriggerFlag = isKeywordsTriggerFlag(matchContentLength, contentFormat, contentVariation, contentWordIndexList, wordDO, keywordsTriggerDetailDO);
                            //如果文本不违规次数小于20，或者 违规率大于0.2
                            if (keywordsDO.getVariationNormalNum() < 20 || keywordsDO.getVariationViolateRatio() > 0.2) {
                                modifyReportStatusFlag = true;
                                //得到拼音的变种
                                keywordsTriggerDetailDO = getKeywordsTriggerDetailDO(baseModelContent, baseModelId, contentType, matchContentLength, contentFormat, contentVariation, contentWordIndexList, keywordsDO);
                            }
                        }

                        //如果触发了这个关键词，则将这个关键词添加到触发详情列表中
                        if (keywordsTriggerDetailDO != null) {
                            keywordsTriggers.add(keywordsTriggerDetailDO);
                        }
                    }
                }
            }
        }
        return keywordsTriggers;
    }

    //变种匹配构建
    private KeywordsTriggerDetailDO getKeywordsTriggerDetailDO(
            String baseModelContent,
            Integer baseModelId,
            String contentType,
            int matchContentLength,
            String contentFormat,
            String contentVariation,
            List<Integer> contentWordIndexList,
            KeywordsDO wordDO
    ) {
        String keywordsVariation = wordDO.getVariationText();

        KeywordsTriggerDetailDO keywordsTriggerDetailDO = null;
        //如果变种文本，包含变种关键词
        if (contentVariation.contains(keywordsVariation)) {
            //修改标识为触发
            //获得变种关键词的位置
            int subStartVariationIndex = contentVariation.indexOf(keywordsVariation);

            int subStartIndex = 0;
            for (int j = 0; j < contentWordIndexList.size(); j++) {
                Integer strIndex = contentWordIndexList.get(j);
                //变种关键词位置，大于等于字体位置时，则为这个字体
                if (strIndex > subStartVariationIndex) {
                    subStartIndex = j - 1;
                    break;
                }
            }

            //截取主要违规内容
            String matchText = StringUtils.substring(contentFormat, subStartIndex, subStartIndex + matchContentLength);

            //存储主要变种内容
            String matchVariation = StringUtils.substring(contentVariation, subStartVariationIndex, subStartVariationIndex + matchContentLength * 3);

            //变种匹配构建
            keywordsTriggerDetailDO = new KeywordsTriggerDetailDO(
                    baseModelContent,
                    baseModelId,
                    contentType,
                    wordDO.getId(),
                    wordDO.getTextShow(),
                    matchText,
                    wordDO.getVariationText(),
                    matchVariation
            );
        }
        return keywordsTriggerDetailDO;
    }
}
