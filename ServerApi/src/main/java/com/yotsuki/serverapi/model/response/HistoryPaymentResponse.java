package com.yotsuki.serverapi.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Builder
@Data
public class HistoryPaymentResponse {
    private Long id;
    private String status;
    private Long transferDate;
    private LocalDateTime cdt;
}
