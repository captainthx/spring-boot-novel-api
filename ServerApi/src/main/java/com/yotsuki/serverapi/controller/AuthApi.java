package com.yotsuki.serverapi.controller;


import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.LoginRequest;
import com.yotsuki.serverapi.model.request.RegisterRequest;
import com.yotsuki.serverapi.model.request.TokenRefreshRequest;
import com.yotsuki.serverapi.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AuthApi {

    private final AuthService authService;

    AuthApi(AuthService authService) {
        this.authService = authService;
    }

    // register
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // login
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authService.login(request, httpRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }

}
