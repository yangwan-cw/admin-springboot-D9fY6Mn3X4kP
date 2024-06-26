package com.ioomex.ioomexadminspringboot.common;

import lombok.Data;
import lombok.Getter;

/**
 * 错误码枚举类
 *
 * @author ioomex
 */
@Getter
public enum ErrorCode {
    SUCCESS(200, "请求成功", "ok"),

    PARAM_ERROR(40000, "请求参数错误", "参数错误"),

    NULL_ERROR(40001, "请求数据错误", "查询数据为空"),
    PARAM_NULL_ERROR(40002, "请求参数错误", "参数为空"),
    DATA_ERROR(40003, "请求数据错误", "数据错误"),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH_ERROR(40101, "无权限", "");


    /**
     * 错误码
     */
    private final Integer code;


    /**
     * 错误信息
     */
    private final String message;


    /**
     * 错误描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}