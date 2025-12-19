# ChvImagesController 文档

## 概述

`ChvImagesController` 是一个 Spring Boot REST 控制器，用于处理与 ChvImages 相关的图像数据操作。该控制器提供分页查询图像列表的功能。

## 基础信息

- **包名**: `com.dc.controller.image`
- **注解**:
  - `@RestController`: 标识为 REST 控制器
  - `@RequestMapping("/chv")`: 基础请求路径为 `/chv`
  - `@Slf4j`: 启用日志记录

## 依赖注入

- `ChvImagesService`: 图像服务接口，用于业务逻辑处理

## API 接口

### 1. 获取图像列表

#### 接口信息
- **请求方法**: GET
- **请求路径**: `/chv/list`
- **描述**: 分页获取 ChvImages 图像列表

#### 请求参数
| 参数名 | 类型 | 默认值 | 必填 | 描述 |
|--------|------|--------|------|------|
| current | Integer | 1 | 否 | 当前页码 |
| size | Integer | 10 | 否 | 每页大小 |

#### 返回值
- **类型**: `ResultVo`
- **成功响应**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "records": [  {
                "imageId": 44,
                "imageName": "20240406-006eVYYigy1hvbrk6payij30qo0zkalk",
                "imageDateGmt": "2025-07-20T21:58:10.000+00:00",
                "imageExtension": "jpg",
                "imageUrl": "http://192.168.30.33:7070//images/2025/07/21/20240406-006eVYYigy1hvbrk6payij30qo0zkalk.jpg"
            }], // ChvImagesVo 对象列表
      "total": 100,     // 总记录数
      "size": 10,       // 每页大小
      "current": 1,     // 当前页码
      "pages": 10       // 总页数
    }
  }
  ```

#### 示例请求
```
GET /chv/list?current=1&size=10
```

#### 异常处理
- 如果服务层出现异常，会通过 `ResultVo` 返回相应的错误信息

## 数据模型

### ChvImagesVo
图像视图对象，包含图像的详细信息。具体字段请参考 `ChvImagesVo` 类定义。

## 安全考虑
- 该控制器可能需要适当的权限控制，建议结合 `@RequiresPermission` 等注解进行访问控制

## 扩展建议
- 可以添加更多操作如上传、删除、更新图像的功能
- 考虑添加图像压缩、格式转换等处理逻辑
