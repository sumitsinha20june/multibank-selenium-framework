package com.multibank.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.multibank.framework.config.ConfigReader;
import com.multibank.framework.utilities.FileUtil;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentReportManager {
    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    // Lazily creates the report singleton. Uses file-path + timestamp from config.
    // Suite-level system info stays global; browser is per-test in TestListener.
    public static synchronized ExtentReports getReport() {
        if (extentReports == null) {
            ConfigReader config = ConfigReader.getInstance();
            Path reportDir = config.getPath("report.path");
            FileUtil.cleanDirectory(reportDir);
            String reportName = config.get("report.name");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path reportPath = reportDir.resolve(timestamp + "_" + reportName);
            FileUtil.createDirectories(reportDir);
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath.toString());
            sparkReporter.config().setDocumentTitle("MultiBank Automation Report");
            sparkReporter.config().setReportName("MultiBank QA Automation");
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Environment", config.get("environment"));
            extentReports.setSystemInfo("Base URL", config.get("base.url"));
            extentReports.setSystemInfo("Platform", config.get("platform"));
            // Browser is NOT set at suite level -- each test logs its own browser
            // via TestListener, so multi-browser runs don't race on a single value.
            extentReports.setSystemInfo("Execution", config.get("execution.mode"));
        }
        return extentReports;
    }

    // Flushes all logged tests to the HTML report.
    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
