package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.BankPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankPaymentRepository extends JpaRepository<BankPayment,Long> {
    List<BankPayment> findByUid(Long uid);
}
