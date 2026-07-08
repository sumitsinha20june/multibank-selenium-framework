# Framework & Design Decisions

This framework deviates from basic linear scripting to implement a highly scalable, maintainable architecture suitable for an enterprise CI/CD pipeline.

### 1. Future-Proof Scalability & Placeholders
To demonstrate architectural foresight, several packages have been stubbed out or partially implemented:
*   **Auto-Healing Capability (`healing` package):** I have included the structural foundation for an AI-driven auto-healing locator strategy. While currently a disabled placeholder (to keep the assessment execution fast and deterministic), the design shows how a `WaitUtil` could catch a `NoSuchElementException`, query a fallback database (or LLM), and heal the locator on the fly.
*   **Action Class Abstractions (`actions` package):** Rather than letting Page Objects call `driver.findElement()`, all interactions pass through wrapper classes like `WebActions` and `WaitActions`. Some unused action classes (e.g., `DropdownActions`, `AlertActions`) are included to demonstrate how the framework scales to handle complex UI elements across a massive application without duplicating wait-logic.

### 2. Multi-Viewport Execution (DWeb vs. MWeb)
*   The framework natively supports both **Desktop Web (DWeb)** and **Mobile Web (MWeb)** execution. 
*   **Why MWeb?** Implementing MWeb was explicitly done to showcase the framework's scalability. By utilizing `ViewportManager` to dynamically resize the browser window (e.g., to `390x844` for an iPhone 14 Pro), we can reuse the exact same Selenium grid to validate responsive layouts, hamburger menus, and stacking orders without needing a heavy Appium integration for simple web views.

### 3. Visual Evidence & Extent Reports Integration
*   **Automated Screenshots:** The framework utilizes a custom `TestListener` connected to ExtentReports. Upon any test failure, a screenshot is captured, converted to Base64, and embedded directly into the HTML report. 
*   **Highlighting Elements:** The `WebActions` wrapper uses a `JavascriptExecutor` to draw a green/red border around elements right before interacting with them. If a test fails, the embedded screenshot explicitly highlights the exact DOM element the script was attempting to manipulate, cutting debugging time in half.

### 4. Data-Driven Abstraction
*   Test data (URLs, dynamic locators, validation text, and viewport dimensions) is driven entirely through Apache POI reading `MultiBankTestData.xlsx`.
*   This ensures that product managers or manual QAs can add new test scenarios (like a new cryptocurrency pair) by simply adding an Excel row, without writing a single line of Java.
