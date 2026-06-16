package com.example.blogserver.interceptor;

import com.example.blogserver.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 限流拦截器：依据后台配置的规则对请求进行限流。
 * 超限时抛出业务异常，由全局异常处理器返回 429。
 */
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathNoCtx = (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath))
                ? uri.substring(contextPath.length()) : uri;
        rateLimitService.check(request, pathNoCtx);
        return true;
    }
}
