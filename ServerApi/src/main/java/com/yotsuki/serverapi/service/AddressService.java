package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.AddressRequest;
import com.yotsuki.serverapi.model.response.AddressResponse;
import com.yotsuki.serverapi.repository.AddressRepository;
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

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * @param userDetailsImp
     * @param request
     * @return
     */
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
        //set foreign key
        User user = new User();
        user.setId(userDetailsImp.getId());
        //save to entity
        Address entity = new Address();
        entity.setUser(user);
        entity.setLine1(request.getLine1());
        entity.setLine2(request.getLine2());
        entity.setZipCode(request.getZipCode());

        Address addressRes = this.addressRepository.save(entity);

        return Response.success(addressResponse(addressRes));
    }


    /**
     * @param userDetailsImp user details
     * @return response
     */
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
