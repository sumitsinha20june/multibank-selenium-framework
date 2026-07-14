package com.multibank.tests.common;

import com.multibank.framework.flows.FooterFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.framework.reporting.ReportLogger;
import com.multibank.framework.utilities.SoftAssertManager;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class FooterLinkTests extends BaseTest {
    private final FooterFlow footerFlow = new FooterFlow();

    @Test(dataProvider = "excelData")
    public void verifyFooterPolicyLinkDestination(TestCaseData data) {
        Assert.assertTrue(footerFlow.linkDestinationMatches(data.data("linkText"), data.data("expectedPath")));
        Assert.assertEquals(footerFlow.targetFor(data.data("linkText")), data.data("target"));
    }

    @Test
    public void verifyAllFooterLinksResolve() {
        Map<String, Integer> results = footerFlow.checkAllFooterLinkStatuses();
        SoftAssert softAssert = SoftAssertManager.create();
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            String label = entry.getKey();
            int status = entry.getValue();
            if (status == -1) {
                if (ReportLogger.getTest() != null) {
                    ReportLogger.getTest().info("Skipped external link: \"" + label + "\"");
                }
                continue;
            }
            if (status >= 400 && ReportLogger.getTest() != null) {
                ReportLogger.getTest().warning("Footer link \"" + label + "\" returned status " + status);
            }
            softAssert.assertTrue(status < 400,
                    "Footer link \"" + label + "\" returned status " + status);
        }
        SoftAssertManager.assertAll();
    }
}
