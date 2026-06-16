package com.example.blogserver.security;

import com.example.blogserver.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Spring Security 登录用户主体，封装用户实体、角色与权限
 */
@Getter
public class LoginUser implements UserDetails {

    private final User user;

    /** 角色编码集合，如 ADMIN、USER */
    private final Set<String> roles;

    /** 权限编码集合，如 article:create */
    private final Set<String> permissions;

    private final List<GrantedAuthority> authorities;

    public LoginUser(User user, Set<String> roles, Set<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
        // 角色加 ROLE_ 前缀，权限保留原编码
        this.authorities = Stream.concat(
                        roles.stream().map(r -> "ROLE_" + r),
                        permissions.stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /** 登录账号使用邮箱 */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() != null && user.getStatus() == 1;
    }
}
