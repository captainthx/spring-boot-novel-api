package com.yotsuki.excommon.common;

import com.yotsuki.excommon.model.BaseResponse;
import com.yotsuki.excommon.model.PaginationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
public class Response {

    public static <E>ResponseEntity<?> success() {
        return success(null);
    }

    public static <E> ResponseEntity<?> success(E result){
        ResponseCode code = ResponseCode.SUCCESS;
        BaseResponse<?> response = BaseResponse.builder()
                .code(code.getValue())
                .text(code)
                .result(result)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<?> successList(Page<T> result) {
        PaginationResponse pagination = PaginationResponse.builder()
                .limit(result.getPageable().getPageSize())
                .current(result.getPageable().getPageNumber() + 1)
                .records((int) result.getTotalElements())
                .pages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
        ResponseCode code = ResponseCode.SUCCESS;
        BaseResponse<?> response = BaseResponse.builder()
                .code(code.getValue())
                .result(result.getContent())
                .text(code)
                .pagination(pagination)
                .build();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<?> error(ResponseCode code) {
        BaseResponse<?> response = BaseResponse.builder()
                .code(code.getValue())
                .text(code)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> unauthorized() {
        ResponseCode code = ResponseCode.UNAUTHORIZED;
        BaseResponse<?> response = BaseResponse.builder()
                .code(code.getValue())
                .text(code)
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> unknown() {
        ResponseCode code = ResponseCode.UNKNOWN;
        BaseResponse<?> response = BaseResponse.builder()
                .code(code.getValue())
                .text(code)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
