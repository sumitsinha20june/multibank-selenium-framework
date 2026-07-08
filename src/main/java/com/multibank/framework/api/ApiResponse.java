package com.multibank.framework.api;

import java.util.Map;

public record ApiResponse(int statusCode, String body, Map<String, String> headers) {
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}
