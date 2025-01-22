package com.vikavika209.catshow;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/main/resources",
        glue = "com.vikavika209.catshow",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true
)
public class RunCucumberTest {
}

