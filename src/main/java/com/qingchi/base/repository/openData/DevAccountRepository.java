package com.qingchi.base.repository.openData;

import com.qingchi.base.model.openData.ContentUnionIdDO;
import com.qingchi.base.model.openData.DevAccountDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author qinkaiyuan
 * @since 1.0.0
 */
public interface DevAccountRepository extends JpaRepository<DevAccountDO, Integer> {

    Optional<DevAccountDO> findFirstBySecretKey(String secretKey);
}