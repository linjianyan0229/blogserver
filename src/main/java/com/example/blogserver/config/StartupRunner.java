package com.example.blogserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 应用启动完成后打印清晰的启动信息
 */
@Slf4j
@Component
public class StartupRunner {

    private final Environment env;

    public StartupRunner(Environment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String host = "localhost";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }
        String base = "http://localhost:" + port + contextPath;

        log.info("\n" +
                "----------------------------------------------------------------------------\n" +
                "  博客后端系统启动成功！ Blog Server is running!\n" +
                "----------------------------------------------------------------------------\n" +
                "  接口根地址  本地: {}\n" +
                "              局域网: http://{}:{}{}\n" +
                "  Swagger文档       : {}/swagger-ui.html\n" +
                "  OpenAPI(JSON)     : {}/v3/api-docs\n" +
                "----------------------------------------------------------------------------\n" +
                "  默认管理员  邮箱: admin@blog.com   密码: admin123\n" +
                "  演示用户    邮箱: user@blog.com    密码: 123456\n" +
                "  数据库      MySQL  库名: blog  账号/密码: root/123456\n" +
                "----------------------------------------------------------------------------",
                base, host, port, contextPath, base, base);
    }
}
