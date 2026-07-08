package com.multibank.tests.common;

import com.multibank.framework.flows.FooterFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FooterLinkTests extends BaseTest {
    private final FooterFlow footerFlow = new FooterFlow();

    @Test(dataProvider = "excelData")
    public void verifyFooterPolicyLinkDestination(TestCaseData data) {
        Assert.assertTrue(footerFlow.linkDestinationMatches(data.data("linkText"), data.data("expectedPath")));
        Assert.assertEquals(footerFlow.targetFor(data.data("linkText")), data.data("target"));
    }
}
