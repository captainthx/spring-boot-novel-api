package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Order;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.OrderListRequest;
import com.yotsuki.serverapi.model.response.OrderResponse;
import com.yotsuki.serverapi.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


//    public ResponseEntity<?> create(UserDetailsImp userDetailsImp, OrderRequest request) {
//
//        if (Objects.isNull(request.getBookId())) {
//            log.warn("order::(block) invalid bookId:{}, udi:{} ", request, userDetailsImp.getId());
//            return Response.error(ResponseCode.INVALID_BOOK_ID);
//        }
//        if (Objects.isNull(request.getName())) {
//            log.warn("order::(block) invalid name:{}, udi:{}  ", request, userDetailsImp.getId());
//            return Response.error(ResponseCode.INVALID_BOOK_NAME);
//        }
//        if (Objects.isNull(request.getPrice())) {
//            log.warn("order::(block) invalid price:{}, udi:{}  ", request, userDetailsImp.getId());
//            return Response.error(ResponseCode.INVALID_BOOK_PRICE);
//        }
//        if (Objects.isNull(request.getQuantity())) {
//            log.warn("order::(block) invalid quantity:{}, udi:{}  ", request, userDetailsImp.getId());
//            return Response.error(ResponseCode.INVALID_BOOK_QUANTITY);
//        }
//        if (Objects.isNull(request.getStatus())) {
//            log.warn("order::(block) invalid status:{}, udi:{}  ", request, userDetailsImp.getId());
//            return Response.error(ResponseCode.INVALID_STATUS);
//        }
//        //set foreign key
//        User userOptional = new User();
//        userOptional.setId(userDetailsImp.getId());
//        //save to entity
//        Order entity = new Order();
//        entity.setUser(userOptional);
//        entity.setBookId(request.getBookId());
//        entity.setName(request.getName());
//        entity.setPrice(request.getPrice());
//        entity.setQuantity(request.getQuantity());
//        entity.setStatus(request.getStatus());
//
//        //save to db
//        Order resOrder = orderRepository.save(entity);
//
//        return Response.success(response(resOrder));
//    }


    public ResponseEntity<?> create(UserDetailsImp userDetailsImp, OrderListRequest request) {

        if (Objects.isNull(request.getOrders())) {
            log.warn("order::(block) invalid orders:{}, uid:{}  ", request, userDetailsImp.getId());
            return Response.error(ResponseCode.INVALID_ORDER_LIST);
        }

        List<Order> orders = request.getOrders().stream().map(order -> {
            User user = new User();
            user.setId(userDetailsImp.getId());
            Order entity = new Order();
            entity.setUser(user);
            entity.setBookId(order.getBookId());
            entity.setName(order.getName());
            entity.setPrice(order.getPrice());
            entity.setQuantity(order.getQuantity());
            entity.setStatus(order.getStatus());
            return entity;
        }).collect(Collectors.toList());

        this.orderRepository.saveAll(orders);

        return Response.success();
    }

    public ResponseEntity<?> getOrderByUid(UserDetailsImp userDetailsImp, String status) {
        if (Objects.isNull(status)) {
            log.warn("order::(block) invalid status:{}, uid:{}  ", status, userDetailsImp.getId());
            return Response.error(ResponseCode.INVALID_STATUS);
        }
        List<OrderResponse> orderList = this.orderRepository.findByUidAndStatus(userDetailsImp.getId(), status).stream().map(this::response).collect(Collectors.toList());
        return Response.success(orderList);
    }

    public ResponseEntity<?> updateOrderStatus(UserDetailsImp userDetailsImp) {
        List<Order> orderList = this.orderRepository.findByUid(userDetailsImp.getId()).stream().map(orders ->{
            orders.setStatus("paid");
            return orders;
        }).collect(Collectors.toList());
        this.orderRepository.saveAll(orderList);
        return Response.success();
    }

    public OrderResponse response(Order order) {
        return OrderResponse.builder()
                .bookId(order.getBookId())
                .name(order.getName())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();
    }

}
