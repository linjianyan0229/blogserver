package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.common.PageResult;
import com.example.blogserver.entity.VisitLog;
import com.example.blogserver.mapper.VisitLogMapper;
import com.example.blogserver.security.LoginUser;
import com.example.blogserver.security.SecurityUtils;
import com.example.blogserver.util.IpUtil;
import com.example.blogserver.vo.VisitStatsVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

/**
 * 网站访问统计服务
 */
@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitLogMapper visitLogMapper;

    /**
     * 记录一次访问，返回最新总访问量(PV)
     */
    public long record(HttpServletRequest request, String path) {
        VisitLog log = new VisitLog();
        log.setIp(IpUtil.getIpAddr(request));
        log.setPath(truncate(StringUtils.hasText(path) ? path : request.getRequestURI(), 255));
        log.setReferer(truncate(request.getHeader("Referer"), 512));
        log.setUserAgent(truncate(request.getHeader("User-Agent"), 512));
        LoginUser loginUser = SecurityUtils.getLoginUserOrNull();
        log.setUserId(loginUser == null ? null : loginUser.getUserId());
        log.setVisitDate(LocalDate.now());
        visitLogMapper.insert(log);
        return visitLogMapper.countPv();
    }

    /**
     * 获取访问量统计（总 PV/UV、今日 PV/UV）
     */
    public VisitStatsVO getStats() {
        LocalDate today = LocalDate.now();
        return VisitStatsVO.builder()
                .totalPv(visitLogMapper.countPv())
                .totalUv(visitLogMapper.countUv())
                .todayPv(visitLogMapper.countPvByDate(today))
                .todayUv(visitLogMapper.countUvByDate(today))
                .build();
    }

    /**
     * 获取当前请求的客户端 IP
     */
    public String getClientIp(HttpServletRequest request) {
        return IpUtil.getIpAddr(request);
    }

    /**
     * 后台分页查询访问日志（可按 IP 过滤）
     */
    public PageResult<VisitLog> adminPage(String ip, Long page, Long size) {
        IPage<VisitLog> result = visitLogMapper.selectPage(new Page<>(page, size),
                Wrappers.<VisitLog>lambdaQuery()
                        .like(StringUtils.hasText(ip), VisitLog::getIp, ip)
                        .orderByDesc(VisitLog::getId));
        return PageResult.of(result);
    }

    private String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() > max ? value.substring(0, max) : value;
    }
}
