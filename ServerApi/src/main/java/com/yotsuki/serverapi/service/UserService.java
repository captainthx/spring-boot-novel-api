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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseEntity<?> findById(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("[user] id notFound! {}", id);
            return Response.error(ResponseCode.NOT_FOUND);
        }

        Optional<User> opt = userRepository.findById(id);

        if (!opt.isPresent()) {
            log.warn("[user] user notFoud! {}", opt);
            return Response.error(ResponseCode.NOT_FOUND);
        }
        User user = opt.get();
         List<Address> addressList = addressRepository.findByUser(user);
        // response
        return Response.success(userResponse(user,addressList));
    }


    public ResponseEntity<?> createAddress(UserDetailsImp userDetailsImp, AddressRequest request) {
        if (Objects.isNull(request.getLine1())) {
            log.warn("[user] address is null! {}", request);
            return Response.error(ResponseCode.INVALID_ADDRESS_LINE);
        }
        if (Objects.isNull(request.getLine2())) {
            log.warn("[user] address is null! {}", request);
            return Response.error(ResponseCode.INVALID_ADDRESS_LINE);
        }
        if (Objects.isNull(request.getZipCode())) {
            log.warn("[user] address zipcode is null! {}", request);
            return Response.error(ResponseCode.INVALID_ADDRESS_ZIPCODE);
        }

        User userOptional = userRepository.findById(userDetailsImp.getId()).get();
        Address entity = new Address();

        //save to entity
        entity.setUser(userOptional);
        entity.setLine1(request.getLine1());
        entity.setLine2(request.getLine2());
        entity.setZipCode(request.getZipCode());

        Address addressRes = addressRepository.save(entity);

        return Response.success(addressResponse(addressRes));
    }


    public UserResponse userResponse(User user,List<Address> address) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .addresses(address)
                .build();
    }

    public AddressResponse addressResponse(Address address) {
        return AddressResponse.builder()
                .line1(address.getLine1())
                .line2(address.getLine2())
                .zipCode(address.getZipCode())
                .user(address.getUser())
                .build();
    }
}
