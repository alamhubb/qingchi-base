package com.qingchi.base.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qingchi.base.constant.CommonStatus;
import com.qingchi.base.constant.ShellOrderType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "shell_order")
@Entity
public class ShellOrderDO implements Serializable {
    //必有
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    //本单贝壳数量，可能是正值，也可能是负值
    private Integer shell;
    //绝对值，
    private Integer absoluteShell;

    private Date createTime;
    private Date updateTime;

    //用户状态，暂未使用正常，封禁
    private String status;

    private String type;

    private Integer relatedOrderId;

    private Integer vipOrderId;

    private Integer userContactId;

    public ShellOrderDO() {
    }

    public ShellOrderDO(Integer userId, Integer shell, String type) {
        this.userId = userId;
        this.shell = shell;
        this.absoluteShell = Math.abs(shell);
        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.status = CommonStatus.normal;
        this.type = type;
    }

    public ShellOrderDO(Integer userId, Integer shell, String type, Integer userContactId) {
        this.userId = userId;
        this.shell = shell;
        this.absoluteShell = Math.abs(shell);
        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.status = CommonStatus.normal;
        this.type = type;
        this.userContactId = userContactId;
    }

    public ShellOrderDO(Integer userId, Integer shell, Integer userContactId, Integer relatedOrderId) {
        this(userId, shell, ShellOrderType.receive, userContactId);
        this.relatedOrderId = relatedOrderId;
// todo       relatedOrder.relatedOrder = this;
    }

    public ShellOrderDO(Integer userId, Integer shell, Integer vipOrderId) {
        this(userId, shell, ShellOrderType.recharge);
        this.vipOrderId = vipOrderId;
    }
}