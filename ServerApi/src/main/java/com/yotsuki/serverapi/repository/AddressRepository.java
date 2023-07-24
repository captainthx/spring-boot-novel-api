package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByUser(User user);
    Optional<Address> findByUid(Long uid);

}
