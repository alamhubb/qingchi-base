package com.qingchi.base.repository.user;

import com.qingchi.base.model.account.AuthenticationDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author qinkaiyuan
 * @date 2018-10-17 21:59
 */
public interface AuthenticationRepository extends JpaRepository<AuthenticationDO, Integer> {
    Optional<AuthenticationDO> findFirstByPhoneNumOrderByCreateTimeDescIdAsc(String phoneNum);

    Integer countByPhoneNum(String phoneNum);

    Integer countByIp(String ip);

    Integer countByUserId(Integer userId);
}


