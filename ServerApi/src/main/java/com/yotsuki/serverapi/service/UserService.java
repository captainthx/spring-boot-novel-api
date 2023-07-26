package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.AddressRequest;
import com.yotsuki.serverapi.model.response.AddressResponse;
import com.yotsuki.serverapi.model.response.UserResponse;
import com.yotsuki.serverapi.repository.AddressRepository;
import com.yotsuki.serverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    // find all
    public ResponseEntity<?> findAll() {
        return Response.success(userRepository.findAll());
    }

    // findbyId
    public ResponseEntity<?> findById(UserDetailsImp userDetailsImp){
        User userOpt = this.userRepository.findById(userDetailsImp.getId()).get();
        return Response.success(userResponse(userOpt));
    }


    public UserResponse userResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

}
