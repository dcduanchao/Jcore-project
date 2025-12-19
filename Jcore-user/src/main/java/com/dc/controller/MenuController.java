package com.dc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc.entity.Menu;
import com.dc.mapper.MenuMapper;
import com.dc.mapper.UserMapper;
import com.dc.result.ResultVo;
import com.dc.auth.RequiresPermission;
import com.dc.auth.UserSessionContext;
import com.dc.auth.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuMapper menuMapper;

    /**
     * 获取菜单列表（分页）
     */
    @GetMapping("/list")
    @RequiresPermission("menu:manage")
    public ResultVo getMenuList(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Menu> page = new Page<>(current, size);
            Page<Menu> result = menuMapper.selectPage(page, null);
            return ResultVo.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取菜单列表失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有菜单
     */
    @GetMapping("/all")
    public ResultVo getAllMenus() {
        try {
            List<Menu> menus = menuMapper.selectList(null);
            return ResultVo.success("获取成功", menus);
        } catch (Exception e) {
            log.error("获取所有菜单失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取菜单树形结构
     */
    @GetMapping("/tree")
    public ResultVo getMenuTree() {
        try {
            List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().orderByAsc("sort_order"));
            List<Menu> tree = buildMenuTree(menus);
            return ResultVo.success("获取成功", tree);
        } catch (Exception e) {
            log.error("获取菜单树失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户角色获取菜单栏
     */
    @GetMapping("/user")
    public ResultVo getUserMenus() {
        try {
            // 获取当前用户信息
            UserSession userSession = UserSessionContext.getUserSession();
            if (userSession == null) {
                return ResultVo.error("用户未登录");
            }
            List<Menu> userMenus = new ArrayList<>();
            List<Long> roles = userSession.getRoles();
            if(roles.contains(1L)){
               userMenus = menuMapper.selectList(new QueryWrapper<Menu>().orderByAsc("sort_order"));
            }else {
                // 使用原生SQL查询用户有权限的菜单
                // 这里通过联表查询获取用户角色对应的菜单权限
             userMenus = menuMapper.selectList(
                        new QueryWrapper<Menu>()
                                .inSql("id",
                                        "SELECT DISTINCT rm.menu_id FROM user_role ur " +
                                                "JOIN role_menu rm ON ur.role_id = rm.role_id " +
                                                "WHERE ur.user_id = " + userSession.getUserId()
                                )
                                .orderByAsc("sort_order")
                );

            }


            if (userMenus.isEmpty()) {
                return ResultVo.success("获取成功", List.of());
            }

            // 构建菜单树
            List<Menu> menuTree = buildMenuTree(userMenus);

            return ResultVo.success("获取成功", menuTree);
        } catch (Exception e) {
            log.error("获取用户菜单失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取菜单
     */
    @GetMapping("/{id}")
    @RequiresPermission("menu:manage")
    public ResultVo getMenuById(@PathVariable Long id) {
        try {
            Menu menu = menuMapper.selectById(id);
            if (menu != null) {
                return ResultVo.success("获取成功", menu);
            } else {
                return ResultVo.error("菜单不存在");
            }
        } catch (Exception e) {
            log.error("获取菜单失败", e);
            return ResultVo.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @RequiresPermission("menu:manage")
    public ResultVo addMenu(@RequestBody Menu menu) {
        try {
            menu.setCreateTime(LocalDateTime.now());
            menu.setUpdateTime(LocalDateTime.now());
            int result = menuMapper.insert(menu);
            if (result > 0) {
                return ResultVo.success("新增成功");
            } else {
                return ResultVo.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增菜单失败", e);
            return ResultVo.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 更新菜单
     */
    @PutMapping("/{id}")
    @RequiresPermission("menu:manage")
    public ResultVo updateMenu(@PathVariable Long id, @RequestBody Menu menu) {
        try {
            Menu existingMenu = menuMapper.selectById(id);
            if (existingMenu == null) {
                return ResultVo.error("菜单不存在");
            }

            menu.setId(id);
            menu.setUpdateTime(LocalDateTime.now());
            int result = menuMapper.updateById(menu);
            if (result > 0) {
                return ResultVo.success("更新成功");
            } else {
                return ResultVo.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新菜单失败", e);
            return ResultVo.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("menu:manage")
    public ResultVo deleteMenu(@PathVariable Long id) {
        try {
            Menu menu = menuMapper.selectById(id);
            if (menu == null) {
                return ResultVo.error("菜单不存在");
            }

            int result = menuMapper.deleteById(id);
            if (result > 0) {
                return ResultVo.success("删除成功");
            } else {
                return ResultVo.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除菜单失败", e);
            return ResultVo.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 构建菜单树形结构
     */
//    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
//        return menus.stream()
//                .filter(menu -> (parentId == null && menu.getParentId() == null) ||
//                               (parentId != null && parentId.equals(menu.getParentId())))
//                .peek(menu -> menu.setChildren(buildMenuTree(menus, menu.getId())))
//                .toList();
//    }
    /**
     * 构建菜单树
     */
    public static List<Menu> buildMenuTree(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 按 parentId 分组
        Map<Long, List<Menu>> parentMap =
                menus.stream()
                        .collect(Collectors.groupingBy(p->p.getParentId()==null ? 0L : p.getParentId()));

        // 2. 取出根节点
        List<Menu> roots = parentMap.getOrDefault(0L, Collections.emptyList());

        // 3. 递归设置 children
        for (Menu root : roots) {
            fillChildren(root, parentMap);
        }

        return roots;
    }

    /**
     * 递归填充子节点
     */
    private static void fillChildren(Menu parent,
                                     Map<Long, List<Menu>> parentMap) {

        List<Menu> children =
                parentMap.getOrDefault(parent.getId(), Collections.emptyList());

        parent.setChildren(children);

        for (Menu child : children) {
            fillChildren(child, parentMap);
        }
    }
}
