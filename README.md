# MultiBank QA Automation Framework

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Selenium](https://img.shields.io/badge/Selenium-4.x-green.svg)
![TestNG](https://img.shields.io/badge/TestNG-7.x-orange.svg)
![Maven](https://img.shields.io/badge/Maven-Build-yellow.svg)

An enterprise-grade, data-driven automation framework designed to validate the core functionality, UI layout, and critical edge cases of the MultiBank public-facing platform across multiple viewports. 

This repository serves as a showcase for a scalable automation architecture, moving beyond basic linear scripting to implement a highly robust Page Object Model (POM) with centralized configurations, custom wait strategies, Docker grid readiness, and rich HTML reporting.

---

## 📑 Table of Contents
1. [Technology Stack](#-technology-stack)
2. [Project Architecture](#-project-architecture)
3. [Key Features](#-key-features)
4. [Prerequisites](#-prerequisites)
5. [Execution Guide (Local)](#-execution-guide)
6. [Docker & CI/CD Scalability](#-docker--cicd-scalability)
7. [Reporting & Evidence](#-reporting--evidence)
8. [Developer Guide](#-developer-guide)

---

## 🛠️ Technology Stack
*   **Language:** Java 17
*   **Core Engine:** Selenium WebDriver 4
*   **Test Runner:** TestNG
*   **Build Management:** Apache Maven
*   **Data Injection:** Apache POI (Excel)
*   **Reporting:** ExtentReports 5 & Allure
*   **Logging:** SLF4J / Log4j2

---

## 📂 Project Architecture

The repository is structured to strictly separate test logic, configuration, and execution environments.

```text
├── docker/                 # Placeholder: Selenium Grid Docker Compose configs
├── docs/                   # QA Strategy, Test Plans, Risk Matrix
├── src/main/java           # Core Framework (Page Objects, Driver Factory, Actions)
├── src/test/java           # Execution Engine (TestNG Tests, Base Test)
├── src/test/resources      # Test Data (Excel), Configs, and TestNG XML Suites
├── Jenkinsfile             # Placeholder: CI/CD Pipeline definition for Jenkins
└── pom.xml                 # Maven dependency management
```

---

## ✨ Key Features
*   **Data-Driven Execution:** All test data (URLs, validation text, dynamic locators) is decoupled from the code and maintained in `src/test/resources/testdata/MultiBankTestData.xlsx`.
*   **Thread-Safe Parallelism:** Built on `ThreadLocal<WebDriver>`, allowing completely isolated, concurrent test execution without session crossover.
*   **Multi-Viewport Support:** Natively supports both **Desktop Web (DWeb)** and simulated **Mobile Web (MWeb)** execution. *Note: MWeb execution simulates a mobile device strictly by dynamically resizing the Chrome browser, rather than using a full Appium stack.*
*   **Action Class Abstraction:** Raw Selenium calls (`driver.findElement`) are forbidden in Page Objects. All interactions pass through safe wrapper classes (`WebActions`, `WaitActions`).
*   **Fail-Fast & Auto-Healing Design:** Includes placeholder nodes for future AI-driven auto-healing locators, and uses Global Retry Analyzers for known flaky tests.

---

## ⚙️ Prerequisites
*   [Java Development Kit (JDK) 17+](https://adoptium.net/)
*   [Apache Maven 3.8+](https://maven.apache.org/download.cgi)

---

## 🚀 Execution Guide

### 1. Local Execution
Executes the baseline Desktop Web suite using your local Chrome browser.
```bash
mvn clean test
```

**Run Explicit Suites:**
```bash
# Run Desktop Web Suite
mvn clean test -DsuiteXmlFile=src/test/resources/suites/dweb.xml -Dplatform=dweb

# Run Mobile Web Suite (Dynamic resizing in Chrome)
mvn clean test -DsuiteXmlFile=src/test/resources/suites/mweb.xml -Dplatform=mweb
```

---

## 🐳 Docker & CI/CD Scalability
*Note: The `docker/` folder and `Jenkinsfile` in this repository are structural placeholders. They are included to demonstrate architectural intent and show how easily this framework scales to CI/CD pipelines and remote Selenium Grids, though they are not fully wired for active execution in this assessment branch.*

### Docker & Selenium Grid (Placeholder Concept)
In a scaled environment, you can spin up a local Selenium Grid to execute tests inside isolated containers.
```bash
# Spin up the Selenium Grid
docker-compose -f docker/docker-compose.yml up -d

# Run the tests pointing to the Grid
mvn clean test -Dremote=true -DhubUrl=http://localhost:4444/wd/hub
```

### CI/CD Integration (Placeholder Concept)
The included `Jenkinsfile` provides a template for standard CI/CD pipelines to:
1. Pull the latest code on a nightly schedule.
2. Spin up the test environment.
3. Execute the Maven goals in parallel.
4. Archive the Extent and Allure reports as build artifacts.

---

## 📊 Reporting & Evidence

*   **Extent Reports (Primary Dashboard):** 
    Located at `test-output/reports/index.html`. Open this file in any browser for a beautiful, dashboard-style view of the execution. **If a test fails, a Base64-encoded screenshot is automatically captured, highlighted with the failing element, and embedded directly into this report.**
*   **Allure Reports (Secondary):** 
    Raw results are generated in `target/allure-results/`.
*   **Raw Screenshots:** 
    Saved directly to the file system at `test-output/screenshots/`.

---

## 👩‍💻 Developer Guide: Adding a New Test

Scaling the framework is designed to be simple and require minimal Java.

1. **Add Data:** Open `MultiBankTestData.xlsx` and add a new row with your test inputs (e.g., a new cryptocurrency pair to validate).
2. **Update Page Object:** If testing a new element, add the `By` locator to the relevant class in `src/main/java/.../pages/`.
3. **Write Test Method:** Add a new `@Test` method in `src/test/java/`, passing `TestCaseData data` as a parameter. Call your Page Object methods using the data from the Excel sheet.

---

## 🏗️ Architecture & Documentation
To deeply understand the engineering, assumptions, and business strategy behind this project, please refer to the detailed markdown files in the `docs/` directory:

1.  **[Task 2 Responses (QA Strategy)](docs/TASK_2.md):** My comprehensive approach to managing QA, risk, and deployment pipelines for this application.
2.  **[Framework & Design Decisions](docs/FRAMEWORK_DESIGN.md):** An explanation of the POM, ThreadLocal architecture, Action wrappers, and the Auto-Healing scalability proxies.
3.  **[Assumptions & Placeholders](docs/ASSUMPTIONS.md):** The engineering assumptions made given the time constraints.
4.  **[Master Test Plan](docs/TEST_PLAN.md):** The scope, in/out parameters, and environmental execution strategy.
5.  **[Release Readiness Checklist](docs/RELEASE_READINESS_CHECKLIST.md):** The final UAT and automated quality gates required before pushing to production.
6.  **[Risk Matrix](docs/RISK_MATRIX.md):** An analysis of potential UI, data, and network risks alongside their programmed mitigation strategies.
