package com.qingchi.base.repository.openData;

import com.qingchi.base.model.openData.ContentUnionIdDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author qinkaiyuan
 * @since 1.0.0
 */
public interface ContentUnionIdRepository extends JpaRepository<ContentUnionIdDO, Long> {

    Optional<ContentUnionIdDO> findFirstByUnionId(String unionId);
}