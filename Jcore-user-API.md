# Jcore-user API 接口文档

## 概述

Jcore-user 是基于Spring Boot的用户权限管理系统，提供完整的用户认证、角色管理、权限控制和菜单管理功能。

**版本**: 1.0.0
**基础URL**: `http://localhost:8080`
**认证方式**: JWT Bearer Token
**跨域支持**: 已配置CORS，支持跨域请求

## 公共响应格式

所有接口返回统一的JSON格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，200表示成功 |
| message | String | 响应消息 |
| data | Object | 响应数据 |
| timestamp | Long | 时间戳 |
| traceId | String | 请求跟踪ID |

## 认证相关接口

### 1. 用户注册

**接口地址**: `POST /user/register`

**请求参数**:
```json
{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名，唯一 |
| password | String | 是 | 密码 |
| email | String | 是 | 邮箱地址 |

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null,
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 2. 用户登录

**接口地址**: `POST /user/login`

**请求参数**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": "eyJhbGciOiJIUzI1NiJ9...",
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 3. 获取用户信息

**接口地址**: `GET /user/info`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@jcore.com",
    "createTime": "2023-12-18T10:00:00",
    "updateTime": "2023-12-18T10:00:00"
  },
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 4. 用户登出

**接口地址**: `POST /user/logout`

**权限要求**: 已登录用户

**响应示例**:
```json
{
  "code": 200,
  "message": "登出成功",
  "data": null,
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 5. 获取用户列表（分页，不显示超级管理员）

**接口地址**: `GET /user/list`

**权限要求**: `user:manage`

**请求参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| current | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 10 | 每页大小 |

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
      {
        "id": 2,
        "username": "testuser",
        "email": "test@example.com",
        "createTime": "2023-12-18T10:00:00",
        "updateTime": "2023-12-18T10:00:00"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  },
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

**注意事项**:
- 超级管理员用户不会在列表中显示
- 按创建时间倒序排列

### 6. 根据ID获取用户

**接口地址**: `GET /user/{id}`

**权限要求**: `user:manage`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 2,
    "username": "testuser",
    "email": "test@example.com",
    "createTime": "2023-12-18T10:00:00",
    "updateTime": "2023-12-18T10:00:00"
  },
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 7. 创建用户

**接口地址**: `POST /user`

**权限要求**: `user:manage`

**请求参数**:
```json
{
  "username": "newuser",
  "password": "password123",
  "email": "newuser@example.com"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名，唯一 |
| password | String | 是 | 密码 |
| email | String | 否 | 邮箱地址 |

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": null,
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 8. 更新用户

**接口地址**: `PUT /user/{id}`

**权限要求**: `user:manage`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数**:
```json
{
  "username": "updateduser",
  "password": "newpassword123",
  "email": "updated@example.com"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 否 | 用户名，如不提供则不更新 |
| password | String | 否 | 密码，如不提供则不更新 |
| email | String | 否 | 邮箱地址，如不提供则不更新 |

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null,
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 9. 删除用户

**接口地址**: `DELETE /user/{id}`

**权限要求**: `user:manage`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

**注意事项**:
- 不能删除超级管理员用户
- 删除操作会同时删除用户的角色关联关系

## 角色管理接口

### 1. 获取角色列表（分页，不显示超级管理员角色）

**接口地址**: `GET /role/list`

**权限要求**: `role:manage`

**请求参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| current | Integer | 否 | 1 | 当前页码 |
| size | Integer | 否 | 10 | 每页大小 |

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "records": [
      {
        "id": 2,
        "name": "管理员",
        "description": "拥有大部分管理权限",
        "createTime": "2023-12-18T10:00:00",
        "updateTime": "2023-12-18T10:00:00"
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  },
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

**注意事项**:
- 超级管理员角色（id=1）不会在列表中显示
- 按创建时间倒序排列

### 2. 获取所有角色（不包括超级管理员角色）

**接口地址**: `GET /role/all`

**权限要求**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 2,
      "name": "管理员",
      "description": "拥有大部分管理权限",
      "createTime": "2023-12-18T10:00:00",
      "updateTime": "2023-12-18T10:00:00"
    }
  ],
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

**注意事项**:
- 超级管理员角色（id=1）不会在列表中显示
- 按创建时间倒序排列

### 3. 根据ID获取角色

**接口地址**: `GET /role/{id}`

**权限要求**: `role:manage`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 角色ID |

### 4. 新增角色

**接口地址**: `POST /role`

**权限要求**: `role:manage`

**请求参数**:
```json
{
  "name": "测试角色",
  "description": "测试角色描述"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 角色名称，唯一 |
| description | String | 否 | 角色描述 |

### 5. 更新角色

**接口地址**: `PUT /role/{id}`

**权限要求**: `role:manage`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 角色ID |

**请求参数**: 同新增角色

### 6. 删除角色

**接口地址**: `DELETE /role/{id}`

**权限要求**: `role:manage`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 角色ID |

## 权限管理接口

### 1. 获取权限列表（分页）

**接口地址**: `GET /permission/list`

**权限要求**: `permission:manage`

**请求参数**: 同角色列表分页参数

### 2. 获取所有权限

**接口地址**: `GET /permission/all`

**权限要求**: 无

### 3. 根据ID获取权限

**接口地址**: `GET /permission/{id}`

**权限要求**: `permission:manage`

### 4. 新增权限

**接口地址**: `POST /permission`

**权限要求**: `permission:manage`

**请求参数**:
```json
{
  "name": "用户查看",
  "code": "user:read",
  "description": "查看用户信息的权限"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 权限名称 |
| code | String | 是 | 权限代码，唯一 |
| description | String | 否 | 权限描述 |

### 5. 更新权限

**接口地址**: `PUT /permission/{id}`

**权限要求**: `permission:manage`

### 6. 删除权限

**接口地址**: `DELETE /permission/{id}`

**权限要求**: `permission:manage`

## 菜单管理接口

### 1. 获取菜单列表（分页）

**接口地址**: `GET /menu/list`

**权限要求**: `menu:manage`

**请求参数**: 同角色列表分页参数

### 2. 获取所有菜单

**接口地址**: `GET /menu/all`

**权限要求**: 无

### 3. 获取菜单树形结构

**接口地址**: `GET /menu/tree`

**权限要求**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "path": "/system",
      "icon": "setting",
      "parentId": null,
      "sortOrder": 1,
      "createTime": "2023-12-18T10:00:00",
      "updateTime": "2023-12-18T10:00:00",
      "children": [
        {
          "id": 2,
          "name": "用户管理",
          "path": "/system/user",
          "icon": "user",
          "parentId": 1,
          "sortOrder": 1,
          "createTime": "2023-12-18T10:00:00",
          "updateTime": "2023-12-18T10:00:00",
          "children": []
        }
      ]
    }
  ],
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

### 4. 根据用户角色获取菜单栏

**接口地址**: `GET /menu/user`

**权限要求**: 无（需要登录）

**请求头**:
```
Authorization: Bearer {token}
```

**功能说明**:
根据当前登录用户的角色权限，返回用户有权访问的菜单列表。系统会自动查询用户的角色，并返回对应角色的菜单权限。

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "path": "/system",
      "icon": "setting",
      "parentId": null,
      "sortOrder": 1,
      "createTime": "2023-12-18T10:00:00",
      "updateTime": "2023-12-18T10:00:00",
      "children": [
        {
          "id": 2,
          "name": "用户管理",
          "path": "/system/user",
          "icon": "user",
          "parentId": 1,
          "sortOrder": 1,
          "createTime": "2023-12-18T10:00:00",
          "updateTime": "2023-12-18T10:00:00",
          "children": []
        }
      ]
    }
  ],
  "timestamp": 1640995200000,
  "traceId": "abc123"
}
```

**注意事项**:
- 需要用户先登录获取JWT Token
- 返回的菜单根据用户的角色权限动态过滤
- 如果用户没有菜单权限，返回空数组
- 菜单按sort_order字段排序

### 4. 根据ID获取菜单

**接口地址**: `GET /menu/{id}`

**权限要求**: `menu:manage`

### 5. 新增菜单

**接口地址**: `POST /menu`

**权限要求**: `menu:manage`

**请求参数**:
```json
{
  "name": "测试菜单",
  "path": "/test",
  "icon": "test",
  "parentId": null,
  "sortOrder": 1
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 菜单名称 |
| path | String | 否 | 菜单路径 |
| icon | String | 否 | 菜单图标 |
| parentId | Long | 否 | 父菜单ID |
| sortOrder | Integer | 否 | 排序号 |

### 6. 更新菜单

**接口地址**: `PUT /menu/{id}`

**权限要求**: `menu:manage`

### 7. 删除菜单

**接口地址**: `DELETE /menu/{id}`

**权限要求**: `menu:manage`

## 权限控制说明

### 权限注解

接口使用 `@RequiresPermission` 注解进行权限控制：

```java
@RequiresPermission("user:manage")
@GetMapping("/users")
public ResultVo getUsers() {
    // 需要 user:manage 权限才能访问
}
```

### 权限代码格式

权限代码采用 `模块:操作` 的格式：

- `user:manage` - 用户管理权限
- `role:read` - 角色查看权限
- `permission:manage` - 权限管理权限
- `menu:read` - 菜单查看权限

### JWT认证

需要认证的接口需要在请求头中携带JWT Token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 401 | 未授权（token无效或缺失） |
| 403 | 权限不足 |
| 500 | 服务器内部错误 |

## 使用示例

### 1. 用户登录获取Token

```bash
curl -X POST http://localhost:8080/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 2. 使用Token访问受保护接口

```bash
curl -X GET http://localhost:8080/role/list \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. 获取菜单树

```bash
curl -X GET http://localhost:8080/menu/tree
```

## 注意事项

1. **Token有效期**: JWT Token有默认过期时间，过期后需要重新登录
2. **权限验证**: 敏感操作接口都需要相应的权限
3. **数据验证**: 所有输入数据都会进行格式验证
4. **错误处理**: 统一的错误响应格式，便于客户端处理
5. **分页查询**: 列表接口支持分页，默认为第1页，每页10条记录

## 数据库初始化

运行项目前，请先执行 `create_tables.sql` 脚本创建数据库表和初始数据。

初始管理员账号：
- 用户名: `dcadmin`
- 密码: `admin123`
- 角色: 超级管理员（拥有所有权限）
