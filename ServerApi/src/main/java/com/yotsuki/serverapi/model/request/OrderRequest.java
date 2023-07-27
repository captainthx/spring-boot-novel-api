package com.yotsuki.serverapi.model.request;

import lombok.Data;

@Data
public class OrderRequest {
    private Long bookId;
    private String name;
    private Integer quantity;
    private Long price;
    private String status;
}
