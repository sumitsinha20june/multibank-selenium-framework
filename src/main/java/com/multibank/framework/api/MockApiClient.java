package com.multibank.framework.api;

import java.util.HashMap;
import java.util.Map;

public class MockApiClient extends ApiClient {
    private final Map<String, ApiResponse> stubs = new HashMap<>();

    public void stubGet(String url, ApiResponse response) {
        stubs.put("GET " + url, response);
    }

    public void stubPost(String url, ApiResponse response) {
        stubs.put("POST " + url, response);
    }

    @Override
    public ApiResponse get(String url, Map<String, String> headers) {
        return responseFor("GET " + url);
    }

    @Override
    public ApiResponse post(String url, String body, Map<String, String> headers) {
        return responseFor("POST " + url);
    }

    private ApiResponse responseFor(String key) {
        ApiResponse response = stubs.get(key);
        if (response == null) {
            throw new IllegalStateException("No mock response registered for " + key);
        }
        return response;
    }
}
