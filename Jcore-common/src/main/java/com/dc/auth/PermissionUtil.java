package com.dc.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 权限校验工具类
 */
@Slf4j
@Component
public class PermissionUtil {

    /**
     * 检查当前用户是否有指定权限
     */
    public static boolean hasPermission(String permissionCode) {
        try {
            UserSession userSession = UserSessionContext.getUserSession();
            if (userSession == null) {
                log.warn("用户会话不存在");
                return false;
            }

            List<Long> roles = userSession.getRoles();
            if (roles == null || roles.isEmpty()) {
                log.warn("用户角色为空");
                return false;
            }

            // 管理员拥有所有权限
            if (roles.contains(1L)) {
                return true;
            }

            // TODO: 这里可以扩展为从数据库查询用户的具体权限
            // 目前简化为角色名称匹配权限代码前缀
            // 例如：user角色只能访问user:*权限，admin可以访问所有权限

            // 简单的权限匹配逻辑（可根据实际需求扩展）

            List<String> permissions = userSession.getPermissions();
            if(permissions.contains(permissionCode)){
                return true;
            }

//
//            if (roles.contains("user") && roles.startsWith("user:")) {
//                return true;
//            }

            log.warn("用户{}没有权限{}，角色：{}", userSession.getUsername(), permissionCode, roles);
            return false;

        } catch (Exception e) {
            log.error("权限校验失败", e);
            return false;
        }
    }

    /**
     * 检查当前用户是否是管理员
     */
    public static boolean isAdmin() {
        UserSession userSession = UserSessionContext.getUserSession();
        return userSession != null && userSession.getRoles() != null && userSession.getRoles().contains("admin");
    }

    /**
     * 检查当前用户是否是普通用户
     */
    public static boolean isUser() {
        UserSession userSession = UserSessionContext.getUserSession();
        return userSession != null && userSession.getRoles() != null && userSession.getRoles().contains("user");
    }
}
