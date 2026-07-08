package com.multibank.framework.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.multibank.framework.models.TestCaseData;
import com.multibank.framework.utilities.LoggerUtil;
import com.multibank.framework.utilities.ScreenshotUtil;
import com.multibank.framework.utilities.ScreenshotUtil.ScreenshotArtifact;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TestListener implements ITestListener {
    private static final Logger log = LoggerUtil.getLogger(TestListener.class);

    private static final AtomicInteger totalExecuted = new AtomicInteger();
    private static final AtomicInteger totalRetries = new AtomicInteger();
    private static final AtomicInteger totalPassed = new AtomicInteger();
    private static final AtomicInteger totalFailed = new AtomicInteger();
    private static final AtomicInteger totalSkipped = new AtomicInteger();
    private static final AtomicInteger totalFlaky = new AtomicInteger();

    private static final Set<String> startedMethods = ConcurrentHashMap.newKeySet();
    private static final Set<String> failedMethods = ConcurrentHashMap.newKeySet();
    private static final Map<String, Integer> retryAttempts = new ConcurrentHashMap<>();
    private static final AtomicInteger finishedContexts = new AtomicInteger();
    private static final Object REPORT_LOCK = new Object();

    // Derives browser from context name rather than System property,
    // so parallel threads don't overwrite each other's browser value.
    private static String browserName(ITestResult result) {
        try {
            String contextName = result.getTestContext().getName();
            if (contextName == null) return "chrome";
            String lowered = contextName.toLowerCase();
            if (lowered.contains("firefox")) return "firefox";
            if (lowered.contains("edge")) return "edge";
        } catch (Exception e) {
            log.warn("Could not determine browser from test context, defaulting to chrome", e);
        }
        return "chrome";
    }

    // Called when a test method starts.  Creates an ExtentReports test entry,
    // attaches TestCaseData metadata and browser info, and binds it to the
    // current thread so onSuccess/onFailure can log against it.
    @Override
    public void onTestStart(ITestResult result) {
        String browser = browserName(result);
        String contextName = result.getTestContext().getName();
        String key = methodKey(result, browser);
        int attempt = retryAttempts.merge(key, 1, Integer::sum);
        if (startedMethods.add(key)) {
            totalExecuted.incrementAndGet();
        } else {
            totalRetries.incrementAndGet();
            totalExecuted.incrementAndGet();
        }

        TestCaseData data = extractData(result);
        String baseName = data == null
                ? result.getMethod().getMethodName()
                : data.getTestCaseId() + " - " + data.getScenario();
        String name = attempt > 1 ? baseName + " (Attempt " + attempt + ")" : baseName;
        log.info("Test started: {} [{}]", name, browser);
        if (attempt > 1) {
            log.info("(retry of previously failed test)");
        }
        String testName = (contextName != null ? contextName + " - " : "") + name;
        ExtentTest test;
        synchronized (REPORT_LOCK) {
            test = ExtentReportManager.getReport().createTest(testName);
        }
        // Log browser per-test so multi-browser runs show each test's browser.
        test.info("Browser: " + browser);
        if (data != null) {
            test.assignCategory(data.getRequirement(), data.getPage(), data.getPriority(), browser);
            test.info("JiraId: " + data.getJiraId());
            test.info("TestClass: " + data.getTestClass());
            test.info("TestMethod: " + data.getTestMethod());
            test.info("TestData: " + data.getTestData());
            test.info("Notes: " + data.getNotes());
        }
        ReportLogger.setTest(test);
    }

    // Logs pass: increments passed count, checks for flaky (previously failed
    // then retried successfully), captures a success screenshot.
    @Override
    public void onTestSuccess(ITestResult result) {
        totalPassed.incrementAndGet();
        String key = methodKey(result, browserName(result));
        if (failedMethods.remove(key)) {
            totalFlaky.incrementAndGet();
            log.warn("Flaky test detected (failed then passed): {}", key);
            if (ReportLogger.getTest() != null) {
                int attempt = retryAttempts.getOrDefault(key, 1);
                ReportLogger.getTest().warning("Flaky — failed on earlier attempt(s), passed on attempt " + attempt);
            }
        }

        log.info("Test passed: {}", result.getMethod().getMethodName());
        if (ReportLogger.getTest() != null) {
            ReportLogger.getTest().pass("Test passed");
            ScreenshotArtifact screenshot = ScreenshotUtil.captureArtifact(
                    result.getMethod().getMethodName() + "_success");
            if (screenshot != null) {
                ReportLogger.getTest().info("Screenshot file: " + screenshot.path());
                ReportLogger.getTest().info("Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot.base64()).build());
            }
        }
        ReportLogger.remove();
    }

    // Logs failure: records the throwable, captures a screenshot, marks the
    // test as failed-for-flaky-detection so a later retry-pass can flag flaky.
    @Override
    public void onTestFailure(ITestResult result) {
        totalFailed.incrementAndGet();
        String key = methodKey(result, browserName(result));
        failedMethods.add(key);

        log.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        if (ReportLogger.getTest() != null) {
            ReportLogger.getTest().fail(result.getThrowable());
            ScreenshotArtifact screenshot = ScreenshotUtil.captureArtifact(
                    result.getMethod().getMethodName());
            if (screenshot != null) {
                ReportLogger.getTest().info("Screenshot file: " + screenshot.path());
                ReportLogger.getTest().info("Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot.base64()).build());
            }
        }
        ReportLogger.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        totalSkipped.incrementAndGet();
        log.warn("Test skipped: {} - {}",
                result.getMethod().getMethodName(),
                result.getThrowable() != null ? result.getThrowable().getMessage() : "no reason");
        if (ReportLogger.getTest() != null) {
            ReportLogger.getTest().skip(result.getThrowable());
        }
        ReportLogger.remove();
    }

    // Called once per test context.  Counts finished contexts against the total
    // from the suite XML.  Only when ALL contexts are done does it create the
    // Suite Summary node and perform the final flush.  Partial flushes happen
    // in between so in-flight tests from the slower context are still persisted.
    @Override
    public void onFinish(ITestContext context) {
        log.info("Test context finished: {} ({} passed, {} failed, {} skipped)",
                context.getName(), context.getPassedTests().size(),
                context.getFailedTests().size(), context.getSkippedTests().size());

        int finished = finishedContexts.incrementAndGet();
        int total = context.getSuite().getXmlSuite().getTests().size();

        if (finished == total) {
            log.info("=== Suite Summary ===");
            log.info("Total executed: {}, Passed: {}, Failed: {}, Skipped: {}",
                    totalExecuted.get(), totalPassed.get(), totalFailed.get(), totalSkipped.get());
            log.info("Retries: {}, Flaky tests: {}", totalRetries.get(), totalFlaky.get());

            synchronized (REPORT_LOCK) {
                ExtentTest summary = ExtentReportManager.getReport().createTest("Suite Summary");
                int executed = totalExecuted.get();
                int passed = totalPassed.get();
                int failed = totalFailed.get();
                int skipped = totalSkipped.get();
                double passRate = executed > 0 ? (passed * 100.0 / executed) : 0;
                double failRate = executed > 0 ? (failed * 100.0 / executed) : 0;
                summary.info("Total Executed: " + executed);
                summary.info("Passed: " + passed);
                summary.info("Failed: " + failed);
                summary.info("Skipped: " + skipped);
                summary.info("Total Retries: " + totalRetries.get());
                summary.info("Flaky Tests: " + totalFlaky.get());
                summary.info("Pass Rate: " + String.format("%.1f%%", passRate));
                summary.info("Failure Rate: " + String.format("%.1f%%", failRate));
            }

            synchronized (REPORT_LOCK) {
                ExtentReportManager.flush();
            }
        } else {
            synchronized (REPORT_LOCK) {
                ExtentReportManager.flush();
            }
        }
    }

    private String methodKey(ITestResult result, String browser) {
        String base = result.getMethod().getQualifiedName();
        Object[] params = result.getParameters();
        if (params.length > 0 && params[0] instanceof TestCaseData data) {
            base += "#" + data.getTestCaseId();
        } else if (params.length > 0) {
            base += "#" + Arrays.hashCode(params);
        }
        return base + "@" + browser;
    }

    // Extracts the first TestCaseData parameter if present (data-driven tests).
    private TestCaseData extractData(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length > 0 && parameters[0] instanceof TestCaseData data) {
            return data;
        }
        return null;
    }
}
