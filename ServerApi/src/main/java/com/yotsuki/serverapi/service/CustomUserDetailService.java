package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.UserDetailsImp;
import com.yotsuki.boot.configJwt.model.Authentication;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImp loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return UserDetailsImp.build(
                Authentication.builder()
                        .id(userEntity.getId())
                        .username(userEntity.getUsername())
                        .build()
        );


    }
}
