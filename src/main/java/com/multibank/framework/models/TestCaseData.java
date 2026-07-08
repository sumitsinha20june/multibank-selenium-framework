package com.multibank.framework.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestCaseData {
    private String jiraId;
    private String testCaseId;
    private String requirement;
    private String page;
    private String testClass;
    private String testMethod;
    private String testData;
    private String runMode;
    private String priority;
    private String scenario;
    private String notes;

    public String getJiraId() { return jiraId; }
    public void setJiraId(String jiraId) { this.jiraId = jiraId; }
    public String getTestCaseId() { return testCaseId; }
    public void setTestCaseId(String testCaseId) { this.testCaseId = testCaseId; }
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    public String getPage() { return page; }
    public void setPage(String page) { this.page = page; }
    public String getTestClass() { return testClass; }
    public void setTestClass(String testClass) { this.testClass = testClass; }
    public String getTestMethod() { return testMethod; }
    public void setTestMethod(String testMethod) { this.testMethod = testMethod; }
    public String getTestData() { return testData; }
    public void setTestData(String testData) { this.testData = testData; }
    public String getRunMode() { return runMode; }
    public void setRunMode(String runMode) { this.runMode = runMode; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getScenario() { return scenario; }
    public void setScenario(String scenario) { this.scenario = scenario; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Map<String, String> dataMap() {
        if (testData == null || testData.isBlank()) {
            return Collections.emptyMap();
        }
        Map<String, String> values = new HashMap<>();
        for (String part : testData.split(";")) {
            String[] tokens = part.split("=", 2);
            if (tokens.length == 2) {
                values.put(tokens[0].trim(), tokens[1].trim());
            }
        }
        return values;
    }

    public String data(String key) {
        return dataMap().get(key);
    }

    public String dataOrDefault(String key, String defaultValue) {
        return dataMap().getOrDefault(key, defaultValue);
    }

    @Override
    public String toString() {
        return testCaseId + " - " + scenario;
    }
}
