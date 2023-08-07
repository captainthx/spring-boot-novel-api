package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.DeliveryRequest;
import com.yotsuki.serverapi.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/delivery")
public class DeliveryApi {
    private final DeliveryService deliveryService;

    public DeliveryApi(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    @PostMapping
    public ResponseEntity<?> createDelivery(@AuthenticationPrincipal UserDetailsImp userDetailsImp , @RequestBody DeliveryRequest request){
        return deliveryService.createDelivery(userDetailsImp,request);
    }
    @GetMapping
    public ResponseEntity<?> findHistory(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return deliveryService.historyDelivery(userDetailsImp);
    }
}
