package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.response.AddressResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByUser(User user);
}
