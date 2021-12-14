package com.github.blokaly.reactiveweather.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
    glue = {"com.github.blokaly.reactiveweather.bdd.stepdefs"},
    plugin = {"pretty", "html:target/bdd_test_report.html", "junit:target/junit.xml"}, monochrome = true)
public class CucumberRunnerTest {

}
