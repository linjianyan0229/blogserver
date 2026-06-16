package com.example.blogserver.security;

import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 当前登录用户工具
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前登录用户，未登录返回 null（用于游客可选场景）
     */
    public static LoginUser getLoginUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            return loginUser;
        }
        return null;
    }

    /**
     * 获取当前登录用户，未登录抛出 401
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = getLoginUserOrNull();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return loginUser;
    }

    /**
     * 获取当前登录用户ID，未登录抛出 401
     */
    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 是否已登录
     */
    public static boolean isAuthenticated() {
        return getLoginUserOrNull() != null;
    }
}
