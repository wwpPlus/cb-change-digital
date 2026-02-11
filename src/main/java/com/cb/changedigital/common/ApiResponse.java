package com.cb.changedigital.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(true, "ok", data);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<T>(false, message, null);
    }
}
