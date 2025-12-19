package com.dc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.entity.Role;
import com.dc.entity.User;
import com.dc.entity.UserRole;
import com.dc.mapper.RoleMapper;
import com.dc.mapper.RolePermissionMapper;
import com.dc.mapper.UserMapper;
import com.dc.auth.JwtUtil;
import com.dc.mapper.UserRoleMapper;
import com.dc.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;


//    public static void main(String[] args) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String admin123 = passwordEncoder.encode("admin123");
//        System.out.println(admin123);
//    }

    /**
     * 用户注册
     */
    public boolean register(String username, String password, String email) {
        try {
            // 检查用户名是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User existingUser = userMapper.selectOne(queryWrapper);
            if (existingUser != null) {
                log.warn("用户名已存在: {}", username);
                return false;
            }

            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            log.error("注册失败", e);
            return false;
        }
    }

    /**
     * 用户登录
     */
    public String login(String username, String password) {
        try {
            // 根据用户名查询用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User user = userMapper.selectOne(queryWrapper);

            if (user == null) {
                log.warn("用户不存在: {}", username);
                return null;
            }

            // 验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.warn("密码错误: {}", username);
                return null;
            }

            // 获取用户角色信息 - 从user_role表查询用户角色名称
            List<Long> roleIds = getUserRoleNames(user.getId());
            List<String> premissions = getPremissions(roleIds);


            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), username, roleIds,premissions);

            log.info("用户登录成功: username={}, roles={}", username, roleIds);
            return token;
        } catch (Exception e) {
            log.error("登录失败", e);
            return null;
        }
    }

    private List<String> getPremissions(List<Long> roleIds) {
        if(CollectionUtils.isEmpty(roleIds)){
            return new ArrayList<>();
        }
        List<String> permissionCodesByRoleIds = rolePermissionMapper.getPermissionCodesByRoleIds(roleIds);
        return permissionCodesByRoleIds;
    }

    /**
     * 根据token获取用户信息
     */
    public User getUserByToken(String token) {
        try {
            // 从JWT中解析用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return null;
            }

            return userMapper.selectById(userId);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }

    /**
     * 根据用户ID获取用户信息
     */
    public User getUserById(Long userId) {
        try {
            return userMapper.selectById(userId);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }

    /**
     * 用户登出
     */
    public boolean logout(Long userId) {
        try {
            log.info("用户登出: userId={}", userId);
            // 由于使用ThreadLocal获取用户信息，登出只需记录日志
            // token会在过期时间后自动失效
            return true;
        } catch (Exception e) {
            log.error("登出失败", e);
            return false;
        }
    }

    /**
     * 获取用户列表（分页，不显示超级管理员）
     */
    public Page<User> getUserList(Integer current, Integer size) {
        try {
            Page<User> page = new Page<>(current, size);
            // 排除超级管理员用户（role_id = 1）
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.notInSql("id", "SELECT user_id FROM user_role WHERE role_id = 1");
            queryWrapper.orderByDesc("create_time");

            return userMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return new Page<>();
        }
    }

    /**
     * 创建用户
     */
    public boolean createUser(String username, String password, String email) {
        try {
            // 检查用户名是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User existingUser = userMapper.selectOne(queryWrapper);
            if (existingUser != null) {
                log.warn("用户名已存在: {}", username);
                return false;
            }

            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            log.error("创建用户失败", e);
            return false;
        }
    }

    /**
     * 更新用户
     */
    public boolean updateUser(Long userId, String username, String password, String email) {
        try {
            User existingUser = userMapper.selectById(userId);
            if (existingUser == null) {
                log.warn("用户不存在: {}", userId);
                return false;
            }

            // 检查用户名是否已被其他用户使用
            if (username != null && !username.equals(existingUser.getUsername())) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", username).ne("id", userId);
                User duplicateUser = userMapper.selectOne(queryWrapper);
                if (duplicateUser != null) {
                    log.warn("用户名已存在: {}", username);
                    return false;
                }
                existingUser.setUsername(username);
            }

            if (password != null && !password.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(password));
            }

            if (email != null) {
                existingUser.setEmail(email);
            }

            existingUser.setUpdateTime(LocalDateTime.now());
            int result = userMapper.updateById(existingUser);
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户失败", e);
            return false;
        }
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long userId) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                log.warn("用户不存在: {}", userId);
                return false;
            }

            // 检查是否为超级管理员用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userId);
            queryWrapper.inSql("id", "SELECT user_id FROM user_role WHERE role_id = 1");
            User adminUser = userMapper.selectOne(queryWrapper);
            if (adminUser != null) {
                log.warn("不能删除超级管理员用户: {}", userId);
                return false;
            }

            int result = userMapper.deleteById(userId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return false;
        }
    }

    /**
     * 获取用户角色名称列表
     */
    private List<Long> getUserRoleNames(Long userId) {
        try {
            // 查询用户角色关联
            LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
            userRoleWrapper.eq(UserRole::getUserId, userId);
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);

            if (userRoles.isEmpty()) {
                return Arrays.asList(3L); // 默认角色
            }

            // 获取角色名称列表
            List<Long> roleIds = userRoles.stream()
                    .map(UserRole::getRoleId)
                    .collect(Collectors.toList());

            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            return roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户角色名称失败: userId={}", userId, e);
            return Arrays.asList(3L); // 默认角色
        }
    }
}
