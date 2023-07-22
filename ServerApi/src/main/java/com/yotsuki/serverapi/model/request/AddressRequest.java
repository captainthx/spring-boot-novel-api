package com.yotsuki.serverapi.model.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String line1;
    private String line2;
    private String zipCode;
}
