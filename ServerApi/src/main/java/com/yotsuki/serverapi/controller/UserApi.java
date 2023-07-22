package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.AddressRequest;
import com.yotsuki.serverapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@Slf4j
class UserApi {
private final UserService userService;

    UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PostMapping("/address")
    public ResponseEntity<?> createAddress(@AuthenticationPrincipal UserDetailsImp userDetailsImp, @RequestBody AddressRequest request){
        return userService.createAddress(userDetailsImp,request);
    }
}
