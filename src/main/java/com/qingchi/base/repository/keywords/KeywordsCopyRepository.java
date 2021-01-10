package com.qingchi.base.repository.keywords;

import com.qingchi.base.model.system.KeywordsCopyDO;
import com.qingchi.base.model.system.KeywordsDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
public interface KeywordsCopyRepository extends JpaRepository<KeywordsCopyDO, Integer> {

}


