package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author qinkaiyuan
 * @date 2019-02-19 22:27
 */
@Entity
@Table(name = "token")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TokenDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tokenCode;

    private Integer userId;

    private Date createDate;

    public TokenDO() {
    }

    public TokenDO(String tokenCode, Integer userId) {
        this.tokenCode = tokenCode;
        this.userId = userId;
        this.createDate = new Date();
    }
}
