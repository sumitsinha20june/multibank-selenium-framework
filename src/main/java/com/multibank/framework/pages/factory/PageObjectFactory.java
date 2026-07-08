package com.multibank.framework.pages.factory;

import com.multibank.framework.config.FrameworkConfig;
import com.multibank.framework.enums.PlatformType;
import com.multibank.framework.pages.dweb.*;
import com.multibank.framework.pages.interfaces.*;
import com.multibank.framework.pages.mweb.*;

public final class PageObjectFactory {

    private PageObjectFactory() {
    }

    public static IHomePage getHomePage() {
        return switch (FrameworkConfig.platform()) {
            case DWEB -> new DWebHomePage();
            case MWEB -> new MWebHomePage();
        };
    }

    public static IExplorePage getExplorePage() {
        return switch (FrameworkConfig.platform()) {
            case DWEB -> new DWebExplorePage();
            case MWEB -> new MWebExplorePage();
        };
    }

    public static IErrorPage getErrorPage() {
        return switch (FrameworkConfig.platform()) {
            case DWEB -> new DWebErrorPage();
            case MWEB -> new MWebErrorPage();
        };
    }

    public static ICompanyPage getCompanyPage() {
        return switch (FrameworkConfig.platform()) {
            case DWEB -> new DWebCompanyPage();
            case MWEB -> new MWebCompanyPage();
        };
    }

    public static IHeaderComponent getHeaderComponent() {
        return switch (FrameworkConfig.platform()) {
            case DWEB -> new DWebHeaderComponent();
            case MWEB -> new MWebHeaderComponent();
        };
    }

    public static IFooterComponent getFooterComponent() {
        return switch (FrameworkConfig.platform()) {
            case DWEB -> new DWebFooterComponent();
            case MWEB -> new MWebFooterComponent();
        };
    }
}
