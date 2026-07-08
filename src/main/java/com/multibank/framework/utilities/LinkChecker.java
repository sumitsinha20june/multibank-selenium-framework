package com.multibank.framework.utilities;

import java.net.HttpURLConnection;
import java.net.URI;

// Checks HTTP link health via HEAD (fallback to GET for 405/403).
// Returns 599 on network error so callers can distinguish "unreachable" from a real status.
public final class LinkChecker {
    private LinkChecker() {
    }

    public static int statusCode(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("HEAD");
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int status = connection.getResponseCode();
            if (status == 405 || status == 403) {
                connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                status = connection.getResponseCode();
            }
            return status;
        } catch (Exception e) {
            return 599;
        }
    }
}
