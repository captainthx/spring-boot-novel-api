package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.AddressRequest;
import com.yotsuki.serverapi.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/address")
public class AddressApi {


    private final AddressService addressService;

    public AddressApi(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping()
    public ResponseEntity<?> getAddressByUid(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return addressService.findAddressByUid(userDetailsImp);
    }

    @PostMapping()
    public ResponseEntity<?> createAddress(@AuthenticationPrincipal UserDetailsImp userDetailsImp, @RequestBody AddressRequest request){
        return addressService.createAddress(userDetailsImp,request);
    }
}
