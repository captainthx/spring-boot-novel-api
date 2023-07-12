package com.yotsuki.excommon.model;

import lombok.Data;

@Data
public class Pagination {
    private Integer page = 0;
    private Integer limit = 10;
    private String sortType = "asc";
    private String sortBy = "id";
}
