package com.qingchi.base.redis;

import com.qingchi.base.constant.CommonConst;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.model.system.DistrictDO;
import com.qingchi.base.repository.district.DistrictRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DistrictRedis {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private DistrictRepository districtRepository;

    @Cacheable(cacheNames = "districtByAdCode", key = "#adCode")
    public Optional<DistrictDO> findFirstOneByAdCode(String adCode) {
        return districtRepository.findFirstOneByAdCode(adCode);
    }

    /**
     * 查询热门下包含子节点，和所有省份不包含子节点
     *
     * @return
     */
    @Cacheable(cacheNames = "districtsHot")
    public List<DistrictVO> getHotDistricts() {
        List<DistrictVO> list = new ArrayList<>();
        list.add(getHotDistrict());
        //获取省，不包含子节点
        list.addAll(DistrictVO.districtDOToVOS(getChinaProvinces()));
        return list;
    }

    /**
     * 显示全部的时候，热门+全部省份包含子节点
     *
     * @return
     */
    @Cacheable(cacheNames = "districtsAll")
    public List<DistrictVO> getAllDistricts() {
        List<DistrictVO> list = new ArrayList<>();
        list.add(getHotDistrict());

        //获取热门和热门的子节点
        //查出来所有，平级结构，按城市编码排序，省在市前，市在区县前
        List<DistrictDO> districtDOS = getChinaProvinces();
        //递归设置子节点
        list.addAll(recurseSetChild(districtDOS));

        return list;
    }


    //递归设置child
    private List<DistrictVO> recurseSetChild(List<DistrictDO> districts) {
        List<DistrictVO> districtVOS = new ArrayList<>();
        //遍历转vo设置子节点
        for (DistrictDO district : districts) {
            DistrictVO districtVO = new DistrictVO(district);
            //如果街道为空设置子节点
            if (StringUtils.isEmpty(district.getDistrictName())) {
                List<DistrictDO> districtDOS = getByParentAdCode(district.getAdCode());
                districtVO.setChilds(this.recurseSetChild(districtDOS));
            }
            districtVOS.add(districtVO);
        }
        return districtVOS;
    }

    /**
     * 获取热门下的子节点获，取前20热门市区
     *
     * @return
     */
    private DistrictVO getHotDistrict() {
        //获取热门和热门的子节点
        DistrictVO hotDistrict = new DistrictVO();
        hotDistrict.setAdName("热门");
        hotDistrict.setProvinceName("中国");
        hotDistrict.setAdCode("999999");
        List<DistrictDO> districtDOS = districtRepository.findTop20ByDistrictCodeAndStatusOrderByCountDesc("1", CommonStatus.enable);

        hotDistrict.setChilds(recurseSetChild(districtDOS));
        return hotDistrict;
    }


    /**
     * 根据父节点获取子节点，内部不包含子节点
     *
     * @param parentAdCode
     * @return
     */
    private List<DistrictDO> getByParentAdCode(String parentAdCode) {
        return districtRepository.findByParentAdCodeAndStatusOrderByAdCode(parentAdCode, CommonStatus.enable);
    }

    private List<DistrictDO> getChinaProvinces() {
        return districtRepository.findByParentAdCodeAndStatusOrderByAdCode(CommonConst.chinaDistrictCode, CommonStatus.enable);
    }
}