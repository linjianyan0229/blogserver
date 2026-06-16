package com.example.blogserver.common;

import lombok.Getter;

/**
 * 统一返回状态码
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),

    PARAM_ERROR(400, "参数校验失败"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有访问权限"),
    NOT_FOUND(404, "请求的资源不存在"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后再试"),

    // 业务相关
    USERNAME_OR_PASSWORD_ERROR(1001, "邮箱或密码错误"),
    EMAIL_ALREADY_EXIST(1002, "邮箱已被注册"),
    USERNAME_ALREADY_EXIST(1003, "用户名已被占用"),
    EMAIL_NOT_EXIST(1004, "邮箱未注册"),
    VERIFY_CODE_ERROR(1005, "验证码错误或已过期"),
    ACCOUNT_DISABLED(1006, "账号已被禁用"),
    OLD_PASSWORD_ERROR(1007, "原密码错误"),
    TOKEN_INVALID(1008, "令牌无效或已过期"),
    RESOURCE_NOT_EXIST(1009, "数据不存在"),
    ARTICLE_NOT_PUBLIC(1010, "该文章未公开，请登录后查看");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
