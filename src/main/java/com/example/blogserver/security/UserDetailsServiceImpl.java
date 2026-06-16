package com.example.blogserver.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.entity.User;
import com.example.blogserver.mapper.PermissionMapper;
import com.example.blogserver.mapper.RoleMapper;
import com.example.blogserver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * 根据邮箱加载用户及其角色、权限
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        if (user == null) {
            throw new UsernameNotFoundException("邮箱未注册：" + email);
        }
        return buildLoginUser(user);
    }

    /**
     * 通过用户ID构建登录主体（供 JWT 过滤器使用）
     */
    public LoginUser loadByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在：" + userId);
        }
        return buildLoginUser(user);
    }

    private LoginUser buildLoginUser(User user) {
        var roles = new HashSet<>(roleMapper.selectRoleCodesByUserId(user.getId()));
        var permissions = new HashSet<>(permissionMapper.selectCodesByUserId(user.getId()));
        return new LoginUser(user, roles, permissions);
    }
}
