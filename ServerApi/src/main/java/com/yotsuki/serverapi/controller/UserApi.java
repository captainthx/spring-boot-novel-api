package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.AddressRequest;
import com.yotsuki.serverapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserApi {
    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        return userService.findAll();
    }

    @GetMapping()
    public ResponseEntity<?> findByUid(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return userService.findById(userDetailsImp);
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAddressByUid(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return userService.findAddressByUid(userDetailsImp);
    }

    @PostMapping("/address")
    public ResponseEntity<?> createAddress(@AuthenticationPrincipal UserDetailsImp userDetailsImp, @RequestBody AddressRequest request){
        return userService.createAddress(userDetailsImp,request);
    }
}
