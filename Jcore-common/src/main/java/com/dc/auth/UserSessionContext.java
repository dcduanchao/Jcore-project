package com.dc.auth;

/**
 * 用户会话上下文，使用ThreadLocal存储当前用户信息
 */
public class UserSessionContext {

    private static final ThreadLocal<UserSession> USER_SESSION = new ThreadLocal<>();

    /**
     * 设置当前用户会话
     */
    public static void setUserSession(UserSession userSession) {
        USER_SESSION.set(userSession);
    }

    /**
     * 获取当前用户会话
     */
    public static UserSession getUserSession() {
        return USER_SESSION.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        UserSession userSession = getUserSession();
        return userSession != null ? userSession.getUserId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        UserSession userSession = getUserSession();
        return userSession != null ? userSession.getUsername() : null;
    }

    /**
     * 获取当前用户角色列表
     */
    public static java.util.List<String> getCurrentRoles() {
        UserSession userSession = getUserSession();
        return userSession != null ? userSession.getRoles() : null;
    }

    /**
     * 获取当前用户主要角色（第一个角色）
     */
    public static String getCurrentRole() {
        java.util.List<String> roles = getCurrentRoles();
        return roles != null && !roles.isEmpty() ? roles.get(0) : null;
    }

    /**
     * 清理当前用户会话
     */
    public static void clear() {
        USER_SESSION.remove();
    }
}
