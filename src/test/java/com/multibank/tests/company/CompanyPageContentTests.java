package com.multibank.tests.company;

import com.multibank.framework.flows.CompanyFlow;
import com.multibank.framework.models.TestCaseData;
import com.multibank.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompanyPageContentTests extends BaseTest {
    private final CompanyFlow companyFlow = new CompanyFlow();

    @Test(dataProvider = "excelData")
    public void verifyWhyMultiBankPageCoreContent(TestCaseData data) {
        companyFlow.open();
        Assert.assertTrue(companyFlow.hasContent(data.data("expectedHeading"), data.data("expectedText")), data.toString());
    }

    @Test(dataProvider = "excelData")
    public void verifyCompanyPageSectionContent(TestCaseData data) {
        companyFlow.open();
        Assert.assertTrue(companyFlow.hasContent(data.data("expectedHeading"), data.data("expectedText")), data.toString());
    }
}
