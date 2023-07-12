package com.yotsuki.serverapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "favorite")
@Data

public class Favorite extends BaseEntity {
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
