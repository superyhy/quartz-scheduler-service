package com.keeson.quartzschedulerservice.controller.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 通用 API 响应封装
 * @author dny
 */
@Data
public class ApiResponse implements Serializable {

    /**
     * 状态码（如 200, 400, 500）
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public ApiResponse() {
    }

    private ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 通用构造方法
     */
    public static ApiResponse of(int code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    /**
     * 成功响应（带数据）
     */
    public static ApiResponse ok(Object data) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 成功响应（仅消息）
     */
    public static ApiResponse ok(String message) {
        return new ApiResponse(HttpStatus.OK.value(), message, null);
    }

    /**
     * 错误响应（带自定义状态码）
     */
    public static ApiResponse error(int code, String message) {
        return new ApiResponse(code, message, null);
    }

    /**
     * 错误响应（默认 500）
     */
    public static ApiResponse error(String message) {
        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    /**
     * 仅消息（兼容旧逻辑）
     */
    public static ApiResponse msg(String message) {
        return new ApiResponse(HttpStatus.OK.value(), message, null);
    }
}
