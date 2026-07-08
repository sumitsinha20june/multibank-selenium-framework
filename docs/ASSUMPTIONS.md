# Assumptions

During the development of this framework, several engineering assumptions were made to fit the scope of a two-week assessment:

1. **Placeholder Implementations:** 
   *   The `healing` package is purely a proof-of-concept placeholder. It assumes that in a production environment, we would wire this to a self-healing database or service. 
   *   Certain classes in the `actions` package (like `AlertActions` and `FrameActions`) are currently unused by the written test cases. They are included to demonstrate the architectural standard for wrapping Selenium operations.
2. **Action Classes Implementation:** While the architecture dictates that all Selenium actions go through action classes, for the sake of time in this assessment, some complex composite actions might still reside closer to the Page Object layer. The assumption is that in a real sprint, all raw driver calls would be strictly refactored into the `actions` package.
3. **MWeb as a Proxy for Mobile:** I assumed that validating responsive design via browser resizing (MWeb) was sufficient to demonstrate viewport regression handling, rather than implementing a full Appium Android/iOS driver stack which requires local hardware setup from the reviewer.
4. **Environment Target:** Tests are executed against the live production site (`https://mb.io/en-AE`) via `prod.properties`. It is assumed that tests designed to fail (like timeout manipulation) will not trigger DDoS protections on the live server.
