package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUid(Long uid);
}
