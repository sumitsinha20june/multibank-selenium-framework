package com.multibank.tests.common;

import com.multibank.framework.models.TestCaseData;
import com.multibank.framework.reporting.ReportLogger;
import com.multibank.framework.utilities.LinkChecker;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BrokenLinkTests extends BaseTest {
    @Test(dataProvider = "excelData")
    public void verifyCriticalLinksAreNotBroken(TestCaseData data) {
        int max = Integer.parseInt(data.data("expectedStatusMax"));
        for (String link : data.data("links").split("\\|")) {
            String[] pair = link.split(":", 2);
            String target = resolveTarget(pair[1]);
            int status = LinkChecker.statusCode(target);
            if (ReportLogger.getTest() != null && status > max) {
                ReportLogger.getTest().warning("Broken link: " + pair[0] + " -> " + target + " returned status " + status);
            }
            Assert.assertTrue(status <= max, pair[0] + " returned status " + status + " for " + target);
        }
    }

    private String resolveTarget(String target) {
        if (target.startsWith("http")) {
            return target;
        }
        if (!target.startsWith("/") && target.contains(".")) {
            return "https://" + target;
        }
        return config.get("base.url") + target;
    }
}
