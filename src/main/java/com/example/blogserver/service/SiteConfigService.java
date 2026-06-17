package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.dto.SiteConfigItemDTO;
import com.example.blogserver.entity.SiteConfig;
import com.example.blogserver.mapper.SiteConfigMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 站点基础配置服务：公开读取（带缓存）+ 后台维护
 */
@Service
@RequiredArgsConstructor
public class SiteConfigService {

    /** 私密配置前缀：以此开头的键不会从公开接口返回（如邮箱 SMTP 配置） */
    private static final String PRIVATE_PREFIX = "mail_";

    private final SiteConfigMapper siteConfigMapper;

    /** 公开配置缓存（60 秒），后台更新时主动失效 */
    private Cache<String, Map<String, String>> cache;

    @PostConstruct
    public void init() {
        this.cache = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    /**
     * 公开读取：返回 key→value 的配置 Map（前端按 key 取用）
     */
    public Map<String, String> getPublicConfig() {
        return cache.get("all", k -> {
            Map<String, String> map = new LinkedHashMap<>();
            for (SiteConfig c : listAll()) {
                // 私密键（如邮箱配置）不对外暴露
                if (c.getConfigKey() != null && c.getConfigKey().startsWith(PRIVATE_PREFIX)) {
                    continue;
                }
                map.put(c.getConfigKey(), c.getConfigValue());
            }
            return map;
        });
    }

    /**
     * 读取单个配置值（含私密项，仅供服务端内部使用，如邮件服务）
     */
    public String getValue(String key) {
        SiteConfig config = siteConfigMapper.selectOne(
                Wrappers.<SiteConfig>lambdaQuery().eq(SiteConfig::getConfigKey, key));
        return config == null ? null : config.getConfigValue();
    }

    /**
     * 后台读取：完整配置列表（含中文名/类型，供后台表单渲染）
     */
    public List<SiteConfig> listAll() {
        return siteConfigMapper.selectList(Wrappers.<SiteConfig>lambdaQuery().orderByAsc(SiteConfig::getSort));
    }

    /**
     * 批量更新配置（按 key 存在则更新值，不存在则新增）
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<SiteConfigItemDTO> items) {
        for (SiteConfigItemDTO item : items) {
            if (!StringUtils.hasText(item.getConfigKey())) {
                continue;
            }
            SiteConfig existing = siteConfigMapper.selectOne(
                    Wrappers.<SiteConfig>lambdaQuery().eq(SiteConfig::getConfigKey, item.getConfigKey()));
            if (existing != null) {
                existing.setConfigValue(item.getConfigValue());
                if (StringUtils.hasText(item.getName())) existing.setName(item.getName());
                if (StringUtils.hasText(item.getType())) existing.setType(item.getType());
                if (item.getSort() != null) existing.setSort(item.getSort());
                siteConfigMapper.updateById(existing);
            } else {
                SiteConfig config = new SiteConfig();
                config.setConfigKey(item.getConfigKey());
                config.setConfigValue(item.getConfigValue());
                config.setName(item.getName());
                config.setType(StringUtils.hasText(item.getType()) ? item.getType() : "text");
                config.setSort(item.getSort() == null ? 0 : item.getSort());
                siteConfigMapper.insert(config);
            }
        }
        cache.invalidateAll();
    }
}
