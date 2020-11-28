package com.qingchi.base.redis;

import com.qingchi.base.model.system.DistrictDO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinkaiyuan
 * @date 2019-10-30 14:08
 */
@Data
public class DistrictVO {
    private Integer id;
    //省
    private String provinceName;
    //市
    private String cityName;
    //区县
    private String districtName;
    //统一标识
    private String adCode;
    private String adName;
    private Integer talkCount;
    private Integer count;
    private Double lon;
    private Double lat;
    private List<DistrictVO> childs;

    public DistrictVO() {
    }

    public DistrictVO(DistrictDO district) {
        this.id = district.getId();
        this.provinceName = district.getProvinceName();
        this.cityName = district.getCityName();
        this.districtName = district.getDistrictName();
        this.adCode = district.getAdCode();
        this.adName = district.getAdName();
        this.count = district.getCount();
        this.talkCount = district.getTalkCount();
        //后台查询出来的所有均不为附近，只有前台写死的才是附近
//        this.使用附近 = CommonStatus.unUseNearbyNum;
        //后台查询出来的所有均不为附近，只有前台写死的才是附近
//        this.childs = new ArrayList<>();
    }


    public static List<DistrictVO> districtDOToVOS(List<DistrictDO> DOs) {
        return DOs.stream().map(DistrictVO::new).collect(Collectors.toList());
    }
}
