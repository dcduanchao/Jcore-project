package com.dc.auth;

import com.dc.auth.JwtUtil;
import com.dc.auth.UserSession;
import com.dc.auth.UserSessionContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * JWT拦截器，用于验证token并设置用户会话
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("请求: {} {}", method, requestURI);
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 跳过登录和注册接口
        if (requestURI.contains("/user/login") || requestURI.contains("/user/register")) {
            return true;
        }
//        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
//            log.info("请求头: {}={}", headerName, request.getHeader(headerName));
//        });
        // 从请求头获取token
        String token = request.getHeader("authorization");
        if (token == null || token.isEmpty()) {
            log.warn("请求缺少Authorization头: {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"缺少认证信息\"}");
            return false;
        }

        // 如果token以Bearer开头，去掉前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            log.warn("无效的token: {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"token无效或已过期\"}");
            return false;
        }

        // 从token中提取用户信息
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            List<Long> roles = jwtUtil.getRolesFromToken(token);
            List<String> pCode = jwtUtil.getPremisssion(token);

            // 创建用户会话并设置到ThreadLocal
            UserSession userSession = new UserSession(userId, username, roles,pCode);
            UserSessionContext.setUserSession(userSession);

            log.debug("用户认证成功: username={}, roles={}", username, roles);
            return true;
        } catch (Exception e) {
            log.error("解析token失败", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"token解析失败\"}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后清理ThreadLocal
        UserSessionContext.clear();
    }
}
