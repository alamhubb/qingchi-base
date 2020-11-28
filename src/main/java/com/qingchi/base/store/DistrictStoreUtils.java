package com.qingchi.base.store;

import com.qingchi.base.model.system.DistrictDO;
import com.qingchi.base.redis.DistrictRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

@Repository
public class DistrictStoreUtils {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static DistrictRedis districtRedis;

    @Resource
    public void setDistrictRedis(DistrictRedis districtRedis) {
        DistrictStoreUtils.districtRedis = districtRedis;
    }

    public static DistrictDO findFirstOneByAdCode(String adCode) {
        Optional<DistrictDO> optionalDistrictDO = districtRedis.findFirstOneByAdCode(adCode);
        return optionalDistrictDO.orElse(null);
    }

}