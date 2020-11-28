package com.qingchi.base.repository.user;

import com.qingchi.base.model.user.UserDistrictDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDistrictRepository extends JpaRepository<UserDistrictDO, Integer> {
    List<UserDistrictDO> findTop7ByUserIdOrderByUpdateTimeDesc(Integer userId);

    Optional<UserDistrictDO> findFirstByUserIdAndDistrictIdOrderByIdDesc(Integer userId, Integer districtId);
}