-- 创建数据库
CREATE DATABASE IF NOT EXISTS jcore_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE jcore_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 创建权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) NOT NULL COMMENT '权限名称',
    `code` varchar(100) NOT NULL COMMENT '权限代码',
    `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 创建菜单表
CREATE TABLE IF NOT EXISTS `menu` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) NOT NULL COMMENT '菜单名称',
    `path` varchar(200) DEFAULT NULL COMMENT '菜单路径',
    `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
    `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- 创建角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `permission_id` bigint NOT NULL COMMENT '权限ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 创建角色菜单关联表
CREATE TABLE IF NOT EXISTS `role_menu` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 插入初始数据

-- 插入管理员用户 (密码: admin123, 使用BCrypt加密)
INSERT INTO `user` (`username`, `password`, `email`) VALUES
('dcadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxp7UeVxGzMsK/u', 'admin@jcore.com');

-- 插入角色数据
INSERT INTO `role` (`name`, `description`) VALUES
('超级管理员', '拥有系统所有权限'),
('管理员', '拥有大部分管理权限'),
('普通用户', '普通用户权限');

-- 插入权限数据
INSERT INTO `permission` (`name`, `code`, `description`) VALUES
('用户管理', 'user:manage', '用户增删改查权限'),
('角色管理', 'role:manage', '角色增删改查权限'),
('权限管理', 'permission:manage', '权限增删改查权限'),
('菜单管理', 'menu:manage', '菜单增删改查权限'),
('用户查看', 'user:read', '查看用户信息权限'),
('角色查看', 'role:read', '查看角色信息权限'),
('权限查看', 'permission:read', '查看权限信息权限'),
('菜单查看', 'menu:read', '查看菜单信息权限');

-- 插入菜单数据
INSERT INTO `menu` (`name`, `path`, `icon`, `parent_id`, `sort_order`) VALUES
('系统管理', '/system', 'setting', NULL, 1),
('用户管理', '/system/user', 'user', 1, 1),
('角色管理', '/system/role', 'team', 1, 2),
('权限管理', '/system/permission', 'lock', 1, 3),
('菜单管理', '/system/menu', 'menu', 1, 4),
('监控中心', '/monitor', 'dashboard', NULL, 2),
('系统监控', '/monitor/system', 'monitor', 6, 1),
('日志监控', '/monitor/log', 'file-text', 6, 2);

-- 关联管理员用户和超级管理员角色
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1);

-- 关联超级管理员角色的所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8);

-- 关联管理员角色的部分权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(2, 5), (2, 6), (2, 7), (2, 8);

-- 关联普通用户的查看权限
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(3, 5), (3, 6), (3, 7), (3, 8);

-- 关联超级管理员角色的所有菜单
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8);

-- 关联管理员角色的部分菜单
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
(2, 1), (2, 2), (2, 3), (2, 6), (2, 7);

-- 关联普通用户的查看菜单
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES
(3, 6), (3, 7);
