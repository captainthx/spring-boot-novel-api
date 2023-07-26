package com.yotsuki.serverapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity(name ="book" )
@Data
public class Book extends BaseEntity  {
    private String name;
    private String type;
    @Lob
    private String synopsis;
    @Lob
    private String content;
    private Long price;
    private String imageName;

}
