package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.AddressRequest;
import com.yotsuki.serverapi.model.response.AddressResponse;
import com.yotsuki.serverapi.repository.AddressRepository;
import com.yotsuki.serverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createAddress(UserDetailsImp userDetailsImp, AddressRequest request) {
        if (Objects.isNull(request.getLine1())) {
            log.warn("address::(block) address is null! {}", request);
            return Response.error(ResponseCode.INVALID_ADDRESS_LINE);
        }
        if (Objects.isNull(request.getLine2())) {
            log.warn("address::(block) address is null! {}", request);
            return Response.error(ResponseCode.INVALID_ADDRESS_LINE);
        }
        if (Objects.isNull(request.getZipCode())) {
            log.warn("address::(block) address zipcode is null! {}", request);
            return Response.error(ResponseCode.INVALID_ADDRESS_ZIPCODE);
        }

        long record =  this.addressRepository.countByUid(userDetailsImp.getId());
        if (record  == 3){
            log.warn("address::(block) address is full! {}", request);
            return Response.error(ResponseCode.ADDRESS_FULL);
        }

        User userOptional = this.userRepository.findById(userDetailsImp.getId()).get();
        Address entity = new Address();
        //save to entity
        entity.setUser(userOptional);
        entity.setLine1(request.getLine1());
        entity.setLine2(request.getLine2());
        entity.setZipCode(request.getZipCode());

        Address addressRes = this.addressRepository.save(entity);

        return Response.success(addressResponse(addressRes));
    }

    public ResponseEntity<?> findAddressByUid(UserDetailsImp userDetailsImp){
        List<AddressResponse> addressList = this.addressRepository.findByUid(userDetailsImp.getId())
                .stream().map(this::addressResponse).collect(Collectors.toList());

        return Response.success(addressList);
    }


    public AddressResponse addressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .line1(address.getLine1())
                .line2(address.getLine2())
                .zipCode(address.getZipCode())
                .build();
    }
}
