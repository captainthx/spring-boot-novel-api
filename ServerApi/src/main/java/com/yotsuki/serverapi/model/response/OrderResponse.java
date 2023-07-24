package com.yotsuki.serverapi.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long uid;
    private Long bookId;
    private String name;
    private Long price;
    private String status;
}
