package com.multibank.framework.api;

import com.multibank.framework.config.ConfigReader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static final ConfigReader CONFIG = ConfigReader.getInstance();
    private static final int CONNECT_TIMEOUT = CONFIG.getInt("api.connect.timeout");
    private static final int REQUEST_TIMEOUT = CONFIG.getInt("api.request.timeout");

    private final HttpClient client;

    public ApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
                .build();
    }

    public ApiResponse get(String url, Map<String, String> headers) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                .GET();
        headers.forEach(request::header);
        return send(request.build());
    }

    public ApiResponse post(String url, String body, Map<String, String> headers) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                .POST(HttpRequest.BodyPublishers.ofString(body));
        headers.forEach(request::header);
        return send(request.build());
    }

    private ApiResponse send(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, String> headers = new HashMap<>();
            response.headers().map().forEach((key, value) -> headers.put(key, String.join(",", value)));
            return new ApiResponse(response.statusCode(), response.body(), headers);
        } catch (IOException e) {
            throw new IllegalStateException("API request failed: " + request.uri(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("API request interrupted: " + request.uri(), e);
        }
    }
}
