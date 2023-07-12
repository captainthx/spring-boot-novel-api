package com.yotsuki.excommon.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yotsuki.excommon.common.ResponseCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<E> {
    private Integer code;
    private ResponseCode text;
    private E result;
    private PaginationResponse pagination;
}
