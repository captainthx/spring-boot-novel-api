package com.yotsuki.serverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "loginLogs")
@Data
public class HistoryLogin extends BaseEntity {

    @Column(nullable = false, length = 32)
    private String ipv4;
    @JsonIgnore
    @Column(nullable = false)
    private String userAgent;
    @Column(nullable = false, length = 7)
    private String device;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long uid;
}
