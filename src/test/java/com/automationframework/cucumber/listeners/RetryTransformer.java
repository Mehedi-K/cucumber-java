package com.automationframework.cucumber.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Attaches {@link RetryCheckoutScenarios} to Cucumber's runScenario test, which
 * lives in a library class and so can't be annotated directly.
 */
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod != null && "runScenario".equals(testMethod.getName())) {
            annotation.setRetryAnalyzer(RetryCheckoutScenarios.class);
        }
    }
}
