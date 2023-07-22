package com.yotsuki.serverapi.model.request;

import lombok.Data;

@Data
public class OrderRequest {
    private Long bookId;
    private String name;
    private Long price;
    private String status;
}
