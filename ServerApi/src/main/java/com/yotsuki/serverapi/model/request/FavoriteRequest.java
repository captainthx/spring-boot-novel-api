package com.yotsuki.serverapi.model.request;

import lombok.Data;

@Data
public class FavoriteRequest {
    private Long bookId;
    private Long userId;
}
