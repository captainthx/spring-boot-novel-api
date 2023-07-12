package com.yotsuki.boot.configJwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@EnableConfigurationProperties({JwtProperties.class})
public class JwtAutoConfiguration {

    private final JwtProperties jwtProperties;

    public JwtAutoConfiguration(JwtProperties rsaKey) {
        this.jwtProperties = rsaKey;
    }

    @Bean
    JwtDecoder jwtDecoder(){return NimbusJwtDecoder.withPublicKey(jwtProperties.getPublicKey()).build();}


    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder( jwtProperties.getPublicKey()).privateKey(jwtProperties.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @ConditionalOnMissingBean(JwtService.class)
      public JwtService jwtService(){
        return new JwtService(
                jwtProperties.getIssuer(),
                jwtProperties.getExpirationMs(),
                jwtEncoder(),
                jwtDecoder()
        );
    }
    @Bean
    @ConditionalOnMissingBean(AuthEntryPointJwt.class)
    public AuthEntryPointJwt authEntryPointJwt(){return new AuthEntryPointJwt();}
}
