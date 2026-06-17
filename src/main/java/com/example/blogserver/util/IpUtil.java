package com.example.blogserver.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * 客户端 IP 获取工具（兼容常见反向代理头）
 */
public final class IpUtil {

    private static final String UNKNOWN = "unknown";

    private IpUtil() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (isValid(ip)) {
            // 多级代理时取第一个非 unknown 的 IP
            int comma = ip.indexOf(',');
            return comma > 0 ? ip.substring(0, comma).trim() : ip.trim();
        }
        ip = firstValid(request, "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP");
        if (isValid(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        // 本机 IPv6 回环归一化
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private static String firstValid(HttpServletRequest request, String... headers) {
        for (String h : headers) {
            String v = request.getHeader(h);
            if (isValid(v)) {
                return v;
            }
        }
        return null;
    }

    private static boolean isValid(String ip) {
        return StringUtils.hasText(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }
}
