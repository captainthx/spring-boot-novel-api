package com.yotsuki.serverapi.service;

import com.yotsuki.boot.configJwt.JwtService;
import com.yotsuki.boot.configJwt.model.Authentication;
import com.yotsuki.boot.configJwt.model.TokenResponse;
import com.yotsuki.excommon.common.Response;
import com.yotsuki.excommon.common.ResponseCode;
import com.yotsuki.excommon.utils.Comm;
import com.yotsuki.excommon.utils.ValidateUtil;
import com.yotsuki.serverapi.entity.HistoryLogin;
import com.yotsuki.serverapi.entity.User;
import com.yotsuki.serverapi.model.request.LoginRequest;
import com.yotsuki.serverapi.model.request.RegisterRequest;
import com.yotsuki.serverapi.model.request.TokenRefreshRequest;
import com.yotsuki.serverapi.model.response.UserResponse;
import com.yotsuki.serverapi.repository.HistoryLoginRepository;
import com.yotsuki.serverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final HistoryLoginRepository historyLoginRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, HistoryLoginRepository historyLoginRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.historyLoginRepository = historyLoginRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // create
    public ResponseEntity<?> register(RegisterRequest request) {
        if (ValidateUtil.invalidUsername(request.getUsername())) {
            log.debug("[register] username validate fail!");
            return Response.error(ResponseCode.INVALID_USERNAME);
        }
        if (ValidateUtil.invalidPassword(request.getPassword())) {
            log.debug("[register] password validate fial!");
            return Response.error(ResponseCode.INVALID_PASSWORD);
        }

        if (ValidateUtil.invalidEmail(request.getEmail())) {
            log.debug("[register] validate email fail!");
            return Response.error(ResponseCode.INVALID_EMAIL);
        }

        if (ValidateUtil.invalidPassword(request.getPassword())) {
            log.debug("[register] validate password fail!");
            return Response.error(ResponseCode.INVALID_PASSWORD);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.debug("[register] email is duplicate");
            return Response.error(ResponseCode.EXISTED_EMAIL);
        }

        //encoder password
        String password = passwordEncoder.encode(request.getPassword());
        // save to entity
        User entity = new User();
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setPassword(password);

        // save
        User user = userRepository.save(entity);
        // response
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return Response.success(response);

    }

    public ResponseEntity<?> login(LoginRequest request, HttpServletRequest httpRequest) {
        String ipv4 = Comm.getIpAddress(httpRequest);
        String userAgent = Comm.getUserAgent(httpRequest);
        String device = Comm.getDeviceType(userAgent);

        if (ValidateUtil.invalidUsername(request.getUsername())) {
            log.debug("[login] validate username fail!");
            return Response.error(ResponseCode.INVALID_USERNAME);
        }

        if (ValidateUtil.invalidPassword(request.getPassword())) {
            log.debug("[login] validate password fail!");
            return Response.error(ResponseCode.INVALID_USERNAME_PASSWORD);
        }
        // check in db
        Optional<User> opt = userRepository.findByUsername(request.getUsername());

        if (!opt.isPresent()) {
            log.debug("[login]  user notFound!");
            return Response.error(ResponseCode.INVALID_USERNAME);
        }
        User user = opt.get();
        // validate password
        if (!MatchPassword(request.getPassword(), user.getPassword())) {
            log.debug("[login] validate password fail!");
            return Response.error(ResponseCode.INVALID_USERNAME_PASSWORD);
        }
        //save loginLog
        HistoryLogin loginLog = new HistoryLogin();
        loginLog.setUid(user.getId());
        loginLog.setIpv4(ipv4);
        loginLog.setDevice(device);
        loginLog.setUserAgent(userAgent);
        historyLoginRepository.save(loginLog);

        //  response token
        return Response.success(getToken(user));
    }
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        if (Objects.isNull(request.getRefreshToken())) {
            return Response.error(ResponseCode.INVALID_REQUEST);
        }

        String refreshToken = request.getRefreshToken();
        if (!jwtService.validates(refreshToken)) {
            log.debug("[refresh token] refresh token expire!");
            return Response.error(ResponseCode.REFRESH_TOKEN_EXPIRE);
        }

        String username = jwtService.getUsername(refreshToken);
        Optional<User> opt = userRepository.findByUsername(username);
        if (!opt.isPresent()) {
            return Response.error(ResponseCode.UNAUTHORIZED);
        }
        User user = opt.get();
        TokenResponse token = getToken(user);
        return Response.success(token);
    }

    public TokenResponse getToken(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return jwtService.generateToken(
                Authentication.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .build()
        );
    }


    public boolean MatchPassword(String rawPassword, String encodePassword) {
        return passwordEncoder.matches(rawPassword, encodePassword);
    }
}
