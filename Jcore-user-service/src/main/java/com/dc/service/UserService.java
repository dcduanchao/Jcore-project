package com.dc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dc.entity.User;
import com.dc.mapper.RoleMapper;
import com.dc.mapper.UserMapper;
import com.dc.auth.JwtUtil;
import com.dc.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

            // 获取用户角色信息 - 需要从user_role表查询，这里暂时使用默认角色
            List<String> roles = Arrays.asList("user"); // 默认角色
            // TODO: 实现从user_role表查询用户角色的逻辑

            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), username, roles);

            log.info("用户登录成功: username={}, roles={}", username, roles);
            return token;
        } catch (Exception e) {
            log.error("登录失败", e);
            return null;
        }
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
     * 用户登出
     */
    public boolean logout(String token) {
        try {
            String tokenKey = "user:token:" + token;
            redisUtil.delete(tokenKey);
            return true;
        } catch (Exception e) {
            log.error("登出失败", e);
            return false;
        }
    }
}
