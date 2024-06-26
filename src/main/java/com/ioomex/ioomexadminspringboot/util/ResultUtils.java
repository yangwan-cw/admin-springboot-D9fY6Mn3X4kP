package com.ioomex.ioomexadminspringboot.util;


import com.ioomex.ioomexadminspringboot.common.BaseResponse;
import com.ioomex.ioomexadminspringboot.common.ErrorCode;
import lombok.Data;

@Data
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "ok");
    }


    public static <T> BaseResponse<T> success(T data, Integer code) {
        return new BaseResponse<>(code, data, "ok");
    }


    public static <T> BaseResponse<T> success(T data, Integer code, String message) {
        return new BaseResponse<>(code, data, message, "");
    }

    public static  <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

}