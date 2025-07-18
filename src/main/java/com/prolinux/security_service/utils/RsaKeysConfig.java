package com.prolinux.security_service.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeysConfig(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {

}
