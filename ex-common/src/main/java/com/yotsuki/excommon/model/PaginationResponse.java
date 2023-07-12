package com.yotsuki.excommon.model;


import lombok.Builder;
import lombok.Data;


/**
 * {
 *     "current": xxxx,
 *     "limit": xxxx,
 *     "records": xxxx,
 *     "pages": xxxx,
 *     "first": xxxx,
 *     "last": xxxx,
 * }
 */
@Data
@Builder
public class PaginationResponse {
    private Integer current;
    private Integer limit;
    private Integer records;
    private Integer pages;
    private boolean first;
    private boolean last;

}
