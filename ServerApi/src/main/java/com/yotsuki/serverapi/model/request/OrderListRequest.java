package com.yotsuki.serverapi.model.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderListRequest {
      private List<OrderRequest> orders;
}
