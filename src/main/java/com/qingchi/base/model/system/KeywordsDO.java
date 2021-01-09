package com.qingchi.base.model.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.promeg.pinyinhelper.Pinyin;
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
    private String reopenCause;
    private String closeTextCause;
    private String reopenTextCause;
    private String closeVariationCause;
    private String reopenVariationCause;

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

    //do必须有空的构造函数
    public KeywordsDO() {}

    public KeywordsDO(String content) {
        this.setTextShow(content);
        this.setText(content.toUpperCase());

        String contentVariation = Pinyin.toPinyin(content, " ");
        this.setVariationTextShow(contentVariation);
        this.setVariationText(contentVariation.replaceAll(" ", "").toUpperCase());
        this.setCause(cause);

        this.setOpenText(true);
        this.setOpenVariation(true);

        this.setTotalNum(0);
        this.setNormalNum(0);
        this.setNormalRatio(0.0);
        this.setViolateRatio(0.0);
        this.setViolateNum(0);

        this.setTextTotalNum(0);
        this.setTextNormalNum(0);
        this.setTextNormalRatio(0.0);
        this.setTextViolateRatio(0.0);
        this.setTextViolateNum(0);

        this.setVariationTotalNum(0);
        this.setVariationNormalNum(0);
        this.setVariationNormalRatio(0.0);
        this.setVariationViolateRatio(0.0);
        this.setVariationViolateNum(0);
    }
}
