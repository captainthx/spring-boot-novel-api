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

    public ResponseEntity<?> getOrderByUid(UserDetailsImp userDetailsImp) {
        List<OrderResponse> orderList = this.orderRepository.findByUid(userDetailsImp.getId()).stream().map(this::response).collect(Collectors.toList());
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
