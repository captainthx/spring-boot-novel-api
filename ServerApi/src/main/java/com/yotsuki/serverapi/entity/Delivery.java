package com.yotsuki.serverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "delivery")
@Data
public class Delivery extends BaseEntity{
    @Column(nullable = false,length = 50)
    private String fullName;
    @Lob
    @Column(nullable = false)
    private String address;
    @Column(length = 10, nullable = false)
    private Integer phone;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long uid;
}
