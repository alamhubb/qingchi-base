package com.qingchi.base.model.talk;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.model.system.DistrictDO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "position")
public class PositionDO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 评论的哪个用户
     */
    private Integer userId;

    private String adCode;
    private String adName;
    private String cityName;

    private Integer districtId;

    private Date createTime;
    /*
     *  经度 Longitude 简写Lng
     * 纬度范围-90~90，经度范围-180~180
     */
    private Double lon;
    /*
     * 纬度 Latitude 简写Lat
     */
    private Double lat;

    public PositionDO() {
    }

    public PositionDO(Integer userId, DistrictDO districtDO, Double lon, Double lat) {
        this.userId = userId;
        this.adCode = districtDO.getAdCode();
        this.adName = districtDO.getAdName();
        this.cityName = districtDO.getCityName();
        this.districtId = districtDO.getId();
        this.createTime = new Date();
        this.lon = lon;
        this.lat = lat;
    }
}
