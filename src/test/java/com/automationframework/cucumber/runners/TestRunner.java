package com.automationframework.cucumber.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Entry point TestNG uses (via src/test/resources/testng.xml) to run every
 * Gherkin feature under src/test/resources/features against the step
 * definitions in the stepdefinitions/hooks packages.
 *
 * Each Cucumber scenario is surfaced to TestNG as its own @Test via
 * AbstractTestNGCucumberTests' data provider, so a single failing scenario
 * doesn't hide the pass/fail result of the others.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automationframework.cucumber.stepdefinitions",
                "com.automationframework.cucumber.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
