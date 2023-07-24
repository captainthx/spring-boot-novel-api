package com.yotsuki.serverapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "book_order")
@Data
public class Order extends BaseEntity {
    private Long bookId;
    private String name;
    private Long price;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long uid;

}
