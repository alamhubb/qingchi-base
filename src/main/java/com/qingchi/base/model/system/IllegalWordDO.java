package com.qingchi.base.model.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @author qinkaiyuan
 * @date 2019-12-22 18:46
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
/**
 * 全局配置表
 */
@Table(name = "illegal_word", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"word"})
})
public class IllegalWordDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String word;
    private String cause;
    private String status;
}
