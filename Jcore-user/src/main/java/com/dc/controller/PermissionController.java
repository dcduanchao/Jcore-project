package com.dc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.entity.Permission;
import com.dc.mapper.PermissionMapper;
import com.dc.result.ResultVo;
import com.dc.auth.RequiresPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionMapper permissionMapper;

    /**
     * 获取权限列表（分页）
     */
    @GetMapping("/list")
    @RequiresPermission("permission:manage")
    public ResultVo getPermissionList(@RequestParam(defaultValue = "1") Integer current,
                                     @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Permission> page = new Page<>(current, size);
            Page<Permission> result = permissionMapper.selectPage(page, null);
            return ResultVo.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取权限列表失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有权限
     */
    @GetMapping("/all")
    public ResultVo getAllPermissions() {
        try {
            List<Permission> permissions = permissionMapper.selectList(null);
            return ResultVo.success("获取成功", permissions);
        } catch (Exception e) {
            log.error("获取所有权限失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取权限
     */
    @GetMapping("/{id}")
    @RequiresPermission("permission:manage")
    public ResultVo getPermissionById(@PathVariable Long id) {
        try {
            Permission permission = permissionMapper.selectById(id);
            if (permission != null) {
                return ResultVo.success("获取成功", permission);
            } else {
                return ResultVo.error("权限不存在");
            }
        } catch (Exception e) {
            log.error("获取权限失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 新增权限
     */
    @PostMapping
    @RequiresPermission("permission:manage")
    public ResultVo addPermission(@RequestBody Permission permission) {
        try {
            // 检查权限代码是否已存在
            QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", permission.getCode());
            Permission existingPermission = permissionMapper.selectOne(queryWrapper);
            if (existingPermission != null) {
                return ResultVo.error("权限代码已存在");
            }

            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            int result = permissionMapper.insert(permission);
            if (result > 0) {
                return ResultVo.success("新增成功");
            } else {
                return ResultVo.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增权限失败", e);
            return ResultVo.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    @RequiresPermission("permission:manage")
    public ResultVo updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        try {
            Permission existingPermission = permissionMapper.selectById(id);
            if (existingPermission == null) {
                return ResultVo.error("权限不存在");
            }

            // 检查权限代码是否已被其他权限使用
            QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", permission.getCode()).ne("id", id);
            Permission duplicatePermission = permissionMapper.selectOne(queryWrapper);
            if (duplicatePermission != null) {
                return ResultVo.error("权限代码已存在");
            }

            permission.setId(id);
            permission.setUpdateTime(LocalDateTime.now());
            int result = permissionMapper.updateById(permission);
            if (result > 0) {
                return ResultVo.success("更新成功");
            } else {
                return ResultVo.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新权限失败", e);
            return ResultVo.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("permission:manage")
    public ResultVo deletePermission(@PathVariable Long id) {
        try {
            Permission permission = permissionMapper.selectById(id);
            if (permission == null) {
                return ResultVo.error("权限不存在");
            }

            int result = permissionMapper.deleteById(id);
            if (result > 0) {
                return ResultVo.success("删除成功");
            } else {
                return ResultVo.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除权限失败", e);
            return ResultVo.error("删除失败：" + e.getMessage());
        }
    }
}
