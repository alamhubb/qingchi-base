package com.qingchi.base.repository.keywords;

import com.qingchi.base.model.system.KeywordsDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
public interface KeywordsRepository extends JpaRepository<KeywordsDO, Integer> {

    Optional<KeywordsDO> findTopOneByText(String word);

    List<KeywordsDO> findAllByStatusIsNull();

    //状态启用的，且文本违规率大于的，且开启了文本的
    List<KeywordsDO> findAllByStatusIsNullAndTextViolateRatioGreaterThanAndOpenTextTrue(Double violateRatio);

    List<KeywordsDO> findAllByStatusIsNullAndTotalNumGreaterThanOrderByTextViolateRatioDesc(Integer num);

    List<KeywordsDO> findTop100ByStatusIsNullOrderByViolateNumDesc();

    List<KeywordsDO> findTop100ByStatusIsNullOrderByTotalNumDesc();

    List<KeywordsDO> findTop100ByStatusIsNullOrderByViolateRatioDesc();

    List<KeywordsDO> findTop100ByStatusIsNullAndTotalNumGreaterThanOrderByViolateRatioDesc(Integer num);

}


