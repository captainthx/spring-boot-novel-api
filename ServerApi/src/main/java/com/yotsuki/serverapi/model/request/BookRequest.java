package com.yotsuki.serverapi.model.request;

import com.yotsuki.serverapi.entity.BaseEntity;
import com.yotsuki.serverapi.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;



@Data
public class BookRequest {
    private String name;
    private String type;
    private String synopsis;
    private String content;
    private Long price;
}
