package com.yotsuki.serverapi.service;

import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.response.UserResponse;
import com.yotsuki.serverapi.repository.FavoriteRepository;
import com.yotsuki.serverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FavoriteRepository favRepository;

    public UserService(UserRepository userRepository, FavoriteRepository favRepository) {
        this.userRepository = userRepository;
        this.favRepository = favRepository;
    }


    // find all
    public ResponseEntity<?> findAll(){
        return Response.success(userRepository.findAll());
    }


    // findbyId
    public ResponseEntity<?> findById(Long id){
        if (!userRepository.existsById(id)){
            log.info("[user] id notFound! {}",id);
            return Response.error(ResponseCode.NOT_FOUND);
        }

        Optional<User> opt = userRepository.findById(id);

        if (!opt.isPresent()){
            log.info("[book] user notFoud! {}",opt);
            return Response.error(ResponseCode.NOT_FOUND);
        }
        User user = opt.get();
        log.info("userdata {}",user);
        // response

        return Response.success(response(user));
    }


    public UserResponse response(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
