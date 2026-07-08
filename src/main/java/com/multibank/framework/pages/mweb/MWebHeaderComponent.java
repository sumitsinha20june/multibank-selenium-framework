package com.multibank.framework.pages.mweb;

import com.multibank.framework.pages.dweb.DWebHeaderComponent;
import com.multibank.framework.pages.interfaces.IHeaderComponent;

public class MWebHeaderComponent extends DWebHeaderComponent implements IHeaderComponent {

    public boolean isNavigationItemVisible(String item) {
        openMenuIfPresent();
        return super.isNavigationItemVisible(item);
    }

    public boolean areNavigationItemsVisible(String items) {
        openMenuIfPresent();
        return super.areNavigationItemsVisible(items);
    }

    public String hrefFor(String item) {
        openMenuIfPresent();
        return super.hrefFor(item);
    }

    public String targetFor(String item) {
        openMenuIfPresent();
        return super.targetFor(item);
    }
}
