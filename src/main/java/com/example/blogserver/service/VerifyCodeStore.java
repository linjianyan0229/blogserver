package com.example.blogserver.service;

import com.example.blogserver.config.properties.MailProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 邮箱验证码本地存储（基于 Caffeine，自动过期）
 * key 形如 register:邮箱 / reset:邮箱
 */
@Component
@RequiredArgsConstructor
public class VerifyCodeStore {

    private final MailProperties mailProperties;
    private Cache<String, String> cache;

    @PostConstruct
    public void init() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(mailProperties.getCodeExpireMinutes(), TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
    }

    public void save(String type, String email, String code) {
        cache.put(key(type, email), code);
    }

    public String get(String type, String email) {
        return cache.getIfPresent(key(type, email));
    }

    public void remove(String type, String email) {
        cache.invalidate(key(type, email));
    }

    /**
     * 校验并消费验证码
     */
    public boolean verify(String type, String email, String code) {
        String saved = get(type, email);
        if (saved != null && saved.equalsIgnoreCase(code)) {
            remove(type, email);
            return true;
        }
        return false;
    }

    private String key(String type, String email) {
        return type + ":" + email;
    }
}
