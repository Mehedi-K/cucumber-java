package com.automationframework.cucumber.listeners;

import io.cucumber.testng.PickleWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed checkout scenario once. Hooks quits the driver after every
 * scenario, so the retry always gets a fresh browser session. Other features
 * are never retried, so genuine regressions there fail immediately.
 */
public class RetryCheckoutScenarios implements IRetryAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(RetryCheckoutScenarios.class);
    private boolean retried;

    @Override
    public boolean retry(ITestResult result) {
        if (retried || !isCheckoutScenario(result)) {
            return false;
        }
        retried = true;
        LOG.warn("Retrying checkout scenario once in a fresh browser after: {}",
                result.getThrowable() == null ? "unknown failure" : result.getThrowable().getMessage());
        return true;
    }

    private static boolean isCheckoutScenario(ITestResult result) {
        Object[] params = result.getParameters();
        return params.length > 0 && params[0] instanceof PickleWrapper pickle
                && pickle.getPickle().getUri().toString().endsWith("checkout.feature");
    }
}
