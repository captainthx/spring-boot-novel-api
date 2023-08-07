package com.yotsuki.serverapi.model.request;

import lombok.Data;

@Data
public class DeliveryRequest {
    private Long addressId;
    private String fullName;
    private Integer phone;
}
