package com.dc.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;

/**
 * 用户更新请求DTO
 */
@Data
public class UserUpdateRequest {

    private String username;

    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;
}
