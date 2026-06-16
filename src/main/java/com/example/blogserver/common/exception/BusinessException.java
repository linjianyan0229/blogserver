package com.example.blogserver.common.exception;

import com.example.blogserver.common.ResultCode;
import lombok.Getter;

/**
 * 业务异常，由全局异常处理器统一捕获并返回 {@link com.example.blogserver.common.Result}
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAILED.getCode();
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
