package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Order;
import com.yotsuki.serverapi.model.request.OrderRequest;
import com.yotsuki.serverapi.model.response.OrderResponse;
import com.yotsuki.serverapi.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public ResponseEntity<?> create(UserDetailsImp userDetailsImp, OrderRequest request) {

        if (Objects.isNull(request.getBookId())) {
            log.warn("[order] (block) invalid bookId: {} ", request);
            return Response.error(ResponseCode.INVALID_BOOK_ID);
        }
        if (Objects.isNull(request.getName())) {
            log.warn("[order] (block) invalid name: {} ", request);
            return Response.error(ResponseCode.INVALID_BOOK_NAME);
        }
        if (Objects.isNull(request.getPrice())) {
            log.warn("[order] (block) invalid price: {} ", request);
            return Response.error(ResponseCode.INVALID_BOOK_PRICE);
        }
        if (Objects.isNull(request.getStatus())) {
            log.warn("[order] (block) invalid status: {} ", request);
            return Response.error(ResponseCode.INVALID_STATUS);
        }

        //save to entity
        Order entity = new Order();
        entity.setUserId(userDetailsImp.getId());
        entity.setBookId(request.getBookId());
        entity.setName(request.getName());
        entity.setPrice(request.getPrice());
        entity.setStatus(request.getStatus());

        //save to db
        Order resOrder = orderRepository.save(entity);

        return Response.success(response(resOrder));
    }




    public OrderResponse response(Order order) {
        return OrderResponse.builder()
                .userId(order.getUserId())
                .bookId(order.getBookId())
                .name(order.getName())
                .price(order.getPrice())
                .status(order.getStatus())
                .build();
    }

}
