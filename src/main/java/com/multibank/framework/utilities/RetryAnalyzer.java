package com.multibank.framework.utilities;

import com.multibank.framework.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

// Retries failing tests up to retry.count times (configured in prod.properties).
// Used together with RetryAnnotationTransformer for data-driven tests.
public class RetryAnalyzer implements IRetryAnalyzer {
    private int count;

    @Override
    public boolean retry(ITestResult result) {
        int max = ConfigReader.getInstance().getInt("retry.count");
        return count++ < max;
    }
}
