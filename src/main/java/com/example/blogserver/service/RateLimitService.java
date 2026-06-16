package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.entity.ApiRateLimit;
import com.example.blogserver.mapper.ApiRateLimitMapper;
import com.example.blogserver.security.LoginUser;
import com.example.blogserver.security.SecurityUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 接口限流服务：规则可在后台动态配置（API 管理配置），本地计数，无需 Redis。
 * 采用固定窗口计数算法。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final ApiRateLimitMapper rateLimitMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /** 规则缓存（10 秒刷新一次，避免每请求查库） */
    private Cache<String, List<ApiRateLimit>> ruleCache;
    /** 计数缓存（固定窗口） */
    private Cache<String, AtomicInteger> counterCache;

    @PostConstruct
    public void init() {
        this.ruleCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS).build();
        this.counterCache = Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.HOURS).maximumSize(200_000).build();
    }

    /**
     * 校验请求是否超出限流；超出则抛出 429 业务异常
     *
     * @param request     当前请求
     * @param pathNoCtx   去掉 context-path 的请求路径
     */
    public void check(HttpServletRequest request, String pathNoCtx) {
        List<ApiRateLimit> rules = getEnabledRules();
        if (rules.isEmpty()) {
            return;
        }
        String method = request.getMethod();
        for (ApiRateLimit rule : rules) {
            if (!methodMatch(rule.getMethod(), method)) {
                continue;
            }
            if (!pathMatcher.match(rule.getUrlPattern(), pathNoCtx)) {
                continue;
            }
            if (!tryAcquire(rule, dimensionValue(rule.getDimension(), request))) {
                log.warn("触发限流：规则[{}] 路径[{}] 维度[{}]", rule.getName(), pathNoCtx, rule.getDimension());
                throw new BusinessException(ResultCode.TOO_MANY_REQUESTS.getCode(),
                        ResultCode.TOO_MANY_REQUESTS.getMessage() + "（" + rule.getName() + "）");
            }
        }
    }

    /**
     * 固定窗口计数：当前窗口内 +1 后是否仍在阈值内
     */
    private boolean tryAcquire(ApiRateLimit rule, String dimValue) {
        long windowIndex = Instant.now().getEpochSecond() / rule.getWindowSeconds();
        String key = "rl:" + rule.getId() + ":" + dimValue + ":" + windowIndex;
        AtomicInteger counter = counterCache.get(key, k -> new AtomicInteger(0));
        return counter.incrementAndGet() <= rule.getMaxRequests();
    }

    private boolean methodMatch(String ruleMethod, String requestMethod) {
        return ruleMethod == null || "ALL".equalsIgnoreCase(ruleMethod)
                || ruleMethod.equalsIgnoreCase(requestMethod);
    }

    private String dimensionValue(String dimension, HttpServletRequest request) {
        if ("GLOBAL".equalsIgnoreCase(dimension)) {
            return "global";
        }
        if ("USER".equalsIgnoreCase(dimension)) {
            LoginUser user = SecurityUtils.getLoginUserOrNull();
            if (user != null) {
                return "u" + user.getUserId();
            }
            // 未登录则退化为按 IP
        }
        return "ip" + getClientIp(request);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int comma = ip.indexOf(',');
            return comma > 0 ? ip.substring(0, comma).trim() : ip.trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private List<ApiRateLimit> getEnabledRules() {
        return ruleCache.get("enabled", k ->
                rateLimitMapper.selectList(Wrappers.<ApiRateLimit>lambdaQuery().eq(ApiRateLimit::getEnabled, 1)));
    }

    // ==================== 后台 CRUD（API 管理配置） ====================

    public List<ApiRateLimit> list() {
        return rateLimitMapper.selectList(Wrappers.<ApiRateLimit>lambdaQuery().orderByDesc(ApiRateLimit::getId));
    }

    public void save(ApiRateLimit rule) {
        if (rule.getId() == null) {
            rateLimitMapper.insert(rule);
        } else {
            rateLimitMapper.updateById(rule);
        }
        ruleCache.invalidateAll();
    }

    public void delete(Long id) {
        if (rateLimitMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.RESOURCE_NOT_EXIST);
        }
        ruleCache.invalidateAll();
    }
}
