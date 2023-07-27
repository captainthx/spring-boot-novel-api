package com.yotsuki.excommon.common;

import java.util.Optional;

public enum ResponseCode {
    SUCCESS(0),
    INVALID_REQUEST(210),
    INVALID_EMAIL(211),
    INVALID_USERNAME(212),
    INVALID_PASSWORD(213),
    INVALID_NEW_PASSWORD(214),
    INVALID_CODE(215),
    INVALID_UID(216),
    INVALID_USERNAME_PASSWORD(217),
    PASSWORD_NOT_MATCH(218),
    INVALID_IMAGE(219),
    INVALID_IMAGE_TYPE(220),
    INVALID_BOOK_TYPE(221),
    INVALID_BOOK_SYNOPSIS(222),
    INVALID_BOOK_ID(223),
    INVALID_BOOK_NAME(224),
    INVALID_BOOK_CONTENT(225),
    INVALID_BOOK_PRICE(226),
    INVALID_ADDRESS_LINE(227),
    INVALID_ADDRESS_ZIPCODE(228),
    INVALID_STATUS(229),
    INVALID_BOOK_QUANTITY(230),
    ADDRESS_FULL(230),
    EXISTED_EMAIL(310),
    EXISTED_USERNAME(311),
    MAX_IMAGE_SIZE(312),
    NOT_FOUND(410),
    NO_DATA(411),
    TOKEN_EXPIRE(510),
    REFRESH_TOKEN_EXPIRE(511),

    UNAUTHORIZED(888),
    UNKNOWN(999);


    private final Integer value;

    ResponseCode( final Integer code) {
        this.value = code;
    }

public Integer getValue(){return value;}


    public static Optional<ResponseCode> find(Integer code){
        for (ResponseCode valuse : ResponseCode.values()){
            if (valuse.value.equals(code))
                return Optional.of(valuse);
        }
        return Optional.empty();
    }
}
