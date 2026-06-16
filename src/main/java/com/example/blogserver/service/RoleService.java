package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.role.RoleSaveDTO;
import com.example.blogserver.entity.Role;
import com.example.blogserver.entity.RolePermission;
import com.example.blogserver.entity.UserRole;
import com.example.blogserver.mapper.PermissionMapper;
import com.example.blogserver.mapper.RoleMapper;
import com.example.blogserver.mapper.RolePermissionMapper;
import com.example.blogserver.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务（含权限分配）
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;

    public List<Role> list() {
        return roleMapper.selectList(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getId));
    }

    /**
     * 角色已分配的权限ID（用于配置页回显）
     */
    public List<Long> getPermissionIds(Long roleId) {
        return permissionMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(RoleSaveDTO dto) {
        if (roleMapper.exists(Wrappers.<Role>lambdaQuery()
                .eq(Role::getCode, dto.getCode()).ne(dto.getId() != null, Role::getId, dto.getId()))) {
            throw new BusinessException("角色编码已存在");
        }
        Role role = new Role();
        BeanUtils.copyProperties(dto, role, "permissionIds");
        if (dto.getId() == null) {
            roleMapper.insert(role);
        } else {
            roleMapper.updateById(role);
        }
        if (dto.getPermissionIds() != null) {
            assignPermissions(role.getId(), dto.getPermissionIds());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (id != null && (id == 1L || id == 2L)) {
            throw new BusinessException("内置角色不可删除");
        }
        if (roleMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        rolePermissionMapper.delete(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, id));
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, id));
    }

    /**
     * 为角色分配权限（权限配置页面）
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionMapper.delete(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId));
        if (permissionIds != null) {
            for (Long pid : permissionIds) {
                rolePermissionMapper.insert(new RolePermission(roleId, pid));
            }
        }
    }
}
