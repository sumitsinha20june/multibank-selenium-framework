package com.multibank.framework.config;

import com.multibank.framework.constants.FrameworkConstants;
import com.multibank.framework.exceptions.ConfigException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigReader {
    private static final ConfigReader INSTANCE = new ConfigReader();
    private final Path projectRoot;
    private final Properties properties = new Properties();

    private ConfigReader() {
        projectRoot = detectProjectRoot();
        String env = System.getProperty("config.env", FrameworkConstants.DEFAULT_ENV);
        if (env == null || env.isBlank()) {
            env = FrameworkConstants.DEFAULT_ENV;
        }
        Path configPath = resolveProjectPath(String.format(FrameworkConstants.CONFIG_FILE_PATTERN, env));
        if (!Files.exists(configPath)) {
            throw new ConfigException("Config file not found: " + configPath.toAbsolutePath());
        }
        try (FileInputStream inputStream = new FileInputStream(configPath.toFile())) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new ConfigException("Unable to load config file: " + configPath, e);
        }
    }

    public static ConfigReader getInstance() {
        return INSTANCE;
    }

    public String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new ConfigException("Missing config key: " + key);
        }
        return value.trim();
    }

    public String getOptional(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        return properties.getProperty(key, defaultValue).trim();
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public Path getPath(String key) {
        return resolveProjectPath(get(key));
    }

    public Path resolveProjectPath(String path) {
        Path candidate = Path.of(path);
        if (candidate.isAbsolute()) {
            return candidate.normalize();
        }
        return projectRoot.resolve(candidate).normalize();
    }

    public Path projectRoot() {
        return projectRoot;
    }

    // --- Auto-healing locators ---
    // These are disabled by default; enabled via config or system property.

    public boolean isAutoHealingEnabled() {
        return Boolean.parseBoolean(getOptional("auto.healing.enabled", "false"));
    }

    public boolean isAutoHealingAIEnabled() {
        return Boolean.parseBoolean(getOptional("auto.healing.ai.enabled", "false"));
    }

    public String getAutoHealingModel() {
        return getOptional("auto.healing.model", "gpt-4.1");
    }

    public int getAutoHealingRequestTimeout() {
        return Integer.parseInt(getOptional("auto.healing.request.timeout", "60"));
    }

    // Secrets: env var first, then config file, then null.
    public String getOpenAIApiKey() {
        return getSensitiveProperty("openai.api.key", "OPENAI_API_KEY");
    }

    public String getMcpServerUrl() {
        return getOptional("auto.healing.mcp.url", null);
    }

    public String getMcpAuthToken() {
        return getSensitiveProperty("auto.healing.mcp.auth.token", "MCP_AUTH_TOKEN");
    }

    private String getSensitiveProperty(String key, String envVar) {
        String envValue = System.getenv(envVar);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }
        return getOptional(key, null);
    }

    public String resolveUrl(String pathKey) {
        String baseUrl = get("base.url");
        String path = get(pathKey);
        if ("/".equals(path)) {
            return baseUrl;
        }
        return baseUrl + path;
    }

    private Path detectProjectRoot() {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path cursor = current;
        while (cursor != null) {
            if (Files.exists(cursor.resolve("pom.xml")) && Files.exists(cursor.resolve("src/test/resources"))) {
                return cursor;
            }
            cursor = cursor.getParent();
        }
        return current;
    }
}
