package com.multibank.framework.utilities;

import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Path;
import java.util.Base64;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Takes full-page screenshots for ExtentReports attachments on test
// success and failure.  Screenshots are saved under the configured path
// and also base64-encoded for inline embedding in the HTML report.
public final class ScreenshotUtil {
    private ScreenshotUtil() {
    }

    public static String capture(String name) {
        ScreenshotArtifact artifact = captureArtifact(name);
        return artifact == null ? null : artifact.path();
    }

    private static boolean screenshotDirCleaned;

    public static ScreenshotArtifact captureArtifact(String name) {
        try {
            Path dir = ConfigReader.getInstance().getPath("screenshot.path");
            if (!screenshotDirCleaned) {
                FileUtil.cleanDirectory(dir);
                screenshotDirCleaned = true;
            }
            FileUtil.createDirectories(dir);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            Path destination = dir.resolve(name + "_" + timestamp + ".png");
            File source = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtil.copyFile(source, destination);
            String base64 = Base64.getEncoder().encodeToString(FileUtil.readAllBytes(destination));
            return new ScreenshotArtifact(destination.toString(), base64);
        } catch (Exception e) {
            return null;
        }
    }

    public record ScreenshotArtifact(String path, String base64) {
    }
}
