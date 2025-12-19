package com.dc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dc.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Select("""
            <script>
            SELECT DISTINCT p.code
            FROM role_permission rp
            JOIN permission p ON rp.permission_id = p.id
            WHERE rp.role_id IN
            <foreach collection="roleIds" item="roleId" open="(" close=")" separator=",">
                #{roleId}
            </foreach>
            </script>
            """)
    List<String> getPermissionCodesByRoleIds(@Param("roleIds") List<Long> roleIds);
}
