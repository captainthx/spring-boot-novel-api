package com.yotsuki.serverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
@EqualsAndHashCode(callSuper = true)
@Entity(name = "bank_payment")
@Data
public class BankPayment extends BaseEntity{
    private String nameAccount;
    private Long transferDate;
    private String slipName;
    private String status;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long uid;
}
