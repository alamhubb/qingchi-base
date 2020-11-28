package com.qingchi.base.repository.user;

import com.qingchi.base.model.user.IdCardDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdCardRepository extends JpaRepository<IdCardDO, Integer> {
    List<IdCardDO> findByUserIdAndStatus(Integer userId, String status);
}

