package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.BankPayment;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.BankPaymentRequest;
import com.yotsuki.serverapi.model.response.HistoryPaymentResponse;
import com.yotsuki.serverapi.repository.BankPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BackPaymentService {
    private final BankPaymentRepository bankPaymentRepository;
    private final String imgPath = "C:/api/img/slip";

    //    private final String imgPath = "/npm/api/img/" ;
    public BackPaymentService(BankPaymentRepository bankPaymentRepository) {
        this.bankPaymentRepository = bankPaymentRepository;
    }

    // create transaction
    public ResponseEntity<?> createTransaction(MultipartFile file, UserDetailsImp userDetailsImp, BankPaymentRequest request) throws IOException, ParseException {
        // validate file
        if (file == null) {
            log.debug("[book] file is null!::{}", file);
            return Response.error(ResponseCode.INVALID_IMAGE);
        }
        if (file.getSize() > 1048576 * 2) {
            log.debug("[book] file size is max size::{}", file.getSize());
            return Response.error(ResponseCode.MAX_IMAGE_SIZE);
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            log.debug("[book] contentType is null!::{}", contentType);
            return Response.error(ResponseCode.INVALID_IMAGE_TYPE);
        }

        List<String> supportType = Arrays.asList("image/png", "image/jpeg", "image/jpg");
        if (!supportType.contains(contentType)) {
            log.info("[book] contentType is not support!::{}", contentType);
            return Response.error(ResponseCode.INVALID_IMAGE_TYPE);
        }

        if (file.isEmpty()) {
            return Response.error(ResponseCode.INVALID_IMAGE);
        }

        String fileName = file.getOriginalFilename();
        this.initDirectory();
        Path path = Paths.get(this.imgPath, fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        //set to entity
        BankPayment entity = new BankPayment();
        entity.setNameAccount(request.getNameAccount());
        entity.setSlipName(fileName);
        entity.setTransferDate(request.getTransferDate());
        entity.setUser(User.getUser(userDetailsImp));
        this.bankPaymentRepository.save(entity);

        return Response.success();
    }


    public ResponseEntity<?> findTransactionHistory(UserDetailsImp userDetailsImp) {
        List<HistoryPaymentResponse> bankPaymentList = this.bankPaymentRepository.findByUid(userDetailsImp.getId()).stream().map(this::build).collect(Collectors.toList());

        return Response.success(bankPaymentList);
    }


    private HistoryPaymentResponse build(BankPayment bankPayment){
        return HistoryPaymentResponse.builder()
                .id(bankPayment.getId())
                .transferDate(bankPayment.getTransferDate())
                .cdt(bankPayment.getCdt())
                .build();
    }
    private void initDirectory() {
        File dir = new File(this.imgPath);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.canWrite();
            dir.canRead();
        }
    }
}
