package com.yotsuki.excommon.utils;

import com.yotsuki.excommon.model.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;


public class Comm {
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

    public static Pageable getPaginate(Pagination paginate) {
        return PageRequest.of(paginate.getPage() > 0 ? paginate.getPage() - 1 : 0, paginate.getLimit(), Sort.Direction.fromString(paginate.getSortType()), paginate.getSortBy());
    }

    public static String getDeviceType(String userAgent) {
        String deviceType;

        String iPhone = "iPhone";
        String Android = "Android";
        String Windows = "Windows";
        String Unknown = "Unknown";

        if (userAgent.contains(iPhone)) {
            deviceType = iPhone;
        } else if (userAgent.contains(Android)) {
            deviceType = Android;
        } else if (userAgent.contains(Windows)) {
            deviceType = Windows;
        } else {
            deviceType = Unknown;
        }

        return deviceType;
    }
}
