package com.automationframework.cucumber.stepdefinitions;

import com.automationframework.cucumber.context.TestContext;
import com.automationframework.cucumber.utils.ConfigReader;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * Assertions shared across features. saucedemo renders every validation error -
 * a failed login, a failed checkout step - through the same
 * {@code h3[data-test="error"]} banner, so a single generic step definition
 * here covers "Then I should see the error message ..." for both login.feature
 * and checkout.feature without duplicating (and risking an ambiguous match on)
 * the same step text in two step definition classes.
 */
public class CommonSteps {

    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");

    private final TestContext context;

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(context.getDriver(), Duration.ofSeconds(ConfigReader.explicitWaitSeconds()));
        String actualMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)).getText();
        Assert.assertEquals(actualMessage, expectedMessage);
    }
}
