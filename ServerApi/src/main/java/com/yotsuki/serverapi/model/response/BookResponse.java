package com.yotsuki.serverapi.model.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BookResponse  {
    private Long id;
    private String name;
    private String type;
    private String synopsis;
    private String imageName;
}
