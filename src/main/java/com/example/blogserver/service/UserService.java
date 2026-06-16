package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.common.PageResult;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.dto.user.*;
import com.example.blogserver.entity.Role;
import com.example.blogserver.entity.User;
import com.example.blogserver.entity.UserRole;
import com.example.blogserver.mapper.RoleMapper;
import com.example.blogserver.mapper.UserMapper;
import com.example.blogserver.mapper.UserRoleMapper;
import com.example.blogserver.vo.UserAdminVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务：个人中心 + 后台用户管理
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    // ==================== 个人中心 ====================

    public void updateProfile(Long userId, UpdateProfileDTO dto) {
        User user = new User();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setBio(dto.getBio());
        user.setGender(dto.getGender());
        userMapper.updateById(user);
    }

    public void changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.OLD_PASSWORD_ERROR);
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(update);
    }

    // ==================== 后台用户管理 ====================

    public PageResult<UserAdminVO> adminPage(UserQueryDTO query) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(query.getStatus() != null, User::getStatus, query.getStatus())
                .orderByDesc(User::getCreateTime);
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(User::getUsername, query.getKeyword())
                    .or().like(User::getEmail, query.getKeyword())
                    .or().like(User::getNickname, query.getKeyword()));
        }
        IPage<User> page = userMapper.selectPage(new Page<>(query.getPage(), query.getSize()), wrapper);
        List<UserAdminVO> records = page.getRecords().stream().map(this::toAdminVO).collect(Collectors.toList());
        return PageResult.of(page, records);
    }

    public UserAdminVO getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        return toAdminVO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(UserSaveDTO dto) {
        // 唯一性校验
        if (userMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getEmail, dto.getEmail()).ne(dto.getId() != null, User::getId, dto.getId()))) {
            throw new BusinessException(ResultCode.EMAIL_ALREADY_EXIST);
        }
        if (userMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, dto.getUsername()).ne(dto.getId() != null, User::getId, dto.getId()))) {
            throw new BusinessException(ResultCode.USERNAME_ALREADY_EXIST);
        }
        User user = new User();
        BeanUtils.copyProperties(dto, user, "password", "roleIds");
        if (dto.getId() == null) {
            if (!StringUtils.hasText(dto.getPassword())) {
                throw new BusinessException("新增用户必须设置密码");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            userMapper.insert(user);
        } else {
            if (StringUtils.hasText(dto.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            userMapper.updateById(user);
        }
        // 重建角色关联
        if (dto.getRoleIds() != null) {
            assignRoles(user.getId(), dto.getRoleIds());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (id != null && id == 1L) {
            throw new BusinessException("超级管理员账号不可删除");
        }
        if (userMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, id));
    }

    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                userRoleMapper.insert(new UserRole(userId, roleId));
            }
        }
    }

    private UserAdminVO toAdminVO(User user) {
        UserAdminVO vo = new UserAdminVO();
        BeanUtils.copyProperties(user, vo);
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        vo.setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toList()));
        vo.setRoleNames(roles.stream().map(Role::getName).collect(Collectors.toList()));
        return vo;
    }
}
