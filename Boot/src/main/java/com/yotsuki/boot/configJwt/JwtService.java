package com.yotsuki.boot.configJwt;

import com.yotsuki.boot.configJwt.model.Authentication;
import com.yotsuki.boot.configJwt.model.Token;
import com.yotsuki.boot.configJwt.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;

@Slf4j
public class JwtService {
    private final String issuer;
    private final Long expirationMs;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public JwtService(String issuer, Long expirationMs, JwtEncoder encoder, JwtDecoder decoder) {
        this.issuer = issuer;
        this.expirationMs = expirationMs;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public TokenResponse generateToken(Authentication authentication) {
        Token accessToken = generateAccessToken(authentication);
        Token refreshToken = generateRefreshToken(authentication);
        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessExpire(accessToken.getExpire())
                .refreshToken(refreshToken.getToken())
                .refreshExpire(refreshToken.getExpire())
                .build();
    }


    public Token generateAccessToken(Authentication authentication) {
        return generateToken(authentication, this.expirationMs);
    }

    public Token generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, this.expirationMs * 2);
    }

    public Token generateToken(Authentication authentication, long expire) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expire);
        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuer(this.issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getUsername())
                .claim("id",authentication.getId())
                .build();
        return Token.builder()
                .token(this.encoder.encode(JwtEncoderParameters.from(claim)).getTokenValue())
                .expire(expiresAt.toEpochMilli())
                .build();
    }

    public boolean validates(String token) {
        try {
            decoder.decode(token);
            return true;
        } catch (Exception e) {
            log.error("TokenService-[validate](invalid token). token: {}, error: {}", token, e.getMessage());
            return false;

        }
    }

    public String getUsername(String token) {
        Jwt decode = decoder.decode(token);
        return decode.getSubject();
    }

}
