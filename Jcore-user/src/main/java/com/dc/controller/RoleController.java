package com.dc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.entity.Role;
import com.dc.mapper.RoleMapper;
import com.dc.result.ResultVo;
import com.dc.auth.RequiresPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleMapper roleMapper;

    /**
     * 获取角色列表（分页）
     */
    @GetMapping("/list")
    @RequiresPermission("role:manage")
    public ResultVo getRoleList(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Role> page = new Page<>(current, size);
            Page<Role> result = roleMapper.selectPage(page, null);
            return ResultVo.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有角色
     */
    @GetMapping("/all")
    public ResultVo getAllRoles() {
        try {
            List<Role> roles = roleMapper.selectList(null);
            return ResultVo.success("获取成功", roles);
        } catch (Exception e) {
            log.error("获取所有角色失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    @RequiresPermission("role:manage")
    public ResultVo getRoleById(@PathVariable Long id) {
        try {
            Role role = roleMapper.selectById(id);
            if (role != null) {
                return ResultVo.success("获取成功", role);
            } else {
                return ResultVo.error("角色不存在");
            }
        } catch (Exception e) {
            log.error("获取角色失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 新增角色
     */
    @PostMapping
    @RequiresPermission("role:manage")
    public ResultVo addRole(@RequestBody Role role) {
        try {
            // 检查角色名是否已存在
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", role.getName());
            Role existingRole = roleMapper.selectOne(queryWrapper);
            if (existingRole != null) {
                return ResultVo.error("角色名已存在");
            }

            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            int result = roleMapper.insert(role);
            if (result > 0) {
                return ResultVo.success("新增成功");
            } else {
                return ResultVo.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增角色失败", e);
            return ResultVo.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    @RequiresPermission("role:manage")
    public ResultVo updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            Role existingRole = roleMapper.selectById(id);
            if (existingRole == null) {
                return ResultVo.error("角色不存在");
            }

            // 检查角色名是否已被其他角色使用
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", role.getName()).ne("id", id);
            Role duplicateRole = roleMapper.selectOne(queryWrapper);
            if (duplicateRole != null) {
                return ResultVo.error("角色名已存在");
            }

            role.setId(id);
            role.setUpdateTime(LocalDateTime.now());
            int result = roleMapper.updateById(role);
            if (result > 0) {
                return ResultVo.success("更新成功");
            } else {
                return ResultVo.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新角色失败", e);
            return ResultVo.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("role:manage")
    public ResultVo deleteRole(@PathVariable Long id) {
        try {
            Role role = roleMapper.selectById(id);
            if (role == null) {
                return ResultVo.error("角色不存在");
            }

            int result = roleMapper.deleteById(id);
            if (result > 0) {
                return ResultVo.success("删除成功");
            } else {
                return ResultVo.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return ResultVo.error("删除失败：" + e.getMessage());
        }
    }
}
