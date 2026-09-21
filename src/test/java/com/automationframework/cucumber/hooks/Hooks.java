package com.automationframework.cucumber.hooks;

import com.automationframework.cucumber.context.TestContext;
import com.automationframework.cucumber.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Driver lifecycle for every scenario, plus a screenshot on failure.
 *
 * The failure screenshot is both attached to the Cucumber HTML/JSON report
 * (visible right next to the failing step) and written to screenshots/
 * (gitignored, uploaded as a CI artifact) for quick access without opening
 * the report.
 */
public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);
    private static final Path SCREENSHOT_DIR = Paths.get("screenshots");

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        // Touching getDriver() here (rather than waiting for the first step) means the
        // browser is up before any step runs, so a failure in the very first step still
        // has a driver available for the @After screenshot.
        context.getDriver();
        LOG.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            captureFailureScreenshot(scenario);
        }
        DriverFactory.quitDriver();
        LOG.info("Finished scenario: {} ({})", scenario.getName(), scenario.getStatus());
    }

    private void captureFailureScreenshot(Scenario scenario) {
        WebDriver driver = context.getDriver();
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", scenario.getName());

        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
            String fileName = sanitize(scenario.getName()) + "_" + timestamp + ".png";
            Files.write(SCREENSHOT_DIR.resolve(fileName), screenshot);
            LOG.warn("Scenario '{}' failed, screenshot saved to {}", scenario.getName(), fileName);
        } catch (IOException e) {
            LOG.error("Failed to save screenshot for scenario '{}'", scenario.getName(), e);
        }
    }

    private String sanitize(String scenarioName) {
        return scenarioName.replaceAll("[^a-zA-Z0-9-]", "_");
    }
}
