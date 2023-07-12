package com.yotsuki.serverapi.model.request;

import lombok.Data;

/**
 * {
 *     "refreshToken": "xxxx"
 * }
 */
@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
