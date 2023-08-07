package com.yotsuki.serverapi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yotsuki.boot.configJwt.UserDetailsImp;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "user")
@Data
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Address> addresses;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HistoryLogin> historyLogins;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Delivery> deliveries;


    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BankPayment> bankPayments;


    public static User getUser(UserDetailsImp userDetailsImp) {
        User user = new User();
        user.setId(userDetailsImp.getId());
        return user;
    }
}
