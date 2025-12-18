package com.dc.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性类
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT密钥
     */
    private String secretKey = "jcore-project-secret-key-for-jwt-token-generation-and-validation-2024";

    /**
     * JWT过期时间（毫秒），默认7天
     */
    private Long expirationTime = 7 * 24 * 60 * 60 * 1000L;

    /**
     * JWT签发者
     */
    private String issuer = "jcore-project";

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
