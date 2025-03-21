package org.ricky.common.exception;

import lombok.Getter;


/**
 * @author Ricky
 * @version 1.0
 * @date 2024/9/9
 * @className ErrorCodeEnum
 * @desc 错误码
 * 400：请求验证错误，请求中资源所属关系错误等
 * 401：认证错误
 * 403：权限不够
 * 404：资源未找到
 * 409：业务异常
 * 426：套餐检查失败
 * 500：系统错误
 */
@Getter
public enum ErrorCodeEnum {

    // 400
    PARAMS_ERROR(400),
    INVALID_EPS(400),
    INVALID_ENUM_KEY(400),
    REQUEST_VALIDATION_FAILED(400),
    INVALID_CMD_ARGS(400),
    INVALID_EXPRESSION(400),
    INVALID_CHARACTER(400),
    INVALID_NUMBER(400),
    INVALID_MEG_HANDLER_ARGS(400),

    // 401
    AUTHENTICATION_FAILED(401),

    // 403
    ACCESS_DENIED(403),

    // 404
    NOT_FOUND(404),
    AR_NOT_FOUND(404),
    AR_NOT_FOUND_ALL(404),
    DOMAIN_EVENT_NOT_FOUND(404),
    ANNOTATION_NOT_FOUND(404),
    CITY_NOT_FOUND(404),

    // 409
    PROCESS_ERROR(409),
    RPC_FAILED(409),
    HANDLING_ERROR(409),
    UNSUPPORTED_EXCEPTION_TYPE(409),
    TOKENIZATION_ERROR(409),
    MISMATCHED_PARENTHESES(409),
    UNEXPECTED_TOKEN(409),
    INSUFFICIENT_OPERANDS(409),
    MALFORMED_EXPRESSION(409),
    DIVIDED_BY_ZERO(409),
    UNKNOWN_OPERATOR(409),

    // 426

    // 429
    TOO_MANY_REQUEST(429),

    // 500
    SYSTEM_ERROR(500),
    ;

    /**
     * 错误码
     */
    final int status;

    ErrorCodeEnum(int status) {
        this.status = status;
    }

}
