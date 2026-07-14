package com.multibank.framework.pages.interfaces;

import java.util.Map;

public interface IFooterComponent {
    String hrefFor(String linkText);
    String targetFor(String linkText);
    Map<String, String> getAllFooterLinks();
}
