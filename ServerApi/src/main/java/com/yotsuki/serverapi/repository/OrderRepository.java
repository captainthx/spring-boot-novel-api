package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.Order;
import com.yotsuki.serverapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUidAndStatus(Long uid, String status);
}
