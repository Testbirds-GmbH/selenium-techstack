package com.testbirds.selenium.techstack;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.testbirds.selenium.techstack.util.JacksonHelper;
import com.testbirds.selenium.techstack.util.ReportPortalSelenideListener;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "json:target/cucumber-report.json", "com.testbirds.selenium.techstack.util.ReportPortalScenarioReporter" },
        features = "classpath:/feature/",
        glue = "com.testbirds.selenium.techstack.step",
        monochrome = true)
public class CucumberIT {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberIT.class);

    static {
        // bridge java.util.logging to SLF4J, so we use only one logger
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @BeforeClass
    public static void beforeClass() {
        SelenideLogger.addListener("reportportal", new ReportPortalSelenideListener());

        // adapt folder structure to Maven
        Configuration.reportsFolder = Paths.get("target", "selenide-reports").toString();

        // load extra capabilities from a json file
        final DesiredCapabilities extraCapabilitiess = new DesiredCapabilities();
        final File capabilitiesFile = new File("capabilities.json");
        if (capabilitiesFile.exists()) {
            try {
                LOG.info("Loading extra capabilities from file {}", capabilitiesFile);
                Map<String, Object> capabilities = JacksonHelper.getMapper().readValue(capabilitiesFile,
                        new TypeReference<Map<String, Object>>() {
                        });
                capabilities.forEach((capability, value) -> {
                    LOG.debug("Set extra capability {} to {}", capability, value);
                    extraCapabilitiess.setCapability(capability, value);
                });
            } catch (final IOException e) {
                LOG.error("Failed to load capabilities from file {}", capabilitiesFile, e);
            }
        } else {
            LOG.info("You can set extra capabilities in {}", capabilitiesFile);
        }
        Configuration.browserCapabilities = extraCapabilitiess;

        // set Configuration.fastSetValue to true on Internet Explorer
        if ("ie".equals(Configuration.browser)) {
            LOG.info("Enabling Configuration.fastSetValue for Internet Explorer");
            Configuration.fastSetValue = true;
        }

        // run in a maximized browser for better demonstration
        Configuration.startMaximized = true;

        // open browser and check that selenide.baseUrl is set properly
        LOG.info("Starting Selenide tests with base URL {} on browser {}", Configuration.baseUrl,
                Configuration.browser);
        open(Configuration.baseUrl);
        if (!title().contains("TodoMVC")) {
            throw new AssumptionViolatedException(
                    "The browser did not load the TodoMVC page - did you configure selenide.baseUrl?");
        }
    }
}
