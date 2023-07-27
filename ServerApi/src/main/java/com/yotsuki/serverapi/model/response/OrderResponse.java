package com.yotsuki.serverapi.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long bookId;
    private String name;
    private Integer quantity;
    private Long price;
    private String status;
}
