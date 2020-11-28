package com.qingchi.base.model.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2019-12-22 18:46
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
/**
 * 违规单词表
 */
@Table(name = "keywords", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"text"})
})
public class KeywordsDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //关键词主体，会变为大写
    private String text;
    //仅展示，保存录入的内容
    private String textShow;
    //会去除空格和变为大写
    private String variationText;
    //仅用来展示
    private String variationTextShow;

    private String status;

    @Column(columnDefinition = "bit default true")
    private Boolean openText;
    @Column(columnDefinition = "bit default true")
    private Boolean openVariation;

    //关键词变种
    private String cause;

    private String deleteCause;

    @Column(columnDefinition = "int default 0")
    private Integer totalNum;
    @Column(columnDefinition = "int default 0")
    private Integer violateNum;
    @Column(columnDefinition = "int default 0")
    private Integer normalNum;
    @Column(columnDefinition = "double default 0")
    private Double violateRatio;
    @Column(columnDefinition = "double default 0")
    private Double normalRatio;

    @Column(columnDefinition = "int default 0")
    private Integer textTotalNum;
    @Column(columnDefinition = "int default 0")
    private Integer textViolateNum;
    @Column(columnDefinition = "double default 0")
    private Double textViolateRatio;
    @Column(columnDefinition = "int default 0")
    private Integer textNormalNum;
    @Column(columnDefinition = "double default 0")
    private Double textNormalRatio;


    @Column(columnDefinition = "int default 0")
    private Integer variationTotalNum;
    @Column(columnDefinition = "int default 0")
    private Integer variationViolateNum;
    @Column(columnDefinition = "double default 0")
    private Double variationViolateRatio;
    @Column(columnDefinition = "int default 0")
    private Integer variationNormalNum;
    @Column(columnDefinition = "double default 0")
    private Double variationNormalRatio;


}
