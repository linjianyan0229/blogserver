package com.example.blogserver.service;

import com.example.blogserver.config.properties.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 邮件发送服务（SMTP）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送验证码邮件（HTML）
     *
     * @param to      收件邮箱
     * @param code    验证码
     * @param subject 邮件主题
     * @param scene   场景描述（注册/重置密码）
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
     * 发送 HTML 邮件
     */
    public void sendHtml(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from, mailProperties.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("邮件发送成功 -> {}（{}）", to, subject);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("邮件发送失败 -> {}：{}", to, e.getMessage());
            throw new RuntimeException("邮件发送失败，请检查邮箱配置：" + e.getMessage());
        }
    }
}
