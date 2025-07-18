package com.prolinux.security_service.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaKeyLoader {

    public static RSAPublicKey loadPublicKey(Resource resource) throws Exception {
        return RsaKeyConverters.x509().convert(resource.getInputStream());
    }

    public static RSAPrivateKey loadPrivateKey(Resource resource) throws Exception {
        return RsaKeyConverters.pkcs8().convert(resource.getInputStream());
    }
}