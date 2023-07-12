package com.yotsuki.serverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "loginLogs")
@Data
 public class HistoryLogin extends BaseEntity  {
    @JsonIgnore
    @Column(nullable = false)
    private Long uid;
    @Column(nullable = false, length = 32)
    private String ipv4;
    @JsonIgnore
    @Column(nullable = false)
    private String userAgent;
    @Column(nullable = false, length = 7)
    private String device;
    
}
