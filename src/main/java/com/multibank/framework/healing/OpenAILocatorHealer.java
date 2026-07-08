package com.multibank.framework.healing;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.reporting.ReportLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Tries MCP then OpenAI to get a working locator when the original fails.
// Configured via auto.healing.* properties — silently returns empty if none.
public class OpenAILocatorHealer {

    private static final Logger logger = LogManager.getLogger(OpenAILocatorHealer.class);

    private static final String OPENAI_RESPONSES_URL = "https://api.openai.com/v1/responses";
    private static final int MAX_PAGE_SOURCE_CHARS = 12000;

    private static final Pattern OUTPUT_TEXT_PATTERN = Pattern.compile(
            "\"output_text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final Pattern TEXT_PATTERN = Pattern.compile(
            "\"text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");

    private final ConfigReader config = ConfigReader.getInstance();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    // Entry point: tries MCP server first, falls back to OpenAI if no MCP URL.
    public Optional<By> heal(By brokenLocator, String elementName, String pageSource) {
        // Priority 1: MCP server
        String mcpUrl = config.getMcpServerUrl();
        if (mcpUrl != null && !mcpUrl.isBlank()) {
            return healViaMcp(mcpUrl, brokenLocator, elementName, pageSource);
        }

        // Priority 2: Direct OpenAI API
        String apiKey = config.getOpenAIApiKey();
        if (apiKey != null && !apiKey.isBlank()) {
            return healViaOpenAI(apiKey, brokenLocator, elementName, pageSource);
        }

        logger.warn("AI locator healing requested, but neither MCP server URL nor OpenAI API key is configured");
        return Optional.empty();
    }

    // Posts locator + page source to the MCP server, expects an "xpath" field back.
    private Optional<By> healViaMcp(String mcpUrl, By brokenLocator, String elementName, String pageSource) {
        String clippedSource = clipPageSource(pageSource);
        String authToken = config.getMcpAuthToken();

        String body = """
                {
                  "locator": "%s",
                  "elementName": "%s",
                  "pageSource": "%s"
                }
                """.formatted(escapeJson(brokenLocator.toString()),
                escapeJson(elementName),
                escapeJson(clippedSource));

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(mcpUrl))
                .timeout(Duration.ofSeconds(config.getAutoHealingRequestTimeout()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (authToken != null && !authToken.isBlank()) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        try {
            HttpResponse<String> response = httpClient.send(builder.build(),
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                logger.warn("MCP healing failed with HTTP {}: {}",
                        response.statusCode(), abbreviate(response.body(), 500));
                return Optional.empty();
            }

            // Expect JSON response with an "xpath" field
            String xpath = extractJsonField(response.body(), "xpath");
            if (xpath == null || xpath.isBlank()) {
                logger.warn("MCP response did not contain an 'xpath' field");
                if (ReportLogger.getTest() != null) {
                    ReportLogger.getTest().warning("MCP healing: response missing 'xpath' field for \"" + elementName + "\"");
                }
                return Optional.empty();
            }

            By healedLocator = LocatorUtils.toByXPath(xpath);
            logger.info("MCP healed locator '{}' as '{}'", brokenLocator, healedLocator);
            if (ReportLogger.getTest() != null) {
                ReportLogger.getTest().info("MCP healed \"" + elementName + "\": " + brokenLocator + " → " + healedLocator);
            }
            return Optional.of(healedLocator);
        } catch (IOException e) {
            logger.warn("MCP healing failed due to I/O error: {}", e.getMessage());
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("MCP healing was interrupted");
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("MCP healing returned unusable output: {}", e.getMessage());
            return Optional.empty();
        }
    }

    // Posts the healing prompt to the OpenAI Responses API.
    private Optional<By> healViaOpenAI(String apiKey, By brokenLocator, String elementName, String pageSource) {
        String clippedSource = clipPageSource(pageSource);
        String prompt = buildPrompt(brokenLocator, elementName, clippedSource);

        String body = """
                {
                  "model": "%s",
                  "instructions": "You are a senior Selenium and Appium automation engineer. Return ONLY one valid XPath locator. No markdown. No explanation.",
                  "input": "%s",
                  "temperature": 0,
                  "max_output_tokens": 120
                }
                """.formatted(escapeJson(config.getAutoHealingModel()),
                escapeJson(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_RESPONSES_URL))
                .timeout(Duration.ofSeconds(config.getAutoHealingRequestTimeout()))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                logger.warn("OpenAI healing failed with HTTP {}: {}",
                        response.statusCode(), abbreviate(response.body(), 500));
                return Optional.empty();
            }

            String healedXPath = extractText(response.body());
            By healedLocator = LocatorUtils.toByXPath(healedXPath);
            logger.info("OpenAI healed locator '{}' as '{}'", brokenLocator, healedLocator);
            if (ReportLogger.getTest() != null) {
                ReportLogger.getTest().info("AI healed \"" + elementName + "\": " + brokenLocator + " → " + healedLocator);
            }
            return Optional.of(healedLocator);
        } catch (IOException e) {
            logger.warn("OpenAI healing failed due to I/O error: {}", e.getMessage());
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("OpenAI healing was interrupted");
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("OpenAI healing returned unusable output: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private String buildPrompt(By brokenLocator, String elementName, String pageSource) {
        String clippedSource = pageSource == null ? "" : pageSource;

        return """
                Human element name:
                %s

                Broken Selenium/Appium locator:
                %s

                Current page source:
                %s

                Task:
                Identify the intended element and return the most stable XPath for the current page source.
                Prefer stable attributes such as id, name, data-testid, accessibility id, aria-label, text, content-desc, label, or value.
                Return ONLY the XPath string.
                """.formatted(elementName, brokenLocator, clippedSource);
    }

    // Caps page source at MAX_PAGE_SOURCE_CHARS to avoid overly large API payloads.
    private String clipPageSource(String pageSource) {
        if (pageSource == null || pageSource.length() <= MAX_PAGE_SOURCE_CHARS) {
            return pageSource;
        }
        return pageSource.substring(0, MAX_PAGE_SOURCE_CHARS);
    }

    // Pulls output_text or text from the OpenAI Responses JSON body.
    private String extractText(String responseBody) {
        Matcher outputTextMatcher = OUTPUT_TEXT_PATTERN.matcher(responseBody);
        if (outputTextMatcher.find()) {
            return unescapeJson(outputTextMatcher.group(1));
        }

        Matcher textMatcher = TEXT_PATTERN.matcher(responseBody);
        if (textMatcher.find()) {
            return unescapeJson(textMatcher.group(1));
        }

        throw new IllegalArgumentException("Could not find text output in OpenAI response");
    }

    // Extracts a string field from JSON via regex (no JSON parser dependency).
    private String extractJsonField(String responseBody, String fieldName) {
        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }
        return null;
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String value) {
        StringBuilder result = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (!escaping) {
                if (current == '\\') {
                    escaping = true;
                } else {
                    result.append(current);
                }
                continue;
            }

            switch (current) {
                case '"' -> result.append('"');
                case '\\' -> result.append('\\');
                case '/' -> result.append('/');
                case 'b' -> result.append('\b');
                case 'f' -> result.append('\f');
                case 'n' -> result.append('\n');
                case 'r' -> result.append('\r');
                case 't' -> result.append('\t');
                default -> result.append(current);
            }
            escaping = false;
        }
        return result.toString();
    }

    private String abbreviate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }
}
