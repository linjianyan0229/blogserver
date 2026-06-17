package com.example.blogserver.service;

import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.config.properties.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 邮件发送服务（SMTP）
 * 邮箱配置优先取站点配置（site_config 的 mail_* 键，可在后台在线修改），
 * 留空则回退到 application.yml 的 spring.mail.*，改完即时生效、无需重启。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final MailProperties mailProperties;
    private final SiteConfigService siteConfigService;
    private final Environment env;

    /**
     * 根据当前配置（DB 优先，yml 回退）构建邮件发送器
     */
    private JavaMailSenderImpl buildSender() {
        String host = effective("mail_host", "spring.mail.host", "smtp.qq.com");
        int port = parsePort(effective("mail_port", "spring.mail.port", "465"));
        String username = effective("mail_username", "spring.mail.username", "");
        String password = effective("mail_password", "spring.mail.password", "");
        boolean ssl = Boolean.parseBoolean(
                effective("mail_ssl_enable", "spring.mail.properties.mail.smtp.ssl.enable", "true"));

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setDefaultEncoding("UTF-8");
        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        if (ssl) {
            sender.setProtocol("smtps");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        } else {
            sender.setProtocol("smtp");
        }
        return sender;
    }

    /** 发件邮箱（=SMTP 用户名） */
    private String getFrom() {
        return effective("mail_username", "spring.mail.username", "");
    }

    /** 发件人显示名 */
    private String getFromName() {
        String name = siteConfigService.getValue("mail_from_name");
        return StringUtils.hasText(name) ? name : mailProperties.getFromName();
    }

    /**
     * 取有效配置：DB(site_config) 非空优先，否则取 application.yml，再否则默认值
     */
    private String effective(String dbKey, String envKey, String defaultValue) {
        String dbValue = siteConfigService.getValue(dbKey);
        if (StringUtils.hasText(dbValue)) {
            return dbValue.trim();
        }
        String envValue = env.getProperty(envKey);
        return StringUtils.hasText(envValue) ? envValue : defaultValue;
    }

    private int parsePort(String port) {
        try {
            return Integer.parseInt(port.trim());
        } catch (NumberFormatException e) {
            return 465;
        }
    }

    /**
     * 发送验证码邮件（HTML）
     */
    public void sendVerifyCode(String to, String code, String subject, String scene) {
        String html = """
                <div style="max-width:600px;margin:0 auto;font-family:Helvetica,Arial,sans-serif;border:1px solid #eee;border-radius:8px;overflow:hidden">
                  <div style="background:#409EFF;color:#fff;padding:20px;font-size:20px">博客系统 · %s</div>
                  <div style="padding:30px;color:#333;font-size:15px;line-height:1.8">
                    <p>您好，您正在进行<strong>%s</strong>操作，验证码为：</p>
                    <p style="font-size:32px;font-weight:bold;letter-spacing:6px;color:#409EFF;text-align:center;margin:24px 0">%s</p>
                    <p>验证码 %d 分钟内有效，请勿泄露给他人。如非本人操作，请忽略本邮件。</p>
                  </div>
                  <div style="background:#fafafa;color:#999;padding:16px;font-size:12px;text-align:center">本邮件由系统自动发送，请勿回复</div>
                </div>
                """.formatted(scene, scene, code, mailProperties.getCodeExpireMinutes());
        sendHtml(to, subject, html);
    }

    /**
     * 发送测试邮件（后台验证邮箱配置是否可用）
     */
    public void sendTest(String to) {
        sendHtml(to, "【博客系统】邮箱配置测试",
                "<p>这是一封测试邮件，如果你收到它，说明当前邮箱(SMTP)配置可正常发送。</p>");
    }

    /**
     * 发送 HTML 邮件
     */
    public void sendHtml(String to, String subject, String htmlContent) {
        String from = getFrom();
        if (!StringUtils.hasText(from)) {
            throw new BusinessException("发件邮箱未配置，请在后台站点配置中设置 mail_username 或在配置文件中配置 spring.mail.username");
        }
        try {
            JavaMailSenderImpl sender = buildSender();
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from, getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            sender.send(message);
            log.info("邮件发送成功 -> {}（{}）", to, subject);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("邮件发送失败 -> {}：{}", to, e.getMessage());
            throw new BusinessException("邮件发送失败，请检查邮箱配置：" + e.getMessage());
        } catch (org.springframework.mail.MailException e) {
            log.error("邮件发送失败 -> {}：{}", to, e.getMessage());
            throw new BusinessException("邮件发送失败，请检查邮箱配置：" + e.getMessage());
        }
    }
}
