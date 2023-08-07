package com.yotsuki.serverapi.controller;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.serverapi.model.request.BankPaymentRequest;
import com.yotsuki.serverapi.service.BackPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/v1/bank/payment")
public class BankPaymentApi {

    private final BackPaymentService backPaymentService;

    public BankPaymentApi(BackPaymentService backPaymentService) {
        this.backPaymentService = backPaymentService;
    }

    @PostMapping()
    public ResponseEntity<?> transaction(MultipartFile file, @AuthenticationPrincipal UserDetailsImp userDetailsImp, String request) throws IOException, ParseException {
        BankPaymentRequest bankPaymentRequest = BankPaymentRequest.getJson(request);
        return backPaymentService.createTransaction(file, userDetailsImp, bankPaymentRequest);
    }

    @GetMapping()
    public ResponseEntity<?>history(@AuthenticationPrincipal UserDetailsImp userDetailsImp){
        return backPaymentService.findTransactionHistory(userDetailsImp);
    }

}
