package com.dc.controller;

import com.dc.dto.LoginRequest;
import com.dc.dto.UserRequest;
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
    public ResultVo getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            User user = userService.getUserByToken(token);
            if (user != null) {
                return ResultVo.success("获取成功", user);
            } else {
                return ResultVo.error("token无效或已过期");
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
    public ResultVo logout(@RequestHeader("Authorization") String token) {
        try {
            boolean result = userService.logout(token);
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
}
