package com.multibank.framework.flows;

import com.multibank.framework.pages.factory.PageObjectFactory;
import com.multibank.framework.pages.interfaces.ICompanyPage;

public class CompanyFlow {
    private final ICompanyPage companyPage;

    public CompanyFlow() {
        this.companyPage = PageObjectFactory.getCompanyPage();
    }

    public void open() {
        companyPage.open();
    }

    public boolean hasContent(String heading, String text) {
        return companyPage.hasContent(heading, text);
    }
}
