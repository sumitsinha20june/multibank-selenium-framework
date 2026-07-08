package com.multibank.framework.pages.interfaces;

import java.util.List;

public interface IHeaderComponent {
    boolean isNavigationItemVisible(String item);
    boolean areNavigationItemsVisible(String items);
    List<String> missingNavigationItems(String items);
    String hrefFor(String item);
    String targetFor(String item);
    boolean isMenuAvailable();
    void openMenuIfPresent();
}
