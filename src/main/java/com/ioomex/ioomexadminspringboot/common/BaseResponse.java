package com.ioomex.ioomexadminspringboot.common;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class BaseResponse<T> implements Serializable {


    private static final long serialVersionUID = 1L;


    private Integer code;


    private T data;

    private String message;


    private String formattedTimestamp;

    private Long timestamp;

    private String description;


    public BaseResponse(Integer code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        Instant now = Instant.now();
        this.timestamp = now.getEpochSecond();
        this.formattedTimestamp = formatTimestamp(Instant.now());
    }


    public BaseResponse(Integer code, T data,String message){
        this.code = code;
        this.data = data;
        this.message = message;
        Instant now = Instant.now();
        this.timestamp = now.getEpochSecond();
        this.formattedTimestamp = formatTimestamp(Instant.now());
    }



    public BaseResponse(Integer code, T data){
        this.code = code;
        this.data = data;
        this.message = message;
        Instant now = Instant.now();
        this.timestamp = now.getEpochSecond();
        this.formattedTimestamp = formatTimestamp(Instant.now());
    }



    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }


    private String formatTimestamp(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }
}