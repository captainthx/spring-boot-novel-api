package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.Address;
import com.yotsuki.serverapi.entity.Delivery;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.DeliveryRequest;
import com.yotsuki.serverapi.model.response.DeliveryResponse;
import com.yotsuki.serverapi.repository.AddressRepository;
import com.yotsuki.serverapi.repository.DeliveryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, AddressRepository addressRepository) {
        this.deliveryRepository = deliveryRepository;
        this.addressRepository = addressRepository;
    }


    // create delivery
    public ResponseEntity<?> createDelivery(UserDetailsImp userDetailsImp, DeliveryRequest request) {
        if (Objects.isNull(request.getAddressId())) {
            log.warn("delivery::(block) invalid address:{}, udi:{}  ", request, userDetailsImp.getId());
            return Response.error(ResponseCode.INVALID_ADDRESS_ID);
        }
        if (Objects.isNull(request.getFullName())) {
            log.warn("delivery::(block) invalid fullName:{}, udi:{}  ", request, userDetailsImp.getId());
            return Response.error(ResponseCode.INVALID_FULL_NAME);
        }

        if (Objects.isNull(request.getPhone())) {
            log.warn("delivery::(block) invalid phone:{}, udi:{}  ", request, userDetailsImp.getId());
            return Response.error(ResponseCode.INVALID_PHONE);
        }
        Address address = this.addressRepository.findById(request.getAddressId()).get();
        //set to entity
        User user = new User();
        user.setId(userDetailsImp.getId());

        Delivery entity = new Delivery();
        entity.setFullName(request.getFullName());
        entity.setAddress(address.getLine1()+address.getLine2()+address.getZipCode());
        entity.setPhone(request.getPhone());
        entity.setUser(user);

        //save to db
        Delivery res = this.deliveryRepository.save(entity);

        return Response.success(build(res));
    }


    // find delivery history by user id
    public ResponseEntity<?> historyDelivery(UserDetailsImp userDetailsImp) {
        List<DeliveryResponse> deliveryList = this.deliveryRepository.findByUid(userDetailsImp.getId())
                .stream().map(this::build).collect(Collectors.toList());

        return Response.success(deliveryList);
    }

    public DeliveryResponse build(Delivery entity) {
        return DeliveryResponse.builder()
                .fullName(entity.getFullName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .cdt(entity.getCdt())
                .build();
    }
}
