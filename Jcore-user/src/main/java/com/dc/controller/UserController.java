package com.dc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.auth.RequiresPermission;
import com.dc.auth.UserSessionContext;
import com.dc.dto.LoginRequest;
import com.dc.dto.UserRequest;
import com.dc.dto.UserUpdateRequest;
import com.dc.entity.User;
import com.dc.result.ResultVo;
import com.dc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResultVo register(@Valid @RequestBody UserRequest userRequest) {
        try {
            boolean result = userService.register(userRequest.getUsername(),
                                                userRequest.getPassword(),
                                                userRequest.getEmail());
            if (result) {
                return ResultVo.success("注册成功");
            } else {
                return ResultVo.error("注册失败，用户名可能已存在");
            }
        } catch (Exception e) {
            log.error("注册异常", e);
            return ResultVo.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultVo login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest.getUsername(),
                                           loginRequest.getPassword());
            if (token != null) {
                return ResultVo.success("登录成功", token);
            } else {
                return ResultVo.error("登录失败，用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("登录异常", e);
            return ResultVo.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public ResultVo getUserInfo() {
        try {
            Long userId = UserSessionContext.getCurrentUserId();
            if (userId == null) {
                return ResultVo.error("用户未登录");
            }
            User user = userService.getUserById(userId);
            if (user != null) {
                return ResultVo.success("获取成功", user);
            } else {
                return ResultVo.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResultVo logout() {
        try {
            Long userId = UserSessionContext.getCurrentUserId();
            if (userId == null) {
                return ResultVo.error("用户未登录");
            }
            boolean result = userService.logout(userId);
            if (result) {
                return ResultVo.success("登出成功");
            } else {
                return ResultVo.error("登出失败");
            }
        } catch (Exception e) {
            log.error("登出异常", e);
            return ResultVo.error("登出失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户列表（分页，不显示超级管理员）
     */
    @GetMapping("/list")
    @RequiresPermission("user:manage")
    public ResultVo getUserList(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<User> page = userService.getUserList(current, size);
            return ResultVo.success("获取成功", page);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    @RequiresPermission("user:manage")
    public ResultVo getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return ResultVo.success("获取成功", user);
            } else {
                return ResultVo.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 创建用户
     */
    @PostMapping
    @RequiresPermission("user:manage")
    public ResultVo createUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            boolean result = userService.createUser(userRequest.getUsername(),
                                                   userRequest.getPassword(),
                                                   userRequest.getEmail());
            if (result) {
                return ResultVo.success("创建成功");
            } else {
                return ResultVo.error("创建失败，用户名可能已存在");
            }
        } catch (Exception e) {
            log.error("创建用户失败", e);
            return ResultVo.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @RequiresPermission("user:manage")
    public ResultVo updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            boolean result = userService.updateUser(id,
                                                   userUpdateRequest.getUsername(),
                                                   userUpdateRequest.getPassword(),
                                                   userUpdateRequest.getEmail());
            if (result) {
                return ResultVo.success("更新成功");
            } else {
                return ResultVo.error("更新失败，用户不存在或用户名已存在");
            }
        } catch (Exception e) {
            log.error("更新用户失败", e);
            return ResultVo.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("user:manage")
    public ResultVo deleteUser(@PathVariable Long id) {
        try {
            boolean result = userService.deleteUser(id);
            if (result) {
                return ResultVo.success("删除成功");
            } else {
                return ResultVo.error("删除失败，用户不存在或为超级管理员");
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return ResultVo.error("删除失败：" + e.getMessage());
        }
    }
}
