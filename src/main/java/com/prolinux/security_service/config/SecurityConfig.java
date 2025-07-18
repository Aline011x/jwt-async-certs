package com.prolinux.security_service.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.prolinux.security_service.utils.RsaKeysConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RsaKeysConfig rsaKeysConfig;

    public SecurityConfig(RsaKeysConfig rsaKeysConfig) {
        this.rsaKeysConfig = rsaKeysConfig;
    }
    public SecurityConfig() throws Exception {
        // Carga las claves usando el utilitario
        this.rsaKeysConfig = new RsaKeysConfig(
                RsaKeyLoader.loadPublicKey(new ClassPathResource("certs/public.pem")),
                RsaKeyLoader.loadPrivateKey(new ClassPathResource("certs/private.pem"))
        );
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
                    UserDetails user1 = User.withUsername("user1")
                             .password("{noop}1234")
                             .authorities("USER")
                             .build();
                    UserDetails user2 = User.withUsername("user2")
                             .password("{noop}1234")
                             .authorities("USER", "ADMIN")
                             .build();
        return new InMemoryUserDetailsManager(user1, user2);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)-> auth.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2)-> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
@Bean
JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.rsaPublicKey()).build();
}
@Bean
JwtEncoder jwtEncoder(){
    JWK jwk = new RSAKey.Builder(rsaKeysConfig.rsaPublicKey()).privateKey(rsaKeysConfig.rsaPrivateKey()).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
    }


}
