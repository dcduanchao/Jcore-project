package com.dc.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT工具类
 */
@Slf4j
@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // 获取签名密钥
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    /**
     * 生成JWT token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param roles 角色名称列表
     * @return JWT token
     */
    public String generateToken(Long userId, String username, List<Long> roles,  List<String> permissions) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + jwtProperties.getExpirationTime());

            String token = Jwts.builder()
                    .claim("role", roles)
                    .claim("userId", userId)
                    .claim("pCode",permissions)
                    .subject(username)
                    .issuer(jwtProperties.getIssuer()).expiration(expiration)
                    .issuedAt(now).signWith(getSigningKey())
                    .compact();
            return token;
        } catch (Exception e) {
            log.error("生成JWT token失败", e);
            return null;
        }
    }

    /**
     * 验证JWT token
     *
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token已过期: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT token: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("JWT token格式错误: {}", e.getMessage());
            return false;
        } catch (SecurityException e) {
            log.warn("JWT token签名验证失败: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("JWT token参数异常: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT token验证失败", e);
            return false;
        }
    }

    /**
     * 从JWT token中获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.valueOf(claims.get("userId").toString());
        } catch (Exception e) {
            log.error("从JWT token中获取用户ID失败", e);
            return null;
        }
    }

    /**
     * 从JWT token中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("从JWT token中获取用户名失败", e);
            return null;
        }
    }

    /**
     * 从JWT token中获取角色列表
     *
     * @param token JWT token
     * @return 角色名称列表
     */
    @SuppressWarnings("unchecked")
    public List<Long> getRolesFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            List role = claims.get("role", List.class);
            List<Long> rolesList = (List<Long>) role.stream()
                    .map(p->Long.valueOf(p.toString())).collect(Collectors.toList());
            return rolesList;
        } catch (Exception e) {
            log.error("从JWT token中获取角色列表失败", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getPremisssion(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("pCode", List.class);
        } catch (Exception e) {
            log.error("从JWT token中获取角色列表失败", e);
            return null;
        }
    }

    /**
     * 从JWT token中获取过期时间
     *
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("从JWT token中获取过期时间失败", e);
            return null;
        }
    }

    /**
     * 检查JWT token是否过期
     *
     * @param token JWT token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            log.error("检查JWT token是否过期失败", e);
            return true;
        }
    }
}
