package com.yotsuki.serverapi.model.request;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class BankPaymentRequest {
    private String nameAccount;
    private Long transferDate;


    public static BankPaymentRequest getJson(String request) {

        BankPaymentRequest bankPaymentRequest;

        try {
            ObjectMapper mapper = new ObjectMapper();
            bankPaymentRequest = mapper.readValue(request, BankPaymentRequest.class);
            return bankPaymentRequest;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
