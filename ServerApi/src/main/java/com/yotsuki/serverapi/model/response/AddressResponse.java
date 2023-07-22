package com.yotsuki.serverapi.model.response;

import com.yotsuki.serverapi.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private String line1;
    private String line2;
    private String zipCode;
    private User user;
}
