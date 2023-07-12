package com.yotsuki.serverapi.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {
    private Long bookId;
    private Long userId;
}
