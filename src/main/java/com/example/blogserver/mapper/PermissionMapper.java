package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限 Mapper
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询用户拥有的权限编码（用于 Spring Security 鉴权）
     */
    @Select("SELECT DISTINCT p.code FROM permission p " +
            "JOIN role_permission rp ON p.id = rp.permission_id " +
            "JOIN user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.code IS NOT NULL AND p.code <> ''")
    List<String> selectCodesByUserId(Long userId);

    /**
     * 查询角色拥有的权限ID
     */
    @Select("SELECT permission_id FROM role_permission WHERE role_id = #{roleId}")
    List<Long> selectPermissionIdsByRoleId(Long roleId);
}
