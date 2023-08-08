package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.OrderListRequest;
import com.yotsuki.serverapi.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderApi {

    private final OrderService orderService;

    public OrderApi(OrderService orderService) {
        this.orderService = orderService;
    }



    @PostMapping()
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetailsImp userDetailsImp, @RequestBody OrderListRequest request){
        return orderService.create(userDetailsImp,request);
    }

    @GetMapping()
    public ResponseEntity<?> findOrderByUid(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return orderService.getOrderByUid(userDetailsImp);
    }

    // update orderstatus
   @PatchMapping
    public ResponseEntity<?> updateOrderStatus(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return orderService.updateOrderStatus(userDetailsImp);
    }
}
