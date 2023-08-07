package com.yotsuki.serverapi.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponse {
    private String fullName;
    private String address;
    private Integer phone;
    private LocalDateTime cdt;
}
